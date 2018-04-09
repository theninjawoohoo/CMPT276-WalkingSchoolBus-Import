package groupdenim.cmpt276.awalkingschoolbus.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import groupdenim.cmpt276.awalkingschoolbus.R;
import groupdenim.cmpt276.awalkingschoolbus.fragments.IncomingRequestsFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.MyRequestsFragment;
import groupdenim.cmpt276.awalkingschoolbus.fragments.SectionsPageAdapter;

public class RequestsActivity extends AppCompatActivity {

    private SectionsPageAdapter sectionsPageAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.container_requests);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.request_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new IncomingRequestsFragment(), "Incoming Requests");
        adapter.addFragment(new MyRequestsFragment(), "My Requests");
        viewPager.setAdapter(adapter);
    }
}
