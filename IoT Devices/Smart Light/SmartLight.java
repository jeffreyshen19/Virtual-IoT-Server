import mraa.*;

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
    Gpio button  = new Gpio(3);
    Gpio led = new Gpio(4);
    Aio lightSensor = new Aio(0);

    led.dir(Dir.DIR_OUT);
    button.dir(Dir.DIR_IN);

    boolean on = false;
    boolean forceOn = false;
    boolean forceOff = false;
    long lightValue = 0;

    while(true){
      if(button.read() == 1){
        if(on){
          on = false;
          forceOn = false;
          forceOff = true;
          led.write(0);
        }
        else{
          on = true;
          forceOn = true;
          forceOff = false;
          led.write(1);
        }

        while(button.read() == 1) wait1Msec(10);
      }

      lightValue = lightSensor.read();

      if(lightValue < 200 && !forceOff){
        on = true;
        led.write(1);
      }
      else if(!forceOn){
        on = false;
        led.write(0);
      }

      wait1Msec(10);
    }
  }
}
