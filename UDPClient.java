import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPClient {
    public static void main(String args[]) throws IOException{
        Scanner teclado = new Scanner(System.in);

        //Criando socket para enviar dados (mensagem)
        DatagramSocket datagrama = new DatagramSocket();

        InetAddress ip = InetAddress.getLocalHost();
        byte buffer[] = null;

        //Loop enquanto cliente não enviar "bye"
        while (true){
            String input = teclado.nextLine();

            /*Convertendo String em array de Bytes
            *(Construtor do método "DatagramPacket" requer array de bytes como primeiro parâmetro)
            * */
            buffer = input.getBytes();

           //Criando datagrama para enviar os dados (mensagem) pela porta 1234
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, ip, 1234);

            //Enviando dados
            datagrama.send(pacote);

            //Recebendo ACK
            DatagramSocket ack = new DatagramSocket(1235);//Conectando ao socket de envio e recebimento de ack
            byte[] receive = new byte[65535];//Criando array de bytes para usar no método "DatagramPacket"
            DatagramPacket ReceivePacote = null;

            ReceivePacote = new DatagramPacket(receive, receive.length);//Colocando conteudo do ack na variável
            ack.receive(ReceivePacote);//Recebendo ack via socket 1235
            String texto = new String(ReceivePacote.getData(), StandardCharsets.UTF_8);//Decodificando com UTF-8

            /*
            /Printando apenas as mensagems (tirar o método trim() faz com que
            caracteres extras não pertinentes ao ocnteúdo da mensagem sejam impressos
             */
            System.out.println("Ack Recebido! A mensagem '"+texto.trim()+"' chegou no servidor com sucesso!");

            /*Fechando conexão para liberar a porta
            *->Toda vez que o servidor enviar um ack para o cliente, usa-se a mesma porta;
            * Se a conexão não for fechada, a classe lança uma exceção, indicando que a porta ainda está sendo usada
             */
            ack.close();

            // Sai do loop caso o usuário digite "bye"
            if (input.equals("bye")) break;
        }
    }
}
