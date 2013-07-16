package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonProperty;

public class RuleFilter extends PeaxyBaseDomain {
	private Integer id;

	private String name;

	private String path;

	private String type;

	private Boolean recursive;

	private long groupId;

	private Integer scId;

	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("prefix")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@JsonProperty("suffix")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("recursive")
	public Boolean getRecursive() {
		return recursive;
	}

	public void setRecursive(Boolean recursive) {
		this.recursive = recursive;
	}

	@JsonProperty("group_id")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@JsonProperty("sc_id")
	public Integer getScId() {
		return scId;
	}

	public void setScId(Integer scId) {
		this.scId = scId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RuleFilter [id=").append(id).append(", name=")
				.append(name).append(", path=").append(path).append(", type=")
				.append(type).append(", recursive=").append(recursive)
				.append(", groupId=").append(groupId).append(", scId=")
				.append(scId).append("]");
		return builder.toString();
	}
}
