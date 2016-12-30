package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.MyDatabase.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Sutha on 11/20/16.
 */

public class MyAccountDAO implements AccountDAO {



    protected SQLiteDatabase database;
    private Database MyDatabase;
    private Context MyContext;

    public MyAccountDAO(Context context) {
        this.MyContext = context;
        MyDatabase = Database.getDatabase(MyContext);
        open();
    }

    public void open() throws SQLException {
        if(MyDatabase == null)
            MyDatabase = Database.getDatabase(MyContext);
        database = MyDatabase.getWritableDatabase();
    }

    @Override
    public void addAccount(Account account) {
        ContentValues values = new ContentValues();
        values.put("AccountNo", account.getAccountNo());
        values.put("BankName", account.getBankName());
        values.put("AccountHolder",account.getAccountHolderName());
        values.put("Balance", account.getBalance());
        database.insert("tblAccount", null, values);

    }

    public List<String> getTransactionID() {
        List<String> accountNo = new ArrayList<>();
        try {
            String query = "select AccountNo from tblAccount";
            Cursor cursor = database.rawQuery(query, null);
            while (cursor.moveToNext()) {
                accountNo.add(cursor.getString(cursor.getColumnIndex("AccountNo")));
            }
        }catch(Exception ex){
            throw ex;
        }
        finally {
            return accountNo;
        }

    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {return null;}

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {}

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {}

    @Override
    public List<String> getAccountNumbersList() {
        return null;
    }

    @Override
    public List<Account> getAccountsList() {
        return null;
    }

}
