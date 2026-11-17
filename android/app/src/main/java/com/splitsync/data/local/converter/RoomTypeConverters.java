package com.splitsync.data.local.converter;

import androidx.room.TypeConverter;

import java.time.Instant;
import java.time.LocalDate;

/**
 * Converts Java time types to SQLite-friendly column values.
 */
public final class RoomTypeConverters {

    @TypeConverter
    public Long fromInstant(Instant value) {
        return value == null ? null : value.toEpochMilli();
    }

    @TypeConverter
    public Instant toInstant(Long value) {
        return value == null ? null : Instant.ofEpochMilli(value);
    }

    @TypeConverter
    public String fromLocalDate(LocalDate value) {
        return value == null ? null : value.toString();
    }

    @TypeConverter
    public LocalDate toLocalDate(String value) {
        return value == null ? null : LocalDate.parse(value);
    }
}
