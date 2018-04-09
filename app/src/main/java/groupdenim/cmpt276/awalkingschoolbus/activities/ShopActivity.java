package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.Gamification;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

/**
 * Created by wwwfl on 2018-04-06.
 */


public class ShopActivity extends AppCompatActivity {

    TextView pointsView;
    Gamification gameInstance = Gamification.getInstance();

    public static User updatedUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);


        pointsView = (TextView) findViewById(R.id.txtViewPoints);
        updatedUser = new User();

        //Proxy to get the current user points
        ProxyBuilder.SimpleCallback<User> callback= user -> setFields(user);
        ServerSingleton.getInstance().getUserById(getApplicationContext(),callback,
                CurrentUserSingleton.getInstance(getApplicationContext()).getId());

        updatePoints();


    }

    private void updatePoints() {
        pointsView.setText(Integer.toString(gameInstance.getPoints()));
    }
    private void setFields(User user) {
        updatedUser.deepCopyUserFields(user);
        gameInstance.setPoints(updatedUser.getTotalPointsEarned());
        pointsView.setText(Integer.toString(gameInstance.getPoints()));
    }





}
