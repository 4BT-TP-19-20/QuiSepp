package it.bx.fallmerayer.tfo.quisepp;

import com.sun.org.apache.xerces.internal.xs.StringList;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Arrays;

public class Server implements Runnable {
    public Socket[] clients;
    public String[] students;
    public String[] inhalt;
    public File[] files;
    String quiz;
    public int j=-1;
    public int i;

    public Server(){
        i=0;
        students=new String[20];
        clients=new Socket[20];
        inhalt=new String[50];
    }


    //LogIn
    public void logIn(Socket client, int i) throws IOException {
        String username=readMessage(client);
        ++j;
        //Lehrer
        if(username.equals("admin")){
            System.out.println("Lehrer hat sich eingeloggt");
        }else{ //Schüler
            username=username.concat(" ");
            username=username.concat(readMessage(client));
            students[i]=username;
            System.out.println("Schüler " + students[i] + " hat sich eingeloggt!");
            returnQuiz(readMessage(client));
            while(true){
                quiz=readMessage(client);
                if(quiz!=null){
                    System.out.println(quiz);
                    //sendMessage(clients[0], quiz);
                    StoreQuizLocal(quiz);
                    return;
                }
            }
        }
    }


    //Gibt Schüler Quiz zurück
    public void returnQuiz(String key) throws IOException {
        File f= new File("Quizze\\");
        String quiz;
        files=f.listFiles();
        int i=0;
        do{
            FileReader fileReader=new FileReader(files[i]);
            BufferedReader br = new BufferedReader(fileReader);
            quiz=br.readLine();
            inhalt=quiz.split(";");
            if(inhalt[1].equals(key)){
                System.out.println("Key gefunden!!!");
                sendMessage(clients[j], quiz);
                return;
            }
            ++i;
        }while(i<files.length);
        System.out.println("Key nicht gefunden!");
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

    //speichert beantwortetes Quiz ab
    public void StoreQuizLocal(String quiz){
        File f = new File("QuizzeErhalten\\" + quiz.split(";")[0] + ".txt");

        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(f.getPath());
             BufferedWriter bw = new BufferedWriter(writer)) {
            bw.write(quiz);
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

        @Override
        public void run() {

    }
}
