/*
  FileSender.java
  Test code to send a file to the Virtual Service
*/

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.util.Scanner;

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
      Scanner kboard = new Scanner(System.in);

      System.out.println("Input a filename: ");
      String filename = kboard.nextLine();
      File file = new File(filename);
      InputStream in = new FileInputStream(file);
      OutputStream out = sslSocket.getOutputStream();

      String fName = file.getName();
      fName += "\n";
      byte[] fileName = fName.getBytes();
      out.write(fileName);
      System.out.println("File name sent successfully");

      System.out.println("Input a passcode: ");
      String password = kboard.nextLine();
      password += "\n";
      byte[] pWord = password.getBytes();
      out.write(pWord);
      System.out.println("Passcode sent successfully");

      long length = file.length();
      byte[] bytes = new byte[16 * 1024];

      int count;
      while ((count = in.read(bytes)) > 0) {
          out.write(bytes, 0, count);
      }

      System.out.println("File sent successfully");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
