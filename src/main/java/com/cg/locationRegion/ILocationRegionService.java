package com.cg.locationRegion;

import com.cg.locationRegion.dto.LocationRegionResult;
import com.cg.model.LocationRegion;
import com.cg.service.IGeneralService;

import javax.transaction.Transactional;
import java.util.List;

public interface ILocationRegionService{

    List<LocationRegionResult> findAll();

    @Transactional
    LocationRegion findById(Long id);

    @Transactional
    LocationRegionResult save(LocationRegion locationRegion);
}
