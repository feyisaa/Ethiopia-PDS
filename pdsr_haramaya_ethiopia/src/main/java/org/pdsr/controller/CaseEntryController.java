package org.pdsr.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.pdsr.CONSTANTS;
import org.pdsr.EmailService;
import org.pdsr.InternetAvailabilityChecker;
import org.pdsr.ServiceApi;
import org.pdsr.json.json_data;
import org.pdsr.json.json_redcap;
import org.pdsr.master.model.abnormality_table;
import org.pdsr.master.model.case_antenatal;
import org.pdsr.master.model.case_babydeath;
import org.pdsr.master.model.case_biodata;
import org.pdsr.master.model.case_birth;
import org.pdsr.master.model.case_delivery;
import org.pdsr.master.model.case_fetalheart;
import org.pdsr.master.model.case_identifiers;
import org.pdsr.master.model.case_labour;
import org.pdsr.master.model.case_mdeath;
import org.pdsr.master.model.case_mdeath_ante;
import org.pdsr.master.model.case_mdeath_early;
import org.pdsr.master.model.case_mdeath_intra;
import org.pdsr.master.model.case_mdeath_other;
import org.pdsr.master.model.case_mdeath_postpart;
import org.pdsr.master.model.case_notes;
import org.pdsr.master.model.case_pregnancy;
import org.pdsr.master.model.case_referral;
import org.pdsr.master.model.complication_table;
import org.pdsr.master.model.cordfault_table;
import org.pdsr.master.model.datamap;
import org.pdsr.master.model.datamapPK;
import org.pdsr.master.model.diagnoses_table;
import org.pdsr.master.model.icd_diagnoses;
import org.pdsr.master.model.placentacheck_table;
import org.pdsr.master.model.resuscitation_table;
import org.pdsr.master.model.risk_table;
import org.pdsr.master.model.sync_table;
import org.pdsr.master.repo.AbnormalityTableRepository;
import org.pdsr.master.repo.CaseAntenatalRepository;
import org.pdsr.master.repo.CaseBabyRepository;
import org.pdsr.master.repo.CaseBiodataRepository;
import org.pdsr.master.repo.CaseBirthRepository;
import org.pdsr.master.repo.CaseDeliveryRepository;
import org.pdsr.master.repo.CaseFetalheartRepository;
import org.pdsr.master.repo.CaseLabourRepository;
import org.pdsr.master.repo.CaseMdeathAnteRepository;
import org.pdsr.master.repo.CaseMdeathEarlyRepository;
import org.pdsr.master.repo.CaseMdeathIntraRepository;
import org.pdsr.master.repo.CaseMdeathOtherRepository;
import org.pdsr.master.repo.CaseMdeathPostpartRepository;
import org.pdsr.master.repo.CaseMdeathRepository;
import org.pdsr.master.repo.CaseNotesRepository;
import org.pdsr.master.repo.CasePregnancyRepository;
import org.pdsr.master.repo.CaseReferralRepository;
import org.pdsr.master.repo.CaseRepository;
import org.pdsr.master.repo.ComplicationTableRepository;
import org.pdsr.master.repo.CordfaultTableRepository;
import org.pdsr.master.repo.DatamapRepository;
import org.pdsr.master.repo.DiagnosesTableRepository;
import org.pdsr.master.repo.IcdDiagnosesRepository;
import org.pdsr.master.repo.PlacentacheckTableRepository;
import org.pdsr.master.repo.ResuscitationTableRepository;
import org.pdsr.master.repo.RiskTableRepository;
import org.pdsr.master.repo.SyncTableRepository;
import org.pdsr.master.repo.UserTableRepository;
import org.pdsr.pojos.RedcapExtraction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/registry")
public class CaseEntryController {

	@Autowired
	private ServiceApi api;

	@Autowired
	private SyncTableRepository syncRepo;

	@Autowired
	private UserTableRepository userRepo;

	@Autowired
	private ComplicationTableRepository compRepo;

	@Autowired
	private ResuscitationTableRepository resusRepo;

	@Autowired
	private DiagnosesTableRepository diagRepo;

	@Autowired
	private IcdDiagnosesRepository icddRepo;

	@Autowired
	private RiskTableRepository riskRepo;

	@Autowired
	private AbnormalityTableRepository abnoRepo;

	@Autowired
	private CordfaultTableRepository cordRepo;

	@Autowired
	private PlacentacheckTableRepository placRepo;

	@Autowired
	private CaseRepository caseRepo;

	@Autowired
	private CaseBiodataRepository bioRepo;

	@Autowired
	private CaseReferralRepository refRepo;

	@Autowired
	private CaseAntenatalRepository antRepo;

	@Autowired
	private CasePregnancyRepository preRepo;

	@Autowired
	private CaseLabourRepository labRepo;

	@Autowired
	private CaseDeliveryRepository delRepo;

	@Autowired
	private CaseBirthRepository birRepo;

	@Autowired
	private CaseFetalheartRepository fetRepo;

	@Autowired
	private CaseBabyRepository babyRepo;

	@Autowired
	private CaseMdeathRepository mdeathRepo;

	@Autowired
	private CaseMdeathEarlyRepository mdeathEarlyRepo;

	@Autowired
	private CaseMdeathAnteRepository mdeathAnteRepo;

	@Autowired
	private CaseMdeathIntraRepository mdeathIntraRepo;

	@Autowired
	private CaseMdeathPostpartRepository mdeathPostpartRepo;

	@Autowired
	private CaseMdeathOtherRepository mdeathOtherRepo;

	@Autowired
	private CaseNotesRepository notRepo;

	@Autowired
	private DatamapRepository mapRepo;

	@Autowired
	private MessageSource msg;

	@Autowired
	private EmailService emailService;

