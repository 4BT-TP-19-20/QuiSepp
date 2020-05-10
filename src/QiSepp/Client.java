package QiSepp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client implements Runnable{

    public Socket socket;

    public void start(String ip) {

        try {
            socket = new Socket(ip, 36187);
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public String readMessage(Socket s) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        return bufferedReader.readLine();
    }

    public void sendMessage(Socket s, String nachricht) throws IOException{
        PrintWriter printWriter = new PrintWriter(s.getOutputStream(), true);
        if (nachricht.compareTo("") != 0) {
            printWriter.println(nachricht);
        }
    }

    //sendMessage

    public void send(String msg) throws IOException {
        sendMessage(socket, msg);
    }
    public String read() throws IOException{
        return readMessage(socket);
    }

    @Override
    public void run() {

        while (true) {
            try {
                String msg = readMessage(socket);
                System.out.println(msg);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
