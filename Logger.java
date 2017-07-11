/*
  Logger.java
  This file records all IP traffic
*/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger{
  private BufferedWriter bw = null;
	private FileWriter fw = null;

  public Logger(String f){ //Constructor
    try{
      fw = new FileWriter(f);
      bw = new BufferedWriter(fw);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public void println(String line){ //Prints line to file
    System.out.println(line);
    try{
      bw.write(line + "\n");
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public void close(){ //Closes the BufferedWriter and FileWriter
    try{
      bw.close();
      fw.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

}
