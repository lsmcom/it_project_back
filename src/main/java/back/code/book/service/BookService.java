package back.code.book.service;

import back.code.book.dto.BookDTO;
import back.code.book.entity.Book;
import back.code.book.repository.BookRepository;
import back.code.category.entity.Category;
import back.code.category.repository.CategoryRepository;
import back.code.common.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtils fileUtils;

    private static final String BOOK_PATH = "C:\\files\\upload\\books\\";
    private static final String BOOK_URL = "/uploads/books/";
    private static final String THUMB_PATH = "C:\\files\\upload\\thumb\\books\\";

    // 카테고리별 (대분류 or 서브) 도서 목록 조회
    public Map<String, Object> getBooksByCategory(Integer categoryId) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            List<Book> books;

            // 대분류인지 서브인지 판별
            boolean isParentCategory = categoryRepository.existsByParentId(categoryId);
            if (isParentCategory) {
                List<Integer> subCategoryIds = categoryRepository.findSubCategoryIds(categoryId);
                books = bookRepository.findByCategoryIdIn(subCategoryIds);
            } else {
                books = bookRepository.findByCategoryId(categoryId);
            }

            resultMap.put("resultCode", 200);
            resultMap.put("msg", "도서 목록 조회 성공");
            resultMap.put("data", books);

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "도서 목록 조회 실패");
        }

        return resultMap;
    }

    //도서 전체 목록 중 특가 도서 정렬
    public Map<String, Object> getSaleBooks() {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            // 가격 낮은 순으로 정렬, 최대 12개
            List<Book> books = bookRepository.findTop12ByOrderByPriceAsc();

            resultMap.put("resultCode", 200);
            resultMap.put("msg", "이번주 특가 도서 조회 성공");
            resultMap.put("data", books);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "이번주 특가 도서 조회 실패");
        }

        return resultMap;
    }

    //도서 상세 조회
    public Map<String, Object> getBookDetail(int bookId) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            List<Object[]> results = bookRepository.findBookWithCategory(bookId);

            if (!results.isEmpty()) {
                Object[] arr = results.get(0);
                Book book = (Book) arr[0];
                Category subCategory = (Category) arr[1];
                Category mainCategory = (Category) arr[2];

                Map<String, Object> data = new HashMap<>();
                data.put("bookId", book.getBookId());
                data.put("bookName", book.getBookName());
                data.put("author", book.getAuthor());
                data.put("description", book.getDescription());
                data.put("price", book.getPrice());
                data.put("filePath", book.getFilePath());
                data.put("regDate", book.getRegDate());
                data.put("mainCategory", mainCategory != null ? mainCategory.getCategoryName() : "메인카테고리 없음");
                data.put("subCategory", subCategory != null ? subCategory.getCategoryName() : "서브카테고리 없음");

                resultMap.put("resultCode", 200);
                resultMap.put("msg", "도서 상세 조회 성공");
                resultMap.put("data", data);
            } else {
                resultMap.put("resultCode", 404);
                resultMap.put("msg", "도서를 찾을 수 없습니다.");
                resultMap.put("data", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "도서 상세 조회 중 오류 발생");
            resultMap.put("data", null);
        }

        return resultMap;
    }

    //도서 검색
    public Map<String, Object> searchBooks(String keyword) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Book> books = bookRepository.searchBooks(keyword);

            resultMap.put("resultCode", 200);
            resultMap.put("msg", "검색 성공");
            resultMap.put("data", books);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "검색 실패");
        }
        return resultMap;
    }

    //전체 베스트셀러 Top10
    public Map<String, Object> getBestSeller() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Book> bestSeller = bookRepository.findTop10BestSeller();
            resultMap.put("data", bestSeller);
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "전체 베스트셀러 조회 성공");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "베스트셀러 조회 실패");
        }
        return resultMap;
    }

    //카테고리별 베스트셀러 Top6
    public Map<String, Object> getBestSellerByCategory(Integer categoryId) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Book> list = bookRepository.findBestSellerByCategoryInclusive(categoryId);
            map.put("resultCode", 200);
            map.put("msg", "카테고리 베스트셀러 조회 성공");
            map.put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("resultCode", 500);
            map.put("msg", "카테고리 베스트셀러 조회 실패");
        }
        return map;
    }

    //도서 등록
    public Map<String, Object> addBook(BookDTO dto, MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Book book = new Book();
            book.setCategoryId(dto.getCategoryId());
            book.setBookName(dto.getBookName());
            book.setAuthor(dto.getAuthor());
            book.setDescription(dto.getDescription());
            book.setPrice(dto.getPrice());

            if (file != null && !file.isEmpty()) {
                Map<String, Object> fileInfo = fileUtils.uploadFiles(file, BOOK_PATH);
                String thumbFileName = fileUtils.thumbNailFIle(200, 240,
                        new File(BOOK_PATH + fileInfo.get("storedFileName")),
                        THUMB_PATH);

                book.setFileName((String) fileInfo.get("fileName"));
                book.setFileSavedName((String) fileInfo.get("storedFileName"));
                book.setFilePath(BOOK_URL + fileInfo.get("storedFileName"));
                book.setThumbnailName(thumbFileName);
            }

            bookRepository.save(book);
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "도서 등록 성공");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "도서 등록 실패");
        }
        return resultMap;
    }

    //도서 수정
    public Map<String, Object> updateBook(int bookId, BookDTO dto, MultipartFile file) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            Book book = bookRepository.findById(bookId)
                    .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));

            book.setBookName(dto.getBookName());
            book.setAuthor(dto.getAuthor());
            book.setDescription(dto.getDescription());
            book.setPrice(dto.getPrice());
            book.setCategoryId(dto.getCategoryId());

            if (file != null && !file.isEmpty()) {
                Map<String, Object> fileInfo = fileUtils.uploadFiles(file, BOOK_PATH);
                String thumbFileName = fileUtils.thumbNailFIle(200, 240,
                        new File(BOOK_PATH + fileInfo.get("storedFileName")),
                        THUMB_PATH);

                book.setFileName((String) fileInfo.get("fileName"));
                book.setFileSavedName((String) fileInfo.get("storedFileName"));
                book.setFilePath(BOOK_URL + fileInfo.get("storedFileName"));
                book.setThumbnailName(thumbFileName);
            }

            bookRepository.save(book);
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "도서 수정 성공");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "도서 수정 실패");
        }
        return resultMap;
    }

    //도서 삭제
    public Map<String, Object> deleteBook(int bookId) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            bookRepository.deleteById(bookId);
            resultMap.put("resultCode", 200);
            resultMap.put("msg", "도서 삭제 성공");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "도서 삭제 실패");
        }
        return resultMap;
    }

    //전체 도서 조회
    public Map<String, Object> getAllBooks() {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            List<Book> books = bookRepository.findAll();
            resultMap.put("resultCode", 200);
            resultMap.put("data", books);
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "도서 조회 실패");
        }
        return resultMap;
    }
}