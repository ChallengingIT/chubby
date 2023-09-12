/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.staff.FileStaff;
import it.innotek.wehub.entity.staff.FileStaffId;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FileStaffRepository extends CrudRepository<FileStaff, FileStaffId> {

    Long countById(FileStaffId id);

    @Procedure(procedureName  = "elimina_file_vecchi_staff")
    void removeFileDoppione(@Param("lista_staff_id") String listId, @Param("tipologia") Integer tipologia);
}