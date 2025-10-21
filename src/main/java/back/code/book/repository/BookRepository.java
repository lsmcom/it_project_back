package back.code.book.repository;

import back.code.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer> {

    //대분류(=parent_id) 카테고리에 속한 모든 도서
    List<Book> findByCategoryIdIn(List<Integer> categoryIds);

    //서브 카테고리별 도서
    List<Book> findByCategoryId(int categoryId);

    //특가 도서 12권 정렬
    List<Book> findTop12ByOrderByPriceAsc();

    //도서와 카테고리 정보 함게 조회
    @Query("SELECT b, c, p FROM Book b " +
            "JOIN Category c ON b.categoryId = c.categoryId " +
            "LEFT JOIN Category p ON c.parentId = p.categoryId " +
            "WHERE b.bookId = :bookId")
    List<Object[]> findBookWithCategory(@Param("bookId") int bookId);

    // 도서명 또는 저자명 부분 일치 검색
    @Query("SELECT b FROM Book b WHERE b.bookName LIKE %:keyword% OR b.author LIKE %:keyword%")
    List<Book> searchBooks(@Param("keyword") String keyword);

    //전체 베스트셀러 Top10
    @Query("""
        SELECT b
        FROM Book b
        JOIN PurchaseItem pi ON b.bookId = pi.book.bookId
        GROUP BY b.bookId
        ORDER BY SUM(pi.quantity) DESC
        LIMIT 10
    """)
    List<Book> findTop10BestSeller();

    //카테고리(메인 또는 서브)별 Top6
    @Query(value = """
        SELECT b.*
        FROM purchase_item pi
        JOIN book b ON pi.book_id = b.book_id
        JOIN category c ON b.category_id = c.category_id
        WHERE c.category_id = :categoryId
           OR c.parent_id  = :categoryId
        GROUP BY b.book_id
        ORDER BY SUM(pi.quantity) DESC
        LIMIT 6
    """, nativeQuery = true)
    List<Book> findBestSellerByCategoryInclusive(@Param("categoryId") Integer categoryId);

}