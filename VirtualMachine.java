/*
  VirtualMachine.java
  Contains the individual thread running for each IoT device
*/

public class VirtualMachine extends Thread {
  private String filename, server;
  private int port, serverPort;

  public VirtualMachine(String s, int sP, int p, String f){
    super();
    
    filename = f;
    server = s;
    port = p;
    serverPort = sP;
  }

  public void run(){
    Logger logger = new Logger("logs/" + filename);
    Receiver receiver = new Receiver(server, serverPort);
    logger.println("Connected to " + server + " on port " + serverPort);

    Sender sender = new Sender(port);
    logger.println("Sender set up on port " + port);

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
