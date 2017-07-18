/*
  VirtualService.java
  Main class holding all the code for the virtual service. Compile and run THIS file.
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

    ArrayList<VirtualMachine> machines = new ArrayList<VirtualMachine>();
    Logger logger = new Logger("logs/" + args[0]);

    Acceptor acceptorTLS = new Acceptor(machines, 2000, false, logger);//Acceptor constructor
    acceptorTLS.start();//start method is called
    logger.println("Started TLS acceptor on port 2000");

    Acceptor acceptorSSL = new Acceptor(machines, 6000, true, logger);//AcceptorSSL constructor
    acceptorSSL.start();//start method called agaain
    logger.println("Started SSL acceptor on port 6000");

    PluginFileTransfer fileTransferer = new PluginFileTransfer(machines);
    fileTransferer.start();
    logger.println("Started Plugin Acceptor on port 9000");
  }
}
