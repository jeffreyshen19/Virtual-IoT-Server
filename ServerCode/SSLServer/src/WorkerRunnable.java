import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
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


public class WorkerRunnable implements Runnable{

  protected Socket clientSocket = null;
  protected String serverText   = null;

  public WorkerRunnable(Socket clientSocket, String serverText) {
    this.clientSocket = clientSocket;
    this.serverText   = serverText;
  }

  public void run() {
    System.out.println("10");
    boolean receiving = true, running = true, running1 = true;
    ArrayList<String> messages = new ArrayList<>();
    int sleepTime = 1000;
    try {
      while (running) {
          if (running) { //two br's , two socket's
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter pw = new PrintWriter(clientSocket.getOutputStream());
            if (receiving) {
              String data = br.readLine();
              messages.add(data);
              System.out.println(data + " is echoed");
            }

            BufferedReader msgTaker = new BufferedReader(new InputStreamReader(System.in));
            long end=System.currentTimeMillis()+1500;
            String message = "";
            while((System.currentTimeMillis()<end)) {
              if (msgTaker.ready())
              message += msgTaker.readLine();
            }
            if (message.equals("")) {
              System.out.println("No input");
            }
            else {
              messages.add(message);
              System.out.println("The message is " + message);
            }
            if (message.equals("")) {
              pw.println(message);
            }
            else {
              if (message.equals("STOP")) {
                receiving = false;
              }
              else if (message.equals("OFF")) {
                running = false;
              }
              else if (message.equals("START")) {
                receiving = true;
              }
              else if (message.contains("PERIOD=")) {
                sleepTime = Integer.parseInt(message.substring(message.indexOf("=") + 1))*1000;
              }
              messages.add(message);
              pw.println(message);
            }
            pw.flush();

        }

      }

      clientSocket.close();

      System.out.println("jeff-o!");
      try{
        PrintWriter writer = new PrintWriter("jeffo.txt", "UTF-8");
        for (int i = 0; i < messages.size(); i++){
          writer.println(messages.get(i));
        }
        writer.close();
      } catch (IOException e) {
        // do something
      }
    } catch (IOException ioe) {
      // Client disconnected
    }
    System.out.println("got here");


    // try {
    //   InputStream input  = clientSocket.getInputStream();
    //   OutputStream output = clientSocket.getOutputStream();
    //   long time = System.currentTimeMillis();
    //   output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: " + this.serverText + " - " + time + "").getBytes());
    //   output.close();
    //   input.close();
    //   System.out.println("Request processed: " + time);
    // } catch (IOException e) {
    //   //report exception somewhere.
    //   e.printStackTrace();
    // }
  }
}
