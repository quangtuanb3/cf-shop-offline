package com.cg.locationRegion;

import com.cg.locationRegion.dto.LocationRegionResult;
import com.cg.model.LocationRegion;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LocationRegionMapper {
    public LocationRegionResult toDTO(LocationRegion entity){
        return new LocationRegionResult()
                .setId(entity.getId())
                .setProvinceId(entity.getProvinceId())
                .setProvinceName(entity.getProvinceName())
                .setWardId(entity.getWardId())
                .setWardName(entity.getWardName())
                .setDistrictId(entity.getDistrictId())
                .setDistrictName(entity.getDistrictName())
                .setAddress(entity.getAddress());
    }

    public List<LocationRegionResult> toDTOList(List<LocationRegion> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
