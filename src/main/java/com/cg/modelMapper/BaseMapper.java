package com.cg.modelMapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.GenericTypeResolver;

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
}

