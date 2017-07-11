/*
  VirtualService.java
  Main class holding all the code for the virtual service. Compile and run THIS file.
*/

public class VirtualService{
  public static void main(String[] args) {
    String serverIP = args[0], filename = args[3];
    int serverPort = Integer.parseInt(args[1]), port = Integer.parseInt(args[2]);
    VirtualMachine virtualMachine = new VirtualMachine(serverIP, serverPort, port, filename);
    virtualMachine.start();
  }
}
