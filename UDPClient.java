import java.net.*;
import java.util.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class UDPClient2 {
    public static void main(String args[]) throws IOException{
        Scanner teclado = new Scanner(System.in);//Input do usuário

        DatagramSocket datagrama = new DatagramSocket();//Criando socket para enviar dados (mensagem)

        InetAddress ip = InetAddress.getLocalHost();//IP = 127.0.0.1 (Localhost)

        byte buffer[] = null;

        //Loop enquanto cliente não encerrar a conexão
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
            Random random = new Random();
            /*
            /Printando apenas as mensagems (tirar o método trim() faz com que
            caracteres extras não pertinentes ao ocnteúdo da mensagem sejam impressos
             */
            if(texto.contains("perdido")){//Simulação de perda de pacote
                System.out.println("TIMEOUT!! ACK não recebido. Pacote perdido, precisa reenviar...");
            } else if (texto.contains("Calculando")) {

                int n = random.nextInt(3);
                int distancia = random.nextInt(100);
                float velocidade = random.nextFloat()*10;
                System.out.println("Distancia: "+distancia+" km"); // 10km = 10000 m
                System.out.printf("Velocidade: %.2f ms",velocidade);
                System.out.println("\nAtraso de propagação (d/v) = ");

            } else if (texto.contains("taxas")) {

                int tamanhoDoPacote = random.nextInt(2000);
                System.out.println("Tamanho do pacote: "+tamanhoDoPacote+" Bytes");
                int[] taxas = new int[5];

                for(int i=0;i<5;i++){
                    taxas[i] = random.nextInt(100);
                    double tempoDeTransmissão = (((tamanhoDoPacote*8)/(taxas[i]))*0.001);
                    System.out.printf("Com uma taxa de tranmissão de %d Kbps, o pacote levaria %.3f segundos pra ser transmitido\n",+taxas[i],tempoDeTransmissão);
                }

            } else {
                System.out.println("Ack Recebido! A mensagem '" + texto.trim() + "' chegou no servidor com sucesso!");
            }
            
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
