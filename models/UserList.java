package models;

import java.util.ArrayList;
import java.util.List;

public class UserList {
  List<User> userList = new ArrayList<>();
  int tokenCount = 0;
  String token = "c00001";
  // Adicionar um qualquer
  public void addUser(User user) {
    userList.add(user); this.tokenCount++;
  }

  // Register
  public boolean createAdm(String userInput, String passInput, String nickInput, String tokenInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    if(this.userIsRegistered(userInput)){ return false; }
    else {userList.add(new User(userInput, passInput, nickInput, tokenInput, true)); return true; }
  }

  public boolean createUser(String userInput, String passInput, String nickInput) { // Adiciona somente usuarios com o campo "user" não utilizados.
    if(this.userIsRegistered(userInput)){ return false; }
    else {userList.add(new User(userInput, passInput, nickInput, this.token, false)); this.tokenCount++; return true; }
  }

  // Delete
  public boolean deleteUserByAll(String userInput, String passInput, String tokenInput) { // Remove o próprio usuários desde que não sejam ADM
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
        if(user.getAdm() == false){ 
          this.userList.remove(user);
          return true;
        }else{
          System.out.println("Tentando apagar usuário administrador");
          return false; 
        }
      }
    }
    return false; 
  }
  
  public boolean deleteUserByToken(String userInput, String tokenInput) { // Remove qualquer usuário desde que não sejam ADM, Remoção utilizada pelo ADM
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getToken().equals(tokenInput) ) {
        if(user.getAdm() == false){ 
          this.userList.remove(user);
          return true;
        }else{
          System.out.println("Tentando apagar usuário administrador");
          return false; 
        }
      }
    }
    return false; 
  }

  public void  deleteUser(User user) { // Remove usuários por instância
    this.userList.remove(user);
  }

  // Update
  public boolean updateUser(String userInput, String passInput, String tokenInput, String newNickInput, String newPassInput) { 
    for (User user : userList) {
      if(user.getUser().equals(userInput) && user.getPass().equals(passInput) && user.getToken().equals(tokenInput) ) {
        if(user.getAdm() == false){ 
          if(! newNickInput.isEmpty()){
            user.setNick(newNickInput); }
          if(! newPassInput.isEmpty()){
            user.setPass(newPassInput); }
          return true; 
        }else{
          System.out.println("Tentando atualizar usuário administrador");
          return false; 
        }
      }
    }
    return false; 
  }

  public boolean updateUser(String userInput, String newNickInput, String newPassInput) { 
    for (User user : userList) {
      if(user.getUser().equals(userInput)) {
        if(user.getAdm() == false){
          if(! newNickInput.isEmpty()){
            user.setNick(newNickInput); }
          if(! newPassInput.isEmpty()){
            user.setPass(newPassInput); }
          return true; }
        }else{
          System.out.println("Tentando atualizar usuário administrador");
          return false; 
        }
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

  public List<String> retrieveUserList(){
    List<String> users = new ArrayList<>();
    for (User user : userList) {
      users.add(user.getUser());
    }
    return users;
  }
  
  public User retrieveUser(String userInput){
    for (User user : userList) {
      if(user.getUser().equals(userInput)) {
        return user; }
    }
    return null;
  }

  public boolean userIsRegistered(String userInput) {
    for (User user : userList) {
      if(user.getUser().equals(userInput)) {
        return true; }
    }
    return false;
  }

  public boolean tokenIsRegistered(String tokenInput) {
    for (User user : userList) {
      if(user.getToken().equals(tokenInput)) {
        return true; }
    }
    return false;
  }

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