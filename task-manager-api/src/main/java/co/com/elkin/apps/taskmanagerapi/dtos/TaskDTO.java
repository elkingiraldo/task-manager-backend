package co.com.elkin.apps.taskmanagerapi.dtos;

import java.io.Serializable;
import java.util.Date;

import co.com.elkin.apps.taskmanagerapi.enums.TaskStatus;

public class TaskDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String description;
	private Date edc;
	private Integer userId;
	private TaskStatus status;

	public Integer getId() {
		return id;
	}

	public void setId(final Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public Date getEdc() {
		return edc;
	}

	public void setEdc(final Date edc) {
		this.edc = edc;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(final Integer userId) {
		this.userId = userId;
	}

	public TaskStatus getStatus() {
		return status;
	}

	public void setStatus(final TaskStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "TaskDTO [id=" + id + ", description=" + description + ", edc=" + edc + ", userId=" + userId
				+ ", status=" + status + "]";
	}

}
