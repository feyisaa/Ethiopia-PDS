package org.pdsr.master.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class monitoring_tool implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@NotNull
	@Size(min = 1, max = 80)
	private String survey_id;
	
	@ManyToOne
	@JoinColumn(name = "case_sync", referencedColumnName = "sync_id", insertable = true, updatable = true)
	private sync_table case_sync;

	@Column
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date prev_survey_date;

	@NotNull
	@Column
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date survey_date;
	
	@NotNull
	@Size(min = 1, max = 80)
	private String survey_author;

	@Column(columnDefinition = "TEXT")
	private String survey_team;
	
	@Column
	private Integer time_spent_collect;
	
	@Column
	private Integer time_spent_score;
	
	@Column
	private Integer time_spent_validate;
	

	@NotNull
	@Column
	private Integer lead_coordinator;
	
	@Column(columnDefinition = "TEXT")
	private String lead_coordinator_txt;
	
	@NotNull
	@Column
	private Integer lead_supervision;
	
	@Column(columnDefinition = "TEXT")
	private String lead_supervision_txt;
	
	@NotNull
	@Column
	private Integer lead_engagement;
	
	@Column(columnDefinition = "TEXT")
	private String lead_engagement_txt;
	
	@NotNull
	@Column
	private Integer policy_guidlines;
	
	@Column(columnDefinition = "TEXT")
	private String policy_guidlines_txt;
	
	@NotNull
	@Column
	private Integer struct_mat_committee;
	
	@Column(columnDefinition = "TEXT")
	private String struct_mat_committee_txt;
	
	@NotNull
	@Column
	private Integer struct_peri_committee;
	
	@Column(columnDefinition = "TEXT")
	private String struct_peri_committee_txt;
	
	@NotNull
	@Column
	private Integer struct_multi_committee;
	
	@Column(columnDefinition = "TEXT")
	private String struct_multi_committee_txt;
	
	@NotNull
	@Column
	private Integer struct_formal_committee;
	
	@Column(columnDefinition = "TEXT")
	private String struct_formal_committee_txt;
	
	@NotNull
	@Column
	private Integer meet_exists;
	
	@Column(columnDefinition = "TEXT")
	private String meet_exists_txt;
	
	@NotNull
	@Column
	private Integer meet_routine;
	
	@Column(columnDefinition = "TEXT")
	private String meet_routine_txt;
	
	@NotNull
	@Column
	private Integer tools_mat_available;
	
	@Column(columnDefinition = "TEXT")
	private String tools_mat_available_txt;
	
	@NotNull
	@Column
	private Integer tools_peri_available;
	
	@Column(columnDefinition = "TEXT")
	private String tools_peri_available_txt;
	
	@NotNull
	@Column
	private Integer tools_mat_used;
	
	@Column(columnDefinition = "TEXT")
	private String tools_mat_used_txt;
	
	@NotNull
	@Column
	private Integer tools_peri_used;
	
	@Column(columnDefinition = "TEXT")
	private String tools_peri_used_txt;
	
	@NotNull
	@Column
	private Integer tools_mat_correctly;
	
	@Column(columnDefinition = "TEXT")
	private String tools_mat_correctly_txt;
	
	@NotNull
	@Column
	private Integer tools_peri_correctly;
	
	@Column(columnDefinition = "TEXT")
	private String tools_peri_correctly_txt;
	
	@NotNull
	@Column
	private Integer train_any_done;
	
	@Column(columnDefinition = "TEXT")
	private String train_any_done_txt;
	
	@NotNull
	@Column
	private Integer train_recent_done;
	
	@Column(columnDefinition = "TEXT")
	private String train_recent_done_txt;
	
	@NotNull
	@Column
	private Integer train_embed_work;
	
	@Column(columnDefinition = "TEXT")
	private String train_embed_work_txt;
	
	@NotNull
	@Column
	private Integer res_processes;
	
	@Column(columnDefinition = "TEXT")
	private String res_processes_txt;
	
	@NotNull
	@Column
	private Integer res_mobilisation;
	
	@Column(columnDefinition = "TEXT")
	private String res_mobilisation_txt;
	
	@NotNull
	@Column
	private Integer res_expenditure;
	
	@Column(columnDefinition = "TEXT")
	private String res_expenditure_txt;
	
	@NotNull
	@Column
	private Integer community_structure;
	
	@Column(columnDefinition = "TEXT")
	private String community_structure_txt;
	
	@NotNull
	@Column
	private Integer community_feedback;
	
	@Column(columnDefinition = "TEXT")
	private String community_feedback_txt;
	
	@NotNull
	@Column
	private Integer impl_reinforcement;
		
	@Column(columnDefinition = "TEXT")
	private String impl_reinforcement_txt;
	
	@NotNull
	@Column
	private Integer impl_confidentiality;
	
	@Column(columnDefinition = "TEXT")
	private String impl_confidentiality_txt;
	
	@NotNull
	@Column
	private Integer impl_participation;
	
	@Column(columnDefinition = "TEXT")
	private String impl_participation_txt;
	
	@NotNull
	@Column
	private Integer track_rec_low;
	
	@Column(columnDefinition = "TEXT")
	private String track_rec_low_txt;
	
	@NotNull
	@Column
	private Integer track_rec_mid;
	
	@Column(columnDefinition = "TEXT")
	private String track_rec_mid_txt;
	
	@NotNull
	@Column
	private Integer track_rec_high;
	
	@Column(columnDefinition = "TEXT")
	private String track_rec_high_txt;

	@Column
	private Integer dhis2_maternal_death_count;
	
	@Column(columnDefinition = "TEXT")
	private String dhis2_maternal_death_count_txt;

	
	@Column
	private Integer dhis2_perinatal_death_count;
	
	@Column(columnDefinition = "TEXT")
	private String dhis2_perinatal_death_count_txt;

	

	@Size(max = 80)
	private String survey_reviewer;
	
	@Column
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date survey_review_date;
	
	@NotNull
	@Column
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date entry_date;
	
	@Column
	private boolean survey_completed;

	public String getSurvey_team() {
		return survey_team;
	}

	public void setSurvey_team(String survey_team) {
		this.survey_team = survey_team;
	}

	public Integer getLead_coordinator() {
		return lead_coordinator;
	}

	public void setLead_coordinator(Integer lead_coordinator) {
		this.lead_coordinator = lead_coordinator;
	}

	public String getLead_coordinator_txt() {
		return lead_coordinator_txt;
	}

	public void setLead_coordinator_txt(String lead_coordinator_txt) {
		this.lead_coordinator_txt = lead_coordinator_txt;
	}

	public Integer getLead_supervision() {
		return lead_supervision;
	}

	public void setLead_supervision(Integer lead_supervision) {
		this.lead_supervision = lead_supervision;
	}

	public Integer getLead_engagement() {
		return lead_engagement;
	}

	public void setLead_engagement(Integer lead_engagement) {
		this.lead_engagement = lead_engagement;
	}

	public Integer getPolicy_guidlines() {
		return policy_guidlines;
	}

	public void setPolicy_guidlines(Integer policy_guidlines) {
		this.policy_guidlines = policy_guidlines;
	}

	public Integer getStruct_mat_committee() {
		return struct_mat_committee;
	}

	public void setStruct_mat_committee(Integer struct_mat_committee) {
		this.struct_mat_committee = struct_mat_committee;
	}

	public Integer getStruct_peri_committee() {
		return struct_peri_committee;
	}

	public void setStruct_peri_committee(Integer struct_peri_committee) {
		this.struct_peri_committee = struct_peri_committee;
	}

	public Integer getStruct_multi_committee() {
		return struct_multi_committee;
	}

	public void setStruct_multi_committee(Integer struct_multi_committee) {
		this.struct_multi_committee = struct_multi_committee;
	}

	public Integer getStruct_formal_committee() {
		return struct_formal_committee;
	}

	public void setStruct_formal_committee(Integer struct_formal_committee) {
		this.struct_formal_committee = struct_formal_committee;
	}

	public Integer getMeet_exists() {
		return meet_exists;
	}

	public void setMeet_exists(Integer meet_exists) {
		this.meet_exists = meet_exists;
	}

	public Integer getMeet_routine() {
		return meet_routine;
	}

	public void setMeet_routine(Integer meet_routine) {
		this.meet_routine = meet_routine;
	}

	public Integer getTools_mat_available() {
		return tools_mat_available;
	}

	public void setTools_mat_available(Integer tools_mat_available) {
		this.tools_mat_available = tools_mat_available;
	}

	public Integer getTools_peri_available() {
		return tools_peri_available;
	}

	public void setTools_peri_available(Integer tools_peri_available) {
		this.tools_peri_available = tools_peri_available;
	}

	public Integer getTools_mat_used() {
		return tools_mat_used;
	}

	public void setTools_mat_used(Integer tools_mat_used) {
		this.tools_mat_used = tools_mat_used;
	}

	public Integer getTools_peri_used() {
		return tools_peri_used;
	}

	public void setTools_peri_used(Integer tools_peri_used) {
		this.tools_peri_used = tools_peri_used;
	}

	public Integer getTools_mat_correctly() {
		return tools_mat_correctly;
	}

	public void setTools_mat_correctly(Integer tools_mat_correctly) {
		this.tools_mat_correctly = tools_mat_correctly;
	}

	public Integer getTools_peri_correctly() {
		return tools_peri_correctly;
	}

	public void setTools_peri_correctly(Integer tools_peri_correctly) {
		this.tools_peri_correctly = tools_peri_correctly;
	}

	public Integer getTrain_any_done() {
		return train_any_done;
	}

	public void setTrain_any_done(Integer train_any_done) {
		this.train_any_done = train_any_done;
	}

	public Integer getTrain_recent_done() {
		return train_recent_done;
	}

	public void setTrain_recent_done(Integer train_recent_done) {
		this.train_recent_done = train_recent_done;
	}

	public Integer getTrain_embed_work() {
		return train_embed_work;
	}

	public void setTrain_embed_work(Integer train_embed_work) {
		this.train_embed_work = train_embed_work;
	}

	public Integer getRes_processes() {
		return res_processes;
	}

	public void setRes_processes(Integer res_processes) {
		this.res_processes = res_processes;
	}

	public Integer getRes_mobilisation() {
		return res_mobilisation;
	}

	public void setRes_mobilisation(Integer res_mobilisation) {
		this.res_mobilisation = res_mobilisation;
	}

	public Integer getRes_expenditure() {
		return res_expenditure;
	}

	public void setRes_expenditure(Integer res_expenditure) {
		this.res_expenditure = res_expenditure;
	}

	public Integer getCommunity_structure() {
		return community_structure;
	}

	public void setCommunity_structure(Integer community_structure) {
		this.community_structure = community_structure;
	}

	public Integer getCommunity_feedback() {
		return community_feedback;
	}

	public void setCommunity_feedback(Integer community_feedback) {
		this.community_feedback = community_feedback;
	}

	public Integer getImpl_reinforcement() {
		return impl_reinforcement;
	}

	public void setImpl_reinforcement(Integer impl_reinforcement) {
		this.impl_reinforcement = impl_reinforcement;
	}

	public Integer getImpl_confidentiality() {
		return impl_confidentiality;
	}

	public void setImpl_confidentiality(Integer impl_confidentiality) {
		this.impl_confidentiality = impl_confidentiality;
	}

	public Integer getImpl_participation() {
		return impl_participation;
	}

	public void setImpl_participation(Integer impl_participation) {
		this.impl_participation = impl_participation;
	}

	public Integer getTrack_rec_low() {
		return track_rec_low;
	}

	public void setTrack_rec_low(Integer track_rec_low) {
		this.track_rec_low = track_rec_low;
	}

	public Integer getTrack_rec_mid() {
		return track_rec_mid;
	}

	public void setTrack_rec_mid(Integer track_rec_mid) {
		this.track_rec_mid = track_rec_mid;
	}

	public Integer getTrack_rec_high() {
		return track_rec_high;
	}

	public void setTrack_rec_high(Integer track_rec_high) {
		this.track_rec_high = track_rec_high;
	}

	public String getLead_supervision_txt() {
		return lead_supervision_txt;
	}

	public void setLead_supervision_txt(String lead_supervision_txt) {
		this.lead_supervision_txt = lead_supervision_txt;
	}

	public String getLead_engagement_txt() {
		return lead_engagement_txt;
	}

	public void setLead_engagement_txt(String lead_engagement_txt) {
		this.lead_engagement_txt = lead_engagement_txt;
	}

	public String getPolicy_guidlines_txt() {
		return policy_guidlines_txt;
	}

	public void setPolicy_guidlines_txt(String policy_guidlines_txt) {
		this.policy_guidlines_txt = policy_guidlines_txt;
	}

	public String getStruct_mat_committee_txt() {
		return struct_mat_committee_txt;
	}

	public void setStruct_mat_committee_txt(String struct_mat_committee_txt) {
		this.struct_mat_committee_txt = struct_mat_committee_txt;
	}

	public String getStruct_peri_committee_txt() {
		return struct_peri_committee_txt;
	}

	public void setStruct_peri_committee_txt(String struct_peri_committee_txt) {
		this.struct_peri_committee_txt = struct_peri_committee_txt;
	}

	public String getStruct_multi_committee_txt() {
		return struct_multi_committee_txt;
	}

	public void setStruct_multi_committee_txt(String struct_multi_committee_txt) {
		this.struct_multi_committee_txt = struct_multi_committee_txt;
	}

	public String getStruct_formal_committee_txt() {
		return struct_formal_committee_txt;
	}

	public void setStruct_formal_committee_txt(String struct_formal_committee_txt) {
		this.struct_formal_committee_txt = struct_formal_committee_txt;
	}

	public String getMeet_exists_txt() {
		return meet_exists_txt;
	}

	public void setMeet_exists_txt(String meet_exists_txt) {
		this.meet_exists_txt = meet_exists_txt;
	}

	public String getMeet_routine_txt() {
		return meet_routine_txt;
	}

	public void setMeet_routine_txt(String meet_routine_txt) {
		this.meet_routine_txt = meet_routine_txt;
	}

	public String getTools_mat_available_txt() {
		return tools_mat_available_txt;
	}

	public void setTools_mat_available_txt(String tools_mat_available_txt) {
		this.tools_mat_available_txt = tools_mat_available_txt;
	}

	public String getTools_peri_available_txt() {
		return tools_peri_available_txt;
	}

	public void setTools_peri_available_txt(String tools_peri_available_txt) {
		this.tools_peri_available_txt = tools_peri_available_txt;
	}

	public String getTools_mat_used_txt() {
		return tools_mat_used_txt;
	}

	public void setTools_mat_used_txt(String tools_mat_used_txt) {
		this.tools_mat_used_txt = tools_mat_used_txt;
	}

	public String getTools_peri_used_txt() {
		return tools_peri_used_txt;
	}

	public void setTools_peri_used_txt(String tools_peri_used_txt) {
		this.tools_peri_used_txt = tools_peri_used_txt;
	}

	public String getTools_mat_correctly_txt() {
		return tools_mat_correctly_txt;
	}

	public void setTools_mat_correctly_txt(String tools_mat_correctly_txt) {
		this.tools_mat_correctly_txt = tools_mat_correctly_txt;
	}

	public String getTools_peri_correctly_txt() {
		return tools_peri_correctly_txt;
	}

	public void setTools_peri_correctly_txt(String tools_peri_correctly_txt) {
		this.tools_peri_correctly_txt = tools_peri_correctly_txt;
	}

	public String getTrain_any_done_txt() {
		return train_any_done_txt;
	}

	public void setTrain_any_done_txt(String train_any_done_txt) {
		this.train_any_done_txt = train_any_done_txt;
	}

	public String getTrain_recent_done_txt() {
		return train_recent_done_txt;
	}

	public void setTrain_recent_done_txt(String train_recent_done_txt) {
		this.train_recent_done_txt = train_recent_done_txt;
	}

	public String getTrain_embed_work_txt() {
		return train_embed_work_txt;
	}

	public void setTrain_embed_work_txt(String train_embed_work_txt) {
		this.train_embed_work_txt = train_embed_work_txt;
	}

	public String getRes_processes_txt() {
		return res_processes_txt;
	}

	public void setRes_processes_txt(String res_processes_txt) {
		this.res_processes_txt = res_processes_txt;
	}

	public String getRes_mobilisation_txt() {
		return res_mobilisation_txt;
	}

	public void setRes_mobilisation_txt(String res_mobilisation_txt) {
		this.res_mobilisation_txt = res_mobilisation_txt;
	}

	public String getRes_expenditure_txt() {
		return res_expenditure_txt;
	}

	public void setRes_expenditure_txt(String res_expenditure_txt) {
		this.res_expenditure_txt = res_expenditure_txt;
	}

	public String getCommunity_structure_txt() {
		return community_structure_txt;
	}

	public void setCommunity_structure_txt(String community_structure_txt) {
		this.community_structure_txt = community_structure_txt;
	}

	public String getCommunity_feedback_txt() {
		return community_feedback_txt;
	}

	public void setCommunity_feedback_txt(String community_feedback_txt) {
		this.community_feedback_txt = community_feedback_txt;
	}

	public String getImpl_reinforcement_txt() {
		return impl_reinforcement_txt;
	}

	public void setImpl_reinforcement_txt(String impl_reinforcement_txt) {
		this.impl_reinforcement_txt = impl_reinforcement_txt;
	}

	public String getImpl_confidentiality_txt() {
		return impl_confidentiality_txt;
	}

	public void setImpl_confidentiality_txt(String impl_confidentiality_txt) {
		this.impl_confidentiality_txt = impl_confidentiality_txt;
	}

	public String getImpl_participation_txt() {
		return impl_participation_txt;
	}

	public void setImpl_participation_txt(String impl_participation_txt) {
		this.impl_participation_txt = impl_participation_txt;
	}

	public String getTrack_rec_low_txt() {
		return track_rec_low_txt;
	}

	public void setTrack_rec_low_txt(String track_rec_low_txt) {
		this.track_rec_low_txt = track_rec_low_txt;
	}

	public String getTrack_rec_mid_txt() {
		return track_rec_mid_txt;
	}

	public void setTrack_rec_mid_txt(String track_rec_mid_txt) {
		this.track_rec_mid_txt = track_rec_mid_txt;
	}

	public String getTrack_rec_high_txt() {
		return track_rec_high_txt;
	}

	public void setTrack_rec_high_txt(String track_rec_high_txt) {
		this.track_rec_high_txt = track_rec_high_txt;
	}

	public String getSurvey_id() {
		return survey_id;
	}

	public void setSurvey_id(String survey_id) {
		this.survey_id = survey_id;
	}

	public Date getPrev_survey_date() {
		return prev_survey_date;
	}

	public void setPrev_survey_date(Date prev_survey_date) {
		this.prev_survey_date = prev_survey_date;
	}

	public Date getSurvey_date() {
		return survey_date;
	}

	public void setSurvey_date(Date survey_date) {
		this.survey_date = survey_date;
	}

	public String getSurvey_author() {
		return survey_author;
	}

	public void setSurvey_author(String survey_author) {
		this.survey_author = survey_author;
	}

	public String getSurvey_reviewer() {
		return survey_reviewer;
	}

	public void setSurvey_reviewer(String survey_reviewer) {
		this.survey_reviewer = survey_reviewer;
	}

	public Date getSurvey_review_date() {
		return survey_review_date;
	}

	public void setSurvey_review_date(Date survey_review_date) {
		this.survey_review_date = survey_review_date;
	}

	public Date getEntry_date() {
		return entry_date;
	}

	public void setEntry_date(Date entry_date) {
		this.entry_date = entry_date;
	}

	public boolean isSurvey_completed() {
		return survey_completed;
	}

	public void setSurvey_completed(boolean survey_completed) {
		this.survey_completed = survey_completed;
	}

	public Integer getDhis2_maternal_death_count() {
		return dhis2_maternal_death_count;
	}

	public void setDhis2_maternal_death_count(Integer dhis2_maternal_death_count) {
		this.dhis2_maternal_death_count = dhis2_maternal_death_count;
	}

	public Integer getDhis2_perinatal_death_count() {
		return dhis2_perinatal_death_count;
	}

	public void setDhis2_perinatal_death_count(Integer dhis2_perinatal_death_count) {
		this.dhis2_perinatal_death_count = dhis2_perinatal_death_count;
	}

	public String getDhis2_maternal_death_count_txt() {
		return dhis2_maternal_death_count_txt;
	}

	public void setDhis2_maternal_death_count_txt(String dhis2_maternal_death_count_txt) {
		this.dhis2_maternal_death_count_txt = dhis2_maternal_death_count_txt;
	}

	public String getDhis2_perinatal_death_count_txt() {
		return dhis2_perinatal_death_count_txt;
	}

	public void setDhis2_perinatal_death_count_txt(String dhis2_perinatal_death_count_txt) {
		this.dhis2_perinatal_death_count_txt = dhis2_perinatal_death_count_txt;
	}

	public Integer getTime_spent_collect() {
		return time_spent_collect;
	}

	public void setTime_spent_collect(Integer time_spent_collect) {
		this.time_spent_collect = time_spent_collect;
	}

	public Integer getTime_spent_score() {
		return time_spent_score;
	}

	public void setTime_spent_score(Integer time_spent_score) {
		this.time_spent_score = time_spent_score;
	}

	public Integer getTime_spent_validate() {
		return time_spent_validate;
	}

	public void setTime_spent_validate(Integer time_spent_validate) {
		this.time_spent_validate = time_spent_validate;
	}

	public sync_table getCase_sync() {
		return case_sync;
	}

	public void setCase_sync(sync_table case_sync) {
		this.case_sync = case_sync;
	}

	@Override
	public int hashCode() {
		return Objects.hash(survey_id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		monitoring_tool other = (monitoring_tool) obj;
		return Objects.equals(survey_id, other.survey_id);
	}

	@Override
	public String toString() {
		return  survey_id;
	}
	
	
}
