/*
  Receiver.java
  Establishes connection between virtual service and server
  Accepts messages from the server and is capable of sending messages back
*/

import java.io.*;
import javax.net.ssl.SSLSocket;
import java.net.UnknownHostException;//essential import statements

public class Receiver{
  private SSLSocket sslSocket = null;//no value given to sslSocket yet
  private SSLClientSocket mSSLClientSocket;//mSSLClientSocket is a type of SSLClientSocket
  private BufferedReader br;//reads text from an input stream
  private PrintWriter pw;//allows various variables to be written in string format

  public Receiver(String server, int port){ //Constructor. Opens up SSL socket
    mSSLClientSocket = new SSLClientSocket(server, port);//mSSLClientSocket has a server and a port

    if(mSSLClientSocket.checkAndAddCertificates()) sslSocket = mSSLClientSocket.getSSLSocket();
    else return;

    try{
      sslSocket.setSoTimeout(1000);
      br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream())); // initializations
      pw = new PrintWriter(sslSocket.getOutputStream());
    }
    catch(Exception e){
      e.printStackTrace();
    }

    System.out.println("\033[1m\033[32mSocket successfully set up\033[0m");
  }

  public void sendMessage(String message){ //sends message to server
    try{
      pw.println(message);
      if(message.length() > 0) System.out.println(message);
      pw.flush();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public String getMessage(){ //receives message from server
    String message = "";

    try{
      message = br.readLine().trim();
      return message;
    }
    catch(Exception e){
      return "";
    }
  }

  public boolean isSocketClosed(){
    return pw.checkError();
  }

}
