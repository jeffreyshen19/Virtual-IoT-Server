/*
  txtReader.java
  Reads .txt files and perform manipulation to information obtained. 
  Has capability of updating plug-in's. 
*/

import java.io.*;
import java.util.*;

public class txtReader{
  private String name;
  private ArrayList<String> info;
  private BufferedReader br;
  private FileReader fr;

  public txtReader(String inName){
    name = inName;
  }

  /*
  reads the selected text file.
  fileLocation is the directory, like "/Users/PeizeHe/Desktop/test.txt"
  */
  public void readtxt(String fileLocation){ 
    ArrayList<String> protocol = new ArrayList<String>();
    br = null;
    fr = null;

    String inter = "";
    String element = "";

    try {
      fr = new FileReader(fileLocation);
      br = new BufferedReader(fr);
      String sCurrentLine;
      br = new BufferedReader(new FileReader(fileLocation));
      while ((sCurrentLine = br.readLine()) != null) { //read lines until no line exists
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

    for(int i = 0; i < protocol.size(); i++){ //remove all spaces
      for(int j = 0; j < protocol.get(i).length(); j++){
        if(protocol.get(i).substring(j, j + 1).equals(" ")){
          element = "" + protocol.get(i).substring(0, j) + protocol.get(i).substring(j + 1);
          protocol.set(i, element.toLowerCase());
        }
      }
    }

    for(int i = 0; i < protocol.size(); i++){ //remove all empty lines
      inter = protocol.get(i);
      if(protocol.get(i).equals("")){
        protocol.remove(i);
      }
    }

    info = protocol;
  }
}
