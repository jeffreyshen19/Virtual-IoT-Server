/*
  DoorSensorPlugin.java
  Supplemental to the smart door lock (IoT device)
*/



public class DoorSensorPlugin extends IoTDevice{

  //Overwrites constructor

  public DoorSensorPlugin(int sP, String ip){
    super(sP, ip);
  }

//Defines filter. Called when server inputs the given message

  public String filterMessage(String message){

    //A test to see if it functions.

    if (message.equals("Change Password")) message = "CHANGE PASSWORD";
    if (message.equals("Lock")) message = "LOCK";
    if(message.equals("Unlock")) message = "UNLOCK";

    return message;
  }
}
