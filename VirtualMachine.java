/*
  VirtualMachine.java
  Contains the individual thread running for each IoT device
*/

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import javax.net.ssl.SSLSocket;

public class VirtualMachine extends Thread {
  private String filename, server;
  private int serverPort;
  private Socket socket;
  private SSLSocket sslSocket;
  private volatile IoTDevice device;
  private boolean isUsingSSl = false;
  private String className;
  private Logger logger;

  public VirtualMachine(Socket ss, Logger l, IoTDevice d, String cn){ //Initializes an simple unencrypted connection
    super();

    filename = f;
    server = d.getServerIP();
    serverPort = d.getServerPort();
    socket = ss;
    device = d;
    className = cn;
    logger = l;
  }

  public VirtualMachine(SSLSocket ss, Logger l, IoTDevice d, String cn){ //Initializes an SSL connection with a client that has SSL enabled
    super();

    filename = f;
    server = d.getServerIP();
    serverPort = d.getServerPort();
    sslSocket = ss;
    device = d;
    isUsingSSl = true;
    className = cn;
    logger = l;
  }

  public void setDevice(IoTDevice d){
    device = d;
  }

  public String getClassName(){
    return className;
  }

  public IoTDevice getDevice(){
    return device;
  }

  public void run(){ //Overwrites run
    Receiver receiver = new Receiver(server, serverPort);
    logger.println("Connected to " + server + " on port " + serverPort);

    Sender sender;

    if(isUsingSSl) sender = new Sender(sslSocket);
    else sender = new Sender(socket);

    String message = "";

    while(true){ //Receive and log the messages sent by client
      receiver.sendMessage("test");

      message = receiver.getMessage();
      System.out.println("Got message \"" + message + "\"");

      message = device.filterMessage(message);

      sender.sendMessage(message);
      System.out.println("Sent message \"" + message + "\" to IoT device");

      try{
        Thread.sleep(10);
      }
      catch(Exception e){
        logger.println("Got Exception " + e.toString() + " in VirtualMachine");
        e.printStackTrace();
      }
    }

  }
}
