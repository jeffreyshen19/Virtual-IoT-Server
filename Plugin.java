import java.io.*;
import java.util.*;

public class Plugin{
  public String name;

  /*
  testing method. This main method should never be called.
  */
  public static void main(String[] args) {
    Plugin plugin1 = new Plugin ("door");
    ArrayList<String> array = plugin1.readtxt(args[0]);

    for(int i = 0; i < array.size(); i++){
      System.out.println(array.get(i));
    }
  }

  public Plugin(String inName){
    name = inName;
  }

  /*
  reads the selected text file.
  fileLocation is the directory, like "/Users/PeizeHe/Desktop/test.txt"
  */
  public ArrayList<String> readtxt(String fileLocation){
    ArrayList<String> protocol = new ArrayList<String>();
    BufferedReader br = null;
    FileReader fr = null;

    String inter = "";
    String element = "";

    try {
      fr = new FileReader(fileLocation);
      br = new BufferedReader(fr);
      String sCurrentLine;
      br = new BufferedReader(new FileReader(fileLocation));
      while ((sCurrentLine = br.readLine()) != null) {
        protocol.add(sCurrentLine);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (br != null)
        br.close();
        if (fr != null)
        fr.close();
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }

    for(int i = 0; i < protocol.size(); i++){
      for(int j = 0; j < protocol.get(i).length(); j++){
        if(protocol.get(i).substring(j, j + 1).equals(" ")){
          element = "" + protocol.get(i).substring(0, j) + protocol.get(i).substring(j + 1);
          protocol.set(i, element);
        }
      }
    }

    for(int i = 0; i < protocol.size(); i++){
      inter = protocol.get(i);
      if(protocol.get(i).equals("")){
        protocol.remove(i);
      }
    }

    return protocol;
  }
}
