import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.net.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class GUI_Cliente {
    // INTERFACE
    public JTextArea textArea1;
    public JPanel panel1;
    public JTextField textField1;
    public JButton enviarButton;
    private JTextArea statusCliente;
    public JTextArea IPdoOutro;
    public JTextArea FilaSize;

    // LOGICA
    public boolean client_is_Off;

    // INFORMACOES
    public String ip;
    public DatagramSocket socket;
    public Queue<String> filaDeEnvio;
    public int port;


    public GUI_Cliente(DatagramSocket socket) {
        this.ip = "0";
        this.port = 35353;
        this.socket = socket;
        this.client_is_Off = true;
        this.filaDeEnvio = new LinkedList<>();

        setInterface();
        //Comandos de envio
        enviarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarMsg();
            }
        });

        textField1.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()==KeyEvent.VK_ENTER && textField1.getText().length() > 0) {
                enviarMsg();
            }
            }
        });
    }
    public void enviarMsg(){
        if (textField1.getText().length() > 0) {
            this.filaDeEnvio.add(textField1.getText());
            this.textArea1.append("Você: " + textField1.getText() + "\n");
            this.textField1.setText("");
        }
    }

    public void setInterface(){
        this.textArea1.setEditable(false);
        JFrame janela = new JFrame();
        janela.setContentPane(panel1);
        janela.setVisible(true);
        janela.pack();
        janela.setSize(540,540);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public void setIp (String ip) {
        this.ip = ip;
        this.IPdoOutro.setText(ip);
    }

    public void init() {
        textArea1.append("Bem-Vindo ao ZapGram! \n");
    }

    public void setClientOff(String msg) {
        if (msg.equals("OFF")) {
            client_is_Off = true;
            this.statusCliente.setText("O outro cliente esta Offline");
        } else {
            client_is_Off = false;
            this.statusCliente.setText("O outro cliente esta Online");
        }
    }
}

