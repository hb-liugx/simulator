package net.peaxy.simulator.manager;

import java.util.ArrayList;
import java.util.List;

import net.peaxy.simulator.data.Record;
import net.peaxy.simulator.data.TableSpace;
import net.peaxy.simulator.entity.domain.DSPerfType;
import net.peaxy.simulator.entity.domain.DataPolicy;
import net.peaxy.simulator.entity.domain.MigrationPolicyAndSchedule;
import net.peaxy.simulator.entity.domain.PeaxyBaseDomain;
import net.peaxy.simulator.entity.domain.RuleCfg;
import net.peaxy.simulator.entity.domain.RuleFilter;
import net.peaxy.simulator.entity.domain.StorageClass;
import net.peaxy.simulator.entity.domain.StorageClassCfg;
import net.peaxy.simulator.entity.domain.StorageClassData;
import net.peaxy.simulator.util.Utility;
import net.peaxy.simulator.web.exception.ExistedException;
import net.peaxy.simulator.web.exception.NotExistException;

import org.apache.log4j.Logger;

public class DataManager {
	
	private final Logger logger = Logger
			.getLogger(DataManager.class);
	
	private static final String MIG_TABLE_NAME = "migration";
	private static final String RULE_TABLE_NAME = "rule";
	private static final String SC_TABLE_NAME = "sc";

	static {
		if (!TableSpace.getInstance().hasTable(MIG_TABLE_NAME)) {
			TableSpace.getInstance().addTable(MIG_TABLE_NAME);
		}
		if (!TableSpace.getInstance().hasTable(RULE_TABLE_NAME)) {
			TableSpace.getInstance().addTable(RULE_TABLE_NAME);
		}
		if (!TableSpace.getInstance().hasTable(SC_TABLE_NAME)) {
			TableSpace.getInstance().addTable(SC_TABLE_NAME);
		}
	}

	private static DataManager instance = null;

	public static DataManager getInstance() {
		if (instance == null) {
			instance = new DataManager();
		}
		return instance;
	}

	public void createMigration(MigrationPolicyAndSchedule migration) {
		if(TableSpace.getInstance().hasData(MIG_TABLE_NAME, migration.getPolicy().getId()+"")){
			logger.error("magration is existed, can't create.");
			throw new ExistedException("Migration existed.");
		} else {
			TableSpace.getInstance().setData(MIG_TABLE_NAME, migration.getPolicy().getId()+"", migration.toJSON());
		}
	}

	public void createIngest(RuleCfg ingest) {
		if(TableSpace.getInstance().hasData(RULE_TABLE_NAME, ingest.getId()+"")){
			logger.error("ingest is existed, can't create.");
			//throw new ExistedException("Ingest existed.");
		} else {
			if(ingest.getId() == null || ingest.getId() == 0)
				ingest.setId((int)Utility.getID());
			TableSpace.getInstance().setData(RULE_TABLE_NAME, ingest.getId()+"", ingest.toJSON());
		}
	}
	
	public void updateIngest(RuleCfg ingest) {
		if(!TableSpace.getInstance().hasData(RULE_TABLE_NAME, ingest.getId()+"")){
			logger.error("ingest is not existed, can't update.");
			throw new NotExistException("Ingest not existed.");
		} else {
			TableSpace.getInstance().setData(RULE_TABLE_NAME, ingest.getId()+"", ingest.toJSON());
		}
	}
	
	public List<DataPolicy> getMigrationPolicies() {
		logger.debug("get Migration policies.");
		List<DataPolicy> migrations = new ArrayList<DataPolicy>();
		List<Record> records = TableSpace.getInstance().getAllData(MIG_TABLE_NAME);
		for(Record record : records){
			try{
				MigrationPolicyAndSchedule mpas = (MigrationPolicyAndSchedule)PeaxyBaseDomain.toDomain(record.toJson().toString());
				migrations.add(mpas.getPolicy());
			} catch(Exception e){
				logger.warn("convert record to Migration error : " + e.getMessage());
			}
		}
		return migrations;
	}

	public List<DataPolicy> getIngestPolicies() {
		logger.debug("get Ingest policies.");
		List<DataPolicy> ingests = new ArrayList<DataPolicy>();
		List<Record> records = TableSpace.getInstance().getAllData(RULE_TABLE_NAME);
		for(Record record : records){
			try{
				ingests.add((DataPolicy)(PeaxyBaseDomain.toDomain(record.toJson().toString())));
			} catch(Exception e){
				logger.warn("convert record to Ingest error : " + e.getMessage());
			}
		}
		return ingests;
	}

	public List<DataPolicy> getAllPolicies() {		
		List<DataPolicy> migrations = getMigrationPolicies();
		List<DataPolicy> ingests = getIngestPolicies();
		List<DataPolicy> policies = new ArrayList<DataPolicy>();
		for(DataPolicy policy : migrations){
			policies.add(policy);
		}
		for(DataPolicy policy : ingests){
			policies.add(policy);
		}
		return policies;
	}

	public void deletePolicy(int id) {
		logger.debug("delete policy by id " + id);
		TableSpace.getInstance().removeData(MIG_TABLE_NAME, id+"");
		TableSpace.getInstance().removeData(RULE_TABLE_NAME, id+"");
	}

