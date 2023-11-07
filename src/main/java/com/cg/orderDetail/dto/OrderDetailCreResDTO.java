package com.cg.orderDetail.dto;
import com.cg.productAvatar.dto.ProductAvatarResult;
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
public class OrderDetailCreResDTO {
    private Long orderDetailId;
    private TableOrderResult table;
    private Long productId;
    private String title;
    private BigDecimal price;
    private Long quantity;
    private BigDecimal amount;
    private String note;
    private BigDecimal totalAmount;
    private ProductAvatarResult avatar;
}
