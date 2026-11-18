package org.pdsr.master.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class sync_table implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@NotNull
	@Size(min = 1, max = 80)
	private String sync_id;//REMOTE SYNC ID (LICENSE ID) ALSO THE COUNTRY ID
	
	@Column(unique = true)
	@NotNull
	@Size(min = 1, max = 80)
	private String sync_code;//FACILITY ID
	
	@Column
	@NotNull
	@Size(min = 1, max = 80)	
	private String sync_name;//facility name
	
	@Column
	@Size(min = 1, max = 80)	
	private String sync_email;
		
	@Column
	private String sync_url;
	
	@Lob
	@Column
	private String sync_json;
	
	@Column
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date prev_survey_date;

		
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "case_sync")
    @JsonIgnore
	private List<case_identifiers> cases;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "case_sync")
    @JsonIgnore
	private List<monitoring_tool> mtools;


	public String getSync_id() {
		return sync_id;
	}

	public void setSync_id(String sync_id) {
		this.sync_id = sync_id;
	}

	public String getSync_code() {
		return sync_code;
	}

	public void setSync_code(String sync_code) {
		this.sync_code = sync_code;
	}

	public String getSync_name() {
		return sync_name;
	}

	public void setSync_name(String sync_name) {
		this.sync_name = sync_name;
	}

	public String getSync_email() {
		return sync_email;
	}

	public void setSync_email(String sync_email) {
		this.sync_email = sync_email;
	}

	public String getSync_url() {
		return sync_url;
	}

	public void setSync_url(String sync_url) {
		this.sync_url = sync_url;
	}

	public String getSync_json() {
		return sync_json;
	}

	public void setSync_json(String sync_json) {
		this.sync_json = sync_json;
	}

	public List<case_identifiers> getCases() {
		return cases;
	}

	public void setCases(List<case_identifiers> cases) {
		this.cases = cases;
	}

	public Date getPrev_survey_date() {
		return prev_survey_date;
	}

	public void setPrev_survey_date(Date prev_survey_date) {
		this.prev_survey_date = prev_survey_date;
	}

	public List<monitoring_tool> getMtools() {
		return mtools;
	}

	public void setMtools(List<monitoring_tool> mtools) {
		this.mtools = mtools;
	}

}
