/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.staff;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class FileStaffId implements Serializable {

    private static final long serialVersionUID = 6529685398267757690L;

    @Column(nullable = false, length = 11, name="id_staff")
    private Integer idStaff;

    @Column(nullable = false, length = 11, name="id_file")
    private Integer idFile;
}
