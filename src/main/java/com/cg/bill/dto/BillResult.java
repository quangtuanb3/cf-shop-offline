package com.cg.bill.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class BillResult {
    private Long id;
    private String tableTitle;
    private BigDecimal total;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;
    private String staffName;
    private Long orderId;


    public BillResult(Long id, String tableTitle, BigDecimal total, Date createdAt, String staffName, Long orderId) {
        this.id = id;
        this.tableTitle = tableTitle;
        this.total = total;
        this.createdAt = createdAt;
        this.staffName = staffName;
        this.orderId = orderId;
    }
}
