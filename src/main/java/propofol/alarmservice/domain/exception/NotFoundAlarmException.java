package propofol.alarmservice.domain.exception;

import java.util.NoSuchElementException;

public class NotFoundAlarmException extends NoSuchElementException {
    public NotFoundAlarmException(String s) {
        super(s);
    }
}
