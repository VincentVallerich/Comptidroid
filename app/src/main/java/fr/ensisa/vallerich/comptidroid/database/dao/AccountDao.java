package fr.ensisa.vallerich.comptidroid.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import fr.ensisa.vallerich.comptidroid.model.Account;
import fr.ensisa.vallerich.comptidroid.model.AccountOperationAssociation;
import fr.ensisa.vallerich.comptidroid.model.FullAccount;

@Dao
public interface AccountDao {

    @Query("SELECT * from account")
    public LiveData<List<Account>> getAll();

    @Transaction
    @Query("SELECT * FROM account WHERE aid = :id")
    public LiveData<FullAccount> getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long upsert(Account account);

    @Delete
    public void delete(Account account);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAccountOperation(AccountOperationAssociation operation);
}
