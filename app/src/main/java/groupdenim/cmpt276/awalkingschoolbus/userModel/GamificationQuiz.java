package groupdenim.cmpt276.awalkingschoolbus.userModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niragmehta on 2018-04-02.
 */

//this is a singleton class
public class GamificationQuiz {

    private int quizID=0;    //starts from 0, then we initialize the acitivty with whatever id we have in the quizID field
    private int points;
    List<Integer> quizzesSolved=new ArrayList<>(Quiz.questions.length);

    //singleton object
    private static GamificationQuiz instance;

    public static GamificationQuiz getInstance()
    {
        if(instance==null)
        {
            instance=new GamificationQuiz();
            for(int i=0;i<Quiz.questions.length;i++)
                instance.quizzesSolved.add(i,0);
        }
        return instance;
    }



    //utility functions
    public void setQuizzedSolvedAtIndex()
    {
        quizzesSolved.set(quizID,1);
    }

    public void incrementQuizID()
    {
        //from 0 to length-1
        if(quizID<Quiz.questions.length-1)
            quizID=quizID+1;
    }
    public void decrementQuizID()
    {
        if(quizID>0)
            quizID=quizID-1;
    }

    public void incrementPointsByTen()
    {
        points=points+10;
    }


    //getters and setters
    public int getQuizID() {
        return quizID;
    }

    public void setQuizID(int quizID) {
        this.quizID = quizID;
    }

    public int getPoints() {
        return points;
    }


    public void setPoints(int points) {
        this.points = points;
    }

    public List<Integer> getQuizzesSolved() {
        return quizzesSolved;
    }

    public void setQuizzesSolved(List<Integer> quizzesSolved) {
        this.quizzesSolved = quizzesSolved;
    }

}
