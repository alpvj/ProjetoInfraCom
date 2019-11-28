import java.io.DataOutput;
import java.io.DataOutputStream;
import java.net.*;
import java.security.Guard;

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
            //Receber cliente 1
            System.out.println("Esperando o primeiro se conectar");
            ServerSocket tmpSocket = new ServerSocket(8888);
            Socket socket1 = tmpSocket.accept();
            String IPAddressA = socket1.getRemoteSocketAddress().toString();
            server.IP1 = IPAddressA.substring(1).split(":")[0];
            System.out.println(server.IP1);
            GUI.IP_Cliente1.setText(server.IP1);
            //Receber cliente 2
            System.out.println("Esperando o segundo se conectar");
            Socket socket2 = tmpSocket.accept();
            String IPAddressB = socket2.getRemoteSocketAddress().toString();
            server.IP2 = IPAddressB.substring(1).split(":")[0];
            System.out.println(server.IP2);
            GUI.IP_Cliente2.setText(server.IP2);
            //Enviar os IPs para os clientes respectivos
            DataOutputStream IPtoClientB = new DataOutputStream(socket2.getOutputStream());
            DataOutputStream IPtoClientA = new DataOutputStream(socket1.getOutputStream());
            IPtoClientA.writeUTF(server.IP2);
            IPtoClientB.writeUTF(server.IP1);
            //Inicializar as threads
            ReceberStatus threadReceber = new ReceberStatus(server, GUI);
            EnviarStatus threadEnviar = new EnviarStatus(server, GUI);
            threadReceber.start(); threadEnviar.start();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}


class EnviarStatus extends Thread{
    Servidor servidor;
    GUI_Server GUI;

    public EnviarStatus(Servidor servidor, GUI_Server gui) {
        this.servidor = servidor;
        this.GUI = gui;
    }

    public void run(){
        //Enviar status2 pra IP1:Porta1 e status1 pra IP2:Porta2
        try {
            InetAddress address1 = InetAddress.getByName(this.servidor.IP1);
            InetAddress address2 = InetAddress.getByName(this.servidor.IP2);
            DatagramSocket socket = new DatagramSocket(8008);

            byte[] sendData1, sendData2;
            while (true) {
                if (this.servidor.Status2) sendData1 = this.servidor.IP2.getBytes();
                else sendData1 = "OFF".getBytes();
                if (this.servidor.Status1) sendData2 = this.servidor.IP1.getBytes();
                else sendData2 = "OFF".getBytes();

                DatagramPacket sendPacket1 = new DatagramPacket(sendData1, sendData1.length, address1, 8008);
                DatagramPacket sendPacket2 = new DatagramPacket(sendData2, sendData2.length, address2, 8008);


                socket.send(sendPacket1);
                socket.send(sendPacket2);
            }

        }catch (Exception e){
            System.out.println("Erro no EnviarStatus: " + e);
        }
    }
}

class ReceberStatus extends Thread{
    Servidor servidor;
    GUI_Server GUI;

    public ReceberStatus(Servidor servidor, GUI_Server gui) {
        this.servidor = servidor;
        this.GUI = gui;
    }

    public void run(){
        try {
            InetAddress address1 = InetAddress.getByName(this.servidor.IP1);
            InetAddress address2 = InetAddress.getByName(this.servidor.IP2);
            while (true){
                boolean status1 = address1.isReachable(200);
                boolean status2 = address2.isReachable(200);
                this.servidor.atualizarStatus1(status1, status2);
                if (status1)
                    this.GUI.Cliente1.setText("Cliente 1: Online");
                else
                    this.GUI.Cliente1.setText("Cliente 1: Offline");
                if (status2)
                    this.GUI.Cliente2.setText("Cliente 2: Online");
                else
                    this.GUI.Cliente2.setText("Cliente 2: Offline");
            }
        }catch (Exception e){
            System.out.println("Erro no ReceberStatus: " + e);
        }
    }
}
