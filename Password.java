public class Password{
  private String className;
  private String password;

  public Password(String c, String p){
    className = c;
    password = p;
  }

  public String getClassName(){
    return className;
  }

  public String getPassword(){
    return password;
  }
}
