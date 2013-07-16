package net.peaxy.simulator.web.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.peaxy.simulator.entity.domain.DataPolicy;
import net.peaxy.simulator.entity.domain.MigrationPolicyAndSchedule;
import net.peaxy.simulator.entity.domain.RuleCfg;
import net.peaxy.simulator.entity.domain.RuleFilter;
import net.peaxy.simulator.entity.domain.StorageClass;
import net.peaxy.simulator.entity.domain.StorageClassCfg;
import net.peaxy.simulator.entity.domain.StorageClassData;
import net.peaxy.simulator.manager.DataManager;
import net.peaxy.simulator.util.Utility;

import org.apache.log4j.Logger;

@Path("/dm")
public class DataManagementService {
	private static final Logger logger = Logger
			.getLogger(DataManagementService.class);

	@POST
	@Path("/policy/migration/test")
	@Consumes(MediaType.APPLICATION_JSON)
	public RuleCfg getMigration() {
		return RuleCfg.create();
	}

	/**
	 * Creates data migration policy with cron scheduling
	 * 
	 * @param policyList
	 */
	@PUT
	@Path("/policy/migration/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createMigrationPolicy(
			List<MigrationPolicyAndSchedule> policyList) {
		DataManager manager = DataManager.getInstance();
		for (MigrationPolicyAndSchedule migration : policyList) {
			try {
				manager.createMigration(migration);
				logger.debug("creating migration policy:"
						+ migration.getPolicy().getName());
			} catch (RuntimeException e) {
				logger.error("creating migration policies error :"
						+ e.getMessage());
			}
		}
	}

	/**
	 * Get the data policies of a type. If the type is not specified then all
	 * policies will be returned.
	 * 
	 * @return
	 */
	@GET
	@Path("/policy/get{type:((/migration)|(/ingest))?}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<DataPolicy> getPolicies(@PathParam("type") String type) {
		logger.debug("get policies.");
		DataManager manager = DataManager.getInstance();
		List<DataPolicy> policies = null;
		try {
			if ("/migration".equalsIgnoreCase(type)) {
				policies = manager.getMigrationPolicies();
			} else if ("/ingest".equalsIgnoreCase(type)) {
				policies = manager.getIngestPolicies();
			} else {
				policies = manager.getAllPolicies();
			}
			return policies;
		} catch (RuntimeException e) {
			logger.error("get policies error :" + e.getMessage());
			throw e;
		}
	}

	/**
	 * Delete a policy
	 * 
	 * @param id
	 *            the ID of the policy
	 */
	@DELETE
	@Path("/policy/delete/{id}")
	public void deletePolicy(@PathParam("id") int id) {
		DataManager manager = DataManager.getInstance();
		try {
			manager.deletePolicy(id);
			logger.debug("delete policy by id " + id);
		} catch (RuntimeException e) {
			logger.error("delete policy id " + id + " error :" + e.getMessage());
			throw e;
		}
	}

	/**
	 * Create ingest policies.
	 * 
	 * @param data
	 */
	@POST
	@Path("/policy/ingest/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createRuleConfig(List<RuleCfg> data) {
		DataManager manager = DataManager.getInstance();
		try {
			for (RuleCfg ingest : data) {
				if(ingest.getId() == null || ingest.getId() == 0)
					ingest.setId((int)Utility.getID());
				manager.createIngest(ingest);
				logger.debug("creating ingest policy " + ingest.getName());
			}
		} catch (RuntimeException e) {
			logger.error("creating ingest policies error :" + e.getMessage());
			throw e;
		}
	}

	/**
	 * Update ingest policies.
	 * 
	 * @param data
	 */
	@PUT
	@Path("/policy/ingest/set")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setRuleConfig(List<RuleCfg> data) {
		DataManager manager = DataManager.getInstance();
		try {
			for (RuleCfg ingest : data) {
				manager.updateIngest(ingest);
				logger.debug("update ingest policies " + ingest.getName());
			}
		} catch (RuntimeException e) {
			logger.error("update ingest policies error :" + e.getMessage());
			throw e;
		}
	}

