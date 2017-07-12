/*
  LightSensorPlugin.java
  Supplemental plugin file for smart light (IoT device)
*/

public class LightSensorPlugin extends IoTDevice{

  public LightSensorPlugin(int sP, String ip){ //Overrides constructor
    super(sP, ip);
  }

  public String filterMessage(String message){ //Defines filter. Called when server inputs the given message
    if(message.equals("OFF")) message = "ON";
    return message;
  }
}
