package lt.butkus.model;

import java.util.List;

import javax.persistence.Id;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Meeting {
	@Id
	@ApiModelProperty(hidden = true)
	private String id;
	private String name;
	@ApiModelProperty(hidden = true)
	private String responsiblePerson;
	private String description;
	private EnumCategory category;
	private EnumType type;
	@ApiModelProperty(value = "yyyy-MM-dd HH:mm:ss", example = "2022-10-03 22:05:06")
	private String startDate;
	@ApiModelProperty(value = "yyyy-MM-dd HH:mm:ss", example = "2022-10-03 22:05:06")
	private String endDate;
	@ApiModelProperty(hidden = true)
	private List<Attendee> attendee;
}
