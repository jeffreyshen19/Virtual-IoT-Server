import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

/*
 *
 * Enter in first the port, then the name of the file that you will be writing to, then the number of connections you will be waiting for
 */

public class SSLTempMonitorMulti extends Thread {

  public static void main(String[] args) throws Exception {
    ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
    ss = ssf.createServerSocket(Integer.parseInt(args[0]));
    sockets = new ArrayList<Socket>();
    filename = args[1];
    System.out.println("Ready for both...");
    while (true) {
      new SSLTempMonitorMulti(Integer.parseInt(args[2])).start();
    }
  }

  private static String filename = "";
  private static ArrayList<Socket> sockets;
  private static ServerSocket ss;

  public SSLTempMonitorMulti(int n) {
    for (int i = 0; i < n; i++) {
      try {
        sockets.add(ss.accept());
      }
      catch (Exception e) {
        System.out.println("nonononon" + e.toString());
      }
    }
  }

  public void run() {
    boolean receiving = true, running = true, running1 = true;
    ArrayList<String> messages = new ArrayList<>();
    int sleepTime = 1000;
    try {
      while (running) {

        for (int i = 0; i < sockets.size(); i++) {

          if (running) { //two br's , two socket's
            BufferedReader br = new BufferedReader(new InputStreamReader(sockets.get(i).getInputStream()));
            PrintWriter pw = new PrintWriter(sockets.get(i).getOutputStream());
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

      }

      for (int i = 0; i < sockets.size(); i++) {
        sockets.get(i).close();
      }

      System.out.println("jeff-o!");
      try{
        PrintWriter writer = new PrintWriter(filename, "UTF-8");
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
  }
}
