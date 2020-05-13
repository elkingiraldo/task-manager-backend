package co.com.elkin.apps.taskmanagerapi.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import co.com.elkin.apps.taskmanagerapi.enums.TaskStatus;

@Entity
@Table(name = "TASK_TBL")
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String description;
	private Date edc;
	private Integer userId;

	@Enumerated(EnumType.STRING)
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
		return "Task [id=" + id + ", description=" + description + ", edc=" + edc + ", userId=" + userId + ", status="
				+ status + "]";
	}

}
