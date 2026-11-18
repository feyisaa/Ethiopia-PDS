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
public class case_mdeath_intra implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Size(min = 1, max = 80)
	private String intra_uuid;

	@JsonIgnore
	@MapsId
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_uuid", referencedColumnName = "case_uuid", insertable = true, updatable = true)
	private case_identifiers case_uuid;

	@Column
	private Boolean intra_instrumental;

	@Column
	private Boolean intra_antibiotic;

	@Column
	private Boolean intra_caesarian;

	@Column
	private Boolean intra_hysterectomy;

	@Column
	private Boolean intra_transfusion;

	@Column
	private Boolean intra_magsulphate;

	@Column
	private Boolean intra_antihyper;

	@Column
	private Boolean intra_diazepam;

	@Lob
	@Column
	private String intra_other;
	
	@JsonIgnore
	@Lob
	@Column
	private String intra_json;

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

	public Boolean getIntra_instrumental() {
		return intra_instrumental;
	}

	public void setIntra_instrumental(Boolean intra_instrumental) {
		this.intra_instrumental = intra_instrumental;
	}

	public Boolean getIntra_antibiotic() {
		return intra_antibiotic;
	}

	public void setIntra_antibiotic(Boolean intra_antibiotic) {
		this.intra_antibiotic = intra_antibiotic;
	}

	public Boolean getIntra_caesarian() {
		return intra_caesarian;
	}

	public void setIntra_caesarian(Boolean intra_caesarian) {
		this.intra_caesarian = intra_caesarian;
	}

	public Boolean getIntra_hysterectomy() {
		return intra_hysterectomy;
	}

	public void setIntra_hysterectomy(Boolean intra_hysterectomy) {
		this.intra_hysterectomy = intra_hysterectomy;
	}

	public Boolean getIntra_transfusion() {
		return intra_transfusion;
	}

	public void setIntra_transfusion(Boolean intra_transfusion) {
		this.intra_transfusion = intra_transfusion;
	}

	public Boolean getIntra_magsulphate() {
		return intra_magsulphate;
	}

	public void setIntra_magsulphate(Boolean intra_magsulphate) {
		this.intra_magsulphate = intra_magsulphate;
	}

	public Boolean getIntra_antihyper() {
		return intra_antihyper;
	}

	public void setIntra_antihyper(Boolean intra_antihyper) {
		this.intra_antihyper = intra_antihyper;
	}

	public Boolean getIntra_diazepam() {
		return intra_diazepam;
	}

	public void setIntra_diazepam(Boolean intra_diazepam) {
		this.intra_diazepam = intra_diazepam;
	}

	public String getIntra_other() {
		return intra_other;
	}

	public void setIntra_other(String intra_other) {
		this.intra_other = intra_other;
	}

	public String getIntra_uuid() {
		return intra_uuid;
	}

	public void setIntra_uuid(String intra_uuid) {
		this.intra_uuid = intra_uuid;
	}

	public String getIntra_json() {
		return intra_json;
	}

	public void setIntra_json(String intra_json) {
		this.intra_json = intra_json;
	}


}
