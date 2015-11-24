package info.fandroid.navdrawer;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import info.fandroid.navdrawer.fragments.FragmentHelp;
import info.fandroid.navdrawer.fragments.FragmentRecent;
import info.fandroid.navdrawer.fragments.FragmentRegularExpenses;
import info.fandroid.navdrawer.fragments.FragmentSend;
import info.fandroid.navdrawer.fragments.FragmentSettings;
import info.fandroid.navdrawer.fragments.FragmentShare;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentRegularExpenses fRegularExpenses;
    FragmentHelp fHelp;
    FragmentRecent fRecent;
    FragmentSettings fSettings;
    FragmentSend fsend;
    FragmentShare fshare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fRegularExpenses = new FragmentRegularExpenses();
        fHelp = new FragmentHelp();
        fRecent = new FragmentRecent();
        fSettings = new FragmentSettings();
        fsend = new FragmentSend();
        fshare = new FragmentShare();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction ftrans = getFragmentManager().beginTransaction();

        if (id == R.id.nav_regular_expenses) {
            ftrans.replace(R.id.container, fRegularExpenses);

        } else if (id == R.id.nav_help) {
            ftrans.replace(R.id.container, fHelp);

        } else if (id == R.id.nav_settings) {
            ftrans.replace(R.id.container, fSettings);

        } else if (id == R.id.nav_recent) {
            ftrans.replace(R.id.container, fRecent);

        } else if (id == R.id.nav_share) {
            ftrans.replace(R.id.container, fshare);

        } else if (id == R.id.nav_send) {
            ftrans.replace(R.id.container, fsend);

        } ftrans.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
