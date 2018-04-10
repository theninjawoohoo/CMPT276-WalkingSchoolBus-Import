package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.fragments.BuyItemFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.GroupInfoDeleteFragment;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.GamificationQuiz;
import groupdenim.cmpt276.awalkingschoolbus.userModel.JSONGamification;
import groupdenim.cmpt276.awalkingschoolbus.userModel.ShopNicknameItems;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

/**
 * Created by wwwfl on 2018-04-06.
 */


public class ShopActivity extends AppCompatActivity {

    TextView pointsView;
    TextView debugView;
    Button refreshButton;
    private ListView listView;
    GamificationQuiz gameInstance = GamificationQuiz.getInstance();
    ObjectMapper mapper = new ObjectMapper();
    JSONGamification unlockedItems;
    private int mode = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        pointsView = (TextView) findViewById(R.id.yourPoints);
        debugView = (TextView) findViewById(R.id.debug);
        refreshButton = (Button) findViewById(R.id.btn_refresh);

        //Proxy to get the current user points
        ProxyBuilder.SimpleCallback<User> callback= user -> {
            try {
                setFields(user);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        ServerSingleton.getInstance().getUserById(getApplicationContext(),callback,
                CurrentUserSingleton.getInstance(getApplicationContext()).getId());

        setupRefresh();

    }

    private void updatePoints() {
        ProxyBuilder.SimpleCallback<User> callback= user -> {
            try {
                setFields(user);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        ServerSingleton.getInstance().getUserById(getApplicationContext(),callback,
                CurrentUserSingleton.getInstance(getApplicationContext()).getId());
        debugView.setText("" + unlockedItems.getUserNickname());
        pointsView.setText("" + gameInstance.getPoints());
    }

    private void setFields(User user) throws IOException {
        if(user.getCustomJson() == null) {

            Log.d("TAGGED", "user.getCustomJson() == null");

            JSONGamification newJSONGame = new JSONGamification();
            unlockedItems = newJSONGame;

            String newJSONString = mapper.writeValueAsString(newJSONGame);
            user.setCustomJson(newJSONString);

            ProxyBuilder.SimpleCallback<User> callback= diffUser -> doNothing(diffUser);
            ServerSingleton.getInstance().editUserById(getApplicationContext(),callback,
                    CurrentUserSingleton.getInstance(getApplicationContext()).getId(), user);
        }
        else {
            Log.d("TAGGED", "user.getCustomJson() != null");
            unlockedItems = mapper.readValue(user.getCustomJson(), JSONGamification.class);
        }

        initializeShopList();
        gameInstance.setPoints(user.getCurrentPoints());
        pointsView.setText("" + gameInstance.getPoints());
        debugView.setText("" + unlockedItems.getUserNickname());
    }

    private void initializeShopList() {
        String[] cheapNames = ShopNicknameItems.cheapNames;
        List<String> tempList = new ArrayList<>();
        boolean[] cheapUnlocks = unlockedItems.getUnlockedCheapNames();

        for(int i = 0; i < cheapNames.length; i++) {
            if(!cheapUnlocks[i]) {
                tempList.add(cheapNames[i] + " - Cost 25 points");
            }
            else {
                tempList.add(cheapNames[i] + " - Unlocked");
            }

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                                                                R.layout.groupitem,
                                                                tempList);

        listView = (ListView) findViewById(R.id.theShopList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(!cheapUnlocks[i]) {
                    specialDialogPopUp(cheapNames, i, 25);
                }
                else {
                    specialDialogPopUp(cheapNames, i, 0);
                }
                updatePoints();

            }
        });


    }


    public void doNothing(User diffUser) {

    }

    public void specialDialogPopUp(String[] cheapNames, int i, int currentPrice) {
        Bundle bundle = new Bundle();
        bundle.putString("nickName", cheapNames[i]);
        bundle.putInt("priceTag", currentPrice);

        FragmentManager manager = getSupportFragmentManager();
        BuyItemFragment dialog = new BuyItemFragment();
        dialog.setArguments(bundle);
        dialog.show(manager, "MessageDialog");
    }

    private void setupRefresh() {
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePoints();
            }
        });
    }

}
