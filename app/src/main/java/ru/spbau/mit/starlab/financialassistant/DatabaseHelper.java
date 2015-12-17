package ru.spbau.mit.starlab.financialassistant;


import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private static final String DATABASE_NAME = "finance.db";
    private static final int DATABASE_VERSION = 5;

    //------------------Create table category------------------
    private static final String CATEGORIES_TABLE = "categories";
    public static final String CATEGORY_NAME_COLUMN = "category_name";

    private static final String CATEGORIES_CREATE_SCRIPT = "create table "
            + CATEGORIES_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + CATEGORY_NAME_COLUMN
            + " text not null);";

    //------------------Create table expenses------------------
    private static final String EXPENSES_TABLE = "expenses";
    public static final String EXPENSE_NAME_COLUMN = "expense_name";
    public static final String EXPENSE_CATEGORY_COLUMN = "expense_category";
    public static final String EXPENSE_SUM_COLUMN = "expense_sum";
    public static final String EXPENSE_COMMENT_COLUMN = "expense_comment";
    public static final String EXPENSE_DATE_COLUMN = "expense_date";
    public static final String EXPENSE_ADD_TIME_COLUMN = "expense_add_time";

    private static final String EXPENSES_CREATE_SCRIPT = "create table "
            + EXPENSES_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + EXPENSE_NAME_COLUMN
            + " text not null, " + EXPENSE_CATEGORY_COLUMN + " integer, "
            + EXPENSE_SUM_COLUMN + " real, " + EXPENSE_COMMENT_COLUMN + " text, "
            + EXPENSE_DATE_COLUMN + " text not null, " + EXPENSE_ADD_TIME_COLUMN +
            " text not null);";

    //-------------------Create table incomes------------------
    private static final String INCOMES_TABLE = "incomes";
    public static final String INCOME_NAME_COLUMN = "income_name";
    public static final String INCOME_SUM_COLUMN = "income_sum";
    public static final String INCOME_COMMENT_COLUMN = "income_comment";
    public static final String INCOME_DATE_COLUMN = "income_date";
    public static final String INCOME_ADD_TIME_COLUMN = "income_add_time";

    private static final String INCOMES_CREATE_SCRIPT = "create table "
            + INCOMES_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + INCOME_NAME_COLUMN
            + " text not null, " + INCOME_SUM_COLUMN + " real, " + INCOME_COMMENT_COLUMN + " text, "
            + INCOME_DATE_COLUMN + " text not null, " + INCOME_ADD_TIME_COLUMN
            + " text not null);";

    //---------------Create table regular expenses-------------
    private static final String REG_EXPENSES_TABLE = "reg_expenses";
    public static final String REG_EXPENSE_NAME_COLUMN = "reg_expense_name";
    public static final String REG_EXPENSE_CATEGORY_COLUMN = "reg_expense_category";
    public static final String REG_EXPENSE_SUM_COLUMN = "reg_expense_sum";
    public static final String REG_EXPENSE_START_PERIOD_COLUMN = "reg_expense_start_period";
    public static final String REG_EXPENSE_END_PERIOD_COLUMN = "reg_expense_end_period";
    public static final String REG_EXPENSE_COMMENT_COLUMN = "reg_expense_comment";
    public static final String REG_EXPENSE_IS_ACTIVE_COLUMN = "reg_expense_is_active";
    public static final String REG_EXPENSE_ADD_TIME_COLUMN = "reg_expense_add_time";

    private static final String REG_EXPENSES_CREATE_SCRIPT = "create table "
            + REG_EXPENSES_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + REG_EXPENSE_NAME_COLUMN
            + " text not null, " + REG_EXPENSE_CATEGORY_COLUMN + " integer, "
            + REG_EXPENSE_SUM_COLUMN + " real, " + REG_EXPENSE_START_PERIOD_COLUMN + " text not null, "
            + REG_EXPENSE_END_PERIOD_COLUMN + " text not null, " + REG_EXPENSE_COMMENT_COLUMN
            + " text, " + REG_EXPENSE_IS_ACTIVE_COLUMN + " integer, " + REG_EXPENSE_ADD_TIME_COLUMN
            + " text not null);";

    //---------------Create table regular incomes--------------
    private static final String REG_INCOMES_TABLE = "reg_incomes";
    public static final String REG_INCOME_NAME_COLUMN = "reg_income_name";
    public static final String REG_INCOME_SUM_COLUMN = "reg_income_sum";
    public static final String REG_INCOME_START_PERIOD_COLUMN = "reg_income_start_period";
    public static final String REG_INCOME_END_PERIOD_COLUMN = "reg_income_end_period";
    public static final String REG_INCOME_COMMENT_COLUMN = "reg_income_comment";
    public static final String REG_INCOME_IS_ACTIVE_COLUMN = "reg_income_is_active";
    public static final String REG_INCOME_ADD_TIME_COLUMN = "reg_income_add_time";

    private static final String REG_INCOMES_CREATE_SCRIPT = "create table "
            + REG_INCOMES_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + REG_INCOME_NAME_COLUMN
            + " text not null, " + REG_INCOME_SUM_COLUMN + " real, "
            + REG_INCOME_START_PERIOD_COLUMN + " text not null, " + REG_INCOME_END_PERIOD_COLUMN
            + " text not null, " + REG_INCOME_COMMENT_COLUMN + " text, "
            + REG_INCOME_IS_ACTIVE_COLUMN + " integer, " + REG_INCOME_ADD_TIME_COLUMN
            + " text not null);";


    //------------------Create table credits-------------------
    private static final String CREDITS_TABLE = "credits";
    public static final String CREDIT_NAME_COLUMN = "credit_name";
    public static final String CREDIT_INIT_PAYMENT_COLUMN = "credit_init_payment";
    public static final String CREDIT_PERCENT_COLUMN = "credit_percent";
    public static final String CREDIT_SUM_COLUMN = "credit_sum";
    public static final String CREDIT_START_PERIOD_COLUMN = "credit_start_period";
    public static final String CREDIT_END_PERIOD_COLUMN = "credit_end_period";
    public static final String CREDIT_IS_ACTIVE_COLUMN = "credit_is_active";
    public static final String CREDIT_ADD_TIME_COLUMN = "credit_add_time";

    private static final String CREDITS_CREATE_SCRIPT = "create table "
            + CREDITS_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + CREDIT_NAME_COLUMN
            + " text not null, " + CREDIT_INIT_PAYMENT_COLUMN + " real, "
            + CREDIT_PERCENT_COLUMN + " real, " + CREDIT_SUM_COLUMN + " real, "
            + CREDIT_START_PERIOD_COLUMN + " text not null, " + CREDIT_END_PERIOD_COLUMN
            + " text not null, " + CREDIT_IS_ACTIVE_COLUMN + " integer, "
            + CREDIT_ADD_TIME_COLUMN + " text not null);";

    //---------------Create table wanted expenses--------------
    private static final String WANTED_EXPENSES_TABLE = "wanted_expenses";
    public static final String WANTED_EXPENSE_NAME_COLUMN = "wanted_expense_name";
    public static final String WANTED_EXPENSE_CATEGORY_COLUMN = "wanted_expense_category";
    public static final String WANTED_EXPENSE_SUM_COLUMN = "wanted_expense_sum";
    public static final String WANTED_EXPENSE_END_PERIOD_COLUMN = "wanted_expense_end_period";
    public static final String WANTED_EXPENSE_COMMENT_COLUMN = "wanted_expense_comment";
    public static final String WANTED_EXPENSE_IS_ACTIVE_COLUMN = "wanted_expense_is_active";
    public static final String WANTED_EXPENSE_ADD_TIME_COLUMN = "wanted_expense_add_time";

    private static final String WANTED_EXPENSES_CREATE_SCRIPT = "create table "
            + WANTED_EXPENSES_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + WANTED_EXPENSE_NAME_COLUMN
            + " text not null, " + WANTED_EXPENSE_CATEGORY_COLUMN + " integer, "
            + WANTED_EXPENSE_SUM_COLUMN + " real, " + WANTED_EXPENSE_END_PERIOD_COLUMN
            + " text not null, " + WANTED_EXPENSE_COMMENT_COLUMN + " text, "
            + WANTED_EXPENSE_IS_ACTIVE_COLUMN + " integer, "
            + WANTED_EXPENSE_ADD_TIME_COLUMN + " text not null);";

    //---------------Create login----------------------------
    private static final String LOGIN_TABLE = "login";
    public static final String LOGIN_USERNAME_COLUMN = "login_username";
    public static final String LOGIN_PASSWORD_COLUMN = "login_password";

    private static final String LOGIN_CREATE_SCRIPT = "create table "
            + LOGIN_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + LOGIN_USERNAME_COLUMN
            + " text not null, " + LOGIN_PASSWORD_COLUMN + " text not null);";

    //---------------Create last actions table----------------------------
    private static final String LAST_ACTIONS_TABLE = "last_actions";
    public static final String LAST_ACTIONS_ID_COLUMN = "last_action_id";
    public static final String LAST_ACTIONS_CATEGORY_COLUMN = "last_action_category";
    public static final String LAST_ACTIONS_NAME_COLUMN = "last_action_name";
    public static final String LAST_ACTIONS_SUM_COLUMN = "last_action_sum";

    private static final String LAST_ACTIONS_CREATE_SCRIPT = "create table "
            + LAST_ACTIONS_TABLE + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + LAST_ACTIONS_ID_COLUMN
            + " integer, " + LAST_ACTIONS_CATEGORY_COLUMN + " text not null, "
            + LAST_ACTIONS_NAME_COLUMN + " text not null, " + LAST_ACTIONS_SUM_COLUMN
            + " real);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CATEGORIES_CREATE_SCRIPT);
        db.execSQL(EXPENSES_CREATE_SCRIPT);
        db.execSQL(INCOMES_CREATE_SCRIPT);
        db.execSQL(REG_EXPENSES_CREATE_SCRIPT);
        db.execSQL(REG_INCOMES_CREATE_SCRIPT);
        db.execSQL(WANTED_EXPENSES_CREATE_SCRIPT);
        db.execSQL(CREDITS_CREATE_SCRIPT);
        db.execSQL(LOGIN_CREATE_SCRIPT);
        db.execSQL(LAST_ACTIONS_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);

        db.execSQL("DROP TABLE IF EXISTS " + CATEGORIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REG_EXPENSES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INCOMES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REG_INCOMES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CREDITS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + WANTED_EXPENSES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + LAST_ACTIONS_TABLE);

        onCreate(db);
    }
}