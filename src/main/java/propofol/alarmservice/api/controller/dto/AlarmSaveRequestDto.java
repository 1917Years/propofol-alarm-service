package propofol.alarmservice.api.controller.dto;

import lombok.Data;
import propofol.alarmservice.domain.entity.AlarmType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AlarmSaveRequestDto {
    @NotNull(message = "저장 아이디 null")
    private long toId;
    @NotBlank(message = "저장 메시지 형식 오류")
    private String message;
    @NotNull(message = "저장 타입 null")
    private AlarmType type;
    private Long boardId;
}
