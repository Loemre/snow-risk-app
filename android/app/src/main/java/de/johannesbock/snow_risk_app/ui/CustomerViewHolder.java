package de.johannesbock.snow_risk_app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import java.text.DecimalFormat;

import de.johannesbock.snow_risk_app.R;

/**
 * Customer View Holder will update the recycler view when called
 */
public class CustomerViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = CustomerViewHolder.class.getName();

    // text views of the recycler view
    private final TextView customerNameTextView;
    private final TextView currentWeatherTextView;
    private final TextView weatherTimeTextView;
    private final TextView riskTextView;
    private final TextView temperatureTextView;

    // image views of the recycler view
    // private final ImageView weatherImageView;
    private final ImageView riskImageView;
    private final ImageView weatherImageView;

    // decimal format for double values
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private CustomerViewHolder(View itemView) {
        super(itemView);
        customerNameTextView = itemView.findViewById(R.id.textViewCustomerName);
        currentWeatherTextView = itemView.findViewById(R.id.textViewCurrentWeather);
        weatherTimeTextView = itemView.findViewById(R.id.textViewWeatherTime);
        weatherImageView = itemView.findViewById(R.id.imageViewWeather);
        riskTextView = itemView.findViewById(R.id.textViewRisk);
        riskImageView = itemView.findViewById(R.id.imageViewRisk);
        temperatureTextView = itemView.findViewById(R.id.textViewTemperature);
    }

    // bind the information to the cards
    public void bind(String customerName, String currentWeather, int risk, double currentTemperature, String iconUrl) {
        customerNameTextView.setText(customerName);
        currentWeatherTextView.setText(currentWeather);
        riskImageView.setImageResource(chooseRiskImage(risk));
        riskImageView.setContentDescription(chooseContentDescriptionRiskImage(risk));
        riskTextView.setText(chooseContentDescriptionRiskImage(risk));
        temperatureTextView.setText(itemView.getResources()
                .getString(R.string.current_temperature, decimalFormat.format(currentTemperature)));
        new DownloadImageHelper(weatherImageView).execute(iconUrl);
    }

    // inflate the view
    public static CustomerViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);

        return new CustomerViewHolder(view);
    }

    /**
     * Helper function to choose the correct image to display for the snow risk
     *
     * @param risk the risk parameter, explanations on the values are provided in the {@link de.johannesbock.snow_risk_app.database.CustomerRepository}
     * @return An integer that holds the drawable for the snow risk
     */
    private int chooseRiskImage(int risk) {
        int image;

        switch (risk) {
            case 1:
                image = R.drawable.ic_baseline_low_risk;
                break;
            case 2:
                image = R.drawable.ic_baseline_small_risk;
                break;
            case 3:
                image = R.drawable.ic_baseline_medium_risk;
                break;
            case 4:
                image = R.drawable.ic_baseline_risk;
                break;
            case 5:
                image = R.drawable.ic_baseline_high_risk;
                break;
            case -1:
                image = R.drawable.ic_baseline_risk_calc_error;
                break;
            case 0:
            default:
                image = R.drawable.ic_baseline_no_risk;
                break;
        }

        return image;
    }

    /**
     * Helper function to set the content description and text view for the snow risk
     * @param risk the risk parameter, explanations on the values are provided in the {@link de.johannesbock.snow_risk_app.database.CustomerRepository}
     * @return A string that describes the risk of snow
     */
    private String chooseContentDescriptionRiskImage(int risk) {

        String contentDescription;

        switch (risk) {
            case 1:
                contentDescription = "20% risk of snow";
                break;
            case 2:
                contentDescription = "40% risk of snow";
                break;
            case 3:
                contentDescription = "60% risk of snow";
                break;
            case 4:
                contentDescription = "80% risk of snow";
                break;
            case 5:
                contentDescription = "100% risk of snow";
                break;
            case -1:
                contentDescription = "risk could not be calculated";
                break;
            case 0:
            default:
                contentDescription = "0% risk of snow";
                break;
        }

        return contentDescription;

    }

}
