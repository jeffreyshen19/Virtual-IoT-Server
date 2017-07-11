/*
  VirtualService.java
  Main class holding all the code for the virtual service. Compile and run THIS file.
*/

public class VirtualService{
  public static void main(String[] args) {
    Logger logger = new Logger(args[3]);

    Receiver receiver = new Receiver(args[0], Integer.parseInt(args[1]));

    logger.println("Connected to " + args[0] " on port " + args[1]);

    receiver.sendMessage("test");
    String message = receiver.getMessage();

    logger.println("Got message \"" + message + "\"");

    try{
      Thread.sleep(5000);
    }
    catch(Exception e){

    }


    Sender sender = new Sender(Integer.parseInt(args[2]));

    logger.println("Sender set up on port " + args[2]);

    sender.sendMessage(message);

    System.out.println("Sent message \"" + message "\" to IoT device");

    logger.close();
  }
}
