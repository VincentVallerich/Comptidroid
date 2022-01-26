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
    private MutableLiveData<FullAccount> fullAccount;
    private MutableLiveData<Account> account;
    private final MutableLiveData<Long> id = new MutableLiveData<>();
    private MutableLiveData<String> name;
    private MutableLiveData<BigDecimal> amount;
    private MutableLiveData<BigDecimal> overdraft;
    private MediatorLiveData<List<Operation>> operations;
    private MediatorLiveData<Boolean> editMode;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
        this.fullAccount = Transformations.switchMap(id, a -> accountDao.getById(a));
        this.account = Transformations.map(fullAccount, f -> f.account);
        this.name = Transformations.map(account, a -> a.getName());
        this.amount = Transformations.map(account, a -> a.getAmount());
        this.overdraft = Transformations.map(account, a -> a.getOverdraft());
        this.operations = Transformations.map(fullAccount, f -> clone(f.operations));

        editMode = new MediatorLiveData<>();
        editMode.setValue(false);
    }

    private List<Operation> clone(List<Operation> original) {
        if (original == null) return null;
        List<Operation> copy = new ArrayList<>(original.size());
        for (Operation operation : original) {
            copy.add(operation.clone());
        }
        return copy;
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

    public MutableLiveData<BigDecimal> getAmount() {
        return amount;
    }

    public MutableLiveData<BigDecimal> getOverdraft() {
        return overdraft;
    }

    public LiveData<Boolean> getEditMode() {
        return editMode;
    }

    public void switchEditMode() {
        editMode.postValue(!editMode.getValue());
    }

    public void save() {
        Account account = new Account(
                id.getValue(),
                name.getValue(),
                amount.getValue(),
                overdraft.getValue());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                accountDao.upsert(account);
            }
        });
    }

    public void setName(String newName) {
        name.postValue(newName);
    }
}
