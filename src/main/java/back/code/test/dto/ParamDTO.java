package back.code.test.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ParamDTO {

    @NotBlank(message = "이름을 입력하십시오")
    @Size(min =2, max=10 , message = "이름은 2 ~10자 사이여야 합니다.")
    private String myName;

    @Email(message = "이메일 형식이 아닙니다")
    private String email;
}
