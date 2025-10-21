package back.code.category.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId; // PK

    @Column(nullable = false, unique = false, length = 100)
    private String categoryName; //카테고리 이름

    @Column(nullable = true)
    private Integer parentId; //상위 카테고리 ID (NULL이면 대분류)
}
