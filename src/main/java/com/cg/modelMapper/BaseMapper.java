package com.cg.modelMapper;

import com.cg.model.Product;
import com.cg.product.dto.ProductResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapper<DTO, Entity> {

    @Autowired
    private  ModelMapper modelMapper;
    private final Class<DTO> dtoClass;
    private final Class<Entity> entityClass;

    @SuppressWarnings("unchecked")
    public BaseMapper()
    {
        this.dtoClass = (Class<DTO>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseMapper.class);
        this.entityClass = (Class<Entity>) GenericTypeResolver.resolveTypeArgument(getClass(), BaseMapper.class);
    }

    public DTO toDTO(Entity entity) {
        return modelMapper.map(entity, dtoClass);
    }

    public Entity toEntity(DTO dto) {
        return modelMapper.map(dto, entityClass);
    }

    public List<DTO> toDTOList(List<Entity> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}

