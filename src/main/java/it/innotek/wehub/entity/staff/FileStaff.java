/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.entity.staff;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table( name = "file_staff")
public class FileStaff  implements Serializable {

    @Serial
    private static final long serialVersionUID = -6529685398267757690L;

    @EmbeddedId
    private FileStaffId id;

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        FileStaff fileStaff = (FileStaff)o;
        return id != null && Objects.equals(id, fileStaff.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

}
