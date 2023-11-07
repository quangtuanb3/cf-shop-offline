package com.cg.staffAvatar;

import com.cg.model.StaffAvatar;
import com.cg.service.IGeneralService;
import com.cg.staffAvatar.dto.StaffAvatarResult;

import java.util.List;
import java.util.Optional;

public interface IStaffAvatarService {

    List<StaffAvatar> findAll();
    List<StaffAvatarResult> getAll();

    StaffAvatar findById(Long id);
    StaffAvatarResult getById(Long id);

    StaffAvatar create(StaffAvatar staffAvatar);

    void delete(StaffAvatar staffAvatar);

    void deleteById(Long id);
}
