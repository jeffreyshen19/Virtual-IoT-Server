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

public class MessageAcceptor implements Runnable {
  protected Socket clientSocket;
  private BufferedReader br;

  public MessageAcceptor(Socket sock) {
    clientSocket = sock;
    System.out.println("MessageAcceptor initialized");
  }



  public void run() {
    try {
      br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (Exception e) {
      System.out.println("error creating BufferedReader");
    }
    while (true) {
      System.out.println("Waiting to receive new message");
      try {
        String data = "";
        if (true) {
          data = br.readLine();
          if (data == null) {
            break;
          }
          System.out.println("The value of data from socket " + clientSocket.getRemoteSocketAddress().toString() + " is: " + data);
        }
      } catch (Exception e) {
        e.printStackTrace();
        System.out.println("ERROR ON MESSAGE ACCEPTOR");
      }
    }
    System.out.println("Message Acceptor disconnected");
  }
}
