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
public class case_mdeath_postpart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Size(min = 1, max = 80)
	private String postpart_uuid;

	@JsonIgnore
	@MapsId
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "case_uuid", referencedColumnName = "case_uuid", insertable = true, updatable = true)
	private case_identifiers case_uuid;

	@Column
	private Boolean postpart_evacuation;

	@Column
	private Boolean postpart_antibiotic;

	@Column
	private Boolean postpart_laparotomy;

	@Column
	private Boolean postpart_hysterectomy;

	@Column
	private Boolean postpart_transfusion;

	@Column
	private Boolean postpart_magsulphate;

	@Column
	private Boolean postpart_placentaremoval;

	@Column
	private Boolean postpart_tacid;

	@Column
	private Boolean postpart_oxytocin;

	@Column
	private Boolean postpart_antihyper;

	@Column
	private Boolean postpart_diazepam;

	@Lob
	@Column
	private String postpart_other;

	@JsonIgnore
	@Lob
	@Column
	private String postpart_json;

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

	public String getPostpart_uuid() {
		return postpart_uuid;
	}

	public void setPostpart_uuid(String postpart_uuid) {
		this.postpart_uuid = postpart_uuid;
	}

	public Boolean getPostpart_evacuation() {
		return postpart_evacuation;
	}

	public void setPostpart_evacuation(Boolean postpart_evacuation) {
		this.postpart_evacuation = postpart_evacuation;
	}

	public Boolean getPostpart_antibiotic() {
		return postpart_antibiotic;
	}

	public void setPostpart_antibiotic(Boolean postpart_antibiotic) {
		this.postpart_antibiotic = postpart_antibiotic;
	}

	public Boolean getPostpart_laparotomy() {
		return postpart_laparotomy;
	}

	public void setPostpart_laparotomy(Boolean postpart_laparotomy) {
		this.postpart_laparotomy = postpart_laparotomy;
	}

	public Boolean getPostpart_hysterectomy() {
		return postpart_hysterectomy;
	}

	public void setPostpart_hysterectomy(Boolean postpart_hysterectomy) {
		this.postpart_hysterectomy = postpart_hysterectomy;
	}

	public Boolean getPostpart_transfusion() {
		return postpart_transfusion;
	}

	public void setPostpart_transfusion(Boolean postpart_transfusion) {
		this.postpart_transfusion = postpart_transfusion;
	}

	public Boolean getPostpart_magsulphate() {
		return postpart_magsulphate;
	}

	public void setPostpart_magsulphate(Boolean postpart_magsulphate) {
		this.postpart_magsulphate = postpart_magsulphate;
	}

	public Boolean getPostpart_placentaremoval() {
		return postpart_placentaremoval;
	}

	public void setPostpart_placentaremoval(Boolean postpart_placentaremoval) {
		this.postpart_placentaremoval = postpart_placentaremoval;
	}

	public Boolean getPostpart_tacid() {
		return postpart_tacid;
	}

	public void setPostpart_tacid(Boolean postpart_tacid) {
		this.postpart_tacid = postpart_tacid;
	}

	public Boolean getPostpart_oxytocin() {
		return postpart_oxytocin;
	}

	public void setPostpart_oxytocin(Boolean postpart_oxytocin) {
		this.postpart_oxytocin = postpart_oxytocin;
	}

	public Boolean getPostpart_antihyper() {
		return postpart_antihyper;
	}

	public void setPostpart_antihyper(Boolean postpart_antihyper) {
		this.postpart_antihyper = postpart_antihyper;
	}

	public Boolean getPostpart_diazepam() {
		return postpart_diazepam;
	}

	public void setPostpart_diazepam(Boolean postpart_diazepam) {
		this.postpart_diazepam = postpart_diazepam;
	}

	public String getPostpart_other() {
		return postpart_other;
	}

	public void setPostpart_other(String postpart_other) {
		this.postpart_other = postpart_other;
	}

	public String getPostpart_json() {
		return postpart_json;
	}

	public void setPostpart_json(String postpart_json) {
		this.postpart_json = postpart_json;
	}

	@Override
	public int hashCode() {
		return Objects.hash(postpart_uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		case_mdeath_postpart other = (case_mdeath_postpart) obj;
		return Objects.equals(postpart_uuid, other.postpart_uuid);
	}

}
