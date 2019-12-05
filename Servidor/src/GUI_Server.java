import javax.swing.*;

public class GUI_Server {
    public JPanel Panel;
    public JTextArea Cliente1;
    public JTextArea Cliente2;
    public JTextArea IP_Cliente1;
    public JTextArea IP_Cliente2;

    public GUI_Server(){
        JFrame frame = new JFrame("Server");
        this.Cliente1.setEditable(false);
        this.Cliente2.setEditable(false);
        this.IP_Cliente1.setEditable(false);
        this.IP_Cliente2.setEditable(false);
        frame.setContentPane(Panel);
        Panel.setSize(1000,1000);
        frame.setVisible(true);
        this.IP_Cliente1.setText("Esperando o primeiro se conectar");
        this.IP_Cliente2.setText("Esperando o segundo se conectar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
