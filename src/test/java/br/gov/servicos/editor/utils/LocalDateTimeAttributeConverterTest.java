package br.gov.servicos.editor.utils;

import org.junit.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class LocalDateTimeAttributeConverterTest {

    @Test
    public void deveConverterLocalDateTimeEmTimestamp() {
        LocalDateTimeAttributeConverter converter = new LocalDateTimeAttributeConverter();
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = converter.convertToDatabaseColumn(localDateTime);
        assertThat(timestamp.toLocalDateTime(), equalTo(localDateTime));
    }

    @Test
    public void deveConverterTimestampEmLocalDateTime() {
        LocalDateTimeAttributeConverter converter = new LocalDateTimeAttributeConverter();
        LocalDateTime expectedLocalDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.from(expectedLocalDateTime.toInstant(ZoneOffset.UTC));
        LocalDateTime actualLocalDateTime = converter.convertToEntityAttribute(timestamp);
        assertThat(actualLocalDateTime, equalTo(expectedLocalDateTime));

    }

}