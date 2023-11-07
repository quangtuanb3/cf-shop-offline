package com.cg.bill;


import com.cg.exception.DataInputException;

import com.cg.bill.dto.CreationBillParam;
import com.cg.bill.dto.BillResult;
import com.cg.bill.dto.BillDetailResult;
import com.cg.tableOrder.ITableOrderService;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillAPI {

    @Autowired
    private IBillService billService;

    @Autowired
    private ValidateUtils validateUtils;

    @Autowired
    private ITableOrderService tableOrderService;


    @GetMapping
    public ResponseEntity<?> showBill() {

        List<BillResult> billResults = billService.findAllBillDTO();

        return new ResponseEntity<>(billResults, HttpStatus.OK);
    }

    @GetMapping("/{billId}")
    public ResponseEntity<?> showBillDetail(@PathVariable("billId") String billIdStr) {

        if (!validateUtils.isNumberValid(billIdStr)) {
            throw new DataInputException("Mã lịch sử không hợp lệ");
        }
        Long billId = Long.parseLong(billIdStr);

        billService.findById(billId).orElseThrow(() -> {
            throw new DataInputException("Mã lịch sử không tồn tại");
        });

        List<BillDetailResult> billDetailResults = billService.findBillById(billId);
        return new ResponseEntity<>(billDetailResults, HttpStatus.OK);
    }

    @GetMapping("/search/day")
    public ResponseEntity<?> seachBill(@RequestParam("eventDate") String eventDate) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date dateBill;
        try {
            dateBill = sdf.parse(eventDate);
        } catch (ParseException e) {
            throw new DataInputException("vui lòng nhập đúng kiểu năm/tháng/ngày");
        }

        List<BillResult> billResults = billService.findBillByCreatedAts(dateBill);

        return new ResponseEntity<>(billResults, HttpStatus.OK);
    }

    @PostMapping("/{tableId}")
    public ResponseEntity<?> createBill(@PathVariable("tableId") String tableIdStr) {

        if (!validateUtils.isNumberValid(tableIdStr)) {
            throw new DataInputException("Mã bàn không hợp lệ");
        }

        Long tableId = Long.parseLong(tableIdStr);

//        tableOrderService.findById(tableId).orElseThrow(() -> {
//            throw new DataInputException("Mã bàn không tồn tại");
//        });

        Boolean isExisted = tableOrderService.existById(tableId);
        if(!isExisted) {
            throw  new DataInputException("Mã bàn không tồn tại");
        }

        CreationBillParam billResDTO = billService.createBill(tableId);

        return new ResponseEntity<>(billResDTO, HttpStatus.CREATED);

    }


    @GetMapping("date")
    public ResponseEntity<?> getBillByDate(@RequestParam(required = false) Integer day, @RequestParam Integer month, @RequestParam Integer year) {
        return new ResponseEntity<>(billService.getBillByDate(year, month, day), HttpStatus.OK);
    }

}
