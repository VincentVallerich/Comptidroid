package fr.ensisa.vallerich.comptidroid.unit;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.ensisa.vallerich.comptidroid.model.Account;

public class AccountTest {

    Account sut;

    @BeforeEach
    void init() {
        sut = new Account();
    }

    @Test
    public void accountMustContainsZeroOperationByDefault() {
//        assertEquals(0, sut.getOperations().size());
    }
}
