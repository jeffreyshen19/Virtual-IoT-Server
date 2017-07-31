/*
Acceptor.java
Superclass for acceptors
*/

import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.ArrayList;
import Plugins.*;

public class Acceptor extends Thread {//acceptor class extends the thread class
  private ArrayList<VirtualMachine> machines;//intitalizing array list and variables
  private int port;
  private boolean usesSSL;
  private Logger logger;//logger allows you to configure the messages that are written

  public Acceptor(ArrayList<VirtualMachine> m, int p, boolean uS, Logger l){
    super();
    machines = m;
    port = p;
    usesSSL = uS;//more variable inititalizing 
    logger = l;
  }

  public void run(){ //Overwrites run
    PrintWriter out = null;
    BufferedReader in = null;
    String line = "";
    ServerSocket serverSocket = null;
    SSLServerSocket sslServerSocket = null;
    SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

    while(true){ //Accepts client connection and establishes server connection with multithreading capability.

      Socket clientSocket = null;
      Socket sslSocket = null;

      try{
        if(usesSSL){
          serverSocket = (SSLServerSocket) factory.createServerSocket(port);
          sslSocket = (SSLSocket) serverSocket.accept();
          out = new PrintWriter(sslSocket.getOutputStream(), true);
          in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
        }
        else {
          serverSocket = new ServerSocket(port);
          clientSocket = serverSocket.accept();
          out = new PrintWriter(clientSocket.getOutputStream(), true);
          in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }

        logger.println("Connected a new device on port " + port);

        line = in.readLine();
        System.out.println(line);
      }
      catch(Exception e){
        e.printStackTrace();
      }

      if(line.indexOf(":") != -1){ //parses the input from the client
        String serverIP = line.split(":")[0]; //IP of server
        int serverPort;
        String className = "";
        if(line.indexOf("|") != -1){
          serverPort = Integer.parseInt(line.split(":")[1].split("\\|")[0]); //Port of Server
          className = line.split("\\|")[1]; //name of the class
        }
        else{
          serverPort = Integer.parseInt(line.split(":")[1]);
        }

        Class clazz;
        IoTDevice device = null;

        System.out.println(className);

        try{ //Calls the constructor of the class
          if(className.length() > 0) {
            clazz = Class.forName(className);

            Class[] cArg = new Class[2];
            cArg[0] = int.class;
            cArg[1] = String.class;



            device = (IoTDevice) clazz.getDeclaredConstructor(cArg).newInstance(serverPort, serverIP);
          }
          else device = new IoTDevice(serverPort, serverIP);
        }
        catch(Exception e){
          e.printStackTrace();
        }

        VirtualMachine virtualMachine;
        if(usesSSL) virtualMachine = new VirtualMachine(sslSocket,logger, device, className);
        else virtualMachine = new VirtualMachine(clientSocket, logger, device, className);

        machines.add(virtualMachine);
        virtualMachine.start(); //sets up a new thread
      }

      boolean portAvailable = false;

      while(!portAvailable){ //Make sure port actually works
        port++;
        portAvailable = true;

        try{
          new ServerSocket(port).close();
        }
        catch(IOException e){
          portAvailable = false;
        }
      }

      logger.println("Now accepting new connections on port " + port);

      try{
        Thread.sleep(10);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }

  }
}
