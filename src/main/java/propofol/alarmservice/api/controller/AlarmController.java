package propofol.alarmservice.api.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import propofol.alarmservice.api.ResponseDto;
import propofol.alarmservice.api.common.annotation.Token;
import propofol.alarmservice.api.controller.dto.*;
import propofol.alarmservice.api.service.AlarmApiService;
import propofol.alarmservice.domain.entity.Alarm;
import propofol.alarmservice.domain.entity.AlarmStatus;
import propofol.alarmservice.domain.exception.NotFoundAlarmException;
import propofol.alarmservice.domain.service.AlarmService;

import static propofol.alarmservice.domain.entity.AlarmStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/alarms")
public class AlarmController {

    private final AlarmService alarmService;
    private final AlarmApiService alarmApiService;
    private final ModelMapper modelMapper;

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto notFoundAlarmException(NotFoundAlarmException e){
        return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "fail", "알림 조회 실패", e.getMessage());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto getAlarms(@RequestParam("page") int page,
                                     @Token Long memberId){
        return createPageResponseWithAlarmResponseDto(page, memberId, null);
    }

    @GetMapping("/unread")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto getUnreadAlarms(@RequestParam("page") int page,
                                           @Token Long memberId){
        return createPageResponseWithAlarmResponseDto(page, memberId, UNREAD);
    }

    @GetMapping("/read")
    @ResponseStatus(HttpStatus.OK)
    public PageResponseDto getReadAlarms(@RequestParam("page") int page,
                                     @Token Long memberId){
        return createPageResponseWithAlarmResponseDto(page, memberId, READ);
    }

//    @GetMapping("/{alarmId}")
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseDto getAlarmDetail(@PathVariable("alarmId") Long alarmId,
//                                      @Token Long memberId){
//        return new ResponseDto(HttpStatus.OK.value(), "success", "알림 조회 성공",
//                createAlarmDetailResponseDto(alarmId));
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto saveAlarm(@Validated @RequestBody AlarmSaveRequestDto requestDto){
        return new ResponseDto(HttpStatus.CREATED.value(), "success", "알림 저장 성공",
                alarmApiService.saveWithAlarm(requestDto.getToId(), requestDto.getMessage(),
                        requestDto.getType(), requestDto.getBoardId()));
    }

    @PostMapping("/list")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto saveAlarm(@Validated @RequestBody AlarmSaveListRequestDto requestDto){
        return new ResponseDto(HttpStatus.CREATED.value(), "success", "알림 저장 성공",
                alarmApiService.saveListWithAlarm(requestDto.getToIds(), requestDto.getMessage(),
                        requestDto.getType(), requestDto.getBoardId()));
    }

//    @PostMapping("/listSave")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseDto saveAlarmList(@Validated @RequestBody AlarmSaveListRequestDto requestDto){
//        return new ResponseDto(HttpStatus.CREATED.value(), "success", "알림 저장 성공",
//                alarmService.saveList(requestDto.getToIds(), requestDto.getMessages(), requestDto.getTypes()));
//    }

    @DeleteMapping("/{alarmId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto deleteAlarm(@PathVariable("alarmId") Long alarmId,
                                   @RequestParam("page") int page,
                                   @Token Long memberId){
        alarmService.deleteAlarm(alarmId);
        return new ResponseDto(HttpStatus.OK.value(), "success", "알림 삭제 성공",
                createPageResponseWithAlarmResponseDto(page, memberId, null));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto deleteAllAlarm(@Token Long memberId){
        return new ResponseDto(HttpStatus.OK.value(), "success", "알림 삭제 성공",
                alarmService.deleteAllAlarm(memberId));
    }


    private PageResponseDto createPageResponseWithAlarmResponseDto(int page, Long memberId, AlarmStatus status) {
        PageResponseDto<AlarmResponseDto> responseDto = new PageResponseDto<>();

        Page<Alarm> alarmPage;

        if(status == null) {
            alarmPage = alarmService.getAllAlarmByMemberId(memberId, page);
        }
        else alarmPage = alarmService.getAllAlarmByMemberIdAndStatus(memberId, page, status);

        responseDto.setTotalCount(alarmPage.getTotalElements());
        responseDto.setTotalPageCount(alarmPage.getTotalPages());

        if(alarmPage.getTotalElements() > 0) {
            alarmPage.getContent().forEach(alarm -> {
                AlarmResponseDto alarmResponseDto = modelMapper.map(alarm, AlarmResponseDto.class);
                alarmResponseDto.setCreatedDateTime(alarm.getCreatedDate().toString());
                responseDto.getData().add(alarmResponseDto);
            });
        }
        return responseDto;
    }

//    private AlarmDetailResponseDto createAlarmDetailResponseDto(long alarmId) {
//        Alarm findAlarm = alarmService.getAlarmById(alarmId);
//
//        return modelMapper.map(findAlarm, AlarmDetailResponseDto.class);
//    }
}
