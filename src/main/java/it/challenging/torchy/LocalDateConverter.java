/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {

        if(source.isEmpty()){
            return null;
        }

        String[] splitSource = source.split("-");

        return LocalDate.of(Integer.parseInt(splitSource[0]),Integer.parseInt(splitSource[1]),Integer.parseInt(splitSource[2]));
    }
}