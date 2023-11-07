package com.cg.staff.dto;

import com.cg.model.Staff;
import com.cg.locationRegion.dto.LocationRegionUpReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UpdateStaffParam implements Validator{
    private String title;
    private String phone;
    private LocationRegionUpReqDTO locationRegion;
    private MultipartFile staffAvatar;

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateStaffParam.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateStaffParam updateStaffParam = (UpdateStaffParam) target;
        String title = updateStaffParam.title;
        String phone = updateStaffParam.phone;

        if (title.isEmpty()) {
            errors.rejectValue("title", "title.null", "Tên không được phép rỗng");
            return;
        }
        if (title.length() >= 25 || title.length() <= 5) {
            errors.rejectValue("title", "title.length", "Tên không ít hơn 5 kí tự và dài hơn 25 kí tự");
        }
        if (phone.isEmpty()) {
            errors.rejectValue("title", "title.null", "Số điện thoại không được phép rỗng");
        }
    }

    public Staff toStaffChangeImage() {
        return new Staff()
                .setTitle(title)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegion())
                ;
    }

    public Staff toStaffUpReqDTO(Long staffId){
        return new Staff()
                .setId(staffId)
                .setTitle(title)
                .setPhone(phone)
                .setLocationRegion(locationRegion.toLocationRegion());

    }
}