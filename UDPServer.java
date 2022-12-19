import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class UDPServer {

    public static void main(String[] args) throws Exception {

        //Three-leg-handshaking com o cliente
        estabelecerConexao();//Conectando ao cliente

        //Criando socket para receber mensagens na porta 1234
        DatagramSocket datagrama = new DatagramSocket(1234);

        //Criando array de bytes para usar no método "DatagramPacket"
        byte[] receive = new byte[65535];

        while (true) { //Mantém a conexão ativa

            //Criando pacote para receber os dados
            DatagramPacket ReceivePacote = new DatagramPacket(receive, receive.length);

            //Recebendo dados da porta 1234 e colocando no array de bytes
            datagrama.receive(ReceivePacote);

            //Montando ACK para enviar para o cliente
            DatagramSocket pacote = new DatagramSocket();//Cria socket para enviar dados
            InetAddress ip = InetAddress.getLocalHost();
            String mensagem = String.valueOf(data(receive));
            DatagramPacket ack = null;

            //Colocando cada palavra da mensagem numa posição do array
            //A posicão 0 contém o código associado. Definido pelos cases 3,4 e 5 do UDPClient
            String[] codigoDaSimulacao = mensagem.split(" ");

            if(codigoDaSimulacao[0].contains("63a0a911a4ee7")) {
                System.out.println("Simulando de perda de pacote: \n");
                System.out.println("===============================================================");
                mensagem = "TIMEOUT!! ACK não recebido. Pacote perdido, precisa reenviar...";//Mensagem a ser enviada para o cliente
                ack = new DatagramPacket(mensagem.getBytes(), mensagem.length(), ip, 1235);
                Thread.sleep(4000);
                pacote.send(ack);//Enviando ACK de fato
                receive = new byte[65535];//Limpando o buffer


            } else if (codigoDaSimulacao[0].contains("63a0a911a4f29")) {

                System.out.println("Simulando de atraso de propagação: \n");
                Random random = new Random();
                int distancia = random.nextInt(100);
                float velocidade = random.nextFloat() * 10;
                System.out.println("Distância: " + distancia + " km"); // 10km = 10000 m
                System.out.printf("Velocidade: %.2f ms", velocidade);
                System.out.println("\nAtraso de propagação (d/v) = ");
                System.out.print("Mensagem recebida: ");
                String[] novaMensagem = new String[mensagem.length()-1];
                for(int i=1;i<codigoDaSimulacao.length;i++) { //Loop passando pela mensagem completa (sem o código, que está no índice (0)
                    novaMensagem[i-1] = codigoDaSimulacao[i]+" ";//String "nova mensagem" recebe a mensagem em si, sem o código)
                    System.out.print(codigoDaSimulacao[i]+" ");
                }
                System.out.println("\nEnviando ACK para o cliente....");
                System.out.println("===============================================================");
                String mensagemReal = "";
                for(int i = 0;i<codigoDaSimulacao.length;i++) mensagemReal += novaMensagem[i]+"";//Obtendo mensagem real para enviar ao cliente
                mensagemReal = mensagemReal.replaceAll("null","");//Substituindo nulls por espaços vazios
                ack = new DatagramPacket(mensagemReal.getBytes(), mensagemReal.length(), ip, 1235);
                Thread.sleep(3000);
                pacote.send(ack);//Enviando ACK de fato
                pacote.close();
                receive = new byte[65535];//Limpando o buffer
                continue;

            } else if (codigoDaSimulacao[0].contains("63a0a911a4f62")) {

                System.out.println("Simulando de buffer de recepção + lentidão para leitura: ");
                String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit " + data(receive);
                String[] bufferSimulado = text.split(" ");
                for (int i = 0; i < bufferSimulado.length; i++) {
                    System.out.println("Buffer[" + i + "] = " + bufferSimulado[i]);
                    Thread.sleep(3000);
                }
                System.out.print("Mensagem recebida: ");
                String[] novaMensagem = new String[mensagem.length() - 1];
                for (int i = 9; i < bufferSimulado.length; i++){
                    novaMensagem[i-9] = codigoDaSimulacao[i-8]+" ";
                    System.out.print(bufferSimulado[i] + " ");
                }
                String mensagemReal = "";
                for(int i = 0;i<codigoDaSimulacao.length;i++) mensagemReal += novaMensagem[i]+"";//Obtendo mensagem real para enviar ao cliente
                mensagemReal = mensagemReal.replaceAll("null","");//Substituindo nulls por espaços vazios

                System.out.println("\nEnviando ACK para o cliente....");
                System.out.println("===============================================================");
                mensagem = String.valueOf(data(receive));
                ack = new DatagramPacket(mensagemReal.getBytes(),mensagemReal.length(),ip,1235);
                Thread.sleep(3000);
                pacote.send(ack);//Enviando ACK de fato
                pacote.close();
                receive = new byte[65535];
                continue;

            } else {

                System.out.println("Mensagem recebida: " + data(receive));
                System.out.println("Enviando ACK para o cliente....");
                System.out.println("===============================================================");
                mensagem = String.valueOf(data(receive));
                ack = new DatagramPacket(mensagem.getBytes(), mensagem.length(), ip, 1235);
                Thread.sleep(1500);
                pacote.send(ack);
                pacote.close();
                receive = new byte[65535];

            }

            pacote.close();//Fechando conexão com porta 1235 para evitar exception de porta sendo usada;

            //Para de executar caso o cliente envie 'exit'
            if (data(receive).toString().equals("exit")) {
                System.out.println("Cliente encerrou a conexão!");
                break;
            }

            // Limpa o buffer depois de enviar o ACK
            receive = new byte[65535];
        }
    }

    //Método para converter um array de bytes em string
    public static StringBuilder data(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0){
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }

    public static void estabelecerConexao() throws Exception{
        //Recebendo RTS do cliente
        DatagramSocket RTS = new DatagramSocket(1272);
        byte[] received = new byte[65535];
        DatagramPacket RTSCliente = new DatagramPacket(received, received.length);
        RTS.receive(RTSCliente);
        Thread.sleep(3000);
        System.out.println("Pedido de conexão recebido! \nEnviando CTS ao cliente para estabelecer a conexão...");
        System.out.println("===============================================================");
        //Enviando CTS
        DatagramSocket socket = new DatagramSocket();//Cria socket para enviar dados
        InetAddress ipv4 = InetAddress.getLocalHost();
        String SynAck = "Conexão estabelecida com sucesso!\n===============================================================";
        DatagramPacket CTS = new DatagramPacket(SynAck.getBytes(), SynAck.length(), ipv4, 1240);
        socket.send(CTS);//Enviando ACK de fato
    }
}
