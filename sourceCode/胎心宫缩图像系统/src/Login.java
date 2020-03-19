import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import javax.imageio.IIOException;
import javax.swing.*;


import java.io.*;

//1.定义Login类，
public class Login {

    // 1.在类中定义主函数
    public static void main(String[] args) {
        // 2.在主函数中，实例化Login类的对象，调用初始化界面的方法。
        Login login = new Login();
        login.ShowUI();

    }


    //在类中定义初始化界面的方法；
    public void  ShowUI(){

        //创建窗口，设置标题、大小、关闭动作、显示位置、不可调节大小
        JFrame jf = new JFrame();
        jf.setTitle("胎心宫缩监测系统");
        jf.setSize(340, 700);
        jf.setDefaultCloseOperation(3);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);

        //创建居中、边距为10px的流体对象，jf调用
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER, 5, 5);
        jf.setLayout(fl);

        //预设四种大小标准，分别供图片、标签、输入框、按钮使用
        Dimension dimPic = new Dimension(340, 300);
        Dimension dimLab = new Dimension(50, 50);
        Dimension dimInp = new Dimension(250, 30);
        Dimension dimBut = new Dimension(340, 40);

        //下面开始添加组件

        //组件1背景图片,期间使用JLabel作为过渡容器
        ImageIcon icon = new ImageIcon("D:\\CTGfiles\\username\\AdministratorFolder\\BackGround.jpg");
        JLabel labpic = new JLabel(icon);
        labpic.setPreferredSize(dimPic);
        jf.add(labpic);

        //组件2账号标签
        JLabel labname = new JLabel();
        labname.setText("账号：");
        labname.setPreferredSize(dimLab);
        jf.add(labname);

        //组件3账号输入框
        JTextField textname = new JTextField();
        textname.setPreferredSize(dimInp);
        jf.add(textname);

        //组件4密码标签
        JLabel labpassword = new JLabel();
        labpassword.setText("密码：");
        labpassword.setPreferredSize(dimLab);
        jf.add(labpassword);

        //组件5密码输入框
        JPasswordField jp = new JPasswordField();
        jp.setPreferredSize(dimInp);
        jf.add(jp);

        //组件5登录按钮
        JButton button = new JButton();
        button.setText("登录");
        button.setPreferredSize(dimBut);
        jf.add(button);

        //组件6密码错误时给出提示
        JTextArea Tip=new JTextArea();
        //Tip.setPreferredSize(dimBut);
        jf.add(Tip);

        //窗口可视设置
        jf.setVisible(true);

        //监听与响应

        //创建监听类对象，并把账号和密码输入框的对象传递给它
        LoginListener ll = new LoginListener(jf, textname, jp,Tip);
        button.addActionListener(ll);


    }

    //监听事件
    public class LoginListener implements ActionListener {

        //创建账号和密码对象
        private JTextField jt;
        private JPasswordField jp;
        private JTextArea Tip;
        //创建一个登录窗口
        private JFrame login;

        //重写方法
        public LoginListener(JFrame login,JTextField jt,JPasswordField jp,JTextArea Tip){
            this.login=login;
            this.jt=jt;
            this.jp=jp;
            this.Tip=Tip;
        }
        public void actionPerformed(ActionEvent e) {
            int choose=1;
            String text=null;String pass;

            try {
                FileReader inOne=new FileReader("D:\\CTGfiles\\username\\AdministratorFolder\\Account.txt");
                BufferedReader intwo=new BufferedReader(inOne);
                while (choose == 1) {
                    text=intwo.readLine();
                    pass=intwo.readLine();
                    //利用get方法来获取账号和密码对象的文本信息，并用equal方法进行判断。最好不要用==，用==这个地方验证不过去。
                    if (jt.getText().equals(text) && jp.getText().equals(pass)) {
                        MainShow mainshow = new MainShow();

                        // 通过我们获取的登录界面对象，用dispose方法关闭它
                        login.dispose();
                        choose=2;

                    }
                    if (text==null){
                        choose=2;
                        Tip.setText("密码或账号错误，请重试");
                    }
                }
            }catch (IIOException e1){System.out.println(e);} catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }
}
