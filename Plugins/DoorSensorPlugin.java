public class DoorSensorPlugin extends IoTDevice{

  public DoorSensorPlugin(int sP, String ip){
    super(sP, ip);
  }

  public String filterMessage(String message){
    if(message.equals("LOCK")) message = "UNLOCK";
    return message;
  }
}
