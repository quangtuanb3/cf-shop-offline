package com.cg.staff;

import com.cg.locationRegion.dto.LocationRegionResDTO;
import com.cg.locationRegion.dto.LocationRegionUpReqDTO;
import com.cg.model.*;
import com.cg.staff.dto.CreationStaffParam;
import com.cg.staff.dto.StaffResult;
import com.cg.staff.dto.UpdateStaffParam;
import com.cg.staffAvatar.StaffAvatarMapper;
import com.cg.staffAvatar.dto.StaffAvatarResult;
import com.cg.user.UserMapper;
import com.cg.user.dto.UserParam;
import com.cg.user.dto.UserResult;
import com.cg.utils.UploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StaffMapper {
    private final UserMapper userMapper;
    private final StaffAvatarMapper staffAvatarMapper;
    private final UploadUtils uploadUtils;

    public StaffResult toDTO(Staff entity) {
        return new StaffResult()
                .setId(entity.getId())
                .setTitle(entity.getTitle())
                .setUser(new User()
                        .setId(entity.getUser().getId()))
                .setPhone(entity.getPhone())
                .setStaffAvatar(new StaffAvatar()
                        .setFileUrl(entity.getStaffAvatar().getFileUrl()))
                .setLocationRegion(new LocationRegion()
                        .setId(entity.getLocationRegion().getId())
                        .setAddress(entity.getLocationRegion().getAddress())
                        .setProvinceId(entity.getLocationRegion().getProvinceId())
                        .setProvinceName(entity.getLocationRegion().getProvinceName())
                        .setDistrictId(entity.getLocationRegion().getDistrictId())
                        .setDistrictName(entity.getLocationRegion().getDistrictName())
                        .setWardId(entity.getLocationRegion().getWardId())
                        .setWardName(entity.getLocationRegion().getWardName()));
    }

    public Staff toEntity(CreationStaffParam creationParam) {
        return new Staff()
                .setUser(new User())
                .setStaffAvatar(new StaffAvatar())
                .setPhone(creationParam.getPhone())
                .setOrders(new ArrayList<>())
                .setTitle(creationParam.getTitle())
                .setLocationRegion(new LocationRegion())
                ;
    }

    public Staff toEntity(UpdateStaffParam updateParam) {
        return new Staff()
                .setUser(new User())
                .setStaffAvatar(new StaffAvatar())
                .setPhone(updateParam.getPhone())
                .setOrders(new ArrayList<>())
                .setTitle(updateParam.getTitle())
                .setLocationRegion(new LocationRegion())
                ;
    }

    public void transferFields(Staff entity, UpdateStaffParam updateParam) {
        entity.setTitle(updateParam.getTitle())
                .setStaffAvatar(
                        uploadUtils.uploadAndStaffImage(entity.getStaffAvatar(),
                                updateParam.getStaffAvatar()))
                .setPhone(updateParam.getPhone())
                .setLocationRegion(updateParam.getLocationRegion().toLocationRegion())
        ;
    }

    public List<StaffResult> toDTOList(List<Staff> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


}
