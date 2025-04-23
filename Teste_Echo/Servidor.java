public class Servidor {

  public String processarMensagem(String mensagem) {
      // Simula um "echo" retornando a mensagem com caracteres em uppercase
      System.out.println("Servidor recebeu: " + mensagem);
      String resposta = mensagem.toUpperCase();
      return resposta;
  }
}