/*
Code accepts a file and server ip and port. Sends file to server.
*/
import javax.net.ssl.SSLSocket;
import java.io.*;

public class FileSender{
  public static void main(String[] args) {
    //SSL
    SSLSocket sslSocket = null;
    SSLClientSocket mSSLClientSocket = new SSLClientSocket(args[0], Integer.parseInt(args[1]));
    if(mSSLClientSocket.checkAndAddCertificates()) {
      sslSocket = mSSLClientSocket.getSSLSocket();
    }
    else {
      return;
    }
    try {
      File file = new File(args[2]);
      InputStream in = new FileInputStream(file);
      OutputStream out = sslSocket.getOutputStream();

      long length = file.length();
      byte[] bytes = new byte[16 * 1024];

      int count;
      while ((count = in.read(bytes)) > 0) {
          out.write(bytes, 0, count);
    }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
