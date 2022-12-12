import java.io.IOException;
import java.net.*;

public class UDPServer {
    public static void main(String[] args) throws IOException{

        //Criando socket para receber dados da porta 1234 (usada pelo cliente para envio)
        DatagramSocket datagrama = new DatagramSocket(1234);

        //Criando array de bytes para usar no método "DatagramPacket"
        byte[] receive = new byte[65535];

        DatagramPacket ReceivePacote = null;
        while (true) {

           //Criando pacote para receber os dados
            ReceivePacote = new DatagramPacket(receive, receive.length);

            //Recebendo dados da porta 1234 e colocando no array de bytes
            datagrama.receive(ReceivePacote);

            System.out.println("Mensagem recebida!!> " + data(receive)+"\nEnviando ACK para o cliente...");

            //Enviando ACK
            DatagramSocket pacote = new DatagramSocket();//Cria socket para enviar dados
            InetAddress ip = InetAddress.getLocalHost();
            String mensagem = String.valueOf(data(receive));


            DatagramPacket ack = new DatagramPacket(mensagem.getBytes(), mensagem.length(), ip, 1235);

            pacote.send(ack);//Enviando ACK de fato
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
}
