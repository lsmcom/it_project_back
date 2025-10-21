package back.code.user.controller;

import back.code.user.dto.*;
import back.code.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 아이디 중복확인 API
    @GetMapping("/check-id")
    public ResponseEntity<Boolean> checkId(@RequestParam String userId) {
        boolean exists = userService.checkDuplicateId(userId);
        return ResponseEntity.ok(exists);
    }

    // 회원가입 API
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody UserJoinRequestDTO request) {
        Map<String, Object> resultMap = userService.joinUser(request);
        return ResponseEntity.ok(resultMap);
    }

    //아이디 찾기 API
    @PostMapping("/find-id")
    public ResponseEntity<Map<String, Object>> findId(@Valid @RequestBody FindIdRequestDTO request) {
        Map<String, Object> resultMap = userService.findUserId(request);
        return ResponseEntity.ok(resultMap);
    }

    //비밀번호 찾기 - 사용자 확인
    @PostMapping("/find-pw")
    public ResponseEntity<Map<String, Object>> findPw(@RequestBody FindPwCheckRequestDTO dto) {
        Map<String, Object> resultMap = userService.findUserPw(dto);
        return ResponseEntity.ok(resultMap);
    }

    //비밀번호 재설정
    @PostMapping("/reset-pw")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody FindPwResetRequestDTO dto) {
        Map<String, Object> resultMap = userService.resetUserPw(dto);
        return ResponseEntity.ok(resultMap);
    }

    //회원정보 조회
    @GetMapping("/info/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable String userId) {
        Map<String, Object> resultMap = userService.getUserInfo(userId);
        return ResponseEntity.ok(resultMap);
    }

    //회원정보 수정
    @PutMapping("/info/update")
    public ResponseEntity<?> updateUserInfo(@RequestBody UserInfoUpdateDTO dto) {
        Map<String, Object> resultMap = userService.updateUserInfo(dto);
        return ResponseEntity.ok(resultMap);
    }

    //회원탈퇴 API
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(@RequestBody UserDeleteRequestDTO dto) {
        Map<String, Object> resultMap = userService.deleteUser(dto);
        return ResponseEntity.ok(resultMap);
    }
}
