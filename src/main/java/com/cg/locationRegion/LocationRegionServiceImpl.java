package com.cg.locationRegion;

import com.cg.exception.DataInputException;
import com.cg.locationRegion.dto.LocationRegionResult;
import com.cg.model.LocationRegion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationRegionServiceImpl implements ILocationRegionService{


    private final LocationRegionRepository locationRegionRepository;
    private final LocationRegionMapper locationRegionMapper;


    @Override
    @Transactional
    public List<LocationRegionResult> findAll() {
        List<LocationRegion> entities = locationRegionRepository.findAll();
        return locationRegionMapper.toDTOList(entities);
    }

    @Override
    @Transactional
    public LocationRegion findById(Long id) {

        return locationRegionRepository.findById(id).orElseThrow(()->{
            throw new DataInputException("ma id k ton tai");
        });
    }

    @Override
    @Transactional
    public LocationRegionResult save(LocationRegion locationRegion) {
        LocationRegion entity = locationRegionRepository.save(locationRegion);
        return locationRegionMapper.toDTO(entity);

    }




}
