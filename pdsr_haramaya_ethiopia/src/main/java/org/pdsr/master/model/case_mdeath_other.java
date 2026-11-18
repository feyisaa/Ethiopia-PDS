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
public class case_mdeath_other implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Size(min = 1, max = 80)
	private String other_uuid;

	@JsonIgnore
	@MapsId
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_uuid", referencedColumnName = "case_uuid", insertable = true, updatable = true)
	private case_identifiers case_uuid;

	@Column
	private Boolean other_anaesthga;

	@Column
	private Boolean other_epidural;

	@Column
	private Boolean other_spinal;

	@Column
	private Boolean other_local;

	@Column
	private Boolean other_invasive;

	@Column
	private Boolean other_antihyper;

	@Column
	private Boolean other_icuventilation;

	@Lob
	@Column
	private String new_intervention;
	

	@JsonIgnore
	@Lob
	@Column
	private String other_json;

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

	public Boolean getOther_anaesthga() {
		return other_anaesthga;
	}

	public void setOther_anaesthga(Boolean other_anaesthga) {
		this.other_anaesthga = other_anaesthga;
	}

	public Boolean getOther_epidural() {
		return other_epidural;
	}

	public void setOther_epidural(Boolean other_epidural) {
		this.other_epidural = other_epidural;
	}

	public Boolean getOther_spinal() {
		return other_spinal;
	}

	public void setOther_spinal(Boolean other_spinal) {
		this.other_spinal = other_spinal;
	}

	public Boolean getOther_local() {
		return other_local;
	}

	public void setOther_local(Boolean other_local) {
		this.other_local = other_local;
	}

	public Boolean getOther_invasive() {
		return other_invasive;
	}

	public void setOther_invasive(Boolean other_invasive) {
		this.other_invasive = other_invasive;
	}

	public Boolean getOther_antihyper() {
		return other_antihyper;
	}

	public void setOther_antihyper(Boolean other_antihyper) {
		this.other_antihyper = other_antihyper;
	}

	public Boolean getOther_icuventilation() {
		return other_icuventilation;
	}

	public void setOther_icuventilation(Boolean other_icuventilation) {
		this.other_icuventilation = other_icuventilation;
	}

	public String getNew_intervention() {
		return new_intervention;
	}

	public void setNew_intervention(String new_intervention) {
		this.new_intervention = new_intervention;
	}

	public String getOther_uuid() {
		return other_uuid;
	}

	public void setOther_uuid(String other_uuid) {
		this.other_uuid = other_uuid;
	}

	public String getOther_json() {
		return other_json;
	}

	public void setOther_json(String other_json) {
		this.other_json = other_json;
	}

	@Override
	public int hashCode() {
		return Objects.hash(other_uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		case_mdeath_other other = (case_mdeath_other) obj;
		return Objects.equals(other_uuid, other.other_uuid);
	}

	

}
