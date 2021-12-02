package fr.ensisa.vallerich.comptidroid.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fr.ensisa.vallerich.comptidroid.database.dao.AccountDao;
import fr.ensisa.vallerich.comptidroid.model.Account;

@TypeConverters({DatabaseTypeConverters.class})
@Database(entities = {Account.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance = null;

    static public void create(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "Comptidroid.db").build();
        }
    }

    static public AppDatabase get () {
        return instance;
    }
    public abstract AccountDao getAccountDao ();

}