	/**
	 * Delete ingest policy. Policies to be deleted can be filtered by policy id
	 * or policies matching the user group id.
	 * 
	 * @param filter
	 */
	@DELETE
	@Path("/policy/ingest/delete")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteRule(RuleFilter filter) {
		DataManager manager = DataManager.getInstance();
		try {
			manager.deleteRule(filter);
			logger.debug("delete rule by filter " + filter.getName());
		} catch (RuntimeException e) {
			logger.error("delete rule by filter " + filter.getName()
					+ " error :" + e.getMessage());
			throw e;
		}
	}

	/**
	 * Get all ingest policies matching the filter
	 * 
	 * @param filter
	 * @return
	 */
	@POST
	@Path("/policy/ingest/getfilter")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<RuleCfg> getRuleConfig(RuleFilter filter) {
		DataManager manager = DataManager.getInstance();
		List<RuleCfg> result = null;
		try {
			result = manager.getRule(filter);
			logger.debug("get rule by filter " + filter.getName());
			return result;
		} catch (RuntimeException e) {
			logger.error("get rule by filter " + filter.getName() + " error :"
					+ e.getMessage());
			throw e;
		}
	}

	/**
	 * Get all ingest policies
	 * 
	 * @return
	 */
	@GET
	@Path("/policy/ingest/get")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RuleCfg> getAllRuleConfig() {
		DataManager manager = DataManager.getInstance();
		List<RuleCfg> result = null;
		try {
			result = manager.getRule();
			logger.debug("get all rules.");
			return result;
		} catch (RuntimeException e) {
			logger.error("get all rules  error :" + e.getMessage());
			throw e;
		}
	}

	/**
	 * Creates a storage class, can create only one at a time after OOB is
	 * complete. Cannot be assigned default storage class when its created as it
	 * takes some time for the data stores to be assigned to new storage class.
	 * 
	 * @param data
	 */
	@POST
	@Path("/sc/create")
	@Consumes(MediaType.APPLICATION_JSON)
	public void createSCConfig(StorageClassCfg data) {
		DataManager manager = DataManager.getInstance();
		try {
			manager.createStorageClass(data);
			logger.debug("create Storage Class " + data.getName());
		} catch (RuntimeException e) {
			logger.error("create Storage Class error :" + e.getMessage());
			throw e;
		}
	}

	/**
	 * Updates storage class. Only default storage class, name and description
	 * can be updated. If name needs to be updated then object should've the id
	 * populated When changing defaultStorageClass both previous SC and new
	 * default SC need to be passed in the list for update.
	 * 
	 * @param data
	 */
	@PUT
	@Path("/sc/set")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setSCConfig(List<StorageClass> data) {
		DataManager manager = DataManager.getInstance();
		try {
			for (StorageClass sc : data) {
				manager.setStorageClass(sc);
				logger.debug("Updates Storage Class " + sc.getName());
			}
		} catch (RuntimeException e) {
			logger.error("Updates Storage Classes error :" + e.getMessage());
			throw e;
		}
	}

	/**
	 * This API is for QA purposes only, should NOT be documented for customer
	 * use.
	 */
	@POST
	@Path("/sc/update")
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateSCConfig(StorageClass data) {
		DataManager manager = DataManager.getInstance();
		try {
			manager.setStorageClass(data);
			logger.debug("Updates Storage Class " + data.getName());
		} catch (RuntimeException e) {
			logger.error("Updates Storage Class error :" + e.getMessage());
			throw e;
		}
	}

	/**
	 * Get the list of storage classes defined.
	 * 
	 * @return
	 */
	@GET
	@Path("/sc/get")
	@Produces(MediaType.APPLICATION_JSON)
	public List<StorageClass> getSCConfig() {
		List<StorageClass> result = null;
		DataManager manager = DataManager.getInstance();
		try {
			result = manager.getStorageClass();
			logger.debug("get all Storage Class.");
		} catch (RuntimeException e) {
			logger.error("get all Storage Class error :" + e.getMessage());
			throw e;
		}
		return result;
	}

	/**
	 * Get storage class capacity list
	 */
	@GET
	@Path("/sc/capacity/get")
	@Produces(MediaType.APPLICATION_JSON)
	public List<StorageClassData> getStorageClassCapacity() {
		List<StorageClassData> result = null;
		DataManager manager = DataManager.getInstance();
		try {
			result = manager.getStorageClassCapacity();
			logger.debug("get all Storage Class Capacity.");
		} catch (RuntimeException e) {
			logger.error("get all Storage Class Capacity error :"
					+ e.getMessage());
			throw e;
		}
		return result;
	}
}
