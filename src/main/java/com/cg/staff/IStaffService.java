package com.cg.staff;

import com.cg.model.Staff;
import com.cg.staff.dto.CreationStaffParam;
import com.cg.staff.dto.StaffResult;
import com.cg.staff.dto.UpdateStaffParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IStaffService {

    Staff findById(Long id);

    Page<StaffResult> findAllStaffDTOPage(Pageable pageable);

    Staff create(CreationStaffParam creationStaffParam);

    List<Staff> findAll();

    Staff findByIdAndDeletedFalse(Long id);

    void deleteById(Long id);

    void deleteByIdTrue(Staff staff);

    List<Staff> findStaffByTitle(String title);

    List<StaffResult> getStaffByTitle(String keySearch);

    Page<StaffResult> findAll(Pageable pageable);

    Staff update(Long staffId, UpdateStaffParam updateStaffParam);

    List<StaffResult> getAll();

    Staff save(Staff updateStaff);
}
