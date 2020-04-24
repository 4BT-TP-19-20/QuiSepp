package it.bx.fallmerayer.tfo.quisepp;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {

    private Thread[] threads;

    public static void main(String[] args){
        ServerMain serverMain= new ServerMain();
        serverMain.start();
    }

    public ServerMain () {
        threads = new Thread[20];
    }

    public void start(){
        try{
            ServerSocket serverSocket = new ServerSocket(36187);
            Server server = new Server();

            for (int i = 0; i < 20; ++i) {
                server.setClients(server.WaitForConnection(serverSocket), i);
                server.logIn(server.getClient(i), i);
                threads[i] = new Thread(server);
                threads[i].start();
            }
            System.out.println("Server wird geschlossen!");
            for (int i = 0; i< 20; ++i) {
                threads[i].join();
            }
        }catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
    }
}
