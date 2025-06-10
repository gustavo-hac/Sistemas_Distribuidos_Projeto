import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import controllers.VerifyJson;
import models.User;
import models.UserList;

public class Server extends Thread {
  protected Socket clientSocket;
  UserList userList = new UserList();
  UserList userLogged = new UserList();
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
        // String errorValidation = "Dados fornecidos não existem ou não conferem com os dados no sistema"; // Erro para testes do projeto, verifica se os dados existem ou conferem com o informado. (Remover futuramente)
        /* Just Test
        User user = new User();*/
        while ((inputLine = in.readLine()) != null) {
          System.out.println("Servidor recebeu: " + inputLine);
          JSONObject jsonResponse = new JSONObject();
          JSONObject jsonInput = new JSONObject();

          try {jsonInput = new JSONObject(inputLine);
          }catch (JSONException  e) {
            jsonInput = new JSONObject();
            System.out.println("Erro: entrada não é um JSON válido.");
            jsonResponse.put("erro", "Formato inválido. Esperado JSON.");
          }
          
          if(! (jsonInput.isEmpty())){
            VerifyJson verifyJsonInput = new VerifyJson(jsonInput);

            if(! (verifyJsonInput.keyIsNULL("op"))){
              String option = verifyJsonInput.getValue("op");
              switch (option) {
                case "000" -> { // Login
                  if(verifyJsonInput.operationIsValid("login")){
                      jsonResponse.put("token", "a1");
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }else{
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }
                }
                case "005" -> { // Retornar Cadastro
                  if(verifyJsonInput.operationIsValid("retrieve")){
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }else{
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }
                }
                case "010" -> { // Cadastro
                  if(verifyJsonInput.operationIsValid("register")){
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }else{
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }
                }
                case "020" -> { // Logout
                  if(verifyJsonInput.operationIsValid("logout")){
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }else{
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }
                }
                case "030" -> { // Update
                  if(verifyJsonInput.operationIsValid("update")){
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }else{
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }
                }
                case "040" -> { // Delete
                  if(verifyJsonInput.operationIsValid("delete")){
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }else{
                      jsonResponse = verifyJsonInput.getJsonResponse();
                  }
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


