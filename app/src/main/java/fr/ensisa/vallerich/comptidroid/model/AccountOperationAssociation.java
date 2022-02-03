package fr.ensisa.vallerich.comptidroid.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
    tableName = "aoas",
    primaryKeys = { "aid", "oid" },
    indices = { @Index("aid"), @Index("oid") }
)
public class AccountOperationAssociation implements Cloneable{

    private long aid;
    private long oid;
    public AccountOperationAssociation() {
        this.aid = 0;
        this.oid = 0;
    }

    @Ignore
    public AccountOperationAssociation(long aid, long oid) {
        this.aid = aid;
        this.oid = oid;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }
}
