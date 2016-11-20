/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.R;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.MyAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.MyTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

import static lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants.EXPENSE_MANAGER;
/**
 *
 */
public class ManageExpensesFragment extends Fragment implements View.OnClickListener {
    private Button submitButton;
    private EditText amount;
    private Spinner accountSelector;
    private RadioGroup expenseTypeGroup;
    private DatePicker datePicker;
    private ExpenseManager currentExpenseManager;

    private MyAccountDAO accountDAO;
    private MyTransactionDAO transactionDAO;

    public static ManageExpensesFragment newInstance(ExpenseManager expenseManager) {
        ManageExpensesFragment manageExpensesFragment = new ManageExpensesFragment();
        Bundle args = new Bundle();
        args.putSerializable(EXPENSE_MANAGER, expenseManager);
        manageExpensesFragment.setArguments(args);

        return manageExpensesFragment;
    }

    public ManageExpensesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_manage_expenses, container, false);
        submitButton = (Button) rootView.findViewById(R.id.submit_amount);
        submitButton.setOnClickListener(this);

        amount = (EditText) rootView.findViewById(R.id.amount);
        accountSelector = (Spinner) rootView.findViewById(R.id.account_selector);
        currentExpenseManager = (ExpenseManager) getArguments().get(EXPENSE_MANAGER);
        ArrayAdapter<String> adapter =
                null;
        accountDAO = new MyAccountDAO(getActivity());
        if (currentExpenseManager != null) {
            adapter = new ArrayAdapter<>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item,
                    accountDAO.getTransactionID());
            Log.d(accountDAO.getTransactionID().toString(),"dsgs");
        }
        accountSelector.setAdapter(adapter);

        expenseTypeGroup = (RadioGroup) rootView.findViewById(R.id.expense_type_group);
        RadioButton expenseType = (RadioButton) rootView.findViewById(R.id.expense);
        RadioButton incomeType = (RadioButton) rootView.findViewById(R.id.income);
        datePicker = (DatePicker) rootView.findViewById(R.id.date_selector);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_amount:
                String selectedAccount = (String) accountSelector.getSelectedItem();
                String amountStr = amount.getText().toString();
                RadioButton checkedType = (RadioButton) getActivity().findViewById(expenseTypeGroup
                        .getCheckedRadioButtonId());
                String type = (String) checkedType.getText();

                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();

                if (amountStr.isEmpty()) {
                    amount.setError(getActivity().getString(R.string.err_amount_required));
                }

                if (currentExpenseManager != null) {
                    try {
//                        currentExpenseManager.updateAccountBalance(selectedAccount, day, month, year,
//                                ExpenseType.valueOf(type.toUpperCase()), amountStr);
                        transactionDAO = new MyTransactionDAO(getActivity());
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month, day);
                        Date transactionDate = calendar.getTime();
                        Transaction transaction = new Transaction(transactionDate,selectedAccount,ExpenseType.valueOf(type.toUpperCase()), Double.parseDouble(amountStr));
                        transactionDAO.addTransaction(transaction);

                    } catch (Exception e) {
                        new AlertDialog.Builder(this.getActivity())
                                .setTitle(this.getString(R.string.msg_account_update_unable) + selectedAccount)
                                .setMessage(e.getMessage())
                                .setNeutralButton(this.getString(R.string.msg_ok),
                                        new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).setIcon(android.R.drawable.ic_dialog_alert).show();
                    }
                }
                amount.getText().clear();
                break;
        }
    }
}
