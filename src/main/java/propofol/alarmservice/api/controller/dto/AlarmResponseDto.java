package propofol.alarmservice.api.controller.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import propofol.alarmservice.domain.entity.AlarmStatus;
import propofol.alarmservice.domain.entity.AlarmType;

@Data
@NoArgsConstructor
public class AlarmResponseDto {
    private Long id;
    private long toId;
    private Long boardId;
    private String message;
    private AlarmStatus status;
    private AlarmType type;
    private String createdDateTime;
}
