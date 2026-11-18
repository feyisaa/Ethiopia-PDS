package org.pdsr.master.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class case_mdeath_ante implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Size(min = 1, max = 80)
	private String ante_uuid;

	@JsonIgnore
	@MapsId
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_uuid", referencedColumnName = "case_uuid", insertable = true, updatable = true)
	private case_identifiers case_uuid;

	@Column
	private Boolean ante_transfusion;

	@Column
	private Boolean ante_antibiotic;

	@Column
	private Boolean ante_externalv;

	@Column
	private Boolean ante_magsulphate;

	@Column
	private Boolean ante_diazepam;

	@Column
	private Boolean ante_antihyper;

	@Column
	private Boolean ante_hysterotomy;

	@Lob
	@Column
	private String ante_other;


	@JsonIgnore
	@Lob
	@Column
	private String ante_json;

	@Column
	private Integer data_complete;

	public Integer getData_complete() {
		return data_complete;
	}

	public void setData_complete(Integer data_complete) {
		this.data_complete = data_complete;
	}

	public String getAnte_uuid() {
		return ante_uuid;
	}

	public void setAnte_uuid(String ante_uuid) {
		this.ante_uuid = ante_uuid;
	}

	public case_identifiers getCase_uuid() {
		return case_uuid;
	}

	public void setCase_uuid(case_identifiers case_uuid) {
		this.case_uuid = case_uuid;
	}

	public Boolean getAnte_transfusion() {
		return ante_transfusion;
	}

	public void setAnte_transfusion(Boolean ante_transfusion) {
		this.ante_transfusion = ante_transfusion;
	}

	public Boolean getAnte_antibiotic() {
		return ante_antibiotic;
	}

	public void setAnte_antibiotic(Boolean ante_antibiotic) {
		this.ante_antibiotic = ante_antibiotic;
	}

	public Boolean getAnte_externalv() {
		return ante_externalv;
	}

	public void setAnte_externalv(Boolean ante_externalv) {
		this.ante_externalv = ante_externalv;
	}

	public Boolean getAnte_magsulphate() {
		return ante_magsulphate;
	}

	public void setAnte_magsulphate(Boolean ante_magsulphate) {
		this.ante_magsulphate = ante_magsulphate;
	}

	public Boolean getAnte_diazepam() {
		return ante_diazepam;
	}

	public void setAnte_diazepam(Boolean ante_diazepam) {
		this.ante_diazepam = ante_diazepam;
	}

	public Boolean getAnte_antihyper() {
		return ante_antihyper;
	}

	public void setAnte_antihyper(Boolean ante_antihyper) {
		this.ante_antihyper = ante_antihyper;
	}

	public Boolean getAnte_hysterotomy() {
		return ante_hysterotomy;
	}

	public void setAnte_hysterotomy(Boolean ante_hysterotomy) {
		this.ante_hysterotomy = ante_hysterotomy;
	}

	public String getAnte_other() {
		return ante_other;
	}

	public void setAnte_other(String ante_other) {
		this.ante_other = ante_other;
	}

	public String getAnte_json() {
		return ante_json;
	}

	public void setAnte_json(String ante_json) {
		this.ante_json = ante_json;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ante_uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		case_mdeath_ante other = (case_mdeath_ante) obj;
		return Objects.equals(ante_uuid, other.ante_uuid);
	}


}
