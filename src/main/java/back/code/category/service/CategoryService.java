package back.code.category.service;

import back.code.category.entity.Category;
import back.code.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    //대분류 조회
    public Map<String, Object> getMainCategories() {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            List<Category> categories = categoryRepository.findByParentIdIsNull();
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "대분류 조회 성공");
            resultMap.put("data", categories);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "대분류 조회 실패");
        }

        return resultMap;
    }

    //서브카테고리 조회
    public Map<String, Object> getSubCategories(Integer parentId) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            List<Category> subCategories = categoryRepository.findByParentId(parentId);
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "서브카테고리 조회 성공");
            resultMap.put("data", subCategories);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "서브카테고리 조회 실패");
        }

        return resultMap;
    }
}