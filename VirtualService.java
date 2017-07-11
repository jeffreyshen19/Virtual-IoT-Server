/*
  VirtualService.java
  Main class holding all the code for the virtual service. Compile and run THIS file.
*/

public class VirtualService{
  public static void main(String[] args) {
    System.out.println("ip address: " + InetAddress.getLocalHost().getHostAddress());
    Acceptor acceptor = new Acceptor();
    acceptor.start();
  }
}
