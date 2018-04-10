package groupdenim.cmpt276.awalkingschoolbus.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.GamificationQuiz;
import groupdenim.cmpt276.awalkingschoolbus.userModel.JSONGamification;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

/**
 * Created by wwwfl on 2018-04-09.
 */

public class BuyItemFragment extends AppCompatDialogFragment {
    int referPrice;
    GamificationQuiz gameInstance = GamificationQuiz.getInstance();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String nickname = getArguments().getString("nickName");
        final int price = getArguments().getInt("priceTag");

        referPrice = price;
        //Create view
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_buy_item, null);

        //Set display message
        TextView textView = v.findViewById(R.id.textView_BuyItemFragment);
        String message = "Are you sure you want to buy " + "\"" + nickname + "\""
                            + " for " + price + " points"+ "?";
        textView.setText(message);

        //Build dialog
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.yes, null)
                .setNegativeButton(android.R.string.cancel, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button buttonYes = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button buttonCancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                buttonYes.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(hasEnough()) {
                            ProxyBuilder.SimpleCallback<User> callback = user -> {
                                try {
                                    getUser(user, nickname);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            };
                            ServerSingleton.getInstance().getUserById(getActivity(), callback,
                                    CurrentUserSingleton.getInstance(BuyItemFragment.this.getContext()).getId());

                        }

                        buttonYes.setEnabled(false);
                        buttonCancel.setEnabled(false);
                        dialog.setCanceledOnTouchOutside(false);
                    }
                });
            }
        });

        return dialog;
    }

    private void getUser(User user, String nickName) throws IOException {
        JSONGamification userJSONGame;
        if(user.getCustomJson() != null) {
            ObjectMapper mapper = new ObjectMapper();
            userJSONGame = mapper.readValue(user.getCustomJson(), JSONGamification.class);
            userJSONGame.setUserNickname(nickName);
            String newJSONString = mapper.writeValueAsString(userJSONGame);
            user.setCustomJson(newJSONString);
        }
        user.setCurrentPoints(user.getCurrentPoints() - referPrice);
        gameInstance.setPoints(user.getCurrentPoints());

        ProxyBuilder.SimpleCallback<User> callback= diffUser -> doNothing(diffUser);
        ServerSingleton.getInstance().editUserById(BuyItemFragment.this.getContext(),callback,
                CurrentUserSingleton.getInstance(BuyItemFragment.this.getContext()).getId(), user);


    }

    private void doNothing(User diffUser) {
        dismiss();
    }

    public boolean hasEnough() {
        int currentPoints = gameInstance.getPoints();
        if(currentPoints >= referPrice) {
            return true;
        }
        else {
            Toast.makeText(BuyItemFragment.this.getContext(), "You don't have enough", Toast.LENGTH_LONG).show();
            return false;
        }
    }


}
