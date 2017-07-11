/*
  VirtualService.java
  Main class holding all the code for the virtual service. Compile and run THIS file.
*/

public class VirtualService{
  public static void main(String[] args) {
    Receiver receiver = new Receiver(args[0], Integer.parseInt(args[1]));

    System.out.println("Receiver set up");

    receiver.sendMessage("test");
    String message = receiver.getMessage();

    System.out.println("Got message");

    //Logger logger = new Logger(args[0]);
    try{
      Thread.sleep(5000);
    }
    catch(Exception e){

    }


    Sender sender = new Sender(Integer.parseInt(args[2]));

    System.out.println("Set up sender");

    sender.sendMessage(message);

    System.out.println("Sent message");
  }
}
