import javax.sound.sampled.*;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class sendAudio extends Thread {
    private InetAddress IPremetente;
    private int PortaRemetente;
    private RTPPacket pacote;

    public sendAudio(RTPPacket pacote, InetAddress IPRemetente,int PortaRemetente) throws IOException {
        this.IPremetente = IPRemetente;
        this.PortaRemetente = PortaRemetente;
        this.pacote = pacote;
    }

    public void run() {

        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);

        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);

        try {
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetLine.open(format);
            targetLine.start();

            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceLine.open(format);
            sourceLine.start();

            int numBytesRead;
            byte[] targetData = new byte[targetLine.getBufferSize() / 5];

            while (true) {
                numBytesRead = targetLine.read(targetData, 0, targetData.length);

                if (numBytesRead == -1)	break;

                sourceLine.write(targetData, 0, numBytesRead);

                this.pacote.sendPacket(this.IPremetente, this.PortaRemetente,targetData);
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }


    }
}