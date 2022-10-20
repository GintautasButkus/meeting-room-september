package lt.butkus.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lt.butkus.exceptions.AttendeeAlreadyParticipateThisMeetingException;
import lt.butkus.exceptions.AttendeeMeetingOverlapsException;
import lt.butkus.model.Attendee;
import lt.butkus.model.EnumCategory;
import lt.butkus.model.EnumType;
import lt.butkus.model.Meeting;

class MeetingServiceTest {

	private List<Attendee> attendees = new ArrayList<Attendee>();
	
	
	
	MeetingService underTest;
	Attendee testAttendee = new Attendee("owner", "2022-10-02 22:22:22");
	Attendee testAttendee2 = new Attendee("invited", "2022-10-02 22:22:22");

	Meeting meeting1 = new Meeting("1", "Meeting name", "ginta", "Funky meeting for the team", EnumCategory.CODE_MONKEY,
			EnumType.IN_PERSON, "2050-10-03T22:05:06", "2050-10-03T23:05:06", attendees);

	Meeting meeting2 = new Meeting("2", "Meeting name2", "sunny", "Sunny Meet", EnumCategory.HUB, EnumType.LIVE,
			"2050-11-03T22:05:06", "2050-11-03T23:05:06", attendees);

	Meeting meeting3 = new Meeting("3", "Meeting name3", "moony", "Moony Meet", EnumCategory.SHORT, EnumType.LIVE,
			"2050-12-03T22:05:06", "2050-12-03T23:05:06", attendees);

	Meeting meeting4 = new Meeting("4", "Meeting name4", "daily", "Daily Meet", EnumCategory.SHORT, EnumType.LIVE,
			"2050-10-03T22:05:06", "2050-10-03T23:05:06", attendees);
	

	@BeforeEach
	void setUp() throws IOException {
		attendees.add(testAttendee);
		underTest = new MeetingService();
		underTest.saveMeeting(meeting1);
		underTest.saveMeeting(meeting2);
		underTest.saveMeeting(meeting3);
	}

	@Test
	void itShouldSaveMeeting() throws IOException {
		underTest.saveMeeting(meeting4);
		assertEquals("Adding 1 more meeting.", 4, underTest.getMeetings().length);
	}

	@Test
	void itShouldGetAllTheMeetings() throws IOException {
		assertEquals("Getting all the meetings.", 3, underTest.getMeetings().length);
	}

	@Test
	void itShouldRemoveOneMeeting() throws IOException {
		underTest.deleteMeeting("1");
		assertEquals("Getting all the meetings when one deleted.", 2, underTest.getMeetings().length);
	}

	@Test
	void itShouldAddAttendeeToTheMeeting() throws IOException {
		underTest.addAttendee(testAttendee2, "1");
		assertEquals("Getting all the attendees of the meeting.", 2, Arrays.stream(underTest.getMeetings())
				.filter(meeting -> meeting.getId().equals("1")).findAny().get().getAttendee().size());
		
		assertThrows("Attendee already invited to the meeting.", AttendeeAlreadyParticipateThisMeetingException.class, () -> {underTest.addAttendee(testAttendee2, "1");});
		
		underTest.saveMeeting(meeting4);
		assertThrows("Meeting time overlaps with another attendee's meeting time.", AttendeeMeetingOverlapsException.class, () -> {underTest.addAttendee(testAttendee2, "4");});
	}

	@AfterEach
	void destroy() throws IOException {
		underTest.deleteMeetings();
	}

}
