package net.peaxy.simulator.web.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.peaxy.simulator.data.TableSpace;
import net.peaxy.simulator.entity.domain.DiscoverInfo;
import net.peaxy.simulator.entity.domain.DnsCfg;
import net.peaxy.simulator.entity.domain.FileSizeList;
import net.peaxy.simulator.entity.domain.HfInfo;
import net.peaxy.simulator.entity.domain.InitHfProgress;
import net.peaxy.simulator.entity.domain.InterfaceInfo;
import net.peaxy.simulator.entity.domain.IpAssignments;
import net.peaxy.simulator.entity.domain.IpInfo;
import net.peaxy.simulator.entity.domain.IpStatus;
import net.peaxy.simulator.entity.domain.NTPCfg;
import net.peaxy.simulator.entity.domain.NetworkSettings;
import net.peaxy.simulator.entity.domain.NetworkSettingsStatus;
import net.peaxy.simulator.entity.domain.OsInstallProgress;
import net.peaxy.simulator.entity.domain.VmInstallProgress;
import net.peaxy.simulator.manager.BootstrapManager;
import net.peaxy.simulator.manager.ConfigManager;
import net.peaxy.simulator.util.Utility;
import net.peaxy.simulator.web.exception.AuthenticationException;

import org.apache.log4j.Logger;

@Path("/bootstrap")
public final class BootstrapService {
	public static final Logger logger = Logger
			.getLogger(BootstrapService.class);
	
	public static final String SERVER_TABLE_NAME = "discover";
	
	static{
		if(!TableSpace.getInstance().hasTable(SERVER_TABLE_NAME)){
    		TableSpace.getInstance().addTable(SERVER_TABLE_NAME);
    	}
	}
	
	/**
	 * Issue a broadcast to discover servers that are ready to be installed.
	 * 
	 * @return - A list of DiscoverInfo objects representing the servers that
	 *         replied.
	 * @throws Exception 
	 * @throws AuthenticationException 
	 */
	@POST
    @Path("/discover")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DiscoverInfo> discover() {
		ConfigManager.addMessage("get all discover info." );		
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		DiscoverInfo[] servers = bootstrap.getAllServers();
		List<DiscoverInfo> list = new ArrayList<DiscoverInfo>();
		if(Utility.canDiscover()){
			for(DiscoverInfo server : servers){
				list.add(server);
			}
		}
		return list;
	}

	/**
	 * Turn the server beacon on or off.
	 * 
	 * @param id
	 *            - The id of a server.
	 * @param on
	 *            - If true, turn the server beacon on, if false turn the server
	 *            beacon off.
	 */
	@POST
	@Path("/beacon/set/{id}/{on}")
	public void setBeacon(@PathParam("id") long id, @PathParam("on") boolean on) {
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		DiscoverInfo server = bootstrap.getServer(id);
		if( server != null && server.getId() == id){
			server.setBeacon(on);
			bootstrap.saveServer(server);
		}
	}

	/**
     * Get the current beacon state for the selected nodes.
     * @param idList - The list of server ids for which to get the current beacon state.
     * @return - A map of server id/boolean pairs indicating current beacon state. True
     * means the beacon is on, False means the beacon is off.
     */
    @POST
    @Path("/beacons/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Long, Boolean> getBeacons(List<Long> idList) {
        return BootstrapManager.getInstance().getBeacons(idList);
    }
	/**
	 * Specify the list of interfaces to bond.
	 * 
	 * @param id
	 *            - The id of a server.
	 * @param interfaceList
	 *            - The list of interfaces that are to be bonded into a single
	 *            interface.
	 */
	@POST
    @Path("/bond_list/set/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void setBondList(@PathParam("id") long id, List<String> interfaceList) {
		ConfigManager.addMessage("set bond list for id " + id + " with " + interfaceList.toString());
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		DiscoverInfo server = bootstrap.getServer(id);
		if( server != null && server.getId() == id){
			for(InterfaceInfo inf : server.getInterfaceList()){
				if(interfaceList.contains(inf.getInterfaceName())){
					inf.setBonded(true);
				} else {
					inf.setBonded(false);
				}
			}
			bootstrap.saveServer(server);
		}
	}

    /**
     * Retrieve the list of file sizes that map to small/medium/large/mixed and the corresponding
     * NS:DS ratio for each entry in the list.
     * @return - A FileSizeList object with the file sizes/NS:DS ratio information.
     */
    @POST
    @Path("/file_size_list/get")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileSizeList getFileSizeList() {
        return new FileSizeList();
    }
    
