import mraa.*;
import java.net.Socket;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

/*
  SmartLight.java
  Contains the code for a connected light.
*/

public class SmartLight{

  static {
    try {
      System.loadLibrary("mraajava");
    } catch (UnsatisfiedLinkError e) {
      System.err.println(
      "Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" +
      e);
      System.exit(1);
    }
  }

  public static void wait1Msec(int msecs){
    try{
      Thread.sleep(msecs);
    }
    catch(Exception e){
      System.out.println("Oops!");
    }
  }

  public static void main(String[] args) {
    Gpio led = new Gpio(4);
    Aio lightSensor = new Aio(0);

    led.dir(Dir.DIR_OUT);

    int on = 0;
    long lightValue = 0;
    String command = "";

    Socket clientSocket = null;
    BufferedReader in = null;

    try{
      clientSocket = new Socket(args[0], Integer.parseInt(args[1]));
    }
    catch(Exception e){}

    try{
      in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    catch(Exception e){}

    while(true){
      lightValue = lightSensor.read();
      try{
        command = in.readLine().trim().toLowerCase();
      }
      catch(Exception e){}

      if(command.equals("off")) on = 1;
      else if(command.equals("on")) on = -1;
      else if(command.equals("reset")) on = 0;

      if(lightValue < 200 && on != -1){
        led.write(1);
      }
      else if(on != 1){
        led.write(0);
      }

      wait1Msec(10);
    }
  }
}
