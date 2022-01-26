package fr.ensisa.vallerich.comptidroid.database;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import fr.ensisa.vallerich.comptidroid.database.dao.AccountDao;
import fr.ensisa.vallerich.comptidroid.database.dao.OperationDao;
import fr.ensisa.vallerich.comptidroid.model.Account;
import fr.ensisa.vallerich.comptidroid.model.AccountOperationAssociation;
import fr.ensisa.vallerich.comptidroid.model.Operation;
import fr.ensisa.vallerich.comptidroid.model.Type;

public class DatabaseFeeder {

    private long[] createAccounts() {
        long[] aids = new long[3];
        AccountDao dao = AppDatabase.get().getAccountDao();
        aids[0] = dao.upsert(new Account(0, "Compte courant", new BigDecimal("1234.56").setScale(2, RoundingMode.FLOOR), new BigDecimal(500).setScale(2, RoundingMode.FLOOR)));
        aids[0] = dao.upsert(new Account(0, "Compte cheque", new BigDecimal("-56.78").setScale(2, RoundingMode.FLOOR), new BigDecimal(100).setScale(2, RoundingMode.FLOOR)));
        aids[0] = dao.upsert(new Account(0, "Livret A", BigDecimal.ZERO, BigDecimal.ZERO));

        return aids;
    }

    private Date getRandomDate() {
        Random random = new Random();
        return new Date(2022, random.nextInt(12), random.nextInt(28));
    }

    private long[] createOperations() {
        long[] oids = new long[6];
        OperationDao dao = AppDatabase.get().getOperationDao();
        oids[0] = dao.upsert(new Operation(0, getRandomDate(), getRandomDate(), new BigDecimal("1232").setScale(2, RoundingMode.FLOOR), Type.CREDIT, "premier credit"));
        oids[1] = dao.upsert(new Operation(0, getRandomDate(), getRandomDate(), new BigDecimal("-10.99").setScale(2, RoundingMode.FLOOR), Type.DEBIT, "premier debit"));
        oids[2] = dao.upsert(new Operation(0, getRandomDate(), getRandomDate(), new BigDecimal("15").setScale(2, RoundingMode.FLOOR), Type.CREDIT, "deuxieme credit"));
        oids[3] = dao.upsert(new Operation(0, getRandomDate(), getRandomDate(), new BigDecimal("-315.58").setScale(2, RoundingMode.FLOOR), Type.DEBIT, "et un autre débit"));
        oids[4] = dao.upsert(new Operation(0, getRandomDate(), getRandomDate(), new BigDecimal("3.15").setScale(2, RoundingMode.FLOOR), Type.CREDIT, "frais bancaires"));
        oids[5] = dao.upsert(new Operation(0, getRandomDate(), getRandomDate(), new BigDecimal("19161").setScale(2, RoundingMode.FLOOR), Type.CREDIT, "mon super virement"));

        return oids;
    }

    private void feedAccounts() {
        AccountDao dao = AppDatabase.get().getAccountDao();
        long[] oids = createOperations();
        long[] aids = createAccounts();
        dao.addAccountOperation(new AccountOperationAssociation(aids[0], oids[0]));
        dao.addAccountOperation(new AccountOperationAssociation(aids[0], oids[1]));
        dao.addAccountOperation(new AccountOperationAssociation(aids[0], oids[2]));
        dao.addAccountOperation(new AccountOperationAssociation(aids[1], oids[3]));
        dao.addAccountOperation(new AccountOperationAssociation(aids[1], oids[4]));
    }

    public void feed() {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                feedAccounts();
            }
        });
    }
}
