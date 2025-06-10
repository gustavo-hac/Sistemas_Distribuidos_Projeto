package models;

import java.util.ArrayList;
import java.util.List;

public class UserList {
  List<User> userList = new ArrayList<>();
  int tokenCount = 0;
  String token = "a00001";
  
  public boolean createUser(String userInput, String passInput, String nickInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    if(this.UserIsRegistered(userInput)){return false; }
    else {userList.add(new User(userInput, passInput, nickInput, this.token)); this.tokenCount++; return true; }
  }

  public boolean deleteUser(String userInput, String passInput, String tokenInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
        this.userList.remove(user);
        return true; }
    }
    return false; 
  }

  public boolean updateUserAll(String userInput, String passInput, String tokenInput, String newNickInput, String newPassInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
        user.setNick(newNickInput);
        user.setPass(newPassInput);
        return true; }
    }
    return false; 
  }

  public boolean updateUserNick(String userInput, String passInput, String tokenInput, String newNickInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
        user.setNick(newNickInput);
        return true; }
    }
    return false; 
  }

  public boolean updateUserPass(String userInput, String passInput, String tokenInput, String newPassInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
        user.setPass(newPassInput);
        return true; }
    }
    return false; 
  }
  // Verifica se os dados do usuario inseridos existem.
  public boolean verifyUser(String userInput, String passInput, String tokenInput) {
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
        return true; }
    }
    return false;
  }

  public boolean UserIsRegistered(String userInput) {
    for (User user : userList) {
      if(user.getUser().equals(userInput)) {
        return true; }
    }
    return false;
  }

  // Exibir todos os usuários
  public void printAll(){
    userList.forEach(System.out::println);
  }
}

