package com.cg.bill.dto;

import com.cg.tableOrder.dto.TableOrderResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CreationBillParam {

    private Long id;
    private TableOrderResult table;
    private BigDecimal totalAmount;
    private Long orderId;
    private Boolean paid;

}
