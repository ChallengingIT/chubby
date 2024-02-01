/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.staff;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@Data
public class SkillStaffId implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @Column(nullable = false, length = 11, name="id_staff")
    private Integer idStaff;

    @Column(nullable = false, length = 11, name="id_skill")
    private Integer idSkill;

}
