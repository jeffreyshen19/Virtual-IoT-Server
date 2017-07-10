/*
  IoTDevice.java
  Object storing all the information for an IoT Device
*/

public class IoTDevice{
  private String pluginFile, String serverIP;

  public IoTDevice(String pF, String ip){
    pluginFile = pF;
    serverIP = ip;
  }

  public String getPluginFile(){
    return pluginFile;
  }

  public String getServerIP(){
    return serverIP;
  }
}
