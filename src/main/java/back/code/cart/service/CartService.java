package back.code.cart.service;

import back.code.book.entity.Book;
import back.code.book.repository.BookRepository;
import back.code.cart.dto.CartDTO;
import back.code.cart.dto.CartResponseDTO;
import back.code.cart.entity.Cart;
import back.code.cart.repository.CartRepository;
import back.code.user.entity.User;
import back.code.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    //장바구니 추가
    public Map<String, Object> addCart(CartDTO dto) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
            Book book = bookRepository.findById(dto.getBookId())
                    .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));

            Optional<Cart> existingCart = cartRepository.findByUser_UserIdAndBook_BookId(dto.getUserId(), dto.getBookId());

            if (existingCart.isPresent()) {
                Cart cart = existingCart.get();
                cart.setQuantity(cart.getQuantity() + dto.getQuantity());
                cartRepository.save(cart);
                resultMap.put("msg", "이미 존재하는 도서입니다. 수량을 추가했습니다.");
            } else {
                Cart cart = new Cart();
                cart.setUser(user);
                cart.setBook(book);
                cart.setQuantity(dto.getQuantity());
                cart.setPrice(book.getPrice());
                cartRepository.save(cart);
                resultMap.put("msg", "장바구니에 추가되었습니다.");
            }

            resultMap.put("resultCode", 200);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "장바구니 추가 중 오류 발생");
        }
        return resultMap;
    }

    //장바구니 목록 조회
    @Transactional(readOnly = true)
    public Map<String, Object> getCartList(String userId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Cart> carts = cartRepository.findByUser_UserId(userId);
            List<CartResponseDTO> cartList = carts.stream()
                    .map(c -> CartResponseDTO.builder()
                            .cartId(c.getCartId())
                            .quantity(c.getQuantity())
                            .price(c.getPrice())
                            .userId(c.getUser().getUserId())
                            .bookId(c.getBook().getBookId())
                            .bookName(c.getBook().getBookName())
                            .author(c.getBook().getAuthor())
                            .bookPrice(c.getBook().getPrice())
                            .filePath(c.getBook().getFilePath())
                            .build())
                    .collect(Collectors.toList());

            resultMap.put("data", cartList); // ✅ DTO 반환
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "장바구니 조회 성공");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "장바구니 조회 실패");
        }
        return resultMap;
    }

    //장바구니 삭제
    public Map<String, Object> deleteCart(int cartId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            cartRepository.deleteById(cartId);
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "장바구니 항목이 삭제되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "장바구니 삭제 실패");
        }
        return resultMap;
    }

    //수량 변경
    public Map<String, Object> updateQuantity(int cartId, int quantity) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Cart cart = cartRepository.findById(cartId)
                    .orElseThrow(() -> new RuntimeException("장바구니 항목을 찾을 수 없습니다."));
            cart.setQuantity(quantity);
            cartRepository.save(cart);
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "수량이 변경되었습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "수량 변경 실패");
        }
        return resultMap;
    }
}