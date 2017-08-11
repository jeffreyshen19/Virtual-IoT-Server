import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

/**
 *
 * SSLSimpleServer class creates a non-blocking SSL server
 * 7/17/17
 * Ryan Goggins
 * Adapted from: http://chanakasameera.blogspot.com/2015/04/ssl-server-socket-and-ssl-client-socket.html
 * Run with: java -Djavax.net.ssl.keyStore=./../SSLServerKeyStore/ExampleServerCertificateKeyStore.jks -Djavax.net.ssl.keyStorePassword=abc12345 SSLSimpleServer [args]
 *
 */

public class SSLSimpleServer extends Thread implements Runnable {

  protected ServerSocket ss;
  protected Thread       runningThread= null;
  protected boolean      isStopped    = false;
  protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
  protected static boolean  portAvailable = true;

  public static void main(String[] args) throws Exception {
    int port = Integer.parseInt(args[0]);
    ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();

    System.out.println("Ready...");




    while (true) {
      ServerSocket ss = ssf.createServerSocket(port);
      SSLSimpleServer server = new SSLSimpleServer(ss);
      new Thread(server).start();

      while (portAvailable) {

      }
      while(!portAvailable){ //Make sure port actually works
        port++;
        portAvailable = true;

        try{
          new ServerSocket(port).close();
        }
        catch(IOException e){
          portAvailable = false;
        }
      }

    }


    /*try {
      Thread.sleep(200 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Stopping Server");
    */
  }

  private synchronized boolean isStopped() {
    return this.isStopped;
  }

  public SSLSimpleServer(ServerSocket s) {
    System.out.println("Now accepting communications on port " + s.getLocalPort());
    this.ss = s;
  }

  public void run() {
    System.out.println("at connecting stage");
    synchronized(this){
      this.runningThread = Thread.currentThread();
    }

    while(! isStopped()){
      try {
        Socket sock = ss.accept();
        portAvailable = false;
        this.threadPool.execute(new WorkerRunnable(sock, "Thread Pooled Server"));
        System.out.println("STARTING WORKER RUNNABLE");
      } catch (Exception e) {
        e.printStackTrace();
      }

      //do this such that WorkerRunnable's are connected via IP
    }
    this.threadPool.shutdown();
    System.out.println("Server Stopped.") ;
  }
}
