package back.code.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {
    private String userId;
    private int bookId;
    private int quantity;
}