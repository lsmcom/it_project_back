package back.code.category.controller;

import back.code.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    //대분류 조회
    @GetMapping
    public ResponseEntity<Map<String, Object>> getMainCategories() {
        Map<String, Object> resultMap = categoryService.getMainCategories();
        return ResponseEntity.ok(resultMap);
    }

    //서브카테고리 조회
    @GetMapping("/{parentId}")
    public ResponseEntity<Map<String, Object>> getSubCategories(@PathVariable Integer parentId) {
        Map<String, Object> resultMap = categoryService.getSubCategories(parentId);
        return ResponseEntity.ok(resultMap);
    }
}