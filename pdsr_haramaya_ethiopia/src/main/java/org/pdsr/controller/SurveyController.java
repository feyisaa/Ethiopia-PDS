package org.pdsr.controller;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.pdsr.CONSTANTS;
import org.pdsr.master.model.monitoring_tool;
import org.pdsr.master.model.sync_table;
import org.pdsr.master.repo.CaseRepository;
import org.pdsr.master.repo.MonitoringToolRepository;
import org.pdsr.master.repo.SyncTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/survey")
public class SurveyController {

	private final int ARRAY_SIZE = 44;

	@Autowired
	private SyncTableRepository syncRepo;

	@Autowired
	private MonitoringToolRepository toolRepo;

	@Autowired
	private CaseRepository caseRepo;

	@GetMapping("")
	public String guide(Principal principal, Model model, @RequestParam(required = false) String success) {

		if (!syncRepo.findById(CONSTANTS.LICENSE_ID).isPresent()) {
			model.addAttribute("activated", "0");
			return "home";

		}

		sync_table synctable = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		model.addAttribute("myf", synctable.getSync_name());

		model.addAttribute("back", "back");
		model.addAttribute("results", "results");
		model.addAttribute("tool", "tool");

		return "survey/survey-guide";
	}

	@GetMapping("/results")
	public String results(Principal principal, Model model) {

		Optional<sync_table> sync_table = syncRepo.findById(CONSTANTS.LICENSE_ID);

		if (!sync_table.isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		final String[] indicators = new String[ARRAY_SIZE];
		indicators[0] = "";

		indicators[1] = "MPDSR coordinator";
		indicators[2] = "Leadership engagement";
		indicators[3] = "External subnational supervision";

		indicators[4] = "Accessible guidelines";

		indicators[5] = "MPDSR-maternal committee exists";
		indicators[6] = "MPDSR-perinatal committee exists";
		indicators[7] = "Multidisciplinary MPDSR committee";
		indicators[8] = "Formalised committee structure";

		indicators[9] = "Committee meets";
		indicators[10] = "Committee meets routinely";

		indicators[11] = "Maternal tools available";
		indicators[12] = "Maternal tools used";
		indicators[13] = "Maternal tools completed correctly";
		indicators[14] = "Perinatal tools available";
		indicators[15] = "Perinatal tools used";
		indicators[16] = "Perinatal tools completed correctly";

		indicators[17] = "Any MPDSR training";
		indicators[18] = "Committee members trained";
		indicators[19] = "MPDSR embedded as part of work expectations";

		indicators[20] = "Facility supports MPDSR processes";
		indicators[21] = "Resource mobilisation";
		indicators[22] = "Resource expenditure";

		indicators[23] = "Community structure exists";
		indicators[24] = "Community feedback loop for MPDSR";

		indicators[25] = "Purpose reinforcement";
		indicators[26] = "Confidentiality";
		indicators[27] = "Learning environment";

		indicators[28] = "Action tracker used";
		indicators[29] = ">50% recommendations implemented";
		indicators[30] = ">70% recommendations implemented";

		indicators[31] = "Total points (add number of points achieved)";
		indicators[32] = "Total available for assessment (add number of indicators assessed)";
		indicators[33] = "MPDSR implementation score (%)";

		indicators[34] = "Maternal deaths reported to DHIS2";
		indicators[35] = "Institutional maternal deaths notification rate";
		indicators[36] = "Institutional maternal death review rate";

		indicators[37] = "Perinatal deaths reported to DHIS2";
		indicators[38] = "Institutional perinatal deaths notification rate";
		indicators[39] = "Institutional perinatal death review rate";

		indicators[40] = "Survey date";
		indicators[41] = "Entered by";
		indicators[42] = "Validation date";
		indicators[43] = "Validated by";

		model.addAttribute("indicators", indicators);

		SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");
		List<String[]> data = new ArrayList<>();
		for (monitoring_tool elem : toolRepo.findByFacility(sync_table.get())) {
			String[] d = new String[ARRAY_SIZE];
			int score = 0;
			int total = 0;

			d[0] = elem.getSurvey_id();

			d[1] = "" + elem.getLead_coordinator();
			score += elem.getLead_coordinator();
			total++;

			d[2] = "" + elem.getLead_engagement();
			score += elem.getLead_engagement();
			total++;

			d[3] = "" + elem.getLead_supervision();
			score += elem.getLead_supervision();
			total++;

			d[4] = "" + elem.getPolicy_guidlines();
			score += elem.getPolicy_guidlines();
			total++;

			d[5] = "" + elem.getStruct_mat_committee();
			score += elem.getStruct_mat_committee();
			total++;

			d[6] = "" + elem.getStruct_peri_committee();
			score += elem.getStruct_peri_committee();
			total++;

			d[7] = "" + elem.getStruct_multi_committee();
			score += elem.getStruct_multi_committee();
			total++;

			d[8] = "" + elem.getStruct_formal_committee();
			score += elem.getStruct_formal_committee();
			total++;

			d[9] = "" + elem.getMeet_exists();
			score += elem.getMeet_exists();
			total++;

			d[10] = "" + elem.getMeet_routine();
			score += elem.getMeet_routine();
			total++;

			d[11] = "" + elem.getTools_mat_available();
			score += elem.getTools_mat_available();
			total++;

			boolean d12_is_valid = elem.getTools_mat_used() != null && elem.getTools_mat_used() > -1;
			d[12] = "" + (d12_is_valid ? elem.getTools_mat_used() : "n/a");
			if (d12_is_valid) {
				score += elem.getTools_mat_used();
				total++;
			}

			boolean d13_is_valid = elem.getTools_mat_correctly() != null && elem.getTools_mat_correctly() > -1;
			d[13] = "" + (d13_is_valid ? elem.getTools_mat_correctly() : "n/a");
			if (d13_is_valid) {
				score += elem.getTools_mat_correctly();
				total++;
			}

			d[14] = "" + elem.getTools_peri_available();
			score += elem.getTools_peri_available();
			total++;

			boolean d15_is_valid = elem.getTools_peri_used() != null && elem.getTools_peri_used() > -1;
			d[15] = "" + (d15_is_valid ? elem.getTools_peri_used() : "n/a");
			if (d15_is_valid) {
				score += elem.getTools_peri_used();
				total++;
			}

			boolean d16_is_valid = elem.getTools_peri_correctly() != null && elem.getTools_peri_correctly() > -1;
			d[16] = "" + (d16_is_valid ? elem.getTools_peri_correctly() : "n/a");
			if (d16_is_valid) {
				score += elem.getTools_peri_correctly();
				total++;
			}

			d[17] = "" + elem.getTrain_any_done();
			score += elem.getTrain_any_done();
			total++;

			d[18] = "" + elem.getTrain_recent_done();
			score += elem.getTrain_recent_done();
			total++;

			d[19] = "" + elem.getTrain_embed_work();
			score += elem.getTrain_embed_work();
			total++;

			d[20] = "" + elem.getRes_processes();
			score += elem.getRes_processes();
			total++;

			d[21] = "" + elem.getRes_mobilisation();
			score += elem.getRes_mobilisation();
			total++;

			d[22] = "" + elem.getRes_expenditure();
			score += elem.getRes_expenditure();
			total++;

			d[23] = "" + elem.getCommunity_structure();
			score += elem.getCommunity_structure();
			total++;

			d[24] = "" + elem.getCommunity_feedback();
			score += elem.getCommunity_feedback();
			total++;

			d[25] = "" + elem.getImpl_reinforcement();
			score += elem.getImpl_reinforcement();
			total++;

			d[26] = "" + elem.getImpl_confidentiality();
			score += elem.getImpl_confidentiality();
			total++;

			boolean d27_is_valid = elem.getImpl_participation() != null && elem.getImpl_participation() > -1;
			d[27] = "" + (d27_is_valid ? elem.getImpl_participation() : "n/a");
			if (d27_is_valid) {
				score += elem.getImpl_participation();
				total++;
			}

			d[28] = "" + elem.getTrack_rec_low();
			score += elem.getTrack_rec_low();
			total++;

			d[29] = "" + elem.getTrack_rec_mid();
			score += elem.getTrack_rec_mid();
			total++;

			d[30] = "" + elem.getTrack_rec_high();
			score += elem.getTrack_rec_high();
			total++;

			d[31] = "" + score;
			d[32] = "" + total;
			final Double mPImpRate = ((score == 0 || total == 0) ? 0
					: Math.round((((double) score / (double) total) * 100) * 10.0) / 10.0);
			d[33] = "" + mPImpRate + "%";

			final Double mTotals = (double) (elem.getDhis2_maternal_death_count() == null ? 0
					: elem.getDhis2_maternal_death_count());
			final Double mNotify = (double) caseRepo.countByMaternal_death_notified(elem.getPrev_survey_date(),
					elem.getSurvey_date());
			final Double mNotifyP = (mTotals == null || mTotals == 0 ? 0
					: Math.round(((mNotify / mTotals) * 100) * 10.0) / 10.0);

			final Double mReview = (double) caseRepo.countByMaternal_death_reviewed(elem.getPrev_survey_date(),
					elem.getSurvey_date());
			final Double mReviewP = (mTotals == null || mTotals == 0 ? 0
					: Math.round(((mReview / mTotals) * 100) * 10.0) / 10.0);

			final Double pTotals = (double) (elem.getDhis2_perinatal_death_count() == null ? 0
					: elem.getDhis2_perinatal_death_count());
			final Double pNotify = (double) caseRepo.countByPerinatal_death_notified(elem.getPrev_survey_date(),
					elem.getSurvey_date());
			final Double pNotifyP = (pTotals == null || pTotals == 0 ? 0
					: Math.round(((pNotify / pTotals) * 100) * 10.0) / 10.0);

			final Double pReview = (double) caseRepo.countByPerinatal_death_reviewed(elem.getPrev_survey_date(),
					elem.getSurvey_date());
			final Double pReviewP = (pTotals == null || pTotals == 0 ? 0
					: Math.round(((pReview / pTotals) * 100) * 10.0) / 10.0);

			d[34] = "" + (mTotals == null || mTotals == 0 ? 0 : mTotals);
			d[35] = "" + mNotify + "(" + (mNotifyP) + "%)";
			d[36] = "" + mReview + "(" + (mReviewP) + "%)";

			d[37] = "" + (pTotals == null || pTotals == 0 ? 0 : pTotals);
			d[38] = "" + pNotify + "(" + pNotifyP + "%)";
			d[39] = "" + pReview + "(" + (pReviewP) + "%)";

			d[40] = f.format(elem.getSurvey_date());
			d[41] = elem.getSurvey_author();

			if ((elem.getSurvey_review_date() != null)) {
				d[42] = f.format(elem.getSurvey_review_date());
				d[43] = elem.getSurvey_reviewer();
			} else {
				d[42] = "Not validated";
				d[43] = "Not validated";
			}

			data.add(d);

		}

		model.addAttribute("results", "results");
		model.addAttribute("dataset", data);

		return "survey/survey-results";
	}

	@GetMapping("/tool")
	public String tool(Principal principal, Model model, @RequestParam(required = false) String id,
			@RequestParam(required = false) String success) {

		Optional<sync_table> sync_table = syncRepo.findById(CONSTANTS.LICENSE_ID);

		if (!sync_table.isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		monitoring_tool selected = new monitoring_tool();

		if (id != null) {
			Optional<monitoring_tool> tool = toolRepo.findById(id);
			if (tool.isPresent()) {
				selected = tool.get();
				if (!selected.getSurvey_author().equals(principal.getName())) {
					model.addAttribute("reviewing", "active");
				}
			}
		}

		if (selected.getPrev_survey_date() == null) {
			selected.setPrev_survey_date(sync_table.get().getPrev_survey_date());
		}

		model.addAttribute("selected", selected);
		model.addAttribute("tool", "tool");

		if (success != null) {
			model.addAttribute("success", "Saved Successfully");
		}

		return "survey/survey-tool";
	}

	@PostMapping("/tool")
	public String tool(Principal principal, @ModelAttribute monitoring_tool selected,
			@RequestParam(required = false) String id) {

		if (id == null) {
			final String survey_id = new SimpleDateFormat("dd-MMM-yyyy").format(selected.getSurvey_date());
			selected.setSurvey_id(survey_id);
			selected.setSurvey_author(principal.getName());
			selected.setEntry_date(new Date());
		}

		//if (selected.isSurvey_completed()) {
			selected.setSurvey_review_date(new Date());
			selected.setSurvey_reviewer(principal.getName());

			// set new previous date after validation is completed. because this is when the
			// assessment date is locked
			sync_table sync_table = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
			selected.setCase_sync(sync_table);
			final Date curr_survey_date = selected.getSurvey_date();
			final Date prev_survey_date = sync_table.getPrev_survey_date();

			if (prev_survey_date == null || curr_survey_date.after(prev_survey_date)) {

				if (prev_survey_date == null) {
					selected.setPrev_survey_date(curr_survey_date);
				}

				sync_table.setPrev_survey_date(curr_survey_date);
				syncRepo.save(sync_table);
			}
		//}

		toolRepo.save(selected);

		return "redirect:/survey/tool?id=" + selected.getSurvey_id() + "&success=yes";
	}

}// end class