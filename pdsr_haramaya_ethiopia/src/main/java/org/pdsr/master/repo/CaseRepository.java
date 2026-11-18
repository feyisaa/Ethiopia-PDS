package org.pdsr.master.repo;

import java.util.Date;
import java.util.List;

import org.pdsr.master.model.case_identifiers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CaseRepository extends JpaRepository<case_identifiers, String> {

	@Query("select c from case_identifiers c where c.case_id = ?1")
	List<case_identifiers> findByCaseID(String case_id);

	@Query("select c from case_identifiers c where c.case_status = 0")
	List<case_identifiers> findByDraftCases();

	@Query("select c from case_identifiers c where c.case_status >= 1")
	List<case_identifiers> findBySubmittedCases();

	@Query("select c from case_identifiers c where c.data_sent = 0 or c.data_sent is null")
	List<case_identifiers> findBySubmittedToPush();

	@Query("select c from case_identifiers c LEFT JOIN audit_case a ON(c.case_uuid=a.audit_uuid) WHERE a.audit_uuid IS NULL AND c.case_status=?1")
	List<case_identifiers> findByPendingCase_status(Integer case_status);

	@Query("select COUNT(c) from case_identifiers c where c.case_death=?1")
	Integer countByCase_death(Integer case_death);

	@Query("select COUNT(c) from case_identifiers c where c.case_death=?1 and year(c.case_date)=?2")
	Integer countByCase_death(Integer case_death, Integer year);

	@Query("select COUNT(c) from case_identifiers c where c.case_status >= 1 AND c.case_death=?1")
	Integer countBySubmittedAndType(Integer case_death);

	@Query("select COUNT(c) from case_identifiers c where c.case_status >= 1 AND c.case_death=?1 and year(c.case_date)=?2")
	Integer countBySubmittedAndType(Integer case_death, Integer year);

	@Query("select COUNT(c) from case_identifiers c INNER JOIN audit_case a ON(c.case_uuid=a.audit_uuid) WHERE c.case_death=?1")
	Integer countSelectedCasesByCase_death(Integer case_death);

	@Query("select COUNT(c) from case_identifiers c INNER JOIN audit_case a ON(c.case_uuid=a.audit_uuid) WHERE c.case_death=?1 and year(c.case_date)=?2")
	Integer countSelectedCasesByCase_death(Integer case_death, Integer year);

	@Query("select COUNT(c) from case_identifiers c INNER JOIN audit_case a ON(c.case_uuid=a.audit_uuid) INNER JOIN a.audit_audit t WHERE c.case_death=?1")
	Integer countReviewedCasesByCase_death(Integer case_death);

	@Query("select COUNT(c) from case_identifiers c INNER JOIN audit_case a ON(c.case_uuid=a.audit_uuid) INNER JOIN a.audit_audit t WHERE c.case_death=?1 and year(c.case_date)=?2")
	Integer countReviewedCasesByCase_death(Integer case_death, Integer year);

	@Query("select COUNT(c) from case_identifiers c INNER JOIN audit_case a ON(c.case_uuid=a.audit_uuid) INNER JOIN a.audit_audit t WHERE c.case_death=?1 and c.case_date between ?2 and ?3")
	Integer countByCase_death_reviewed(Integer case_death, Date from, Date to);

	@Query("select COUNT(c) from case_identifiers c where c.case_status >= 1 and c.case_death = 3 and c.case_date between ?1 and ?2")
	Integer countByMaternal_death_notified(Date from, Date to);

	@Query("select COUNT(c) from case_identifiers c where c.case_status >= 1 and (c.case_death=1 or (c.case_death=2 and DATEDIFF(DAY, c.delivery.delivery_date, c.babydeath.baby_ddate) < 8)) and c.case_date between ?1 and ?2")
	Integer countByPerinatal_death_notified(Date from, Date to);

	@Query("select COUNT(c) from case_identifiers c INNER JOIN audit_case a ON(c.case_uuid=a.audit_uuid) INNER JOIN a.audit_audit t where c.case_death = 3 and c.case_date between ?1 and ?2")
	Integer countByMaternal_death_reviewed(Date from, Date to);

	@Query("select COUNT(c) from case_identifiers c INNER JOIN audit_case a ON(c.case_uuid=a.audit_uuid) INNER JOIN a.audit_audit t where (c.case_death=1 or (c.case_death=2 and DATEDIFF(DAY, c.delivery.delivery_date, c.babydeath.baby_ddate) < 8)) and c.case_date between ?1 and ?2")
	Integer countByPerinatal_death_reviewed(Date from, Date to);
	
	@Query(
		    "SELECT DISTINCT c FROM case_identifiers c " +
		 
		    "LEFT JOIN FETCH c.biodata " +
		    "LEFT JOIN FETCH c.pregnancy " +
		    "LEFT JOIN FETCH c.delivery " +
		    "LEFT JOIN FETCH c.referral " +
		    "LEFT JOIN FETCH c.antenatal " +
		    "LEFT JOIN FETCH c.labour " +
		    "LEFT JOIN FETCH c.fetalheart " +
		    "LEFT JOIN FETCH c.birth " +
		    "LEFT JOIN FETCH c.babydeath " +
		    "LEFT JOIN FETCH c.mdeath " +
		    "LEFT JOIN FETCH c.notes"
		)
		List<case_identifiers> findAllWithAllDetails();

}
