package propofol.alarmservice.api.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import propofol.alarmservice.domain.entity.AlarmStatus;
import propofol.alarmservice.domain.entity.AlarmType;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

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
