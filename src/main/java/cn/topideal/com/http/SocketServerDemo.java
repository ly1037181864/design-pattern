package cn.topideal.com.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServerDemo {


    static class SocketRunnable implements Runnable {
        private Socket accept;

        SocketRunnable(Socket accept) {
            this.accept = accept;
        }

        @Override
        public void run() {
            BufferedReader bReader = null;
            try {
                bReader = new BufferedReader(new InputStreamReader(accept.getInputStream(), "utf-8"));
                String readLine = null;
                while ((readLine = bReader.readLine()) != null) {
                    System.out.println(Thread.currentThread().getName() + "\t" + readLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bReader != null) {
                    try {
                        bReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
        try {
            ServerSocket socket = new ServerSocket(8080);
            while (true) {
                new Thread(new SocketRunnable(socket.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
