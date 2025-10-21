package back.code.purchase.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseResponseDTO {
    private int orderId;
    private LocalDateTime purchaseDate;
    private int totalPrice;
    private List<PurchaseItemResponseDTO> items;
}