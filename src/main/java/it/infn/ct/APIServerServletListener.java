/**
 * ************************************************************************ Copyright (c) 2011:
 * Istituto Nazionale di Fisica Nucleare (INFN), Italy Consorzio COMETA (COMETA), Italy
 *
 * <p>See http://www.infn.it and and http://www.consorzio-cometa.it for details on the copyright
 * holders.
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author <a href="mailto:riccardo.bruno@ct.infn.it">Riccardo Bruno</a>(INFN)
 *     **************************************************************************
 */
package it.infn.ct;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Servlet context listener object used to instantiate the APIServerDaemon class and initalize the
 * daemon ThreadPool and its polling thread.
 *
 * @author <a href="mailto:riccardo.bruno@ct.infn.it">Riccardo Bruno</a>(INFN)
 * @see APIServerDaemon
 */
public class APIServerServletListener implements ServletContextListener {
  /** APIServer daemin class instance. */
  private APIServerDaemon asDaemon = null;

  private static void initalizeLog4J(final String apiSrvDaemonPath) {
    final File apiSrvDaemon = new File(apiSrvDaemonPath);
    final File webInf = new File(apiSrvDaemon, "WEB-INF");
    final File log4j = new File(webInf, "log4j.properties");

    if (log4j.exists()) {
      System.out.println("Initializing log4j with: " + log4j.getAbsolutePath());
      PropertyConfigurator.configure(log4j.getAbsolutePath());
    } else {
      System.err.println(
          "WARNING: '"
              + log4j.getAbsolutePath()
              + " 'file not found, so initializing log4j with BasicConfigurator");
      BasicConfigurator.configure();
    }
  }

  private static void tryInitialContext(final String pool) throws NamingException {
    Context initContext = null;
    Context envContext = null;

    try {
      initContext = new InitialContext();
      envContext = (Context) initContext.lookup("java:comp/env");

      final DataSource dataSource = (DataSource) envContext.lookup(pool);
      APIServerServletListener.tryGetConnection(dataSource, pool);
    } catch (final SQLException | NamingException e) {
      System.err.println("Failed to get connection from connection pool: " + pool);
      e.printStackTrace(System.err);
    } finally {
      if (initContext != null) {
        initContext.close();
      }
    }
  }

  private static void tryGetConnection(final DataSource dataSource, final String pool)
      throws SQLException {
    try (final Connection ignored = dataSource.getConnection()) {
      System.err.println("Successfully got connection from connection pool: " + pool);
    }
  }

  private static void tryConnectionPool(final String pool) {
    try {
      APIServerServletListener.tryInitialContext(pool);
    } catch (final NamingException e) {
      System.err.println("Failed to try connection pool: " + pool);
      e.printStackTrace(System.err);
    }
  }

  /**
   * Called while destroying servlet context.
   *
   * @param servletContextEvent - Servlet context event
   * @see APIServerDaemon
   */
  @Override
  public final void contextDestroyed(final ServletContextEvent servletContextEvent) {
    if (asDaemon != null) {
      asDaemon.shutdown();
    }

    // Unregister MySQL driver
    APIServerDaemonDB.unregisterDriver();

    // Notify termination
    System.out.println("--- APIServerDaemon Stopped ---");
  }

  /**
   * Called during servlet context initalization, it instantiate the APIServerDaemon class.
   *
   * @param servletContextEvent - Servlet context event object
   * @see APIServerDaemon
   */
  @Override
  public final void contextInitialized(final ServletContextEvent servletContextEvent) {
    final ServletContext context = servletContextEvent.getServletContext();
    final String apiSrvDaemonPath = context.getRealPath("/");

    System.setProperty("APISrvDaemonPath", apiSrvDaemonPath);
    System.setProperty("APISrvDaemonVersion", "v.0.0.2-22-g2338f25-2338f25-40");

    // Notify execution
    System.out.println("--- Starting APIServerDaemon v.0.0.2-22-g2338f25-2338f25-40 ---");
    System.out.println("Java vendor : '" + System.getProperty("java.vendor") + "'");
    System.out.println("Java vertion: '" + System.getProperty("java.version") + "'");
    System.out.println("Running as  : '" + System.getProperty("user.name") + "' username");
    System.out.println("Servlet path: '" + apiSrvDaemonPath + "'");

    // Initialize log4j logging
    APIServerServletListener.initalizeLog4J(apiSrvDaemonPath);

    // Try the connection pool
    APIServerServletListener.tryConnectionPool("jdbc/fgApiServerPool");

    // Register MySQL driver
    APIServerDaemonDB.registerDriver();

    // Initializing the daemon
    if (asDaemon == null) {
      asDaemon = new APIServerDaemon();
    }

    asDaemon.startup();
  }
}
