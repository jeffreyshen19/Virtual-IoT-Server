import java.io.*;
import javax.net.ssl.SSLSocket;
import java.net.UnknownHostException;

public class Receiver{

  public String connect (String server, int port){
    String message = "";
    SSLSocket sslSocket = null;
    SSLClientSocket mSSLClientSocket = new SSLClientSocket(server, port);
    if(mSSLClientSocket.checkAndAddCertificates()) {
      sslSocket = mSSLClientSocket.getSSLSocket();
    }
    else {
      return;
    }

    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
      PrintWriter pw = new PrintWriter(sslSocket.getOutputStream());
      pw.println("Initiating connection from the client");
      System.out.println("\033[1m\033[32mSuccessfully connected to secure server\033[0m");
      pw.flush();
      br.readLine();

      while(true){
        message = br.readLine().trim();
        if(message.length() > 0){
          System.out.println(message); 
          pw.println("Client received the command");
          break; 
        }
        Thread.sleep(1000);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return message;
  }
}
