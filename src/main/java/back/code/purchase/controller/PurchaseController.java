package back.code.purchase.controller;

import back.code.purchase.service.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/purchase")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    //장바구니 구매
    @PostMapping("/cart")
    public ResponseEntity<Map<String, Object>> purchaseFromCart(@RequestParam String userId) {
        Map<String, Object> result = purchaseService.purchaseFromCart(userId);
        return ResponseEntity.ok(result);
    }

    //상품 상세에서 구매
    @PostMapping("/direct")
    public ResponseEntity<Map<String, Object>> directPurchase(
            @RequestParam String userId,
            @RequestParam int bookId,
            @RequestParam int quantity
    ) {
        Map<String, Object> result = purchaseService.directPurchase(userId, bookId, quantity);
        return ResponseEntity.ok(result);
    }

    //장바구니 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getPurchaseList(@RequestParam String userId) {
        Map<String, Object> result  = purchaseService.getPurchaseList(userId);
        return ResponseEntity.ok(result);
    }

    //사용자별 구매 횟수 조회
    @GetMapping("/count")
    public ResponseEntity<Map<String, Object>> getPurchaseCount(@RequestParam String userId) {
        Map<String, Object> result = purchaseService.getPurchaseCount(userId);
        return ResponseEntity.ok(result);
    }

}
