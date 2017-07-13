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

public class SSLSimpleServer extends Thread implements Runnable {

  protected Socket         sock;
  protected Thread       runningThread= null;
  protected boolean      isStopped    = false;
  protected ExecutorService threadPool = Executors.newFixedThreadPool(10);

  public static void main(String[] args) throws Exception {
    ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
    ServerSocket ss = ssf.createServerSocket(Integer.parseInt(args[0]));

    System.out.println("Ready...");
    Socket ssSocket = ss.accept();

    SSLSimpleServer server = new SSLSimpleServer(ssSocket);

    System.out.println("process of connecting");
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

  public SSLSimpleServer(Socket s) {
    System.out.println("connecting the sockets");
    sock = s;
  }

  public void run() {
    System.out.println("at connecting stage");
    synchronized(this){
      this.runningThread = Thread.currentThread();
    }

    while(! isStopped()){
      this.threadPool.execute(new WorkerRunnable(sock));
    }
    this.threadPool.shutdown();
    System.out.println("Server Stopped.") ;
    System.out.println("got here");
  }
}
