/*
  VirtualService.java
  Main class holding all the code for the virtual service. Compile and run THIS file.
*/

import java.net.InetAddress;

public class VirtualService{
  public static void main(String[] args) {
    try{
      System.out.println("\033[1mIP Address: " + InetAddress.getLocalHost().getHostAddress() + "\033[0m");
    }
    catch(Exception e){
      e.printStackTrace();
    }

    Acceptor acceptor = new Acceptor();//Acceptor constructor
    acceptor.start();//start method is called
    System.out.println("Started TLS acceptor on port \033[1m2000\033[0m");

    AcceptorSSL acceptorSSL = new AcceptorSSL();//AcceptorSSL constructor
    acceptorSSL.start();//start method called agaain
    System.out.println("Started SSL acceptor on port \033[1m6000\033[0m");

    PluginFileTransfer fileTransferer = new PluginFileTransfer();
    fileTransferer.start();
    System.out.println("Started Plugin Acceptor on port \033[1m9000\033[0m");
  }
}
