/*
  IoTDevice.java
  Object storing all the information for an IoT Device. Superclass for all the plugins
*/

public abstract class IoTDevice{
  private String pluginFile, serverIP;
  private int serverPort;

  public IoTDevice(int sP, String ip){
    serverIP = ip;
    serverPort = sP;
  }

  public int getServerPort(){
    return serverPort;
  }

  public String getServerIP(){
    return serverIP;
  }

  public abstract String filterMessage(String message);
}
