import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import controllers.VerifyJson;
import javax.management.openmbean.OpenDataException;
import models.User;
import models.UserList;

public class Server extends Thread {
  protected Socket clientSocket;
  UserList userList = new UserList(); // Usuarios existentes
  UserList userSessionList = new UserList(); // Usuarios conectados
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

  /**
    * Override java.language.Thread
    */
  @Override
  public void run() {
    System.out.println("Nova thread de comunicacao iniciada.\n");

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

          String opSucess;
          String opError;
          String msgRegisterError = "Já existe uma conta com este usuário"; 
          String msgValidationError = "Dados fornecidos não existem ou não conferem com os dados no sistema";
          // Declaração dos campos que podem ser fornecidos pelo Cliente.
          String userInput;
          String passInput;
          String nickInput;
          String tokenInput;
          String newPassInput;
          String newNickInput;

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
                      userSessionList.createUser(userData.getUser(), userData.getPass(), userData.getNick());
                      jsonResponse.put("op", opSucess);
                      jsonResponse.put("token", userData.getToken());
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
                      userSessionList.createUser(userData.getUser(), userData.getPass(), userData.getNick());
                      jsonResponse.put("op", opSucess);
                      jsonResponse.put("user", userData.getUser());
                      jsonResponse.put("nick", userData.getNick());
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
                      userSessionList.deleteUser(user); // Remove um objeto direto da lista de usuarios conectados.
                      System.out.printf("Lista: %s", userSessionList.toString());
                      jsonResponse.put("op", opSucess);
                    } else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                case "030" -> { // Update
                  opSucess = "031";
                  opError = "032";
                  if(verifyJsonInput.operationIsValid("update")){
                    userInput = verifyJsonInput.getValue("user");
                    tokenInput = verifyJsonInput.getValue("token");
                    passInput = verifyJsonInput.getValue("pass");
                    newNickInput = verifyJsonInput.getValue("new_nick");
                    newPassInput = verifyJsonInput.getValue("new_pass");
                    if(userList.updateUser(userInput, passInput, tokenInput, newNickInput, newPassInput)){
                      System.out.printf("Lista: %s", userList.toString());
                      jsonResponse.put("op", opSucess);
                    } else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                case "040" -> { // Delete
                  opSucess = "041";
                  opError = "042";
                  if(verifyJsonInput.operationIsValid("delete")){
                    userInput = verifyJsonInput.getValue("user");
                    tokenInput = verifyJsonInput.getValue("token");
                    passInput = verifyJsonInput.getValue("pass");
                    User user = userList.retrieveUserByAll(userInput, passInput, tokenInput);
                    if(user != null){
                      userSessionList.deleteUser(user); // Faz o Logou do usuario antes de remover.
                      userList.deleteUser(user); // Remove um objeto direto da lista de usuarios existentes.
                      System.out.printf("Lista: %s", userList.toString());
                      jsonResponse.put("op", opSucess);
                    } else{ jsonResponse.put("op", opError); jsonResponse.put("msg", msgValidationError);}
                  }else{ jsonResponse = verifyJsonInput.getJsonResponse(); }
                }

                default -> { jsonResponse.put("msg", "OP não estabelecido"); }
              }
            }else{ jsonResponse.put("msg", "OP está Nulo"); }
          }else{ jsonResponse.put("msg", "JSON está vazio"); }
          
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


