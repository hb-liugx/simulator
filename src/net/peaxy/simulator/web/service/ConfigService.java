package net.peaxy.simulator.web.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.peaxy.simulator.entity.domain.ConfigDataType;
import net.peaxy.simulator.entity.domain.DnsCfg;
import net.peaxy.simulator.entity.domain.EmailCfg;
import net.peaxy.simulator.entity.domain.HsCfg;
import net.peaxy.simulator.entity.domain.JsonKeyValue;
import net.peaxy.simulator.entity.domain.LogCfg;
import net.peaxy.simulator.entity.domain.NTPCfg;
import net.peaxy.simulator.entity.domain.StatsCfg;
import net.peaxy.simulator.manager.ConfigManager;
import net.peaxy.simulator.web.exception.InternalException;

import org.apache.log4j.Logger;

@Path("/conf")
public class ConfigService {
	protected static final Logger logger = Logger.getLogger(ConfigService.class);
	
    @POST
    @Path("/message/get/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage(@PathParam("id") String id) {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            logger.debug("get message :" + id);
            return manager.getMessage(id);
        }
        catch ( Exception e ) {
            logger.error("error setting config", e);
            throw new InternalException(e.getMessage());
        }
    }

    @POST
    @Path("/dns/set")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setDnsConfig(DnsCfg data) {
    	ConfigManager manager = ConfigManager.getInstance();
        try {
            logger.debug("DNS set :" + data.toString());
            manager.configure(data);
        }
        catch ( Exception e ) {
            logger.error("error setting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @GET
    @Path("/dns/get")
    @Produces(MediaType.APPLICATION_JSON)
    public DnsCfg getDnsConfig() {
    	ConfigManager manager = ConfigManager.getInstance();
        try {
            DnsCfg res = (DnsCfg) manager.get(ConfigDataType.DNSCfg);
            return res;
        }
        catch ( Exception e ) {
            logger.error("error getting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @POST
    @Path("/email/set")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setEmailConfig(EmailCfg data) {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            logger.debug("Email set :" + data.toString());
            manager.configure(data);
        }
        catch ( Exception e ) {
            logger.error("error setting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @GET
    @Path("/email/get")
    @Produces(MediaType.APPLICATION_JSON)
    public EmailCfg getEmailConfig() {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            EmailCfg res = (EmailCfg) manager.get(ConfigDataType.EmailCfg);
            return res;
        }
        catch ( Exception e ) {
            logger.error("error getting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @POST
    @Path("/hs/set")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setHsConfig(HsCfg data) {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            logger.debug("HS set :" + data.toString());
            manager.configure(data);
        }
        catch ( Exception e ) {
            logger.error("error setting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @GET
    @Path("/hs/get")
    @Produces(MediaType.APPLICATION_JSON)
    public HsCfg getHsConfig() {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            HsCfg res = (HsCfg) manager.get(ConfigDataType.HsCfg);
            return res;
        }
        catch ( Exception e ) {
            logger.error("error getting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @POST
    @Path("/log/set")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setLogConfig(LogCfg data) {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            logger.debug("Log set :" + data.toString());
            manager.configure(data);
        }
        catch ( Exception e ) {
            logger.error("error setting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @GET
    @Path("/log/get")
    @Produces(MediaType.APPLICATION_JSON)
    public LogCfg getLogConfig() {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            LogCfg res = (LogCfg) manager.get(ConfigDataType.LogCfg);
            return res;
        }
        catch ( Exception e ) {
            logger.error("error getting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @POST
    @Path("/ntp/set")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setNTPConfig(NTPCfg data) {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            logger.debug("NTP set :" + data.toString());
            manager.configure(data);
        }
        catch ( Exception e ) {
            logger.error("error setting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @GET
    @Path("/ntp/get")
    @Produces(MediaType.APPLICATION_JSON)
    public NTPCfg getNTPConfig() {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            NTPCfg res = (NTPCfg) manager.get(ConfigDataType.NTPCfg);
            return res;
        }
        catch ( Exception e ) {
            logger.error("error getting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @POST
    @Path("/stats/set")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setStatsConfig(StatsCfg data) {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            logger.debug("Stats set :" + data.toString());
            manager.configure(data);
        }
        catch ( Exception e ) {
            logger.error("error setting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @GET
    @Path("/stats/get")
    @Produces(MediaType.APPLICATION_JSON)
    public StatsCfg getStatsConfig() {
        ConfigManager manager = ConfigManager.getInstance();
        try {
            StatsCfg res = (StatsCfg) manager.get(ConfigDataType.StatsCfg);
            return res;
        }
        catch ( Exception e ) {
            logger.error("error getting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @POST
    @Path("/kv/set")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setKV(JsonKeyValue kv) {

    	ConfigManager manager = ConfigManager.getInstance();
        try {
            manager.configure(kv);
        }
        catch ( Exception e ) {
            logger.error("error setting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    @GET
    @Path("/kv/get/{key}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonKeyValue getKV(@PathParam("key") String key) {
    	ConfigManager manager = ConfigManager.getInstance();
        try {
        	JsonKeyValue res = manager.get(key);
            return res;
        }
        catch ( Exception e ) {
            logger.error("error getting config", e);
            throw new InternalException(e.getMessage());
        }
    }
    
    
}
