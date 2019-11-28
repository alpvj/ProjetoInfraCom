import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RTPPacket {

    public int numSeq = 1; // Numero de sequencia não é Zero-Based.
    DatagramSocket socket;

    public RTPPacket(){

    }

    public void sendPacket(InetAddress IPRemetente,int PortaRemetente, byte[] data) throws IOException {
        RTP pacote = new RTP(this.numSeq, data);
        byte[] RTPPacket = pacote.getPacket();

        this.socket = new DatagramSocket();
        DatagramPacket pkt = new DatagramPacket( RTPPacket, RTPPacket.length,
                IPRemetente, PortaRemetente );
        this.socket.send(pkt);

        increaseSeqNumber();
    }

    public byte[] receber() throws IOException {
        byte[] receivedData = new byte[1024];
        DatagramPacket receivedPacket = new DatagramPacket(receivedData, receivedData.length);

        if(this.socket != null) {
            this.socket.receive(receivedPacket);
        }

        byte[] RTPPacket = receivedPacket.getData();

        byte[] audioData = new byte[1024]; // SETAR QUANTIDADE DE BYTES DE UM PACOTE DE AUDIO

        System.out.println(RTPPacket.length);

        for(int i=0;i<RTPPacket.length; i++){
            if(i > 12) /*ACESSANDO APENAS O DATA DO RTPPacket*/{
                audioData[i] = RTPPacket[i];
            }
        }

        return audioData;
}

    private void increaseSeqNumber(){
        this.numSeq++;
    }
}
