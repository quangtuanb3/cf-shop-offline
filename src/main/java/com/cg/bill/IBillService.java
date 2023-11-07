package com.cg.bill;


import com.cg.model.Bill;
import com.cg.bill.dto.CreationBillParam;
import com.cg.bill.dto.BillResult;
import com.cg.bill.dto.BillDetailResult;
import com.cg.service.IGeneralService;

import java.util.Date;
import java.util.List;

public interface IBillService extends IGeneralService<Bill,Long> {
    List<BillResult> findAllBillDTO();

    List<BillDetailResult> findBillById(Long billId);

    List<BillResult> findBillByCreatedAts(Date BillDate);

    CreationBillParam createBill(Long tableId);

    List<BillResult> getBillByDate(Integer year, Integer month, Integer day);


}
