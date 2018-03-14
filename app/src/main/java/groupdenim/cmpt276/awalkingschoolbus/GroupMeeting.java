package groupdenim.cmpt276.awalkingschoolbus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by wwwfl on 2018-03-13.
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
                finish();
            }
        });
    }
}
