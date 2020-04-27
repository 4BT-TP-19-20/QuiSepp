package QiSepp;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerMain {

    private Thread[] threads;

    public ServerMain () {

        threads = new Thread[20];
    }

    public void start(){
        try{
            ServerSocket serverSocket = new ServerSocket(36187);
            Server server = new Server();

            for (int i = 0; i < 20; ++i) {
                threads[i] = new Thread(server);
                threads[i].start();
                server.setClients(server.WaitForConnection(serverSocket), i);
                System.out.println("Seppl");
                server.getQuizRequest(server.getClient(i), i);
                //server.logIn(server.getClient(i), i);
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
