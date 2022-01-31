package fr.ensisa.vallerich.comptidroid.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Operation {
    @PrimaryKey (autoGenerate = true)
    private long oid;
    private Date operationDate;
    private Date valueDate;
    private BigDecimal amount;
    private Type type;
    private String label;

    public Operation() {
        this.oid = 0;
    }

    @Ignore
    public Operation(long id, Date operationDate, Date valueDate, BigDecimal amount, Type type, String label) {
        this.oid = id;
        this.operationDate = operationDate;
        this.valueDate = valueDate;
        this.amount = amount;
        this.type = type;
        this.label = label;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
