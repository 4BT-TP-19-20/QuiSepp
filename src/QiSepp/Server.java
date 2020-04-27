package QiSepp;

import com.sun.org.apache.xerces.internal.xs.StringList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    public Socket[] clients;
    public String[] students;
    public int i;
    String path = "D:\\Informatk\\Programme\\spiel\\src\\QuiSeppApp\\src\\QiSepp\\Quizzes\\";
    File folder = new File(path);

    public Server(){
        i=0;
        students=new String[20];
        clients=new Socket[20];
    }



    public void logIn(Socket client, int i) throws IOException {
        String username=readMessage(client);

        if(username.equals("admin")){
            System.out.println("Lehrer hat sich eingeloggt");
        }else{
            students[i]=username;
            System.out.println("Schüler " + students[i] + " hat sich eingeloggt!");
        }
    }

    public void getQuizRequest(Socket client, int i) throws IOException {
        String quizName = readMessage(client);

        File[] listOfFiles = folder.listFiles();
        for(File file : listOfFiles){
            BufferedReader br = new BufferedReader(new FileReader(path + file.getName()));
            String strQuiz = br.readLine();
            String[] password = strQuiz.split(";");
            if(password[1].compareTo(quizName) == 0){
                sendMessage(client, strQuiz);
            }
        }
    }

    public void sendMessage(Socket socket, String msg) throws IOException {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        printWriter.println(msg);
    }

    public String readMessage(Socket socket) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String msg=bufferedReader.readLine();
        return msg;
    }

    public void setClients(Socket client, int j) {
        this.clients[j] = client;
    }

    public Socket getClient(int j) {
        return clients[j];
    }

    public Socket WaitForConnection(ServerSocket s) {
        try {
            System.out.println("Waiting for Client...");
            return s.accept();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

        @Override
        public void run() {

    }
}
