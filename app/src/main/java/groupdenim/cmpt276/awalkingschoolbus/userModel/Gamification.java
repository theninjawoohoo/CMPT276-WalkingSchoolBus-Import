package groupdenim.cmpt276.awalkingschoolbus.userModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by niragmehta on 2018-04-02.
 */

//this is a singleton class
public class Gamification {



    private int quizID=0;    //starts from 0, then we initialize the acitivty with whatever id we have in the quizID field
    private int imageID;
    private int titleID;
    private int points;
    List<Integer> quizzesSolved=new ArrayList<>(Quiz.questions.length);
    Date date;

    //singleton object
    private static Gamification instance;

    public static Gamification getInstance()
    {
        if(instance==null)
        {
            instance=new Gamification();
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

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public int getTitleID() {
        return titleID;
    }

    public void setTitleID(int titleID) {
        this.titleID = titleID;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
