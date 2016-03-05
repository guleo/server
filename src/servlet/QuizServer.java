package servlet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by frank on 2016/3/5.
 */
public class QuizServer {
    public static void main(String[] args) {
        ServerSocket ss = null;
        Socket s = null;
        DataInputStream din = null;
        DataOutputStream dout = null;
        try {
            ss = new ServerSocket(8888);
            System.out.println("ÒÑ¼àÌýµ½8888¶Ë¿Ú");
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(true) {
            try {
                s = ss.accept();
                din = new DataInputStream(s.getInputStream());
                dout = new DataOutputStream(s.getOutputStream());
                String msg = din.readUTF();
                System.out.println("ip:"+s.getInetAddress());
                System.out.print("msg: " + msg);
                dout.writeUTF("Hello Client !");
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    if (dout != null) {
                        dout.close();
                    }
                    if (din != null) {
                        din.close();
                    }
                    if (s != null) {
                        s.close();
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }
}
