package net.peaxy.simulator.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.peaxy.simulator.data.Record;
import net.peaxy.simulator.data.TableSpace;
import net.peaxy.simulator.entity.domain.DSPerfType;
import net.peaxy.simulator.entity.domain.DiscoverInfo;
import net.peaxy.simulator.entity.domain.HfInfo;
import net.peaxy.simulator.entity.domain.HvInfo;
import net.peaxy.simulator.entity.domain.HvType;
import net.peaxy.simulator.entity.domain.InitHfProgress;
import net.peaxy.simulator.entity.domain.InitHfStatus;
import net.peaxy.simulator.entity.domain.InterfaceInfo;
import net.peaxy.simulator.entity.domain.IpAssignments;
import net.peaxy.simulator.entity.domain.IpInfo;
import net.peaxy.simulator.entity.domain.OsInstallProgress;
import net.peaxy.simulator.entity.domain.OsInstallStatus;
import net.peaxy.simulator.entity.domain.PeaxyBaseDomain;
import net.peaxy.simulator.entity.domain.VmConfig;
import net.peaxy.simulator.entity.domain.VmInstallProgress;
import net.peaxy.simulator.util.Utility;

public class BootstrapManager {
	private static final String SERVER_TABLE_NAME = "discover";
	private static final String VM_TABLE_NAME = "vm";
	private static final String HV_TABLE_NAME = "hv";
	private static final String HF_TABLE_NAME = "hf";
	private static int requestProgressCount = 0;
	
	private final int REBOOT_TIME = 6;
	private final int SERVER_COUNT = 20;
	private List<DiscoverInfo> servers = new ArrayList<DiscoverInfo>();
	private Map<Long,VmInstallProgress> vmProgress;
	private Map<Long,InitHfProgress> hfProgress;
	private IpAssignments ipAssign;
	
	static {
		if(!TableSpace.getInstance().hasTable(SERVER_TABLE_NAME)){
    		TableSpace.getInstance().addTable(SERVER_TABLE_NAME);
    	}
		if(!TableSpace.getInstance().hasTable(VM_TABLE_NAME)){
    		TableSpace.getInstance().addTable(VM_TABLE_NAME);
    	}
		if(!TableSpace.getInstance().hasTable(HV_TABLE_NAME)){
    		TableSpace.getInstance().addTable(HV_TABLE_NAME);
    	}
		if(!TableSpace.getInstance().hasTable(HF_TABLE_NAME)){
    		TableSpace.getInstance().addTable(HF_TABLE_NAME);
    	}
	}
	
	private static BootstrapManager instance = null;	
	
	private BootstrapManager(){
		vmProgress = new HashMap<Long, VmInstallProgress>();
		hfProgress = new HashMap<Long, InitHfProgress>();
	}
	
	public static BootstrapManager getInstance(){
		if(instance == null){
			instance = new BootstrapManager();
		}
		return instance;
	}
	
	public DiscoverInfo[] getAllServers(){
		if(servers.size() == 0) {
			List<Record> list = TableSpace.getInstance().getAllData(SERVER_TABLE_NAME);
			if (list.isEmpty()) {
				try {
					Thread.sleep(new Random(System.nanoTime()).nextInt(10)*1000L + 5000L);
				} catch (Exception e) {
				}
				for (int i = 0; i < SERVER_COUNT; i++) {
					DiscoverInfo discover = DiscoverInfo.generate(i);
					TableSpace.getInstance().setData(SERVER_TABLE_NAME,
							"" + discover.getId(), discover.toJSON());
					servers.add(discover);
				}
			} else {
				for (Record json : list) {
					servers.add((DiscoverInfo) PeaxyBaseDomain.toDomain(json
							.toJson().toString()));
				}
			}
		}
		return servers.toArray(new DiscoverInfo[0]);
	}
	
	public DiscoverInfo getServer(long id){
		for(DiscoverInfo server : servers){
			if(server.getId() == id)
				return server;
		}
		return null;
	}
	
