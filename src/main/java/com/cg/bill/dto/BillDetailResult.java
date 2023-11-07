package com.cg.bill.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class BillDetailResult {

    private Long id;
    private BigDecimal totalAmount;
    private BigDecimal amount;
    private String note;
    private BigDecimal price;
    private Long quantity;
    private String title;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdAt;


    public BillDetailResult(Long id, BigDecimal totalAmount, BigDecimal amount, String note, BigDecimal price, Long quantity, String title, Date createdAt) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.amount = amount;
        this.note = note;
        this.price = price;
        this.quantity = quantity;
        this.title = title;
        this.createdAt = createdAt;
    }
}
