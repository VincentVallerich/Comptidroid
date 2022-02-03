package fr.ensisa.vallerich.comptidroid.database.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import fr.ensisa.vallerich.comptidroid.model.FullAccount;
import fr.ensisa.vallerich.comptidroid.model.Operation;

@Dao
public interface OperationDao {

    @Query("SELECT * from Operation")
    LiveData<List<Operation>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long upsert(Operation operation);

    @Transaction
    @Query("SELECT * FROM Operation WHERE oid = :id")
    LiveData<Operation> getById(long id);

    @Delete
    void delete(Operation operation);
}
