package propofol.alarmservice.api.common.exception.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import propofol.alarmservice.api.ResponseDto;
import propofol.alarmservice.api.common.exception.dto.ErrorDetailDto;
import propofol.alarmservice.api.common.exception.dto.ErrorDto;

@RestControllerAdvice
@Slf4j
public class ExceptionAdviceController {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto badRequestType1Error(HttpMessageNotReadableException e){
        ErrorDto errorDto = createError("잘못된 요청입니다.");
        return errorDto;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto validationError(MethodArgumentNotValidException e){
        ErrorDto errorDto = createError("게시글 생성 실패!");
        e.getFieldErrors().forEach(
                error -> {
                    errorDto.getErrors().add(new ErrorDetailDto(error.getField(), error.getDefaultMessage()));
                }
        );
        return new ResponseDto<>(HttpStatus.BAD_REQUEST.value(), "fail", "검증 오류", errorDto);
    }

    private ErrorDto createError(String errorMessage) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setErrorMessage(errorMessage);
        return errorDto;
    }
}
