import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import controllers.VerifyJson;
import models.User;
import models.UserList;

public class Server extends Thread {
  protected Socket clientSocket;
  UserList userList = new UserList(); // Usuarios existentes
  UserList userSessionList = new UserList(); // Usuarios conectados
  String admKeyUser = "admin123";
  String admKeyPass = "admin123";
  String admKeyNick = "admin123";
  String admKeyToken = "a00001";
  public static void main(String[] args) throws IOException {
    ServerSocket serverSocket = null;

    System.out.println("Qual porta o servidor deve usar? ");
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int porta = Integer.parseInt(br.readLine());

    System.out.println("Servidor carregado na porta " + porta);
    System.out.println("Aguardando conexao....\n ");

    try {
      serverSocket = new ServerSocket(porta); // instancia o socket do servidor na porta especificada
      System.out.println("Criado Socket de Conexao.\n");
      try {
        while (true) {
          new Server(serverSocket.accept());
          System.out.println("Accept ativado. Esperando por uma conexao...\n");
        }
      } catch (IOException e) {
        System.err.println("Accept falhou!");
        System.exit(1);
      }
    } catch (IOException e) {
      System.err.println("Nao foi possivel ouvir a porta " + porta);
      System.exit(1);
    } finally {
      try {
        serverSocket.close();
      } catch (IOException e) {
        System.err.println("Nao foi possivel fechar a porta " + porta);
        System.exit(1);
      }
    }
  }

  // Constructor
  private Server(Socket clientSoc) {
    clientSocket = clientSoc; 
    start();
  }

