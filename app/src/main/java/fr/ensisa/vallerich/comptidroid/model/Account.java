package fr.ensisa.vallerich.comptidroid.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class Account {
    @PrimaryKey (autoGenerate = true)
    private long aid;
    private String name;
    private BigDecimal amount;
    private BigDecimal overdraft;

    public Account() { this.aid = 0; }

    @Ignore
    public Account(long id, String name, BigDecimal amount, BigDecimal overdraft) {
        this.aid = id;
        this.name = name;
        this.amount = amount;
        this.overdraft = overdraft;
    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(BigDecimal overdraft) {
        this.overdraft = overdraft;
    }
}
