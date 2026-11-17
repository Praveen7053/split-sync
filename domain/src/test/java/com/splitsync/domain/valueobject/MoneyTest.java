package com.splitsync.domain.valueobject;

import com.splitsync.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoneyTest {

    @Test
    void ofNormalizesToScaleTwo() {
        Money money = Money.of("10.5");
        assertEquals("10.50", money.toString());
        assertEquals(1050L, money.toMinorUnits());
    }

    @Test
    void plusAndMinusKeepExactScale() {
        Money total = Money.of("10.00").plus(Money.of("0.10")).minus(Money.of("0.05"));
        assertEquals(Money.of("10.05"), total);
    }

    @Test
    void requirePositiveRejectsZero() {
        ValidationException exception = assertThrows(ValidationException.class, () -> Money.zero().requirePositive("amount"));
        assertTrue(exception.getMessage().contains("greater than zero"));
    }
}
