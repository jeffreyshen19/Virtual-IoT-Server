import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.net.SocketAddress;

/*
 * WorkerRunnable.java
 * 7/11/17
 * Handles messaging between client and server (Iot Device and Virtual Service)
 * Ryan Goggins
 * Adapted from: http://tutorials.jenkov.com/java-multithreaded-servers/thread-pooled-server.html
 */

public class WorkerRunnable implements Runnable{

  protected Socket clientSocket;
  protected String address;

  /*
   * Constructor takes in a socket and a message (unused)
   */
  public WorkerRunnable(Socket clientSocket, String address) {
    this.clientSocket = clientSocket;
    this.address = address;
  }

  public void run() {

    //Create message acceptor thread
    MessageAcceptor ma = new MessageAcceptor(clientSocket);
    new Thread(ma).start();

    boolean running = true;
    try {
      while (running) {
        if (running) {

          //Create a writer to pass messages from client to server
          PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());

          //for reading input text
          BufferedReader msgTaker = new BufferedReader(new InputStreamReader(System.in));
          System.out.println("");
          long end=System.currentTimeMillis()+1500;
          //non-blocking text input such that the user is given some time to enter a message
          String message = "";
          while((System.currentTimeMillis()<end)) {
            if (msgTaker.ready())
            message += msgTaker.readLine();
          }

          //Message handling - non blocking
          if (message.equals("")) {
            System.out.println("No input on socket: " + clientSocket.getLocalAddress().toString());
          }
          else {
            System.out.println("The message is " + message + " on socket " + clientSocket.getLocalAddress().toString());
          }
          pw.println(message);

          //Check if client disconnected
          if (pw.checkError()) {
            running = false;
            System.out.println("Cliented disconnected");
          }

          pw.flush();
        }
      }
      clientSocket.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      System.out.println("Error connecting");
    }
  }
}
