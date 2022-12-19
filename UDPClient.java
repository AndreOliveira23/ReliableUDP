import java.net.*;
import java.util.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class UDPClient {

    public static void main(String args[]) throws Exception {
        //Three-leg-handshaking com o servidor
        estabelecerConexao();//Conectando ao servidor

        Scanner teclado = new Scanner(System.in);//Scanner para receber input do usuário

        DatagramSocket datagrama = new DatagramSocket();//Criando socket para enviar mensagem ao servidor
        InetAddress ip = InetAddress.getLocalHost();//IP = 127.0.0.1 (Localhost)
        byte buffer[] = null;

        //Loop enquanto cliente não encerrar a conexão
        while (true) {
            System.out.println("Digite a mensagem ou digite 'exit' para encerrar a conexão: ");
            String input = teclado.nextLine();

            /*Convertendo String em array de Bytes
             *(Construtor do método "DatagramPacket" requer array de bytes como primeiro parâmetro)
             * */
            buffer = input.getBytes();

            //Criando datagrama para enviar os dados (mensagem) pela porta 1234
            DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, ip, 1234);

            //Usando Random pra definir qual será a simulação feita.
            Random random = new Random();

            int n = random.nextInt(7);

            /*O pacote é enviado para o servidor. No servidor é usado Random pra decidir qual simulação será feita
                O servidor envia mensagem para o cliente, e a partir disso, as simulações começam
             */

            switch(n){
                case 0:
                    System.out.println("Simulando roteadores com filas e diferentes tamanhos: \n");
                    System.out.println("Seu pacote saiu da máquina-cliente");
                    Thread.sleep(2000);
                    System.out.println("Pacote chegou no roteador R1, sendo o 5º na fila");
                    Thread.sleep(2400);
                    System.out.println("Roteador R1 enviou o pacote 1...");
                    Thread.sleep(2700);
                    System.out.println("Roteador R1 enviou o pacote 2...");
                    Thread.sleep(2600);
                    System.out.println("Roteador R1 enviou o pacote 3...");
                    Thread.sleep(2900);
                    System.out.println("Roteador R1 enviou o pacote 4...");
                    Thread.sleep(2700);
                    System.out.println("O Roteador R1 enviou o seu pacote para o Roteador R2!");
                    Thread.sleep(2800);
                    System.out.println("O pacote chegou no roteador R2, sendo o 3º na fila!");
                    Thread.sleep(2600);
                    System.out.println("Roteador R2 enviou o pacote 1...");
                    Thread.sleep(2300);
                    System.out.println("Roteador R2 enviou o pacote 2...");
                    Thread.sleep(2200);
                    System.out.println("O Roteador R2 enviou o seu pacote pacote para o servidor!");
                    datagrama.send(pacote);
                    break;

                case 1:
                    System.out.println("Simulando diferentes taxas de transmissão: \n");

                    int tamanhoDoPacote = random.nextInt(2000);
                    System.out.println("Tamanho do pacote: " + tamanhoDoPacote + " Bytes");
                    int[] taxas = new int[5];

                    for (int i = 0; i < 5; i++) {
                        taxas[i] = 1 + random.nextInt(100);
                        double tempoDeTransmissao = (((tamanhoDoPacote * 8) / (taxas[i])) * 0.001);
                        System.out.printf("Com uma taxa de tranmissão de %d Kbps, o pacote levaria %.3f segundos pra ser transmitido\n", +taxas[i], tempoDeTransmissao);
                    }
                    System.out.println("Enviando pacote ao servidor...");
                    datagrama.send(pacote);
                    break;

                case 2:
                    System.out.println("Simulando compartilhamento de banda: \n");

                    int tamanhoDaMensagem = input.length();
                    String pacote1 = input.substring(0, tamanhoDaMensagem / 2);
                    String pacote2 = input.substring((tamanhoDaMensagem / 2), tamanhoDaMensagem);
                    System.out.println("Pacote1 = " + pacote1);
                    System.out.println("Pacote2 = " + pacote2);
                    int x = random.nextInt(2);
                    if (x == 0) {
                        System.out.println("Houve colisão de RTS entre os pacotes. Aplicando back-off exponencial\nno pacote 2 e reenviando pacote 1 ");
                    } else {
                        System.out.println("Não houve colisão de RTS. CTS obtido para o pacote 1");
                    }
                    System.out.println("Enviando pacote 1....");
                    Thread.sleep(3000);
                    System.out.println("Pacote 1 enviado!!");
                    Thread.sleep(2000);
                    System.out.println("CTS obtido para o pacote 2");
                    Thread.sleep(3000);
                    System.out.println("Enviando pacote 2....");
                    Thread.sleep(1500);
                    System.out.println("Pacote 2 enviado!!");
                    datagrama.send(pacote);
                    break;

                case 3: //Simulação de perda de pacote (ocorre no servidor)

                    input = "63a0a911a4ee7 "+input;
                    buffer = input.getBytes();
                    //Criando datagrama para enviar os dados (mensagem) pela porta 1234
                    pacote = new DatagramPacket(buffer, buffer.length, ip, 1234);
                    datagrama.send(pacote);
                    break;

                case 4: //Simulação de cálculo de atraso de propagação (ocorre no servidor)
                    input = "63a0a911a4f29 "+input;
                    buffer = input.getBytes();
                    //Criando datagrama para enviar os dados (mensagem) pela porta 1234
                    pacote = new DatagramPacket(buffer, buffer.length, ip, 1234);
                    datagrama.send(pacote);
                    break;

                case 5: //Simulação de buffer de recepção (ocorre no servidor)
                    input = "63a0a911a4f62 "+input;
                    buffer = input.getBytes();
                    //Criando datagrama para enviar os dados (mensagem) pela porta 1234
                    pacote = new DatagramPacket(buffer, buffer.length, ip, 1234);
                    datagrama.send(pacote);
                    break;

                default:
                    datagrama.send(pacote);

            }

//==================================================================================================================================================

            //RECEBENDO ACK DO SERVIDOR
            DatagramSocket ack = new DatagramSocket(1235);//Conectando ao socket de envio e recebimento de ack
            byte[] receive = new byte[65535];//Criando array de bytes para usar no método "DatagramPacket"
            DatagramPacket ReceivePacote = null;

            ReceivePacote = new DatagramPacket(receive, receive.length);//Colocando conteudo do ack na variável
            ack.receive(ReceivePacote);//Recebendo ack via socket 1235
            String texto = new String(ReceivePacote.getData(), StandardCharsets.UTF_8);//Decodificando texto enviado pelo servidor com UTF-8

            //Printando apenas as mensagems (tirar o método trim() faz com que
            //caracteres extras não pertinentes ao ocnteúdo da mensagem sejam impressos

            if (texto.contains("perdido")) { //Se o texto contém a palavra "perdido", signfica que é simulação de pacote perdido, então só precisa printar a mensagem enviada pelo servidor
                System.out.println(texto.trim() + "\n===============================================================");
                ack.close();
            } else {
                System.out.println("Ack Recebido! A mensagem '" + texto.trim() + "' chegou no servidor com sucesso!");
                System.out.println("===============================================================");

                /*Fechando conexão para liberar a porta
                 *Toda vez que o servidor enviar um ack para o cliente, usa-se a mesma porta;
                 * Se a conexão não for fechada, a classe lança uma exceção, indicando que a porta ainda está sendo usada
                 */
                ack.close();
            }
                // Sai do loop caso o usuário digite "bye"
                if (input.equals("exit")) break;
        }
    }

    public static void estabelecerConexao() throws Exception {
        Thread.sleep(1500);
        System.out.println("Tentando conectar ao servidor....");
        DatagramSocket datagrama = new DatagramSocket();
        InetAddress ip = InetAddress.getLocalHost();
        String RTS = "Tentando conectar ao servidor....";
        byte buffer[] = RTS.getBytes();
        DatagramPacket pacote = new DatagramPacket(buffer, buffer.length, ip, 1272);
        datagrama.send(pacote);
        //Recebendo ACK do servidor
        DatagramSocket ack = new DatagramSocket(1240);//Conectando ao socket de envio e recebimento de ack
        byte[] receive = new byte[65535];//Criando array de bytes para usar no método "DatagramPacket"
        DatagramPacket ReceivePacote = null;
        ReceivePacote = new DatagramPacket(receive, receive.length);//Colocando conteudo do ack na variável
        ack.receive(ReceivePacote);//Recebendo ack via socket 1235
        String CTS = new String(ReceivePacote.getData(), StandardCharsets.UTF_8);
        if (CTS.contains("estabelecida")) Thread.sleep(500);  System.out.println(CTS.trim());
    }
}