	/**
	 * Determine the status of each of the fields in the NetworkSettings object.
	 * For the most part the function determines whether an IP is pingable or
	 * not. In some cases pingable is good, and in other cases pingable is bad.
	 * The caller needs to check these statuses in the return object and
	 * determine how to report the findings.
	 * 
	 * @param settings
	 *            - A NetworkSettings object with fields for the management IP,
	 *            NTP servers, and so on.
	 * @return - A NetworkSettingsStatus object indicating the status of each
	 *         field in the NetworkSettings object.
	 */
	@POST
    @Path("/network_settings/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public NetworkSettingsStatus validateNetworkSettings(NetworkSettings settings) {
		ConfigManager.addMessage("validate network setting  " + settings.toString());
		NetworkSettingsStatus status = new NetworkSettingsStatus();
		if(settings.getDnsDomain() != null && settings.getDnsDomain().indexOf(".") > 0){
			status.setDnsDomainValid(true);
		} else {
			status.setDnsDomainValid(false);
		}
		// check dns server1
		if(settings.getDnsServer1() != null && !"".equals(settings.getDnsServer1())){
			if(Utility.isValidIP(settings.getDnsServer1())){
				if(Utility.isPingable(settings.getDnsServer1())){
					status.setDnsServer1Status(IpStatus.PINGABLE);
				} else {
					status.setDnsServer1Status(IpStatus.NOT_PINGABLE);
				}
			} else {
				status.setDnsServer1Status(IpStatus.INVALID);
			}
		} else {
			status.setDnsServer1Status(IpStatus.PINGABLE);
		}
		// check dns server2
		if(settings.getDnsServer2() != null && !"".equals(settings.getDnsServer2())){
			if(Utility.isValidIP(settings.getDnsServer2())){
				if(Utility.isPingable(settings.getDnsServer2())){
					status.setDnsServer2Status(IpStatus.PINGABLE);
				} else {
					status.setDnsServer2Status(IpStatus.NOT_PINGABLE);
				}
			} else {
				status.setDnsServer2Status(IpStatus.INVALID);
			}
		} else {
			status.setDnsServer2Status(IpStatus.PINGABLE);
		}
		// check dns server3
		if(settings.getDnsServer3() != null && !"".equals(settings.getDnsServer3())){
			if(Utility.isValidIP(settings.getDnsServer3())){
				if(Utility.isPingable(settings.getDnsServer3())){
					status.setDnsServer3Status(IpStatus.PINGABLE);
				} else {
					status.setDnsServer3Status(IpStatus.NOT_PINGABLE);
				}
			} else {
				status.setDnsServer3Status(IpStatus.INVALID);
			}
		} else {
			status.setDnsServer3Status(IpStatus.PINGABLE);
		}
		// check management ip
		if(Utility.isValidIP(settings.getManagementIp())){
			if(Utility.isPingable(settings.getManagementIp())){
				status.setManagementIpStatus(IpStatus.PINGABLE);
			} else {
				status.setManagementIpStatus(IpStatus.NOT_PINGABLE);
			}
		} else {
			status.setManagementIpStatus(IpStatus.INVALID);
		}
		// check gateway
		if(Utility.isValidIP(settings.getGateway())){
			if(Utility.isPingable(settings.getGateway())){
				status.setGatewayStatus(IpStatus.PINGABLE);
			} else {
				status.setGatewayStatus(IpStatus.NOT_PINGABLE);
			}
		} else {
			status.setGatewayStatus(IpStatus.INVALID);
		}
		// check net mask
		if(Utility.isNetmask(settings.getNetmask())){
			status.setNetmaskValid(true);
		} else {
			status.setNetmaskValid(false);
		}
		// check ntp server1
		if(settings.getNtpServer1() != null && !"".equals(settings.getNtpServer1())){
			if(Utility.isPingable(settings.getNtpServer1())){
				status.setNtpServer1Status(IpStatus.PINGABLE);
			} else {
				status.setNtpServer1Status(IpStatus.NOT_PINGABLE);
			}
		} else {
			status.setNtpServer1Status(IpStatus.PINGABLE);
		}
		// check ntp server2
		if(settings.getNtpServer2() != null && !"".equals(settings.getNtpServer2())){
			if(Utility.isPingable(settings.getNtpServer2())){
				status.setNtpServer2Status(IpStatus.PINGABLE);
			} else {
				status.setNtpServer2Status(IpStatus.NOT_PINGABLE);
			}
		} else {
			status.setNtpServer2Status(IpStatus.PINGABLE);
		}
		// check ntp server3
		if(settings.getNtpServer3() != null && !"".equals(settings.getNtpServer3())){
			if(Utility.isPingable(settings.getNtpServer3())){
				status.setNtpServer3Status(IpStatus.PINGABLE);
			} else {
				status.setNtpServer3Status(IpStatus.NOT_PINGABLE);
			}
		} else {
			status.setNtpServer3Status(IpStatus.PINGABLE);
		}
		// check hyperfiler name
		if(settings.getHyperfilerName() != null && settings.getHyperfilerName().length() < 15){
			status.setHyperfilerNameValid(true);
		} else {
			status.setHyperfilerNameValid(false);
		}
		ConfigManager config = ConfigManager.getInstance();
		NTPCfg ntp = new NTPCfg();
		ntp.setNtpServer1(settings.getNtpServer1());
		ntp.setNtpServer2(settings.getNtpServer2());
		ntp.setNtpServer3(settings.getNtpServer3());
		config.configure(ntp);
		DnsCfg dns = new DnsCfg();
		dns.setSearch1(settings.getDnsServer1());
		dns.setSearch2(settings.getDnsServer2());
		dns.setSearch3(settings.getDnsServer3());
		dns.setServer1(settings.getDnsServer1());
		dns.setServer2(settings.getDnsServer2());
		dns.setServer3(settings.getDnsServer3());
		config.configure(dns);
		
		config.configure("gate_way", settings.getGateway());
		config.configure("net_mask", settings.getNetmask());
		config.configure("dns_domain", settings.getDnsDomain());
		config.configure("hyperfile_name", settings.getHyperfilerName());
		config.configure("management_ip", settings.getManagementIp());

		return status;
	}

