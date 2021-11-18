package de.johannesbock.snow_risk_app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

/**
 * The Dao accesses the database and provides functions to other classes
 */
@Dao
public interface CustomerDAO {

    // DAO for the customer table

    // a new customer will be ignored if already in table
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Customer customer);

    // Update the customer, will search for primary key and update the whole customer, even if information are missing
    @Update
    void update(Customer customer);

    // Deletes the whole table
    @Query("DELETE FROM customer_table")
    void deleteAll();

    // delete a specific customer from the table
    @Query("DELETE FROM customer_table WHERE customerId = :customerId")
    void deleteCustomer(int customerId);

    // On every change this list updates
    // Return a live data list with all customers sorted by risk from highest to lowest
    @Query("SELECT * FROM customer_table ORDER BY risk DESC")
    LiveData<List<Customer>> getCustomersSortedByRisk();

    // Returns a current list from the table without updating after every change
    @Query("SELECT * FROM customer_table")
    List<Customer> getAllCustomers();

    // Return a specific customer from the table by name
    @Query("SELECT * FROM customer_table WHERE name LIKE :name")
    Customer getCustomerByName(String name);

}
