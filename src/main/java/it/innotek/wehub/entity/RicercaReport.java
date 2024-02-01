/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RicercaReport implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    private Integer anno;
    private Integer mese;
    private Integer giornoInizio;
    private Integer giornoFine;
}
