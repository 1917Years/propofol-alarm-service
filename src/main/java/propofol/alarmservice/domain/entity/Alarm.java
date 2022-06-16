package propofol.alarmservice.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import propofol.alarmservice.domain.BaseEntity;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    private long toId;
    private String message;
    private Long boardId;

    @Enumerated(value = EnumType.STRING)
    private AlarmStatus status;

    @Enumerated(value = EnumType.STRING)
    private AlarmType type;

    public void changeStatus(AlarmStatus status){
        this.status = status;
    }

    @Builder(builderMethodName = "createAlarm")
    public Alarm(long toId, String message, Long boardId, AlarmType type) {
        this.toId = toId;
        this.message = message;
        this.type = type;
        this.boardId = boardId;
        this.status = AlarmStatus.UNREAD;
    }
}
