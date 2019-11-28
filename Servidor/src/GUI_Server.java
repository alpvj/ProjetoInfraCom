import javax.swing.*;

public class GUI_Server {
    public JPanel Panel;
    public JTextArea Cliente1;
    public JTextArea Cliente2;

    public GUI_Server(){
        JFrame frame = new JFrame("Server");
        frame.setContentPane(Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        Panel.setSize(540, 540);
        frame.setVisible(true);

    }
}
