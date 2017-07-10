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

  public static void main(String[] args) {
    Gpio button  = new Gpio(3);
    Gpio led = new Gpio(4);
    Aio lightSensor = new Aio(0);

    led.dir(Dir.DIR_OUT);
    button.dir(Dir.DIR_IN);

    //lightSensor.read();

    boolean on = false;

    while(true){
      if(button.read() == 1){
        if(on){
          on = false;
          led.write(0);
        }
        else{
          on = true;
          led.write(1);
        }
      }

      Thread.sleep(10);
    }
  }
}
