/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import it.innotek.wehub.entity.timesheet.Giorno;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Report implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    private String nome;
    private List<Giorno> giorni;

}