	private HvInfo[] getAllHVs(){
		List<HvInfo> hvs = new ArrayList<HvInfo>();
		List<Record> list = TableSpace.getInstance().getAllData(HV_TABLE_NAME);
		if (!list.isEmpty()) {
			for (Record json : list) {
				hvs.add((HvInfo) PeaxyBaseDomain.toDomain(json
						.toJson().toString()));
			}
		}
		return hvs.toArray(new HvInfo[0]);
	}
	
	private HvInfo getHV(long id){
		HvInfo[] hvs = getAllHVs();
		for(HvInfo hv : hvs){
			if(hv.getId() == id)
				return hv;
		}
		return null;
	}
	
	private void saveHV(HvInfo hv){
		if(hv != null){
			TableSpace.getInstance().setData(HV_TABLE_NAME,
				"" + hv.getId(), hv.toJSON());
		}
	}
	
	public void saveServer(DiscoverInfo server){
		if(server != null){
			TableSpace.getInstance().setData(SERVER_TABLE_NAME,
				"" + server.getId(), server.toJSON());
		}
	}
	
	public Map<Long, Boolean> getBeacons(List<Long> idList) {
		Map<Long, Boolean> beacons = new HashMap<Long, Boolean>();
		for(long id : idList){
			DiscoverInfo server = getServer(id);
			if(server != null){
				beacons.put(id, server.isBeacon());
			}
		}
		return beacons;
	}
	
	public IpAssignments setIpInfo(IpInfo ipInfo) {
		List<String> allIPs = parseIpInfo(ipInfo);
		IpAssignments assign = new IpAssignments();
		List<String> available = new ArrayList<String>();
		List<String> inUse = new ArrayList<String>();
		HashMap<Long, String> ipList = new HashMap<Long, String>();
		int ipCount = allIPs.size();
		int ipNeed  = 0;
		for(long id : ipInfo.getIdList()){
			ipNeed += this.getServer(id).getIpCount();
		}
		for(int i=ipCount-1;i>-1;i--){
			if(Utility.isPingable(allIPs.get(i))){
				inUse.add(allIPs.get(i));
				allIPs.remove(i);
			} 
		}
		ipCount = allIPs.size();		
		if(ipCount < ipNeed) {
			assign.setAdditionalNeeded(ipNeed - ipCount);
		} else {
			assign.setAdditionalNeeded(0);
		}
		int index = 0;
		for(long id : ipInfo.getIdList()){
			String ip = "";
			DiscoverInfo  server = this.getServer(id);
			for(InterfaceInfo inf : server.getInterfaceList()){
				if(inf.isActive() && index < ipCount)
					ip = (allIPs.get(index) + " ");
				index++;
			}
			ipList.put(id, ip);
		}
		for(int i = ipNeed; i < ipCount; i++){
			available.add(allIPs.get(i));
		}
		assign.setAvailable(available);
		assign.setInUse(inUse);
		assign.setIpList(ipList);
		this.ipAssign = assign;
		return assign;
	}
	
	public Map<Long, String> startOsInstall() {
		Map<Long, String> errors = new HashMap<Long, String>();
		DiscoverInfo[] servers = getAllServers();
		for (DiscoverInfo server : servers) {
			if(this.ipAssign.getIpList().containsKey(server.getId())){
				server.getProgress().setPercentComplete(0);
				String logFile = "log/install_"+server.getId()+".log";
				new File(logFile).delete();
				Utility.writeInstallLog(logFile, "Begin install server " + server.getHostname());
				server.setLogFile(logFile);
				try {
					if(new Random(System.nanoTime()).nextInt(100) > 2){
						server.getProgress().setStatus(OsInstallStatus.SUCCESSFUL);
					} else {
						server.getProgress().setStatus(OsInstallStatus.FAILED);
						server.getProgress().setPercentComplete(100);
						errors.put(server.getId(), "Unable to start install on "+server.getHostname()+": Server did not respond to ping");
					}
				} catch (Exception e) {
					server.getProgress().setStatus(OsInstallStatus.FAILED);
					server.getProgress().setPercentComplete(100);
					errors.put(server.getId(), "Unable to start install on "+server.getHostname()+": Unable to send message to server");
				}			
				TableSpace.getInstance().setData(SERVER_TABLE_NAME,"" + server.getId(), server.toJSON());
			}
		}
		requestProgressCount = 0;
		return errors;
	}
	
