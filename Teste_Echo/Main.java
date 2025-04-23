import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        Cliente cliente = new Cliente();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite uma mensagem (ou 'sair' para encerrar):");

        while (true) {
            System.out.print("> ");
            String mensagem = scanner.nextLine();

            if (mensagem.equalsIgnoreCase("sair")) {
                System.out.println("Encerrando o programa.");
                break;
            }

            cliente.enviarMensagem(servidor, mensagem);
        }

        scanner.close();
    }
}
