/*
  VirtualMachine.java
  Contains the individual thread running for each IoT device
*/

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class VirtualMachine extends Thread {
  private String filename, server;
  private int serverPort;
  private ServerSocket serverSocket;

  public VirtualMachine(String s, int sP, ServerSocket ss, String f){
    super();

    filename = f;
    server = s;
    serverPort = sP;
    serverSocket = ss;
  }

  public void run(){
    Logger logger = new Logger("logs/" + filename);
    Receiver receiver = new Receiver(server, serverPort);
    logger.println("Connected to " + server + " on port " + serverPort);

    Sender sender = new Sender(serverSocket);

    String message = "";

    while(true){
      receiver.sendMessage("test");

      message = receiver.getMessage();
      logger.println("Got message \"" + message + "\"");

      sender.sendMessage(message);
      logger.println("Sent message \"" + message + "\" to IoT device");

      try{
        Thread.sleep(10);
      }
      catch(Exception e){
        logger.close();
        e.printStackTrace();
      }
    }

  }
}
