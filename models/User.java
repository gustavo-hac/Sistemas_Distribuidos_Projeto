package models;

public class User {
  private String user;
  private String pass;
  private String nick;
  private String token;
  
  public User(String user, String pass, String nick, String token) {
      this.user = user;
      this.pass = pass;
      this.nick = nick;
      this.token = token;
  }
  
  public User(String user, String pass, String nick) {
      this.user = user;
      this.pass = pass;
      this.nick = nick;
  }
  
  // Getters
  public String getUser() {
      return user;
  }
  
  public String getPass() {
      return pass;
  }
  
  public String getNick() {
      return nick;
  }
  
  public String getToken() {
      return token;
  }
  
  // Setters
  public void setUser(String user) {
      this.user = user;
  }
  
  public void setPass(String pass) {
      this.pass = pass;
  }
  
  public void setNick(String nick) {
      this.nick = nick;
  }
  
  public void setToken(String token) {
      this.token = token;
  }
  
  @Override
  public String toString() {
      return "Usuario{" +
              "user='" + user + '\'' +
              ", nick='" + nick + '\'' +
              ", token='" + token + '\'' +
              '}'; // Não inclui a senha por segurança
  }
  
  public String printALL() { // Print de testes (inclui senha)
    return "Usuario{" +
            "user='" + user + '\'' +
            ", pass='" + pass + '\'' +
            ", nick='" + nick + '\'' +
            ", token='" + token + '\'' +
            '}';
  }

  /* Método para comparação de objetos
  @Override
  public boolean equals(Object obj) {
      if (this == obj) return true;
      if (obj == null || getClass() != obj.getClass()) return false;
      
      Usuario usuario = (Usuario) obj;
      return user != null ? user.equals(usuario.user) : usuario.user == null;
  }*/
}