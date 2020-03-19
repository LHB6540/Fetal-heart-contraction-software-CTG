import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    //服务器的主机主机名
    public static final String DEFAULT_SERVER_HOST = "127.0.0.1";
    //提供服务的端口
    public static final int DEFAULT_SERVER_PORT = 8090;
    //默认存放文件的工作目录
    public static final String DEFAULT_DIRECTORY = "D:\\CTGfiles\\username\\CTG";
    //操作标识符：
    public static final String CLIENT_FILE_DOWNLOAD = "download";
    //文件判断标识
    public static final String SERVER_FILE_NOTEXIST = "not existed";
    public static final String SERVER_FILE_EXIST = "existed";

    //服务端的Socket套接字，用于监听入站请求
    private static ServerSocket server;
    //客户端的Socket套接字，存放入站Socket
    private static Socket client;

    static {
        try {
            //在静态块中初始化服务端Socket
            server = new ServerSocket(DEFAULT_SERVER_PORT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

        void DownloadServer() {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        //主线程不停循环监听端口的入栈请求
        while (true) {
            try {
                //该方法会阻塞
                client = server.accept();
                //有入站请求后打印信息
                System.out.println("client accepted : " + client.getInetAddress() + " , port : " + client.getPort());
                bis = new BufferedInputStream(client.getInputStream());
                bos = new BufferedOutputStream(client.getOutputStream());

                //以下操作为判断入站的操作类型
                byte[] buf = new byte[1024];
                int len = -1;
                len = bis.read(buf);
                String fileOperation = new String(buf, 0, len);
                System.out.println("fileOperation : " + fileOperation);
                String[] files = fileOperation.split(",");

                if (CLIENT_FILE_DOWNLOAD.equals(files[0])) {
                    System.out.println("upload operation : download");
                    //开启下载处理线程
                    Thread downloadThread = new DownloadFileHandler(client, bis, bos, fileOperation);
                    downloadThread.start();
                    downloadThread.join();
                    System.out.println("donwload ====> done");
                } else {

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }



    private static class DownloadFileHandler extends Thread {
        private Socket client;
        private BufferedInputStream bis;
        private BufferedOutputStream bos;
        private BufferedInputStream fbis;
        private String fileOperation;

        public DownloadFileHandler(Socket client, BufferedInputStream bis, BufferedOutputStream bos, String fileOperation) {
            this.client = client;
            this.bis = bis;
            this.bos = bos;
            this.fileOperation = fileOperation;
        }

        @Override
        public void run() {

            byte[] buf = new byte[1024];
            int len = -1;

            try {
                //解析文件操作标识，获取客户端下载的文件名
                String donwloadFile = fileOperation.split(",")[2];
                System.out.println("downloadFile : " + donwloadFile);
                File file = new File(DEFAULT_DIRECTORY, donwloadFile);
                System.out.println("file : " + file);

                //判断文件是否存在，当文件不存在时线程结束
                if (!file.exists()) {
                    String result = "not existed";
                    bos.write(result.getBytes(), 0, result.getBytes().length);
                    bos.flush();
                    System.out.println("downloadFile is not existed!");
                    return ;

                    //文件存在是继续操作
                } else {
                    fbis = new BufferedInputStream(new FileInputStream(file));
                    String result = "existed";
                    bos.write(result.getBytes(), 0, result.getBytes().length);
                    bos.flush();
                    System.out.println("downloadFile is existed!");
                }

                //开始从本地读取文件数据，并且发给客户端
                while ((len = fbis.read(buf)) > 0) {
                    //System.out.println("len : " + len);
                    bos.write(buf, 0, len);
                    bos.flush();
                }

                //System.out.println("downloadFile is done!");

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (fbis != null) {
                    try {
                        fbis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

        }

    }

}
