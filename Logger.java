/*
  Logger.java
  This file records all IP traffic
*/

import java.io.PrintWriter;
import java.io.FileOutputStream;
import java.io.IOException;

public class Logger{
	private PrintWriter pw = null;

  public Logger(String f){ //Constructor
    try{
      pw = new PrintWriter(new FileOutputStream(f, true));
    }
    catch(Exception e){
      e.printStackTrace();
    }
  }

  public void println(String line){ //Prints line to file
    try{
      pw.println(line);
      pw.flush();
      System.out.println(line);
    }
    catch(Exception e){//exception e is thrown and the printStackTrace funtion is carried out
      e.printStackTrace();
    }
  }

}