	@GetMapping("")
	public String list(Principal principal, Model model) {

		if (!syncRepo.findById(CONSTANTS.LICENSE_ID).isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		sync_table synctable = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		model.addAttribute("myf", synctable.getSync_name());

		List<case_identifiers> entered_cases = caseRepo.findByDraftCases();// find cases not yet submitted
		entered_cases.addAll(caseRepo.findByPendingCase_status(1));// find cases just entered and pending review

		model.addAttribute("items", entered_cases);
		model.addAttribute("back", "back");

		RedcapExtraction redcap = new RedcapExtraction();
		model.addAttribute("redcap", redcap);

		return "registry/case-retrieve";
	}
	
	@GetMapping("/all")
    public String showAllCases(Model model) {

        List<case_identifiers> cases =
        		caseRepo.findAllWithAllDetails();

        model.addAttribute("cases", cases);
        return "registry/case-all";
    }
	
	 @GetMapping("/full")
	    public void exportAllCases(HttpServletResponse response) throws IOException {

	        response.setContentType("text/csv");
	        response.setHeader(
	            "Content-Disposition",
	            "attachment; filename=cases_full_export.csv"
	        );

	        List<case_identifiers> cases =
	        		caseRepo.findAllWithAllDetails();

	        PrintWriter writer = response.getWriter();

	        // ---- CSV HEADER (ALL TABLES) ----
	        writer.println(
	        	"FacilityName,"+
	            "CaseID,CaseDate,DeathType,Mother's ID,National ID,Mother's name," +
	            "Sex of the child,Mother age,Religion,HDSS LocationId,Mother's Litracy,Mother's Education,Traceable address,Name of Village,Residency,phone numbers,Occupation,Marital status,Ethnicity," +
	            "PregnancyWeeks,In days,Type of pregnancy," +
	            "Woman deliver,Abortion or miscarriage,Deliver in facility,Delivery period,DeliveryDate,"+
	            "woman referred to facility,Who was referred,Referred from,Facility name the woman referred from,Referral date,Mode of transportation,"+
	            "Gravida/Gravidity,Para/Parity,"+
	            "Did labour occur,How did labour start,"+
	            "Fetal Heart sound present from the referring facility,Fetal Heart sound present on arrival,"+
	            "Mode of delivery of this baby,Delivered by,"
	           
	            
	        );

	        for (case_identifiers c : cases) {

	            writer.println(
	            	safe("") + "," +
	                safe(c.getCase_id()) + "," +
	                safe(c.getCase_date()) + "," +
	                safe(c.getCase_death()) + "," +
	                safe(c.getCase_mid()) + "," +
	                safe(c.getCase_nid()) + "," +
	                safe(c.getCase_mname()) + "," +

	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_sex(): "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_mage() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_religion() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_hdssid() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_mlit() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_medu() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_maddress() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_location() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_residency() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_contact() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_work() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_marital() : "") + "," +
	                safe(c.getBiodata() != null ? c.getBiodata().getBiodata_ethnic() : "") + "," +

	                
	                safe(c.getPregnancy() != null ? c.getPregnancy().getPregnancy_weeks() : "") + "," +
	                safe(c.getPregnancy() != null ? c.getPregnancy().getPregnancy_days() : "") + "," +
	                safe(c.getPregnancy() != null ? c.getPregnancy().getPregnancy_type() : "") + "," +
	                
	                safe(c.getDelivery() != null ? c.getDelivery().getDelivery_occured() : "") + "," +
	                safe(c.getDelivery() != null ? c.getDelivery().getDelivery_abortion() : "") + "," +
	                safe(c.getDelivery() != null ? c.getDelivery().getDelivery_occured_facility() : "") + "," +
	                safe(c.getDelivery() != null ? c.getDelivery().getDelivery_period() : "") + "," +
	                safe(c.getDelivery() != null ? c.getDelivery().getDelivery_date() : "") + "," +
	                
	                
                    safe(c.getReferral() != null ? c.getReferral().getReferral_case() : "") + "," +
                    safe(c.getReferral() != null ? c.getReferral().getReferral_patient() : "") + "," +
                    safe(c.getReferral() != null ? c.getReferral().getReferral_source() : "") + "," +
                    safe(c.getReferral() != null ? c.getReferral().getReferral_date() : "") + "," +
                    safe(c.getReferral() != null ? c.getReferral().getReferral_transport() : "") + "," +
                    
                    safe(c.getAntenatal() != null ? c.getAntenatal().getAntenatal_gravida() : "") + "," +
                    safe(c.getAntenatal() != null ? c.getAntenatal().getAntenatal_para() : "") + "," +
                    
                    safe(c.getLabour() != null ? c.getLabour().getLabour_occured() : "") + "," +
                    safe(c.getLabour() != null ? c.getLabour().getLabour_startmode() : "") + "," +
                    
                    safe(c.getFetalheart() != null ? c.getFetalheart().getFetalheart_refered() : "") + "," +
                    safe(c.getFetalheart() != null ? c.getFetalheart().getFetalheart_arrival() : "") + "," +
                    
                    safe(c.getBirth() != null ? c.getBirth().getBirth_mode() : "") + "," +
                    safe(c.getBirth() != null ? c.getBirth().getBirth_provider() : "") + "," +
                    
                    safe(c.getBabydeath() != null ? c.getBabydeath().getBaby_cry() : "") + "," +
                    safe(c.getBabydeath() != null ? c.getBabydeath().getBaby_resuscitation() : "") 
                    
                  
                    
                    

	            );
	        }

	        writer.flush();
	        writer.close();
	    }
	  private String safe(Object value) {
	        return value == null ? "" : value.toString().replace(",", " ");
	    }
	

	

	@GetMapping("/redcap")
	public String redcap(Principal principal, Model model) {

		if (!syncRepo.findById(CONSTANTS.LICENSE_ID).isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		sync_table synctable = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		model.addAttribute("myf", synctable.getSync_name());

		RedcapExtraction redcap = new RedcapExtraction();
		redcap.setProcessingStage(0);
		model.addAttribute("redcap", redcap);
		model.addAttribute("back", "back");

		return "registry/case-redcap";
	}

	@Transactional
	@PostMapping("/redcap")
	public String redcap(Principal principal, Model model, @ModelAttribute RedcapExtraction redcap) {

		if (!syncRepo.findById(CONSTANTS.LICENSE_ID).isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		sync_table synctable = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		model.addAttribute("myf", synctable.getSync_name());

		List<case_identifiers> entered_cases = caseRepo.findByDraftCases();// find cases not yet submitted
		entered_cases.addAll(caseRepo.findByPendingCase_status(1));// find cases just entered and pending review

		// pull red-cap cases within a date range
		if (redcap.getProcessingStage() == null || redcap.getProcessingStage() == 0) {
			List<json_redcap> redcap_cases = api.extractRedCapIdentifiers(redcap.getFrom(), redcap.getTo(), "", "");

			List<String> uploadErrors = new ArrayList<>();

//			int counter = 1;
			a: for (json_redcap elem : redcap_cases) {

				if (elem.getCase_date() == null || elem.getCase_date() instanceof Date || elem.getCase_death() == null
						|| elem.getCase_mid() == null || elem.getCase_mid().trim().isEmpty()
						|| elem.getCase_mname() == null || elem.getCase_mname().trim().isEmpty()) {

					String error = "Record ID : " + elem.getRecord_id()
							+ " has no basic identity information. Was not uploaded";

					uploadErrors.add(error);

					continue a;
				}

				final String case_uuid = UUID.randomUUID().toString();

				case_identifiers page1 = new case_identifiers();
				page1.setCase_uuid(case_uuid);
				page1.setCase_id(
						"T" + elem.getCase_death() + "C" + (elem.getCase_date().getTime()) + elem.getRecord_id());

				page1.setCase_date(elem.getCase_date());
				page1.setCase_death(elem.getCase_death());
				page1.setCase_mid(elem.getCase_mid());
				page1.setCase_mname(elem.getCase_mname());

				page1.setCase_status(0);// entry
				page1.setData_sent(0);
				page1.setCase_sync(synctable);
				caseRepo.save(page1);

				case_biodata page2 = new case_biodata();
				page2.setBiodata_uuid(case_uuid);
				page2.setCase_uuid(page1);
				page2.setBiodata_mage(elem.getBiodata_mage());
				page2.setBiodata_sex(elem.getBiodata_sex());
				page2.setBiodata_medu(elem.getBiodata_medu());
				page2.setData_complete(elem.getCase_demographic_form_complete());
				bioRepo.save(page2);

				case_referral page3 = new case_referral();
				page3.setReferral_uuid(case_uuid);
				page3.setCase_uuid(page1);
				page3.setReferral_case(elem.getReferral_case());
				page3.setReferral_patient(elem.getReferral_patient());
				page3.setReferral_source(elem.getReferral_source());
				page3.setReferral_facility(elem.getReferral_facility());
				page3.setReferral_date(elem.getReferral_date());
				page3.setReferral_time(elem.getReferral_time());
				{
					Date time = page3.getReferral_time();
					if (time != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(time);
						page3.setReferral_hour(cal.get(Calendar.HOUR_OF_DAY));
						page3.setReferral_minute(cal.get(Calendar.MINUTE));
					}
				}
				page3.setReferral_datetime_notstated(elem.getReferral_datetime_notstated());
				page3.setReferral_adate(elem.getReferral_adate());
				page3.setReferral_atime(elem.getReferral_atime());
				{
					Date time = page3.getReferral_atime();
					if (time != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(time);
						page3.setReferral_ahour(cal.get(Calendar.HOUR_OF_DAY));
						page3.setReferral_aminute(cal.get(Calendar.MINUTE));
					}
				}
				page3.setReferral_adatetime_notstated(elem.getReferral_adatetime_notstated());
				page3.setReferral_transport(elem.getReferral_transport());
				page3.setReferral_notes(elem.getReferral_notes());
				// can I pull the referral file uploaded?
				page3.setData_complete(elem.getCase_referral_form_complete());
				refRepo.save(page3);

				case_pregnancy page4 = new case_pregnancy();
				page4.setPregnancy_uuid(case_uuid);
				page4.setCase_uuid(page1);
				page4.setPregnancy_weeks(elem.getPregnancy_weeks());
				page4.setPregnancy_days(elem.getPregnancy_days());
				page4.setPregnancy_type(elem.getPregnancy_type());
				page4.setData_complete(elem.getCase_pregnancy_form_complete());
				preRepo.save(page4);

				case_antenatal page5 = new case_antenatal();
				page5.setAntenatal_uuid(case_uuid);
				page5.setCase_uuid(page1);
				page5.setAntenatal_gravida(elem.getAntenatal_gravida());
				page5.setAntenatal_para(elem.getAntenatal_para());
				page5.setAntenatal_attend(elem.getAntenatal_attend());
				page5.setAntenatal_attendno(elem.getAntenatal_attendno());
				page5.setAntenatal_facility(elem.getAntenatal_facility());
				page5.setAntenatal_weeks(elem.getAntenatal_weeks());
				page5.setAntenatal_days(elem.getAntenatal_days());
				page5.setAntenatal_hiv(elem.getAntenatal_hiv());
				page5.setAntenatal_art(elem.getAntenatal_art());
				page5.setAntenatal_alcohol(elem.getAntenatal_alcohol());
				page5.setAntenatal_smoker(elem.getAntenatal_smoker());
				page5.setAntenatal_herbal(elem.getAntenatal_herbal());
				page5.setAntenatal_folicacid(elem.getAntenatal_folicacid());
				page5.setAntenatal_folicacid3m(elem.getAntenatal_folicacid3m());
				page5.setAntenatal_tetanus(elem.getAntenatal_tetanus());
				page5.setAntenatal_malprophy(elem.getAntenatal_malprophy());
				page5.setAntenatal_risks(elem.getAntenatal_risks());
				page5.setNew_risks(elem.getNew_risks());
				page5.setData_complete(elem.getCase_antenatal_form_complete());
				antRepo.save(page5);

				case_labour page6 = new case_labour();
				page6.setLabour_uuid(case_uuid);
				page6.setCase_uuid(page1);
				page6.setLabour_seedate(elem.getLabour_seedate());
				page6.setLabour_seetime(elem.getLabour_seetime());
				{
					Date time = page6.getLabour_seetime();
					if (time != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(time);
						page6.setLabour_seehour(cal.get(Calendar.HOUR_OF_DAY));
						page6.setLabour_seeminute(cal.get(Calendar.MINUTE));
					}
				}
				page6.setLabour_seedatetime_notstated(elem.getLabour_seedatetime_notstated());
				page6.setLabour_seeperiod(elem.getLabour_seeperiod());
				page6.setLabour_startmode(elem.getLabour_startmode());
				page6.setLabour_herbalaug(elem.getLabour_herbalaug());
				page6.setLabour_partograph(elem.getLabour_partograph());
				page6.setLabour_lasthour1(elem.getLabour_lasthour1());
				page6.setLabour_lastminute1(elem.getLabour_lastminute1());
				page6.setLabour_lasthour2(elem.getLabour_lasthour2());
				page6.setLabour_lastminute2(elem.getLabour_lastminute2());
				page6.setLabour_complications(elem.getLabour_complications());
				page6.setNew_complications(elem.getNew_complications());
				page6.setData_complete(elem.getCase_labour_form_complete());
				labRepo.save(page6);

				case_delivery page7 = new case_delivery();
				page7.setDelivery_uuid(case_uuid);
				page7.setCase_uuid(page1);
				page7.setDelivery_date(elem.getDelivery_date());
				page7.setDelivery_time(elem.getDelivery_time());
				{
					Date time = page7.getDelivery_time();
					if (time != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(time);
						page7.setDelivery_hour(cal.get(Calendar.HOUR_OF_DAY));
						page7.setDelivery_minute(cal.get(Calendar.MINUTE));
					}
				}
				page7.setDelivery_datetime_notstated(elem.getDelivery_datetime_notstated());
				page7.setDelivery_period(elem.getDelivery_period());
				page7.setData_complete(elem.getCase_delivery_form_complete());
				delRepo.save(page7);

				case_birth page8 = new case_birth();
				page8.setBirth_uuid(case_uuid);
				page8.setCase_uuid(page1);
				page8.setBirth_mode(elem.getBirth_mode());
				page8.setBirth_insistnormal(elem.getBirth_insistnormal());
				page8.setBirth_csproposedate(elem.getBirth_csproposedate());
				page8.setBirth_csproposetime(elem.getBirth_csproposetime());
				{
					Date time = page8.getBirth_csproposetime();
					if (time != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(time);
						page8.setBirth_csproposehour(cal.get(Calendar.HOUR_OF_DAY));
						page8.setBirth_csproposeminute(cal.get(Calendar.MINUTE));
					}
				}
				page8.setBirth_provider(elem.getBirth_provider());
				page8.setBirth_facility(elem.getBirth_facility());
				page8.setBirth_abnormalities(elem.getBirth_abnormalities());
				page8.setNew_abnormalities(elem.getNew_abnormalities());
				page8.setBirth_cordfaults(elem.getBirth_cordfaults());
				page8.setNew_cordfaults(elem.getNew_cordfaults());
				page8.setBirth_placentachecks(elem.getBirth_placentachecks());
				page8.setNew_placentachecks(elem.getNew_placentachecks());
				page8.setBirth_liqourvolume(elem.getBirth_liqourvolume());
				page8.setBirth_liqourcolor(elem.getBirth_liqourcolor());
				page8.setBirth_liqourodour(elem.getBirth_liqourodour());
				page8.setBirth_motheroutcome(elem.getBirth_motheroutcome());
				page8.setBirth_babyoutcome(elem.getBirth_babyoutcome());
				page8.setData_complete(elem.getCase_birth_form_complete());
				birRepo.save(page8);

				if (page1.getCase_death() == 1) {

					case_fetalheart page9 = new case_fetalheart();
					page9.setFetalheart_uuid(case_uuid);
					page9.setCase_uuid(page1);
					page9.setFetalheart_refered(elem.getFetalheart_refered());
					page9.setFetalheart_arrival(elem.getFetalheart_arrival());
					page9.setFetalheart_lastheard(elem.getFetalheart_lastheard());
					page9.setData_complete(elem.getCase_fetal_heart_complete());
					fetRepo.save(page9);
				}

				if (page1.getCase_death() == 2) {

					case_babydeath page0 = new case_babydeath();
					page0.setBaby_uuid(case_uuid);
					page0.setCase_uuid(page1);
					page0.setBaby_cry(elem.getBaby_cry());
					page0.setBaby_resuscitation(elem.getBaby_resuscitation());
					page0.setNew_resuscitation(elem.getNew_resuscitation());
					page0.setBaby_apgar1(elem.getBaby_apgar1());
					page0.setBaby_apgar5(elem.getBaby_apgar5());
					page0.setBaby_admitted(elem.getBaby_admitted());
					page0.setNew_diagnoses(elem.getNew_diagnoses());
					page0.setBaby_ddate(elem.getBaby_ddate());
					page0.setBaby_dtime(elem.getBaby_dtime());
					{
						Date time = page0.getBaby_dtime();
						if (time != null) {
							Calendar cal = Calendar.getInstance();
							cal.setTime(time);
							page0.setBaby_dhour(cal.get(Calendar.HOUR_OF_DAY));
							page0.setBaby_dminute(cal.get(Calendar.MINUTE));
						}
					}
					page0.setBaby_ddatetime_notstated(elem.getBaby_ddatetime_notstated());
					page0.setBaby_medicalcod(elem.getBaby_medicalcod());
					page0.setData_complete(elem.getCase_baby_death_complete());
					babyRepo.save(page0);

				}

				if (page1.getCase_death() == 3) {

					case_mdeath page0 = new case_mdeath();
					page0.setMdeath_uuid(case_uuid);
					page0.setCase_uuid(page1);
					page0.setMdeath_date(elem.getMdeath_date());
					page0.setMdeath_time(elem.getMdeath_time());
					{
						Date time = page0.getMdeath_time();
						if (time != null) {
							Calendar cal = Calendar.getInstance();
							cal.setTime(time);
							page0.setMdeath_hour(cal.get(Calendar.HOUR_OF_DAY));
							page0.setMdeath_minute(cal.get(Calendar.MINUTE));
						}
					}
					page0.setMdeath_datetime_notstated(elem.getMdeath_datetime_notstated());
					page0.setMdeath_autopsy(elem.getMdeath_autopsy());
					page0.setMdeath_autopsy_date(elem.getMdeath_autopsy_date());
					page0.setMdeath_autopsy_location(elem.getMdeath_autopsy_location());
					page0.setMdeath_autopsy_by(elem.getMdeath_autopsy_by());
					page0.setMdeath_autopsy_final_cod(elem.getMdeath_autopsy_final_cod());
					page0.setMdeath_autopsy_antec_cod(elem.getMdeath_autopsy_antec_cod());
					page0.setMdeath_autopsy_ops_cod(elem.getMdeath_autopsy_ops_cod());

					if (page1.getCase_mdeath() == 1) {
						case_mdeath_early page01 = new case_mdeath_early();
						page01.setEarly_uuid(case_uuid);
						page01.setCase_uuid(page1);
						page01.setEarly_evacuation(elem.getMdeath_early_evacuation());
						page01.setEarly_antibiotic(elem.getMdeath_early_antibiotic());
						page01.setEarly_laparotomy(elem.getMdeath_early_laparotomy());
						page01.setEarly_hysterectomy(elem.getMdeath_early_hysterectomy());
						page01.setEarly_transfusion(elem.getMdeath_early_transfusion());
						page01.setEarly_antihyper(elem.getMdeath_early_antihyper());
						page01.setEarly_other(elem.getMdeath_early_other());
						mdeathEarlyRepo.save(page01);

					}

					if (page1.getCase_mdeath() == 2) {
						case_mdeath_ante page01 = new case_mdeath_ante();
						page01.setAnte_uuid(case_uuid);
						page01.setCase_uuid(page1);
						page01.setAnte_transfusion(elem.getMdeath_ante_transfusion());
						page01.setAnte_antibiotic(elem.getMdeath_ante_antibiotic());
						page01.setAnte_externalv(elem.getMdeath_ante_externalv());
						page01.setAnte_magsulphate(elem.getMdeath_ante_magsulphate());
						page01.setAnte_diazepam(elem.getMdeath_ante_diazepam());
						page01.setAnte_antihyper(elem.getMdeath_ante_antihyper());
						page01.setAnte_hysterotomy(elem.getMdeath_ante_hysterotomy());
						page01.setAnte_other(elem.getMdeath_ante_other());
						mdeathAnteRepo.save(page01);
					}

					if (page1.getCase_mdeath() == 2) {
						case_mdeath_intra page01 = new case_mdeath_intra();
						page01.setIntra_uuid(case_uuid);
						page01.setCase_uuid(page1);
						page01.setIntra_instrumental(elem.getMdeath_intra_instrumental());
						page01.setIntra_antibiotic(elem.getMdeath_intra_antibiotic());
						page01.setIntra_caesarian(elem.getMdeath_intra_caesarian());
						page01.setIntra_hysterectomy(elem.getMdeath_intra_hysterectomy());
						page01.setIntra_transfusion(elem.getMdeath_intra_transfusion());
						page01.setIntra_magsulphate(elem.getMdeath_intra_magsulphate());
						page01.setIntra_antihyper(elem.getMdeath_intra_antihyper());
						page01.setIntra_diazepam(elem.getMdeath_intra_diazepam());
						page01.setIntra_other(elem.getMdeath_intra_other());
						mdeathIntraRepo.save(page01);
					}

					if (page1.getCase_mdeath() == 3) {
						case_mdeath_postpart page01 = new case_mdeath_postpart();
						page01.setPostpart_uuid(case_uuid);
						page01.setCase_uuid(page1);
						page01.setPostpart_evacuation(elem.getMdeath_postpart_evacuation());
						page01.setPostpart_antibiotic(elem.getMdeath_postpart_antibiotic());
						page01.setPostpart_laparotomy(elem.getMdeath_postpart_laparotomy());
						page01.setPostpart_hysterectomy(elem.getMdeath_postpart_hysterectomy());
						page01.setPostpart_transfusion(elem.getMdeath_postpart_transfusion());
						page01.setPostpart_magsulphate(elem.getMdeath_postpart_magsulphate());
						page01.setPostpart_placentaremoval(elem.getMdeath_postpart_placentaremoval());
						page01.setPostpart_antihyper(elem.getMdeath_postpart_antihyper());
						page01.setPostpart_diazepam(elem.getMdeath_postpart_diazepam());
						page01.setPostpart_other(elem.getMdeath_postpart_other());
						mdeathPostpartRepo.save(page01);
					}
					{
						case_mdeath_other page01 = new case_mdeath_other();
						page01.setOther_uuid(case_uuid);
						page01.setCase_uuid(page1);
						page01.setOther_anaesthga(elem.getMdeath_other_anaesthga());
						page01.setOther_epidural(elem.getMdeath_other_epidural());
						page01.setOther_spinal(elem.getMdeath_other_spinal());
						page01.setOther_local(elem.getMdeath_other_local());
						page01.setOther_invasive(elem.getMdeath_other_invasive());
						page01.setOther_antihyper(elem.getMdeath_other_antihyper());
						page01.setOther_icuventilation(elem.getMdeath_other_icuventilation());
						page01.setNew_intervention(elem.getMdeath_new_intervention());
						mdeathOtherRepo.save(page01);
					}
					page0.setData_complete(elem.getCase_mdeath_complete());
					mdeathRepo.save(page0);

				}

				case_notes notes = new case_notes();
				notes.setNotes_uuid(case_uuid);
				notes.setCase_uuid(page1);
				notes.setNotes_text(elem.getNotes_text());
				notRepo.save(notes);

			}
			if (!uploadErrors.isEmpty()) {
				model.addAttribute("uploaderrors", uploadErrors);
			}
		}

		model.addAttribute("redcap", redcap);
		model.addAttribute("back", "back");

		return "registry/case-redcap";
	}

	@GetMapping("/file")
	public ResponseEntity<Resource> downloadFile() {
		Resource resource = new ClassPathResource("static/PDSR_DEATH_CASE_ENTRY.xlsx");

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pdsr-sample.xlsx");

		return new ResponseEntity<>(resource, headers, HttpStatus.OK);
	}

	@GetMapping("/add")
	public String add(Principal principal, Model model) {

		if (!syncRepo.findById(CONSTANTS.LICENSE_ID).isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		sync_table synctable = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		model.addAttribute("myf", synctable.getSync_name());

		case_identifiers selected = new case_identifiers();
		selected.setCase_date(new Date());

		Optional<sync_table> code = syncRepo.findById(CONSTANTS.LICENSE_ID);
		if (code.isPresent()) {
			selected.setCase_sync(synctable);
		}

		model.addAttribute("selected", selected);

		return "registry/case-create";
	}

	@Transactional
	@PostMapping("/add")
	public String add(Principal principal, Model model, @ModelAttribute case_identifiers selected) {
		if (!syncRepo.findById(CONSTANTS.LICENSE_ID).isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		sync_table synctable = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		model.addAttribute("myf", synctable.getSync_name());

		selected.setCase_uuid(UUID.randomUUID().toString());
		selected.setCase_id("T" + selected.getCase_death() + "C" + (new java.util.Date().getTime()));
		selected.setCase_status(0);
		selected.setData_sent(0);
		selected.setCase_sync(synctable);

		caseRepo.save(selected);

		// place here some of the transactions that will happen based on death timing
		// - if death occurred during pregnancy then auto-fill delivery and birth as not
		// applicable and hide
		// - if death occurred during delivery then delivery is required but birth is
		// optional
		// - if death occurred after delivery then delivery and birth are required

		return "redirect:/registry/edit/" + selected.getCase_uuid() + "?page=1&success=yes";
	}

	@GetMapping("/edit/{id}")
	public String edit(Principal principal, final Model model, @PathVariable("id") String case_uuid,
			@RequestParam(required = true) Integer page, @RequestParam(required = false) String success) {

		if (!syncRepo.findById(CONSTANTS.LICENSE_ID).isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		sync_table synctable = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		model.addAttribute("myf", synctable.getSync_name());

		case_identifiers selected = caseRepo.findById(case_uuid).get();

		if (selected.getCase_death() == CONSTANTS.MATERNAL_DEATH) {
			model.addAttribute("maternal_death", "active");
		}

		boolean completed = selected.getBiodata() != null && selected.getPregnancy() != null
				&& selected.getReferral() != null && selected.getDelivery() != null && selected.getAntenatal() != null
				&& selected.getLabour() != null && selected.getBirth() != null
				&& (selected.getFetalheart() != null || selected.getBabydeath() != null || selected.getMdeath() != null)
				&& selected.getNotes() != null;

		if (completed) {
			final boolean bio = selected.getBiodata().getData_complete() == 1;
			final boolean pre = selected.getPregnancy().getData_complete() == 1;
			final boolean ref = selected.getReferral().getData_complete() == 1;
			final boolean del = selected.getDelivery().getData_complete() == 1;

			final boolean ant = selected.getAntenatal().getData_complete() == 1;
			final boolean lab = selected.getLabour().getData_complete() == 1;
			final boolean bir = selected.getBirth().getData_complete() == 1;

			final boolean fet = selected.getFetalheart() != null && selected.getFetalheart().getData_complete() == 1;
			final boolean bab = selected.getBabydeath() != null && selected.getBabydeath().getData_complete() == 1;
			final boolean mat = selected.getMdeath() != null && selected.getMdeath().getData_complete() == 1;

			completed = bio && ref && del && ant && lab && bir;

			if (selected.getCase_death() == CONSTANTS.STILL_BIRTH) {
				completed = completed && pre && fet;
			} else if (selected.getCase_death() == CONSTANTS.NEONATAL_DEATH) {
				completed = completed && pre && bab;
			} else if (selected.getCase_death() == CONSTANTS.MATERNAL_DEATH) {
				completed = completed && mat;
			}
		}
		if (completed) {
			model.addAttribute("completed", completed);
		}

		switch (page) {
		case 1: {
			model.addAttribute("bioactive", "active");
			if (selected.getBiodata() == null) {
				case_biodata data = new case_biodata();
				data.setBiodata_uuid(selected.getCase_uuid());
				data.setCase_uuid(selected);
				data.setBiodata_ethnic(8);
				selected.setBiodata(data);
			}
			break;
		}

		case 2: {
			model.addAttribute("refactive", "active");
			if (selected.getReferral() == null) {
				case_referral data = new case_referral();
				data.setReferral_uuid(selected.getCase_uuid());
				data.setCase_uuid(selected);

				selected.setReferral(data);
			}
			final String encodedImage = convertBinImageToString(selected.getReferral().getReferral_file());
			selected.getReferral().setBase64image(encodedImage);
			break;
		}
		case 3: {
			model.addAttribute("antactive", "active");
			if (selected.getAntenatal() == null) {
				case_antenatal data = new case_antenatal();
				data.setAntenatal_uuid(selected.getCase_uuid());
				data.setCase_uuid(selected);
				selected.setAntenatal(data);
			}
			model.addAttribute("risk_options", riskRepo.findAll());
			break;
		}

		case 4: {
			model.addAttribute("labactive", "active");
			if (selected.getLabour() == null) {
				case_labour data = new case_labour();
				data.setLabour_uuid(selected.getCase_uuid());
				data.setCase_uuid(selected);
				selected.setLabour(data);
			}
			model.addAttribute("complication_options", compRepo.findAll());
			break;
		}

		case 5: {
			model.addAttribute("delactive", "active");
			if (selected.getDelivery() == null) {
				case_delivery data = new case_delivery();
				data.setDelivery_uuid(selected.getCase_uuid());
				data.setCase_uuid(selected);
				selected.setDelivery(data);
			}
			if (selected.getPregnancy() == null) {
				case_pregnancy data = new case_pregnancy();
				data.setPregnancy_uuid(selected.getCase_uuid());
				data.setCase_uuid(selected);
				selected.setPregnancy(data);
			}

			break;
		}

		case 6: {

			model.addAttribute("biractive", "active");
			if (selected.getBirth() == null) {
				case_birth data = new case_birth();
				data.setBirth_uuid(selected.getCase_uuid());
				data.setCase_uuid(selected);
				selected.setBirth(data);
			}

			if (selected.getDelivery() == null || selected.getDelivery().getDelivery_occured() != 1) {

				model.addAttribute("no_birth", "active");

			} else {

				model.addAttribute("abnormality_options", abnoRepo.findAll());
				model.addAttribute("cordfault_options", cordRepo.findAll());
				model.addAttribute("placentacheck_options", placRepo.findAll());

			}

			break;
		}
		case 7: {
			if (selected.getCase_death() == CONSTANTS.STILL_BIRTH) {
				model.addAttribute("fetactive", "active");
				if (selected.getFetalheart() == null) {
					case_fetalheart data = new case_fetalheart();
					data.setFetalheart_uuid(selected.getCase_uuid());
					data.setCase_uuid(selected);
					selected.setFetalheart(data);

				}
			} else if (selected.getCase_death() == CONSTANTS.NEONATAL_DEATH) {

				model.addAttribute("babactive", "active");
				if (selected.getBabydeath() == null) {
					case_babydeath data = new case_babydeath();
					data.setBaby_uuid(selected.getCase_uuid());
					data.setCase_uuid(selected);
					selected.setBabydeath(data);
				}
				model.addAttribute("diagnoses_options", diagRepo.findAll());
				model.addAttribute("resuscitation_options", resusRepo.findAll());

			} else if (selected.getCase_death() == CONSTANTS.MATERNAL_DEATH) {

				model.addAttribute("itvactive", "active");
				if (selected.getMdeath() == null) {
					case_mdeath data = new case_mdeath();
					data.setMdeath_uuid(selected.getCase_uuid());
					data.setCase_uuid(selected);
					selected.setMdeath(data);
				}
			}
			break;
		}
		case 8: {
			model.addAttribute("notactive", "active");
			if (selected.getNotes() == null) {
				case_notes data = new case_notes();
				data.setNotes_uuid(selected.getCase_uuid());
				data.setCase_uuid(selected);
				selected.setNotes(data);
			}

			final String encodedImage = convertBinImageToString(selected.getNotes().getNotes_file());
			selected.getNotes().setBase64image(encodedImage);

			break;
		}

		}

		model.addAttribute("selected", selected);
		model.addAttribute("page", page);

		if (success != null)

		{
			model.addAttribute("success", "Saved Successfully");
		}

		return "registry/case-update";
	}

	@Transactional
	@PostMapping("/edit/{id}")
	public String edit(Principal principal, Model model, @ModelAttribute("selected") case_identifiers selected,
			@RequestParam(required = true) Integer page, @RequestParam(required = false) Integer go,
			BindingResult results) {

		ObjectMapper objectMapper = new ObjectMapper();

		if (!syncRepo.findById(CONSTANTS.LICENSE_ID).isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		sync_table synctable = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		model.addAttribute("myf", synctable.getSync_name());

		case_identifiers existing = caseRepo.findById(selected.getCase_uuid()).get();
		selected.setData_sent(0);// reset the data sending indicator when any record is edited
		selected.setCase_status(0);// reset the submission status to not submitted (incomplete)

		switch (page) {
		case 1: {

			try {
				case_biodata o = selected.getBiodata();
				if (o.getBiodata_mage() == null || o.getBiodata_medu() == null || o.getBiodata_sex() == null) {
					o.setData_complete(0);
				} else {
					o.setData_complete(1);
					final String arrayToJson = objectMapper.writeValueAsString(processListOf(selected.getBiodata()));
					selected.getBiodata().setBiodata_json(arrayToJson);

				}
				bioRepo.save(o);

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			break;
		}
		case 2: {
			try {

				case_referral o = selected.getReferral();

				if (o.getReferral_case() != null && o.getReferral_case() == 1) {

					Date time = o.getReferral_time();
					if (time != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(time);
						o.setReferral_hour(cal.get(Calendar.HOUR_OF_DAY));
						o.setReferral_minute(cal.get(Calendar.MINUTE));
					}

					Date atime = o.getReferral_atime();
					if (atime != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(atime);
						o.setReferral_ahour(cal.get(Calendar.HOUR_OF_DAY));
						o.setReferral_aminute(cal.get(Calendar.MINUTE));
					}

					validateTheTimesOnReferralPage(model, results, selected, existing);

					MultipartFile file = selected.getReferral().getFile();
					o.setBase64image(null);

					try {
						if (file != null && file.getBytes() != null && file.getBytes().length > 0) {
							o.setReferral_file(file.getBytes());
							o.setReferral_filetype(file.getContentType());
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					if (results.hasErrors()) {
						model.addAttribute("selected", selected);
						model.addAttribute("page", page);
						return "registry/case-update";
					}

				}

				boolean referral_adatetime_expected = (o.getReferral_adatetime_notstated() == null
						|| o.getReferral_adatetime_notstated() == 0);

				boolean referral_adatetime_any_specified =
//						o.getReferral_ahour() != null || 
						o.getReferral_adate() != null;// ||
//						o.getReferral_aminute() != null || 
//						o.getReferral_atime() != null;

				boolean referral_adatetime_any_missing =
						// o.getReferral_ahour() == null ||
						o.getReferral_adate() == null;// ||
				// o.getReferral_aminute() == null ||
				// o.getReferral_atime() == null;

				boolean referral_datetime_expected = (o.getReferral_datetime_notstated() == null
						|| o.getReferral_datetime_notstated() == 0);

				boolean referral_datetime_any_specified =
						// o.getReferral_hour() != null ||
						o.getReferral_date() != null;// ||
				// o.getReferral_minute() != null ||
				// o.getReferral_time() != null;

				boolean referral_datetime_any_missing =
						// o.getReferral_hour() == null ||
						o.getReferral_date() == null; // ||
				// o.getReferral_minute() == null ||
				// o.getReferral_time() == null;

				if ((referral_adatetime_expected && referral_adatetime_any_missing)
						|| (!referral_adatetime_expected && referral_adatetime_any_specified)
						|| o.getReferral_case() == null || (referral_datetime_expected && referral_datetime_any_missing)
						|| (!referral_datetime_expected && referral_datetime_any_specified)
						|| o.getReferral_facility() == null || o.getReferral_facility().trim() == ""
						// || o.getReferral_patient() == null
						|| o.getReferral_source() == null || o.getReferral_transport() == null) {

					o.setData_complete(0);
				} else {
					o.setData_complete(1);
					final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
					o.setReferral_json(arrayToJson);
				}

				refRepo.save(o);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			break;
		}
		case 3: {
			try {

				case_antenatal o = selected.getAntenatal();
				if (o.getAntenatal_alcohol() == null || o.getAntenatal_attend() == null
						|| o.getAntenatal_attendno() == null || o.getAntenatal_days() == null
						|| o.getAntenatal_facility() == null || o.getAntenatal_facility().trim() == ""
						|| o.getAntenatal_folicacid() == null || o.getAntenatal_folicacid3m() == null
						|| o.getAntenatal_gravida() == null || o.getAntenatal_herbal() == null
						|| o.getAntenatal_hiv() == null || o.getAntenatal_malprophy() == null
						|| o.getAntenatal_para() == null || o.getAntenatal_risks() == null
						|| o.getAntenatal_smoker() == null || o.getAntenatal_tetanus() == null
						|| o.getAntenatal_weeks() == null) {
					o.setData_complete(0);
				} else {
					o.setData_complete(1);
					final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
					o.setAntenatal_json(arrayToJson);
				}

				antRepo.save(o);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			break;
		}
//		case 4: {
//			try {
//
//				case_labour o = selected.getLabour();
//
//				Date seetime = o.getLabour_seetime();
//				if (seetime != null) {
//					Calendar cal = Calendar.getInstance();
//					cal.setTime(seetime);
//					o.setLabour_seehour(cal.get(Calendar.HOUR_OF_DAY));
//					o.setLabour_seeminute(cal.get(Calendar.MINUTE));
//				}
//
////				final Integer period = selected.getLabour().getLabour_seeperiod();
////				final Integer hour = selected.getLabour().getLabour_seehour();
////
////				if (period != null && hour != null) {
////					boolean morning = (hour >= 8 && hour < 14);
////					boolean afternoon = (hour >= 14 && hour < 20);
////					boolean night = (hour >= 20 || hour < 8);
////
////					if (period == 88 && (morning || afternoon || night)) {
////						results.rejectValue("labour.labour_seeperiod", "error.nostated");
////					} else if (period == 1 && !morning) {
////						results.rejectValue("labour.labour_seeperiod", "error.morning");
////					} else if (period == 3 && !afternoon) {
////						results.rejectValue("labour.labour_seeperiod", "error.afternoon");
////					} else if (period == 6 && !night) {
////						results.rejectValue("labour.labour_seeperiod", "error.night");
////					}
////
////					if (results.hasErrors()) {
////						model.addAttribute("selected", selected);
////						model.addAttribute("page", page);
////						return "registry/case-update";
////					}
////				}
//
//				if (existing.getReferral() != null && existing.getReferral().getReferral_case() == 1) {
//
//					validateTheTimesOnLabourPage(model, results, selected, existing);
//
//					if (results.hasErrors()) {
//						model.addAttribute("selected", selected);
//						model.addAttribute("page", page);
//						return "registry/case-update";
//					}
//
//				}
//
//				boolean labour_seedatetime_expected = (o.getLabour_seedatetime_notstated() == null
//						|| o.getLabour_seedatetime_notstated() == 0);
//
//				boolean labour_seedatetime_any_specified = o.getLabour_seehour() != null
//						|| o.getLabour_seedate() != null || o.getLabour_seeminute() != null
//						|| o.getLabour_seetime() != null;
//
//				boolean labour_seedatetime_any_missing =
//						// o.getLabour_seehour() == null ||
//						o.getLabour_seedate() == null;// ||
//				// o.getLabour_seeminute() == null ||
//				// o.getLabour_seetime() == null;
//
//				if (o.getLabour_complications() == null || o.getLabour_herbalaug() == null
//						|| o.getLabour_lasthour1() == null || o.getLabour_lasthour2() == null
//						|| o.getLabour_lastminute1() == null || o.getLabour_lastminute2() == null
//						|| o.getLabour_partograph() == null
//						|| (labour_seedatetime_expected && labour_seedatetime_any_missing)
//						|| (!labour_seedatetime_expected && labour_seedatetime_any_specified)
//						|| o.getLabour_seeperiod() == null || o.getLabour_startmode() == null) {
//					o.setData_complete(0);
//				} else {
//					final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
//					o.setLabour_json(arrayToJson);
//					o.setData_complete(1);
//				}
//
//				labRepo.save(o);
//
//			} catch (JsonProcessingException e) {
//				e.printStackTrace();
//			}
//			break;
//		}
		
		case 4: {
		    try {
		        case_labour o = selected.getLabour();

		        // 1. Process time parameters if the see-time timestamp is present
		        Date seetime = o.getLabour_seetime();
		        if (seetime != null) {
		            Calendar cal = Calendar.getInstance();
		            cal.setTime(seetime);
		            o.setLabour_seehour(cal.get(Calendar.HOUR_OF_DAY));
		            o.setLabour_seeminute(cal.get(Calendar.MINUTE));
		        }

		        // 2. Validate linked referral dependencies if applicable
		        if (existing.getReferral() != null && existing.getReferral().getReferral_case() == 1) {
		            validateTheTimesOnLabourPage(model, results, selected, existing);

		            if (results.hasErrors()) {
		                model.addAttribute("selected", selected);
		                model.addAttribute("page", page);
		                return "registry/case-update";
		            }
		        }

		        // 3. Base completeness check
		        if (o.getLabour_occured() == null) {
		            o.setData_complete(0);
		        } 
		        // If labor did NOT occur (e.g., status is not 1/Yes), it is technically complete as-is
		        else if (o.getLabour_occured() != 1) { 
		            final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
		            o.setLabour_json(arrayToJson);
		            o.setData_complete(1);
		        } 
		        // If labor DID occur, evaluate all fields present in your HTML layout
		        else {
		            boolean labour_seedatetime_expected = (o.getLabour_seedatetime_notstated() == null
		                    || o.getLabour_seedatetime_notstated() == 0);

		            boolean labour_seedatetime_any_specified = o.getLabour_seehour() != null
		                    || o.getLabour_seedate() != null || o.getLabour_seeminute() != null
		                    || o.getLabour_seetime() != null;

		            boolean labour_seedatetime_any_missing = o.getLabour_seedate() == null;

		            // REMOVED: o.getLabour_seeperiod() == null (Since it's not in your HTML form)
		            if (o.getLabour_complications() == null 
		                    || o.getLabour_herbalaug() == null
		                    || o.getLabour_lasthour1() == null 
		                    || o.getLabour_lasthour2() == null
		                    || o.getLabour_lastminute1() == null 
		                    || o.getLabour_lastminute2() == null
		                    || o.getLabour_partograph() == null
		                    || (labour_seedatetime_expected && labour_seedatetime_any_missing)
		                    || (!labour_seedatetime_expected && labour_seedatetime_any_specified)
		                    || o.getLabour_startmode() == null) {
		                
		                o.setData_complete(0);
		            } else {
		                final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
		                o.setLabour_json(arrayToJson);
		                o.setData_complete(1);
		            }
		        }

		        // 4. Operational sync to database
		        labRepo.save(o);

		    } catch (JsonProcessingException e) {
		        e.printStackTrace();
		    }
		    break;
		}
		case 5: {
			try {

				case_delivery o = selected.getDelivery();

				Date time = o.getDelivery_time();
				if (time != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(time);
					o.setDelivery_hour(cal.get(Calendar.HOUR_OF_DAY));
					o.setDelivery_minute(cal.get(Calendar.MINUTE));
				}
//				final Integer period = selected.getDelivery().getDelivery_period();
//				final Integer hour = selected.getDelivery().getDelivery_hour();
//
//				if (period != null && hour != null) {
//					boolean morning = (hour >= 8 && hour < 14);
//					boolean afternoon = (hour >= 14 && hour < 20);
//					boolean night = (hour >= 20 || hour < 8);
//
//					if (period == 88 && (morning || afternoon || night)) {
//						results.rejectValue("delivery.delivery_period", "error.nostated");
//					} else if (period == 1 && !morning) {
//						results.rejectValue("delivery.delivery_period", "error.morning");
//					} else if (period == 3 && !afternoon) {
//						results.rejectValue("delivery.delivery_period", "error.afternoon");
//					} else if (period == 6 && !night) {
//						results.rejectValue("delivery.delivery_period", "error.night");
//					}
//
//					if (results.hasErrors()) {
//						model.addAttribute("selected", selected);
//						model.addAttribute("page", page);
//						return "registry/case-update";
//					}
//				}

				if (existing.getLabour() != null && existing.getLabour().getLabour_occured() == 1) {
					validateTheTimesOnArrivalAndDelivery(model, results, selected, existing);

					if (results.hasErrors()) {
						model.addAttribute("selected", selected);
						model.addAttribute("page", page);
						return "registry/case-update";
					}

				}

				if (existing.getBirth() != null) {

					validateTheTimesOnDeliveryPage(model, results, selected, existing);

					if (results.hasErrors()) {
						model.addAttribute("selected", selected);
						model.addAttribute("page", page);
						return "registry/case-update";
					}

				}

				boolean delivery_not_answered = o.getDelivery_occured() == null;

				boolean delivery_abortion_expected = (o.getDelivery_occured() != null && o.getDelivery_occured() == 0
						&& o.getDelivery_abortion() == null);

				boolean delivery_datetime_expected = (o.getDelivery_datetime_notstated() == null
						|| o.getDelivery_datetime_notstated() == 0);

				boolean delivery_datetime_any_specified = o.getDelivery_hour() != null || o.getDelivery_date() != null
						|| o.getDelivery_minute() != null || o.getDelivery_time() != null;

				boolean delivery_datetime_any_missing =
						// o.getDelivery_hour() == null ||
						o.getDelivery_date() == null;// ||
				// o.getDelivery_minute() == null ||
				// o.getDelivery_time() == null;

				if (delivery_not_answered || delivery_abortion_expected
						|| (delivery_datetime_expected && delivery_datetime_any_missing)
						|| (!delivery_datetime_expected && delivery_datetime_any_specified)
						|| o.getDelivery_occured_facility() == null || o.getDelivery_period() == null) {
					o.setData_complete(0);
				} else {

					o.setData_complete(1);
					final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
					o.setDelivery_json(arrayToJson);
				}

				case_pregnancy o1 = selected.getPregnancy();

				if (o1 != null) {

					if (o1.getPregnancy_days() == null || o1.getPregnancy_type() == null
							|| o1.getPregnancy_weeks() == null) {
						o1.setData_complete(0);
						//o.setData_complete(0);
					} else {
						o1.setData_complete(1);
						final String arrayToJson = objectMapper
								.writeValueAsString(processListOf(selected.getPregnancy()));
						selected.getPregnancy().setPregnancy_json(arrayToJson);
					}

					preRepo.save(o1);
				}

				delRepo.save(o);

				if (o.getDelivery_occured() != 1 && o.getDelivery_occured()!=null) {

					case_birth data = new case_birth();
					data.setBirth_uuid(selected.getCase_uuid());
					data.setCase_uuid(selected);
					selected.setBirth(data);

					data.setData_complete(1);

					birRepo.save(data);

				} else {
					case_birth data = selected.getBirth();
					if (data == null) {
						data = new case_birth();
						data.setBirth_uuid(selected.getCase_uuid());
						data.setCase_uuid(selected);
						selected.setBirth(data);

					}

					data.setData_complete(0);
					birRepo.save(data);

				}

			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			break;
		}
		case 6: {
			try {

				case_birth o = selected.getBirth();

				Date cstime = o.getBirth_csproposetime();

				if (cstime != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(cstime);
					o.setBirth_csproposehour(cal.get(Calendar.HOUR_OF_DAY));
					o.setBirth_csproposeminute(cal.get(Calendar.MINUTE));
				}

				if (existing.getDelivery() != null) {

					validateTheTimesOnBirthPage(model, results, selected, existing);

					if (results.hasErrors()) {
						model.addAttribute("selected", selected);
						model.addAttribute("page", page);
						return "registry/case-update";
					}

				}

				boolean insistnormal_notselected = (o.getBirth_mode() != null
						&& (o.getBirth_mode() == 0 || o.getBirth_mode() == 1)) && o.getBirth_insistnormal() == null;

				boolean insistnormal_selected_but_any_cs_datetime_missing = (o.getBirth_mode() != null
						&& (o.getBirth_mode() == 0 || o.getBirth_mode() == 1)) && o.getBirth_insistnormal() != null
						&& o.getBirth_insistnormal() == 1
						&& (o.getBirth_csproposehour() == null || o.getBirth_csproposedate() == null
								|| o.getBirth_csproposeminute() == null || o.getBirth_csproposetime() == null);

				boolean birth_weight_not_provided = o.getBirth_weight() == null;

				if (o.getBirth_abnormalities() == null || o.getBirth_babyoutcome() == null
						|| o.getBirth_cordfaults() == null || o.getBirth_mode() == null || insistnormal_notselected
						|| insistnormal_selected_but_any_cs_datetime_missing || o.getBirth_facility() == null
						|| o.getBirth_insistnormal() == null || o.getBirth_liqourcolor() == null
						|| o.getBirth_liqourodour() == null || o.getBirth_liqourvolume() == null
						|| o.getBirth_motheroutcome() == null || o.getBirth_placentachecks() == null
						|| o.getBirth_provider() == null) {

					o.setData_complete(0);
				} else {
					o.setData_complete(1);
					final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
					o.setBirth_json(arrayToJson);
				}

				birRepo.save(o);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			break;
		}
		case 7: {
			if (selected.getCase_death() == CONSTANTS.STILL_BIRTH) {
				try {

					case_fetalheart o = selected.getFetalheart();
					if (o.getFetalheart_arrival() == null || o.getFetalheart_lastheard() == null
							) {
						o.setData_complete(0);
					} else {
						o.setData_complete(1);
						final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
						selected.getFetalheart().setFetalheart_json(arrayToJson);
					}

					fetRepo.save(o);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

			} else if (selected.getCase_death() == CONSTANTS.NEONATAL_DEATH) {
				try {

					case_babydeath o = selected.getBabydeath();
					if (o == null) {
			            o = new case_babydeath();
			            o.setBaby_uuid(selected.getCase_uuid());
			            o.setCase_uuid(selected);
			            selected.setBabydeath(o);
			        }
					Date time = o.getBaby_dtime();
					if (time != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(time);
						o.setBaby_dhour(cal.get(Calendar.HOUR_OF_DAY));
						o.setBaby_dminute(cal.get(Calendar.MINUTE));
					}

					if (existing.getDelivery() != null) {

						validateTheTimesOnBabydeathPage(model, results, selected, existing);

						if (results.hasErrors()) {
							model.addAttribute("selected", selected);
							model.addAttribute("page", page);
							return "registry/case-update";
						}

					}

					boolean baby_ddatetime_expected = (o.getBaby_ddatetime_notstated() == null
							|| o.getBaby_ddatetime_notstated() == 0);

					boolean baby_ddatetime_any_specified = o.getBaby_dhour() != null || o.getBaby_ddate() != null
							|| o.getBaby_dminute() != null || o.getBaby_dtime() != null;

					boolean baby_ddatetime_any_missing = o.getBaby_dhour() == null || o.getBaby_ddate() == null
							|| o.getBaby_dminute() == null || o.getBaby_dtime() == null;

					if (o.getBaby_admitted() == null || o.getBaby_apgar1() == null || o.getBaby_apgar5() == null
							|| o.getBaby_cry() == null || (baby_ddatetime_expected && baby_ddatetime_any_missing)
							|| (!baby_ddatetime_expected && baby_ddatetime_any_specified)
							|| o.getBaby_medicalcod() == null || o.getBaby_resuscitation() == null) {
						o.setData_complete(0);
					} else {
						o.setData_complete(1);
						final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
						o.setBaby_json(arrayToJson);
					}

					babyRepo.save(o);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

			} else if (selected.getCase_death() == CONSTANTS.MATERNAL_DEATH) {
				try {

					case_mdeath o = selected.getMdeath();
					//added by Feyisa
					if (selected.getBabydeath() == null) {
			            case_babydeath babyDeath = new case_babydeath();
			            babyDeath.setBaby_uuid(selected.getCase_uuid());
			            babyDeath.setCase_uuid(selected);
			            selected.setBabydeath(babyDeath);
			        }

					Date time = o.getMdeath_time();
					if (time != null) {
						Calendar cal = Calendar.getInstance();
						cal.setTime(time);
						o.setMdeath_hour(cal.get(Calendar.HOUR_OF_DAY));
						o.setMdeath_minute(cal.get(Calendar.MINUTE));
					}

					if (existing.getDelivery() != null) {

						validateTheTimesOnMdeathPage(model, results, selected, existing);

						if (results.hasErrors()) {
							model.addAttribute("selected", selected);
							model.addAttribute("page", page);
							return "registry/case-update";
						}

					}

					boolean mdeath_early_interv_missing = false;
					boolean mdeath_ante_interv_missing = false;

					if (selected.getCase_mdeath() == CONSTANTS.DURING_PREGNANCY) {

						case_mdeath_early o1 = selected.getMdeath_early();

						if (o1 != null) {
							o1.setEarly_uuid(selected.getCase_uuid());
							o1.setCase_uuid(selected);

//							mdeath_early_interv_missing =
//							        (
//							            o1.getData_complete() == null
//							            && o1.getEarly_evacuation() == null
//							            && o1.getEarly_antibiotic() == null
//							            && o1.getEarly_laparotomy() == null
//							            && o1.getEarly_hysterectomy() == null
//							            && o1.getEarly_transfusion() == null
//							            && o1.getEarly_antihyper() == null
//							            && o1.getEarly_other() == null
//							        )
//							        ||
//							        (
//							            o1.getData_complete() == 1
//							            && !(Boolean.TRUE.equals(o1.getEarly_evacuation())
//							                || Boolean.TRUE.equals(o1.getEarly_antibiotic())
//							                || Boolean.TRUE.equals(o1.getEarly_laparotomy())
//							                || Boolean.TRUE.equals(o1.getEarly_hysterectomy())
//							                || Boolean.TRUE.equals(o1.getEarly_transfusion())
//							                || Boolean.TRUE.equals(o1.getEarly_antihyper()))
//							            && (o1.getEarly_other() == null || o1.getEarly_other().trim().isEmpty())
//							        );
							mdeath_early_interv_missing = (o1.getData_complete() == null
									&& o1.getEarly_evacuation() == null && o1.getEarly_antibiotic() == null
									&& o1.getEarly_laparotomy() == null && o1.getEarly_hysterectomy() == null
									&& o1.getEarly_transfusion() == null && o1.getEarly_antihyper() == null
									&& o1.getEarly_other() == null)
									|| (o1.getData_complete() == 1
											&& !(Boolean.TRUE.equals(o1.getEarly_evacuation())
													|| Boolean.TRUE.equals(o1.getEarly_antibiotic())
													|| Boolean.TRUE.equals(o1.getEarly_laparotomy())
													|| Boolean.TRUE.equals(o1.getEarly_hysterectomy())
													|| Boolean.TRUE.equals(o1.getEarly_transfusion())
													|| Boolean.TRUE.equals(o1.getEarly_antihyper()))
											&& (o1.getEarly_other() == null || o1.getEarly_other().trim().isEmpty()));
							mdeathEarlyRepo.save(o1);
						}
					}

					if (selected.getCase_mdeath() == CONSTANTS.DURING_PREGNANCY) {

						case_mdeath_ante o1 = selected.getMdeath_ante();

						if (o1 != null) {
							o1.setAnte_uuid(selected.getCase_uuid());
							o1.setCase_uuid(selected);

//							mdeath_ante_interv_missing =
//							        (
//							            o1.getData_complete() == null
//							            && o1.getAnte_transfusion() == null
//							            && o1.getAnte_antibiotic() == null
//							            && o1.getAnte_externalv() == null
//							            && o1.getAnte_magsulphate() == null
//							            && o1.getAnte_diazepam() == null
//							            && o1.getAnte_antihyper() == null
//							            && o1.getAnte_hysterotomy() == null
//							            && (o1.getAnte_other() == null || o1.getAnte_other().trim().isEmpty())
//							        )
//							        ||
//							        (
//							            o1.getData_complete() == 1
//							            && !(Boolean.TRUE.equals(o1.getAnte_transfusion())
//							                || Boolean.TRUE.equals(o1.getAnte_antibiotic())
//							                || Boolean.TRUE.equals(o1.getAnte_externalv())
//							                || Boolean.TRUE.equals(o1.getAnte_magsulphate())
//							                || Boolean.TRUE.equals(o1.getAnte_diazepam())
//							                || Boolean.TRUE.equals(o1.getAnte_antihyper())
//							                || Boolean.TRUE.equals(o1.getAnte_hysterotomy()))
//							            && (o1.getAnte_other() == null || o1.getAnte_other().trim().isEmpty())
//							        );
							mdeath_ante_interv_missing = (o1.getData_complete() == null
									&& o1.getAnte_transfusion() == null && o1.getAnte_antibiotic() == null
									&& o1.getAnte_externalv() == null && o1.getAnte_magsulphate() == null
									&& o1.getAnte_diazepam() == null && o1.getAnte_antihyper() == null
									&& o1.getAnte_hysterotomy() == null
									&& (o1.getAnte_other() == null || o1.getAnte_other().trim().isEmpty()))
									|| (o1.getData_complete() == 1
											&& !(Boolean.TRUE.equals(o1.getAnte_transfusion())
													|| Boolean.TRUE.equals(o1.getAnte_antibiotic())
													|| Boolean.TRUE.equals(o1.getAnte_externalv())
													|| Boolean.TRUE.equals(o1.getAnte_magsulphate())
													|| Boolean.TRUE.equals(o1.getAnte_diazepam())
													|| Boolean.TRUE.equals(o1.getAnte_antihyper())
													|| Boolean.TRUE.equals(o1.getAnte_hysterotomy()))
											&& (o1.getAnte_other() == null || o1.getAnte_other().trim().isEmpty()));				
							
							mdeathAnteRepo.save(o1);
						}
					}

					boolean mdeath_intra_interv_missing = false;
					if (selected.getCase_mdeath() == CONSTANTS.DURING_DELIVERY) {

						case_mdeath_intra o1 = selected.getMdeath_intra();

						if (o1 != null) {
							o1.setIntra_uuid(selected.getCase_uuid());
							o1.setCase_uuid(selected);

//							mdeath_intra_interv_missing =
//							        (
//							            o1.getData_complete() == null
//							            && o1.getIntra_instrumental() == null
//							            && o1.getIntra_antibiotic() == null
//							            && o1.getIntra_caesarian() == null
//							            && o1.getIntra_hysterectomy() == null
//							            && o1.getIntra_transfusion() == null
//							            && o1.getIntra_magsulphate() == null
//							            && o1.getIntra_antihyper() == null
//							            && o1.getIntra_diazepam() == null
//							            && (o1.getIntra_other() == null || o1.getIntra_other().trim().isEmpty())
//							        )
//							        ||
//							        (
//							            o1.getData_complete() == 1
//							            && !(Boolean.TRUE.equals(o1.getIntra_instrumental())
//							                || Boolean.TRUE.equals(o1.getIntra_antibiotic())
//							                || Boolean.TRUE.equals(o1.getIntra_caesarian())
//							                || Boolean.TRUE.equals(o1.getIntra_hysterectomy())
//							                || Boolean.TRUE.equals(o1.getIntra_transfusion())
//							                || Boolean.TRUE.equals(o1.getIntra_magsulphate())
//							                || Boolean.TRUE.equals(o1.getIntra_antihyper())
//							                || Boolean.TRUE.equals(o1.getIntra_diazepam()))
//							            && (o1.getIntra_other() == null || o1.getIntra_other().trim().isEmpty())
//							        );
							mdeath_intra_interv_missing = (o1.getData_complete() == null
									&& o1.getIntra_instrumental() == null && o1.getIntra_antibiotic() == null
									&& o1.getIntra_caesarian() == null && o1.getIntra_hysterectomy() == null
									&& o1.getIntra_transfusion() == null && o1.getIntra_magsulphate() == null
									&& o1.getIntra_antihyper() == null && o1.getIntra_diazepam() == null
									&& (o1.getIntra_other() == null || o1.getIntra_other().trim().isEmpty()))
									|| (o1.getData_complete() == 1
											&& !(Boolean.TRUE.equals(o1.getIntra_instrumental())
													|| Boolean.TRUE.equals(o1.getIntra_antibiotic())
													|| Boolean.TRUE.equals(o1.getIntra_caesarian())
													|| Boolean.TRUE.equals(o1.getIntra_hysterectomy())
													|| Boolean.TRUE.equals(o1.getIntra_transfusion())
													|| Boolean.TRUE.equals(o1.getIntra_magsulphate())
													|| Boolean.TRUE.equals(o1.getIntra_antihyper())
													|| Boolean.TRUE.equals(o1.getIntra_diazepam()))
											&& (o1.getIntra_other() == null || o1.getIntra_other().trim().isEmpty()));
							
							mdeathIntraRepo.save(o1);

						}
					}

					boolean mdeath_postpart_interv_missing = false;
					if (selected.getCase_mdeath() == CONSTANTS.DURING_DELIVERY) {

						case_mdeath_postpart o1 = selected.getMdeath_postpart();

						if (o1 != null) {
							o1.setPostpart_uuid(selected.getCase_uuid());
							o1.setCase_uuid(selected);

//							mdeath_postpart_interv_missing =
//							        (
//							            o1.getData_complete() == null
//							            && o1.getPostpart_evacuation() == null
//							            && o1.getPostpart_antibiotic() == null
//							            && o1.getPostpart_laparotomy() == null
//							            && o1.getPostpart_hysterectomy() == null
//							            && o1.getPostpart_transfusion() == null
//							            && o1.getPostpart_magsulphate() == null
//							            && o1.getPostpart_placentaremoval() == null
//							            && o1.getPostpart_antihyper() == null
//							            && o1.getPostpart_diazepam() == null
//							            && (o1.getPostpart_other() == null || o1.getPostpart_other().trim().isEmpty())
//							        )
//							        ||
//							        (
//							            o1.getData_complete() == 1
//							            && !(Boolean.TRUE.equals(o1.getPostpart_evacuation())
//							                || Boolean.TRUE.equals(o1.getPostpart_antibiotic())
//							                || Boolean.TRUE.equals(o1.getPostpart_laparotomy())
//							                || Boolean.TRUE.equals(o1.getPostpart_hysterectomy())
//							                || Boolean.TRUE.equals(o1.getPostpart_transfusion())
//							                || Boolean.TRUE.equals(o1.getPostpart_magsulphate())
//							                || Boolean.TRUE.equals(o1.getPostpart_placentaremoval())
//							                || Boolean.TRUE.equals(o1.getPostpart_antihyper())
//							                || Boolean.TRUE.equals(o1.getPostpart_diazepam()))
//							            && (o1.getPostpart_other() == null || o1.getPostpart_other().trim().isEmpty())
//							        );
							mdeath_postpart_interv_missing = (o1.getData_complete() == null
									&& o1.getPostpart_evacuation() == null && o1.getPostpart_antibiotic() == null
									&& o1.getPostpart_laparotomy() == null && o1.getPostpart_hysterectomy() == null
									&& o1.getPostpart_transfusion() == null && o1.getPostpart_magsulphate() == null
									&& o1.getPostpart_placentaremoval() == null && o1.getPostpart_antihyper() == null
									&& o1.getPostpart_diazepam() == null
									&& (o1.getPostpart_other() == null || o1.getPostpart_other().trim().isEmpty()))
									|| (o1.getData_complete() == 1
											&& !(Boolean.TRUE.equals(o1.getPostpart_evacuation())
													|| Boolean.TRUE.equals(o1.getPostpart_antibiotic())
													|| Boolean.TRUE.equals(o1.getPostpart_laparotomy())
													|| Boolean.TRUE.equals(o1.getPostpart_hysterectomy())
													|| Boolean.TRUE.equals(o1.getPostpart_transfusion())
													|| Boolean.TRUE.equals(o1.getPostpart_magsulphate())
													|| Boolean.TRUE.equals(o1.getPostpart_placentaremoval())
													|| Boolean.TRUE.equals(o1.getPostpart_antihyper())
													|| Boolean.TRUE.equals(o1.getPostpart_diazepam()))
											&& (o1.getPostpart_other() == null
													|| o1.getPostpart_other().trim().isEmpty()));					
							
							mdeathPostpartRepo.save(o1);

						}
					}
					
					//added by feyisa

					if (selected.getCase_mdeath() == CONSTANTS.AFTER_DELIVERY) { 

						case_mdeath_postpart o1 = selected.getMdeath_postpart();

						if (o1 == null) {
							o1 = new case_mdeath_postpart();
							o1.setPostpart_uuid(selected.getCase_uuid());
							o1.setCase_uuid(selected);
							selected.setMdeath_postpart(o1);
						} else {
							o1.setPostpart_uuid(selected.getCase_uuid());
							o1.setCase_uuid(selected);
						}

						mdeath_postpart_interv_missing = (o1.getData_complete() == null
								&& o1.getPostpart_evacuation() == null
								&& o1.getPostpart_antibiotic() == null
								&& o1.getPostpart_laparotomy() == null
								&& o1.getPostpart_hysterectomy() == null
								&& o1.getPostpart_transfusion() == null
								&& o1.getPostpart_magsulphate() == null
								&& o1.getPostpart_placentaremoval() == null
								&& o1.getPostpart_antihyper() == null
								&& o1.getPostpart_diazepam() == null
								&& (o1.getPostpart_other() == null || o1.getPostpart_other().trim().isEmpty()))
								|| (o1.getData_complete() == 1
										&& !(Boolean.TRUE.equals(o1.getPostpart_evacuation())
												|| Boolean.TRUE.equals(o1.getPostpart_antibiotic())
												|| Boolean.TRUE.equals(o1.getPostpart_laparotomy())
												|| Boolean.TRUE.equals(o1.getPostpart_hysterectomy())
												|| Boolean.TRUE.equals(o1.getPostpart_transfusion())
												|| Boolean.TRUE.equals(o1.getPostpart_magsulphate())
												|| Boolean.TRUE.equals(o1.getPostpart_placentaremoval())
												|| Boolean.TRUE.equals(o1.getPostpart_antihyper())
												|| Boolean.TRUE.equals(o1.getPostpart_diazepam()))
										&& (o1.getPostpart_other() == null || o1.getPostpart_other().trim().isEmpty()));
						
						mdeathPostpartRepo.save(o1);
					}
					
					
					
					
					boolean mdeath_other_interv_missing = false;
					{
						case_mdeath_other o1 = selected.getMdeath_other();

						if (o1 != null) {
							o1.setOther_uuid(selected.getCase_uuid());
							o1.setCase_uuid(selected);

//							mdeath_other_interv_missing =
//							        (
//							            o1.getData_complete() == null
//							            && o1.getOther_anaesthga() == null
//							            && o1.getOther_epidural() == null
//							            && o1.getOther_spinal() == null
//							            && o1.getOther_local() == null
//							            && o1.getOther_invasive() == null
//							            && o1.getOther_antihyper() == null
//							            && o1.getOther_icuventilation() == null
//							            && (o1.getNew_intervention() == null
//							                || o1.getNew_intervention().trim().isEmpty())
//							        )
//							        ||
//							        (
//							            o1.getData_complete() == 1
//							            && !(Boolean.TRUE.equals(o1.getOther_anaesthga())
//							                || Boolean.TRUE.equals(o1.getOther_epidural())
//							                || Boolean.TRUE.equals(o1.getOther_spinal())
//							                || Boolean.TRUE.equals(o1.getOther_local())
//							                || Boolean.TRUE.equals(o1.getOther_invasive())
//							                || Boolean.TRUE.equals(o1.getOther_antihyper())
//							                || Boolean.TRUE.equals(o1.getOther_icuventilation()))
//							            && (o1.getNew_intervention() == null
//							                || o1.getNew_intervention().trim().isEmpty())
//							        );
							mdeath_other_interv_missing = (o1.getData_complete() == null
									&& o1.getOther_anaesthga() == null && o1.getOther_epidural() == null
									&& o1.getOther_spinal() == null && o1.getOther_local() == null
									&& o1.getOther_invasive() == null && o1.getOther_antihyper() == null
									&& o1.getOther_icuventilation() == null
									&& (o1.getNew_intervention() == null || o1.getNew_intervention().trim().isEmpty()))
									|| (o1.getData_complete() == 1
											&& !(Boolean.TRUE.equals(o1.getOther_anaesthga())
													|| Boolean.TRUE.equals(o1.getOther_epidural())
													|| Boolean.TRUE.equals(o1.getOther_spinal())
													|| Boolean.TRUE.equals(o1.getOther_local())
													|| Boolean.TRUE.equals(o1.getOther_invasive())
													|| Boolean.TRUE.equals(o1.getOther_antihyper())
													|| Boolean.TRUE.equals(o1.getOther_icuventilation()))
											&& (o1.getNew_intervention() == null
													|| o1.getNew_intervention().trim().isEmpty()));						
							
							mdeathOtherRepo.save(o1);
						}
					}

					boolean mdeath_datetime_expected = (o.getMdeath_datetime_notstated() == null
							|| o.getMdeath_datetime_notstated() == 0);

					boolean mdeath_datetime_any_specified = o.getMdeath_hour() != null || o.getMdeath_date() != null
							|| o.getMdeath_minute() != null || o.getMdeath_time() != null;

					boolean mdeath_datetime_any_missing = o.getMdeath_hour() == null || o.getMdeath_date() == null
							|| o.getMdeath_minute() == null || o.getMdeath_time() == null;

					boolean mdeath_autopsy_missing = o.getMdeath_autopsy() == null;

					boolean mdeath_autopsy_expected = (o.getMdeath_autopsy() != null && o.getMdeath_autopsy() == 1)
							&& (o.getMdeath_autopsy_date() == null || o.getMdeath_autopsy_location() == null
									|| o.getMdeath_autopsy_location().trim().isEmpty()
									|| o.getMdeath_autopsy_by() == null || o.getMdeath_autopsy_final_cod() == null
									|| o.getMdeath_autopsy_final_cod().trim().isEmpty()
									|| o.getMdeath_autopsy_antec_cod() == null
									|| o.getMdeath_autopsy_antec_cod().trim().isEmpty()
									|| o.getMdeath_autopsy_ops_cod() == null
									|| o.getMdeath_autopsy_ops_cod().trim().isEmpty());
//									|| o.getMdeath_autopsy_icd_mm() == null);

					if ((mdeath_datetime_expected && mdeath_datetime_any_missing)
							|| (!mdeath_datetime_expected && mdeath_datetime_any_specified)
							|| (mdeath_early_interv_missing || mdeath_ante_interv_missing || mdeath_intra_interv_missing
									|| mdeath_postpart_interv_missing || mdeath_other_interv_missing
									|| mdeath_autopsy_missing || mdeath_autopsy_expected)) {

						o.setData_complete(0);
					} else {
						o.setData_complete(1);
						if (!mdeath_early_interv_missing) {
							case_mdeath_early o1 = selected.getMdeath_early();
							if (o1 != null) {
								final String arrayToJson = objectMapper.writeValueAsString(processListOf(o1));
								o1.setEarly_json(arrayToJson);
							}
						}
						if (!mdeath_ante_interv_missing) {
							case_mdeath_ante o1 = selected.getMdeath_ante();
							if (o1 != null) {
								final String arrayToJson = objectMapper.writeValueAsString(processListOf(o1));
								o1.setAnte_json(arrayToJson);
							}
						}
						if (!mdeath_intra_interv_missing) {
							case_mdeath_intra o1 = selected.getMdeath_intra();
							if (o1 != null) {
								final String arrayToJson = objectMapper.writeValueAsString(processListOf(o1));
								o1.setIntra_json(arrayToJson);
							}
						}
						if (!mdeath_postpart_interv_missing) {
							case_mdeath_postpart o1 = selected.getMdeath_postpart();
							if (o1 != null) {
								final String arrayToJson = objectMapper.writeValueAsString(processListOf(o1));
								o1.setPostpart_json(arrayToJson);
							}
						}
						if (!mdeath_other_interv_missing) {
							case_mdeath_other o1 = selected.getMdeath_other();
							if (o1 != null) {
								final String arrayToJson = objectMapper.writeValueAsString(processListOf(o1));
								o1.setOther_json(arrayToJson);
							}
						}

						final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
						o.setMdeath_json(arrayToJson);
					}

					mdeathRepo.save(o);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}

			}
			break;
		}
		case 8: {

			try {

				case_notes o = selected.getNotes();

				MultipartFile file = o.getFile();
				o.setBase64image(null);

				try {
					if (file != null && file.getBytes() != null && file.getBytes().length > 0) {
						o.setNotes_file(file.getBytes());
						o.setNotes_filetype(file.getContentType());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

				final String arrayToJson = objectMapper.writeValueAsString(processListOf(o));
				o.setNotes_json(arrayToJson);

				notRepo.save(o);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			break;
		}
		default: {
			selected.setCase_status(0);
			caseRepo.save(selected);
			break;
		}
		}

		return "redirect:/registry/edit/" + selected.getCase_uuid() + "?page="
				+ ((go != null && page < 9) ? ++page : page) + "&success=yes";
	}

	@GetMapping("/submit/{id}")
	public String submit(Principal principal, Model model, @PathVariable("id") String case_uuid) {

		if (!syncRepo.findById(CONSTANTS.LICENSE_ID).isPresent()) {
			model.addAttribute("activated", "0");
			return "home";
		}

		sync_table synctable = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		model.addAttribute("myf", synctable.getSync_name());

		case_identifiers selected = caseRepo.findById(case_uuid).get();

		model.addAttribute("selected", selected);

		return "registry/case-submit";
	}

	@PostMapping("/submit/{id}")
	public String submit(Principal principal, @PathVariable("id") String case_uuid) throws MessagingException {
		case_identifiers selected = caseRepo.findById(case_uuid).get();
		selected.setCase_status(1);

		caseRepo.save(selected);
		String datetoShow = "";
		String childsex="";
		String deathdate="";
		SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
		if (selected.getCase_death() == 1) {

			if (selected.getDelivery().getDelivery_date() == null) {
				//datetoShow = "<b>Child's sex:</b> " + getAnswer("sex_options", selected.getBiodata().getBiodata_sex())
				//		+ "<br><b>Date of death:</b> " + "Not stated";
				childsex=getAnswer("sex_options", selected.getBiodata().getBiodata_sex());
				deathdate="Not stated";
				
			} else {
				//datetoShow = "<b>Child's sex:</b> " + getAnswer("sex_options", selected.getBiodata().getBiodata_sex())
				//		+ "<br><b>Date of death:</b> " + df.format(selected.getDelivery().getDelivery_date());
				childsex=getAnswer("sex_options", selected.getBiodata().getBiodata_sex());
				deathdate=df.format(selected.getDelivery().getDelivery_date());
			}
		} else if (selected.getCase_death() == 2) {
			if (selected.getBabydeath().getBaby_ddate() == null) {
				//datetoShow = "<b>Child's sex:</b> " + getAnswer("sex_options", selected.getBiodata().getBiodata_sex())
				//		+ "<br><b>Date of death:</b> " + "Not stated";
				childsex=getAnswer("sex_options", selected.getBiodata().getBiodata_sex());
				deathdate="Not stated";
				
			} else {
				//datetoShow = "<b>Child's sex:</b> " + getAnswer("sex_options", selected.getBiodata().getBiodata_sex())
				//		+ "<br><b>Date of death:</b> " + df.format(selected.getBabydeath().getBaby_ddate());
				childsex=getAnswer("sex_options", selected.getBiodata().getBiodata_sex());
				deathdate=df.format(selected.getBabydeath().getBaby_ddate());
			}
		} else if (selected.getCase_death() == 3) {
			if (selected.getMdeath().getMdeath_date() == null) {
				//datetoShow = "<br><b>Date of death:</b> " + "Not stated";
				childsex="-";
				deathdate="Not stated";
			} else {
				//datetoShow = "<br><b>Date of death:</b> " + df.format(selected.getMdeath().getMdeath_date());
				childsex="-";
				deathdate=df.format(selected.getMdeath().getMdeath_date());
			}
		}

		try {
			if (InternetAvailabilityChecker.isInternetAvailable()) {

				sync_table sync = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
				//String[] recipients = {
					//    "feyisa142@gmail.com",
					//    "fgelashe@hararghe.org"
					//};
				String htmlBody =
					    "<p>Hello dear all,</p>" +
					    "<p>I hope this email finds you well.</p>" +

					    "<p>This is to notify you of <b>"
					    + getAnswer("death_options", selected.getCase_death())
					    + ".  The detail is provided in the table below.</b></p>" +

					    "<table border='1' cellpadding='8' cellspacing='0' " +
					    "style='border-collapse:collapse; width:100%; font-family:Arial, sans-serif; text-align:center;'>" +

					        "<tr style='background-color:#f2f2f2; font-weight:bold;'>" +
					            "<th style='border:1px solid #000; text-align:center;'>Mother's Age</th>" +
					            "<th style='border:1px solid #000; text-align:center;'>Child's Sex</th>" +
					            "<th style='border:1px solid #000; text-align:center;'>Date of Death</th>" +
					            "<th style='border:1px solid #000; text-align:center;'>Mother's Address</th>" +
					            "<th style='border:1px solid #000; text-align:center;'>Mother's ID</th>" +
					            "<th style='border:1px solid #000; text-align:center;'>HDSS Location</th>" +
					            "<th style='border:1px solid #000; text-align:center;'>Health Facility</th>" +
					        "</tr>" +

					        "<tr>" +
					            "<td style='border:1px solid #000; text-align:center;'>" + selected.getBiodata().getBiodata_mage() + "</td>" +
					            "<td style='border:1px solid #000; text-align:center;'>" + childsex + "</td>" +
					            "<td style='border:1px solid #000; text-align:center;'>" + deathdate + "</td>" +
					            "<td style='border:1px solid #000; text-align:center;'>" + selected.getBiodata().getBiodata_location() + "</td>" +
					            "<td style='border:1px solid #000; text-align:center;'>" + selected.getCase_mid() + "</td>" +
					            "<td style='border:1px solid #000; text-align:center;'>" + selected.getBiodata().getBiodata_hdssid() + "</td>" +
					            "<td style='border:1px solid #000; text-align:center;'>" + sync.getSync_name() + " - " + sync.getSync_code() + "</td>" +
					        "</tr>" +

					    "</table>" +

					    "<hr/>" +

                        "<p style='font-size:14px; color:#555555; line-height:1.5; font-style:italic;'>" 
					    + "This is a <b>pilot implementation</b> of the Enhanced Automated MPDSR tool "
					    + "developed by Eliezer and Feyisa (customization)."
					    + "</p>" +

					    "<p><b>Best Regards</b>,<br/><b>PDSR ETHIOPIA.</b></p>";

				emailService.sendHtmlEmailf(getRecipients(), "MPDSR DEATH NOTIFICATION!",htmlBody);
				//emailService.sendSimpleMessage(getRecipients(), "MPDSR DEATH NOTIFICATION!",
					//	"Hello, \nThis is is to notify you of a " + getAnswer("death_options", selected.getCase_death())
					//			+ "\nMother's age: " + selected.getBiodata().getBiodata_mage() + datetoShow
						//		+ "\nHealth Facility: " + sync.getSync_name() + " - " + sync.getSync_code()
						//		+ "\nThis is a PILOT IMPLEMENTATION of the Enhanced Automated MPDSR tool developed by Alex and Eliezer");
			}
		} catch (IOException e) {
		}
		//sync_table sync = syncRepo.findById(CONSTANTS.LICENSE_ID).get();
		return "redirect:/registry?page=1&success=yes";
	}

	private Integer validateTheTimesOnReferralPage(Model model, BindingResult results, case_identifiers selected,
			case_identifiers existing) {
		// validate
		final Date referralDate = selected.getReferral().getReferral_date();
		final Date arrivalDate = selected.getReferral().getReferral_adate();
		if (referralDate != null && arrivalDate != null) {

			if (arrivalDate.before(referralDate)) {
				results.rejectValue("referral.referral_adate", "error.date.arrived.before.referral");
				return 1;

			} else if (arrivalDate.compareTo(referralDate) == 0) {
				final Date referralTime = selected.getReferral().getReferral_time();
				final Date arrivalTime = selected.getReferral().getReferral_atime();

				if (referralTime != null && arrivalTime != null) {
					final Integer referralHour = selected.getReferral().getReferral_hour();
					final Integer referralMins = selected.getReferral().getReferral_minute();

					final Integer arrivalHour = selected.getReferral().getReferral_ahour();
					final Integer arrivalMins = selected.getReferral().getReferral_aminute();

					if (((arrivalHour * 60) + arrivalMins) <= ((referralHour * 60) + referralMins)) {
						results.rejectValue("referral.referral_atime", "error.time.arrived.before.referral");
						return 2;
					}
				}
			}

		}

		if (existing.getLabour() != null) {
			final Date seenDate = existing.getLabour().getLabour_seedate();

			if (arrivalDate != null && seenDate != null) {

				if (seenDate.before(arrivalDate)) {
					results.rejectValue("referral.referral_adate", "error.date.seen.before.arrival");
					return 3;
				} else if (arrivalDate.compareTo(seenDate) == 0) {
					final Date arrivalTime = selected.getReferral().getReferral_time();
					final Date seenTime = existing.getLabour().getLabour_seetime();

					if (arrivalTime != null && seenTime != null) {
						final Integer arrivalHour = selected.getReferral().getReferral_hour();
						final Integer arrivalMins = selected.getReferral().getReferral_minute();

						final Integer seenHour = existing.getLabour().getLabour_seehour();
						final Integer seenMins = existing.getLabour().getLabour_seeminute();

						if (((seenHour * 60) + seenMins) <= ((arrivalHour * 60) + arrivalMins)) {
							results.rejectValue("referral.referral_atime", "error.time.seen.before.arrival");
							return 4;
						}
					}
				}

			}
		}

		return 0;

	}

	private Integer validateTheTimesOnLabourPage(Model model, BindingResult results, case_identifiers selected,
			case_identifiers existing) {
		// validate
		final Date arrivalDate = existing.getReferral().getReferral_adate();
		final Date seenDate = selected.getLabour().getLabour_seedate();

		if (arrivalDate != null && seenDate != null) {

			if (seenDate.before(arrivalDate)) {
				results.rejectValue("labour.labour_seedate", "error.date.seen.before.arrival");
				return 1;
			} else if (seenDate.compareTo(arrivalDate) == 0) {
				final Date arrivalTime = existing.getReferral().getReferral_atime();
				final Date seenTime = selected.getLabour().getLabour_seetime();

				if (arrivalTime != null && seenTime != null) {
					final Integer arrivalHour = existing.getReferral().getReferral_ahour();
					final Integer arrivalMins = existing.getReferral().getReferral_aminute();

					final Integer seenHour = selected.getLabour().getLabour_seehour();
					final Integer seenMins = selected.getLabour().getLabour_seeminute();

					if (((seenHour * 60) + seenMins) <= ((arrivalHour * 60) + arrivalMins)) {
						results.rejectValue("labour.labour_seetime", "error.time.seen.before.arrival");
						return 2;
					}
				}
			}

		}

		return 0;

	}

	private Integer validateTheTimesOnArrivalAndDelivery(Model model, BindingResult results, case_identifiers selected,
			case_identifiers existing) {
		// validate
		final Date arrivalDate = existing.getLabour().getLabour_seedate();
		final Date deliveryDate = selected.getDelivery().getDelivery_date();
		final boolean isFacilityDelivery = selected.getDelivery().getDelivery_occured_facility() != null
				&& selected.getDelivery().getDelivery_occured_facility() == 1;

		if (arrivalDate != null && deliveryDate != null) {

			if (isFacilityDelivery && deliveryDate.before(arrivalDate)) {
				results.rejectValue("delivery.delivery_date", "error.date.delivered.before.arrival");
				return 1;
			}

			if (!isFacilityDelivery && arrivalDate.before(deliveryDate)) {
				results.rejectValue("delivery.delivery_date", "error.date.arrived.before.delivery");
				return 1;
			}

			if (deliveryDate.compareTo(arrivalDate) == 0) {
				final Date arrivalTime = existing.getLabour().getLabour_seetime();
				final Date deliveryTime = selected.getDelivery().getDelivery_time();

				if (arrivalTime != null && deliveryTime != null) {
					final Integer arrivalHour = existing.getLabour().getLabour_seehour();
					final Integer arrivalMins = existing.getLabour().getLabour_seeminute();

					final Integer deliveryHour = selected.getDelivery().getDelivery_hour();
					final Integer deliveryMins = selected.getDelivery().getDelivery_minute();

					if (isFacilityDelivery
							&& ((deliveryHour * 60) + deliveryMins) <= ((arrivalHour * 60) + arrivalMins)) {
						results.rejectValue("delivery.delivery_time", "error.time.delivered.before.arrival");
						return 2;
					}

					if (!isFacilityDelivery
							&& ((arrivalHour * 60) + arrivalMins) <= ((deliveryHour * 60) + deliveryMins)) {
						results.rejectValue("delivery.delivery_time", "error.time.arrived.before.delivery");
						return 2;
					}
				}
			}

		}

		return 0;

	}

	private Integer validateTheTimesOnDeliveryPage(Model model, BindingResult results, case_identifiers selected,
			case_identifiers existing) {
		// validate
		final Date csdecisionDate = existing.getBirth().getBirth_csproposedate();
		final Date deliveryDate = selected.getDelivery().getDelivery_date();

		if (deliveryDate != null && csdecisionDate != null) {

			if (deliveryDate.before(csdecisionDate)) {
				results.rejectValue("delivery.delivery_date", "error.date.delivery.before.csdecision");
				return 3;
			} else if (deliveryDate.compareTo(csdecisionDate) == 0) {
				final Date csdecisionTime = existing.getBirth().getBirth_csproposetime();
				final Date deliveryTime = selected.getDelivery().getDelivery_time();

				if (csdecisionTime != null && deliveryTime != null) {
					final Integer csdecisionHour = existing.getBirth().getBirth_csproposehour();
					final Integer csdecisionMins = existing.getBirth().getBirth_csproposeminute();

					final Integer deliveryHour = selected.getDelivery().getDelivery_hour();
					final Integer deliveryMins = selected.getDelivery().getDelivery_minute();

					if (((deliveryHour * 60) + deliveryMins) <= ((csdecisionHour * 60) + csdecisionMins)) {
						results.rejectValue("delivery.delivery_time", "error.time.delivery.before.csdecision");
						return 4;
					}
				}
			}

		}

		if (existing.getBabydeath() != null) {

			final Date deathDate = existing.getBabydeath().getBaby_ddate();

			if (deathDate != null && deliveryDate != null) {

				if (deathDate.before(deliveryDate)) {
					results.rejectValue("delivery.delivery_date", "error.date.death.before.delivery");
					return 5;
				} else if (deathDate.compareTo(deliveryDate) == 0) {
					final Date deliveryTime = selected.getDelivery().getDelivery_time();
					final Date deathTime = existing.getBabydeath().getBaby_dtime();

					if (deliveryTime != null && deathTime != null) {
						final Integer deliveryHour = selected.getDelivery().getDelivery_hour();
						final Integer deliveryMins = selected.getDelivery().getDelivery_minute();

						final Integer deathHour = existing.getBabydeath().getBaby_dhour();
						final Integer deathMins = existing.getBabydeath().getBaby_dminute();

						if (((deathHour * 60) + deathMins) <= ((deliveryHour * 60) + deliveryMins)) {
							results.rejectValue("delivery.delivery_time", "error.time.death.before.delivery");
							return 6;
						}
					}
				}
			}
		}

		return 0;

	}

	private Integer validateTheTimesOnBirthPage(Model model, BindingResult results, case_identifiers selected,
			case_identifiers existing) {
		// validate
		final Date csdecisionDate = selected.getBirth().getBirth_csproposedate();
		final Date deliveryDate = existing.getDelivery().getDelivery_date();

		if (deliveryDate != null && csdecisionDate != null) {
			if (deliveryDate.before(csdecisionDate)) {
				results.rejectValue("birth.birth_csproposedate", "error.date.delivery.before.csdecision");
				return 1;
			} else if (deliveryDate.compareTo(csdecisionDate) == 0) {
				final Date csdecisionTime = selected.getBirth().getBirth_csproposetime();
				final Date deliveryTime = existing.getDelivery().getDelivery_time();

				if (csdecisionTime != null && deliveryTime != null) {
					final Integer csdecisionHour = selected.getBirth().getBirth_csproposehour();
					final Integer csdecisionMins = selected.getBirth().getBirth_csproposeminute();

					final Integer deliveryHour = existing.getDelivery().getDelivery_hour();
					final Integer deliveryMins = existing.getDelivery().getDelivery_minute();

					if (((deliveryHour * 60) + deliveryMins) < ((csdecisionHour * 60) + csdecisionMins)) {
						results.rejectValue("birth.birth_csproposetime", "error.time.delivery.before.csdecision");
						return 2;
					}
				}
			}

		}

		return 0;

	}

	private Integer validateTheTimesOnBabydeathPage(Model model, BindingResult results, case_identifiers selected,
			case_identifiers existing) {
		// validate
		final Date deliveryDate = existing.getDelivery().getDelivery_date();
		final Date deathDate = selected.getBabydeath().getBaby_ddate();

		if (deathDate != null && deliveryDate != null) {

			if (deathDate.before(deliveryDate)) {
				results.rejectValue("babydeath.baby_ddate", "error.date.death.before.delivery");
				return 1;
			} else if (deathDate.compareTo(deliveryDate) == 0) {
				final Date deliveryTime = existing.getDelivery().getDelivery_time();
				final Date deathTime = selected.getBabydeath().getBaby_dtime();

				if (deliveryTime != null && deathTime != null) {
					final Integer deliveryHour = existing.getDelivery().getDelivery_hour();
					final Integer deliveryMins = existing.getDelivery().getDelivery_minute();

					final Integer deathHour = selected.getBabydeath().getBaby_dhour();
					final Integer deathMins = selected.getBabydeath().getBaby_dminute();

					if (((deathHour * 60) + deathMins) <= ((deliveryHour * 60) + deliveryMins)) {
						results.rejectValue("babydeath.baby_dtime", "error.time.death.before.delivery");
						return 2;
					}
				}
			}

		}

		return 0;
	}

	private Integer validateTheTimesOnMdeathPage(Model model, BindingResult results, case_identifiers selected,
			case_identifiers existing) {
		// validate
		final Date deliveryDate = existing.getDelivery().getDelivery_date();
		final Date deathDate = selected.getMdeath().getMdeath_date();

		if (deathDate != null && deliveryDate != null) {

			if (deathDate.before(deliveryDate)) {
				results.rejectValue("mdeath.mdeath_date", "error.date.death.before.delivery");
				return 1;
			} else if (deathDate.compareTo(deliveryDate) == 0) {
				final Date deliveryTime = existing.getDelivery().getDelivery_time();
				final Date deathTime = selected.getBabydeath().getBaby_dtime();

				if (deliveryTime != null && deathTime != null) {
					final Integer deliveryHour = existing.getDelivery().getDelivery_hour();
					final Integer deliveryMins = existing.getDelivery().getDelivery_minute();

					final Integer deathHour = selected.getBabydeath().getBaby_dhour();
					final Integer deathMins = selected.getBabydeath().getBaby_dminute();

					if (((deathHour * 60) + deathMins) <= ((deliveryHour * 60) + deliveryMins)) {
						results.rejectValue("mdeath.mdeath_time", "error.time.death.before.delivery");
						return 2;
					}
				}
			}

		}

		return 0;
	}

	@GetMapping(value = "/icdselect")
	public @ResponseBody Set<icd_diagnoses> findByDiagnosis(@RequestParam(value = "q", required = true) String search) {

		Set<icd_diagnoses> diaglist = icddRepo.findByDiagnosese("%" + search + "%");

		return diaglist;
	}

	private String[] getRecipients() {
		List<String> recipientList = userRepo.findByUser_alerted(true);
		if (recipientList == null) {
			recipientList = new ArrayList<>();
		}
		recipientList.add("dechasaadare@gmail.com");
		recipientList.add("garihunduma@gmail.com");
		// , "thailegebriel@unicef.org",
		// "pwobil@unicef.org", "mkim@unicef.org" };

		return recipientList.toArray(new String[recipientList.size()]);

	}

	@ModelAttribute("death_options")
	public Map<Integer, String> deathOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("death_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("mdeath_options")
	public Map<Integer, String> mdeathOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("mdeath_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("sex_options")
	public Map<Integer, String> sexOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("sex_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}
	@ModelAttribute("litracy_options")
	public Map<Integer, String> litracyOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("litracy_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}
	
	@ModelAttribute("mage_options")
	public Map<Integer, String> mageOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("mage_options")) {
			map.put(elem.getMap_value(), elem.getMap_value() + " " + elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("edu_options")
	public Map<Integer, String> eduOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("edu_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("work_options")
	public Map<Integer, String> workOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("work_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}
	
	@ModelAttribute("residency_options")
	public Map<Integer, String> residencyOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("residency_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("marital_options")
	public Map<Integer, String> maritalOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("marital_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("religion_options")
	public Map<Integer, String> religionOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("religion_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("ethnic_options")
	public Map<Integer, String> ethnicOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("ethnic_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("pweeks_options")
	public Map<Integer, String> pweeksOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		for (datamap elem : mapRepo.findByMap_feature("pweeks_options")) {
			map.put(elem.getMap_value(), elem.getMap_value() + " " + elem.getMap_label());
		}

		return new TreeMap<Integer, String>(map);
	}

	@ModelAttribute("pdays_options")
	public Map<Integer, String> pdaysOptionsSelectOne() {
		final Map<Integer, String> map = new HashMap<>();

		for (datamap elem : mapRepo.findByMap_feature("pdays_options")) {
			map.put(elem.getMap_value(), elem.getMap_value() + " " + elem.getMap_label());
		}

		return new TreeMap<Integer, String>(map);
	}

	@ModelAttribute("ptype_options")
	public Map<Integer, String> ptypeOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("ptype_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("frequency_options")
	public Map<Integer, String> frequencyOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("frequency_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("yesnodk_options")
	public Map<Integer, String> yesnodkOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("yesnodk_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}
	
	@ModelAttribute("autopsyby_options")
	public Map<Integer, String> autopsyby_optionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("autopsyby_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("yesnodkna_options")
	public Map<Integer, String> yesnodknaOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("yesnodkna_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("hdssyn_options")
	public Map<Integer, String> hdssynOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("hdssyn_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}
	
	@ModelAttribute("birth_optionsyn")
	public Map<Integer, String> birthoptionsynSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("birth_optionsyn")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}
	
	@ModelAttribute("hiv_options")
	public Map<Integer, String> hivOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("hiv_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("patient_options")
	public Map<Integer, String> patientOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("patient_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("source_options")
	public Map<Integer, String> sourceOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("source_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("trans_options")
	public Map<Integer, String> transOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("trans_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("period_options")
	public Map<Integer, String> periodOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		map.put(1, getQuestion("shift.morning"));
		map.put(3, getQuestion("shift.afternoon"));
		map.put(6, getQuestion("shift.night"));
		map.put(88, "Not Stated");

		return map;
	}

	@ModelAttribute("mode_options")
	public Map<Integer, String> modeOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("mode_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("attendno_options")
	public Map<Integer, String> attendnoOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (int i = 1; i < 25; i++) {
			map.put(i, i + "");
		}
		map.put(88, "Not stated");
		map.put(99, "Not Applicable");

		return map;
	}

	@ModelAttribute("gravipara_options")
	public Map<Integer, String> graviparaOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (int i = 1; i < 25; i++) {
			map.put(i, i + "");
		}
		map.put(88, "Not stated");

		return map;
	}
	@ModelAttribute("para_options")
	public Map<Integer, String> paraOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (int i = 0; i < 25; i++) {
			map.put(i, i + "");
		}
		map.put(88, "Not stated");

		return map;
	}
	
	@ModelAttribute("hour_options")
	public Map<Integer, String> hourOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (int i = 0; i < 25; i++) {
			map.put(i, i + " hour(s)");
		}
		map.put(88, "Not stated");
		map.put(99, "Not Applicable");

		return map;
	}

	@ModelAttribute("minute_options")
	public Map<Integer, String> minuteOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (int i = 0; i < 60; i++) {
			map.put(i, i + " minute(s)");
		}
		map.put(88, "Not stated");
		map.put(99, "Not Applicable");

		return map;
	}

	@ModelAttribute("startmode_options")
	public Map<Integer, String> startmodeOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("startmode_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("provider_options")
	public Map<Integer, String> providerOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("provider_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("birthloc_options")
	public Map<Integer, String> birthlocOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("birthloc_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("liqourvolume_options")
	public Map<Integer, String> liqourvolumeOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("liqourvolume_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("liqourcolor_options")
	public Map<Integer, String> liqourcolorOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("liqourcolor_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("liqourodour_options")
	public Map<Integer, String> liqourodourOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("liqourodour_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("babyoutcome_options")
	public Map<Integer, String> babyoutcomeOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		List<datamap> datamap = mapRepo.findByMap_feature("babyoutcome_options");
		datamap.addAll(mapRepo.findByMap_feature("babyoutcome_options_ext"));
		datamap.sort(new Comparator<datamap>() {
			public int compare(datamap a, datamap b) {
				return a.getMap_value() - b.getMap_value();
			}
		});

		for (datamap elem : datamap) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("babyoutcome_options_ext")
	public Map<Integer, String> babyoutcomeOptionsExtSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		List<datamap> datamap = mapRepo.findByMap_feature("babyoutcome_options");
		datamap.addAll(mapRepo.findByMap_feature("babyoutcome_options"));
		datamap.sort(new Comparator<datamap>() {
			public int compare(datamap a, datamap b) {
				return a.getMap_value() - b.getMap_value();
			}
		});

		for (datamap elem : datamap) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("motheroutcome_options")
	public Map<Integer, String> motheroutcomeOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("motheroutcome_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("neocod_options")
	public Map<Integer, String> neocodOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("neocod_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("lastheard_options")
	public Map<Integer, String> lastheardOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("lastheard_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	@ModelAttribute("apgar_options")
	public Map<Integer, String> apgarOptionsSelectOne() {
		final Map<Integer, String> map = new LinkedHashMap<>();

		map.put(null, "Select one");
		for (datamap elem : mapRepo.findByMap_feature("apgar_options")) {
			map.put(elem.getMap_value(), elem.getMap_label());
		}

		return map;
	}

	private String getQuestion(String code) {
		return msg.getMessage(code, null, Locale.getDefault());
	}

	private String getAnswer(String feature, Integer value) {
		if (value == null) {
			return "";
		}

		if (mapRepo.findById(new datamapPK(feature, value)).isPresent()) {
			return mapRepo.findById(new datamapPK(feature, value)).get().getMap_label();
		} else if (mapRepo.findById(new datamapPK(feature + "_ext", value)).isPresent()) {
			return mapRepo.findById(new datamapPK(feature + "_ext", value)).get().getMap_label();
		}

		return "No answer";
	}

	private String getAnswer(String feature, Boolean value) {
		if (value == null) {
			return "Never Selected";
		}

		if (value = Boolean.TRUE)
			return "Yes";
		else if (value = Boolean.FALSE)
			return "No";

		return "Never Selected";
	}

	private List<json_data> processListOf(case_biodata o) {
		List<json_data> list = Stream.of(
				new json_data(getQuestion("label.biodata_mdob"),
						(o.getBiodata_mdob() == null) ? "Not Stated"
								: new SimpleDateFormat("dd-MMM-yyyy").format(o.getBiodata_mdob()),
						true),
				new json_data(getQuestion("label.biodata_mage"), o.getBiodata_mage() + getQuestion("txt.years"), true),
				new json_data(getQuestion("label.biodata_medu"), getAnswer("edu_options", o.getBiodata_medu()), true),
				new json_data(getQuestion("label.biodata_maddress"), o.getBiodata_maddress(), true),
				new json_data(getQuestion("label.biodata_location"), o.getBiodata_location(), true),
				new json_data(getQuestion("label.biodata_contact"), o.getBiodata_contact(), true),
				new json_data(getQuestion("label.biodata_work"), getAnswer("work_options", o.getBiodata_work()), true),
				new json_data(getQuestion("label.biodata_marital"),
						getAnswer("marital_options", o.getBiodata_marital()), true),
				new json_data(getQuestion("label.biodata_religion"),
						getAnswer("religion_options", o.getBiodata_religion()), true),
				new json_data(getQuestion("label.biodata_ethnic"), getAnswer("ethnic_options", o.getBiodata_ethnic()),
						true),
				new json_data(getQuestion("label.biodata_sex"), getAnswer("sex_options", o.getBiodata_sex()),
						(o.getCase_uuid().getCase_death() != CONSTANTS.MATERNAL_DEATH)))
				.collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_pregnancy o) {
		List<json_data> list = Stream.of(
				new json_data(getQuestion("label.pregnancy_gest"),
						o.getPregnancy_weeks() + getQuestion("txt.weeks") + " and " + o.getPregnancy_days()
								+ getQuestion("txt.days"),
						true),
				new json_data(getQuestion("label.pregnancy_type"), getAnswer("ptype_options", o.getPregnancy_type()),
						true))
				.collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_referral o) {
		boolean isreferral = (o.getReferral_case() != null && o.getReferral_case() == 1);
		List<json_data> list = Stream.of(
				new json_data(getQuestion("label.referral_case"), getAnswer("yesnodk_options", o.getReferral_case()),
						true),
				new json_data(getQuestion("label.referral_patient"),
						getAnswer("patient_options", o.getReferral_patient()), isreferral),
				new json_data(getQuestion("label.referral_source"), getAnswer("source_options", o.getReferral_source()),
						isreferral),
				new json_data(getQuestion("label.referral_facility"), o.getReferral_facility(), isreferral),
				new json_data(getQuestion("label.referral_datetime"),
						(o.getReferral_datetime_notstated() != null && o.getReferral_datetime_notstated() == 1)
								? "Not stated"
								: new SimpleDateFormat("dd-MMM-yyyy").format(o.getReferral_date()) + " at "
										+ new SimpleDateFormat("HH:mm a").format(o.getReferral_time()),
						isreferral),
				new json_data(getQuestion("label.referral_adatetime"),
						(o.getReferral_adatetime_notstated() != null && o.getReferral_adatetime_notstated() == 1)
								? "Not stated"
								: new SimpleDateFormat("dd-MMM-yyyy").format(o.getReferral_adate()) + " at "
										+ new SimpleDateFormat("HH:mm a").format(o.getReferral_atime()),
						isreferral),
				new json_data(getQuestion("label.referral_transport"),
						getAnswer("trans_options", o.getReferral_transport()), isreferral),
				new json_data(getQuestion("label.referral_notes"), o.getReferral_notes(), isreferral),
				new json_data(getQuestion("label.referral_file"),
						CONSTANTS.IMAGE_TAG + convertBinImageToString(o.getReferral_file()), isreferral)

		).collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_delivery o) {
		List<json_data> list = Stream.of(
				new json_data(getQuestion("label.delivery_occured"),
						getAnswer("yesnodk_options", o.getDelivery_occured()), true),
				new json_data(getQuestion("label.delivery_datetime"),
						(o.getDelivery_datetime_notstated() != null && o.getDelivery_datetime_notstated() == 1)
								? "Not stated"
//								: new SimpleDateFormat("dd-MMM-yyyy").format(o.getDelivery_date()) + " at "
//										+ new SimpleDateFormat("HH:mm a").format(o.getDelivery_time()),
										: new SimpleDateFormat("dd-MMM-yyyy").format(o.getDelivery_date())
										+ ((o.getDelivery_time() == null) ? ""
												: (" at " + new SimpleDateFormat("HH:mm a")
														.format(o.getDelivery_time()))),
						true),
				new json_data(getQuestion("label.delivery_period"), getAnswer("period_options", o.getDelivery_period()),
						true))
				.collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_antenatal o) {
		boolean isattend = o.getAntenatal_attend() == 1;
		boolean hasrisks = o.getAntenatal_risks() == 1;
		boolean hasfolic = o.getAntenatal_folicacid() == 1;
		String items = "";
		if (o.getRisks() != null && !o.getRisks().isEmpty())
			for (risk_table elem : o.getRisks()) {
				items += elem.getRisk_name() + "<br/>";
			}
		if (o.getNew_risks() != null && o.getNew_risks().trim() != "")
			items += o.getNew_risks();

		List<json_data> list = Stream.of(
				new json_data(getQuestion("label.antenatal_gravida"), "" + o.getAntenatal_gravida(), true),
				new json_data(getQuestion("label.antenatal_para"), "" + o.getAntenatal_para(), true),
				new json_data(getQuestion("label.antenatal_attend"),
						getAnswer("yesnodk_options", o.getAntenatal_attend()), true),
				new json_data(getQuestion("label.antenatal_attendno"), "" + o.getAntenatal_attendno(), isattend),
				new json_data(getQuestion("label.antenatal_facility"), "" + o.getAntenatal_facility(), isattend),
				new json_data(getQuestion("label.antenatal_gestage"),
						o.getAntenatal_weeks() + getQuestion("txt.weeks") + " and " + o.getAntenatal_days()
								+ getQuestion("txt.days"),
						isattend),
				new json_data(getQuestion("label.antenatal_risks"),
						getAnswer("yesnodk_options", o.getAntenatal_risks()), true),
				new json_data(getQuestion("label.risks"), items, hasrisks),
				new json_data(getQuestion("label.antenatal_hiv"), getAnswer("hiv_options", o.getAntenatal_hiv()), true),
				new json_data(getQuestion("label.antenatal_alcohol"),
						getAnswer("yesnodk_options", o.getAntenatal_alcohol()), true),
				new json_data(getQuestion("label.antenatal_smoker"),
						getAnswer("yesnodk_options", o.getAntenatal_smoker()), true),
				new json_data(getQuestion("label.antenatal_herbal"),
						getAnswer("yesnodk_options", o.getAntenatal_herbal()), true),
				new json_data(getQuestion("label.antenatal_folicacid"),
						getAnswer("yesnodk_options", o.getAntenatal_folicacid()), true),
				new json_data(getQuestion("label.antenatal_folicacid3m"),
						getAnswer("yesnodkna_options", o.getAntenatal_folicacid3m()), hasfolic),
				new json_data(getQuestion("label.antenatal_tetanus"),
						getAnswer("yesnodk_options", o.getAntenatal_tetanus()), true),
				new json_data(getQuestion("label.antenatal_malprophy"),
						getAnswer("yesnodk_options", o.getAntenatal_malprophy()), true)

		).collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_labour o) {
		boolean hascomplications = o.getLabour_complications() == 1;

		String items = "";
		if (o.getComplications() != null && !o.getComplications().isEmpty())
			for (complication_table elem : o.getComplications()) {
				items += elem.getComplication_name() + "<br/>";
			}
		if (o.getNew_complications() != null && o.getNew_complications().trim() != "")
			items += o.getNew_complications();

		List<json_data> list = Stream
				.of(new json_data(getQuestion("label.labour_occured"),
						getAnswer("yesnodk_options", o.getLabour_occured()), true),
						new json_data(getQuestion("label.labour_datetime"),
								(o.getLabour_seedatetime_notstated() != null
										&& o.getLabour_seedatetime_notstated() == 1)
												? "Not stated"
												: new SimpleDateFormat("dd-MMM-yyyy").format(o.getLabour_seedate())
//														+ " at "
//														+ new SimpleDateFormat("HH:mm a").format(o.getLabour_seetime()),
												+ ((o.getLabour_seetime() == null) ? ""
														: (" at " + new SimpleDateFormat("HH:mm a")
																.format(o.getLabour_seetime()))),
								true),
//						new json_data(getQuestion("label.labour_seeperiod"),
//								getAnswer("period_options", o.getLabour_seeperiod()), true),
						new json_data(getQuestion("label.labour_startmode"),
								getAnswer("startmode_options", o.getLabour_startmode()), true),
						new json_data(getQuestion("label.labour_herbalaug"),
								getAnswer("yesnodk_options", o.getLabour_herbalaug()), true),
						new json_data(getQuestion("label.labour_partograph"),
								getAnswer("yesnodkna_options", o.getLabour_partograph()), true),
						new json_data(getQuestion("label.labour_lasttime1"),
								o.getLabour_lasthour1() + getQuestion("txt.hours") + " and " + o.getLabour_lastminute1()
										+ getQuestion("txt.minutes"),
								true),
						new json_data(getQuestion("label.labour_lasttime2"),
								o.getLabour_lasthour2() + getQuestion("txt.hours") + " and " + o.getLabour_lastminute2()
										+ getQuestion("txt.minutes"),
								true),
						new json_data(getQuestion("label.labour_complications"),
								getAnswer("yesnodk_options", o.getLabour_complications()), true),
						new json_data(getQuestion("label.complications"), items, hascomplications))
				.collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_birth o) {
		boolean forcenormal = o.getBirth_insistnormal() == 1;
		boolean hasabnormal = o.getBirth_abnormalities() == 1;
		boolean hascordfaults = o.getBirth_cordfaults() == 1;
		boolean hasplacentack = o.getBirth_placentachecks() == 1;

		String items = "", items1 = "", items2 = "";
		if (o.getAbnormalities() != null && !o.getAbnormalities().isEmpty())
			for (abnormality_table elem : o.getAbnormalities()) {
				items += elem.getAbnormal_name() + "<br/>";
			}
		if (o.getNew_abnormalities() != null && o.getNew_abnormalities().trim() != "")
			items += o.getNew_abnormalities();

		if (o.getCordfaults() != null && !o.getCordfaults().isEmpty())
			for (cordfault_table elem : o.getCordfaults()) {
				items1 += elem.getCordfault_name() + "<br/>";
			}
		if (o.getNew_cordfaults() != null && o.getNew_cordfaults().trim() != "")
			items1 += o.getNew_cordfaults();

		if (o.getPlacentachecks() != null && !o.getPlacentachecks().isEmpty())
			for (placentacheck_table elem : o.getPlacentachecks()) {
				items2 += elem.getPlacentacheck_name() + "<br/>";
			}
		if (o.getNew_placentachecks() != null && o.getNew_placentachecks().trim() != "")
			items2 += o.getNew_placentachecks();

		List<json_data> list = Stream
				.of(new json_data(getQuestion("label.birth_mode"), getAnswer("mode_options", o.getBirth_mode()), true),
						new json_data(getQuestion("label.birth_insistnormal"),
								getAnswer("yesnodkna_options", o.getBirth_insistnormal()), true),
						new json_data(getQuestion("label.birth_csproposedatetime"),
								(o.getBirth_csproposetime() == null) ? "Not stated"
										: new SimpleDateFormat("HH:mm a").format(o.getBirth_csproposetime()),
								forcenormal),
						new json_data(getQuestion("label.birth_provider"),
								getAnswer("provider_options", o.getBirth_provider()), true),
						new json_data(getQuestion("label.birth_facility"),
								getAnswer("birthloc_options", o.getBirth_facility()), true),
						new json_data(getQuestion("label.birth_weight"),
								(o.getBirth_weight() == null) ? "Not stated"
										: new DecimalFormat("0.00").format(o.getBirth_weight()),
								true),

						new json_data(getQuestion("label.birth_abnormalities"),
								getAnswer("yesnodk_options", o.getBirth_abnormalities()), true),
						new json_data(getQuestion("label.abnormalities"), items, hasabnormal),
						new json_data(getQuestion("label.birth_cordfaults"),
								getAnswer("yesnodk_options", o.getBirth_cordfaults()), true),
						new json_data(getQuestion("label.cordfaults"), items1, hascordfaults),
						new json_data(getQuestion("label.birth_placentachecks"),
								getAnswer("yesnodk_options", o.getBirth_placentachecks()), true),
						new json_data(getQuestion("label.placentachecks"), items2, hasplacentack),
						new json_data(getQuestion("label.birth_liqourvolume"),
								getAnswer("liqourvolume_options", o.getBirth_liqourvolume()), true),
						new json_data(getQuestion("label.birth_liqourcolor"),
								getAnswer("liqourcolor_options", o.getBirth_liqourcolor()), true),
						new json_data(getQuestion("label.birth_liqourodour"),
								getAnswer("liqourodour_options", o.getBirth_liqourodour()), true),
						new json_data(getQuestion("label.birth_babyoutcome"),
								getAnswer("babyoutcome_options", o.getBirth_babyoutcome()), true),
						new json_data(getQuestion("label.birth_motheroutcome"),
								getAnswer("motheroutcome_options", o.getBirth_motheroutcome()), true))
				.collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_fetalheart o) {
		List<json_data> list = Stream
				.of(new json_data(getQuestion("label.fetalheart_refered"),
						getAnswer("yesnodk_options", o.getFetalheart_refered()), true),
						new json_data(getQuestion("label.fetalheart_arrival"),
								getAnswer("yesnodk_options", o.getFetalheart_arrival()), true),
						new json_data(getQuestion("label.fetalheart_lastheard"),
								getAnswer("lastheard_options", o.getFetalheart_lastheard()), true))
				.collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_babydeath o) {
		boolean isresus = o.getBaby_resuscitation() != null && o.getBaby_resuscitation() == 1;
		boolean isadmit = o.getBaby_admitted() != null && o.getBaby_admitted() == 1;

		String items = "", items1 = "";
		if (o.getResuscitations() != null && !o.getResuscitations().isEmpty())
			for (resuscitation_table elem : o.getResuscitations()) {
				items += elem.getResuscitation_name() + "<br/>";
			}
		if (o.getNew_resuscitation() != null && o.getNew_resuscitation().trim() != "")
			items += o.getNew_resuscitation();

		if (o.getDiagnoses() != null && !o.getDiagnoses().isEmpty())
			for (diagnoses_table elem : o.getDiagnoses()) {
				items1 += elem.getDiagnosis_name() + "<br/>";
			}
		if (o.getNew_diagnoses() != null && o.getNew_diagnoses().trim() != "")
			items1 += o.getNew_diagnoses();

		List<json_data> list = Stream
				.of(new json_data(getQuestion("label.baby_cry"), getAnswer("yesnodk_options", o.getBaby_cry()), true),
						new json_data(getQuestion("label.baby_resuscitation"),
								getAnswer("yesnodk_options", o.getBaby_resuscitation()), true),
						new json_data(getQuestion("label.baby_resuscitation"), items, isresus),
						new json_data(getQuestion("label.baby_apgar1"), "" + o.getBaby_apgar1(), true),
						new json_data(getQuestion("label.baby_apgar5"), "" + o.getBaby_apgar5(), true),
						new json_data(getQuestion("label.baby_admitted"),
								getAnswer("yesnodk_options", o.getBaby_admitted()), true),
						new json_data(getQuestion(
								"label.diagnoses"), items1, isadmit),
						new json_data(getQuestion("label.baby_ddatetime"),
								(o.getBaby_ddate() == null) ? ""
										: new SimpleDateFormat("dd-MMM-yyyy").format(o.getBaby_ddate()) + " at "
												+ new SimpleDateFormat("HH:mm a").format(o.getBaby_dtime()),
								isadmit)

				).collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_mdeath_early o) {

		List<json_data> list = Stream.of(
				new json_data(getQuestion("label.mdeath_early_evacuation"),
						getAnswer("yesnodk_options", o.getEarly_evacuation()), true),
				new json_data(getQuestion("label.mdeath_early_antibiotic"),
						getAnswer("yesnodk_options", o.getEarly_antibiotic()), true),
				new json_data(getQuestion("label.mdeath_early_laparotomy"),
						getAnswer("yesnodk_options", o.getEarly_laparotomy()), true),
				new json_data(getQuestion("label.mdeath_early_hysterectomy"),
						getAnswer("yesnodk_options", o.getEarly_hysterectomy()), true),
				new json_data(getQuestion("label.mdeath_early_transfusion"),
						getAnswer("yesnodk_options", o.getEarly_transfusion()), true),
				new json_data(getQuestion("label.mdeath_early_antihyper"),
						getAnswer("yesnodk_options", o.getEarly_antihyper()), true),
				new json_data(getQuestion("label.mdeath_early_other"), o.getEarly_other(), true)

		).collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_mdeath_ante o) {

		List<json_data> list = Stream.of(
				new json_data(getQuestion("label.mdeath_ante_transfusion"),
						getAnswer("yesnodk_options", o.getAnte_transfusion()), true),
				new json_data(getQuestion("label.mdeath_ante_antibiotic"),
						getAnswer("yesnodk_options", o.getAnte_antibiotic()), true),
				new json_data(getQuestion("label.mdeath_ante_externalv"),
						getAnswer("yesnodk_options", o.getAnte_externalv()), true),
				new json_data(getQuestion("label.mdeath_ante_magsulphate"),
						getAnswer("yesnodk_options", o.getAnte_magsulphate()), true),
				new json_data(getQuestion("label.mdeath_ante_diazepam"),
						getAnswer("yesnodk_options", o.getAnte_diazepam()), true),
				new json_data(getQuestion("label.mdeath_ante_antihyper"),
						getAnswer("yesnodk_options", o.getAnte_antihyper()), true),
				new json_data(getQuestion("label.mdeath_ante_hysterotomy"),
						getAnswer("yesnodk_options", o.getAnte_hysterotomy()), true),
				new json_data(getQuestion("label.mdeath_ante_other"), o.getAnte_other(), true)

		).collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_mdeath_intra o) {

		List<json_data> list = Stream.of(
				new json_data(getQuestion("label.mdeath_intra_instrumental"),
						getAnswer("yesnodk_options", o.getIntra_instrumental()), true),
				new json_data(getQuestion("label.mdeath_intra_antibiotic"),
						getAnswer("yesnodk_options", o.getIntra_antibiotic()), true),
				new json_data(getQuestion("label.mdeath_intra_caesarian"),
						getAnswer("yesnodk_options", o.getIntra_caesarian()), true),
				new json_data(getQuestion("label.mdeath_intra_hysterectomy"),
						getAnswer("yesnodk_options", o.getIntra_hysterectomy()), true),
				new json_data(getQuestion("label.mdeath_intra_transfusion"),
						getAnswer("yesnodk_options", o.getIntra_transfusion()), true),
				new json_data(getQuestion("label.mdeath_intra_magsulphate"),
						getAnswer("yesnodk_options", o.getIntra_magsulphate()), true),
				new json_data(getQuestion("label.mdeath_intra_antihyper"),
						getAnswer("yesnodk_options", o.getIntra_antihyper()), true),
				new json_data(getQuestion("label.mdeath_intra_diazepam"),
						getAnswer("yesnodk_options", o.getIntra_diazepam()), true),
				new json_data(getQuestion("label.mdeath_intra_other"), o.getIntra_other(), true)

		).collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_mdeath_postpart o) {

		List<json_data> list = Stream.of(

				new json_data(getQuestion("label.mdeath_postpart_evacuation"),
						getAnswer("yesnodk_options", o.getPostpart_evacuation()), true),
				new json_data(getQuestion("label.mdeath_postpart_antibiotic"),
						getAnswer("yesnodk_options", o.getPostpart_antibiotic()), true),
				new json_data(getQuestion("label.mdeath_postpart_laparotomy"),
						getAnswer("yesnodk_options", o.getPostpart_laparotomy()), true),
				new json_data(getQuestion("label.mdeath_postpart_hysterectomy"),
						getAnswer("yesnodk_options", o.getPostpart_hysterectomy()), true),
				new json_data(getQuestion("label.mdeath_postpart_transfusion"),
						getAnswer("yesnodk_options", o.getPostpart_transfusion()), true),
				new json_data(getQuestion("label.mdeath_postpart_magsulphate"),
						getAnswer("yesnodk_options", o.getPostpart_magsulphate()), true),
				new json_data(getQuestion("label.mdeath_postpart_placentaremoval"),
						getAnswer("yesnodk_options", o.getPostpart_placentaremoval()), true),
				new json_data(getQuestion("label.mdeath_postpart_antihyper"),
						getAnswer("yesnodk_options", o.getPostpart_antihyper()), true),
				new json_data(getQuestion("label.mdeath_postpart_diazepam"),
						getAnswer("yesnodk_options", o.getPostpart_diazepam()), true),
				new json_data(getQuestion("label.mdeath_postpart_other"), o.getPostpart_other(), true)

		).collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_mdeath_other o) {

		List<json_data> list = Stream.of(

				new json_data(getQuestion("label.mdeath_other_anaesthga"),
						getAnswer("yesnodk_options", o.getOther_anaesthga()), true),
				new json_data(getQuestion("label.mdeath_other_epidural"),
						getAnswer("yesnodk_options", o.getOther_epidural()), true),
				new json_data(getQuestion("label.mdeath_other_spinal"),
						getAnswer("yesnodk_options", o.getOther_spinal()), true),
				new json_data(getQuestion("label.mdeath_other_local"), getAnswer("yesnodk_options", o.getOther_local()),
						true),
				new json_data(getQuestion("label.mdeath_other_invasive"),
						getAnswer("yesnodk_options", o.getOther_invasive()), true),
				new json_data(getQuestion("label.mdeath_other_antihyper"),
						getAnswer("yesnodk_options", o.getOther_antihyper()), true),
				new json_data(getQuestion("label.mdeath_other_icuventilation"),
						getAnswer("yesnodk_options", o.getOther_icuventilation()), true),
				new json_data(getQuestion("label.mdeath_new_intervention"), o.getNew_intervention(), true)

		).collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_mdeath o) {

		List<json_data> list = Stream.of(

				new json_data(getQuestion("label.case_mdeath"),
						o.getCase_uuid() == null ? "Not Stated"
								: getAnswer("mdeath_options", o.getCase_uuid().getCase_mdeath()),
						true),

				new json_data(getQuestion("label.mdeath_datetime"),
						(o.getMdeath_date() == null) ? ""
								: new SimpleDateFormat("dd-MMM-yyyy").format(o.getMdeath_date()) + " at "
										+ new SimpleDateFormat("HH:mm a").format(o.getMdeath_time()),
						true),
				new json_data(getQuestion("label.mdeath_autopsy"), getAnswer("yesnodk_options", o.getMdeath_autopsy()),
						true),
				new json_data(getQuestion("label.mdeath_autopsy_date"),
						(o.getMdeath_date() == null) ? ""
								: new SimpleDateFormat("dd-MMM-yyyy").format(o.getMdeath_date()),
						true),
				new json_data(getQuestion("label.mdeath_autopsy_location"), o.getMdeath_autopsy_location(), true),
				new json_data(getQuestion("label.mdeath_autopsy_by"),
						getAnswer("autopsyby_options", o.getMdeath_autopsy_by()), true),
				new json_data(getQuestion("label.mdeath_autopsy_final_cod"), o.getMdeath_autopsy_final_cod(), true),
				new json_data(getQuestion("label.mdeath_autopsy_antec_cod"), o.getMdeath_autopsy_antec_cod(), true),
				new json_data(getQuestion("label.mdeath_autopsy_ops_cod"), o.getMdeath_autopsy_ops_cod(), true),
				new json_data(getQuestion("label.mdeath_autopsy_icd_mm"),
						getAnswer("autopsyby_options", o.getMdeath_autopsy_icd_mm()), true)

		).collect(Collectors.toList());

		return list;
	}

	private List<json_data> processListOf(case_notes o) {
		List<json_data> list = Stream.of(new json_data(getQuestion("label.notes_text"), o.getNotes_text(), true),
				new json_data(getQuestion("label.notes_file"),
						CONSTANTS.IMAGE_TAG + convertBinImageToString(o.getNotes_file()), true)

		).collect(Collectors.toList());

		return list;
	}

	@GetMapping("/file/referral/{id}")
	@ResponseBody
	public void referralFile(@PathVariable("id") String id, HttpServletResponse response) {
		case_identifiers selected = caseRepo.findById(id).get();

		try {
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, (new StringBuilder()).append("inline;filename=\"")
					.append("referral_notes").append("\"").toString());

			response.setContentType(selected.getReferral().getReferral_filetype());

			java.io.OutputStream out = response.getOutputStream();
			IOUtils.copy(new ByteArrayInputStream(selected.getReferral().getReferral_file()), out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@GetMapping("/file/notes/{id}")
	@ResponseBody
	public void notesFile(@PathVariable("id") String id, HttpServletResponse response) {
		case_identifiers selected = caseRepo.findById(id).get();

		try {
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
					(new StringBuilder()).append("inline;filename=\"").append("case_summray").append("\"").toString());

			response.setContentType(selected.getNotes().getNotes_filetype());

			java.io.OutputStream out = response.getOutputStream();
			IOUtils.copy(new ByteArrayInputStream(selected.getNotes().getNotes_file()), out);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private String convertBinImageToString(byte[] binImage) {
		if (binImage != null && binImage.length > 0) {
			return Base64.getEncoder().encodeToString(binImage);
		} else
			return "";
	}

}// end class