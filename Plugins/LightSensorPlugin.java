/*
  LightSensorPlugin.java
  Supplemental plugin file for smart light (IoT device)
*/

public class LightSensorPlugin extends IoTDevice{

  //Overwrites constructor

  public LightSensorPlugin(int sP, String ip){
    super(sP, ip);
  }

  //Defines filter. Called when server inputs the given message

  public String filterMessage(String message){
    if (message.equals("test")) {
      message = "Off";
    }
    if (message.equals("bug")) {
      message = null;
      message.toString();
    }
    return message;
  }
}
