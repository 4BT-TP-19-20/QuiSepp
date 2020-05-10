package it.bx.fallmerayer.tfo.quisepp;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {

    public Thread[] threads;

    public static void main(String[] args) throws InterruptedException {
        ServerMain serverMain= new ServerMain();
        serverMain.start();
    }

    public ServerMain () {
        threads = new Thread[100];
    }

    public void start() throws InterruptedException {
        try{
            ServerSocket serverSocket = new ServerSocket(36187);
            Server server = new Server();
            int i =0;
            while(true) {
                server.setClients(server.WaitForConnection(serverSocket), i);
                server.logIn(server.getClient(i), i);
                threads[i] = new Thread(server);
                threads[i].start();
            }

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
