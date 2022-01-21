package fr.ensisa.vallerich.comptidroid.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class FullAccount {

    @Embedded
    public Account account;
    @Relation(
            parentColumn = "aid",
            entityColumn = "oid",
            associateBy = @Junction(AccountOperationAssociation.class)
    )
    public List<Operation> operations;
}
