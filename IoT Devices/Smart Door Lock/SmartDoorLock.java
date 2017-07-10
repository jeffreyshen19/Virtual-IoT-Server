import mraa.Aio;
import mraa.Gpio;
import mraa.Pwm;
import java.util.concurrent.TimeUnit;

public class SmartDoorLock {
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
  public static void main(String[] args){
    //instantiate the sensors/motor
    Gpio button = new Gpio(3);
    Aio light = new Aio(3);
    Pwm servo = new Pwm(6);
    servo.write(.01f);
    servo.enable(true);
    servo.period_ms(20);
    try {
      TimeUnit.SECONDS.sleep(3);
    }catch (InterruptedException e) {
    }
    servo.enable(false);

  }
}
