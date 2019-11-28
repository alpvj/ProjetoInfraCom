import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class ReceiveAudio extends Thread {

    private RTPPacket pacote;

    public ReceiveAudio(RTPPacket pacote) throws IOException {
        this.pacote = pacote;
    }

    public void run() {
        try {
            while (true) {
                this.pacote.receber();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}