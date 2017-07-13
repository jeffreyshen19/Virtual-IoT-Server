/*
  SmartDoorLock.java
  IoT Device for testing purpose
  Includes basic functions including lock, unlock, and change password.
*/

import mraa.Aio;
import mraa.Gpio;
import mraa.Pwm;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.net.ssl.SSLSocket;
import java.io.PrintWriter;
import java.util.Timer;

public class SmartDoorLock {
  private String status;

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

    //SSL
    SSLSocket sslSocket = null;
    SSLClientSocket mSSLClientSocket = new SSLClientSocket(args[0], Integer.parseInt(args[1]));
    if(mSSLClientSocket.checkAndAddCertificates()) {
      sslSocket = mSSLClientSocket.getSSLSocket();
    }
    else {
      return;
    }

    try {

      String userInput = "" , serverResponse = "";
      BufferedReader br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
      PrintWriter pw = new PrintWriter(sslSocket.getOutputStream());
      pw.println("Initiating connection from the client");
      pw.flush();
      br.readLine();

      //while(br.readLine() == null ) {
        pw.println(args[2] + ":" args[3] + "|DoorSensorPlugin");
        /*try {
          Thread.sleep(1000);
        } catch (Exception e) {}
      } */

      System.out.println("\033[1m\033[32mSuccessfully connected to secure server\033[0m");

      //instantiate the sensors/motor
      Gpio button = new Gpio(3);
      Aio light = new Aio(3);
      Pwm servo = new Pwm(6);

      //setting default password
      double password = 1111;
      double enteredPassword;

      while(true) {
        serverResponse = br.readLine().trim();
        Timer timer = new Timer();
        timer.schedule(pw.println(status), 0, 5000);

        if (button.read() == 1) {
          if (inputPassword(button) == password) { //checks the password entered by button pattern
            unlock(servo);
          }
        }

        //analyzes server's message
        if(serverResponse.equals("LOCK")) {
          lock(servo);
          pw.println("Succesfully locked.");
          pw.flush();
        }
        if(serverResponse.equals("UNLOCK")) {
          unlock(servo);
          pw.println("Succesfully unlocked.");
          pw.flush();
        }
        if(serverResponse.equals("CHANGE PASSWORD")) {
          //System.out.println("got here !!!!! ");
          password = changePassword(br,pw,password);
          pw.println("Succesful password change. New password is " + password);
          pw.flush();
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }


  }

  public static String unlock(Pwm door) { //unlocks the door. Called when server issues "UNLOCK"
    door.enable(true);
    door.period_ms(20);
    door.pulsewidth_us(500);
    try {
      TimeUnit.SECONDS.sleep(1);
    }catch (InterruptedException e) {
    }
    door.enable(false);
    status = "UNLOCKED";
  }

  public static String lock(Pwm door) { //locks the door. Called when server issues "LOCK"
    door.enable(true);
    door.period_ms(20);
    door.pulsewidth_us(2500);
    try {
      TimeUnit.SECONDS.sleep(1);
    }catch (InterruptedException e) {
    }
    door.enable(false);
    status = "LOCKED";
  }

  public static double inputPassword(Gpio doorButton) { //allows user to enter a password.

    ArrayList<Integer> generatedPassword = new ArrayList<Integer>();
    int passLength = 0;
    int value = 0;
    double pass = 0;
    int shouldContinue = 0;

    BufferedReader length = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("How long is the password?");

    try {
      passLength = Integer.parseInt(length.readLine());
    } catch (Exception e)  {}

    for (int i = 0; i < passLength; i++) {
      BufferedReader cont = new BufferedReader(new InputStreamReader(System.in));
      System.out.println("Type 1 to continue recording the password: ");

      try {
      shouldContinue = Integer.parseInt(cont.readLine());
      } catch (Exception e) {}

      if (shouldContinue == 1) {
        value = doorButton.read();
        generatedPassword.add(value);
      }
      else {
        break;
      }

    }

      for(int i = 0; i < passLength; i++){
      pass = pass + (Math.pow(10,(passLength-i-1)) * generatedPassword.get(i));
      }

    return pass;
  }

  public static double changePassword(BufferedReader br, PrintWriter pw, double currentPass) { //alllows server to enter a new password
    pw.println("What is the new password?");
    double password = currentPass;
    try {
       password = Integer.parseInt(br.readLine());
    } catch (Exception e)  {}
    pw.flush();
    return password;
  }

}
