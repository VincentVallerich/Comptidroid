package fr.ensisa.vallerich.comptidroid.ui.operation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.ensisa.vallerich.comptidroid.database.dao.OperationDao;
import fr.ensisa.vallerich.comptidroid.livedata.Transformations;
import fr.ensisa.vallerich.comptidroid.model.Account;
import fr.ensisa.vallerich.comptidroid.model.Operation;
import fr.ensisa.vallerich.comptidroid.model.Type;

public class OperationViewModel extends ViewModel {

    private OperationDao operationDao;
    private final MutableLiveData<Long> id = new MutableLiveData<>();
    private MutableLiveData<Operation> operation;
    private MutableLiveData<BigDecimal> amount;
    private MutableLiveData<Date> operationDate;
    private MutableLiveData<Date> valueDate;
    private MutableLiveData<Type> type;
    private MutableLiveData<String> label;
    private MutableLiveData<Boolean> editMode;

    public void setOperationDao(OperationDao dao) {
        this.operationDao = dao;
        this.operation = Transformations.switchMap(id, o -> operationDao.getById(o));
        this.amount = Transformations.map(operation, o -> o.getAmount());
        this.operationDate = Transformations.map(operation, o -> o.getOperationDate());
        this.valueDate = Transformations.map(operation, o -> o.getValueDate());
        this.type = Transformations.map(operation, o -> o.getType());
        this.label = Transformations.map(operation, o -> o.getLabel());

        editMode = new MediatorLiveData<>();
        editMode.setValue(false);
    }

    public void save() {
        Operation operation = new Operation(
                id.getValue(),
                operationDate.getValue(),
                valueDate.getValue(),
                amount.getValue(),
                type.getValue(),
                label.getValue());
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                operationDao.upsert(operation);
            }
        });
    }

    public void createOperation() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Operation operation = new Operation();
                long id = operationDao.upsert(operation);
                setId(id);
            }
        });
    }

    public void setId(long id) {
        this.id.postValue(id);
    }

    public LiveData<Boolean> getEditMode() {
        return editMode;
    }

    public MutableLiveData<Operation> getOperation() {
        return operation;
    }

    public MutableLiveData<String> getLabel() {
        return label;
    }

    public MutableLiveData<BigDecimal> getAmount() {
        return amount;
    }

    public MutableLiveData<Date> getOperationDate() {
        return operationDate;
    }

    public MutableLiveData<Date> getValueDate() {
        return valueDate;
    }

    public MutableLiveData<Type> getType() {
        return type;
    }

    public void setLabel(String newLabel) {
        this.label.postValue(newLabel);
    }

    public void switchEditMode() {
        editMode.postValue(!editMode.getValue());
    }
}
