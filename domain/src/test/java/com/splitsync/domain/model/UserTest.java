package com.splitsync.domain.model;

import com.splitsync.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @Test
    void createRequiresDisplayNameAndGeneratesUserId() {
        User user = User.create("Praveen");
        assertNotNull(user.userId());
        assertFalse(user.userId().value().isBlank());
    }

    @Test
    void createRejectsBlankDisplayName() {
        assertThrows(ValidationException.class, () -> User.create("  "));
    }
}
