package back.code.purchase.repository;

import back.code.purchase.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Integer> {
    List<PurchaseOrder> findByUser_UserId(String userId);

    //구매 횟수 조회
    int countByUser_UserId(String userId);
}
