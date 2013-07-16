package net.peaxy.simulator.entity.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.peaxy.simulator.util.Utility;

import org.codehaus.jackson.annotate.JsonProperty;

public class DiscoverInfo extends PeaxyBaseDomain {
	private String hostname;
	private long id;
	private String manufacturer;
	private String product;
	private String serial_number;
	private long memory;
	private List<DiskInfo> disk_list;
	private List<InterfaceInfo> interface_list;
	private int ip_count;

	private boolean beacon;

	public boolean isBeacon() {
		return beacon;
	}

	public void setBeacon(boolean beacon) {
		this.beacon = beacon;
	}

	public OsInstallProgress getProgress() {
		return progress;
	}

	public void setProgress(OsInstallProgress progress) {
		this.progress = progress;
	}

	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

	private OsInstallProgress progress;
	private String logFile;

	public static DiscoverInfo generate(int i) {
		Random r = new Random(System.nanoTime());
		DiscoverInfo discover = new DiscoverInfo();
		discover.setId(Utility.getID());
		discover.setSerialNumber(Utility.generateSN(16));
		List<DiskInfo> disk = new ArrayList<DiskInfo>();
		for (int d = 0; d < 5; d++) {
			disk.add(DiskInfo.generate(d));
		}
		discover.setDiskList(disk);
		int ipCount = 0;
		List<InterfaceInfo> interfaces = new ArrayList<InterfaceInfo>();
		for (int d = 0; d < 10; d++) {
			InterfaceInfo inf = InterfaceInfo.generate(d);
			interfaces.add(inf);
			if(inf.isActive()){
				ipCount++;
			}
		}
		discover.setInterfaceList(interfaces);
		discover.setHostname( Utility.generateSN(15));
		discover.setIpCount(ipCount);
		discover.setManufacturer( Utility.generateSN(5));
		discover.setMemory(r.nextInt(20));
		discover.setProduct(Utility.generateSN(2));
		
		discover.setProgress(new OsInstallProgress());
		discover.getProgress().setStatus(OsInstallStatus.SUCCESSFUL);
		return discover;
	}

	@JsonProperty("hostname")
	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	@JsonProperty("id")
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@JsonProperty("manufacturer")
	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@JsonProperty("product")
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	@JsonProperty("serial_number")
	public String getSerialNumber() {
		return serial_number;
	}

	public void setSerialNumber(String serialNumber) {
		this.serial_number = serialNumber;
	}

	@JsonProperty("memory")
	public long getMemory() {
		return memory;
	}

	public void setMemory(long memory) {
		this.memory = memory;
	}

	@JsonProperty("disk_list")
	public List<DiskInfo> getDiskList() {
		return disk_list;
	}

	public void setDiskList(List<DiskInfo> diskList) {
		this.disk_list = diskList;
	}

	@JsonProperty("interface_list")
	public List<InterfaceInfo> getInterfaceList() {
		return interface_list;
	}

	public void setInterfaceList(List<InterfaceInfo> interfaceList) {
		this.interface_list = interfaceList;
	}

	@JsonProperty("ip_count")
	public int getIpCount() {
		return ip_count;
	}

	public void setIpCount(int ipCount) {
		this.ip_count = ipCount;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("id=").append(id).append(",");
		buffer.append("manufacturer=").append(manufacturer).append(",");
		buffer.append("product=").append(product).append(",");
		buffer.append("serial-number=").append(serial_number).append(",");
		buffer.append("memory=").append(memory).append(",");
		buffer.append("disk-list=");
		for (DiskInfo di : disk_list)
			buffer.append(di.toString() + " ");
		buffer.append(",");
		buffer.append("interface-list=");
		for (InterfaceInfo di : interface_list)
			buffer.append(di.toString() + " ");
		return buffer.toString();
	}

	public long getCapacity() {
		long capacity = 0;
		for(DiskInfo di : disk_list){
			capacity += di.getSize();
		}
		return capacity;
	}
}
