/*
  Runtime exception to test if Virtual Service Crashes
*/
public class LightSensorPluginBug extends IoTDevice{

    //Overwrites constructor

    public LightSensorPluginBug(int sP, String ip){
      super(sP, ip);
    }

    //Defines filter. Called when server inputs the given message

    public String filterMessage(String message){

      if (message.equals("Bug")) {
        message = null;
        message.toString();
      }
      if (message.equals("testOff")) {
        message = "Off";
      }
      if (message.equals("testOn")) {
        message = "On";
      }

      return message;
    }
  }
