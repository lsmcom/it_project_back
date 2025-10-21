package back.code.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseItemResponseDTO {
    private int bookId;
    private String bookName;
    private String author;
    private int quantity;
    private int price;
    private String filePath;
}
