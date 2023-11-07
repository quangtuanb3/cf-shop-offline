package com.cg.locationRegion.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class LocationRegionCreResDTO {
    private String provinceName;

    private String districtName;

    private String wardName;

    private String address;
}
