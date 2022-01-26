package fr.ensisa.vallerich.comptidroid.database.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import fr.ensisa.vallerich.comptidroid.model.FullAccount;
import fr.ensisa.vallerich.comptidroid.model.Operation;

@Dao
public interface OperationDao {

    @Query("SELECT * from operation")
    public LiveData<List<Operation>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long upsert(Operation operation);

    @Transaction
    @Query("SELECT * FROM operation WHERE oid = :id")
    public LiveData<Operation> getById(long id);
}
