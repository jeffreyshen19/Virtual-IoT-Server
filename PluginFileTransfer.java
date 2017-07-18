/*
  PluginFileTransfer.java
  This file defines the thread that accepts new plugins and installs them on the Virtual Service.
*/

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.net.ssl.*;
import java.net.URLClassLoader;
import java.net.URL;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class PluginFileTransfer extends Thread{
  private ArrayList<VirtualMachine> machines;

  public PluginFileTransfer(ArrayList<VirtualMachine> m){
    super();
    machines = m;
  }

  public void run(){ //Overrides run method.
    SSLServerSocket serverSocket = null;
    SSLServerSocketFactory factory= (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
    SSLSocket sslsocket = null;

    InputStream in = null;
    OutputStream out = null;

    String filename = "";

    try{
      serverSocket = (SSLServerSocket) factory.createServerSocket(9000); //sets up a distinct socket.
    }
    catch(Exception e){
      e.printStackTrace();
    }

    while(true){
      try{
        sslsocket = (SSLSocket) serverSocket.accept(); //listens for connection from client.
      }
      catch(Exception e){
        e.printStackTrace();
      }

      try {
        in = sslsocket.getInputStream();
      } catch (IOException ex) {
        System.out.println("Can't get socket input stream. ");
      }

      try{ //reads the name of the file, which is the first line of input.
        filename = new BufferedReader(new InputStreamReader(sslsocket.getInputStream())).readLine();
      }
      catch(Exception e){
        e.printStackTrace();
      }

      File f = new File("Plugins/" + filename);

      if(filename.endsWith(".class")){

        System.out.println("\033[1m\033[32mNow receiving " + filename + "\033[0m");

        filename = filename.replace("class", "txt");

        try {
          new FileOutputStream("Plugins/" + filename, false).close(); //Create filename
          out = new FileOutputStream("Plugins/" + filename);
        }
        catch (Exception e) {
          e.printStackTrace();
        }

        byte[] bytes = new byte[16*1024];

        try{
          int count;
          while ((count = in.read(bytes)) > 0) {
            out.write(bytes, 0, count);
          }

          //Rename to the file's original name
          File file = new File("Plugins/" + filename);
          filename = filename.replace("txt", "class");
          File file2 = new File("Plugins/" + filename);

          file.renameTo(file2);

          URL[] urls = null;

          File dir = new File("./Plugins/");
          URL url = dir.toURI().toURL();
          urls = new URL[] { url };

          URLClassLoader cl = new URLClassLoader(urls);
          //Class cls = cl.loadClass(filename.split("\\.")[0], false);
          Class cls = Class.forName(filename.split("\\.")[0], true, (ClassLoader) cl);
          //Class cls = Class.forName("LightSensorPluginNew", true, cl);

          cl.close();

          Class[] cArg = new Class[2];
          cArg[0] = int.class;
          cArg[1] = String.class;

          for(int i = 0; i < machines.size(); i++){
            VirtualMachine machine = machines.get(i);
            //if(machine.getClassName().equals(filename.split("\\.")[0])){
            if(true){
              IoTDevice currentDevice = machine.getDevice();
              IoTDevice newDevice = (IoTDevice) cls.getDeclaredConstructor(cArg).newInstance(currentDevice.getServerPort(), currentDevice.getServerIP());

              System.out.println(newDevice.filterMessage("yeeeeee"));
              machine.setDevice(newDevice);
            }
          }

          System.out.println("\033[1m\033[32mSuccessfuly loaded plugin\033[0m");
        }
        catch(Exception e){
          e.printStackTrace();
        }

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
      else{
        System.out.println("The file received is not a class file and thus cannot be run.");
      }
    }
  }
}
