package back.code.user.dto;

import back.code.user.entity.Role;
import back.code.user.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserJoinRequestDTO {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotNull(message = "생년월일을 입력해주세요.")
    private LocalDate birth;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "상세주소를 입력해주세요.")
    private String detailAddress;

    public static User to(UserJoinRequestDTO dto, BCryptPasswordEncoder bCryptPasswordEncoder) {
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setBirth(dto.getBirth());
        user.setAddress(dto.getAddress());
        user.setDetailAddress(dto.getDetailAddress());
        user.setRole(Role.USER);

        return user;
    }
}
