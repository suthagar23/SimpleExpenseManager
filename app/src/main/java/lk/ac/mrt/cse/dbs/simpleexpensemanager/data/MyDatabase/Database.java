package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.MyDatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by sutha on 11/20/2016.
 */

public class Database extends SQLiteOpenHelper  {

    private static Database instance;

    public static synchronized Database getDatabase(Context context) {
        instance = new Database(context);
        return instance;
    }

    public Database(Context context) {
        super(context, "140611L_DBMS", null, 1);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // to create the Account table in the Database
        String query1 = "CREATE TABLE tblAccount"
                + "(AccountNo INTEGER PRIMARY KEY, BankName VARCHAR(255), AccountHolder VARCHAR(255), Balance DOUBLE )";
        db.execSQL(query1);

        // t create the Transaction table in the Database
        String query2 = "CREATE TABLE tblTransaction(ID INTEGER PRIMARY KEY  AUTOINCREMENT, "
                + "transDate DATE, AccountNo VARCHAR(100), ExpenseType VARCHAR(100), Amount DOUBLE )";
        db.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
