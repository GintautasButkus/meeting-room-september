package lt.butkus.exceptions;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class ResponsiblePersonCannotBeRemovedException extends NoSuchElementException {
		private static final long serialVersionUID = 1L;
		
		public ResponsiblePersonCannotBeRemovedException(String message) {
			super(message);
		}
	

}
