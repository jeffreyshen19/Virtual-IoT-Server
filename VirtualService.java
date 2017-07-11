/*
  VirtualService.java
  Main class holding all the code for the virtual service. Compile and run THIS file.
*/

public class VirtualService{
  public static void main(String[] args) {
    Receiver receiver = new Receiver(args[0], Integer.parseInt(args[1]));
    Logger logger = new Logger(args[0]);
  }
}
