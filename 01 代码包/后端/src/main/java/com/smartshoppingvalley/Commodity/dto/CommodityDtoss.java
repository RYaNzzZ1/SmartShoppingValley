package com.smartshoppingvalley.Commodity.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommodityDtoss extends CommodityDto {
    private long fatherId;

    public static CommodityDtoss fromCommodity(CommodityDto commodityDto) {
        CommodityDtoss dtos = new CommodityDtoss();
        BeanUtils.copyProperties(commodityDto, dtos);
        return dtos;
    }
}
