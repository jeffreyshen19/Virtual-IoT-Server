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

  public Logger(String f){
    fw = new FileWriter(f);
    bw = new BufferedWriter(fw);
  }

  public void println(String line){ //Prints line to file
    bw.write(line + "\n");
  }

  public void close(){ //Closes the BufferedWriter and FileWriter
    bw.close();
    fw.close();
  }

}
