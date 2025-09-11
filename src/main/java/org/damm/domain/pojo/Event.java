package org.damm.domain.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
	private Integer idEvent = null;
	private String description;
	private Date eventDate;
	private String name;
	private Category category;
}
