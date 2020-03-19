import javax.swing.*;
import java.io.*;
import java.net.Socket;

public  class DownloadFileHandler extends Thread {
    //客户端Socket
    private Socket client;
    //需要下载的文件，存放在服务端中
    private String downloadFile;
    //下载文件的目的路径，本机路径
    private String downloadPath;

    JFrame jftip;
    JTextArea jttip;
    public DownloadFileHandler(String downloadFile, String downloadPath) {
        try {
            this.downloadFile = downloadFile;
            this.downloadPath = downloadPath;
            //建立和服务器的连接
            client = new Socket(Server.DEFAULT_SERVER_HOST, Server.DEFAULT_SERVER_PORT);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
    }

    @Override
    public void run() {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        BufferedOutputStream sbos = null;

        byte[] buf = new byte[1024];
        int len = -1;
        try {
            //从服务器端读取数据的输入流
            bis = new BufferedInputStream(client.getInputStream());
            //将数据写到目的路径中的输出流
            bos = new BufferedOutputStream(new FileOutputStream(new File(downloadPath, downloadFile)));
            //用于和服务端传输的输出流
            sbos = new BufferedOutputStream(client.getOutputStream());

            /**
             * 发送操作标志：服务器需要判断入站操作时上传文件还是下载文件
             * 将操作标志发给服务端解析处理
             * TextServer.CLIENT_FILE_UPLOAD : 上传操作
             * TextServer.CLIENT_FILE_DOWNLOAD : 下载操作
             */
            String uploadsring = Server.CLIENT_FILE_DOWNLOAD + "," +downloadPath + "," + downloadFile;
            byte[] uploadbyte = uploadsring.getBytes();
            sbos.write(uploadbyte, 0, uploadbyte.length);
            sbos.flush();

            //发送操作标志以后，等待获取服务端的回复，确认文件是否存在
            len = bis.read(buf);
            String result = new String(buf, 0, len);
            //如果客户端请求文件不存在服务器则退出
            if (Server.SERVER_FILE_NOTEXIST.equals(result)) {
                System.out.println("download file not exist on server!");
                return ;
            } else {
                //文件存在继续执行操作
                System.out.println("download file exist on server!");
            }

            jttip=new JTextArea();
            jttip.setText("文件下载到D盘成功");
            jttip.setSize(200,75);
            jftip=new JFrame();
            jftip.setTitle("提示");
            jftip.setVisible(true);
            jftip.setLocationRelativeTo(null);
            jftip.setSize(300,100);

            jftip.add(jttip);
            //开始下载文件数据，并且保存到本地路径当中
            while ((len = bis.read(buf)) > 0) {
                //System.out.println("len : " + len);
                bos.write(buf, 0, len);
                bos.flush();
            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            jttip=new JTextArea();
            jttip.setText("文件下载异常");
            jttip.setSize(200,75);
            jftip=new JFrame();
            jftip.setTitle("提示");
            jftip.setVisible(true);
            jftip.setLocationRelativeTo(null);
            jftip.setSize(300,100);
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