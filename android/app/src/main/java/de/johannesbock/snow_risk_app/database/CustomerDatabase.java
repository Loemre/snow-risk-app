package de.johannesbock.snow_risk_app.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The database to store all customers
 * Gets prefilled on first launch with 20 cities around the world
 */
@Database(entities = {Customer.class}, version = 1, exportSchema = false)
public abstract class CustomerDatabase extends RoomDatabase {

    private static final String TAG = CustomerDatabase.class.getName();

    public abstract CustomerDAO customerDAO();

    // everything needed for the Database instance
    private static volatile CustomerDatabase INSTANCE;
    private static final String DB_NAME = "customer_db";

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    // Initialise the Database or return the already running instance
    // makes sure, there is always only one Database Instance
    public static synchronized CustomerDatabase getInstance(Context context) {

        // make sure there will only be one instance of CustomerDatabase
        if(INSTANCE == null) {
            // no running database instance, so initialise a new one
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CustomerDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(customerRoomDatabaseCallback)
                    .build();
        }

        return INSTANCE;

    }

    // prefill the database on first start of the app
    private static RoomDatabase.Callback customerRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            // FIXME: only fake data and just one city to test the room implementation
            databaseWriteExecutor.execute(() -> {

                CustomerDAO dao = INSTANCE.customerDAO();

                // example input for cities around the world
                // big cities or cold places right now
                dao.insert(new Customer("Berlin"));
                dao.insert(new Customer("Sydney"));
                dao.insert(new Customer("New York"));
                dao.insert(new Customer("London"));
                dao.insert(new Customer("Washington DC"));
                dao.insert(new Customer("Green Bay"));
                dao.insert(new Customer("Montreal"));
                dao.insert(new Customer("Edmonton"));
                dao.insert(new Customer("Köln"));
                dao.insert(new Customer("Tokyo"));
                dao.insert(new Customer("Warsaw"));
                dao.insert(new Customer("Juneau,Alaska"));
                dao.insert(new Customer("Buenos Aires"));
                dao.insert(new Customer("Wien"));
                dao.insert(new Customer("Kiruna"));
                dao.insert(new Customer("Amsterdam"));
                dao.insert(new Customer("Helsinki"));
                dao.insert(new Customer("Stockholm"));
                dao.insert(new Customer("Oslo"));
                dao.insert(new Customer("Peking"));
            });
        }
    };

}
