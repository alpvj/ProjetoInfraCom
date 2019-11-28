import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;


public class RTP {
    public long cargaUtil = 0; // Tipo de codificação usada, 0 = PCM.
    public long numSequencia;   // Numero de sequencia do pacote enviado
    public long timestamp;  // Momento em que o pacote foi enviado
    public long SSRC;

    public byte[] data; // Informações a serem repassadas

    public byte [] packet = new byte[0];  // Pacote a ser encapsulado em UDP.

    public RTP(long numSequencia, byte[] data) throws IOException {
        this.numSequencia = numSequencia;
        this.data = data;

        // Cria o Timestamp
        Random RandomNumGenerator = new Random();
        short RandomOffset = (short) Math.abs ( RandomNumGenerator.nextInt() & 0x000000FF) ;
        this.timestamp =  (new Date().getTime() + RandomOffset);

        // CRIA SSRC
        long SSRC = Math.abs( RandomNumGenerator.nextInt() ) ;
        this.SSRC = SSRC;

        // Bytes do Pacote
        byte[] VPXCC = {(byte) 0x80};
        byte[] bCargaUtil = {(byte)((0x0 << 8) | (byte) this.cargaUtil)};
        byte[] bNumSeq = longToBytes(this.numSequencia, 2);
        byte[] bTimestamp = longToBytes(this.timestamp, 4);
        byte[] bSSRC = longToBytes(SSRC, 4);


        appendBytes(VPXCC); // 1 byte
        appendBytes(bCargaUtil); // 1 byte
        appendBytes(bNumSeq); // 2 bytes
        appendBytes(bTimestamp); // 4 bytes
        appendBytes(bSSRC); // 4 bytes
        appendBytes(data);
    }

    public byte[] getPacket(){
        return this.packet;
    }

    private byte [] longToBytes(long num, int byteSize){
        byte byteArray[] = new byte [byteSize];

        for ( int i=byteSize-1; i>=0; i--)
        {
            byteArray [ i ] = (byte) num;
            num = num >> 8;
        }
        return byteArray;
    }

    private void appendBytes(byte[] toAppend) throws IOException {
        byte [] packet = this.packet;
        byte [] bytesToAppend = toAppend;

        this.packet = ByteBuffer.allocate(packet.length + bytesToAppend.length).put(packet).put(bytesToAppend).array();
//        System.out.println(Arrays.toString(this.packet));
    }

}
