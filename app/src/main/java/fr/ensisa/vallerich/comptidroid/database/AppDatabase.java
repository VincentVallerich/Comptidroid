package fr.ensisa.vallerich.comptidroid.database;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import fr.ensisa.vallerich.comptidroid.database.dao.AccountDao;
import fr.ensisa.vallerich.comptidroid.database.dao.OperationDao;
import fr.ensisa.vallerich.comptidroid.model.Account;
import fr.ensisa.vallerich.comptidroid.model.AccountOperationAssociation;
import fr.ensisa.vallerich.comptidroid.model.Operation;

@TypeConverters({DatabaseTypeConverters.class})
@Database(entities = {Account.class, Operation.class, AccountOperationAssociation.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance = null;
    private static MutableLiveData<AppDatabase> ready = new MutableLiveData<>(null);

    static public void create(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, "Comptidroid.db").build();
            ready.postValue(instance);
        }
    }

    static public AppDatabase get () {
        return instance;
    }

    public static LiveData<AppDatabase> isReady() {
        return ready;
    }

    public abstract AccountDao getAccountDao();

    public abstract OperationDao getOperationDao();
}
