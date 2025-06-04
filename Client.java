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
        String userInput;
        String option;

        JSONObject json;
        String user;
        String pass;
        String nick;
        String token;
        do {
          System.out.print("0 = sair, 1 = Echo, 2 = Regitrar\n");
          option = stdIn.readLine();
          switch (option){
            case "0" -> {
              break;
            }
            case "1" -> {
              // echo_function(out, in);
              System.out.println("Conectado. Digite (\"bye\" para sair)");
              System.out.print("Digite: ");
              while ((userInput = stdIn.readLine()) != null) {
                  //out.println(userInput);
                  json = new JSONObject();
                  json.put("message", userInput);
                  
                  out.println(json.toString());
                  
                  // end loop
                  if (userInput.toUpperCase().equals("BYE"))
                      break;
                  
                  System.out.println("Servidor retornou: " + in.readLine());
                  System.out.print("Digite: ");
              } 
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
            }
            case "3" -> {
              json = new JSONObject();
              json.put("op", "020");
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
            }
            default -> System.out.println("Digite uma opção valida");
          }
        } while (!option.equals("0"));
          
        out.close();
        in.close();
        stdIn.close();
        socket.close();
    }

    // public static void echo_function(PrintWriter out,BufferedReader in){
    //   BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    //   String userInput;
    //   System.out.print("Digite: ");
    //   while ((userInput = stdIn.readLine()) != null) {
    //     //out.println(userInput);
      
    //     JSONObject json = new JSONObject();
    //     json.put("message", userInput);
        
    //     out.println(json.toString());
        
    //     // end loop
    //     if (userInput.toUpperCase().equals("BYE"))
    //         break;

    //     System.out.println("Servidor retornou: " + in.readLine());
    //     System.out.print("Digite: ");
    // }
    // }
}
