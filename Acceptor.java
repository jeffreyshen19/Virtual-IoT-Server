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

public class Acceptor extends Thread {
  private ArrayList<VirtualMachine> machines;
  private int port;
  private boolean usesSSL;

  public Acceptor(ArrayList<VirtualMachine> m, int p, boolean uS){
    super();
    machines = m;
    port = p;
    usesSSL = uS;
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
        if(line.indexOf("\\|") != -1){
          serverPort = Integer.parseInt(line.split(":")[1].split("\\|")[0]); //Port of Server
        }
        else{
          serverPort = Integer.parseInt(line.split(":")[1]);
          className = line.split("\\|")[1]; //name of the class
        }

        Class clazz;
        IoTDevice device = null;

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
        if(usesSSL) virtualMachine = new VirtualMachine(sslSocket, "test.txt", device, className);
        else virtualMachine = new VirtualMachine(clientSocket, "test.txt", device, className);

        machines.add(virtualMachine);
        virtualMachine.start(); //sets up a new thread
      }

      port++;
      System.out.println("Now accepting new connections on port " + port);

      try{
        Thread.sleep(10);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }

  }
}
