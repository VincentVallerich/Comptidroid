package fr.ensisa.vallerich.comptidroid.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import fr.ensisa.vallerich.comptidroid.model.Operation;

@Dao
public interface OperationDao {

    @Query("SELECT * from operation")
    public LiveData<List<Operation>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long upsert(Operation operation);
}
