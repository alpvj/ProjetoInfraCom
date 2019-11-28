import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) throws IOException {
        int port = 8888;
        String servidorIP = "G3C04";
        InetAddress adress = InetAddress.getByName(servidorIP);
        DatagramSocket clientSocket = new DatagramSocket(35353); // msgs do cliente
        DatagramSocket statusSocket = new DatagramSocket(8008); // msgs de status

        Socket socket = new Socket(adress, port); // inicia conexão com servidor
        GUI_Cliente graphicUi = new GUI_Cliente (clientSocket);
        System.out.println("Conectei");
        DataInputStream entrada = new DataInputStream(socket.getInputStream()); // pega mensagem do servidor
        String info = entrada.readUTF();
        String clientIP = info;

        RTPPacket rtpPacket = new RTPPacket();
        sendAudio sendAudio = new sendAudio(rtpPacket,InetAddress.getByName(clientIP),35353);
        ReceiveAudio receiveAudio = new ReceiveAudio(rtpPacket);


        System.out.println("IP do outro cliente: " + clientIP);
        graphicUi.setIp(clientIP);
        graphicUi.init();

        Receive receive = new Receive(clientSocket, graphicUi);
        Status status = new Status(statusSocket, graphicUi);
        EnviarMensagem enviar = new EnviarMensagem(clientIP, graphicUi, clientSocket, "Andre");

        receiveAudio.start();
        sendAudio.start();
        receive.start();
        status.start();
        enviar.start();

    }
}

class Status extends Thread {
    private DatagramSocket statusSocket;
    private GUI_Cliente graphicGui;

    public Status(DatagramSocket skt, GUI_Cliente gui) {
        this.statusSocket = skt;
        this.graphicGui = gui;
    }

    public void run() {
        try {
            while (true) {
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                this.statusSocket.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                msg = msg.trim();//Recebe o IP se estiver ON e "OFF" caso esteja OFF
                this.graphicGui.setClientOff(msg);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}

class EnviarMensagem extends Thread{
    private String IP;
    private GUI_Cliente GUI;
    private DatagramSocket socket;
    private String Nome;

    public EnviarMensagem(String ip, GUI_Cliente gg, DatagramSocket skt, String nome){
        this.IP = ip;
        this.GUI = gg;
        this.socket = skt;
        this.Nome = nome;
    }

    public void run() {
        while(true){
            //Enquanto o Cliente2 esta ON e TEM elementos na fila de envio, envie
            this.GUI.FilaSize.setText(this.GUI.filaDeEnvio.size()+"");
            while(this.GUI.client_is_Off == false && this.GUI.filaDeEnvio.isEmpty() == false){
                String msg = this.GUI.filaDeEnvio.remove();
                msg = this.Nome + ": " + msg + "\n";
                try{
                    InetAddress address = InetAddress.getByName(this.IP);
                    byte[] sendData = msg.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, 35353);
                    this.socket.send(sendPacket);
                    System.out.println("Mensagem enviada com sucesso!");
                }catch (Exception e){
                    System.out.println("Error na Thread de EnviarMsg: " + e);
                    e.printStackTrace();
                }
            }

        }
    }
}

class Receive extends Thread {
    private DatagramSocket clientSocket;
    private GUI_Cliente graphicGui;

    public Receive(DatagramSocket skt, GUI_Cliente gui) throws IOException {
        this.clientSocket = skt;
        this.graphicGui = gui;
    }

    public void run() {
        while(true){
            //System.out.println(this.graphicGui.textArea1.getX());
            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try{
                this.clientSocket.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                msg = msg.trim();
                System.out.println("Mensagem recebida com sucesso!");
                this.graphicGui.textArea1.append(msg + "\n");
            }catch (Exception e){
                System.out.println("Error na Thread de Receive: " + e);
            }
        }
    }
}