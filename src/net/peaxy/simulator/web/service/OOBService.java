package net.peaxy.simulator.web.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import net.peaxy.simulator.entity.domain.ConfigDataType;
import net.peaxy.simulator.entity.domain.DSPerfType;
import net.peaxy.simulator.entity.domain.DnsCfg;
import net.peaxy.simulator.entity.domain.EmailCfg;
import net.peaxy.simulator.entity.domain.HsCfg;
import net.peaxy.simulator.entity.domain.JsonKeyValue;
import net.peaxy.simulator.entity.domain.LogCfg;
import net.peaxy.simulator.entity.domain.NTPCfg;
import net.peaxy.simulator.entity.domain.NodeType;
import net.peaxy.simulator.entity.domain.OobState;
import net.peaxy.simulator.entity.domain.OobVM;
import net.peaxy.simulator.entity.domain.RuleCfg;
import net.peaxy.simulator.entity.domain.StatsCfg;
import net.peaxy.simulator.entity.domain.StorageClass;
import net.peaxy.simulator.entity.domain.StorageClassCfg;
import net.peaxy.simulator.entity.domain.SystemConfig;
import net.peaxy.simulator.entity.domain.User;
import net.peaxy.simulator.entity.domain.UserRole;
import net.peaxy.simulator.manager.ConfigManager;
import net.peaxy.simulator.manager.DataManager;
import net.peaxy.simulator.manager.UserManager;
import net.peaxy.simulator.util.Utility;
import net.peaxy.simulator.web.WebServerContainer;
import net.peaxy.simulator.web.WebServerSessionManager;

import org.apache.log4j.Logger;

@Path("/oob")
public class OOBService {

	private static Logger logger = Logger.getLogger(OOBService.class);
        
	@GET
    @Path("/completion")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCompletion(@Context HttpHeaders headers) {
		if(headers.getCookies().containsKey("JSESSIONID")){
			String sessionID = headers.getCookies().get("JSESSIONID").getValue();
			WebServerSessionManager session = WebServerContainer.getSessionManager();
			if(session.getData(sessionID, UserManager.SESSION_USER_NAME) != null){
				return "init";
			}
		}
		UserManager manager = UserManager.getInstance();
        return String.valueOf(!manager.hasUser());
    }
	
	@PUT
    @Path("/config/set")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String setSystemConfig(SystemConfig config) {
		logger.debug("set system config " + config.toString());
		
		try {
			ConfigManager cm = ConfigManager.getInstance();
			cm.configure("hyperfile_name", config.getClusterName());
			cm.configure("management_ip", config.getManagementIp());
			cm.configure("ns_repfactor", config.getCardinality()+"");
			cm.configure("file_size", config.getSpareCount()+"");
			cm.configure("dns_domain", config.getDnsDomain());
			cm.configure("net_mask", config.getSubnetMask());
			cm.configure("gate_way", config.getGateWay());
		} catch (Exception e) {
		}
		//set AAA
		UserManager um = UserManager.getInstance();
		User user = new User();
		user.setEmail(config.getUserEmail());
		user.setName(config.getUserName());
		user.setPassword(config.getPassword());
		user.setRole(UserRole.ADMIN);
		user.setToken(Utility.generateSN(8));
		um.createUser(user);
		
		ConfigManager manager =  ConfigManager.getInstance();
		JsonKeyValue value;
		// config hyperfile name
		value = new JsonKeyValue();
		value.setKey("hyperfile_name");
		value.setValue(config.getClusterName());
		manager.configure(value);
		// config management ip
		value = new JsonKeyValue();
		value.setKey("management_ip");
		value.setValue(config.getManagementIp());
		manager.configure(value);
        // config NTP
        NTPCfg ntpConfig = new NTPCfg();
        ntpConfig.setNtpServer1(config.getNtpServer1());
        ntpConfig.setNtpServer2(config.getNtpServer2());
        ntpConfig.setNtpServer3(config.getNtpServer3());
        ntpConfig.setTimeZone(config.getTimeZone());
        manager.configure(ntpConfig);
        // config DNS
        DnsCfg dns = new DnsCfg();
        dns.setSearch1(config.getDnsSearch1());
        dns.setSearch2(config.getDnsSearch2());
        dns.setSearch3(config.getDnsSearch3());
        dns.setServer1(config.getDnsServer1());
        dns.setServer2(config.getDnsServer2());
        dns.setServer3(config.getDnsServer3());
        manager.configure(dns);
        // config Email
        EmailCfg email = new EmailCfg();
        email.setEmailFrom(config.getEmailFrom());
        email.setEmailLogin(config.getEmailLogin());
        email.setEmailPassword(config.getEmailPassword());
        email.setEmailPort(config.getEmailPort());
        email.setEmailProtocol(config.getEmailProtocol());
        email.setEmailServer(config.getEmailServer());
        manager.configure(email);
        // config HS
        HsCfg hs = new HsCfg();
        hs.setCardinality(config.getCardinality());
        //hs.setReplacementEnabled(config.get));
        hs.setSpareCount(config.getSpareCount());
        manager.configure(hs);
        // config Log
        LogCfg log = new LogCfg();
        log.setPort(config.getLogServerPort());
        log.setServer(config.getLogServerIp());
        manager.configure(log);
        // config Stats
        //StatsCfg stats = new StatsCfg();
        //stats.setInterval(config.get)
        manager.configure(new StatsCfg());
        DataManager dm = DataManager.getInstance();
        // save RuleCfg
        dm.deleteAllRules();
        if(config.getInjestList() != null && config.getInjestList().size() != 0){
        	for(RuleCfg ingest : config.getInjestList()){
        		if(ingest != null){
        			dm.createIngest(ingest);
        		}
        	}
        }
        // save StorageClassCfg
        if(config.getScList() != null){
        	for(StorageClassCfg sc : config.getScList()){
        		dm.createStorageClass(sc);
        	}
        }
        
        return "success";
    }

