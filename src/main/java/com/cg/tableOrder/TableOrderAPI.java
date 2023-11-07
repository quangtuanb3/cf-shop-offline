package com.cg.tableOrder;

import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.TableOrder;
import com.cg.tableOrder.dto.TableOrderParam;
import com.cg.tableOrder.dto.TableOrderResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tableOrders")
public class TableOrderAPI {
    @Autowired
    private ITableOrderService tableOrderService;

    @GetMapping
    public ResponseEntity<?> getAllTableOrder() {
        List<TableOrderResult> tableOrderDTO = tableOrderService.findAll();
        if(tableOrderDTO.isEmpty()) {
            throw new ResourceNotFoundException("Không có bàn nào vui lòng kiểm tra lại hệ thống");
        }
        return new ResponseEntity<>(tableOrderDTO,HttpStatus.OK);
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<?> getTableOrderById(@PathVariable Long tableId){
        return new ResponseEntity<>(tableOrderService.getById(tableId),HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createTableOrder(@RequestBody TableOrderParam tableOrderCreateReqDTO){
        return new ResponseEntity<>(tableOrderService.create(tableOrderCreateReqDTO),HttpStatus.CREATED);
    }

    @GetMapping("/tables-without-tableSend/{tableId}")
    public ResponseEntity<?> getAllTablesWithoutSender(@PathVariable Long tableId) {

        List<TableOrderResult> tableSelect = tableOrderService.findAllTablesWithoutSenderId(tableId);

        return new ResponseEntity<>(tableSelect, HttpStatus.OK);
    }

}
