/*
  SmartDoorLock.java
  IoT Device for testing purpose
  Includes basic functions including lock, unlock, and change password.
*/

/*
  Usage Notes
  - Must type # to trigger password input
  - Must type CHANGE PASSWORD on user side to change the password
  - Follow prompts when inputting password
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

public class SmartDoorLock {
  private static String status;

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

      //Setting up input
      sslSocket.setSoTimeout(1000);
      String serverResponse = "", userInput = "";
      BufferedReader br = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
      PrintWriter pw = new PrintWriter(sslSocket.getOutputStream());
      BufferedReader tag = new BufferedReader(new InputStreamReader(System.in));

      //Sending Virtual Service messages

      //IoT device supplies server ip and port and plugin name

      pw.println(args[2] + ":" + args[3] + "|DoorSensorPlugin");
      pw.flush();

      System.out.println("\033[1m\033[32mSuccessfully connected to secure server\033[0m");
      System.out.println("Press # to begin inputting password.");
      //Declare/instantiate the sensors/motor

      Gpio button = new Gpio(3);
      Aio light = new Aio(3);
      Pwm servo = new Pwm(6);

      long endTime;

      //Setting default password

      double password = 1111;
      double enteredPassword;


      status = "LOCKED";
      lock(servo);

      while(true) {
        endTime = System.currentTimeMillis() + 1000;
        while (endTime > System.currentTimeMillis()) {
          try{
            serverResponse = br.readLine().trim();
          }
          catch (Exception e){

          }
          if (tag.ready()) {
            userInput = tag.readLine().trim();
          }
        }
        //Send packets of locked/unlocked to server

        pw.println(status);
        pw.flush();

        //Checks the password entered by button pattern
        if (userInput.equals ("#")) {
          if (inputPassword(button) == password) {
            unlock(servo);
            System.out.println("Succesfully unlocked.");
            userInput = "";
          }
          else {
            lock(servo);
            System.out.println("Unsuccesful unlock. Please try again.");
            userInput = "";
          }
        }

        if(userInput.equals ("CHANGE PASSWORD")) {
          System.out.println("Verify current password by following the prompts.");
          if (inputPassword(button) == password) {
            System.out.println("Now change the password by follwing the prompts.");
            password = inputPassword(button);
          }
        }
        //Analyzes server's message

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
          long time =  System.currentTimeMillis();

          //Gives server 10 seconds to issue a new password. Otherwise it times out.
          while ((System.currentTimeMillis() - time) <  10000) {
            serverResponse = br.readLine().trim();
            pw.println("The password may be anywhere from 1 to 8 digits long, consisting of 1's and 0's.");
            pw.flush();
            if(!serverResponse.equals("CHANGE PASSWORD")) {
              try {
                password = Integer.parseInt(serverResponse);
              }
              catch (NumberFormatException nfe) {
                System.out.println("Server inputted too long a password.");
                nfe.printStackTrace();
              }
              System.out.println("Succesful password change. New password is " + password);
              pw.flush();
            }
          }
        }
        Thread.sleep(10);
      }
    } catch(Exception e) {
      e.printStackTrace();
    }


  }

  //Unlocks the door. Called when server issues "UNLOCK".

  public static void unlock(Pwm door) {
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

  //Locks the door. Called when server issues "LOCK".
  public static void lock(Pwm door) {
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

  //Allows user to enter a password through the button.

  public static double inputPassword(Gpio doorButton) {

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
}
