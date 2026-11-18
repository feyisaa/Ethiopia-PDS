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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class case_mdeath implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Size(min = 1, max = 80)
	private String mdeath_uuid;

	@JsonIgnore
	@MapsId
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_uuid", referencedColumnName = "case_uuid", insertable = true, updatable = true)
	private case_identifiers case_uuid;

	@Column
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date mdeath_date;

	@Column
	private Integer mdeath_hour;

	@Column
	private Integer mdeath_minute;

	@Column
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	private Date mdeath_time;

	@Column
	private Integer mdeath_datetime_notstated;

	@Column
	private Integer mdeath_autopsy;// yes no

	@Column
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date mdeath_autopsy_date;

	@Column
	private String mdeath_autopsy_location;

	@Column
	private Integer mdeath_autopsy_by;

	@Lob
	@Column
	private String mdeath_autopsy_final_cod;

	@Lob
	@Column
	private String mdeath_autopsy_antec_cod;

	@Lob
	@Column
	private String mdeath_autopsy_ops_cod;

	@Column
	private Integer mdeath_autopsy_icd_mm;
	

	@JsonIgnore
	@Lob
	@Column
	private String mdeath_json;

	@Column
	private Integer data_complete;

	public Integer getData_complete() {
		return data_complete;
	}

	public void setData_complete(Integer data_complete) {
		this.data_complete = data_complete;
	}

	public String getMdeath_uuid() {
		return mdeath_uuid;
	}

	public void setMdeath_uuid(String mdeath_uuid) {
		this.mdeath_uuid = mdeath_uuid;
	}

	public case_identifiers getCase_uuid() {
		return case_uuid;
	}

	public void setCase_uuid(case_identifiers case_uuid) {
		this.case_uuid = case_uuid;
	}

	public Date getMdeath_date() {
		return mdeath_date;
	}

	public void setMdeath_date(Date mdeath_date) {
		this.mdeath_date = mdeath_date;
	}

	public Integer getMdeath_hour() {
		return mdeath_hour;
	}

	public void setMdeath_hour(Integer mdeath_hour) {
		this.mdeath_hour = mdeath_hour;
	}

	public Integer getMdeath_minute() {
		return mdeath_minute;
	}

	public void setMdeath_minute(Integer mdeath_minute) {
		this.mdeath_minute = mdeath_minute;
	}

	public Date getMdeath_time() {
		return mdeath_time;
	}

	public void setMdeath_time(Date mdeath_time) {
		this.mdeath_time = mdeath_time;
	}

	public Integer getMdeath_datetime_notstated() {
		return mdeath_datetime_notstated;
	}

	public void setMdeath_datetime_notstated(Integer mdeath_datetime_notstated) {
		this.mdeath_datetime_notstated = mdeath_datetime_notstated;
	}


	public Integer getMdeath_autopsy() {
		return mdeath_autopsy;
	}

	public void setMdeath_autopsy(Integer mdeath_autopsy) {
		this.mdeath_autopsy = mdeath_autopsy;
	}

	public Date getMdeath_autopsy_date() {
		return mdeath_autopsy_date;
	}

	public void setMdeath_autopsy_date(Date mdeath_autopsy_date) {
		this.mdeath_autopsy_date = mdeath_autopsy_date;
	}

	public String getMdeath_autopsy_location() {
		return mdeath_autopsy_location;
	}

	public void setMdeath_autopsy_location(String mdeath_autopsy_location) {
		this.mdeath_autopsy_location = mdeath_autopsy_location;
	}

	public Integer getMdeath_autopsy_by() {
		return mdeath_autopsy_by;
	}

	public void setMdeath_autopsy_by(Integer mdeath_autopsy_by) {
		this.mdeath_autopsy_by = mdeath_autopsy_by;
	}

	public String getMdeath_autopsy_final_cod() {
		return mdeath_autopsy_final_cod;
	}

	public void setMdeath_autopsy_final_cod(String mdeath_autopsy_final_cod) {
		this.mdeath_autopsy_final_cod = mdeath_autopsy_final_cod;
	}

	public String getMdeath_autopsy_antec_cod() {
		return mdeath_autopsy_antec_cod;
	}

	public void setMdeath_autopsy_antec_cod(String mdeath_autopsy_antec_cod) {
		this.mdeath_autopsy_antec_cod = mdeath_autopsy_antec_cod;
	}

	public String getMdeath_autopsy_ops_cod() {
		return mdeath_autopsy_ops_cod;
	}

	public void setMdeath_autopsy_ops_cod(String mdeath_autopsy_ops_cod) {
		this.mdeath_autopsy_ops_cod = mdeath_autopsy_ops_cod;
	}

	public Integer getMdeath_autopsy_icd_mm() {
		return mdeath_autopsy_icd_mm;
	}

	public void setMdeath_autopsy_icd_mm(Integer mdeath_autopsy_icd_mm) {
		this.mdeath_autopsy_icd_mm = mdeath_autopsy_icd_mm;
	}

	public String getMdeath_json() {
		return mdeath_json;
	}

	public void setMdeath_json(String mdeath_json) {
		this.mdeath_json = mdeath_json;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mdeath_uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		case_mdeath other = (case_mdeath) obj;
		return Objects.equals(mdeath_uuid, other.mdeath_uuid);
	}

}