	public void deleteRule(RuleFilter filter) {
		logger.debug("delete ingest policy by filter " + filter.getName());
    	List<RuleCfg> allRuleCfgs = getRule();
    	for(RuleCfg cfg : allRuleCfgs){
    		if((filter.getType() == null || filter.getType().equals(cfg.getFileType())) &&
    				(filter.getGroupId() == null || filter.getGroupId() == 0 || filter.getGroupId().longValue() == cfg.getGroupId().longValue()) &&
    				(filter.getScId() == null || filter.getScId() == 0 || filter.getScId().intValue() == cfg.getStorageClassId().intValue()) && 
    				(filter.getRecursive() == null || filter.getRecursive().booleanValue() == cfg.isNullGroupId()) ) {
    			ConfigManager.addMessage("delete rule config " + cfg.getId());
            	TableSpace.getInstance().removeData(RULE_TABLE_NAME, ""+cfg.getId());
    		}
    	}
    	allRuleCfgs.clear();
	}

	public void deleteAllRules(){
		List<RuleCfg> allRuleCfgs = getRule();
		for(RuleCfg cfg : allRuleCfgs){
			if(cfg.getId() == null)
				cfg.setId(0);
			TableSpace.getInstance().removeData(RULE_TABLE_NAME, ""+cfg.getId());
		}
		allRuleCfgs.clear();
	}
	
	public List<RuleCfg> getRule(RuleFilter filter) {
		logger.debug("get ingest policy by filter " + filter.getName());
		List<RuleCfg> allRuleCfgs = getRule();
    	List<RuleCfg> result = new ArrayList<RuleCfg>();
    	for(RuleCfg cfg : allRuleCfgs){
    		if((filter.getType() == null || filter.getType().equals(cfg.getFileType())) &&
    				(filter.getGroupId() == null || filter.getGroupId() == 0 || filter.getGroupId().longValue() == cfg.getGroupId().longValue()) &&
    				(filter.getScId() == null || filter.getScId() == 0 || filter.getScId().intValue() == cfg.getStorageClassId().intValue()) && 
    				(filter.getRecursive() == null || filter.getRecursive().booleanValue() == cfg.isNullGroupId()) ) {
    			result.add(cfg);
    		}
    	}   	
    	return result;
	}

	public List<RuleCfg> getRule() {
		logger.debug("get all ingest policies.");
		List<Record> allRecords = TableSpace.getInstance().getAllData(RULE_TABLE_NAME);
    	List<RuleCfg> allRuleCfgs = new ArrayList<RuleCfg>();
    	for(Record record : allRecords){
    		try {
    			allRuleCfgs.add((RuleCfg)PeaxyBaseDomain.toDomain(record.toJson().toString()));
    		} catch(Exception e){
    			logger.warn("convert record to StorageClass error : " + e.getMessage());
    		}
    	}
		return allRuleCfgs;
	}

	public void createStorageClass(StorageClassCfg data) {
		logger.debug("create storage class " + data.getName());
		StorageClass sc = new StorageClass();
		sc.setCapacityGb(data.getCapacityGb());
		sc.setCardinality(data.getCardinality());
		sc.setDefaultSC(data.isDefaultSC());
		sc.setDescription(data.getDescription());
		sc.setDsList(new ArrayList<Integer>());
		sc.setName(data.getName());
		sc.setType(data.getType());
		createStorageClass(sc);
	}

	public void setStorageClass(StorageClass sc) {
		logger.debug("update storage class " + sc.getName());
		if(!TableSpace.getInstance().hasData(SC_TABLE_NAME, sc.getName())){
			throw new NotExistException("storage class not exist.");
		} else {
			TableSpace.getInstance().setData(SC_TABLE_NAME, sc.getName(), sc.toJSON());
		}
	}

	private void createStorageClass(StorageClass sc) {
		logger.debug("create storage class id = " + sc.getId());
		if(TableSpace.getInstance().hasData(SC_TABLE_NAME, sc.getName()+"")){
			setStorageClass(sc);
		} else {
			sc.setId((int)Utility.getID());
			TableSpace.getInstance().setData(SC_TABLE_NAME, sc.getName(), sc.toJSON());
		}
	}
	
	public List<StorageClass> getStorageClass() {
		logger.debug("get all storage classes ");
		List<StorageClass> scs = new ArrayList<StorageClass>();
		List<Record> records = TableSpace.getInstance().getAllData(SC_TABLE_NAME);
		for(Record record : records){
			try{
				scs.add((StorageClass)(PeaxyBaseDomain.toDomain(record.toJson().toString())));
			} catch(Exception e){
				logger.warn("convert record to StorageClass error : " + e.getMessage());
			}
		}
		return scs;
	}

	public List<StorageClassData> getStorageClassCapacity() {
		logger.debug("get all storage classes capacity .");
		List<StorageClass> allSC = getStorageClass();
		List<StorageClassData> result = new ArrayList<StorageClassData>();
		for(StorageClass sc : allSC){
			StorageClassData scData = new StorageClassData();
			scData.setId(sc.getId());
			scData.setTotalMB((int) (sc.getCapacityGb() * 1024));
			scData.setFreeMB(scData.getTotalMB() * sc.getCardinality() / 100);
			scData.setUsedMB(scData.getTotalMB() - scData.getFreeMB());
			result.add(scData);
		}
		return result;
	}

	public void createDefaultSC() {
		StorageClass sc = new StorageClass();
		sc.setCapacityGb(100*1024);
		sc.setCardinality(3);
		sc.setDefaultSC(true);
		sc.setDescription("Catch-all Storage Class");
		sc.setDsList(new ArrayList<Integer>());
		sc.setName("Default Storage Class");
		sc.setType(DSPerfType.LEVEL_1);
		sc.setId((int)Utility.getID());
		createStorageClass(sc);
	}
}
