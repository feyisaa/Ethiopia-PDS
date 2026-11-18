package org.pdsr.master.repo;

import org.pdsr.master.model.case_mdeath_postpart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseMdeathPostpartRepository extends JpaRepository<case_mdeath_postpart, String> {


}
