package back.code.purchase.service;

import back.code.book.entity.Book;
import back.code.book.repository.BookRepository;
import back.code.cart.entity.Cart;
import back.code.cart.repository.CartRepository;
import back.code.purchase.dto.PurchaseItemResponseDTO;
import back.code.purchase.dto.PurchaseResponseDTO;
import back.code.purchase.entity.PurchaseItem;
import back.code.purchase.entity.PurchaseOrder;
import back.code.purchase.repository.PurchaseItemRepository;
import back.code.purchase.repository.PurchaseOrderRepository;
import back.code.user.entity.User;
import back.code.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    //장바구니에서 구매
    @Transactional
    public Map<String, Object> purchaseFromCart(String userId) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

            List<Cart> cartItems = cartRepository.findByUser_UserId(userId);
            if (cartItems.isEmpty()) {
                resultMap.put("resultCode", 400);
                resultMap.put("msg", "장바구니가 비어 있습니다.");
                return resultMap;
            }

            int totalPrice = cartItems.stream()
                    .mapToInt(c -> c.getPrice() * c.getQuantity())
                    .sum();

            PurchaseOrder order = new PurchaseOrder();
            order.setUser(user);
            order.setTotalPrice(totalPrice);

            List<PurchaseItem> purchaseItems = new ArrayList<>();
            for (Cart cart : cartItems) {
                PurchaseItem item = new PurchaseItem();
                item.setOrder(order);
                item.setBook(cart.getBook());
                item.setQuantity(cart.getQuantity());
                item.setPrice(cart.getBook().getPrice());
                purchaseItems.add(item);
            }

            order.setItems(purchaseItems);
            purchaseOrderRepository.save(order);
            cartRepository.deleteAll(cartItems); // 장바구니 비우기

            resultMap.put("resultCode", 200);
            resultMap.put("msg", "구매가 완료되었습니다.");
            resultMap.put("orderId", order.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "구매 처리 중 오류 발생");
        }

        return resultMap;
    }

    //상품 상세에서 바로 구매
    public Map<String, Object> directPurchase(String userId, int bookId, int quantity) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));

            int totalPrice = book.getPrice() * quantity;

            PurchaseOrder order = new PurchaseOrder();
            order.setUser(user);
            order.setTotalPrice(totalPrice);

            PurchaseItem item = new PurchaseItem();
            item.setOrder(order);
            item.setBook(book);
            item.setQuantity(quantity);
            item.setPrice(book.getPrice());

            order.setItems(Collections.singletonList(item));
            purchaseOrderRepository.save(order);

            resultMap.put("resultCode", 200);
            resultMap.put("msg", "구매가 완료되었습니다.");
            resultMap.put("orderId", order.getOrderId());
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "바로 구매 중 오류 발생");
        }

        return resultMap;
    }

    //사용자별 구매내역 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getPurchaseList(String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<PurchaseOrder> orders = purchaseOrderRepository.findByUser_UserId(userId);

            List<PurchaseResponseDTO> data = orders.stream().map(order -> {
                List<PurchaseItemResponseDTO> items = order.getItems().stream()
                        .map(item -> new PurchaseItemResponseDTO(
                                item.getBook().getBookId(),
                                item.getBook().getBookName(),
                                item.getBook().getAuthor(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getBook().getFilePath()
                        ))
                        .collect(Collectors.toList());

                return new PurchaseResponseDTO(
                        order.getOrderId(),
                        order.getOrderDate(),
                        order.getTotalPrice(),
                        items
                );
            }).collect(Collectors.toList());

            resultMap.put("resultCode", 200);
            resultMap.put("msg", "구매내역 조회 성공");
            resultMap.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "구매내역 조회 실패");
        }

        return resultMap;
    }

    //사용자별 구매 횟수 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getPurchaseCount(String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            int count = purchaseOrderRepository.countByUser_UserId(userId);
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "구매횟수 조회 성공");
            resultMap.put("data", count);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "구매횟수 조회 실패");
        }
        return resultMap;
    }
}
