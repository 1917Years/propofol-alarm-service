package propofol.alarmservice.api.controller.dto;

import lombok.Data;
import propofol.alarmservice.domain.entity.AlarmType;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AlarmSaveListRequestDto {
    @NotNull(message = "저장 아이디 null")
    private List<Long> toIds;
    @NotNull(message = "저장 메시지 null")
    private String message;
    @NotNull(message = "저장 타입 null")
    private String type;
    private Long boardId;
}
