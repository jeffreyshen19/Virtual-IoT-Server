public class Password{
  private String className;
  private int password;

  public Password(String c, int p){
    className = c;
    password = p;
  }

  public String getClassName(){
    return className;
  }

  public int getPassword(){
    return password;
  }
}