  @Override
  public void run() {
    System.out.println("Nova thread de comunicacao iniciada.\n");
    userList.createAdm(admKeyUser, admKeyPass, admKeyNick, admKeyToken);

    try {
      try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          System.out.println("Servidor recebeu: " + inputLine);
          JSONObject jsonResponse = new JSONObject();
          JSONObject jsonInput = new JSONObject();
        
          try {jsonInput = new JSONObject(inputLine);
          }catch (JSONException  e) {
            jsonInput = new JSONObject();
            System.out.println("Erro: entrada não é um JSON válido.");
            // jsonResponse.put("erro", "Formato inválido. Esperado JSON.");
          }
          String opSucess, opError;
          String opProtocolError = "998";
          String msgRegisterError = "Já existe uma conta com este usuário"; 
          String msgValidationError = "Dados fornecidos não existem ou não conferem com os dados no sistema";
          // Declaração dos campos que podem ser fornecidos pelo Cliente.
          String userInput= "", passInput= "", nickInput= "", tokenInput= "", newPassInput= "", newNickInput= "", titleInput= "", subjectInput= "", msgInput= "";

          if(! (jsonInput.isEmpty())){
            VerifyJson verifyJsonInput = new VerifyJson(jsonInput);

            if(! (verifyJsonInput.keyIsNULL("op"))){
              String option = verifyJsonInput.getValue("op");
              switch (option) {
                case "000" -> { // Login
                  opSucess = "001";
                  opError = "002";
                  if(verifyJsonInput.operationIsValid("login")){
                    userInput = verifyJsonInput.getValue("user");
                    passInput = verifyJsonInput.getValue("pass");
                    User userData = userList.retrieveUserByPass(userInput, passInput);
                    if(userData != null){
                      if(! userSessionList.userIsRegistered(userInput)){
                        userSessionList.createUser(userData.getUser(), userData.getPass(), userData.getNick());
                        jsonResponse.put("op", opSucess);
                        jsonResponse.put("token", userData.getToken());
                      }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Você já está logado");}
                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse();} // "verifyJsonInput.operationIsValid" em caso de erro nas validações é criado um json de reposta para validação de nulo e REGEX.
                }
                case "005" -> { // Retornar Cadastro
                  opSucess = "006";
                  opError = "007";
                  if(verifyJsonInput.operationIsValid("retrieve")){
                    userInput = verifyJsonInput.getValue("user");
                    tokenInput = verifyJsonInput.getValue("token");
                    User userData = userList.retrieveUserByToken(userInput, tokenInput);
                    if(userData != null){
                      if(userSessionList.userIsRegistered(userInput)){
                        jsonResponse.put("op", opSucess);
                        jsonResponse.put("user", userData.getUser());
                        jsonResponse.put("nick", userData.getNick());
                      }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Usuário não está logado");}
                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse();}
                }
                
                case "010" -> { // Cadastro
                  opSucess = "011";
                  opError = "012";
                  if(verifyJsonInput.operationIsValid("register")){
                    userInput = verifyJsonInput.getValue("user");
                    passInput = verifyJsonInput.getValue("pass");
                    nickInput = verifyJsonInput.getValue("nick");
                    if(userList.createUser(userInput, passInput, nickInput)){
                      jsonResponse.put("op", opSucess);
                      jsonResponse.put("msg", "Cadastro realizado com sucesso");
                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgRegisterError);}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                case "020" -> { // Logout
                  opSucess = "021";
                  opError = "022";
                  if(verifyJsonInput.operationIsValid("logout")){
                    userInput = verifyJsonInput.getValue("user");
                    tokenInput = verifyJsonInput.getValue("token");
                    User user = userSessionList.retrieveUserByToken(userInput, tokenInput);
                    if(user != null){
                      if(userSessionList.userIsRegistered(userInput)){
                        userSessionList.deleteUser(user); // Remove um objeto direto da lista de usuarios conectados.
                        System.out.printf("Lista: %s", userSessionList.toString());
                        jsonResponse.put("op", opSucess);
                        jsonResponse.put("msg", "Logout realizado com sucesso");
                      }else { jsonResponse.put("op", opError); jsonResponse.put("msg", "Usuário não está logado");}
                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                case "030" -> { // Update (Self)
                  opSucess = "031";
                  opError = "032";
                  if(verifyJsonInput.operationIsValid("update")){
                    userInput = verifyJsonInput.getValue("user");
                    tokenInput = verifyJsonInput.getValue("token");
                    passInput = verifyJsonInput.getValue("pass");
                    newNickInput = verifyJsonInput.getValue("new_nick");
                    newPassInput = verifyJsonInput.getValue("new_pass");
                    User user = userList.retrieveUserByAll(userInput, passInput, tokenInput);
                    if (user != null){
                      if (user.getAdm() == false) {
                        if(userSessionList.userIsRegistered(userInput)){
                          userSessionList.updateUser(userInput, passInput, tokenInput, newNickInput, newPassInput);
                          userList.updateUser(userInput, passInput, tokenInput, newNickInput, newPassInput);
                          System.out.printf("Lista: %s", userList.toString());
                          jsonResponse.put("op", opSucess);
                          jsonResponse.put("msg", "Update realizado com sucesso");
                        }else { jsonResponse.put("op", opError); jsonResponse.put("msg", "Usuário não está logado");}
                      }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Tentando atualizar usuário administrador");}
                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                case "040" -> { // Delete (Self)
                  opSucess = "041";
                  opError = "042";
                  if(verifyJsonInput.operationIsValid("delete")){
                    userInput = verifyJsonInput.getValue("user");
                    tokenInput = verifyJsonInput.getValue("token");
                    passInput = verifyJsonInput.getValue("pass");
                    User user = userList.retrieveUserByAll(userInput, passInput, tokenInput); // Verificação dos dados
                    if(user != null){
                      if (user.getAdm() == false) {
                        if(userSessionList.userIsRegistered(userInput)){ 
                          userSessionList.deleteUser(user); // Faz o Logout do usuario antes de remover.
                          userList.deleteUser(user); // Remove um objeto direto da lista de usuarios existentes.
                          System.out.printf("Lista: %s", userList.toString());
                          jsonResponse.put("op", opSucess);
                          jsonResponse.put("msg", "Delete realizado com sucesso");
                        }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Usuário não está logado");}
                      }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Tentando remover usuário administrador");}
                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }
                
                case "050" -> { // Enviar Tópico
                  opSucess = "051";
                  opError = "052";
                  if(verifyJsonInput.operationIsValid("sendTopic")){
                    tokenInput = verifyJsonInput.getValue("token");
                    titleInput = verifyJsonInput.getValue("title");
                    subjectInput = verifyJsonInput.getValue("subject");
                    msgInput = verifyJsonInput.getValue("msg");
                    if(userSessionList.userIsRegistered(userInput)){ 

                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Usuário não está logado");}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                case "080" -> { // Update (ADM)
                  opSucess = "081";
                  opError = "082";
                  if(verifyJsonInput.operationIsValid("updateADM")){
                    tokenInput = verifyJsonInput.getValue("token");
                    userInput = verifyJsonInput.getValue("user");
                    newNickInput = verifyJsonInput.getValue("new_nick");
                    newPassInput = verifyJsonInput.getValue("new_pass");
                    if(tokenInput.equals(admKeyToken)){
                      User user = userList.retrieveUser(userInput);
                      if(user != null){
                        if (user.getAdm() == false) {
                          if(userSessionList.tokenIsRegistered(tokenInput)){
                            userList.updateUser(userInput, newNickInput, newPassInput);
                            System.out.printf("Lista: %s", userList.toString());
                            jsonResponse.put("op", opSucess);
                            jsonResponse.put("msg", "Update realizado com sucesso");
                          }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Admin não está logado");}
                        }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Tentando atualizar usuário administrador");}
                      }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Usuário sem permissão para realizar operação");}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                case "090" -> { // Delete (ADM)
                  opSucess = "091";
                  opError = "092";
                  if(verifyJsonInput.operationIsValid("deleteADM")){
                    tokenInput = verifyJsonInput.getValue("token");
                    userInput = verifyJsonInput.getValue("user");
                    if(tokenInput.equals(admKeyToken)){
                      User user = userList.retrieveUser(userInput);
                      if(user != null){
                        if (user.getAdm() == false) {
                          if(userSessionList.tokenIsRegistered(tokenInput)){
                            userSessionList.deleteUser(user); // Faz o Logout do usuario antes de remover.
                            userList.deleteUser(user); // Remove um objeto direto da lista de usuarios existentes.
                            System.out.printf("Lista: %s", userList.toString());
                            jsonResponse.put("op", opSucess);
                            jsonResponse.put("msg", "Delete realizado com sucesso");
                          }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Admin não está logado");}
                        }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Tentando deletar usuário administrador");}
                      }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Usuário sem permissão para realizar operação");}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                case "110" -> { // Retrieve (ADM)
                  opSucess = "111";
                  opError = "112";
                  if(verifyJsonInput.operationIsValid("retrieveADM")){
                    tokenInput = verifyJsonInput.getValue("token");
                    if(tokenInput.equals(admKeyToken)){
                      if(userSessionList.tokenIsRegistered(tokenInput)){
                        List<String> users = this.userList.retrieveUserList();
                        jsonResponse.put("op", opSucess);
                        jsonResponse.put("user_list", users);
                      }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Admin não está logado");}
                    }else{ jsonResponse.put("op", opError); jsonResponse.put("msg", "Usuário sem permissão para realizar operação");}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                case "999" -> { // Erro
                  if(verifyJsonInput.operationIsValid("error_response")){
                    System.out.println("\nRetorno de falha recebido");
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse();}
                }
                default -> { jsonResponse.put("op", opProtocolError); jsonResponse.put("msg", "OP não estabelecido"); }
              }
            }else{ jsonResponse.put("op", opProtocolError); jsonResponse.put("msg", "OP está Nulo"); }
          }else{ jsonResponse.put("op", opProtocolError); jsonResponse.put("msg", "JSON está vazio"); }
          
          if (inputLine.toUpperCase().equals("BYE"))
            break;

          System.out.println("\nServidor enviou: " + jsonResponse.toString() + " " + clientSocket.getInetAddress().getHostAddress() + " : " + clientSocket.getPort());
          out.println(jsonResponse.toString());
        } 
      out.close();
      in.close();
      clientSocket.close();
      } catch (java.net.SocketException e) {
        clientSocket.close();
        System.err.println("Conexão encerrada abruptamente pelo cliente: " + clientSocket.getInetAddress().getHostAddress() + " : " + clientSocket.getPort());
      }

    }catch (IOException e) {
      System.err.println(e);
      System.err.println("Problema com Servidor de Communicacao!");
      System.exit(1);
    }
  }
}


