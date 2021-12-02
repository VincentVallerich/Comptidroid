package fr.ensisa.vallerich.comptidroid.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.math.BigDecimal;

@Entity
public class Account {
    @PrimaryKey
    private long id;
    private String name;
    private BigDecimal amount;

    public Account() { this.id = 0; }

    @Ignore
    public Account(String name, BigDecimal amount) {
        this.name = name;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}
