package org.pdsr.master.repo;

import org.pdsr.master.model.case_mdeath_early;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseMdeathEarlyRepository extends JpaRepository<case_mdeath_early, String> {


}
