import javax.swing.*;

public class GUI_Server {
    public JPanel Panel;
    public JLabel Cliente_1;
    public JLabel Cliente_2;
    public JLabel IPCliente1;
    public JLabel IPCliente2;


    public GUI_Server(){
        JFrame frame = new JFrame("Servidor");
        frame.setContentPane(Panel);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500,150);
        this.IPCliente1.setText("Esperando o primeiro se conectar");
        this.IPCliente2.setText("Esperando o segundo se conectar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
