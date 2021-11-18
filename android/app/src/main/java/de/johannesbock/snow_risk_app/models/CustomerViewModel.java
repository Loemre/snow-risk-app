package de.johannesbock.snow_risk_app.models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

import de.johannesbock.snow_risk_app.database.Customer;
import de.johannesbock.snow_risk_app.database.CustomerRepository;

/**
 * The CustomerViewModel extends the AndroidViewModel and handles the data from the database for the UI
 * The connection between the repository and the ui, prevents the ui from leaking any hint on the database
 */
public class CustomerViewModel extends AndroidViewModel {

    private CustomerRepository customerRepository;

    private final LiveData<List<Customer>> allCustomers;

    public CustomerViewModel(Application application) {
        super(application);
        customerRepository = CustomerRepository.getInstance(application);

        allCustomers = customerRepository.getAllCustomers();
    }

    public LiveData<List<Customer>> getAllCustomers() {
        return allCustomers;
    }

    public void insert(Customer customer) {
        customerRepository.insert(customer);
    }

    public void update(Customer customer) {
        customerRepository.update(customer);
    }

}