    @GET
    @Path("/config/get")
    @Produces(MediaType.APPLICATION_JSON)
    public SystemConfig getSystemConfig() {
    	logger.debug("get system config " );
    	SystemConfig sc = new SystemConfig();
    	// get User
    	UserManager um = UserManager.getInstance();
    	User user = um.getAdminUser();
    	if(user != null){
    		sc.setUserName(user.getName());
    		sc.setUserEmail(user.getEmail());
    		sc.setPassword(user.getPassword());
    	}
    	ConfigManager manager = ConfigManager.getInstance();
    	
        // get hyperfile name and ip
        try {
			sc.setClusterName(manager.get("hyperfile_name").getValue());
			sc.setManagementIp(manager.get("management_ip").getValue());
			sc.setCardinality(Integer.parseInt(manager.get("ns_repfactor").getValue()));
			sc.setSpareCount(Integer.parseInt(manager.get("file_size").getValue()));
			sc.setDnsDomain(manager.get("dns_domain").getValue());
			sc.setSubnetMask(manager.get("net_mask").getValue());
			sc.setGateWay(manager.get("gate_way").getValue());
		} catch (Exception e) {
		}
        
        // get NTP config
        try {
        	NTPCfg ntp = (NTPCfg)manager.get(ConfigDataType.NTPCfg);
            sc.setTimeZone(ntp.getTimeZone());
            sc.setNtpServer1(ntp.getNtpServer1());
            sc.setNtpServer2(ntp.getNtpServer2());
            sc.setNtpServer3(ntp.getNtpServer3());
		} catch (Exception e) {
		}
        
        // get DNS config
        try {
        	DnsCfg dns = (DnsCfg)manager.get(ConfigDataType.DNSCfg);
            sc.setDnsSearch1(dns.getSearch1());
            sc.setDnsSearch2(dns.getSearch2());
            sc.setDnsSearch3(dns.getSearch3());
            sc.setDnsServer1(dns.getServer1());
            sc.setDnsServer2(dns.getServer2());
            sc.setDnsServer3(dns.getServer3());
		} catch (Exception e) {
		}        
        // get  Email config
        try {
        	EmailCfg email = (EmailCfg)manager.get(ConfigDataType.EmailCfg);
            sc.setEmailFrom(email.getEmailFrom());
            sc.setEmailLogin(email.getEmailLogin());
            sc.setEmailPassword(email.getEmailPassword());
            sc.setEmailPort(email.getEmailPort());
            sc.setEmailProtocol(email.getEmailProtocol());
            sc.setEmailServer(email.getEmailServer());
		} catch (Exception e) {
		}
        
        // get HS config
        try {
        	HsCfg hs = (HsCfg)manager.get(ConfigDataType.HsCfg);
            sc.setCardinality(hs.getCardinality());
            //hs.setReplacementEnabled(config.get));
            sc.setSpareCount(hs.getSpareCount());
		} catch (Exception e) {
		}
        
        // get Log config
        try {
        	LogCfg log = (LogCfg)manager.get(ConfigDataType.LogCfg);
            sc.setLogServerPort(log.getPort());
            sc.setLogServerIp(log.getServer());
		} catch (Exception e) {
		}
        DataManager dataManager = DataManager.getInstance();
        List<RuleCfg> rules = dataManager.getRule();
        // get ingest
        try {
            sc.setInjestList(rules);
		} catch (Exception e) {
		} 
        // get storage class
        List<StorageClassCfg> storageCfgs = new ArrayList<StorageClassCfg>();
        try {
        	List<StorageClass> storages = dataManager.getStorageClass();
            for(StorageClass storage : storages){
            	StorageClassCfg storageCfg = new StorageClassCfg();
            	storageCfg.setCapacityGb(storage.getCapacityGb());
            	storageCfg.setCardinality(storage.getCardinality());
            	storageCfg.setDefaultSC(storage.isDefaultSC());
            	storageCfg.setDescription(storage.getDescription());
            	storageCfg.setName(storage.getName());
            	storageCfg.setType(storage.getType());
            	storageCfgs.add(storageCfg);
            }
		} catch (Exception e) {
		}
        sc.setScList(storageCfgs);
        return sc;
    }
    
    @GET
    @Path("/state/get")
    @Produces(MediaType.APPLICATION_JSON)
    public OobState getServiceState() {
    	logger.debug("get service state.");
        return OobState.WAIT_CONFIG;
    }
    
    @GET
    @Path("/vms/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<OobVM> getVMs() {
    	Set<OobVM> vms = new HashSet<OobVM>();
    	OobVM vm ;
    	for(int i=1;i<33;i++){
    		vm = new OobVM();
    		vm.setCapacity(1024 * i * 10);
    		vm.setDataPartition("VM"+i);
    		vm.setDriveType(DSPerfType.values()[i % 16]);
    		vm.setHostId(1);
    		vm.setIp("192.168.1."+i);
    		if(i % 9 == 0)
    			vm.setType(NodeType.NS);
    		else 
    			vm.setType(NodeType.DS);    		
    		vm.setName(vm.getType() + "VM"+i);
    		vm.setId(Utility.getID());
    		vms.add(vm);
    	}
        return vms;
    }
    
    @POST
    @Path("/service/restart")
    public void restart() {
    	logger.debug("service restart.");
    }
    
}
