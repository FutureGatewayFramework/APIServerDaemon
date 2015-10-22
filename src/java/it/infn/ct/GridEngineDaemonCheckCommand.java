/**************************************************************************
Copyright (c) 2011:
Istituto Nazionale di Fisica Nucleare (INFN), Italy
Consorzio COMETA (COMETA), Italy

See http://www.infn.it and and http://www.consorzio-cometa.it for details on
the copyright holders.

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

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Runnable class responsible to check GridEngineDaemon commands
 * This class mainly checks for commands consistency and it does not 
 * handle directly GridEngine API calls but rather uses GridEngineInterface 
 * class instances
 * The use of an interface class may help targeting other command 
 * executor services if needed
 * @author <a href="mailto:riccardo.bruno@ct.infn.it">Riccardo Bruno</a>(INFN)
 * @see GridEngineInterface
 */
class GridEngineDaemonCheckCommand implements Runnable {        
    
    private GridEngineDaemonCommand gedCommand;    
    private String gedConnectionURL;      
    
    /*
      GridEngineDaemon config
    */
    GridEngineDaemonConfig gedConfig;        
    
    /**
     * Supported commands
     */
    private enum Commands {
         SUBMIT     
        ,GETSTATUS // This command is directly handled by GE API Server
        ,GETOUTPUT // This command is directly handled by GE API Server
        ,JOBCANCEL
    }

    /*
      Logger
    */
    private static final Logger _log = Logger.getLogger(GridEngineDaemonLogger.class.getName());
    
    public static final String LS = System.getProperty("line.separator");
    
    String threadName;
    
    /**
     * Constructor that retrieves the command to execute and the 
     * GridEngineDaemon database connection URL, necessary to finalize 
     * executed commands
     * @param gedCommand
     * @param gedConnectionURL 
     */
    public GridEngineDaemonCheckCommand(GridEngineDaemonCommand gedCommand
                                         ,String gedConnectionURL) {
        this.gedCommand = gedCommand;
        this.gedConnectionURL = gedConnectionURL;
        threadName = Thread.currentThread().getName();
    }
    
    /**
     * Load GridEngineDaemon configuration settings
     * @param gedConfig GridEngineDaemon configuration object
     */
    public void setConfig(GridEngineDaemonConfig gedConfig) {
        // Save all configs
        this.gedConfig=gedConfig;                        
    }

    /**
     * Execution of the GridEngineCommand
     */
    @Override
    public void run() {
        _log.info("Checking command: "+gedCommand);
        
        switch (Commands.valueOf(gedCommand.getAction())) {
            case SUBMIT:    submit();
                break;
            case GETSTATUS: getStatus();
                break;
            case GETOUTPUT: getOutput();
                break;
            case JOBCANCEL: jobCancel();
                break;
            default:
                _log.warning("Unsupported command: '"+gedCommand.getAction()+"'");
                break;
        }                
    }
    
    /*
      Commands implementations
    */
    
    /**
     * Execute a GridEngineDaemon 'submit' command
     */
    private void submit() {
        // Finalize commands PROCESSED and GESTATUS == DONE
        _log.info("Checking submitted command: "+gedCommand);        
    }
    
    /**
     * Execute a GridEngineDaemon 'status' command
     * Asynchronous GETSTATUS commands should never come here
     */
    private void getStatus() {
        _log.info("Checkinig get status command: "+gedCommand);
    }
    
    /**
     * Execute a GridEngineDaemon 'get output' command
     * Asynchronous GETOUTPUT commands should never come here
     */
    private void getOutput() {
        _log.info("Check get output command: "+gedCommand);
    }
    
    /**
     * Execute a GridEngineDaemon 'job cancel' command
     */
    private void jobCancel() {
        _log.info("Check job cancel command: "+gedCommand);
    }
    
    /**
     * update the GridEngine command status
     */
    private void updateCommandStatus(String status) {
        GridEngineDaemonDB gedDB = null;
        
        try {
                gedDB= new GridEngineDaemonDB(gedConnectionURL);
                gedDB.updateCommand(gedCommand);                                
            } catch (Exception e) {
                _log.severe("Unable release command:"+LS+gedCommand
                                                     +LS+e.toString());
            }
            finally {
               if(gedDB!=null) gedDB.close(); 
            }
    }
}