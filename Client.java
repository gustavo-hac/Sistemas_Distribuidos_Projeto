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
          System.out.print("Digite a opção:\n-1 = TRAVAR O SERVER, 0 = sair, 1 = Login, 2 = Cadastrar, 3 = Logout, 4 = Alterar Cadastro, 5 = Apagar Cadastro, 6 = Alterar Cadastro(admin) 7 = Apagar Cadastro(admin)\n");
          option = stdIn.readLine();
          switch (option){        
            case "-1" -> {
              json = new JSONObject();
              json.put("op", "010");
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
              break;
            }
            case "1" -> {
              json = new JSONObject();
              json.put("op", "000");
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Senha:");
              pass = stdIn.readLine();
              json.put("pass", pass);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
            }
            case "2" -> {
              json = new JSONObject();
              json.put("op", "010");
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
            }
            case "3" -> {
              json = new JSONObject();
              json.put("op", "020");
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Token:");
              token = stdIn.readLine();
              json.put("token", token);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
            }
            case "4" -> {
              json = new JSONObject();
              json.put("op", "030");
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
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
            }
            case "5" -> {
              json = new JSONObject();
              json.put("op", "040");
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
            }
            case "6" -> {
              json = new JSONObject();
              json.put("op", "050");
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
            }
            case "7" -> {
              json = new JSONObject();
              json.put("op", "020");
              System.out.println("Usuario:");
              user = stdIn.readLine();
              json.put("user", user);
              System.out.println("Token");
              token = stdIn.readLine();
              json.put("token", token);
              out.println(json.toString());
              System.out.println("Servidor enviou: " + json.toString());
            }
            default -> System.out.println("Digite uma opção valida");
          }
          System.out.println("Servidor retornou: " + in.readLine());
        } while (!option.equals("0"));
          
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }
}
