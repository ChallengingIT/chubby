/*
 * Copyright (c) 2023.  Marco Sciuto ITA for Innotek. All rights reserved.
 */

package it.innotek.wehub.repository;

import it.innotek.wehub.entity.staff.FileStaff;
import it.innotek.wehub.entity.staff.FileStaffId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileStaffRepository extends JpaRepository<FileStaff, FileStaffId> {
}