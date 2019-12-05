import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
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
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
            SourceDataLine speakers;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            int bytesRead = 0;
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();
            try {
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];

                while (true) {

                    byte[] buffer = new byte[1024];
                    buffer = this.pacote.receber();

                    out.write(buffer, 0, buffer.length);
                    speakers.write(buffer, 0, buffer.length);;
                    String quote = buffer.toString();
//                   System.out.println(quote);
                }

            } catch (Exception e) {
                System.out.println("Error: " + e);

            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}