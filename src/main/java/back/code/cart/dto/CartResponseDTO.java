package back.code.cart.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {
    private int cartId;
    private int quantity;
    private int price;
    private String userId;
    private int bookId;
    private String bookName;
    private String author;
    private int bookPrice;
    private String filePath;
}