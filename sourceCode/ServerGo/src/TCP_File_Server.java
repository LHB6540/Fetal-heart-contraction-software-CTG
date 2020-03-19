import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCP_File_Server {
    void ser() throws Exception {
        /**
         * 创建服务端套接字
         */
        ServerSocket ss = new ServerSocket();

        /**
         * 绑定指定端口
         */
        ss.bind(new InetSocketAddress(8091));

        /**
         * 监听并接受客户端socket连接,并返回一个socket
         */

        /**
         * 持续接收客户端发来的信息，并交给线程处理
         */
        while(true) {
            Socket socket = ss.accept();
            new Thread(new UpLoad(socket)).start();
        }
    }
}

class UpLoad implements Runnable {

    private Socket socket = null;

    public UpLoad(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        OutputStream out = null;
        try {
            // 创建文件输入流，接收客户端的socket中的文件流
            InputStream in = socket.getInputStream();
            /**
             * 获取文件名长度
             * 文件格式：文件名长度(数字)\r\文件名\r\n文件内容\r\n
             * 获取文件名 - 读到第一个回车换行之前 截取出文件名的长度 接着读取这个长度的字节 就是文件名
             * 读取数据 直到遇到第一个回车换行
             * 每次从流中读取一个字节 转成字符串 拼到line上 只要line还不是\r\n结尾 就重复这个过程
             */
            String line1 = "";
            byte[] by1 = new byte[1];
            while (!line1.endsWith("\r\n")) {
                in.read(by1);
                String str = new String(by1);
                line1 += str;
            }
            /**
             * 1.读到长度，去掉\r\n就是文件名字的长度
             * 2.parseInt():作用是将可分析的字符串转化为整数。
             * 3.substring():返回一个新字符串，它是此字符串的一个子字符串。
             */
            int len1 = Integer.parseInt(line1.substring(0, line1.length() - 2));
            /**
             * 1.读取文件名
             * 2.先创建一个长度和文件名长度相等的字节数组，用来存放文件名
             * 3.read(data)：从输入流中读取一定数量的字节，并将其存储在缓冲区数组 data 中
             *      data数组有多大，就在in输入流里面读取多少内容，并将内容存放在data数组里面
             */
            byte[] data = new byte[len1];
            in.read(data);
            String fileName = new String(data);

            // 获取文件内容字节长度
            String line2 = "";
            byte[] by2 = new byte[1];
            while (!line2.endsWith("\r\n")) {
                in.read(by2);
                String str = new String(by2);
                line2 += str;
            }
            int len2 = Integer.parseInt(line2.substring(0, line2.length() - 2));

            // 创建输文件出流，指定文件输出地址
            String path = "D:\\CTGfiles\\username\\CTG\\" + fileName;
            out = new FileOutputStream(path);
            // 获取文件内容字节
            // 流对接
            byte[] by3 = new byte[len2];
            in.read(by3);
            out.write(by3);

            System.out.println("接受到来自" + socket.getInetAddress().getHostAddress() + "上传的文件" + path);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            // 关闭输出流
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                out = null;
            }
            // 关闭socket
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                socket = null;
            }
        }
    }
}
