package com.cg.productAvatar.dto;

import com.cg.model.ProductAvatar;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ProductAvatarResult {
    private String id;
    private String fileName;
    private String fileFolder;
    private String fileUrl;
    private String cloudId;
}