	public void cancelOsInstalls() {
		DiscoverInfo[] servers = getAllServers();
		for (DiscoverInfo server : servers) {
			if(this.ipAssign.getIpList().containsKey(server.getId())){
				server.getProgress().setPercentComplete(0);
				server.getProgress().setStatus(OsInstallStatus.SUCCESSFUL);
				TableSpace.getInstance().setData(SERVER_TABLE_NAME,
						"" + server.getId(), server.toJSON());
				if(server.getLogFile() != null){
					File f = new File(server.getLogFile());
					f.delete();
				}
			}
		}
	}
	
	public Map<Long, OsInstallProgress> getOsInstallProgress() {
		Random r = new Random(System.nanoTime());
		Map<Long, OsInstallProgress> result = new HashMap<Long, OsInstallProgress>();
		requestProgressCount++;
		DiscoverInfo[] servers = getAllServers();
		for (DiscoverInfo server : servers) {
			if(this.ipAssign.getIpList().containsKey(server.getId())){
				int percent = server.getProgress().getPercentComplete();
				if (percent < 100 && server.getProgress().getStatus() != OsInstallStatus.FAILED) {
					server.getProgress().setPercentComplete(percent + r.nextInt(50));				
					percent = server.getProgress().getPercentComplete();
					if(percent >= 100){
						server.getProgress().setPercentComplete(100);
						server.getProgress().setStatus(OsInstallStatus.SUCCESSFUL);
						Utility.writeInstallLog(server.getLogFile(), "Server " + server.getHostname() + " is installed successfully.");
						createHV(server);
					} else {
						if(requestProgressCount > 4){
							server.getProgress().setStatus(OsInstallStatus.FAILED);
							server.getProgress().setPercentComplete(100);
							Utility.writeInstallLog(server.getLogFile(), "Install server " + server.getHostname() + " failed.");
						} else {
							Utility.writeInstallLog(server.getLogFile(), "Installing server " + server.getHostname() + ", " + percent + "% is completed.");
						}
					}
					saveServer(server);
				}
				result.put(server.getId(), server.getProgress());
			}
		}
		return result;
	}
	
	private List<String> parseIpInfo(IpInfo ipinfo){
		List<String> list = new ArrayList<String>();
		for(String ip : ipinfo.getIpRangeList()){
			list.addAll(Utility.getIpList(ip));
		}
		return list;
	}
	
	public void createHV(DiscoverInfo server) {
		HvInfo hv = new HvInfo();
		hv.setHostname(server.getHostname());
		hv.setIp(this.ipAssign.getIpList().get(server.getId()));
		hv.setId((int) server.getId());
		hv.setProcess(100);
		hv.setType(HvType.AWS);
		hv.setVmList(new ArrayList<VmConfig>());
		hv.setStatus(0);
		TableSpace.getInstance().setData(HV_TABLE_NAME,
				"" + hv.getId(), hv.toJSON());
	}
	
