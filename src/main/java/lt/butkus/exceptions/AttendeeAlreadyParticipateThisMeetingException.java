package lt.butkus.exceptions;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class AttendeeAlreadyParticipateThisMeetingException extends NoSuchElementException {
		private static final long serialVersionUID = 1L;
		
		public AttendeeAlreadyParticipateThisMeetingException(String message) {
			super(message);
		}
	

}
