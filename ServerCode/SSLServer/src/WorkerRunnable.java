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

public class WorkerRunnable implements Runnable{//workerrunnable is an interface that implements the runnable class

  protected Socket clientSocket;// client socket is the socket (protected variable)
  protected String address;//address is a protected string variable

  /*
   * Constructor takes in a socket and a message (unused)
   */
  public WorkerRunnable(Socket clientSocket, String address) {//workerrunnable has 2 parameters: one is the socket and other is address
    this.clientSocket = clientSocket;
    this.address = address;
  }

  public void run() {//run method is called

    //Create message acceptor thread
    MessageAcceptor ma = new MessageAcceptor(clientSocket);//ma is a message acceptor and it takes in the clientSocket parameter
    new Thread(ma).start();//ma is a new thread and calls on the start method

    boolean running = true;//running is a true boolean
    try {
      while (running) {//try that while running is going, the pw will be a new socket from the output message
        if (running) {

          //Create a writer to pass messages from client to server
          PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());

          //for reading input text
          BufferedReader msgTaker = new BufferedReader(new InputStreamReader(System.in));//msgTaker reads through the input stream
          System.out.println("");//print out a blank statement
          long end=System.currentTimeMillis()+1500;//end prints out the time +1500
          //non-blocking text input such that the user is given some time to enter a message
          String message = "";
          while((System.currentTimeMillis()<end)) {//while the time is less than the end
            if (msgTaker.ready())//if the buffered reader calls on the ready method
            message += msgTaker.readLine();//concatinate the following
          }

          //Message handling - non blocking
          if (message.equals("")) {//if the message is still blank
            System.out.println("No input on socket: " + clientSocket.getLocalAddress().toString());
          }//the following will print out
          else {
            System.out.println("The message is " + message + " on socket " + clientSocket.getLocalAddress().toString());
          }
          pw.println(message);

          //Check if client disconnected
          if (pw.checkError()) {//if the pw calls on the error message
            running = false;//running is false
            System.out.println("Cliented disconnected");
          }

          pw.flush();//flush out the print writer
        }
      }
      clientSocket.close();//close connection with the client socket
    } catch (IOException ioe) {//catch the following exception
      ioe.printStackTrace();//print stack trace
      System.out.println("Error connecting");//error messge will pop up
    }
  }
}
