package fr.ensisa.vallerich.comptidroid.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.ensisa.vallerich.comptidroid.database.dao.AccountDao;
import fr.ensisa.vallerich.comptidroid.livedata.Transformations;
import fr.ensisa.vallerich.comptidroid.model.Account;
import fr.ensisa.vallerich.comptidroid.model.FullAccount;
import fr.ensisa.vallerich.comptidroid.model.Operation;

public class AccountViewModel extends ViewModel {

    private AccountDao accountDao;
    private LiveData<FullAccount> account;
    private MutableLiveData<Long> id = new MutableLiveData<>();
    private MutableLiveData<String> name;
    private MutableLiveData<BigDecimal> amount;
    private MediatorLiveData<List<Operation>> operations;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
        this.account = Transformations.switchMap(id, a -> accountDao.getById(a));
        this.name = Transformations.map(account, a -> a.account.getName());
        this.amount = Transformations.map(account, a -> a.account.getAmount());
        this.operations = Transformations.map(account, a -> new ArrayList<>(a.operations));
    }

    public void createAccount() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Account account = new Account();
                long id = accountDao.upsert(account);
                setId(id);
            }
        });
    }

    public void setId(long id) {
        this.id.postValue(id);
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public MutableLiveData<List<Operation>> getOperations() {
        return operations;
    }
}
