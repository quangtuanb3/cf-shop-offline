package com.cg.tableOrder;

import com.cg.model.TableOrder;
import com.cg.tableOrder.dto.TableOrderParam;
import com.cg.tableOrder.dto.TableOrderResult;

import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITableOrderService{
    List<TableOrderResult> findAll();
    TableOrderResult create(TableOrderParam dto);

    List<TableOrderResult> findAllTablesWithoutSenderId(@Param("tableId") Long tableId);

    TableOrder findById(Long id);

    TableOrderResult getById(Long id);

    TableOrderResult update(Long id, TableOrderParam updateParam);

    Boolean existById(Long tableId);

    void save(TableOrder tableOrder);
}