	/**
	 * Specify the set of IP addresses that are available to assign to servers
	 * and VMs.
	 * 
	 * @param ipInfo
	 *            - An IpInfo object detailing the IP address ranges from which
	 *            to select IPs for the servers.
	 * @return - An IpAssignments object identifying the IP addresses allocated
	 *         for the specified servers.
	 */
	@POST
    @Path("/ipinfo/set")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public IpAssignments setIpInfo(IpInfo ipInfo) {
		ConfigManager.addMessage("set ip infomation  " + ipInfo.toString());
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		return bootstrap.setIpInfo(ipInfo);
	}

	/**
	 * Instruct back-end to commence the OS install process.
	 */
	@POST
    @Path("/os_install/start")
	@Produces(MediaType.APPLICATION_JSON)
    public Map<Long, String> startOsInstall() {
		ConfigManager.addMessage("start install all server.");
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		return bootstrap.startOsInstall();		
	}
	
	/**
	 * Retrieve the current OS installation progress of all servers.
	 * 
	 * @return - A map of servers being installed and their percentage that
	 *         their installs have progressed.
	 */
	@POST
    @Path("/os_install/progress")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Long, OsInstallProgress> getOsInstallProgress() {
		ConfigManager.addMessage("get all server install progress.");
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		return bootstrap.getOsInstallProgress();		
	}

	/**
	 * Retrieve the filename URL pointing to the install log of a specified
	 * server. The caller can then open the URL and process it as needed.
	 * 
	 * @param id
	 *            - The id of a discovered server.
	 * @return - The filename for the logs collected during the install of the
	 *         server with the specified id.
	 */
	@POST
    @Path("/os_install/log/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getOsInstallLog(@PathParam("id") long id) {
		ConfigManager.addMessage("get server install log for id " + id);
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		DiscoverInfo discover = bootstrap.getServer(id);
		return discover.getLogFile();
	}

	/**
	 * Cancel all active OS installs currently running. This causes the servers
	 * to shutdown and power off.
	 */
	@POST
	@Path("/os_install/cancel_all")
	public void cancelOsInstalls() {
		ConfigManager.addMessage("cancel install for all servers.");
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		bootstrap.cancelOsInstalls();		
	}

