public class Password{//Password is a new public class
  private String className;//className is a string
  private int password;//password is an int

  public Password(String c, int p){//c and p are the parameters of password
    className = c;//also could be this.c=c
    password = p;
  }

  public String getClassName(){//getClassName has no parameters
    return className;//will return the variable
  }

  public int getPassword(){//no parameters as well
    return password;//will return the variable
  }
}
