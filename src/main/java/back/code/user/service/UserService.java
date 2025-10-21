package back.code.user.service;

import back.code.security.dto.UserSecureDTO;
import back.code.user.dto.*;
import back.code.user.entity.User;
import back.code.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //아이디 중복확인
    public boolean checkDuplicateId(String userId) {
        return userRepository.existsByUserId(userId);
    }

    //회원 가입
    public Map<String,Object> joinUser(UserJoinRequestDTO dto) {
        Map<String,Object> resultMap = new HashMap<>();

        try{

            //아이디 중복확인
            if(checkDuplicateId(dto.getUserId())) {
                throw new IllegalAccessException("이미 존재하는 아이디입니다.");
            }

            //User 엔티티 생성
            User user = UserJoinRequestDTO.to(dto, passwordEncoder);
            //DB에 저장
            userRepository.save(user);

            resultMap.put("msg", "회원가입 성공");
            resultMap.put("resultCode", 200);
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("msg", "회원가입 실패");
            resultMap.put("resultCode", 500);
        }

        return resultMap;
    }

    //로그인
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

        User user =
                userRepository.findByUserId(userId)
                        .orElseThrow(()-> new UsernameNotFoundException(userId + "을 찾을 수 없습니다."));

        return new UserSecureDTO(user.getUserId(), user.getName(),
                user.getPassword(), user.getRole().name());
    }

    //아이디 찾기
    public Map<String, Object> findUserId(FindIdRequestDTO dto) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            Optional<User> user = userRepository.findByNameAndEmail(dto.getName(), dto.getEmail());

            if (user.isPresent()) {
                resultMap.put("msg", "아이디 찾기 성공");
                resultMap.put("resultCode", 200);
                resultMap.put("userId", user.get().getUserId());
            } else {
                resultMap.put("msg", "등록된 회원 정보를 찾을 수 없습니다.");
                resultMap.put("resultCode", 404);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("msg", "아이디 찾기 중 오류가 발생했습니다.");
            resultMap.put("resultCode", 500);
        }

        return resultMap;
    }

    //비밀번호 찾기 - 사용자 정보 확인
    public Map<String, Object> findUserPw(FindPwCheckRequestDTO dto) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            User user = userRepository.findById(dto.getUserId()).orElse(null);

            if (user != null
                    && user.getName().equals(dto.getName())
                    && user.getEmail().equals(dto.getEmail())) {

                resultMap.put("msg", "사용자 확인 완료");
                resultMap.put("resultCode", 200);
            } else {
                resultMap.put("msg", "등록된 회원 정보를 찾을 수 없습니다.");
                resultMap.put("resultCode", 404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("msg", "비밀번호 찾기 중 오류가 발생했습니다.");
            resultMap.put("resultCode", 500);
        }

        return resultMap;
    }

    //비밀번호 재설정
    public Map<String, Object> resetUserPw(FindPwResetRequestDTO dto) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            User user = userRepository.findById(dto.getUserId()).orElse(null);

            if (user != null) {
                String encodedPw = passwordEncoder.encode(dto.getNewPassword());
                user.setPassword(encodedPw);
                userRepository.save(user);

                resultMap.put("msg", "비밀번호가 변경되었습니다.");
                resultMap.put("resultCode", 200);
            } else {
                resultMap.put("msg", "해당 사용자를 찾을 수 없습니다.");
                resultMap.put("resultCode", 404);
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("msg", "비밀번호 변경 중 오류가 발생했습니다.");
            resultMap.put("resultCode", 500);
        }

        return resultMap;
    }

    //회원정보 조회
    public Map<String, Object> getUserInfo(String userId) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            Optional<User> userOpt = userRepository.findById(userId);

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", user.getUserId());
                userInfo.put("name", user.getName());
                userInfo.put("email", user.getEmail());
                userInfo.put("address", user.getAddress());
                userInfo.put("detailAddress", user.getDetailAddress());
                userInfo.put("role", user.getRole().name());

                resultMap.put("resultCode", 200);
                resultMap.put("msg", "회원정보 조회 성공");
                resultMap.put("user", userInfo);

            } else {
                resultMap.put("resultCode", 404);
                resultMap.put("msg", "해당 사용자를 찾을 수 없습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "회원정보 조회 중 오류 발생");
        }

        return resultMap;
    }

    //회원정보 수정
    public Map<String, Object> updateUserInfo(UserInfoUpdateDTO dto) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            Optional<User> userOpt = userRepository.findById(dto.getUserId());

            if (userOpt.isEmpty()) {
                resultMap.put("resultCode", 404);
                resultMap.put("msg", "해당 사용자를 찾을 수 없습니다.");
                return resultMap;
            }

            User user = userOpt.get();

            // 기본 정보 수정
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            user.setAddress(dto.getAddress());
            user.setDetailAddress(dto.getDetailAddress());

            // 비밀번호 변경이 요청된 경우만 암호화 후 업데이트
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                String encodedPw = passwordEncoder.encode(dto.getPassword());
                user.setPassword(encodedPw);
            }

            userRepository.save(user);

            resultMap.put("resultCode", 200);
            resultMap.put("msg", "회원정보 수정 성공");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "회원정보 수정 중 오류 발생");
        }

        return resultMap;
    }

    //회원탈퇴
    public Map<String, Object> deleteUser(UserDeleteRequestDTO dto) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
            Optional<User> userOpt = userRepository.findById(dto.getUserId());

            //사용자 존재 여부 확인
            if (userOpt.isEmpty()) {
                resultMap.put("resultCode", 404);
                resultMap.put("msg", "해당 사용자를 찾을 수 없습니다.");
                return resultMap;
            }

            User user = userOpt.get();

            //관리자 계정 여부 확인
            if (user.getRole().name().equalsIgnoreCase("ADMIN")) {
                resultMap.put("resultCode", 403);
                resultMap.put("msg", "관리자 계정은 탈퇴할 수 없습니다.");
                return resultMap;
            }

            //비밀번호 일치 여부 확인
            if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                resultMap.put("resultCode", 401);
                resultMap.put("msg", "비밀번호가 일치하지 않습니다.");
                return resultMap;
            }

            //회원 삭제
            userRepository.delete(user);

            resultMap.put("resultCode", 200);
            resultMap.put("msg", "회원탈퇴가 완료되었습니다.");

        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
            resultMap.put("msg", "회원탈퇴 중 오류가 발생했습니다.");
        }

        return resultMap;
    }

}
