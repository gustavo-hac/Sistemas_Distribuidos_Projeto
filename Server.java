import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;

import controllers.Verify;
//import models.User;

public class Server extends Thread {

  protected Socket clientSocket;

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
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

      String inputLine;

      /* Just Test
       User user = new User();*/

      while ((inputLine = in.readLine()) != null) {
        System.out.println("Servidor recebeu: " + inputLine);
              
        JSONObject jsonRecebido = new JSONObject(inputLine);
        JSONObject jsonResposta = new JSONObject();

        if(jsonRecebido.isEmpty()){
          jsonResposta.put("msg", "JSON está vazio");
        }else{
          Verify verifica = new Verify(jsonRecebido);
          if(verifica.opIsNULL()){
            jsonResposta.put("msg", "OP está Nulo");
          }else{
            String option = verifica.getOp();
            switch (option) {
              case "000" -> {
                  System.out.println(option);
                  jsonResposta.put("msg", "case 000");
                  if(verifica.loginContainNULL()){
                    jsonResposta.put("op", "case 002");
                  }else{
                    
                  }
                  }
              case "010" -> {
                  System.out.println(option);
                  jsonResposta.put("msg", "case 010");
                  if(verifica.loginContainNULL()){
                    jsonResposta.put("op", "case 002");
                  }else{
                    
                  }
                  }
              case "020" -> {
                  System.out.println(option);
                  jsonResposta.put("msg", "case 020");
                  }
              default -> {
                  System.out.println(option);
                  jsonResposta.put("msg", "OP não estabelecido");
                  }
            }
          }
        }

        System.out.println("Servidor enviou: " + jsonResposta.toString() + " " + clientSocket.getInetAddress().getHostAddress() + " : " + clientSocket.getPort());
        out.println(jsonResposta.toString());

        if (inputLine.toUpperCase().equals("BYE"))
          break;
      }

      out.close();
      in.close();
      clientSocket.close();

    }catch (IOException e) {
      System.err.println("Problema com Servidor de Communicacao!");
      System.exit(1);
    }
  }
}


