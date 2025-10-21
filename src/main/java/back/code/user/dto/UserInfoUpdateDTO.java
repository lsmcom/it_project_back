package back.code.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoUpdateDTO {
    private String userId;
    private String name;
    private String email;
    private String address;
    private String detailAddress;
    private String password; // 변경 비밀번호 (선택)
}