import java.net.*;
import java.io.*;
import org.json.JSONObject;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
         serverSocket = new ServerSocket(porta);  // instancia o socket do servidor na porta especificada
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

         while ((inputLine = in.readLine()) != null) {
            System.out.println("Servidor recebeu: " + inputLine);
            
            JSONObject jsonRecebido = new JSONObject(inputLine);
            JSONObject jsonResposta = new JSONObject();

            if(jsonRecebido.isEmpty()){
              jsonResposta.put("message", "NULL JSON");
            }else if(jsonRecebido.isNull("op")){
              jsonResposta.put("message", "NULL \"op\"");
              break;
            }else{
              if(!jsonRecebido.isNull("op")){
                String usuario= jsonRecebido.getString("op");

                String regexOp = "[0-9]{3}$";  
                if(!Pattern.matches(regexOp, usuario)){
                  System.out.println("\"op\" Fora do padrão");
                }
              }
              if(!jsonRecebido.isNull("user")){
                String usuario= jsonRecebido.getString("user");
                
                String regexUser = "^[a-zA-Z0-9]{6,16}$";
                if(!Pattern.matches(regexUser, usuario)){
                  System.out.println("\"usuario\" Fora do padrão");
                }
              }
              if(!jsonRecebido.isNull("pass")){
                String senha= jsonRecebido.getString("pass");

                String regexPass = "^([a-zA-Z0-9]{6,32}$";
                if(!Pattern.matches(regexPass, senha)){
                  System.out.println("\"pass\" Fora do padrão");
                }
              }
              if(!jsonRecebido.isNull("nick")){
                String apelido= jsonRecebido.getString("nick");

                String regexNick = "^[a-zA-Z0-9]{6,16}$";
                if(!Pattern.matches(regexNick, apelido)){
                  System.out.println("\"nick\" Fora do padrão");
                }
              }
              jsonResposta.put("op", "012");
                           
            }

            System.out.println("Servidor enviou: " + jsonResposta.toString() + " " + clientSocket.getInetAddress().getHostAddress() + " : " + clientSocket.getPort());
            out.println(jsonResposta.toString());

            if (inputLine.toUpperCase().equals("BYE"))
               break;
         }

         out.close();
         in.close();
         clientSocket.close();
      } catch (IOException e) {
         System.err.println("Problema com Servidor de Communicacao!");
         System.exit(1);
      }
   }
}
