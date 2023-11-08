package com.cg.staff;

import com.cg.model.Staff;
import com.cg.model.user.User;
import com.cg.staff.dto.StaffResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Long> {
    Optional<Staff> findStaffByIdAndDeletedFalse(Long id);

    List<Staff> findStaffByTitle(String title);

//    Page<StaffResult> findAllStaffDTOPage(Pageable pageable);

//    @Query("SELECT NEW com.cg.staff.DTO.StaffDTO(" +
//            "st.id," +
//            "st.title," +
//            "st.phone," +
//            "st.locationRegion," +
//            "st.staffAvatar," +
//            "st.user" +
//            ") " +
//            "FROM Staff as st " +
//            "WHERE st.title like :keySearch "
//    )
//    List<StaffResult> findStaffByTitle(String keySearch);
//

@Query("SELECT new com.cg.staff.dto.StaffResult(st.id, st.title, st.phone, st.locationRegion, st.staffAvatar, st.user) " +
        "FROM Staff st " +
        "WHERE st.deleted = false " +
        "ORDER BY st.id ASC"
)
Page<StaffResult> findAllStaffResultPage(Pageable pageable);

//    Staff findByUserAndDeletedIsFalse(User user);
    Staff findStaffByUserAndDeletedIsFalse(User user);


//
//    Optional<Staff> findByUserId(Long userId);
//
//    Optional<Staff> findByUserAndDeletedIsFalse(User user);
//
//    Optional<Staff> findByIdAndDeletedFalse(Long id);
//
//
//    List<Staff> findAllByDeletedIsFalse();
}