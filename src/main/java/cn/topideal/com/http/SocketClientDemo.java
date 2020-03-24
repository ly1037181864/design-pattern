package cn.topideal.com.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketClientDemo {
    public static void main(String[] args) {
        Socket socket = null;
        OutputStream outputStream = null;
        try {
            socket = new Socket("localhost", 8080);

            outputStream = socket.getOutputStream();

            String line;
            Scanner scanner;
            while (true) {

                System.out.println("请输入要传入的字符");
                scanner = new Scanner(System.in);
                line = scanner.nextLine();
                if ("stop".equals(line))
                    break;

                System.out.println("输入字符为：" + line);
                outputStream.write(line.getBytes());
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
