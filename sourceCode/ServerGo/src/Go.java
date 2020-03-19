import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Go implements ActionListener {
    public static void main(String args[]){
        Go go=new Go();
        go.GoUI();
    }
    JButton jbuttonGo;
    JButton Close;
    JFrame Goserver;
    public void GoUI(){
        Goserver=new JFrame();
        Goserver.setTitle("一键启动服务器");
        Goserver.setVisible(true);
        Goserver.setLocation(100, 100);
        Goserver.setSize(300, 200);
        Goserver.setDefaultCloseOperation(3);

        FlowLayout flow=new FlowLayout();
        Goserver.setLayout(flow);
        jbuttonGo=new JButton();
        jbuttonGo.setText("启动服务器");

        //Close=new JButton();
        //Close.setText("关闭服务器");

        Dimension dimension=new Dimension(100,100);
        jbuttonGo.setPreferredSize(dimension);
        //Close.setPreferredSize(dimension);

        Goserver.add(jbuttonGo);
        //Goserver.add(Close);
        Goserver.validate();
        jbuttonGo.addActionListener(this);
        //Close.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton action = (JButton) e.getSource();
        if (action == jbuttonGo) {
            Goserver.dispose();
            Server Download = new Server();
            Download.DownloadServer();

            TCP_File_Server tcpupload = new TCP_File_Server();
            try {
                tcpupload.ser();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        if (action==Close){
            System.exit(0);
        }
    }

}
