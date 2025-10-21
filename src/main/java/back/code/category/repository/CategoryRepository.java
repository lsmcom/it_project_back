package back.code.category.repository;

import back.code.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    //대분류 조회(parent_id IS NULL)
    List<Category> findByParentIdIsNull();

    //특정 parent_id 하위 카테고리 조회
    List<Category> findByParentId(Integer parentId);

    boolean existsByParentId(Integer parentId);

    @Query("SELECT c.categoryId FROM Category c WHERE c.parentId = :parentId")
    List<Integer> findSubCategoryIds(Integer parentId);
}
