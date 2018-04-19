package cn.alias.framework.sapjco.handler;

import com.sap.conn.jco.server.JCoServer;
import com.sap.conn.jco.server.JCoServerState;
import com.sap.conn.jco.server.JCoServerStateChangedListener;

public  class RFcStateChangedListener implements JCoServerStateChangedListener
{
    public void serverStateChangeOccurred(JCoServer server, JCoServerState oldState, JCoServerState newState)
    {
        
        // Defined states are: STARTED, DEAD, ALIVE, STOPPED;
        // see JCoServerState class for details.
        // Details for connections managed by a server instance
        // are available via JCoServerMonitor
        //System.out.println("Server state changed from " + oldState.toString() + " to " + newState.toString() + " on server with program id "
          //      + server.getProgramID());
    }
}
