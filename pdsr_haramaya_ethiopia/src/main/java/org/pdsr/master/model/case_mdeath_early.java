package org.pdsr.master.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class case_mdeath_early implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Size(min = 1, max = 80)
	private String early_uuid;

	@JsonIgnore
	@MapsId
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_uuid", referencedColumnName = "case_uuid", insertable = true, updatable = true)
	private case_identifiers case_uuid;

	@Column
	private Boolean early_evacuation;

	@Column
	private Boolean early_antibiotic;

	@Column
	private Boolean early_laparotomy;

	@Column
	private Boolean early_hysterectomy;

	@Column
	private Boolean early_transfusion;

	@Column
	private Boolean early_antihyper;

	@Lob
	@Column
	private String early_other;

	@JsonIgnore
	@Lob
	@Column
	private String early_json;

	@Column
	private Integer data_complete;

	public Integer getData_complete() {
		return data_complete;
	}

	public void setData_complete(Integer data_complete) {
		this.data_complete = data_complete;
	}

	public case_identifiers getCase_uuid() {
		return case_uuid;
	}

	public void setCase_uuid(case_identifiers case_uuid) {
		this.case_uuid = case_uuid;
	}

	public String getEarly_uuid() {
		return early_uuid;
	}

	public void setEarly_uuid(String early_uuid) {
		this.early_uuid = early_uuid;
	}

	public Boolean getEarly_evacuation() {
		return early_evacuation;
	}

	public void setEarly_evacuation(Boolean early_evacuation) {
		this.early_evacuation = early_evacuation;
	}

	public Boolean getEarly_antibiotic() {
		return early_antibiotic;
	}

	public void setEarly_antibiotic(Boolean early_antibiotic) {
		this.early_antibiotic = early_antibiotic;
	}

	public Boolean getEarly_laparotomy() {
		return early_laparotomy;
	}

	public void setEarly_laparotomy(Boolean early_laparotomy) {
		this.early_laparotomy = early_laparotomy;
	}

	public Boolean getEarly_hysterectomy() {
		return early_hysterectomy;
	}

	public void setEarly_hysterectomy(Boolean early_hysterectomy) {
		this.early_hysterectomy = early_hysterectomy;
	}

	public Boolean getEarly_transfusion() {
		return early_transfusion;
	}

	public void setEarly_transfusion(Boolean early_transfusion) {
		this.early_transfusion = early_transfusion;
	}

	public Boolean getEarly_antihyper() {
		return early_antihyper;
	}

	public void setEarly_antihyper(Boolean early_antihyper) {
		this.early_antihyper = early_antihyper;
	}

	public String getEarly_other() {
		return early_other;
	}

	public void setEarly_other(String early_other) {
		this.early_other = early_other;
	}

	public String getEarly_json() {
		return early_json;
	}

	public void setEarly_json(String early_json) {
		this.early_json = early_json;
	}

	@Override
	public int hashCode() {
		return Objects.hash(early_uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		case_mdeath_early other = (case_mdeath_early) obj;
		return Objects.equals(early_uuid, other.early_uuid);
	}

}
