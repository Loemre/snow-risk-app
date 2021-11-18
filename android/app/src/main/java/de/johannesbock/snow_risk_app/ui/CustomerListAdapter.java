package de.johannesbock.snow_risk_app.ui;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import de.johannesbock.snow_risk_app.database.Customer;

/**
 * Customer List Adapter updates the recycler view for every element in the list
 * Calls the View Holder for every customer in the database
 */
public class CustomerListAdapter extends ListAdapter<Customer, CustomerViewHolder> {

    private static final String TAG = CustomerListAdapter.class.getName();


    public CustomerListAdapter(@NonNull DiffUtil.ItemCallback<Customer> diffCallback) {
        super(diffCallback);
    }

    @Override
    public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CustomerViewHolder.create(parent);
    }

    /**
     * Calls the bind function in {@link de.johannesbock.snow_risk_app.ui.CustomerViewHolder} for each position in the Customers list
     * The temperatures in the database are stored in Kelvin and converted to celsius in this function
     * @param holder An instance of the customer view holder
     * @param position The position inside the list of customers
     */
    @Override
    public void onBindViewHolder(CustomerViewHolder holder, int position) {
        Customer current = getItem(position);
        holder.bind(current.getName(), current.getWeather(), current.getRisk(),
                current.getCurrentTmp(), current.getIconURL());
    }

    /**
     * this inner class makes sure every customer will only be displayed once
     */
    public static class CustomerDiff extends DiffUtil.ItemCallback<Customer> {

        @Override
        public boolean areItemsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Customer oldItem, @NonNull Customer newItem) {
            return oldItem.getName().equals(newItem.getName());
        }
    }

}
