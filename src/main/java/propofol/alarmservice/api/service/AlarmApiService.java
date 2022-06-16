package propofol.alarmservice.api.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import propofol.alarmservice.api.Sse.service.SseService;
import propofol.alarmservice.api.service.dto.AlarmResponseDto;
import propofol.alarmservice.domain.entity.Alarm;
import propofol.alarmservice.domain.entity.AlarmType;
import propofol.alarmservice.domain.service.AlarmService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlarmApiService {
    private final SseService sseService;
    private final AlarmService alarmService;
    private final ModelMapper modelMapper;

    @Transactional
    public String saveWithAlarm(long toId, String message, AlarmType type, Long boardId){
        Alarm saveAlarm = alarmService.save(toId, message, boardId, type);

        Map<String, SseEmitter> emitter = sseService.getEmitter(String.valueOf(toId));

        if(emitter != null){
            // 알림 하나만 연결을 유지하므로 보통 하나만 있음.
            System.out.println("emitter.size() = " + emitter.size());
            Map.Entry<String, SseEmitter> emitterEntry = emitter.entrySet().iterator().next();
            AlarmResponseDto responseDto = modelMapper.map(saveAlarm, AlarmResponseDto.class);
            responseDto.setCreatedDateTime(saveAlarm.getCreatedDate().toString());

            sseService.sendMessage(emitterEntry.getValue(), String.valueOf(toId),
                    emitterEntry.getKey(), responseDto);
        }

        return "ok";
    }

    public String saveListWithAlarm(List<Long> toIds, String message, String type, Long boardId) {
        for (int i = 0; i < toIds.size(); i++) {
            Alarm saveAlarm = alarmService.save(toIds.get(i), message, boardId, AlarmType.valueOf(type));

            Map<String, SseEmitter> emitter = sseService.getEmitter(String.valueOf(toIds.get(i)));
            if(emitter != null){
                System.out.println("emitter.size() = " + emitter.size());
                // 알림 하나만 연결을 유지하므로 보통 하나만 있음.
                Map.Entry<String, SseEmitter> emitterEntry = emitter.entrySet().iterator().next();
                AlarmResponseDto responseDto = modelMapper.map(saveAlarm, AlarmResponseDto.class);
                responseDto.setCreatedDateTime(saveAlarm.getCreatedDate().toString());

                sseService.sendMessage(emitterEntry.getValue(), String.valueOf(toIds.get(i)),
                        emitterEntry.getKey(), responseDto);
            }
        }
        return "ok";
    }
}
