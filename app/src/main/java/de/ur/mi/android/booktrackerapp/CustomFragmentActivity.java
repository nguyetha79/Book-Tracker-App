package de.ur.mi.android.booktrackerapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import de.ur.mi.android.booktrackerapp.Fragments.Alarm.AlarmManagerFragment;
import de.ur.mi.android.booktrackerapp.Fragments.Search.SearchFragment;
import de.ur.mi.android.booktrackerapp.Fragments.Home.ShowAllBooksFragment;

public class CustomFragmentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private NavigationView navView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_fragment);

        drawer = findViewById(R.id.drawer_layout);
        bottomNav = findViewById(R.id.bottom_nav);
        navView = findViewById(R.id.nav_view);

        initMenuButton();
        setUpDrawer();
        setUpNavView();
        setUpBottomNav();
        goToFragment(new ShowAllBooksFragment());
        setUpShortcuts();
    }

    private void initMenuButton() {
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawer,
                R.string.nav_drawer_open,
                R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }



    private void setUpNavView() {
        navView.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) item -> {
            item.setCheckable(false);
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home_drawer:
                    selectedFragment = new ShowAllBooksFragment();
                    break;
                case R.id.nav_search_drawer:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_alarm_drawer:
                    selectedFragment = new AlarmManagerFragment();
                    break;
            }
            goToFragment(selectedFragment);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpBottomNav() {
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.nav_home_bottom:
                    selectedFragment = new ShowAllBooksFragment();
                    break;
                case R.id.nav_search_bottom:
                    selectedFragment = new SearchFragment();
                    break;
                case R.id.nav_alarm_bottom:_bottom:
                    selectedFragment = new AlarmManagerFragment();
                    break;
            }

            goToFragment(selectedFragment);

            return true;
        });
    }

    private void setUpShortcuts() {

        if ("HOME_SHORTCUT".equals(getIntent().getAction())) {
            goToFragment(new ShowAllBooksFragment());
        }

        if ("SEARCH_SHORTCUT".equals(getIntent().getAction())) {
            goToFragment(new SearchFragment());
        }

        if ("ALARM_SHORTCUT".equals(getIntent().getAction())) {
            goToFragment(new AlarmManagerFragment());
        }
    }

    private void goToFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                fragment).commit();
    }

}