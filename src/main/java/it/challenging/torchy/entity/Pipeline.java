/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.challenging.torchy.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Pipeline implements Serializable {

    private Integer itwPianificate;
    private Integer itwFatte;
    private Integer cfDisponibili;
    private Integer cfInviati;
    private Integer qmPianificate;
    private Integer qmFatte;
    private Integer followUpPool;
    private Integer followUpPositivi;

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }
}