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

/*
 * WorkerRunnable.java
 * 7/11/17
 * Handles messaging between client and server (Iot Device and Virtual Service)
 * Ryan Goggins
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
    System.out.println("ENTERING FOR FIRST TIME");
    boolean running = true, running1 = true;
    ArrayList<String> messages = new ArrayList<>();
    try {
      while (running) {
        if (running) {

          //Create a writer and a reader to pass messages from client to server
          BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());

          //for reading message from user
          String data = "";
          if (br.ready()) {
            data = br.readLine();
            messages.add(data);
            System.out.println(data + " is echoed");
          }

          //for reading input text
          BufferedReader msgTaker = new BufferedReader(new InputStreamReader(System.in));
          long end=System.currentTimeMillis()+50;
          //non-blocking text input such that the user is given some time to enter a message
          String message = "";
          while((System.currentTimeMillis()<end)) {
            if (msgTaker.ready())
            message += msgTaker.readLine();
          }

          //Message handling
          if (message.equals("")) {
            System.out.println("No input");
          }
          else {
            System.out.println("The message is " + message);
          }
          pw.println(message);
          messages.add(message);
          pw.flush();
        }
      }
      clientSocket.close();
    } catch (IOException ioe) {
      System.out.println("Client disconnected");
    }
    System.out.println("got here");
  }
}
