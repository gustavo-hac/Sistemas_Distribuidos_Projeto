package models;

import java.util.ArrayList;
import java.util.List;

public class UserList {
  List<User> userList = new ArrayList<>();
  int tokenCount = 0;
  String token = "a00001";
  // Adicionar um qualquer
  public void addUser(User user) { // Adiciona somente usuarios com o campo "user" não utilizados.
    userList.add(user); this.tokenCount++;
  }

  // Register
  public boolean createUser(String userInput, String passInput, String nickInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    if(this.UserIsRegistered(userInput)){ return false; }
    else {userList.add(new User(userInput, passInput, nickInput, this.token)); this.tokenCount++; return true; }
  }

  // Delete
  public boolean deleteUserByAll(String userInput, String passInput, String tokenInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
        this.userList.remove(user);
        return true; }
    }
    return false; 
  }
  
  public boolean deleteUserByToken(String userInput, String tokenInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getToken().equals(tokenInput) ) {
        this.userList.remove(user);
        return true; }
    }
    return false; 
  }

  public void  deleteUser(User user) { // Adiciona somente usuarios com o campo "user" não utilizados.
    this.userList.remove(user);
  }

  // Update
  public boolean updateUser(String userInput, String passInput, String tokenInput, String newNickInput, String newPassInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
        if(! newNickInput.isEmpty()){
          user.setNick(newNickInput); }
        if(! newPassInput.isEmpty()){
          user.setPass(newPassInput); }
        return true; }
    }
    return false; 
  }

  // Retrieve
  public User retrieveUserByAll(String userInput, String userPass, String userToken){
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(userPass) && user.getPass().equals(userPass)) {
        return user; }
    }
    return null;
  }
  public User retrieveUserByToken(String userInput, String userToken){
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getToken().equals(userToken)) {
        return user; }
    }
    return null;
  }
  
  public User retrieveUserByPass(String userInput, String userPass){
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(userPass)) {
        return user; }
    }
    return null;
  }
  // Verifica se os dados do usuario inseridos existem
  // public boolean verifyUser(String userInput, String passInput, String tokenInput) {
  //   for (User user : userList) {
  //     if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
  //       return true; }
  //   }
  //   return false;
  // }

  // public boolean  verifyUserPass(String userInput, String passInput) {
  //   for (User user : userList) {
  //     if(user.getUser().equals(userInput) && user.getPass().equals(passInput)) {
  //       return true; }
  //   }
  //   return false;
  // }

  // public boolean UserIsRegistered(String userInput) {
  //   for (User user : userList) {
  //     if(user.getUser().equals(userInput)) {
  //       return true; }
  //   }
  //   return false;
  // }

  // Exibir todos os usuários
  @Override
  public String toString() {
    String users = "";
    for (User user : userList) {
      users +=  (user.toString() + "\n");
    }
    return users;
  }

  public void printAll(){
    userList.forEach(System.out::println);
  }
}