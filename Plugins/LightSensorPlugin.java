public class LightSensorPlugin extends IoTDevice{
  
  public LightSensorPlugin(int sP, String ip){
    super(sP, ip);
  }

  public String filterMessage(String message){
    return message;
  }
}
