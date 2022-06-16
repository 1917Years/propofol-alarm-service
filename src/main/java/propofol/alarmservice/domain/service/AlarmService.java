package propofol.alarmservice.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import propofol.alarmservice.domain.entity.Alarm;
import propofol.alarmservice.domain.entity.AlarmStatus;
import propofol.alarmservice.domain.entity.AlarmType;
import propofol.alarmservice.domain.exception.NotFoundAlarmException;
import propofol.alarmservice.domain.repository.AlarmRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final AlarmRepository alarmRepository;

    public Page<Alarm> getAllAlarmByMemberId(Long memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        return alarmRepository.findAllAlarmByMemberId(memberId, pageRequest);
    }

    public Page<Alarm> getAllAlarmByMemberIdAndStatus(Long memberId, int page, AlarmStatus status) {
        PageRequest pageRequest = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        return alarmRepository.findAllAlarmByMemberIdAndStatus(memberId, status, pageRequest);
    }

    public Alarm save(long toId, String message, Long boardId, AlarmType type) {
        return alarmRepository.save(createAlarm(toId, message, boardId, type));
    }

    private Alarm createAlarm(long toId, String message, Long boardId, AlarmType type) {
        return Alarm.createAlarm().toId(toId).message(message).type(type).boardId(boardId).build();
    }

    public String deleteAlarm(Long alarmId) {
        alarmRepository.deleteByAlarmId(alarmId);
        return "ok";
    }

    public String deleteAllAlarm(Long memberId) {
        alarmRepository.deleteAllByMemberId(memberId);
        return "ok";
    }

//    public String saveList(List<Long> toIds, List<String> messages, List<AlarmType> types) {
//        List<Alarm> alarms = new ArrayList<>();
//        for (int i = 0; i < toIds.size(); i++) {
//            alarms.add(createAlarm(toIds.get(i), messages.get(i), types.get(i)));
//        }
//
//        alarmRepository.saveAll(alarms);
//        return "ok";
//    }
}
