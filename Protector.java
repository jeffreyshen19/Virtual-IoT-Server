public class Protector {
  private String IPAddress;
  private ArrayList<String> blacklist;

  public Protector(String inAddr, ArrayList<String> inList){
    IPAddress = inAddr;
    ArrayList<String> blacklist = inList;
  }

  public boolean safeFromDDoS(){
    return true;
  }

  public boolean safeIP(){
    for (int i = 0; i < blacklist.size(); i++){
      if (IPAddress == blacklist.get(i)){
        return false;
      }
    }

    return true;
  }
}
