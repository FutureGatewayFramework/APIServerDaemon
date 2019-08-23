/**************************************************************************
Copyright (c) 2011:
Istituto Nazionale di Fisica Nucleare (INFN), Italy

See http://www.infn.it for details on the copyright holders.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

@author <a href="mailto:riccardo.bruno@ct.infn.it">Riccardo Bruno</a>(INFN)
****************************************************************************/
package it.infn.ct;

import java.io.File;
import static java.lang.Thread.sleep;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

/**
 * APIServerDaemon Loop.
 *
 * @author <a href="mailto:riccardo.bruno@ct.infn.it">Riccardo Bruno</a>(INFN)
 * @see APIServerDaemon
 */
public class APIServerDaemonLoop implements Runnable {
    /**
     * Line separator constant.
     */
    public static final String LS = System.getProperty("line.separator");
     /**
      * File path separator.
      */
    private static final String PS = System.getProperty("file.separator");
    /**
     * User name.
     */
    private static final String US = System.getProperty("user.name");
    /**
     * Java runtime vendor.
     */
    private static final String VN = System.getProperty("java.vendor");
    /**
     * Java runtime version.
     */
    private static final String VR = System.getProperty("java.version");
    /**
     * APIServer daemin class instance.
     */
    private APIServerDaemon asDaemon = null;

    /**
     *
     */
    @Override
    public final void run() {

        String apiSrvDaemonPath = PS;
        System.setProperty("APISrvDaemonPath", apiSrvDaemonPath);
        System.setProperty("APISrvDaemonVersion",
                           "v.0.0.2-22-g2338f25-2338f25-40");

        // Notify execution
        System.out.println("--- " + "Starting APIServerDaemon "
                + System.getProperty("APISrvDaemonVersion") + " ---");
        System.out.println("Java vendor : '" + VN + "'");
        System.out.println("Java vertion: '" + VR + "'");
        System.out.println("Running as  : '" + US + "' username");
        System.out.println("Servlet path: '" + apiSrvDaemonPath + "'");

        // Initialize log4j logging
        String log4jPropPath =
                apiSrvDaemonPath + "log4j.properties";
        File log4PropFile = new File(log4jPropPath);

        if (log4PropFile.exists()) {
            System.out.println("Initializing log4j with: " + log4jPropPath);
            PropertyConfigurator.configure(log4jPropPath);
        } else {
            System.err.println(
                    "WARNING: '" + log4jPropPath
                  + " 'file not found, so initializing log4j "
                  + "with BasicConfigurator");
            BasicConfigurator.configure();
        }

        // Register MySQL driver
        APIServerDaemonDB.registerDriver();

        // Initializing the daemon
        if (asDaemon == null) {
            asDaemon = new APIServerDaemon();
        }

        asDaemon.startup();

        final int loopDealy = 60000;

        try {
            // use file flag instead of true
            while (true) {
                sleep(loopDealy);
            }
        } catch (InterruptedException e) {

        }

        // Cleanly release resources
        if (asDaemon != null) {
            asDaemon.shutdown();
        }

        // Unregister MySQL driver
        APIServerDaemonDB.unregisterDriver();

        // Notify termination
        System.out.println("--- APIServerDaemon Stopped ---");
    }
}