	public VmInstallProgress getVmProgress(DiscoverInfo server) {
		VmInstallProgress progress = null;
		if(vmProgress.containsKey(server.getId())){
			progress = vmProgress.get(server.getId());
		} else {
			progress = new VmInstallProgress();
			progress.setInstallsCompleted(0);
			progress.setInstallsFailed(0);
			progress.setInstallsPending(10);
			progress.setInstallsStarted(0);
			progress.setPercentComplete(0);
			progress.setFailedList(new ArrayList<String>());
			vmProgress.put(server.getId(), progress);
		}
		Random r = new Random(System.nanoTime());
		if(progress.getInstallsStarted() != 0 || r.nextInt(10) < REBOOT_TIME || progress.getPercentComplete() < 100){
			if(r.nextInt(10) > 2){ //finished
				progress.setInstallsCompleted(progress.getInstallsCompleted() + 1);
				progress.setInstallsStarted(progress.getInstallsStarted() + 1);
				progress.setInstallsPending(progress.getInstallsPending() - 1);
				progress.getFailedList().add("install vm successful "+progress.getInstallsCompleted()+"\n");
				
				HvInfo hv = this.getHV(server.getId());
				if(hv != null){
					VmConfig vm = new VmConfig();
					vm.setId((int)Utility.getID());
					vm.setName("VM"+vm.getId());
					vm.setIp(hv.getIp());
					vm.setCapacity((int)server.getCapacity()/10);
					vm.setDriveType(DSPerfType.LEVEL_1);
					vm.setOs("Linux");
					vm.setCores(2);
					hv.getVmList().add(vm);
					saveHV(hv);
				}
			} 
			progress.setPercentComplete(progress.getInstallsCompleted() * 10);
			if(progress.getPercentComplete() > 100){
				progress.setPercentComplete(100);
			}
		}
		return progress;
	}

	

	public String getVmInstallLog(long id) {
		if(vmProgress.containsKey(id)){
			//Record record = TableSpace.getInstance().getData(VM_TABLE_NAME, id+"");
			VmInstallProgress progress = vmProgress.get(id);
			StringBuffer buffer = new StringBuffer();
			if(progress != null){
				for(String s : progress.getFailedList()){
					buffer.append(s);
				}
			}
			return buffer.toString();
		}
		return "";
	}

	public void cancelVmInstall() {
		HvInfo[] hvs = getAllHVs();
		for(HvInfo hv : hvs){
			hv.getVmList().clear();
			TableSpace.getInstance().removeData(HV_TABLE_NAME, hv.getId()+"");
		}
		vmProgress.clear();
	}

	public Map<Long, InitHfProgress> getHfProgress() {
		HvInfo[] hvs = getAllHVs();
		InitHfProgress progress;
		for(HvInfo hv : hvs){
			if(hfProgress.containsKey((long)hv.getId())){
				progress = hfProgress.get((long)hv.getId());
			} else {
				progress = new InitHfProgress();
				progress.setPercentComplete(0);
				progress.setStatus(InitHfStatus.INCOMPLETE);
				progress.setFailedList(new ArrayList<String>());
				progress.setSuccessList(new ArrayList<String>());
				hfProgress.put((long)hv.getId(), progress);
			}
			progress.setPercentComplete(progress.getPercentComplete()+new Random(System.nanoTime()).nextInt(30));
			if(progress.getPercentComplete() > 100){
				progress.setPercentComplete(100);
				progress.setStatus(InitHfStatus.SUCCESSFUL);
			}
		}
		return hfProgress;
	}

	public HfInfo getHfInfo() {
		List<Record> list = TableSpace.getInstance().getAllData(HF_TABLE_NAME);
		if(list.size() > 0){
			return (HfInfo) PeaxyBaseDomain.toDomain(list.get(0)
					.toJson().toString());
		}
		return null;
	}

	public void initHf(List<Long> idList, int fileSize, long nsRepFactor) {
		hfProgress.clear();
		ConfigManager manager = ConfigManager.getInstance();
		// save file size
		manager.configure("file_size",""+fileSize);
		// save ns_repfactor
		manager.configure("ns_repfactor",""+nsRepFactor);
		
		HfInfo hf = new HfInfo();
		hf.setHvInfoMap(new HashMap<Integer, HvInfo>());
		for(long id : idList){
			HvInfo hv = this.getHV(id);
			if(hv != null){
				hf.getHvInfoMap().put((int)id, hv);
				hf.setTotalCapacity(hf.getTotalCapacity());
			}
		}
		hf.setLeaderId((int)Utility.getID());
		TableSpace.getInstance().setData(HF_TABLE_NAME,
				"" + hf.getLeaderId(), hf.toJSON());
		DataManager dm = DataManager.getInstance();
		dm.createDefaultSC();
	}	
}
