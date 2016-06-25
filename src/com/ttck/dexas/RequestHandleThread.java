package com.ttck.dexas;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandleThread implements Runnable {
    private static final int BUFSIZE = 1024*1024; // Size (in bytes) of I/O buffer
    private Socket clientSocket; // Socket connect to client
    private Logger logger; // Server logger
	  
    public RequestHandleThread(Socket clientSocket, Logger logger) {
    	this.clientSocket = clientSocket;
    	this.logger = logger;
    } 
    
    public static void handleClientRequest(Socket clientSocket, Logger logger) {
    	try {
    		// Get the input and output I/O streams from socket
    		InputStream in = clientSocket.getInputStream();
    		OutputStream out = clientSocket.getOutputStream();
    		int recvMsgSize; // Size of received message 
    		int totalBytesEchoed = 0; // Bytes received from client
    		byte[] inputBuffer = new byte[BUFSIZE];
    		byte[] outputBuffer = new byte[BUFSIZE]; // Receive Buffer 
    		// Receive until client closes connection, indicated by -1 
    		while ((recvMsgSize = in.read(inputBuffer)) != -1) {
    			//TODO - Need to handle the request to fill-in outputBuffer here.
    			System.arraycopy(inputBuffer, 0, outputBuffer, 0, recvMsgSize);
    			out.write(outputBuffer, 0, recvMsgSize);
    			totalBytesEchoed += recvMsgSize;
    		}
    		logger.info("Client " + clientSocket.getRemoteSocketAddress() + 
    				", echoed " + totalBytesEchoed + " bytes.");
    	} catch (IOException ex) {
    		logger.log(Level.WARNING, "Exception in echo protocol", ex);
	    }
    	finally{
    		try {
    			clientSocket.close();
    		}catch (IOException e) {
    			logger.log(Level.SEVERE, "Faied to close client Socket.", e);
    		}
    	}
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		handleClientRequest(this.clientSocket, this.logger);
	}

}
