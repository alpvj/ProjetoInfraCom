import javax.sound.sampled.*;
import java.io.ByteArrayOutputStream;
import java.net.*;
import java.io.IOException;

public class sendAudio extends Thread {
    private InetAddress IPremetente;
    private int PortaRemetente;
    private RTPPacket pacote;
    private GUI_Cliente graphicUi;

    public sendAudio(RTPPacket pacote, InetAddress IPRemetente, int PortaRemetente, GUI_Cliente graphicUi) throws IOException {
        this.IPremetente = IPRemetente;
        this.PortaRemetente = PortaRemetente;
        this.pacote = pacote;
        this.graphicUi = graphicUi;
    }

    public void run() {

        boolean mute = this.graphicUi.muteMicrofone;

        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, true);
        TargetDataLine microphone;
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
            while(true) {
                mute = this.graphicUi.muteMicrofone;
                System.out.print("");
                while (!mute && !this.graphicUi.client_is_Off && !this.graphicUi.my_client_is_Off) {
                    mute = this.graphicUi.muteMicrofone;
                    numBytesRead = microphone.read(data, 0, CHUNK_SIZE);
                    out.write(data, 0, numBytesRead);
                    this.pacote.sendPacket(this.IPremetente, this.PortaRemetente, data);
                }
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