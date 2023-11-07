package com.cg.staffAvatar;

import com.cg.model.StaffAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffAvatarRepository extends JpaRepository<StaffAvatar,Long> {
}
