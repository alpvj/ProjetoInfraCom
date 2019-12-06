import javax.swing.*;
import java.awt.event.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Queue;


public class GUI_Cliente {
    // INTERFACE
    public JTextArea textArea1;
    public JPanel panel1;
    public JTextField textField1;
    public JButton enviarButton;
    private JTextArea statusCliente;

    public JLabel meuStatus;
    public JTextArea FilaSize;
    private JCheckBox mutarMicrofoneCheckBox;
    private JCheckBox onlineOfflineCheckBox;

    // LOGICA
    public boolean client_is_Off;
    public boolean my_client_is_Off;
    public boolean muteMicrofone;

    // INFORMACOES
    public String ip;
    public DatagramSocket socket;
    public Queue<String> filaDeEnvio;
    public int port;


    public GUI_Cliente(DatagramSocket socket) {
        this.ip = "0";
        this.port = 35353;
        this.socket = socket;
        this.client_is_Off = false;
        this.my_client_is_Off = false;
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

        mutarMicrofoneCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(itemEvent.getStateChange() == itemEvent.DESELECTED){
                    muteMicrofone = false;
                }
                else{
                    muteMicrofone = true;
                }
            }

        });

        onlineOfflineCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                if(itemEvent.getStateChange() == itemEvent.DESELECTED){
                    meuStatus.setText("Status: Online");
                    my_client_is_Off = false;
                    System.out.println(my_client_is_Off);
                }
                else{
                    meuStatus.setText("Status: Offline");
                    my_client_is_Off = true;
                    System.out.println(my_client_is_Off);
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
        this.statusCliente.setEditable(false);
        this.FilaSize.setEditable(false);
        JFrame janela = new JFrame();
        janela.setContentPane(panel1);
        janela.setVisible(true);
        janela.pack();
        janela.setSize(540,540);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }



    public void init() {
        this.textArea1.append("Bem-Vindo ao ZapGram! \n");
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

