package lt.butkus.services;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import lt.butkus.exceptions.AttendeeAlreadyParticipateThisMeetingException;
import lt.butkus.exceptions.ResponsiblePersonCannotBeRemovedException;
import lt.butkus.model.Attendee;
import lt.butkus.model.EnumCategory;
import lt.butkus.model.EnumType;
import lt.butkus.model.Meeting;

@Service
public class MeetingService {

	Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public void saveMeeting(Meeting meeting) throws IOException {

		Type dtoListType = new TypeToken<List<Meeting>>() {
		}.getType();
		FileReader fr = new FileReader("C:\\Users\\ginta\\Downloads\\meetup.json");

		List<Meeting> dtos = gson.fromJson(fr, dtoListType);
		fr.close();
		if (null == dtos) {
			dtos = new ArrayList<>();
		}
		dtos.add(meeting);
		FileWriter fw = new FileWriter("C:\\Users\\ginta\\Downloads\\meetup.json");
		gson.toJson(dtos, fw);
		fw.close();
	}

	public Meeting[] getMeetings() {
		Gson gson1 = new Gson();
		try (Reader reader = new FileReader("C:\\Users\\ginta\\Downloads\\meetup.json")) {
			return gson1.fromJson(reader, Meeting[].class);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Meeting[0];
	}

	public void deleteMeeting(String id) throws IOException {
		List<Meeting> meetingsList = new ArrayList<>();
		Meeting[] meetings = getMeetings();

		for (int i = 0; i < meetings.length; i++) {
			if (!meetings[i].getId().equals(id)
					|| !meetings[i].getResponsiblePerson().equals(System.getProperty("user.name"))) {
				meetingsList.add(meetings[i]);
			}

			File file = new File("C:\\Users\\ginta\\Downloads\\meetup.json");
			file.delete();
			PrintWriter pw = new PrintWriter(new FileWriter("C:\\Users\\ginta\\Downloads\\meetup.json"));

			for (Meeting meeting : meetingsList) {
				saveMeeting(meeting);
			}

		}
	}

	public void addAttendee(Attendee attendee, String meetingId) throws IOException {
		List<Meeting> meetingsList = new ArrayList<>();
		Meeting[] meetings = getMeetings();

		for (Meeting meeting : meetings) {
			if (meeting.getId().equals(meetingId)
					&& meeting.getAttendee().stream().noneMatch(a -> a.getName().equals(attendee.getName()))) {
				meeting.getAttendee().add(attendee);
				meetingsList.add(meeting);

			} else if (meeting.getId().equals(meetingId)
					&& meeting.getAttendee().stream().anyMatch(a -> a.getName().equals(attendee.getName()))) {
				throw new AttendeeAlreadyParticipateThisMeetingException(
						"Attendee already participates in the meeting.");
			} else {
				meetingsList.add(meeting);
			}
		}
		File file = new File("C:\\Users\\ginta\\Downloads\\meetup.json");
		file.delete();
		PrintWriter pw = new PrintWriter(new FileWriter("C:\\Users\\ginta\\Downloads\\meetup.json"));

		for (Meeting meeting : meetingsList) {
			saveMeeting(meeting);
		}
	}

	public void deleteAttendee(String name, String meetingId) throws IOException {
		List<Meeting> meetingsList = new ArrayList<>();
		Meeting[] meetings = getMeetings();

		for (Meeting meeting : meetings) {
			if (meeting.getId().equals(meetingId) && meeting.getResponsiblePerson().equals(name)) {
				throw new ResponsiblePersonCannotBeRemovedException(
						"Responsible person cannot be removed from the meeting.");
			} else if (meeting.getId().equals(meetingId) && !meeting.getResponsiblePerson().equals(name)
					&& meeting.getAttendee().stream().anyMatch(a -> a.getName().equals(name))) {
				meeting.getAttendee().removeIf(a -> a.getName().equals(name));
				meetingsList.add(meeting);
			} else {
				meetingsList.add(meeting);
			}
		}

		File file = new File("C:\\Users\\ginta\\Downloads\\meetup.json");
		file.delete();
		PrintWriter pw = new PrintWriter(new FileWriter("C:\\Users\\ginta\\Downloads\\meetup.json"));

		for (Meeting meeting : meetingsList) {
			saveMeeting(meeting);
		}
	}
	
	public List<Meeting> filterMeeting(String description, String responsiblePerson, EnumCategory category, EnumType type, String startDate, String endDate, Integer numberAttendees){
		Stream<Meeting> stream = Arrays.stream(getMeetings());
		
		return stream
			.filter(meeting -> description == null || meeting.getDescription().toLowerCase().contains(description.toLowerCase()))
			.filter(meeting -> responsiblePerson == null || meeting.getResponsiblePerson().equals(responsiblePerson))
			.filter(meeting -> category == null || meeting.getCategory().equals(category))
			.filter(meeting -> type == null || meeting.getType().equals(type))
			.filter(meeting -> startDate == null || LocalDateTime.parse(meeting.getStartDate()).isAfter(LocalDateTime.parse(startDate)) || LocalDateTime.parse(meeting.getStartDate()).isEqual(LocalDateTime.parse(startDate)))
			.filter(meeting -> endDate == null || LocalDateTime.parse(meeting.getEndDate()).isBefore(LocalDateTime.parse(endDate)))
			.filter(meeting -> numberAttendees == null || meeting.getAttendee().size() >= numberAttendees)
		.collect(Collectors.toList());
	}
}
