package groupdenim.cmpt276.awalkingschoolbus;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * This class generates a arraylist of place objects you
 */



public class GroupMeeting extends AppCompatActivity{
    private Button switchMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmeeting);
        switchMap = (Button) findViewById(R.id.btn_on_screen_two_map);
        initialize();
    }

    private void initialize() {
        switchMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapIntent = new Intent(GroupMeeting.this, MapActivity.class);
                startActivity(mapIntent);
                finish();
            }
        });
    }
}
