package com.assesment.users.infrastructure.output;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeConverter implements DynamoDBTypeConverter<String, LocalDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public String convert(LocalDateTime time) {
        return time.format(FORMATTER);
    }

    @Override
    public LocalDateTime unconvert(String value) {
        return LocalDateTime.parse(value, FORMATTER);
    }
}