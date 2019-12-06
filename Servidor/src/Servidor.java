import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.net.*;
import java.time.*;


public class Servidor {
    public String IP1 = "-1", IP2 = "-1";
    public boolean Status1 = false, Status2 = false;

    public void atualizarStatus1(boolean status1, boolean status2){
        this.Status1 = status1;
        this.Status2 = status2;
    }

    public static void main(String[] args){
        Servidor server = new Servidor();
        GUI_Server GUI = new GUI_Server();
        try{
            DatagramSocket socket = new DatagramSocket(8008);
            //Receber cliente 1
            System.out.println("Esperando o primeiro se conectar");
            ServerSocket tmpSocket = new ServerSocket(8888);
            Socket socket1 = tmpSocket.accept();
            String IPAddressA = socket1.getRemoteSocketAddress().toString();
            server.IP1 = IPAddressA.substring(1).split(":")[0];
            System.out.println(server.IP1);
            GUI.IPCliente1.setText(server.IP1);
            //Receber cliente 2
            System.out.println("Esperando o segundo se conectar");
            Socket socket2 = tmpSocket.accept();
            String IPAddressB = socket2.getRemoteSocketAddress().toString();
            server.IP2 = IPAddressB.substring(1).split(":")[0];
            System.out.println(server.IP2);
            GUI.IPCliente2.setText(server.IP2);
            //Enviar os IPs para os clientes respectivos
            DataOutputStream IPtoClientB = new DataOutputStream(socket2.getOutputStream());
            DataOutputStream IPtoClientA = new DataOutputStream(socket1.getOutputStream());
            IPtoClientA.writeUTF(server.IP2);
            IPtoClientB.writeUTF(server.IP1);
            //Inicializar as threads
            ReceberStatus threadReceber = new ReceberStatus(server, GUI, socket);
            EnviarStatus threadEnviar = new EnviarStatus(server, GUI, socket);
            threadReceber.start(); threadEnviar.start();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}


class EnviarStatus extends Thread{
    Servidor servidor;
    DatagramSocket socket;
    GUI_Server GUI;

    public EnviarStatus(Servidor servidor, GUI_Server gui, DatagramSocket socket) {
        this.servidor = servidor;
        this.GUI = gui;
        this.socket = socket;
    }

    public void run(){
        //Enviar status2 pra IP1:Porta1 e status1 pra IP2:Porta2
        try {
            InetAddress address1 = InetAddress.getByName(this.servidor.IP1);
            InetAddress address2 = InetAddress.getByName(this.servidor.IP2);
            //DatagramSocket socket = new DatagramSocket(8008);

            byte[] sendData1, sendData2;
            while (true) {
                if (this.servidor.Status2) sendData1 = this.servidor.IP2.getBytes();
                else sendData1 = "OFF".getBytes();
                if (this.servidor.Status1) sendData2 = this.servidor.IP1.getBytes();
                else sendData2 = "OFF".getBytes();

                DatagramPacket sendPacket1 = new DatagramPacket(sendData1, sendData1.length, address1, 8008);
                DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, address2, 8008);


                this.socket.send(sendPacket1);
                this.socket.send(sendPacket2);
            }

        }catch (Exception e){
            System.out.println("Erro no EnviarStatus: " + e);
        }
    }
}

class ReceberStatus extends Thread{
    Servidor servidor;
    DatagramSocket socket;
    GUI_Server GUI;

    public ReceberStatus(Servidor servidor, GUI_Server gui, DatagramSocket socket) {
        this.servidor = servidor;
        this.GUI = gui;
        this.socket = socket;
    }

    public void setText(boolean status1, boolean status2) {
        if (status1)
            this.GUI.Cliente_1.setText("Online");
        else
            this.GUI.Cliente_1.setText("Offline");
        if (status2)
            this.GUI.Cliente_2.setText("Online");
        else
            this.GUI.Cliente_2.setText("Offline");
    }

    public void run(){
        try {
            InetAddress address1 = InetAddress.getByName(this.servidor.IP1);
            InetAddress address2 = InetAddress.getByName(this.servidor.IP2);
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            boolean status1 = true;
            boolean status2 = true;
            this.servidor.atualizarStatus1(status1,status2);
            long delta1 = 0;
            long delta2 = 0;
            Instant before1 = Instant.now();
            Instant before2 = Instant.now();
            Instant after1;
            Instant after2;
            this.socket.setSoTimeout(1000);
            while (true){
                try {
                    System.out.print("");
                    this.socket.receive(receivePacket);
                    if (receivePacket.getAddress().toString().equals(address1.toString())) {
                        status1 = true;
                        before1 = Instant.now();
                        after2 = Instant.now();
                        delta2 = Duration.between(before2, after2).toMillis();
                      //  System.out.println("Delta2: " + delta2);
                        if (delta2 > 1000) {
                            status2 = false;
                           // System.out.println("Cliente 2 off");
                        }
                        this.servidor.atualizarStatus1(status1,status2);
                        setText(status1,status2);
                    } else if (receivePacket.getAddress().toString().equals(address2.toString())){
                        status2 = true;
                        before2 = Instant.now();
                        after1 = Instant.now();
                        delta1 = Duration.between(before1, after1).toMillis();
                        //System.out.println("Delta1: " + delta1);
                        if (delta1 > 1000) {
                            status1 = false;
                           // System.out.println("Cliente 1 off");
                        }
                        this.servidor.atualizarStatus1(status1,status2);
                        setText(status1,status2);
                    }
                }
                catch (SocketTimeoutException e) {
                    status1 = false;
                    status2 = false;
                    this.servidor.atualizarStatus1(status1, status2);
                    setText(status1,status2);
                    this.socket.setSoTimeout(1000);
                }
            }
        }catch (Exception e){
            System.out.println("Erro no ReceberStatus: " + e);
        }
    }
}
