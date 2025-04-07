public class Cliente {

  public void enviarMensagem(Servidor servidor, String mensagem) {
      System.out.println("Cliente enviando: " + mensagem);
      String resposta = servidor.processarMensagem(mensagem);
      System.out.println("Cliente recebeu resposta: " + resposta);
  }
}
