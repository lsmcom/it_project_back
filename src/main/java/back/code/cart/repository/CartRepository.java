package back.code.cart.repository;

import back.code.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser_UserIdAndBook_BookId(String userId, int bookId);
    List<Cart> findByUser_UserId(String userId);
}