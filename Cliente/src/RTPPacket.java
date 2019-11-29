import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RTPPacket {

    public int numSeq = 1; // Numero de sequencia não é Zero-Based.
    DatagramSocket socket;

    public RTPPacket(int PortaDestinatário) throws IOException {
        this.socket = new DatagramSocket(PortaDestinatário);
    }

    public void sendPacket(InetAddress IPDestinatário ,int PortaDestinatário, byte[] data) throws IOException {
        RTP pacote = new RTP(this.numSeq, data);
        byte[] RTPPacket = pacote.getPacket();

        DatagramPacket pkt = new DatagramPacket( RTPPacket, RTPPacket.length, IPDestinatário, PortaDestinatário);
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
