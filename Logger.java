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

  public Logger(String f){ //Constructor
    try{
      fw = new FileWriter(f);//file writer object created. Used for writting streams of characters
      bw = new BufferedWriter(fw);//bufferedWriter object created
    }
    catch(Exception e){//exception e is thrown
      e.printStackTrace();//printStackTrace function will be carried out 
    }
  }

  public void println(String line){ //Prints line to file
    System.out.println(line);
    try{
      bw.write(line + "\n");//bw tries to write line
    }
    catch(Exception e){//exception e is thrown and the printStackTrace funtion is carried out
      e.printStackTrace();
    }
  }

  public void close(){ //Closes the BufferedWriter and FileWriter
    try{
      bw.flush();
      fw.flush();
      bw.close();//close method for bw
      fw.close();//close method for wf
    }
    catch(Exception e){//exception e thrown
      e.printStackTrace();//printStackTrace error
    }
  }

}
