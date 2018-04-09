package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Gamification;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Quiz;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class QuizActivity extends AppCompatActivity {

    TextView txtViewQuestion;
    TextView txtViewPoints;
    Button buttonA;
    Button buttonB;
    Button buttonC;
    Button buttonD;
    Gamification gameInstance=Gamification.getInstance();

    public static User updatedCurrentUser;
    int currentPoints=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);


        updatedCurrentUser=new User();
        //we get the current user
        ProxyBuilder.SimpleCallback<User> callback= user -> setUserFields(user);
        ServerSingleton.getInstance().getUserById(getApplicationContext(),callback,
                CurrentUserSingleton.getInstance(getApplicationContext()).getId());



        buttonA=findViewById(R.id.btnOptionA);
        buttonB=findViewById(R.id.btnOptionB);
        buttonC=findViewById(R.id.btnOptionC);
        buttonD=findViewById(R.id.btnOptionD);
        txtViewQuestion=findViewById(R.id.txtViewQuestion);
        txtViewPoints=findViewById(R.id.txtViewPoints);

        setFields();
        nextQuestion();
        previousQuestion();

        pressButtons();

        goBack();

    }

    public void setUserFields(User user)
    {
        updatedCurrentUser.deepCopyUserFields(user);
        gameInstance.setPoints(updatedCurrentUser.getTotalPointsEarned());
    }

    public void setFields()
    {
        buttonA.setText(Quiz.optionA[gameInstance.getQuizID()]);
        buttonB.setText(Quiz.optionB[gameInstance.getQuizID()]);
        buttonC.setText(Quiz.optionC[gameInstance.getQuizID()]);
        buttonD.setText(Quiz.optionD[gameInstance.getQuizID()]);
        txtViewQuestion.setText(Quiz.questions[gameInstance.getQuizID()]);
        txtViewPoints.setText(Integer.toString(gameInstance.getPoints()));
    }


    public void pressButtons()
    {
        pressButtonA();
        pressButtonB();
        pressButtonC();
        pressButtonD();
    }

    public void pressButtonA()
    {
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameInstance.getQuizzesSolved().get(gameInstance.getQuizID())==1)
                {
                    Toast.makeText(getApplicationContext(),"Question attempted",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    gameInstance.setQuizzedSolvedAtIndex();
                }
                if(Quiz.answers[gameInstance.getQuizID()]==1)
                {
                    //correct answer
                    currentPoints+=10;
                    int points=updatedCurrentUser.getTotalPointsEarned()+10;
                    updatedCurrentUser.setTotalPointsEarned(points);
                    updatedCurrentUser.setCurrentPoints(currentPoints);

                    ProxyBuilder.SimpleCallback<User> callback=user-> updateUserFunction(user);
                    ServerSingleton.getInstance().editUserById(getApplicationContext(),callback,
                            CurrentUserSingleton.getInstance(getApplicationContext()).getId(),updatedCurrentUser);

                    gameInstance.setQuizzedSolvedAtIndex();
                    gameInstance.incrementPointsByTen();
                    setFields();
                }


            }
        });
    }


    public void pressButtonB()
    {
        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(gameInstance.getQuizzesSolved().get(gameInstance.getQuizID())==1)
                {
                    Toast.makeText(getApplicationContext(),"Question attempted",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    gameInstance.setQuizzedSolvedAtIndex();
                }
                if(Quiz.answers[gameInstance.getQuizID()]==2)
                {
                    //correct answer
                    currentPoints+=10;
                    int points=updatedCurrentUser.getTotalPointsEarned()+10;
                    updatedCurrentUser.setTotalPointsEarned(points);
                    updatedCurrentUser.setCurrentPoints(currentPoints);

                    ProxyBuilder.SimpleCallback<User> callback=user-> updateUserFunction(user);
                    ServerSingleton.getInstance().editUserById(getApplicationContext(),callback,
                            CurrentUserSingleton.getInstance(getApplicationContext()).getId(),updatedCurrentUser);

                    gameInstance.setQuizzedSolvedAtIndex();
                    gameInstance.incrementPointsByTen();
                    setFields();
                }


            }
        });
    }
    public void pressButtonC()
    {
        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameInstance.getQuizzesSolved().get(gameInstance.getQuizID())==1)
                {
                    Toast.makeText(getApplicationContext(),"Question attempted",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    gameInstance.setQuizzedSolvedAtIndex();
                }
                if(Quiz.answers[gameInstance.getQuizID()]==3)
                {
                    //correct answer
                    currentPoints+=10;
                    int points=updatedCurrentUser.getTotalPointsEarned()+10;
                    updatedCurrentUser.setTotalPointsEarned(points);
                    updatedCurrentUser.setCurrentPoints(currentPoints);

                    ProxyBuilder.SimpleCallback<User> callback=user-> updateUserFunction(user);
                    ServerSingleton.getInstance().editUserById(getApplicationContext(),callback,
                            CurrentUserSingleton.getInstance(getApplicationContext()).getId(),updatedCurrentUser);

                    gameInstance.setQuizzedSolvedAtIndex();
                    gameInstance.incrementPointsByTen();
                    setFields();
                }
            }
        });
    }
    public void pressButtonD()
    {
        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gameInstance.getQuizzesSolved().get(gameInstance.getQuizID())==1)
                {
                    Toast.makeText(getApplicationContext(),"Question attempted",Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    gameInstance.setQuizzedSolvedAtIndex();
                }
                if(Quiz.answers[gameInstance.getQuizID()]==4)
                {
                    //correct answer
                    currentPoints+=10;
                    int points=updatedCurrentUser.getTotalPointsEarned()+10;
                    updatedCurrentUser.setTotalPointsEarned(points);
                    updatedCurrentUser.setCurrentPoints(currentPoints);

                    ProxyBuilder.SimpleCallback<User> callback=user-> updateUserFunction(user);
                    ServerSingleton.getInstance().editUserById(getApplicationContext(),callback,
                            CurrentUserSingleton.getInstance(getApplicationContext()).getId(),updatedCurrentUser);

                    gameInstance.setQuizzedSolvedAtIndex();
                    gameInstance.incrementPointsByTen();
                    setFields();
                }


            }
        });
    }
    public void nextQuestion()
    {
        Button btnNext=findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameInstance.incrementQuizID();
                setFields();
            }
        });


    }
    public void previousQuestion()
    {
        Button btnPrevious=findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameInstance.decrementQuizID();
                setFields();
            }
        });

    }

    private void updateUserFunction(User user) {
    }


    public void goBack()
    {
        Button btnBack=findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}
