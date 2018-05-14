package org.gakendor.ubpdaily.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import org.gakendor.ubpdaily.R;
import org.gakendor.ubpdaily.adapter.SectionPagerAdapter;
import org.gakendor.ubpdaily.fragment.EventsFragment;
import org.gakendor.ubpdaily.fragment.HomeFragment;
import org.gakendor.ubpdaily.fragment.MyEventsFragment;
import org.gakendor.ubpdaily.fragment.SettingFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.container)
    ViewPager container;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SectionPagerAdapter sectionPagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());
        sectionPagerAdapter.addFragment(new HomeFragment());
        sectionPagerAdapter.addFragment(new EventsFragment());
        sectionPagerAdapter.addFragment(new MyEventsFragment());
        sectionPagerAdapter.addFragment(new SettingFragment());

        container.setAdapter(sectionPagerAdapter);
        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        bottomNavigation.getMenu().findItem(R.id.act_home).setChecked(true);
                        break;
                    case 1:
                        bottomNavigation.getMenu().findItem(R.id.act_events).setChecked(true);
                        break;
                    case 2:
                        bottomNavigation.getMenu().findItem(R.id.act_your_events).setChecked(true);
                        break;
                    case 3:
                        bottomNavigation.getMenu().findItem(R.id.act_setting).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                bottomNavigation.getMenu().findItem(item.getItemId()).setChecked(true);
                switch (item.getItemId()){
                    case R.id.act_home:
                        container.setCurrentItem(0);
                        break;
                    case R.id.act_events:
                        container.setCurrentItem(1);
                        break;
                    case R.id.act_your_events:
                        container.setCurrentItem(2);
                        break;
                    case R.id.act_setting:
                        container.setCurrentItem(3);
                        break;
                }
                return false;
            }
        });

    }
}
