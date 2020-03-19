import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class  TCP_File_Client {
    public int TCP(String b) {

        Scanner scan = null;
        InputStream in = null;
        Socket socket = null;
        JFrame jftip;
        JTextArea jttip;
        int panduan = 0;
        try {
            /**
             * 1.扫描控制台接收文件路径名
             *   创建一个file引用，指向一个新的File对象，并给文件赋予地址
             */

            String path = b;
            File file = new File(path);

            /**
             * 2.判断文件是文本文件而不是文件夹并且路径存在
             *  exists()：判断文件是否存在
             *  isFile()：判断是不是文件
             */
            if (file.exists() && file.isFile()) {

                /**
                 * 3.创建文件输入流，发送文件
                 *   将文件输入的内容都放在file里面
                 */
                in = new FileInputStream(file);

                /**
                 * Socket 这个类实现客户端套接字(也称为“套接字”)。套接字是两台机器间通信的端点。
                 *
                 * 4.创建客户端套接字
                 */
                socket = new Socket();
                //InetSocketAddress Inets = new InetSocketAddress("127.0.0.1", 12345);

                /**
                 * 5.连接TCP服务器
                 *       确定服务端的IP和端口号
                 */
                socket.connect(new InetSocketAddress("127.0.0.1", 8091));

                /**
                 * 6.获取到客户端的输出流
                 *   OutputStream     getOutputStream()
                 *                         返回此套接字的输出流。
                 */
                OutputStream out = socket.getOutputStream();

                /**
                 * 7.向服务器发送文件
                 *   自己定义了一个协议来解决粘包现象，获取文件名
                 *   7.1.我们先将文件中的内容读取出来，放到file里面
                 *   7.2.先读文件名  file.getName()
                 *   7.3.将文件名转换成字节  file.getName().getBytes()
                 *   7.4.获取文件名的字节的长度  file.getName().getBytes().length
                 *   7.5.再在文件名长度的后面加上  \r\n 作为标识符
                 */
                // 向服务器发送[文件名字节长度 \r\n]
                out.write((file.getName().getBytes().length + "\r\n").getBytes());
                // 向服务器发送[文件名字节]
                out.write(file.getName().getBytes());
                // 向服务器发送[文件字节长度\r\n]
                out.write((file.length() + "\r\n").getBytes());
                // 向服务器发送[文件字节内容]
                byte[] data = new byte[1024];
                int i = 0;
                while ((i = in.read(data)) != -1) {
                    out.write(data, 0, i);
                }

            } else {
                System.out.println("文件不存在或者一个文件~~");
                jttip = new JTextArea();
                jttip.setText("找不到该文件");
                jttip.setSize(200, 75);
                jftip = new JFrame();
                jftip.setTitle("提示");
                jftip.setVisible(true);
                jftip.setLocation(100, 100);
                jftip.setSize(300, 100);
                jftip.add(jttip);
                panduan = 1;

            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            /**
             * 关闭Scanner，文件输入流，套接字
             * 套接字装饰了输出流，所以不用关闭输出流
             */
            if (scan != null) {
                scan.close();
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 强制将输入流置为空
                in = null;
            }
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 强制释放socket
                socket = null;
            }

        }

        if (panduan == 0) {
            jttip = new JTextArea();
            jttip.setText("文件传输成功");
            jttip.setSize(200, 75);
            jftip = new JFrame();
            jftip.setTitle("提示");
            jftip.setVisible(true);
            jftip.setLocation(100, 100);
            jftip.setSize(300, 100);
            jftip.add(jttip);
        }
        return panduan;
    }
}