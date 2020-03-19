import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

class MainShow  extends JFrame implements ItemListener, ActionListener {//两个事件类型，所以两种接口
    //创建两个下拉框
    JComboBox listID=new JComboBox();
    JComboBox listDATA=new JComboBox();
    JButton buttonDownload;
    JButton buttonRead;
    JTextField IDin;
    JTextField Datain;
    JTextField NameIN;//文件名输入框
    JTextField PathIN;//路径输入框
    JButton uploadButton;
    JFrame jf;
    MainShow() {

        //添加并设置窗口
        jf = new javax.swing.JFrame();//创建窗口
        jf.setTitle("胎心宫缩监测系统");//设置标题
        jf.setSize(700, 500);//只对顶级容器有效
        jf.setDefaultCloseOperation(3);//窗体关闭时结束程序
        jf.setLocationRelativeTo(null);//居中
        jf.setResizable(false);//不可调整大小
        jf.setVisible(true);//窗口可见

        //CardLayout布局,分开两个界面，上传和图像读取
        JTabbedPane p=new JTabbedPane(JTabbedPane.LEFT);//将选项卡设置位于左侧
        JPanel upload=new JPanel();//建立两个选项卡对应的功能区有几个功能区就有几个选项卡
        JPanel readPic=new JPanel();
        p.setBounds(100,100,300,400);//设置格式
        p.setVisible(true);
        p.add("上传",upload);//按选项卡添加的顺序添加名字
        p.add("图像生成和下载",readPic);

        //图像生成和下载部分
        //两个下拉栏，一个用来选择Id，一个用来选择Id的对应的各个文件的日期

        //这一部分用来获取库中已有的所有的ID，库文件的存储信息格式如下
        /*
        ID
        日期
        文件信息
        依此类推，每组信息占三行，因此按行读取时如果一个ID有多个日期的信息记录，读取ID时就会出现重复，并且，信息
        的录入是按时间来的，读出的ID不是按顺序排列的，对使用者不友好，因此要解决两个问题，1是重复，2是顺序
        我们将使用字符型链表存储获取，因为其提供的方法可以快速解决第一个问题，但是第二个问题尚未解决。然后我们再利用链表存储的信息向
        下拉框添加选项
        */

        String a="1",b,c;//a、b、c都是用来存储按行读取的信息的，只是临时用来过渡，不用使用链表或数组记录下来
        ArrayList<String> list = new ArrayList<String>();//建立链表

        FileReader inID= null;//声明文件对象，这一步要放在try外边，否则无法供其他部分调用
        try{
            inID = new FileReader("D:\\CTGfiles\\username\\AdministratorFolder\\CTG.txt");//实例化，将存储文件信息的库文件放入
            BufferedReader inid=new BufferedReader(inID);//buffer数据类，用来按行读取
            while ( a!= null) { //使用a来做判断条件而不是用readline作为判断条件，应为那样的话会提前读取第一行，第一行读错其他的就都读错了
                a=inid.readLine();//a用来放置id信息
                list.add(a);//将a加入列表
                b=inid.readLine();//bc接着按行读这样到下一行a就是id了
                c=inid.readLine();//
            }}
        catch (IOException e){System.out.println("wrong");}
        //这三句可以去除链表中的重复项
        HashSet h=new HashSet(list);
        list.clear();
        list.addAll(h);
        //去除后加入下拉框
        //为什么j要从1开始，因为上面去除重复项的过程中，链表倒序了，第一个变成了空
        for (int j=1;j<list.size();j++)
        {listID.addItem(list.get(j));System.out.println(list.get(j));}
        //为ID下拉框注册监听器
        listID.addItemListener(this);

        //预置大小格式
        Dimension dimBut = new Dimension(340, 30);
        //添加标签
        JLabel labid = new JLabel();
        labid.setText("请选择ID");
        JLabel labdata=new JLabel();
        labdata.setText("请选择日期");

        JTextArea TipPic=new JTextArea();
        //调用的第三方包，生成的图像是个功能区，右键有很多功能,包括保存当前显示的状态的图片，所谓当前状态，就是说可以选中图片区域局部查看
        TipPic.setText("提示：如需保存图片到本地，直接右键图像即可\n" +
                "用鼠标选中区域即可放大查看相应细节，同样可以右键保存");


        //添加按钮,设置格，注册监视器
        buttonRead=new JButton();
        buttonRead.setText("读取图像");
        buttonRead.setPreferredSize(dimBut);
        buttonRead.addActionListener(this);

        //现在开始写下载功能
        //首先添加一个下载按钮，并格式化，并注册监视器
        buttonDownload=new JButton();
        buttonDownload.setText("下载文件");
        buttonDownload.setPreferredSize(dimBut);
        buttonDownload.addActionListener(this);

        //格式化，以及盒式布局的建立
        Box boxupload=Box.createVerticalBox();
        BoxLayout liebuju=new BoxLayout(upload,2);
        Dimension dimLab = new Dimension(50, 50);
        Dimension dimInp = new Dimension(250, 30);
        //现在开始写上传功能
        //先是两个标签，两个文本框，让用户输入ID，输入报告时间
        JLabel InID=new JLabel();
        InID.setText("输入ID");
        InID.setPreferredSize(dimLab);
        JLabel InData=new JLabel();
        InData.setText("输入日期");
        InData.setPreferredSize(dimLab);
        JLabel InFileName=new JLabel();
        InFileName.setText("输入文件名（需后缀名）");
        InFileName.setPreferredSize(dimLab);
        JLabel InFilePath=new JLabel();
        InFilePath.setText("输入文件路径");
        InFilePath.setPreferredSize(dimLab);
        JTextArea Tipxls=new JTextArea();
        Tipxls.setText("为保证向下兼容性，excel的读取为office2003版本格式,即.xls格式\n" +
                "如果您的office版本高于2007，请将.xlsx文件转存为.xls格式\n" +
                "建议您的文件命名时采用“IDxxxDATAxxx”的格式，方便您和他人下载此文件时查阅\n" +
                "！！！输入信息时切勿使用中文，因为未使用数据库，而是使用文本文件存储库信息\n" +
                "！！！按行写入中文字符时会出现未知错误！！！导致整个库文件信息受损");
        IDin=new JTextField();
        Datain=new JTextField();
        NameIN=new JTextField();
        PathIN=new JTextField();
        uploadButton=new JButton();
        uploadButton.setPreferredSize(dimBut);
        uploadButton.setText("上传文件");
        uploadButton.addActionListener(this);
        IDin.setPreferredSize(dimInp);
        Datain.setPreferredSize(dimInp);
        NameIN.setPreferredSize(dimInp);
        PathIN.setPreferredSize(dimInp);

        //选择文件路径







        //把那些组件都加进去
        readPic.add(labid);
        readPic.add(listID);
        readPic.add(labdata);
        readPic.add(listDATA);
        readPic.add(buttonRead);
        readPic.add(TipPic);
        readPic.add(buttonDownload);
        boxupload.add(InID);
        boxupload.add(Datain);
        boxupload.add(IDin);
        boxupload.add(InData);
        boxupload.add(Datain);
        boxupload.add(InFileName);
        boxupload.add(NameIN);
        boxupload.add(InFilePath);
        boxupload.add(PathIN);
        boxupload.add(uploadButton);
        boxupload.add(Tipxls);

        upload.add(boxupload);
        p.validate();
        jf.add(p, BorderLayout.CENTER);
        jf.validate();
        jf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    //下拉栏的响应，只有ID有响应能生成时间下拉框就行了，按钮的响应会对应下拉列表的选中项
    public  void itemStateChanged(ItemEvent e) {
        String idName;
        idName=(listID.getSelectedItem()).toString();
        listDATA.removeAllItems();
        //listDATA.addItem("请选择一个时间");
        FileReader inDATA= null;
        try{
            inDATA = new FileReader("D:\\CTGfiles\\username\\AdministratorFolder\\CTG.txt");
            BufferedReader indata=new BufferedReader(inDATA);
            String d="1",g,c;ArrayList<String> list = new ArrayList<String>();
            System.out.println("test");
            while(d!=null){
                d=indata.readLine();
                g=indata.readLine();
                c=indata.readLine();
                if (idName.equals(d))
                    list.add(g);
                // ReadExcelToPic readexceltopic=new ReadExcelToPic(g);
            }
            for (int j=0;j<list.size();j++)
                listDATA.addItem(list.get(j));
        }catch (IOException e1){System.out.println("wrong");}
    }


    public void actionPerformed(ActionEvent e) {
        JButton actionbutton=(JButton)e.getSource();
        if (actionbutton==buttonRead){
            String IDName=(listID.getSelectedItem().toString());
            String DATAName=(listDATA.getSelectedItem().toString()); //ReadExcelToPic readexceltopic;
            if (IDName!=null||DATAName!=null)
            {
                try{
                    FileReader source = new FileReader("D:\\CTGfiles\\username\\AdministratorFolder\\CTG.txt");
                    BufferedReader indata=new BufferedReader(source);
                    String d="1",g,c;ArrayList<String> list = new ArrayList<String>();
                    //System.out.println("test");
                    while(d!=null){
                        d=indata.readLine();
                        g=indata.readLine();
                        c=indata.readLine();
                        if (d.equals(IDName)&&g.equals(DATAName))
                        {
                            new ReadExcelToPic(d, c);break;
                        }
                    }

                }catch (IOException e1){System.out.println("wrong");}
            }
        }
        if (actionbutton==buttonDownload){
            String IDName=(listID.getSelectedItem().toString());
            String DATAName=(listDATA.getSelectedItem().toString());

            if (IDName!=null&&DATAName!=null)
            {
                try{
                    FileReader source = new FileReader("D:\\CTGfiles\\username\\AdministratorFolder\\CTG.txt");
                    BufferedReader indata=new BufferedReader(source);
                    String d="1",g,c;ArrayList<String> list = new ArrayList<String>();
                    System.out.println("test");
                    while(d!=null){
                        d=indata.readLine();
                        g=indata.readLine();
                        c=indata.readLine();
                        if (d.equals(IDName)&&g.equals(DATAName))
                        { //默认下载位置D盘根目录
                            Thread downloadThread = new DownloadFileHandler(c, "D:\\");
                            downloadThread.start();
                            try {
                                downloadThread.join();
                            } catch (InterruptedException e1) {
                                e1.printStackTrace();
                            }
                            break;}
                    }

                }catch (IOException e1){System.out.println("wrong");}
            }
        }
        if (actionbutton==uploadButton){
            String ID=IDin.getText();
            String DATA=Datain.getText();
            String NAME=NameIN.getText();
            String PATH=PathIN.getText();
            BufferedWriter out=null;
            TCP_File_Client tt=new TCP_File_Client();
            int upload= tt.TCP(PATH+NAME);
            if (upload==0) {
                try {
                    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\CTGfiles\\username\\AdministratorFolder\\CTG.txt", true)));
                    out.append("\r\n" + ID);
                    out.newLine();
                    out.append(DATA);
                    out.newLine();
                    out.append(NAME);
                    out.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            jf.dispose();
            MainShow newmain=new MainShow();

        }
    }
}
