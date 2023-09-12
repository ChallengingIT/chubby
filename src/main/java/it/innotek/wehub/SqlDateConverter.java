/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class SqlDateConverter implements Converter<String, java.sql.Date> {

    @Override
    public java.sql.Date convert(String source) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        try {

            if (source.isEmpty()) {
                return null;
            }

            return new java.sql.Date(formatter.parse(source).getTime());

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}