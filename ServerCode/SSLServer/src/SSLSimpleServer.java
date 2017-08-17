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

  public static void main(String[] args) throws Exception {
    ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
    ServerSocket ss = ssf.createServerSocket(Integer.parseInt(args[0]));

    System.out.println("Ready...");


    SSLSimpleServer server = new SSLSimpleServer(ss);

    new Thread(server).start();
    try {
      Thread.sleep(200 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Stopping Server");

  }

  private synchronized boolean isStopped() {
    return this.isStopped;
  }

  public SSLSimpleServer(ServerSocket s) {
    System.out.println("connecting the sockets");
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
