package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.MyDatabase.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Sutha on 11/20/16.
 */

public class MyTransactionDAO implements TransactionDAO {

    protected SQLiteDatabase database;
    private Database MyDatabase;
    private Context MyContext;

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd-yyyy-MM", Locale.ENGLISH);

    public MyTransactionDAO(Context context) {
        this.MyContext = context;
        MyDatabase = Database.getDatabase(MyContext);
        open();
    }

    public void open() throws SQLException {
        if(MyDatabase == null)
            MyDatabase = Database.getDatabase(MyContext);
        database = MyDatabase.getWritableDatabase();
    }

    public void addTransaction(Transaction transaction) {
        ContentValues values = new ContentValues();
        values.put("AccountNo", transaction.getAccountNo());
        values.put("ExpenseType", transaction.getExpenseType());
        values.put("transDate",formatter.format(transaction.getDate()));
        values.put("Amount", transaction.getAmount());
        database.insert("tblTransaction", null, values);
    }


    public ArrayList<Transaction> getTransaction() throws ParseException {
        ArrayList<Transaction> CurrentTransactions = new ArrayList<Transaction>();
        try{
            Cursor cursor = database.query("tblTransaction",
                    new String[] { "transDate", "AccountNo", "ExpenseType", "Amount"}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                Transaction transaction = new Transaction(formatter.parse(cursor.getString(0)),cursor.getString(1),ExpenseType.valueOf(cursor.getString(2).toUpperCase()),cursor.getDouble(3));
                CurrentTransactions.add(transaction);
            }
        }
        catch(Exception ex){
            throw ex;
        }
        finally {
            return CurrentTransactions;
        }
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {}

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return null;
    }





}

