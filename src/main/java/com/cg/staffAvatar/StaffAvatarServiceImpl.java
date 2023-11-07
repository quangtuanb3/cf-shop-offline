package com.cg.staffAvatar;

import com.cg.exception.ResourceNotFoundException;
import com.cg.model.StaffAvatar;
import com.cg.staffAvatar.dto.StaffAvatarResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class StaffAvatarServiceImpl implements IStaffAvatarService{
    private final StaffAvatarRepository staffAvatarRepository;
    private final StaffAvatarMapper staffAvatarMapper;

    @Override
    @Transactional(readOnly = true)
    public List<StaffAvatar> findAll() {
        return staffAvatarRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StaffAvatarResult> getAll() {
        return staffAvatarMapper.toListDTO(this.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public StaffAvatar findById(Long id) {
        return staffAvatarRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Not found!"));
    }

    @Override
    @Transactional(readOnly = true)
    public StaffAvatarResult getById(Long id) {
        return staffAvatarMapper.toDTO(this.findById(id));
    }

    @Override
    @Transactional
    public StaffAvatar create(StaffAvatar staffAvatar) {
        return staffAvatarRepository.save(staffAvatar);
    }

    @Override
    @Transactional
    public void delete(StaffAvatar staffAvatar) {
        staffAvatarRepository.delete(staffAvatar);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        staffAvatarRepository.deleteById(id);
    }
}
