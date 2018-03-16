package groupdenim.cmpt276.awalkingschoolbus.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import org.w3c.dom.Text;

import groupdenim.cmpt276.awalkingschoolbus.R;

/**
 * Make a custom Google maps info display window
 * Code based off of https://www.youtube.com/watch?v=DhYofrJPzlI
 */

public class customWindow implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private Context mContext;

    public customWindow(Context context) {
        mContext = context;
        mWindow = LayoutInflater.from(context).inflate(R.layout.info_window_display, null);
    }

    private void renderWindow(Marker marker, View view) {
        String title = marker.getTitle();
        TextView TitleTextView = (TextView) view.findViewById(R.id.title);

        if(!title.equals("")){
            TitleTextView.setText(title);
        }

        String info = marker.getSnippet();
        TextView InfoTextView = (TextView) view.findViewById(R.id.snippet);
        if(!info.equals("")){
            InfoTextView.setText(info);
        }


    }

    @Override
    public View getInfoWindow(Marker marker) {
        renderWindow(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        renderWindow(marker, mWindow);
        return mWindow;
    }
}
