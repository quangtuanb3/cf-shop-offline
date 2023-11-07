package com.cg.staff.dto;

import com.cg.locationRegion.dto.LocationRegionResDTO;
import com.cg.model.LocationRegion;
import com.cg.model.StaffAvatar;
import com.cg.model.User;
import com.cg.staffAvatar.dto.StaffAvatarResult;
import com.cg.user.dto.UserResult;
import lombok.*;
import lombok.experimental.Accessors;


@Getter
@Setter
@Data
//@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)

public class StaffResult {
    private Long id;
    private String title;
    private String phone;
    private LocationRegion locationRegion;
    private StaffAvatar staffAvatar;
    private User user;

    public StaffResult(Long id, String title,String phone, LocationRegion locationRegion, StaffAvatar staffAvatar, User user){
        this.id = id;
        this.title = title;
        this.phone = phone;
        this.locationRegion = locationRegion;
        this.staffAvatar = staffAvatar;
        this.user = user;
    }
}
