/*
  DoorSensorPlugin.java
  Supplemental to the smart door lock (IoT device) 
*/

public class DoorSensorPlugin extends IoTDevice{ //Overwrites constructor

  public DoorSensorPlugin(int sP, String ip){
    super(sP, ip);
  }

  public String filterMessage(String message){ //Defines filter. Called when server inputs the given message
    if(message.equals("CHANGE PASSWORD")) message = "UNLOCK";
    return message;
  }
}
