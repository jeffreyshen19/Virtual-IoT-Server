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

  public VirtualMachine(Socket ss, String f, IoTDevice d, String cn){ //Initializes an simple unencrypted connection
    super();

    filename = f;
    server = d.getServerIP();
    serverPort = d.getServerPort();
    socket = ss;
    device = d;
    className = cn;
  }

  public VirtualMachine(SSLSocket ss, String f, IoTDevice d, String cn){ //Initializes an SSL connection with a client that has SSL enabled
    super();

    filename = f;
    server = d.getServerIP();
    serverPort = d.getServerPort();
    sslSocket = ss;
    device = d;
    isUsingSSl = true;
    className = cn;
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
    Logger logger = new Logger("logs/" + filename);
    Receiver receiver = new Receiver(server, serverPort);
    logger.println("Connected to " + server + " on port " + serverPort);

    Sender sender;

    if(isUsingSSl) sender = new Sender(sslSocket);
    else sender = new Sender(socket);


    String message = "";

    while(true){ //Receive and log the messages sent by client
      receiver.sendMessage("test");

      message = receiver.getMessage();
      logger.println("Got message \"" + message + "\"");

      message = device.filterMessage(message);

      sender.sendMessage(message);
      logger.println("Sent message \"" + message + "\" to IoT device");
      logger.close();
      
      try{
        Thread.sleep(10);
      }
      catch(Exception e){
        
        e.printStackTrace();
      }
    }

  }
}
