import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class UDPServer {

    public static void main(String[] args) throws Exception {
        //Three-leg-handshaking com o cliente
        estabelecerConexao();

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

            Random random = new Random();
            int n = random.nextInt(6);

            switch(n){
                case 0:
                    System.out.println("Simulação de perda de pacote: \n");
                    System.out.println("===============================================================");
                    mensagem = "ACK não recebido. Pacote perdido, precisa reenviar...";
                    ack = new DatagramPacket(mensagem.getBytes(), mensagem.length(), ip, 1235);
                    pacote.send(ack);//Enviando ACK de fato
                    receive = new byte[65535];//Limpando o buffer
                    continue;

                case 1:
                    System.out.println("Simulação de atraso de propagação: \n");
                    System.out.println("Enviando ACK para o cliente....");
                    System.out.println("===============================================================");
                    Thread.sleep(3000);
                    mensagem = "ACK recebido! Calculando atraso de propagação do pacote....";
                    ack = new DatagramPacket(mensagem.getBytes(), mensagem.length(), ip, 1235);
                    pacote.send(ack);//Enviando ACK de fato
                    receive = new byte[65535];//Limpando o buffer
                    continue;

                case 2:
                    System.out.println("Simulação de roteador com fila e diferentes tamanhos: \n");
                    System.out.println("===============================================================");

                case 3:
                    System.out.println("Simulação de diferentes taxas de transmissão: \n");
                    Thread.sleep(3000);
                    System.out.println("Enviando ACK para o cliente....");
                    System.out.println("===============================================================");
                    mensagem = "ACK recebido! Simulando entregas com diferentes taxas e transmissão....";
                    ack = new DatagramPacket(mensagem.getBytes(),mensagem.length(),ip,1235);
                    pacote.send(ack);
                    receive = new byte[65535];
                    continue;

                case 4:
                    System.out.println("Simulação de compartilhamento de banda");
                    String input = String.valueOf(data(receive));
                    int tamanhoDaMensagem = input.length();
                    String pacote1 = input.substring(0,tamanhoDaMensagem/2);
                    String pacote2 = input.substring((tamanhoDaMensagem/2),tamanhoDaMensagem);
                    System.out.println("Pacote1 = "+pacote1);
                    System.out.println("Pacote2 = "+pacote2);
                    int x = random.nextInt(2);
                    if(x == 0) {
                        System.out.println("Houve colisão de RTS entre os pacotes. Aplicando back-off exponencial no pacote 2 e reenviando pacote 1 ");

                    }else {
                        System.out.println("Não houve colisão de RTS. CTS obtido para o pacote 1");
                        System.out.println("Enviando pacote 1....");
                        Thread.sleep(3000);
                        System.out.println("Pacote 1 enviado!!");
                        Thread.sleep(2000);
                        System.out.println("CTS obtido para o pacote 2");
                        Thread.sleep(3000);
                        System.out.println("Enviando pacote 2....");
                        Thread.sleep(1500);
                        System.out.println("Pacote 2 enviado!!");
                        Thread.sleep(2000);
                        System.out.println("Mensagem completa> "+pacote1+pacote2);
                    }
                    System.out.println("Enviando ACK para o cliente....");
                    System.out.println("===============================================================");
                    Thread.sleep(5000);
                    mensagem = "ACK Recebido! Simulação de compartilhamento de banda aconteceu no servidor!";
                    ack = new DatagramPacket(mensagem.getBytes(),mensagem.length(),ip,1235);
                    pacote.send(ack);
                    receive = new byte[65535];
                    continue;

                case 5:
                    System.out.println("Simulação de buffer de recepção + lentidão para leitura");
                    String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit "+data(receive);
                    String[] bufferSimulado = text.split(" ");
                    for (int i=0;i<bufferSimulado.length;i++){
                        System.out.println("Buffer["+i+"] = "+bufferSimulado[i]);
                        Thread.sleep(3000);
                    }
                    System.out.println("Mensagem recebida!!> " + data(receive)+"");
                    System.out.println("Enviando ACK para o cliente....");
                    System.out.println("===============================================================");
                    mensagem = "6-ACK Recebido! Simulação de buffer!66";
                    ack = new DatagramPacket(mensagem.getBytes(),mensagem.length(),ip,1235);
                    pacote.send(ack);
                    continue;
            }

            pacote.close();//Fechando conexão com porta 1235 para evitar exception de porta sendo usada;

            //Para de executar caso o cliente envie 'bye'
            if (data(receive).toString().equals("bye")) {
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
    DatagramSocket RTS = new DatagramSocket(1232);
    byte[] received = new byte[65535];
    DatagramPacket RTSCliente = new DatagramPacket(received, received.length);
    RTS.receive(RTSCliente);
    Thread.sleep(3000);
    System.out.println("Pedido de conexão recebido! \nEnviando CTS ao cliente para estabelecer a conexão...");
    System.out.println("===============================================================");
    //Enviando CTS
    DatagramSocket socket = new DatagramSocket();//Cria socket para enviar dados
    InetAddress ipv4 = InetAddress.getLocalHost();

    String SynAck = "Conexão estabelecida!";
    DatagramPacket CTS = new DatagramPacket(SynAck.getBytes(), SynAck.length(), ipv4, 1240);
    socket.send(CTS);//Enviando ACK de fato
    }
}
