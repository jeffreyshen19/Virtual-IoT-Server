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
    acceptor.start();
  }
}
