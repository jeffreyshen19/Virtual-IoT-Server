/*
  Acceptor.java
  Contains the thread that monitors for new connection
*/

import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;
import java.io.*;
import java.util.Arrays;
import java.lang.reflect.*;
import Plugins.*;

public class Acceptor extends Thread {

  public void run(){ //Overwrites run

    PrintWriter out = null;
    BufferedReader in = null;

    int port = 2000;

    String line = "";

    ServerSocket serverSocket = null;

    try{

    }
    catch(Exception e){

    }

    while(true){ //Accepts client connection and establishes server connection

      Socket clientSocket = null;
      try{
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      }
      catch(Exception e){
        e.printStackTrace();
      }

      try{
        line = in.readLine();
        System.out.println(line);
      }
      catch(Exception e){
        e.printStackTrace();
      }

      if(line.indexOf(":") != -1 && line.indexOf("|") != -1){ //parses the input from the client
        String serverIP = line.split(":")[0]; //IP of server
        int serverPort = Integer.parseInt(line.split(":")[1].split("\\|")[0]); //Port of Server
        String className = line.split("|")[1]; //name of the class

        Class clazz;
        IoTDevice device = null;

        try{ //Calls the constructor of the class
          clazz = Class.forName(className);

          Class[] cArg = new Class[2];
          cArg[0] = int.class;
          cArg[1] = String.class;

          device = (IoTDevice) clazz.getDeclaredConstructor(cArg).newInstance(serverPort, serverIP);
        }
        catch(Exception e){
          e.printStackTrace();
        }

        VirtualMachine virtualMachine = new VirtualMachine(clientSocket, "test.txt", device);
        virtualMachine.start();
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
