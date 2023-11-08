package com.cg.staff;


import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;

import com.cg.exception.ResourceNotFoundException;
import com.cg.model.*;
import com.cg.locationRegion.dto.UpdateLocationRegionParam;
import com.cg.staff.dto.CreationStaffParam;
import com.cg.staff.dto.StaffResult;
import com.cg.staff.dto.UpdateStaffParam;
import com.cg.locationRegion.LocationRegionRepository;
import com.cg.role.IRoleService;
import com.cg.service.upload.IUploadService;

import com.cg.staffAvatar.StaffAvatarRepository;
import com.cg.user.IUserService;
import com.cg.utils.AppUtils;
import com.cg.utils.UploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StaffServiceImpl implements IStaffService {

    private final StaffRepository staffRepository;
    private final IUploadService uploadService;
    private final UploadUtils uploadUtils;
    private final StaffAvatarRepository staffAvatarRepository;
    private final LocationRegionRepository locationRegionRepository;
//    private final IStaffService staffService;
    private final IUserService userService;
    private final IRoleService roleService;
    private final StaffMapper staffMapper;
    private final AppUtils appUtils;

    @Override
    @Transactional(readOnly = true)
    public List<Staff> findAll() {
        return staffRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Staff findById(Long id) {
        return staffRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Staff findByIdAndDeletedFalse(Long id) {
        return staffRepository.findStaffByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not Found"));
    }
    @Transactional
    public void delete(Staff staff) {
        staffRepository.delete(staff);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        staffRepository.deleteById(id);
    }
    @Transactional(readOnly = true)
    public List<StaffResult> getAll() {
        return staffMapper.toDTOList(staffRepository.findAll());
    }

    @Override
    public Staff save(Staff updateStaff) {
        return staffRepository.save(updateStaff);
    }

    @Override
    @Transactional
    public void deleteByIdTrue(Staff staff) {
        staff.setDeleted(true);
        staffRepository.save(staff);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Staff> findStaffByTitle(String keySearch) {
        return staffRepository.findStaffByTitle(keySearch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffResult> getStaffByTitle(String keySearch) {
        return staffMapper.toDTOList(staffRepository.findStaffByTitle(keySearch));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffResult> findAll(Pageable pageable) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StaffResult> findAllStaffDTOPage(Pageable pageable) {
        return staffRepository.findAllStaffResultPage(pageable);
    }

    private void uploadAndSaveStaffImage(StaffAvatar staffAvatar, MultipartFile file) {
        try {
            Map uploadResult = uploadService.uploadImage(file, uploadUtils.buildImageUploadParamsStaff(staffAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            staffAvatar.setFileName(staffAvatar.getId() + "." + fileFormat);
            staffAvatar.setFileUrl(fileUrl);
            staffAvatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
            staffAvatar.setCloudId(staffAvatar.getFileFolder() + "/" + staffAvatar.getId());
            staffAvatarRepository.save(staffAvatar);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }

    @Override
    @Transactional
    public Staff create(CreationStaffParam creationStaffParam) {
        Staff entity = staffMapper.toEntity(creationStaffParam);
        MultipartFile file = creationStaffParam.getStaffAvatar();

        LocationRegion locationRegion = entity.getLocationRegion();
        locationRegionRepository.save(locationRegion);

        StaffAvatar staffAvatar = new StaffAvatar();
        staffAvatarRepository.save(staffAvatar);

        uploadAndSaveStaffImage(staffAvatar, file);

        Boolean existsByUsername = userService.existsByUsername(creationStaffParam.getUsername());

        if (existsByUsername) {
            throw new EmailExistsException("UserName đã tồn tại");
        }
        Role role = roleService.findById(creationStaffParam.getRoleId());

        try {
            User user = userService.save(creationStaffParam.toUser(role));
            Staff staff = creationStaffParam.toStaff();
            staff.setLocationRegion(locationRegion);
            staff.setStaffAvatar(staffAvatar);
            staff.setUser(user);
            staffRepository.save(staff);

            return staff;
        } catch (DataIntegrityViolationException e) {
            throw new DataInputException("Account information is not valid, please check the information again");
        }
    }

    @Override
    @Transactional
    public Staff update(Long staffId, UpdateStaffParam updateStaffParam) {
        MultipartFile file = updateStaffParam.getStaffAvatar();
        Staff staff = this.findById(staffId);
        Long locationRegionId = staff.getLocationRegion().getId();
        UpdateLocationRegionParam updateLocationRegionParam = updateStaffParam.getLocationRegion();
        LocationRegion locationRegion = updateLocationRegionParam.toLocationRegionUp(locationRegionId);
        locationRegionRepository.save(locationRegion);

        StaffAvatar staffAvatar = new StaffAvatar();
        staffAvatarRepository.save(staffAvatar);

        uploadAndSaveStaffImage(staffAvatar, file);

        Staff staffUpdate = updateStaffParam.toStaffChangeImage();


        staffUpdate.setId(staffId);
        staffUpdate.setStaffAvatar(staffAvatar);
        staffUpdate.setLocationRegion(locationRegion);
        staffUpdate.setUser(staff.getUser());

//        staffRepository.save(staffUpdate);

        return staffUpdate;

    }
}