import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

public class ThreadPooledSSLServer implements Runnable{

  protected int          serverPort   = 8080;
  protected ServerSocket serverSocket = null;
  protected boolean      isStopped    = false;
  protected Thread       runningThread= null;
  protected ExecutorService threadPool =
  Executors.newFixedThreadPool(10);


  public static void main(String[] args) { //put the port in the input
    ThreadPooledSSLServer server = new ThreadPooledSSLServer(Integer.parseInt(args[0]));
    new Thread(server).start();

    try {
      Thread.sleep(200 * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Stopping Server");
    server.stop();

  }


  public ThreadPooledSSLServer(int port){
    this.serverPort = port;
  }

  public void run(){
    System.out.println("connected");
    synchronized(this){
      this.runningThread = Thread.currentThread();
    }
    openServerSocket();
    while(! isStopped()){
      Socket clientSocket = null;


      try {
        clientSocket = this.serverSocket.accept();
        System.out.println("Socket created");
      } catch (IOException e) {
        if(isStopped()) {
          System.out.println("Server Stopped.") ;
          break;
        }
        throw new RuntimeException(
        "Error accepting client connection", e);
      }

      //start that new thread boyo
      this.threadPool.execute(
      new WorkerRunnable(clientSocket));

    }
    this.threadPool.shutdown();
    System.out.println("Server Stopped.") ;
  }


  private synchronized boolean isStopped() {
    return this.isStopped;
  }

  public synchronized void stop(){
    this.isStopped = true;
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      throw new RuntimeException("Error closing server", e);
    }
  }

  private void openServerSocket() {
    try {
      ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
      this.serverSocket = ssf.createServerSocket(this.serverPort);
      //this.serverSocket = new ServerSocket(this.serverPort);
    } catch (IOException e) {
      throw new RuntimeException("Cannot open port 8080", e);
    }
  }
}
