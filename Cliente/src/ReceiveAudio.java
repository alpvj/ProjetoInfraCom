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
            //TargetDataLine microphone;
            SourceDataLine speakers;

            //microphone = AudioSystem.getTargetDataLine(format);

            //DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            //microphone = (TargetDataLine) AudioSystem.getLine(info);
            //microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            //byte[] data = new byte[microphone.getBufferSize() / 5];
            //microphone.start();

            int bytesRead = 0;
            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
            speakers.open(format);
            speakers.start();

            //String hostname = "G3C38";
            //int port = 5555;

            try {
                //InetAddress address = InetAddress.getByName(hostname);
                // DatagramSocket socket = new DatagramSocket();

               //  DatagramSocket serverSocket = new DatagramSocket(8888);
                byte[] receiveData = new byte[1024];
                byte[] sendData = new byte[1024];

                while (true) {

                    byte[] buffer = new byte[1024];
                    //DatagramPacket response = new DatagramPacket(buffer, buffer.length);
                    buffer = this.pacote.receber();

                    out.write(buffer, 0, buffer.length);
                    speakers.write(buffer, 0, buffer.length);
                    //String quote = new String(buffer, 0, response.getLength());
                    //speakers.write(microphone, 0, numBytesRead);
                    String quote = buffer.toString();
                    System.out.println(quote);

                    //System.out.println();
                    //Thread.sleep(10000);
                }

            } catch (Exception e) {
                System.out.println("Error: " + e);

            }
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}