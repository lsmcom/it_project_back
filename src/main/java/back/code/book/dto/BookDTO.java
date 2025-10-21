package back.code.book.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private int categoryId;
    private String bookName;
    private String author;
    private String description;
    private Integer price;

}
