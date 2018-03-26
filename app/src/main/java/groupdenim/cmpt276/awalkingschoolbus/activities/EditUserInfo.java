package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ProxyBuilder;
import groupdenim.cmpt276.awalkingschoolbus.serverModel.ServerSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.CurrentUserSingleton;
import groupdenim.cmpt276.awalkingschoolbus.userModel.User;

public class EditUserInfo extends AppCompatActivity {

    //for some reason doesnt change
    public static User updatedCurrentUser;

    Button btnDate;
    Button btnReset;
    Button btnConfirm;
    EditText editTextName;
    EditText editTextEmail;
    EditText editTextAddress;
    EditText editTextTeacher;
    EditText editTextGrade;
    EditText editTextCellPhone;
    EditText editTextHomePhone;
    EditText editTextEmergency;

    //date stuff
    static final int DIALOG_ID=0;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);


        //deep copy stuff into updatedCurrentUser
        updatedCurrentUser=new User();
        ProxyBuilder.SimpleCallback<User> callback=user -> setUserFields(user);
        ServerSingleton.getInstance().getUserById(getApplicationContext(),callback,
                CurrentUserSingleton.getInstance(getApplicationContext()).getEditUserId());
        //here we have acquired the latest current userObject


        btnDate=findViewById(R.id.btnDate);
        selectDate(btnDate);
        editTextName=findViewById(R.id.editTextName);
        editTextEmail=findViewById(R.id.editTextEmail);
        editTextAddress=findViewById(R.id.editTextAddress);
        editTextTeacher=findViewById(R.id.editTextTeacher);
        editTextGrade=findViewById(R.id.editTextGrade);
        editTextCellPhone=findViewById(R.id.editTextCellPhone);
        editTextHomePhone=findViewById(R.id.editTextHomePhone);
        editTextEmergency=findViewById(R.id.editTextEmergency);


        btnReset=findViewById(R.id.btnReset);
        btnConfirm=findViewById(R.id.btnConfirm);

        confirmSelection(btnConfirm);
        resetEditTextFields(btnReset);

        //go back by deleting the current activity
        Button back=findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setEditTextFields()
    {
        editTextName.setText(updatedCurrentUser.getName());
        editTextEmail.setText(updatedCurrentUser.getEmail());
        editTextAddress.setText(updatedCurrentUser.getAddress());
        editTextTeacher.setText(updatedCurrentUser.getTeacherName());
        editTextGrade.setText(updatedCurrentUser.getGrade());
        editTextCellPhone.setText(updatedCurrentUser.getCellPhone());
        editTextHomePhone.setText(updatedCurrentUser.getHomePhone());
        editTextEmergency.setText(updatedCurrentUser.getEmergencyContactInfo());
    }

    public void confirmSelection(Button btnConfirm)
    {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //update the user object & send stuff back to the server

                updatedCurrentUser.setName(editTextName.getText().toString());
                updatedCurrentUser.setEmail(editTextEmail.getText().toString());
                updatedCurrentUser.setAddress(editTextAddress.getText().toString());
                updatedCurrentUser.setTeacherName(editTextTeacher.getText().toString());
                updatedCurrentUser.setGrade(editTextGrade.getText().toString());
                updatedCurrentUser.setCellPhone(editTextCellPhone.getText().toString());
                updatedCurrentUser.setHomePhone(editTextHomePhone.getText().toString());
                updatedCurrentUser.setEmergencyContactInfo(editTextEmergency.getText().toString());

                //here we update the server with our updatedCurrentUser object
                ProxyBuilder.SimpleCallback<User> callback=user-> updateUserFunction(user);
                ServerSingleton.getInstance().editUserById(getApplicationContext(),callback,
                        CurrentUserSingleton.getInstance(getApplicationContext()).getEditUserId(),updatedCurrentUser);

                finish();
            }
        });
    }

    public void resetEditTextFields(Button btnReset)
    {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditTextFields();
            }
        });
    }

    public void updateUserFunction(User user)
    {

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
            dialog.getDatePicker().setMinDate(30 * 12 * 24 * 60 * 60 * 1000L);  //minimum pickable date is 1970
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
            updatedCurrentUser.setBirthMonth(Integer.toString(monthX));
            //Toast.makeText(EditUserInfo.this,dayX+"/"+monthX+"/"+updatedCurrentUser.getBirthYear(),Toast.LENGTH_LONG).show();
        }
    };

    public void setUserFields(User user)
    {
        //updatedCurrentUser=user;

        //my updatedCurrentUser re-initialized everything back to null after this function ends
        updatedCurrentUser.deepCopyUserFields(user);
        setEditTextFields();
    }

    //class ends here
}
