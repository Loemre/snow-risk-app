package de.johannesbock.snow_risk_app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import de.johannesbock.snow_risk_app.R;
import de.johannesbock.snow_risk_app.database.Customer;
import de.johannesbock.snow_risk_app.database.CustomerRepository;
import de.johannesbock.snow_risk_app.databinding.ActivityMainBinding;
import de.johannesbock.snow_risk_app.models.CustomerViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private ActivityMainBinding binding;

    // connect the data to the activity
    private CustomerViewModel customerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        TextView textViewWeatherAPI = findViewById(R.id.textViewWeatherApi);

        // make link clickable
        textViewWeatherAPI.setMovementMethod(LinkMovementMethod.getInstance());

        // connect the list adapter
        final CustomerListAdapter customerListAdapter =
                new CustomerListAdapter(new CustomerListAdapter.CustomerDiff());
        recyclerView.setAdapter(customerListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // connect the View Model
        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        // add the observer to the live data
        customerViewModel.getAllCustomers().observe(this, customers -> {
            // update the list in the adapter
            customerListAdapter.submitList(customers);
        });

        // launches the new activity to add a new customer to the list
        // called by the add button
        ActivityResultLauncher<Intent> newCustomerActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            Customer customer = new Customer(data.getStringExtra(NewCustomerActivity.EXTRA_REPLY));
                            customerViewModel.insert(customer);
                            // update data for the newly added customer
                            CustomerRepository.getInstance(MainActivity.this.getApplication())
                                    .updateOneCustomerFromAPI(MainActivity.this.getApplication(), customer.getName());
                        } else {
                            View view = findViewById(R.id.content).getRootView();
                            Snackbar.make(view, R.string.no_customer_inserted, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                });

        // update the customers on start
        CustomerRepository.getInstance(MainActivity.this.getApplication())
                .updateDatabaseFromAPI(MainActivity.this.getApplication());

        // set the onclick listener for the add button
        // call the newCustomerActivityResultLauncher with the intent
        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewCustomerActivity.class);
                newCustomerActivityResultLauncher.launch(intent);
            }
        });

        // set the onclick listener for the reload button
        // calls the reload function in the customer repository
        binding.fabReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerRepository.getInstance(MainActivity.this.getApplication())
                        .updateDatabaseFromAPI(MainActivity.this.getApplication());
            }
        });

        // an implementation of the swipe refresh layout
        // swipe down to refresh the database and update all cities
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CustomerRepository.getInstance(MainActivity.this.getApplication())
                        .updateDatabaseFromAPI(MainActivity.this.getApplication());
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

}