	/**
	 * Retrieve the latest progress information for the VMs being installed on
	 * the servers. This is intended to be called after the user has progressed
	 * to the next page after the OS installs have all completed (or failed).
	 * The boxes will presumably be powered off at this point and the user will
	 * need to go into the server room, remove the USB keys, and then power the
	 * boxes back on. Repeated calls will need to be made to this function to
	 * track the VM install progress of the systems. Note that no progress will
	 * be reported until the systems are back up and post reboot activity has
	 * started started. This means there will be a period during which time a
	 * percent complete of 0 will be return by this call for each system as they
	 * are rebooting. Eventually a call to this function will report that all VM
	 * installs have reached 100 percent and the VmInstallProgress object will
	 * indicate if any failures occurred.
	 * 
	 * @param idList
	 *            - A list of the servers for which to monitor VM install
	 *            activity.
	 * @return - A map of servers being installed and the percentage that their
	 *         installs have progressed.
	 */
	@POST
    @Path("/vm_install/progress")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<Long, VmInstallProgress> getVmInstallProgress(List<Long> idList) {
		ConfigManager.addMessage("get vm install progress.");
		Map<Long, VmInstallProgress> vmProgress = new HashMap<Long, VmInstallProgress>();
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		DiscoverInfo[] servers = bootstrap.getAllServers();
		for(DiscoverInfo server : servers){
			if(idList.contains(server.getId())){
				vmProgress.put(server.getId(), bootstrap.getVmProgress(server));
			}
		}
		return vmProgress;
	}

	/**
	 * Retrieve VM install information for the specified server.
	 * 
	 * @param id
	 *            - The id of a server.
	 * @return - VM log info for the specified.
	 */
	@POST
    @Path("/vm_install/log/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getVmInstallLog(@PathParam("id") long id) {
		ConfigManager.addMessage("get vm install log.");
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		return bootstrap.getVmInstallLog(id);
	}

	/**
	 * Cancel all active VM installs currently running.
	 */
	@POST
    @Path("/vm_install/cancel_all")
    public void cancelVmInstalls() {
		ConfigManager.addMessage("cancel vm install .");
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		bootstrap.cancelVmInstall();
	}

	/**
	 * Initialize the hyperfiler, launching the VMs required based on the
	 * parameters specified.
	 * 
	 * @param idList
	 *            - A list of the servers to initialize.
	 * @param file_size
	 *            - Setting for the expected average file size: 1=small files,
	 *            2=medium files, 3=large files, and 4=mixed-size files.
	 * @param ns_repfactor
	 *            - The namespace replication factor to configure.
	 */
	@POST
    @Path("/init_hf/start/{file_size}/{ns_repfactor}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void initHf(List<Long> idList, @PathParam("file_size") int fileSize, @PathParam("ns_repfactor") long nsRepFactor) {	
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		bootstrap.initHf(idList,fileSize,nsRepFactor);
	}

	/**
	 * Retrieve the latest progress information for the hyperfiler
	 * initialization process.
	 * 
	 * @return - A map of servers being initialized and the percentage that
	 *         their initialization has progressed.
	 */
	@POST
    @Path("/init_hf/progress")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<Long, InitHfProgress> getInitHfProgress() {
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		return bootstrap.getHfProgress();
	}

    /**
     * Retrieve information about the hypervisor with the specified id. This can be called while the
     * init_hf operation is in progress to see what has been accomplished so far.
     */
    @POST
    @Path("/init_hf/log/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getInitHfLog(@PathParam("id") long id) {
    	ConfigManager.addMessage("get init hf log for id  " + id);
    	BootstrapManager bootstrap = BootstrapManager.getInstance();
    	DiscoverInfo server = bootstrap.getServer(id);
		StringBuffer lines = new StringBuffer();
		if(server != null){
			File f = new File(server.getLogFile());
			if (f.canRead()) {
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(f));
					String line = br.readLine();
					while (line != null) {
						lines.append(line).append("\n");
						line = br.readLine();
					}
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
						}
					}
				}
			}
		}
        return lines.toString();
    }
    
	/**
	 * Retrieve information about the hyperfiler after all servers have
	 * completed their initialization process.
	 * 
	 * @return - A map of servers and their VM lists.
	 */
	@POST
    @Path("/init_hf/info")
    @Produces(MediaType.APPLICATION_JSON)
    public HfInfo getInitHfInfo() {
		BootstrapManager bootstrap = BootstrapManager.getInstance();
		return bootstrap.getHfInfo();
	}

	/**
	 * Instruct the bootstrap application to terminate. This occurs when the
	 * remote MSP has been started and the UI changes to that web server to
	 * continue the install process.
	 */
	@POST
    @Path("/app/exit")
    public void terminateBootstrap() {
		ConfigManager.addMessage("exit this app.");
	}
}
