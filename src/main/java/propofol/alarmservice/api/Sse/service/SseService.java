package propofol.alarmservice.api.Sse.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import propofol.alarmservice.api.Sse.repository.SseRepository;
import propofol.alarmservice.api.Sse.service.dto.ConnectResponseDto;
import propofol.alarmservice.api.common.exception.JwtExpiredException;
import propofol.alarmservice.api.common.jwt.JwtProvider;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SseService {

    private final SseRepository sseRepository;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper;

    private static long timeout = 1000 * 60 * 10L;

    public Object subscribe(String token) {
        String memberId = null;
        try{
            memberId = jwtProvider.getMemberId(token);
        }catch (Exception e){
            throw new JwtExpiredException("Please RefreshToken.");
        }
        String emitterId = createId(memberId);
        
        SseEmitter sseEmitter = new SseEmitter(timeout);
        sseEmitter.onCompletion(() -> sseRepository.deleteById(emitterId)); // 네트워크 오류 및 시간 완료 시 실행
        sseEmitter.onTimeout(() -> sseRepository.deleteById(emitterId)); // 시간 초과 시

        // 더미 데이터 보내기, SseEmitter 생성 후 TimeOut 시간까지 하나도 전송하지 않으면 5xx 에러 발생
        sendMessage(sseEmitter, memberId, emitterId, new ConnectResponseDto("연결 성공 : [userId=" + memberId + "]"));

        return sseRepository.save(emitterId, sseEmitter);
    }

    public Map<String, SseEmitter> getEmitter(String memberId){
        Map<String, SseEmitter> findInfo = sseRepository.findAllEmitterStartWithByMemberId(memberId);
        if(findInfo.size() > 0){
            return findInfo;
        }
        return null;
    }

    public void sendMessage(SseEmitter sseEmitter, String memberId, String emitterId, Object data) {
        String eventId = createId(memberId);
        try{
            sseEmitter.send(SseEmitter.event().id(eventId).data(data));
        } catch (Exception e) {
            log.error("Sse Event Error", e);
            sseRepository.deleteById(emitterId);
        }
    }

    private String createId(String memberId) {
        return memberId + "_" + System.currentTimeMillis();
    }
}
