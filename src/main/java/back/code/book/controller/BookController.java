package back.code.book.controller;

import back.code.book.dto.BookDTO;
import back.code.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 카테고리별 도서 목록 조회
    @GetMapping("/list/{categoryId}")
    public ResponseEntity<Map<String, Object>> getBooksByCategory(@PathVariable Integer categoryId) {
        Map<String, Object> resultMap = bookService.getBooksByCategory(categoryId);
        return ResponseEntity.ok(resultMap);
    }

    //도서 전체 목록 중 특가 도서 정렬
    @GetMapping("/sale")
    public ResponseEntity<Map<String, Object>> getSaleBooks() {
        Map<String, Object> resultMap = bookService.getSaleBooks();
        return ResponseEntity.ok(resultMap);
    }

    //도서 상세 조회
    @GetMapping("/detail/{bookId}")
    public ResponseEntity<Map<String, Object>> getBookDetail(@PathVariable int bookId) {
        Map<String, Object> result = bookService.getBookDetail(bookId);
        return ResponseEntity.ok(result);
    }

    //도서 검색
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBooks(@RequestParam String keyword) {
        Map<String, Object> result = bookService.searchBooks(keyword);
        return ResponseEntity.ok(result);
    }

    //전체 베스트셀러 Top10
    @GetMapping("/bestSeller")
    public ResponseEntity<Map<String, Object>> getBestSeller() {
        Map<String, Object> result =  bookService.getBestSeller();
        return ResponseEntity.ok(result);
    }

    //카테고리(메인/서브)별 베스트셀러 Top6
    @GetMapping("/bestSeller/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getBestSellerByCategory(@PathVariable Integer categoryId) {
        Map<String, Object> result = bookService.getBestSellerByCategory(categoryId);
        return ResponseEntity.ok(result);
    }

    //도서 등록
    @PostMapping("")
    public ResponseEntity<Map<String, Object>> addBook(
            @RequestPart("book") BookDTO bookDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(bookService.addBook(bookDTO, file));
    }

    //도서 수정
    @PutMapping("/{bookId}")
    public ResponseEntity<Map<String, Object>> updateBook(
            @PathVariable int bookId,
            @RequestPart("book") BookDTO bookDTO,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(bookService.updateBook(bookId, bookDTO, file));
    }

    //도서 삭제
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Map<String, Object>> deleteBook(@PathVariable int bookId) {
        return ResponseEntity.ok(bookService.deleteBook(bookId));
    }

    //도서 전체 목록 조회 (관리자용)
    @GetMapping("")
    public ResponseEntity<Map<String, Object>> getBookList() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
}