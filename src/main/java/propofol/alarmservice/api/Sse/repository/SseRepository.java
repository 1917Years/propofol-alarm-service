package propofol.alarmservice.api.Sse.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class SseRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCaches = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId, SseEmitter sseEmitter){
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    public void saveEventCache(String eventCacheId, Object event){
        eventCaches.put(eventCacheId, event);
    }

    public Map<String, SseEmitter> findAllEmitterStartWithByMemberId(String memberId){
        return emitters.entrySet().stream().filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, Object> findAllEventCacheStartWithByMemberId(String memberId){
        return eventCaches.entrySet().stream().filter(entry -> entry.getKey().startsWith(memberId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deleteById(String id){
        emitters.remove(id);
    }

    public void deleteAllEmitterStartWithByMemberId(String memberId){
        emitters.forEach((key, emitter) -> {
            if(key.startsWith(memberId)) emitters.remove(key);
        });
    }

    public void deleteAllCacheStartWithByMemberId(String memberId){
        eventCaches.forEach((key, object) -> {
            if(key.startsWith(memberId)) eventCaches.remove(key);
        });
    }
}
