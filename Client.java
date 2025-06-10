import java.io.*;
import java.net.*;

import org.json.JSONObject;

public class Client {

    public static void main(String[] args) throws IOException {

        System.out.println("Qual o IP do servidor? ");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String serverIP = br.readLine();

        System.out.println("Qual a Porta do servidor? ");
        br = new BufferedReader(new InputStreamReader(System.in));
        int serverPort = Integer.parseInt(br.readLine());

        System.out.println("Tentando conectar com host " + serverIP + " na porta " + serverPort);

        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            socket = new Socket(serverIP, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Host " + serverIP + " nao encontrado!");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Não foi possivel reservar I/O para conectar com " + serverIP);
            System.exit(1);
        }

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String option;

        JSONObject json;
        String user, pass, nick, token, new_nick, new_pass;
        do {
          System.out.print("Digite a opção:\n-1 = TRAVAR O SERVER, 0 = sair, 1 = Login, 2 = Retornar Dados, 3 = Cadastrar, 4 = Logout, 5 = Alterar Cadastro, 6 = Apagar Cadastro, 7 = Alterar Cadastro(admin) 8 = Apagar Cadastro(admin)\n");
          System.out.println("ANTES\n");
          option = stdIn.readLine();
          if(option == null){
            System.out.println("NULO");
          }else{
            System.out.println(option);
            System.out.println("ALGO");
          }
          System.out.println(option);
          switch (option){        
            case "-1" -> {
              json = new JSONObject();
              json.put("op", "000"); // Cadastro
              System.out.println("Usuario:(I'ts id)");
              String id = stdIn.readLine();
              json.put("id", id);
              System.out.println("Senha:(I'ts z)");
              String z = stdIn.readLine();
              json.put("z", z);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
              break;
            }
            case "0" -> {
              out.println("BYE");
              break;
            }
            case "1" -> {
              json = new JSONObject();
              json.put("op", "000"); // Login
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Senha:");
              pass = stdIn.readLine();
              json.put("pass", pass);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
              break;
            }
            case "2" -> {
              json = new JSONObject();
              json.put("op", "005"); // Retornar Dados
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Token:");
              token = stdIn.readLine();
              json.put("token", token);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
              break;
            }
            case "3" -> {
              json = new JSONObject();
              json.put("op", "010"); // Cadastro
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Apelido:");
              nick = stdIn.readLine();
              json.put("nick", nick);
              System.out.println("Senha:");
              pass = stdIn.readLine();
              json.put("pass", pass);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
              break;
            }
            case "4" -> {
              json = new JSONObject();
              json.put("op", "020"); // Logout
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Token:");
              token = stdIn.readLine();
              json.put("token", token);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
              break;
            }
            case "5" -> {
              json = new JSONObject();
              json.put("op", "030"); // Alterar Cadastro
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Senha:");
              pass = stdIn.readLine();
              json.put("pass", pass);
              System.out.println("Nova Apelido:");
              new_nick = stdIn.readLine();
              json.put("new_nick", new_nick);
              System.out.println("Nova Senha:");
              new_pass = stdIn.readLine();
              json.put("new_pass", new_pass);
              System.out.println("Token:");
              token = stdIn.readLine();
              json.put("token", token);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
              break;
            }
            case "6" -> {
              json = new JSONObject();
              json.put("op", "040"); // Apagar Cadastro
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Senha:");
              pass = stdIn.readLine();
              json.put("pass", pass);
              System.out.println("Token");
              token = stdIn.readLine();
              json.put("token", token);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
              break;
            }
            case "7" -> {
              json = new JSONObject();
              json.put("op", "080"); // Alterar Cadastro (admin)
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Token:");
              token = stdIn.readLine();
              json.put("token", token);
              System.out.println("Nova Apelido:");
              new_nick = stdIn.readLine();
              json.put("new_nick", new_nick);
              System.out.println("Nova Senha:");
              new_pass = stdIn.readLine();
              json.put("new_pass", new_pass);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
              break;
            }
            case "8" -> {
              json = new JSONObject();
              json.put("op", "090"); // Apagar Cadastro (admin)
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Token");
              token = stdIn.readLine();
              json.put("token", token);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
              break;
            }
            default -> {
              System.out.println("Digite uma opção valida");
              break;
            }
          }
          System.out.println("Servidor retornou: " + in.readLine());
        } while (!option.equals("0"));
          
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
}
