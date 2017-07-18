/*
  Logger.java
  This file records all IP traffic
*/

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger{
  private BufferedWriter bw = null;//bw set to null
	private FileWriter fw = null;//fw set to null
  private String file;

  public Logger(String f){ //Constructor
    file = f;
    open();
  }

  public void println(String line){ //Prints line to file
    System.out.println(line);
    try{
      bw.write(line + "\n");//bw tries to write line
      close();
      open();
    }
    catch(Exception e){//exception e is thrown and the printStackTrace funtion is carried out
      e.printStackTrace();
    }
  }

  public void open(){
    try{
      fw = new FileWriter(file);
      bw = new BufferedWriter(fw);
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public void close(){
    try{
      bw.close();
      fw.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

}
