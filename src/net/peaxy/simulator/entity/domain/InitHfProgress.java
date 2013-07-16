package net.peaxy.simulator.entity.domain;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class InitHfProgress extends PeaxyBaseDomain{
    private int percent_complete;
    private InitHfStatus status;
    private List<String> success_list;
    private List<String> failed_list;
    
    @JsonProperty("percent_complete")
    public int getPercentComplete() {
        return percent_complete;
    }
    
    public void setPercentComplete(int percentComplete) {
        this.percent_complete = percentComplete;
    }
    
    @JsonProperty("status")
    public InitHfStatus getStatus() {
        return status;
    }
    
    public void setStatus(InitHfStatus status) {
        this.status = status;
    }

    @JsonProperty("success_list")
   public List<String> getSuccessList() {
        return success_list;
    }

    public void setSuccessList(List<String> successList) {
        this.success_list = successList;
    }

    @JsonProperty("failed_list")
    public List<String> getFailedList() {
        return failed_list;
    }

    public void setFailedList(List<String> failedList) {
        this.failed_list = failedList;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("InitHfProgress [percentComplete=")
                .append(percent_complete).append(", status=").append(status)
                .append(", successList=").append(success_list)
                .append(", failedList=").append(failed_list).append("]");
        return builder.toString();
    }
}
