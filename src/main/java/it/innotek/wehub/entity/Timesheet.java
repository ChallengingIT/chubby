/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity;

import it.innotek.wehub.entity.timesheet.Mese;
import it.innotek.wehub.entity.timesheet.Progetto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Timesheet implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    private Mese           mese;
    private String         meseCorrenteItaliano;
    private boolean        isMeseInviato;
    private LocalDate      dataInizio;
    private LocalDate      dataFine;
    private Integer        numeroMese;
    private Integer        annoCorrente;
    private List<Progetto> progetti;
    private Integer        numeroProgetti;
    private Integer        totaleOre;
}
