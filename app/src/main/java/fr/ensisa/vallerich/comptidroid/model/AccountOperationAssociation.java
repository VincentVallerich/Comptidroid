package fr.ensisa.vallerich.comptidroid.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
    tableName = "aoas",
    primaryKeys = { "aid", "oid" },
    indices = { @Index("aid"), @Index("oid") }
)
public class AccountOperationAssociation {

    public long aid;
    public long oid;

    public AccountOperationAssociation() {}

    @Ignore
    public AccountOperationAssociation(long aid, long oid) {
        this.aid = aid;
        this.oid = oid;
    }
}
