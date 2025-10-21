package back.code.user.repository;

import back.code.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {

    //아이디 중복 확인
    boolean existsByUserId(String userId);

    //아이디 찾기
    Optional<User> findByUserId(String userId);

    //이름 + 이메일로 아이디 찾기
    Optional<User> findByNameAndEmail(String name, String email);
}
