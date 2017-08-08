import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.lang.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.net.SocketAddress;
import java.nio.channels.*;
import java.nio.*;
import java.net.*;

public class MessageAcceptor implements Runnable {//interface which implements the runnable class
  protected Socket clientSocket;//client socket is an socek
  private BufferedReader br;//initialize br as bufferedreader

  public MessageAcceptor(Socket sock) {//message acceptor function has sock parameter
    clientSocket = sock;//cleintSocket is initialized as sock
    System.out.println("MessageAcceptor initialized");//print statement
  }



  public void run() {//run method is called
    try {
      br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//tries to get the input stream from the cs
    } catch (Exception e) {//exception e is throw
      System.out.println("error creating BufferedReader");//cannot make br
    }
    while (true) {//if the run method is true
      System.out.println("Waiting to receive new message");//message will print out
      try {//try to initalize data as null
        String data = "";
        if (true) {//if the data is null
          data = br.readLine();//data will have the value after the br reads the line
          if (data == null) {//if the data is still null, then end the loop
            break;
          }
          System.out.println("The value of data from socket " + clientSocket.getRemoteSocketAddress().toString() + " is: " + data);
        }//will print out the data from the client socket and the br
      } catch (Exception e) {//exception e is thrown and willl print the stackTrace
        e.printStackTrace();
        System.out.println("ERROR ON MESSAGE ACCEPTOR");
      }
    }
    System.out.println("Message Acceptor disconnected");//the server will disconect
  }
}
