package fr.ensisa.vallerich.comptidroid.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.ensisa.vallerich.comptidroid.model.Account;

@Dao
public interface AccountDao {

    @Query("SELECT * from account")
    public LiveData<List<Account>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long upsert (Account account);

    @Delete
    public void delete (Account account);
}
