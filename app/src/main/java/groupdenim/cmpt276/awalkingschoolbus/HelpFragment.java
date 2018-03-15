package groupdenim.cmpt276.awalkingschoolbus;


import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A help dialog box to show what the buttons do on the map
 */

public class HelpFragment extends DialogFragment {
    private final String TAG = "TAG";

    private Button Ok;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_helpbox, container, false);
        Ok = (Button)view.findViewById(R.id.btn_okhelp);

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: closing dialog box");
                getDialog().dismiss();
            }
        });

        return view;
    }
}
