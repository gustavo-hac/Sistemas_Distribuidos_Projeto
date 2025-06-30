package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class VerifyJson {
  JSONObject json = new JSONObject();
  JSONObject jsonResponse = new JSONObject();

  String regexOp = "[0-9]{3}$";
  String regexUser = "^[a-zA-Z0-9]{6,16}$";
  String regexPass = "^[a-zA-Z0-9]{6,32}$";
  String regexNewPass = "^[a-zA-Z0-9]{6,32}$|^$";
  String regexNick = "^[a-zA-Z0-9]{6,16}$";
  String regexNewNick = "^[a-zA-Z0-9]{6,16}$|^$";
  String regexToken = "^(a|c)[0-9]{1,5}$";
  String regexMsg = "^[a-zA-Z].{1,128}$";

  String errorRegex = "Algum campo com formato errado";
  String errorNull = "Algum campo obrigatório com valor nulo";
  // Validações que o servidor precisa fazer nos campos após verificar "op"
  List<String> registerKeyList  = new ArrayList<>(Arrays.asList("user", "pass", "nick"));
  List<String> loginKeyList = new ArrayList<>(Arrays.asList("user", "pass"));
  List<String> logoutKeyList = new ArrayList<>(Arrays.asList("user", "token"));
  List<String> retrieveKeyList = new ArrayList<>(Arrays.asList("user", "token"));
  List<String> updateKeyList = new ArrayList<>(Arrays.asList("user", "pass", "token", "new_nick", "new_pass"));
  List<String> deleteKeyList = new ArrayList<>(Arrays.asList("user", "pass", "token"));
  List<String> updateAdmKeyList = new ArrayList<>(Arrays.asList("user", "token", "new_nick", "new_pass"));
  List<String> deleteAdmKeyList = new ArrayList<>(Arrays.asList("user", "token"));
  List<String> retrieveAdmKeyList = new ArrayList<>(Arrays.asList( "token"));
  // Validações que o cliente precisa fazer fazer nos campos após verificar "op" de sucesso
  List<String> registerResponseKeyList  = new ArrayList<>(Arrays.asList("msg"));
  List<String> loginResponseKeyList = new ArrayList<>(Arrays.asList("token"));
  List<String> logoutResponseKeyList = new ArrayList<>(Arrays.asList("msg"));
  List<String> retrieveResponseKeyList = new ArrayList<>(Arrays.asList("user", "nick"));
  List<String> updateResponseKeyList = new ArrayList<>(Arrays.asList("msg"));
  List<String> deleteResponseKeyList = new ArrayList<>(Arrays.asList("msg"));
  List<String> updateAdmResponseKeyList = new ArrayList<>(Arrays.asList("msg"));
  List<String> deleteAdmResponseKeyList = new ArrayList<>(Arrays.asList("msg"));
  List<String> retrieveAdmResponseKeyList = new ArrayList<>(Arrays.asList("user_list"));
  // Validação após verificar "op" de erro
  List<String> errorResponseKeyList = new ArrayList<>(Arrays.asList("msg"));

  public VerifyJson(JSONObject json) {
    this.json = json;
  }
  // Retorna o JSON de resposta para o servidor.
  public JSONObject getJsonResponse(){
    return this.jsonResponse;
  }
  // Faz as verificações de Nulo e Regex e retorna se o JSON está válido.
  public boolean operationIsValid(String operation){
    String error;
    switch (operation) {
      case "register" -> {error = "012";}
      case "login" -> {error = "002";}
      case "logout" -> {error = "022";}
      case "retrieve" -> {error = "007";}
      case "update" -> {error = "032";}
      case "delete" -> {error = "042";}
      case "updateADM" -> {error = "082";}
      case "deleteADM" -> {error = "092";}
      case "retrieveADM" -> {error = "112";}
      case "error_response" -> {error = "998";}
      default -> {System.out.printf("Erro na string da operação, String: %s , {função: operationIsValid[switch(operation)]}  ", operation); return false;}
    }
    if(this.operationIsNULL(operation)){ //  Verifica se algum campo está NULO.
      jsonResponse.put("op", error);
      jsonResponse.put("msg", this.errorNull);
      return false;
    }else{
      if(this.operationRegex(operation)){ // Verifica se todos os campos estão dentro do REGEX.
        return true;
      }else{
        jsonResponse.put("op", error);
        jsonResponse.put("msg", this.errorRegex);
        return false;
      }
    }
  }

  public boolean operationResponseIsValid(String operation){
    String error = "999";
    if(this.operationIsNULL(operation)){ //  Verifica se algum campo está NULO.
      System.out.println("operationIsNULL");
      jsonResponse.put("op", error);
      jsonResponse.put("msg", this.errorNull);
      return false;
    }else{
      if(this.operationRegex(operation)){ // Verifica se todos os campos estão dentro do REGEX.
        System.out.println("operationRegex");
        return true;
      }else{
        jsonResponse.put("op", error);
        jsonResponse.put("msg", this.errorRegex);
        return false;
      }
    }
  }

  // Verificações de Nulo para cada "op" (Operação)
  public boolean operationIsNULL(String operation){
    List<String> keyList;
    boolean isNull = false;
    switch (operation) {
      case "register" -> {keyList = this.registerKeyList;}
      case "login" -> {keyList = this.loginKeyList;}
      case "logout" -> {keyList = this.logoutKeyList;}
      case "retrieve" -> {keyList = this.retrieveKeyList;}
      case "update" -> {keyList = this.updateKeyList;}
      case "delete" -> {keyList = this.deleteKeyList;}
      case "updateADM" -> {keyList = this.updateAdmKeyList;}
      case "deleteADM" -> {keyList = this.deleteAdmKeyList;}
      case "retrieveADM" -> {keyList = this.retrieveAdmKeyList;}
      case "sucess_login" -> {keyList = this.loginResponseKeyList;}
      case "sucess_register" -> {keyList = this.registerResponseKeyList;}
      case "sucess_logout" -> {keyList = this.logoutResponseKeyList;}
      case "sucess_retrieve" -> {keyList = this.retrieveResponseKeyList;}
      case "sucess_update" -> {keyList = this.updateResponseKeyList;}
      case "sucess_delete" -> {keyList = this.deleteResponseKeyList;}
      case "sucess_updateADM" -> {keyList = this.updateAdmResponseKeyList;}
      case "sucess_deleteADM" -> {keyList = this.deleteAdmResponseKeyList;}
      case "sucess_retrieveADM" -> {keyList = this.retrieveAdmResponseKeyList;}
      case "error_response" -> {keyList = this.errorResponseKeyList;}
      default -> {System.out.printf("Erro na string da operação, String: %s  ", operation); return true;}
    }
    for (String key : keyList) {
      if(this.keyIsNULL(key)) {System.out.printf("\"%s\" com valor Nulo  ", key); isNull = true;}
    }
    return isNull;
  }

  // Verificaçõe de qual REGEX será utilizado com base na "op" (Operação)
  public boolean operationRegex(String operation){
    List<String> keyList;
    boolean succes = true;
    switch (operation) {
      case "register" -> {keyList = this.registerKeyList;}
      case "login" -> {keyList = this.loginKeyList;}
      case "logout" -> {keyList = this.logoutKeyList;}
      case "retrieve" -> {keyList = this.retrieveKeyList;}
      case "update" -> {keyList = this.updateKeyList;}
      case "delete" -> {keyList = this.deleteKeyList;}
      case "updateADM" -> {keyList = this.updateAdmKeyList;}
      case "deleteADM" -> {keyList = this.deleteAdmKeyList;}
      case "retrieveADM" -> {keyList = this.retrieveAdmKeyList;}
      case "sucess_login" -> {keyList = this.loginResponseKeyList;}
      case "sucess_register" -> {keyList = this.registerResponseKeyList;}
      case "sucess_logout" -> {keyList = this.logoutResponseKeyList;}
      case "sucess_retrieve" -> {keyList = this.retrieveResponseKeyList;}
      case "sucess_update" -> {keyList = this.updateResponseKeyList;}
      case "sucess_delete" -> {keyList = this.deleteResponseKeyList;}
      case "sucess_updateADM" -> {keyList = this.updateAdmResponseKeyList;}
      case "sucess_deleteADM" -> {keyList = this.deleteAdmResponseKeyList;}
      case "sucess_retrieveADM" -> {keyList = this.retrieveAdmResponseKeyList;}
      case "error_response" -> {keyList = this.errorResponseKeyList;}
      default -> {System.out.printf("Erro na string da operação, String: %s  ", operation); return false;}
    }
    for (String key : keyList) {
      if(!this.keyRegex(key)) {System.out.printf("\"%s\" no formato errado  ", key); succes = false;}
    }
    return succes;
  }

  // Verificações de formatação por REGEX (Campos)
  public boolean keyRegex(String key) { // Função que verifica se o valor um campo(key) do JSON está de acordo com o REGEX. Retorna TRUE se for igual, FALSE caso contrário.
    String regex;
    String value;
    switch (key) {
        case "op" -> {regex = this.regexOp; value = this.getValue(key);}
        case "user" -> {regex = this.regexUser; value = this.getValue(key);}
        case "pass" -> {regex = this.regexPass; value = this.getValue(key);}
        case "nick" -> {regex = this.regexNick; value = this.getValue(key);}
        case "token" -> {regex = this.regexToken; value = this.getValue(key);}
        case "new_nick" -> {regex = this.regexNewNick; value = this.getValue(key);}
        case "new_pass" -> {regex = this.regexNewPass; value = this.getValue(key);}
        case "msg" -> {regex = this.regexMsg; value = this.getValue(key);}
        case "user_list" -> {
          regex = this.regexUser;
          JSONArray userArray = this.json.getJSONArray("user_list");
          for (int i = 0; i < userArray.length(); i++) {
            value = userArray.getString(i);
            if(! Pattern.matches(regex, value)){
              return false;
            }
          }
          return true;
        }
        default -> {System.err.printf("Campo \" %s\" não definido no protocolo", key); return false;}
    }
    return Pattern.matches(regex, value);
  }

  // Verificações de Nulo
  public boolean keyIsNULL(String key) {
    return this.json.isNull(key);
  }

  // Getters
  public String getValue(String key) { // Função que retorna o valor de um campo(key) do JSON. Se for NULO retorna String vazia ("").
    if (!this.json.isNull(key)){
      return this.json.getString(key); 
    }else{
      return "";
    } 
  }
}