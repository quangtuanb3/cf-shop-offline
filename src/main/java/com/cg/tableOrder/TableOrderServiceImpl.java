package com.cg.tableOrder;

import com.cg.exception.ResourceNotFoundException;
import com.cg.model.TableOrder;
import com.cg.tableOrder.dto.TableOrderParam;
import com.cg.tableOrder.dto.TableOrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableOrderServiceImpl implements ITableOrderService {

    private final TableOrderRepository tableOrderRepository;
    private final TableOrderMapper tableOrderMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TableOrderResult> findAll() {
        return tableOrderMapper.toDTOList(tableOrderRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public TableOrder findById(Long id) {
        return tableOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public TableOrderResult getById(Long id) {
        return tableOrderMapper.toDTO(this.findById(id));
    }

    @Override
    @Transactional
    public TableOrderResult update(Long id, TableOrderParam updateParam) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existById(Long tableId) {
        return tableOrderRepository.existsById(tableId);
    }

    @Override
    public void save(TableOrder tableOrder) {
        tableOrderRepository.save(tableOrder);
    }

    @Override
    @Transactional
    public TableOrderResult create(TableOrderParam param) {
        TableOrder entity = tableOrderMapper.toEntity(param);
        entity = tableOrderRepository.save(entity);
        return tableOrderMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TableOrderResult> findAllTablesWithoutSenderId(Long tableId) {
        return tableOrderMapper
                .toDTOList(tableOrderRepository
                        .findAllTablesWithoutSenderId(tableId));
    }

}
