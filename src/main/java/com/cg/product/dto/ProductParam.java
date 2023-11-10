package com.cg.product.dto;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Accessors(chain = true)
public class ProductParam {

    @NotBlank(message = "Tên không được phép rỗng")
    @Size(min = 5, max = 40, message = "Tên món không ít hơn 5 kí tự và dài hơn 40 kí tự")
    private String title;

    @NotBlank(message = "Vui lòng nhập giá tiền")
    @Pattern(regexp = "^-?\\d+$", message = "Vui lòng nhập giá trị tiền bằng chữ số")
    private String price;

    private String unit;
    private String categoryId;
    private MultipartFile avatar;


}
