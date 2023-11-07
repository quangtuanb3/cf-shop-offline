package com.cg.staff;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.locationRegion.ILocationRegionService;
import com.cg.model.LocationRegion;
import com.cg.model.Role;
import com.cg.model.Staff;
import com.cg.model.User;
import com.cg.role.IRoleService;
import com.cg.locationRegion.dto.LocationRegionUpReqDTO;
import com.cg.staff.dto.CreationStaffParam;
import com.cg.staff.dto.StaffResult;
import com.cg.staff.dto.UpdateStaffParam;
import com.cg.user.IUserService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/staffs")
@RequiredArgsConstructor
public class StaffAPI {
    private final StaffMapper staffMapper;
    private final IStaffService staffService;
    private final ValidateUtils validateUtils;
    private final AppUtils appUtils;
    private final IUserService userService;
    private final ILocationRegionService locationRegionService;
    private final IRoleService roleService;


    @GetMapping
    public ResponseEntity<?> getAllStaff() {
        return new ResponseEntity<>(staffService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<?> getById(@PathVariable Long staffId) {
        Staff staff = staffService.findByIdAndDeletedFalse(staffId);
        StaffResult staffResult = staffMapper.toDTO(staff);
        staffResult.setStaffAvatar(staff.getStaffAvatar());
        return new ResponseEntity<>(staffResult, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@ModelAttribute CreationStaffParam creationStaffParam, BindingResult bindingResult) {

        new CreationStaffParam().validate(creationStaffParam, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Boolean existsByUsername = userService.existsByUsername(creationStaffParam.getUsername());

        if (existsByUsername) {
            throw new EmailExistsException("Tài khoản đã tồn tại");
        }
        Role role = roleService.findById(creationStaffParam.getRoleId());
        Staff staff = staffService.create(creationStaffParam);
        StaffResult dto = staffMapper.toDTO(staff);
        dto.setStaffAvatar(staff.getStaffAvatar());
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PatchMapping("/{staffId}")
    public ResponseEntity<?> update(@PathVariable("staffId") String staffIdStr, UpdateStaffParam updateStaffParam, BindingResult bindingResult) {

        if (!validateUtils.isNumberValid(staffIdStr)) {
            throw new DataInputException("Mã nhân viên không hợp lệ");
        }

        Long staffId = Long.parseLong(staffIdStr);
        Staff staff = staffService.findByIdAndDeletedFalse(staffId);

        User user = userService.findById(staff.getUser().getId());
        new UpdateStaffParam().validate(updateStaffParam, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (updateStaffParam.getStaffAvatar() == null) {
            Staff updateStaff = updateStaffParam.toStaffUpReqDTO(staffId);
            Long locationRegionId = staff.getLocationRegion().getId();
            LocationRegionUpReqDTO locationRegionUpReqDTO = updateStaffParam.getLocationRegion();
            LocationRegion locationRegion = locationRegionUpReqDTO.toLocationRegionUp(locationRegionId);
            locationRegionService.save(locationRegion);

            updateStaff.setStaffAvatar(staff.getStaffAvatar());
            updateStaff.setUser(user);
            updateStaff.setLocationRegion(locationRegion);

            staffService.save(updateStaff);

            StaffResult staffResult = new StaffResult()
                    .setStaffAvatar(staff.getStaffAvatar())
                    .setUser(staff.getUser());
            return new ResponseEntity<>(staffResult, HttpStatus.OK);
        } else {
            Staff staffUp = staffService.update(staffId, updateStaffParam);
            StaffResult staffResult = staffMapper.toDTO(staffUp);
            staffResult.setStaffAvatar(staff.getStaffAvatar())
                    .setUser(staff.getUser());
            return new ResponseEntity<>(staffResult, HttpStatus.OK);
        }
    }


    @DeleteMapping("/delete/{staffId}")
    public ResponseEntity<?> deleteStaff(@PathVariable("staffId") String staffIdStr) {
        if (!validateUtils.isNumberValid(staffIdStr)) {
            throw new DataInputException("UserId không hợp lệ");
        }
        Long staffId = Long.valueOf(staffIdStr);
        Staff staff = staffService.findById(staffId);
        staffService.deleteByIdTrue(staff);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/searchTitle/{keySearch}")
    public ResponseEntity<List<StaffResult>> getStaffByTitle(@PathVariable("keySearch") String keySearch) {

        keySearch = '%' + keySearch + '%';
        List<StaffResult> staffResults = staffService.getStaffByTitle(keySearch);
        if (staffResults.isEmpty()) {
            throw new DataInputException("Tên nhân viên không tồn tại");
        }
        return new ResponseEntity<>(staffResults, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<StaffResult>> getAllStaffDTO(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        try {
            Page<StaffResult> staffDTOS = staffService.findAllStaffDTOPage(PageRequest.of(page - 1, pageSize));

            if (staffDTOS.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(staffDTOS, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}