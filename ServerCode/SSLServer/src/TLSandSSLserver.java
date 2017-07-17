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

public class TLSandSSLserver extends Thread implements Runnable {

  protected ServerSocket        ss = null, ssTLS = null;
  protected Socket              sock, sockTLS;
  protected Thread              runningThread= null;
  protected boolean             isStopped    = false;
  protected ExecutorService     threadPool = Executors.newFixedThreadPool(10);

  public static void main(String[] args) throws Exception {

    TLSandSSLserver server = new TLSandSSLserver();
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

  public TLSandSSLserver() {
    System.out.println("connecting the sockets");
  }

  public void run() {
    synchronized(this){
      this.runningThread = Thread.currentThread();
    }

    System.out.println("Ready...");

    openServerSocket();

    System.out.println("process of connecting");

    while(! isStopped()){
      try {
        this.ss.setSoTimeout(1000);
        //do i need the following line?
        sock = this.ss.accept();
        this.threadPool.execute(new WorkerRunnable(sock));
        //do this in nonblocking ^^^
      } catch (IOException e) {
        System.out.println("ERROR ON THIS");
      }
      try {
        this.ssTLS.setSoTimeout(1000);
        sockTLS = this.ssTLS.accept();
        this.threadPool.execute(new WorkerRunnable(sockTLS));
      } catch (IOException e) {
        System.out.println("ERROR ON THAT");
      }
      //also do another tcp
    }
    this.threadPool.shutdown();
    System.out.println("Server Stopped.") ;
  }

  private void openServerSocket() {
    ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
    ServerSocketFactory ssfTLS = ServerSocketFactory.getDefault();
    try {
      this.ss = ssf.createServerSocket(5000);
      this.ssTLS = ssfTLS.createServerSocket(8000);

    } catch (IOException e) {
      System.out.println("MESSED UP");
      e.printStackTrace();
    }
  }
}
