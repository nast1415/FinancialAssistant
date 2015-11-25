package ru.spbau.mit.starlab.financialassistant;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import ru.spbau.mit.starlab.financialassistant.fragments.CreditsFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.ExpensesFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.HelpFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.IncomesFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.RecentActionsFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.RegularExpensesFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.StatisticsFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.ToolsFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.WantedExpensesFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CreditsFragment creditsFragment;
    private ExpensesFragment expensesFragment;
    private HelpFragment helpFragment;
    private IncomesFragment incomesFragment;
    private RecentActionsFragment recentActionsFragment;
    private RegularExpensesFragment regularExpensesFragment;
    private StatisticsFragment statisticsFragment;
    private ToolsFragment toolsFragment;
    private WantedExpensesFragment wantedExpensesFragment;

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


        creditsFragment = new CreditsFragment();
        expensesFragment = new ExpensesFragment();
        helpFragment = new HelpFragment();
        incomesFragment = new IncomesFragment();
        recentActionsFragment = new RecentActionsFragment();
        regularExpensesFragment = new RegularExpensesFragment();
        statisticsFragment = new StatisticsFragment();
        toolsFragment = new ToolsFragment();
        wantedExpensesFragment = new WantedExpensesFragment();
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

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

        if (id == R.id.nav_credits) {
            fragmentTransaction.replace(R.id.container, creditsFragment);
        } else if (id == R.id.nav_expenses) {
            fragmentTransaction.replace(R.id.container, expensesFragment);
        } else if (id == R.id.nav_help) {
            fragmentTransaction.replace(R.id.container, helpFragment);
        } else if (id == R.id.nav_incomes) {
            fragmentTransaction.replace(R.id.container, incomesFragment);
        } else if (id == R.id.nav_recent_actions) {
            fragmentTransaction.replace(R.id.container, recentActionsFragment);
        } else if (id == R.id.nav_regular_expenses) {
            fragmentTransaction.replace(R.id.container, regularExpensesFragment);
        } else if (id == R.id.nav_statistics) {
            fragmentTransaction.replace(R.id.container, statisticsFragment);
        } else if (id == R.id.nav_tools) {
            fragmentTransaction.replace(R.id.container, toolsFragment);
        } else if (id == R.id.nav_wanted_expenses) {
            fragmentTransaction.replace(R.id.container, wantedExpensesFragment);
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
