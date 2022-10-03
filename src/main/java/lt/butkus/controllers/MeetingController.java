package lt.butkus.controllers;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lt.butkus.exceptions.MeetingCannotStartBeforeEndDateException;
import lt.butkus.model.Attendee;
import lt.butkus.model.EnumCategory;
import lt.butkus.model.EnumType;
import lt.butkus.model.Meeting;
import lt.butkus.services.MeetingService;

//@CrossOrigin(origins = "*", maxAge = 3600)
@Api(value = "", tags = { "Meeting Manager" })
@Tag(name = "Meeting Manager", description = "Meeting Schdule Manager")
@RestController
@RequestMapping("/api")
public class MeetingController {

	@Autowired
	private MeetingService meetingService;

	@ApiOperation(value = "CREATE NEW MEETING.")
	@PostMapping("/meeting")
	@ResponseStatus(HttpStatus.CREATED)
	public Meeting createMeeting(@RequestBody Meeting meetingObj,
			@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
			@RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate)
			throws IOException {
		List<Attendee> attendees = new ArrayList<>();
		Attendee creator = new Attendee(System.getProperty("user.name"), LocalDateTime.now().toString());
		attendees.add(creator);
		
		if(startDate.equals(endDate) || startDate.isAfter(endDate)) {
			throw new MeetingCannotStartBeforeEndDateException("Meeting cannot start after the end date.");
		}
		
		Meeting createdMeeting = new Meeting(UUID.randomUUID().toString(), meetingObj.getName(), System.getProperty("user.name"),
				meetingObj.getDescription(), meetingObj.getCategory(), meetingObj.getType(), startDate.toString(),
				endDate.toString(), attendees);
		
		meetingService.saveMeeting(createdMeeting);
		return createdMeeting;
	} 
	
	@ApiOperation(value = "DELETE MEETING BY ITS ID.")
	@DeleteMapping("/meeting/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteMeeting(@RequestParam String id) throws IOException {
			meetingService.deleteMeeting(id);
	}
	
	@ApiOperation(value = "ADD NEW ATTENDEE.")
	@PostMapping("/attendee/{meetingId}")
	@ResponseStatus(HttpStatus.CREATED)
	public Attendee addAttendee(@RequestBody Attendee attendeeObj, String meetingId)
			throws IOException {
		Attendee newAttendee = new Attendee(attendeeObj.getName(), LocalDateTime.now().toString());
		meetingService.addAttendee(newAttendee, meetingId);
		return newAttendee;
	} 
	
	@ApiOperation(value = "REMOVE ATTENDEE.")
	@DeleteMapping("/attendee/{meetingId}")
	@ResponseStatus(HttpStatus.OK)
	public void removeAttendee(@RequestParam String meetingId, String name) throws IOException {
			meetingService.deleteAttendee(name, meetingId);
	}
	
	@ApiOperation(value = "FILTER THE MEETING.")
	@GetMapping("meeting/filter")
	@ResponseStatus(HttpStatus.FOUND)
	public List<Meeting> filterMeetings(
			@RequestParam(required = false) String description, 
			@RequestParam(required = false) String responsiblePerson, 
			@RequestParam(required = false) EnumCategory category, 
			@RequestParam(required = false) EnumType type, 
			@RequestParam(required = false) String startDate, 
			@RequestParam(required = false) String endDate, 
			@RequestParam(required = false) Integer numberAttendees){
		
		return meetingService.filterMeeting(description, 
				responsiblePerson, 
				category, 
				type, 
				startDate, 
				endDate, 
				numberAttendees);
	}
}
