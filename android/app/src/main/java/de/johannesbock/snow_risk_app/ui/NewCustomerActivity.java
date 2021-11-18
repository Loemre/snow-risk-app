package de.johannesbock.snow_risk_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import de.johannesbock.snow_risk_app.R;

/**
 * Very simple activity that provides an input field to add a new customer to the database
 */
public class NewCustomerActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "de.johannesbock.snow_risk_app.REPLY";

    private EditText editTextCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);

        editTextCustomer = findViewById(R.id.editTextCustomer);

        final Button button = findViewById(R.id.buttonSave);
        button.setOnClickListener(view -> {
            Intent saveIntent = new Intent();

            if(TextUtils.isEmpty(editTextCustomer.getText())) {
                setResult(RESULT_CANCELED, saveIntent);
            } else {
                String customer = editTextCustomer.getText().toString();
                saveIntent.putExtra(EXTRA_REPLY, customer);
                setResult(RESULT_OK, saveIntent);
            }
            finish();
        });

    }
}