package ru.spbau.mit.starlab.financialassistant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.app.DialogFragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ru.spbau.mit.starlab.financialassistant.fragments.CreditsFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.ExpensesFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.HelpFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.IncomesFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.RecentActionsFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.RegularExpensesFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.RegularIncomesFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.ShowStatisticsFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.StatisticsFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.ToolsFragment;
import ru.spbau.mit.starlab.financialassistant.fragments.WantedExpensesFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase mSqLiteDatabase;

    private CreditsFragment creditsFragment;
    private ExpensesFragment expensesFragment;
    private IncomesFragment incomesFragment;
    private RecentActionsFragment recentActionsFragment;
    private RegularExpensesFragment regularExpensesFragment;
    private RegularIncomesFragment regularIncomesFragment;
    private StatisticsFragment statisticsFragment;
    private ToolsFragment toolsFragment;
    private WantedExpensesFragment wantedExpensesFragment;

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new ru.spbau.mit.starlab.financialassistant.fragments.DatePicker();
        Bundle args = new Bundle();
        args.putInt("txtDateId", v.getId());
        newFragment.setArguments(args);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onShowStatisticsBtnClick(View v) {
        DialogFragment fragment = new ShowStatisticsFragment();
        Bundle args = new Bundle();
        RadioButton radioButton = (RadioButton) findViewById(R.id.rBtnStatistics);
        TextView dateBegin = (TextView) findViewById(R.id.eTxtStatisticsStartPeriod);
        TextView dateEnd = (TextView) findViewById(R.id.eTxtStatisticsEndPeriod);
        args.putBoolean("isStatistics", radioButton.isChecked());
        if (radioButton.isChecked()) {
            args.putString("dateBegin", dateBegin.getText().toString());
            args.putString("dateEnd", dateEnd.getText().toString());
        }
        ArrayList<String> dateList = new ArrayList<>();
        List<String> categoryNameList = new ArrayList<>();
        List<Double> sumList = new ArrayList<>();
        getDataForStatistics(dateList, categoryNameList, sumList);

        String[] dates = new String[sumList.size()];
        String[] categories = new String[sumList.size()];
        double[] sums = new double[sumList.size()];
        for (int i = 0; i < sumList.size(); i++) {
            dates[i] = dateList.get(i);
            categories[i] = categoryNameList.get(i);
            sums[i] = sumList.get(i);
        }
        args.putStringArray("dateList", dates);
        args.putStringArray("categoryNameList", categories);
        args.putDoubleArray("sumList", sums);

        fragment.setArguments(args);
        fragment.show(getFragmentManager(), "showStatistics");
    }

    public void getDataForStatistics(List<String> dateList, List<String> categoryNameList, List<Double> sumList) {
        List<Integer> categoryIdList = new ArrayList<>();
        String query = "SELECT " + DatabaseHelper._ID + ", "
                + DatabaseHelper.EXPENSE_DATE_COLUMN + ", "
                + DatabaseHelper.EXPENSE_CATEGORY_COLUMN + ", "
                + DatabaseHelper.EXPENSE_SUM_COLUMN + " FROM expenses";
        Cursor cursor = mSqLiteDatabase.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(DatabaseHelper._ID));
            int categoryId = cursor.getInt(cursor
                    .getColumnIndex(DatabaseHelper.EXPENSE_CATEGORY_COLUMN));
            String date = cursor.getString(cursor
                    .getColumnIndex(DatabaseHelper.EXPENSE_DATE_COLUMN));
            Double sum = cursor.getDouble(cursor
                    .getColumnIndex(DatabaseHelper.EXPENSE_SUM_COLUMN));

            categoryIdList.add(categoryId);
            dateList.add(date);
            sumList.add(sum);

            Log.i("LOG_TAG", "New data added: categoryId: " + categoryId + " date: " + date
                    + " sum: " + sum);
        }
        cursor.close();

        for (int i = 0; i < categoryIdList.size(); i++) {
            String queryForCategory = "SELECT " + DatabaseHelper._ID + ", "
                    + DatabaseHelper.CATEGORY_NAME_COLUMN + " FROM categories WHERE "
                    + DatabaseHelper._ID + " = " + categoryIdList.get(i);
            Cursor cursor2 = mSqLiteDatabase.rawQuery(queryForCategory, null);

            while (cursor2.moveToNext()) {
                int id = cursor2.getInt(cursor2
                        .getColumnIndex(DatabaseHelper._ID));
                String categoryName = cursor2.getString(cursor2
                        .getColumnIndex(DatabaseHelper.CATEGORY_NAME_COLUMN));

                categoryNameList.add(categoryName);

                Log.i("LOG_TAG", "New data added: categoryName: " + categoryName + " id: " + id);

            }
            cursor2.close();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDatabaseHelper = new DatabaseHelper(this, "finance.db", null, 6);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        creditsFragment = new CreditsFragment();
        expensesFragment = new ExpensesFragment();
        incomesFragment = new IncomesFragment();
        recentActionsFragment = new RecentActionsFragment();
        regularExpensesFragment = new RegularExpensesFragment();
        regularIncomesFragment = new RegularIncomesFragment();
        statisticsFragment = new StatisticsFragment();
        toolsFragment = new ToolsFragment();
        wantedExpensesFragment = new WantedExpensesFragment();

    }

    public void onClick(View v) {
        Cursor cursor = mSqLiteDatabase.query("expenses", new String[]{
                        DatabaseHelper._ID, DatabaseHelper.EXPENSE_NAME_COLUMN}, null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
            String name = cursor.getString(cursor
                    .getColumnIndex(DatabaseHelper.EXPENSE_NAME_COLUMN));

            //TextView infoTextView = (TextView) findViewById(R.id.txtComment);
            //infoTextView.setText("Категория траты: " + name);

            Log.i("LOG_TAG", "Трата " + name + " имеет id " + id);
        }
        cursor.close();
    }

    public int parseCategory(String category) {
        int category_id = 0;
        Cursor cursor = mSqLiteDatabase.query("categories", new String[]{
                        DatabaseHelper._ID, DatabaseHelper.CATEGORY_NAME_COLUMN}, null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
            String name = cursor.getString(cursor
                    .getColumnIndex(DatabaseHelper.CATEGORY_NAME_COLUMN));


            if (category.equals(name)) {
                Log.i("LOG_TAG", "Category " + name + " is in database and have id: " + id);
                category_id = id;
            }
        }
        cursor.close();
        return category_id;
    }

    public int addCategory(String categoryName) {
        int categoryId = 0;
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseHelper.CATEGORY_NAME_COLUMN, categoryName);

        mSqLiteDatabase.insert("categories", null, newValues);

        Cursor cursor = mSqLiteDatabase.query("categories", new String[]{
                        DatabaseHelper._ID}, null, null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
            categoryId = id;
        }
        cursor.close();
        return categoryId;
    }

    public void addDataToLastActions(int id, String category, String name, String sum){
        ContentValues newValues = new ContentValues();

        newValues.put(DatabaseHelper.LAST_ACTIONS_ID_COLUMN, id);
        newValues.put(DatabaseHelper.LAST_ACTIONS_CATEGORY_COLUMN, category);
        newValues.put(DatabaseHelper.LAST_ACTIONS_NAME_COLUMN, name);
        newValues.put(DatabaseHelper.LAST_ACTIONS_SUM_COLUMN, sum);

        mSqLiteDatabase.insert("last_actions", null, newValues);
        Log.i("LOG_TAG", "New last_action added");
    }

    public void addNewExpense(View v) {

        ContentValues newValues = new ContentValues();

        TextView name = (TextView) findViewById(R.id.eTxtExpName);
        String expenseName = name.getText().toString();

        newValues.put(DatabaseHelper.EXPENSE_NAME_COLUMN, expenseName);

        TextView categoryTextView = (TextView) findViewById(R.id.eTxtExpCategory);
        String category = categoryTextView.getText().toString();

        int categoryId = parseCategory(category);
        if (categoryId == 0) {
            categoryId = addCategory(category);
        }
        newValues.put(DatabaseHelper.EXPENSE_CATEGORY_COLUMN, categoryId);

        TextView sum = (TextView) findViewById(R.id.eTxtExpSum);
        String expenseSum = sum.getText().toString();

        newValues.put(DatabaseHelper.EXPENSE_SUM_COLUMN, expenseSum);

        TextView comment = (TextView) findViewById(R.id.eTxtExpComment);
        String expenseComment = comment.getText().toString();
        newValues.put(DatabaseHelper.EXPENSE_COMMENT_COLUMN, expenseComment);

        TextView date = (TextView) findViewById(R.id.eTxtExpDate);
        String expenseDate = date.getText().toString();

        newValues.put(DatabaseHelper.EXPENSE_DATE_COLUMN, expenseDate);

        Date curDate = new Date();
        String expenseAddTime = curDate.toString();
        newValues.put(DatabaseHelper.EXPENSE_ADD_TIME_COLUMN, expenseAddTime);

        mSqLiteDatabase.insert("expenses", null, newValues);
        Log.i("LOG_TAG", "New expense added");

        String query = "SELECT " + DatabaseHelper._ID + " FROM expenses";
        Cursor cursor = mSqLiteDatabase.rawQuery(query, null);

        int idExp = 0;
        while (cursor.moveToNext()) {
            idExp = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
        }
        cursor.close();

        String categoryExp = "Трата";
        addDataToLastActions(idExp, categoryExp, expenseName, expenseSum);

    }

    public void addNewIncome(View v) {
        ContentValues newValues = new ContentValues();

        TextView name = (TextView) findViewById(R.id.eTxtIncName);
        String incomeName = name.getText().toString();
        newValues.put(DatabaseHelper.INCOME_NAME_COLUMN, incomeName);

        TextView sum = (TextView) findViewById(R.id.eTxtIncSum);
        String incomeSum = sum.getText().toString();
        newValues.put(DatabaseHelper.INCOME_SUM_COLUMN, incomeSum);

        TextView comment = (TextView) findViewById(R.id.eTxtIncComment);
        String incomeComment = comment.getText().toString();
        newValues.put(DatabaseHelper.INCOME_COMMENT_COLUMN, incomeComment);

        TextView date = (TextView) findViewById(R.id.eTxtIncDate);
        String incomeDate = date.getText().toString();
        newValues.put(DatabaseHelper.INCOME_DATE_COLUMN, incomeDate);

        Date curDate = new Date();
        String incomeAddTime = curDate.toString();
        newValues.put(DatabaseHelper.INCOME_ADD_TIME_COLUMN, incomeAddTime);

        mSqLiteDatabase.insert("incomes", null, newValues);
        Log.i("LOG_TAG", "New income added");

        String query = "SELECT " + DatabaseHelper._ID + " FROM incomes";
        Cursor cursor = mSqLiteDatabase.rawQuery(query, null);

        int idInc = 0;
        while (cursor.moveToNext()) {
            idInc = cursor.getInt(cursor.getColumnIndex(DatabaseHelper._ID));
        }
        cursor.close();

        String categoryInc = "Доход";
        addDataToLastActions(idInc, categoryInc, incomeName, incomeSum);
    }

    public void addNewRegExpense(View v) {

        ContentValues newValues = new ContentValues();

        TextView name = (TextView) findViewById(R.id.eTxtRegExpName);
        String regExpenseName = name.getText().toString();

        newValues.put(DatabaseHelper.REG_EXPENSE_NAME_COLUMN, regExpenseName);

        TextView categoryTextView = (TextView) findViewById(R.id.eTxtRegExpCategory);
        String category = categoryTextView.getText().toString();

        int categoryId = parseCategory(category);
        if (categoryId == 0) {
            categoryId = addCategory(category);
        }
        newValues.put(DatabaseHelper.REG_EXPENSE_CATEGORY_COLUMN, categoryId);

        TextView sum = (TextView) findViewById(R.id.eTxtRegExpSum);
        String regExpenseSum = sum.getText().toString();

        newValues.put(DatabaseHelper.REG_EXPENSE_SUM_COLUMN, regExpenseSum);

        TextView startPeriod = (TextView) findViewById(R.id.eTxtRegExpStartPeriod);
        String regExpStartPeriod = startPeriod.getText().toString();
        newValues.put(DatabaseHelper.REG_EXPENSE_START_PERIOD_COLUMN, regExpStartPeriod);

        TextView endPeriod = (TextView) findViewById(R.id.eTxtRegExpEndPeriod);
        String regExpEndPeriod = endPeriod.getText().toString();
        newValues.put(DatabaseHelper.REG_EXPENSE_END_PERIOD_COLUMN, regExpEndPeriod);

        TextView comment = (TextView) findViewById(R.id.eTxtRegExpComment);
        String regExpenseComment = comment.getText().toString();
        newValues.put(DatabaseHelper.REG_EXPENSE_COMMENT_COLUMN, regExpenseComment);

        Date curDate = new Date();
        String regExpenseAddTime = curDate.toString();
        newValues.put(DatabaseHelper.REG_EXPENSE_ADD_TIME_COLUMN, regExpenseAddTime);

        mSqLiteDatabase.insert("reg_expenses", null, newValues);
        Log.i("LOG_TAG", "New regular expense added");

    }

    public void addNewRegIncome(View v) {

        ContentValues newValues = new ContentValues();

        TextView name = (TextView) findViewById(R.id.eTxtRegIncName);
        String regIncName = name.getText().toString();
        newValues.put(DatabaseHelper.REG_INCOME_NAME_COLUMN, regIncName);

        TextView sum = (TextView) findViewById(R.id.eTxtRegIncSum);
        String regIncSum = sum.getText().toString();

        newValues.put(DatabaseHelper.REG_INCOME_SUM_COLUMN, regIncSum);

        TextView startPeriod = (TextView) findViewById(R.id.eTxtRegIncStartPeriod);
        String regIncStartPeriod = startPeriod.getText().toString();
        newValues.put(DatabaseHelper.REG_INCOME_START_PERIOD_COLUMN, regIncStartPeriod);

        TextView endPeriod = (TextView) findViewById(R.id.eTxtRegIncEndPeriod);
        String regIncEndPeriod = endPeriod.getText().toString();
        newValues.put(DatabaseHelper.REG_INCOME_END_PERIOD_COLUMN, regIncEndPeriod);

        TextView comment = (TextView) findViewById(R.id.eTxtRegIncComment);
        String regIncComment = comment.getText().toString();
        newValues.put(DatabaseHelper.REG_INCOME_COMMENT_COLUMN, regIncComment);

        Date curDate = new Date();
        String regIncomeAddTime = curDate.toString();
        newValues.put(DatabaseHelper.REG_INCOME_ADD_TIME_COLUMN, regIncomeAddTime);

        mSqLiteDatabase.insert("reg_incomes", null, newValues);
        Log.i("LOG_TAG", "New regular income added");

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
        } else if (id == R.id.nav_regular_incomes) {
            fragmentTransaction.replace(R.id.container, regularIncomesFragment);
        }
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
