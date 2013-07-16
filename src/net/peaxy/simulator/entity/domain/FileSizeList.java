package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class FileSizeList extends PeaxyBaseDomain{
    String[] fileSizes;
    String[] nsdsRatio;
    
    public FileSizeList() {
        fileSizes = new String[] {
            "Small files (< 1MB)",
            "Medium files (< 20MB)",
            "Large files (> 20MB)",
            "Mixed size files"
        };
        nsdsRatio = new String[] {
            "1:2",
            "1:4",
            "1:6",
            "1:4"
        };
    }
    
    @JsonProperty("fileSizes")
	public String[] getFileSizes() {
		return fileSizes;
	}

	public void setFileSizes(String[] fileSizes) {
		this.fileSizes = fileSizes;
	}
	@JsonProperty("nsdsRatio")
	public String[] getNsdsRatio() {
		return nsdsRatio;
	}

	public void setNsdsRatio(String[] nsdsRatio) {
		this.nsdsRatio = nsdsRatio;
	}
    
    
}
