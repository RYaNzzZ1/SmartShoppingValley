package com.smartshoppingvalley.Commodity.dto;

import com.smartshoppingvalley.Commodity.entity.Commodity;
import com.smartshoppingvalley.Commodity.entity.CommodityAttribute;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommodityDto extends Commodity {
    private List<CommodityAttribute> commodityAttributeList = new ArrayList<>();
    private List<String> urls = new ArrayList<>();


    public static CommodityDto fromCommodity(Commodity commodity) {
        CommodityDto dto = new CommodityDto();
        BeanUtils.copyProperties(commodity, dto);
        return dto;
    }
}
