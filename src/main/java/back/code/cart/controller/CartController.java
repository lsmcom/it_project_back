package back.code.cart.controller;

import back.code.cart.dto.CartDTO;
import back.code.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addCart(@RequestBody CartDTO dto) {
        Map<String, Object> result = cartService.addCart(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getCartList(@RequestParam String userId) {
        Map<String, Object> result = cartService.getCartList(userId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<Map<String, Object>> deleteCart(@PathVariable int cartId) {
        Map<String, Object> result = cartService.deleteCart(cartId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{cartId}")
    public ResponseEntity<Map<String, Object>> updateQuantity(@PathVariable int cartId, @RequestParam int quantity) {
        Map<String, Object> result = cartService.updateQuantity(cartId, quantity);
        return ResponseEntity.ok(result);
    }
}