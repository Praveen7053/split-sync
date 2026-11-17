package com.splitsync.domain.model;

import com.splitsync.domain.exception.ValidationException;
import com.splitsync.domain.valueobject.CurrencyCode;
import com.splitsync.domain.valueobject.GroupId;
import com.splitsync.domain.valueobject.Money;
import com.splitsync.domain.valueobject.UserId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SettlementTest {

    @Test
    void rejectsSamePayerAndReceiver() {
        UserId user = UserId.of("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        GroupId groupId = GroupId.of("22222222-2222-2222-2222-222222222222");
        Money amount = Money.of("10.00");
        CurrencyCode currency = CurrencyCode.of("INR");
        LocalDate date = LocalDate.of(2026, 1, 1);

        assertThrows(ValidationException.class, () -> Settlement.create(groupId, user, user, amount, currency, user, date));
    }
}
