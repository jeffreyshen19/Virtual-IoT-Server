import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.*;

public class PluginFileTransfer extends Thread{
  public void run(){
    SSLServerSocket serverSocket = null;
    SSLServerSocketFactory factory= (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    SSLSocket sslsocket = null;

    InputStream in = null;
    OutputStream out = null;

    String filename = "";

    try{
      serverSocket = (SSLServerSocket) factory.createServerSocket(9000);
    }
    catch(Exception e){
      e.printStackTrace();
    }


    while(true){
      try{
        sslsocket = (SSLSocket) serverSocket.accept();
      }
      catch(Exception e){
        e.printStackTrace();
      }

      try {
        in = sslsocket.getInputStream();
      } catch (IOException ex) {
        System.out.println("Can't get socket input stream. ");
      }

      try {
        new FileOutputStream("Plugins/" + filename, false).close(); //Create filename
        out = new FileOutputStream("Plugins/" + filename);
      } catch (Exception e) {
        e.printStackTrace();
      }

      byte[] bytes = new byte[16*1024];

      try{
        int count;
        while ((count = in.read(bytes)) > 0) {
          out.write(bytes, 0, count);
        }
      }
      catch(Exception e){
        e.printStackTrace();
      }

      //Wrap it up
      try{
        out.close();
        in.close();
        sslsocket.close();
        Thread.sleep(10);
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
  }
}
