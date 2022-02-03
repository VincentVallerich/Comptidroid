package fr.ensisa.vallerich.comptidroid.ui.operation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.ensisa.vallerich.comptidroid.database.dao.OperationDao;
import fr.ensisa.vallerich.comptidroid.model.Operation;

public class OperationListViewModel extends ViewModel {
    private OperationDao operationDao;
    private MediatorLiveData<List<Operation>> operations;

    public void setOperationDao(OperationDao dao) {
        this.operationDao = dao;
        this.operations = new MediatorLiveData<>();
        this.operations.addSource(dao.getAll(), operations::setValue);
    }

    public LiveData<List<Operation>> getOperations() { return operations; }

    public void deleteOperation(Operation operation) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> operationDao.delete(operation));
    }
}
