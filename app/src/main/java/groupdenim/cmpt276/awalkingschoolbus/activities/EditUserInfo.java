package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class EditUserInfo extends AppCompatActivity {

    User updatedCurrentUser;
    static final int DIALOG_ID=0;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        updatedCurrentUser=new User();
        ProxyBuilder.SimpleCallback<User> callback=user -> setUserFields(user);
        ServerSingleton.getInstance().getUserById(getApplicationContext(),callback, CurrentUserSingleton.getInstance(getApplicationContext()).getId());
        //here we have acquired the latest current userObject

        Button btnDate=findViewById(R.id.btnDate);
        selectDate(btnDate);




        //go back by deleting the current activity
        Button back=findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //Button to open Date Dialog
    public void selectDate(Button btnDate)
    {
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID);
            }
        });
    }

    //creates the Date Picker Dialog
    protected Dialog onCreateDialog(int id)
    {
        if(id==DIALOG_ID)
        {
            DatePickerDialog dialog = new DatePickerDialog(this, dPickListener, year, month, day);
            dialog.getDatePicker().setMinDate(30 * 12 * 24 * 60 * 60 * 1000L);
            return dialog;
            //return new DatePickerDialog(this,dPickListener,year,month,day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener dPickListener
            =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int yearX, int monthX, int dayX) {
            updatedCurrentUser.setBirthYear(Integer.toString(yearX));
            updatedCurrentUser.setBirthMonth(monthX);
            //Toast.makeText(EditUserInfo.this,dayX+"/"+monthX+"/"+updatedCurrentUser.getBirthYear(),Toast.LENGTH_LONG).show();
        }
    };

    public void setUserFields(User user)
    {
        updatedCurrentUser.deepCopyUserFields(user);
    }

    //class ends here
}
