  /*
  Compiler.java
  Compiiles a class and run any method in the class.
  Supplemental to the Plugin class (and related classes).
  */

import java.io.*;
import java.util.*;
import java.lang.reflect.*;

public class Compiler {

  public Compiler(){
    //nothing needed in constructor yet
  }

  /*
  changes the format of file, such as from .txt to a .java file
  Can be used to rename a file as well.
  Can be used to set up plugins.
  */
  public void renameFile(String location, String newName){
    File file = new File(location);
    file.renameTo(new File(newName));
  }

  /*
  runs a selected (non-main) method in a class.
  program breaks and returns error if the method does not exist in the class.
  */
  public void runProgram(String class1, String method1) throws Exception{

    Class params[] = {};
    Object paramsObj[] = {};

    Class thisClass = Class.forName(class1);

    Object iClass = thisClass.newInstance();

    Method thisMethod = thisClass.getDeclaredMethod(method1, params);

    System.out.println(thisMethod.invoke(iClass, paramsObj));
  }

  /*
  compiles the selected class.
  program breaks and returns error if the class does not exist in the directory.
  equivalent to punching javac "class name".java in terminal.
  */
  public void compile (String class2) throws Exception{

    try
    {
      runProcess("javac "+ class2 +".java");
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }

  /*
  runs a selected class' main method.
  program breaks and returns error if the class does not have a main method.
  equivalent to punching java "class name" in terminal.
  */
  public void runMain(String class3) throws Exception{

    try
    {
      runProcess("java " + class3);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

  }
  /*
  reads the results of the operations given by the system.
  Called in runProcess to print out those results.
  Used in compile and runMain.
  */
  private static void printLines(String name, InputStream ins) throws Exception {

    String line = null;
    BufferedReader in = new BufferedReader(new InputStreamReader(ins));

    while ((line = in.readLine()) != null) {
      System.out.println(name + " " + line);
    }
  }

  /*
  prints out the results of operations completed.
  Used in compile and runMain.
  */
  private void runProcess(String command) throws Exception {

    Process pro = Runtime.getRuntime().exec(command);

    printLines(command + " stdout:", pro.getInputStream());
    printLines(command + " stderr:", pro.getErrorStream());

    pro.waitFor();
    System.out.println(command + " exitValue() " + pro.exitValue());
  }
}
