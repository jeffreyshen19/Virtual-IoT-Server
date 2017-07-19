/*
  Sender.java
  Establishes connection between virtual service and IoT devices
*/

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import javax.net.ssl.SSLSocket;

public class Sender{
  private ServerSocket serverSocket;
  private Socket clientSocket;
  private PrintWriter out;
  private BufferedReader in;

  public Sender(Socket ss){ //Port to create server on
    try{
      clientSocket = ss;
      clientSocket.setSoTimeout(1000);
      out = new PrintWriter(clientSocket.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    catch(Exception e){
      e.printStackTrace();
    }

    System.out.println("\033[1m\033[32mSender socket successfully set up\033[0m");
  }

  public Sender(SSLSocket ss){ //Port to create server on
    try{
      out = new PrintWriter(ss.getOutputStream(), true);
      in = new BufferedReader(new InputStreamReader(ss.getInputStream()));
    }
    catch(Exception e){
      e.printStackTrace();
    }

    System.out.println("\033[1m\033[32mSender socket successfully set up\033[0m");
  }

  public void sendMessage(String message){ //sends a message to the client
    try{
      out.println(message);
      if(message.length() > 0) System.out.println(message);
      out.flush();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public String getMessage(){ //takes input from client
    String message = "";

    try{
      message = in.readLine().trim();
      return message;
    }
    catch(Exception e){
      return "";
    }
  }

  public boolean isSocketClosed(){
    return out.checkError();
  }

}
