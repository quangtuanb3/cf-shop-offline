package com.cg.staff.dto;

import com.cg.model.LocationRegion;
import com.cg.model.Role;
import com.cg.model.Staff;
import com.cg.model.User;
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
public class CreationStaffParam implements Validator {
    private String title;
    private String phone;
    private String provinceId;
    private String provinceName;
    private String districtId;
    private String districtName;
    private String wardId;
    private String wardName;
    private String address;
    private MultipartFile staffAvatar;
    private String username;
    private String password;
    private Long roleId;

    public Staff toStaff() {
        return new Staff()
                .setTitle(title)
                .setPhone(phone);
    }

    public User toUser(Role role) {
        return new User()
                .setUsername(username)
                .setPassword(password)
                .setRole(role)
                ;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CreationStaffParam.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CreationStaffParam creationStaffParam = (CreationStaffParam) target;
        String title = creationStaffParam.title;
        String phone = creationStaffParam.phone;
        String username = creationStaffParam.username;
        String password = creationStaffParam.password;
        if (title.isEmpty()) {
            errors.rejectValue("title", "title.null", "Tên không được phép rỗng");
            return;
        }
        if (title.length() >= 25 || title.length() <= 5) {
            errors.rejectValue("title", "title.length", "Tên không ít hơn 5 kí tự và dài hơn 25 kí tự");
        }
        if (phone.isEmpty()) {
            errors.rejectValue("phone", "phone.null", "Số điện thoại không được phép rỗng");
        }

        if (username.isEmpty()) {
            errors.rejectValue("username", "username.null", "Tên tài khoản không được phép rỗng");

        }
        if (password.isEmpty()) {
            errors.rejectValue("password", "password.null", "Mật khẩu không được để trống");
        }

    }


}