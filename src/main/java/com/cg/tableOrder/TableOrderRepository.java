package com.cg.tableOrder;

import com.cg.model.TableOrder;
import com.cg.tableOrder.dto.TableOrderResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TableOrderRepository extends JpaRepository<TableOrder,Long> {
    @Query("SELECT to " +
            "FROM TableOrder to " +
            "WHERE to.id <> :tableId " +
            "AND to.status = 'EMPTY'"
    )
    List<TableOrder> findAllTablesWithoutSenderId(@Param("tableId") Long tableId);

}
