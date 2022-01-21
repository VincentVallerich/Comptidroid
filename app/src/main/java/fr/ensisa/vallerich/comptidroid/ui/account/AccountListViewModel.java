package fr.ensisa.vallerich.comptidroid.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.ensisa.vallerich.comptidroid.database.dao.AccountDao;
import fr.ensisa.vallerich.comptidroid.model.Account;

public class AccountListViewModel extends ViewModel {

    private AccountDao dao;
    private MediatorLiveData<List<Account>> accounts;

    public void setAccountDao(AccountDao dao) {
        this.dao = dao;
        this.accounts = new MediatorLiveData<>();
        this.accounts.addSource(dao.getAll(), accounts::setValue);
    }

    public LiveData<List<Account>> getAccounts() { return accounts; }

    public void deleteAccount(Account account) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> dao.delete(account));
    }
}
