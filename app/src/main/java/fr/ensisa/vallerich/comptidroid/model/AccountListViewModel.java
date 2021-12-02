package fr.ensisa.vallerich.comptidroid.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import fr.ensisa.vallerich.comptidroid.database.dao.AccountDao;

public class AccountListViewModel extends ViewModel {

    private AccountDao dao;

    public void setAccountDao(AccountDao dao) {
        this.dao = dao;
    }

    public LiveData<List<Account>> getAccounts() { return dao.getAll(); }
}
