/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.timesheet;

import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.LocalDate;

@Data
public class AggiornaTimesheet {

    private static final long serialVersionUID = 6529685398267757690L;

    private Integer ore;

    private LocalDate data;

    @Nullable
    private LocalDate dataFinePeriodo;

    private Progetto progetto;

    private Boolean ferie;

    private Boolean malattia;

    private Boolean permesso;

    private Integer oreStraordinarie;

    private Integer oreStraordinarieNotturne;

    private Integer orePermesso;
}
