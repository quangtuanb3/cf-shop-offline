package com.cg.tableOrder;

import com.cg.model.TableOrder;
import com.cg.tableOrder.dto.TableOrderParam;
import com.cg.tableOrder.dto.TableOrderResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TableOrderMapper {
    public TableOrderResult toDTO(TableOrder entity) {
        return new TableOrderResult()
                .setId(entity.getId().toString())
                .setTitle(entity.getTitle())
                .setStatus(entity.getStatus().toString());
    }

    public TableOrder toEntity(TableOrderParam param) {
        return new TableOrder().setTitle(param.getTitle());
    }

    public void transferFields(TableOrder entity, TableOrderParam param) {
        entity.setTitle(param.getTitle());
    }

    public List<TableOrderResult> toDTOList(List<TableOrder> entities) {
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
