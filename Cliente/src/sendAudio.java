import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.net.*;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.IOException;

public class sendAudio extends Thread {
    private InetAddress IPremetente;
    private int PortaRemetente;
    private RTPPacket pacote;

    public sendAudio(RTPPacket pacote, InetAddress IPRemetente, int PortaRemetente) throws IOException {
        this.IPremetente = IPRemetente;
        this.PortaRemetente = PortaRemetente;
        this.pacote = pacote;
    }

    public void run() {

        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
        TargetDataLine microphone;
        //SourceDataLine speakers;
        try {
            microphone = AudioSystem.getTargetDataLine(format);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int numBytesRead;
            int CHUNK_SIZE = 1024;
            byte[] data = new byte[microphone.getBufferSize() / 5];
            microphone.start();


//            DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, format);
//            speakers = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
//            speakers.open(format);
//            speakers.start();


            // Configure the ip and port
            //String hostname = "localhost";
            //int port = 5555;

            //InetAddress address = InetAddress.getByName(hostname);
            //DatagramSocket socket = new DatagramSocket();
            byte[] buffer = new byte[1024];
            for (; ; ) {
                numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                //  bytesRead += numBytesRead;
                // write the mic data to a stream for use later
                out.write(data, 0, numBytesRead);
                // write mic data to stream for immediate playback
                //speakers.write(data, 0, numBytesRead);
                //DatagramPacket request = new DatagramPacket(data, numBytesRead, address, port);
                //socket.send(request);
                this.pacote.sendPacket(IPremetente, PortaRemetente, data);
            }

        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}