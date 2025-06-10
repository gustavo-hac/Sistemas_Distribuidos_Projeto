package models;

import java.util.ArrayList;
import java.util.List;

public class UserList {
  List<User> userList = new ArrayList<>();
  public void addUser(String user, String pass, String nick, String token) {
    userList.add(new User(user, pass, nick, token));
  }

  public void addUser(String user, String pass, String nick) {
    userList.add(new User(user, pass, nick));
  }
  
  // Exibir todos os usuários
  public void printAll(){
    userList.forEach(System.out::println);
  }
}

