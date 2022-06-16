package propofol.alarmservice.api.Sse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import propofol.alarmservice.api.ResponseDto;
import propofol.alarmservice.api.Sse.service.SseService;
import propofol.alarmservice.api.common.exception.JwtExpiredException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subscribe")
public class SseController {

    private final SseService sseService;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto jwtExpiredException(JwtExpiredException e){
        return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "fail", "api-gateway 오류", e.getMessage());
    }

    /**
     * 구독 기능
     * Last-Event-Id는 구현 특성상 필요 없을 것 같다. 연결이 끊겨 전송 미스 데이터는 재 로그인 시 알림을 결국 다시 조회하기 때문
     */
    @GetMapping(value = "/{token}", produces = "text/event-stream")
    @ResponseStatus(HttpStatus.OK)
    public Object subscribe(@PathVariable("token") String token){
        return sseService.subscribe(token);
    }
}
