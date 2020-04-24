package it.bx.fallmerayer.tfo.quisepp;

public class Quiz {
    public int anzFragen;
    public String[] fragen;
    public int[] antworten;

    public Quiz(int anzFragen){
        this.anzFragen=anzFragen;
        fragen=new String[anzFragen];
        antworten=new int[anzFragen];
    }

    public void createQuiz(String[] fragenC, int[] antwortenC){
        for(int i=0; i<anzFragen; ++i){
            fragen[i]=fragenC[i];
            antworten[i]=antwortenC[i];
        }
    }

    //public void deleteQuiz(){
       // this.=null;
   // }
}
