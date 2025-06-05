package controllers;

import java.util.regex.*;
import org.json.JSONObject;

public class Verify {
  JSONObject json = new JSONObject();
  //Criação dos REGEX
  String regexOp = "[0-9]{3}$";  
  String regexUser = "^[a-zA-Z0-9]{6,16}$";
  String regexPass = "^[a-zA-Z0-9]{6,32}$";
  String regexNick = "^[a-zA-Z0-9]{6,16}$";
  String regexToken = "^(a|c)[0-9]{1,5}$";

  public Verify(JSONObject json) {
    this.json = json;
  }
  // Verificações de Nulo
  public boolean anyIsNULL(String any) { // Função Generalizada para possíveis updates
    return this.json.isNull(any);
  }

  public boolean opIsNULL() {
    return this.json.isNull("op");
  }

  public boolean userIsNULL() {
    return this.json.isNull("user");
  }

    public boolean passIsNULL() {
    return this.json.isNull("pass");
  }

    public boolean nickIsNULL() {
    return this.json.isNull("nick");
  }

    public boolean tokenIsNULL() {
    return this.json.isNull("token");
  }

    public boolean new_nickIsNULL() {
    return this.json.isNull("new_nick");
  }

    public boolean new_passIsNULL() {
    return this.json.isNull("new_pass");
  }
  // Verificações de formatação por REGEX
  public boolean anyIsValid(String any) { // Função Generalizada para possíveis updates
    String regex;
    switch (any) {
        case "op" -> regex = regexOp;
        case "user" -> regex = regexUser;
        case "pass" -> regex = regexPass;
        case "nick" -> regex = regexNick;
        case "token" -> regex = regexToken;
        case "new_nick" -> regex = regexNick;
        case "new_pass" -> regex = regexPass;
        default -> throw new AssertionError();
    }
    if(Pattern.matches(regex, any)){
      System.out.println("\"{any}\" Fora do padrão");
      return true;
    }
    return false;
  }

  public boolean opIsValid(){
    String usuario = json.getString("op");
    if(Pattern.matches(regexOp, usuario)){
      System.out.println("\"Op\" Fora do padrão");
      return true;
    }
    return false;
  }

  public boolean userIsValid(){
    String usuario= json.getString("user");
    if(Pattern.matches(regexUser, usuario)){
      System.out.println("\"Usuario\" Fora do padrão");
      return true;
    }
    return false;
  }

  public boolean passIsValid(){
    String senha= json.getString("pass");
    if(Pattern.matches(regexPass, senha)){
      System.out.println("\"Senha\" Fora do padrão");
      return true;
    }
    return false;
  }

  public boolean nickIsValid(){
    String apelido= json.getString("nick");
    if(Pattern.matches(regexNick, apelido)){
      System.out.println("\"Nick\" Fora do padrão");
      return true;
    }
    return false;
  }

  public boolean tokenIsValid(){
    String apelido= json.getString("token");
    if(Pattern.matches(regexToken, apelido)){
      System.out.println("\"Token\" Fora do padrão");
      return true;
    }
    return false;
  }

  public boolean new_nickIsValid(){
    String apelido= json.getString("new_nick");
    if(Pattern.matches(regexNick, apelido)){
      System.out.println("\"Novo Nick\" Fora do padrão");
      return true;
    }
    return false;
  }

  public boolean new_passIsValid(){
    String apelido= json.getString("new_pass");
    if(Pattern.matches(regexPass, apelido)){
      System.out.println("\"Nova Senha\" Fora do padrão");
      return true;
    }
    return false;
  }
  // Verificações de Nulo para cada "op"
  public boolean loginContainNULL(){
    if(this.json.isNull("user"))
      return true;
    if(this.json.isNull("pass"))
      return true;
    return false;
  }

  public boolean registerContainNULL(){
    if(this.json.isNull("user"))
      return true;
    if(this.json.isNull("pass"))
      return true;
    if(this.json.isNull("nick"))
      return true;
    return false;
  }
  // Verificaçõe de REGEX para cada "op"
  public boolean login(){
    if(!this.userIsValid()){
      return false;
    }
    if(!this.passIsValid()){
      return false;
    }
    return true;
  }

  public boolean register(){
    if(!this.userIsValid()){
      return false;
    }
    if(!this.passIsValid()){
      return false;
    }
    if(!this.nickIsValid()){
      return false;
    }
    return true;
  }
  // Getters
  public String getAny(String any) { // Função Generalizada para possíveis updates
    if (!this.json.isNull(any)){
      return this.json.getString(any);
    }else{
      return "";
    } 
  }

  public String getOp() {
    return this.json.getString("op");
  }

  public String getUser() {
    return this.json.getString("user");
  }

  public String getPass() {
    return this.json.getString("pass");
  }

  public String getNick() {
    return this.json.getString("nick");
  }

  public String getToken() {
    return this.json.getString("token");
  }

  public String getNew_nick() {
    return this.json.getString("new_nick");
  }

  public String getNew_pass() {
    return this.json.getString("new_pass");
  }
} 
