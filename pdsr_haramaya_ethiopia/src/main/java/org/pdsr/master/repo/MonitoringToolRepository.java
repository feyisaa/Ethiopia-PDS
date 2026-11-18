package org.pdsr.master.repo;

import java.util.List;

import org.pdsr.master.model.monitoring_tool;
import org.pdsr.master.model.sync_table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringToolRepository extends JpaRepository<monitoring_tool, String> {

	@Query("select m FROM monitoring_tool m WHERE m.case_sync=?1")
	List<monitoring_tool> findByFacility(sync_table case_sync);

}
