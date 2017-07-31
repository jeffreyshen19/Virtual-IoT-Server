/*
  VirtualService.java
  Main class handling all the code for the virtual service through threads. Compile and run THIS file.
*/

import java.net.InetAddress;
import java.util.ArrayList;

public class VirtualService{
  public static void main(String[] args) {
    try{
      System.out.println("\033[1mIP Address: " + InetAddress.getLocalHost().getHostAddress() + "\033[0m");
    }
    catch(Exception e){
      e.printStackTrace();
    }

    ArrayList<VirtualMachine> machines = new ArrayList<VirtualMachine>();//initializing array list
    Logger logger = new Logger("logs/" + args[0]);//logger constructor

    Acceptor acceptorTLS = new Acceptor(machines, 2000, false, logger);//Acceptor constructor
    acceptorTLS.start();//start method is called
    logger.println("Started TLS acceptor on port 2000");

    Acceptor acceptorSSL = new Acceptor(machines, 6000, true, logger);//AcceptorSSL constructor
    acceptorSSL.start();//start method called again
    logger.println("Started SSL acceptor on port 6000");

    PluginFileTransfer fileTransferer = new PluginFileTransfer(machines);//PluginFileTransfer constructor
    fileTransferer.start();//start method called again
    logger.println("Started Plugin Acceptor on port 9000");
  }
}
