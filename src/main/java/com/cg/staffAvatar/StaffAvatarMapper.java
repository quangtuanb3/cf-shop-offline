package com.cg.staffAvatar;

import com.cg.model.StaffAvatar;
import com.cg.staffAvatar.dto.StaffAvatarResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StaffAvatarMapper {

    public List<StaffAvatarResult> toListDTO(List<StaffAvatar> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    StaffAvatarResult toDTO(StaffAvatar staffAvatar) {
        return new StaffAvatarResult()
                .setId(staffAvatar.getId())
                .setHeight(staffAvatar.getHeight())
                .setWidth(staffAvatar.getHeight())
                .setFileName(staffAvatar.getFileName())
                .setCloudId(staffAvatar.getCloudId())
                .setFileFolder(staffAvatar.getFileFolder())
                .setFileUrl(staffAvatar.getFileUrl())
                .setFileType(staffAvatar.getFileType())
                ;
    }

    private StaffAvatar toEntity(StaffAvatarResult dto) {
        return new StaffAvatar()
                .setCloudId(dto.getCloudId())
                .setHeight(dto.getHeight())
                .setWidth(dto.getWidth())
                .setFileType(dto.getFileType())
                .setFileName(dto.getFileName())
                .setFileFolder(dto.getFileFolder())
                .setFileUrl(dto.getFileUrl())
                ;
    }
}
