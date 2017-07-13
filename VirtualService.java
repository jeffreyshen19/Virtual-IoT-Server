/*
  VirtualService.java
  Main class holding all the code for the virtual service. Compile and run THIS file.
*/

import java.net.InetAddress;

public class VirtualService{
  public static void main(String[] args) {
    try{
      System.out.println("ip address: " + InetAddress.getLocalHost().getHostAddress());
    }
    catch(Exception e){
      e.printStackTrace();
    }
    Acceptor acceptor = new Acceptor();
    AcceptorSSL acceptorSSL = new AcceptorSSL();
    acceptor.start();
    System.out.println("Started acceptor on port 2000");
    acceptorSSL.start();
    System.out.println("Started acceptor SSL on port 6000");
  }
}
