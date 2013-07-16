package net.peaxy.simulator.entity.domain;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

public class RuleCfg extends ConfigData implements DataPolicy {
	public static final String TYPE = "ingest";
	public static final int SCHEMA_VERSION = 1;
	private static Logger logger = Logger.getLogger(RuleCfg.class);

	public static final String MATCH_ALL_TYPE = "*";
	/* Max len is based on the max column length set in db_schema.xml */
	public static final int MAX_PARENT_PATH_LEN = 1024;
	public static final int MAX_FILE_TYPE_LEN = 255;

	private Integer id;

	private String name;

	private String description;
	
	private long group_id;

	private String parent_path;

	private String file_type;

	private boolean recursive;

	private Integer sc_id;

	/* To configure with script, pass the storage class name */
	private String storage_class_name;

	private RuleFlag flag;

	public RuleCfg() {
		setType(ConfigDataType.RuleCfg);
		setSchemaVersion(SCHEMA_VERSION);
	}

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

	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonProperty("parent_path")
	public String getParentPath() {
		return parent_path;
	}

	public void setParentPath(String parentPath) {
		this.parent_path = parentPath;
	}

	@JsonProperty("file_type")
	public String getFileType() {
		return file_type;
	}

	public void setFileType(String fileType) {
		this.file_type = fileType;
	}

	@JsonProperty("recursive")
	public boolean isRecursive() {
		return recursive;
	}

	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	@JsonProperty("sc_id")
	public Integer getStorageClassId() {
		return sc_id;
	}

	@JsonProperty("group_id")
	public Long getGroupId() {
		return group_id;
	}

	public void setGroupId(Long groupId) {
		this.group_id = groupId;
	}

	@JsonIgnore
	public RuleFlag getFlag() {
		return flag;
	}

	public void setFlag(RuleFlag flag) {
		// Leave the DEFAULT_RULE flag as it is
		// its always marked for ADD (not allowed to be deleted)
		if (this.flag != RuleFlag.DEFAULT_RULE) {
			this.flag = flag;
		} else if (flag == RuleFlag.MARKED_FOR_DELETE) {
			logger.error("Trying to delete DEFAULT_RULE");
			throw new RuntimeException("Trying to delete DEFAULT_RULE");
		}
	}

	public void setStorageClassId(Integer storageClassId) {
		this.sc_id = storageClassId;
	}

	@JsonProperty("storage_class_name")
	public String getStorageClassName() {
		return storage_class_name;
	}

	public void setStorageClassName(String storageClassName) {
		this.storage_class_name = storageClassName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RuleCfg [id=").append(id).append(", name=")
				.append(name).append(", groupId=").append(group_id)
				.append(", parentPath=").append(parent_path)
				.append(", fileType=").append(file_type).append(", recursive=")
				.append(recursive).append(", storageClassId=")
				.append(sc_id).append(", flag=").append(flag)
				.append("]");
		return builder.toString();
	}

	public void prePack() throws IOException {
		// No need to pass storage class name in gossip packets
		storage_class_name = null;
	}

	/**
	 * Call this function once right after receiving from UI before validate
	 */
	public void normalize() {
	}

	@JsonIgnore
	public boolean isNullGroupId() {
		if (group_id == 0)
			return true;

		return false;
	}

	public void validate() {

	}

	/**
	 * Check for duplicate rule
	 * 
	 * @param other
	 */
	public void validateWithOther(RuleCfg other) {

	}

	public static RuleCfg create() {
		RuleCfg r = new RuleCfg();
		r.setFileType("exe");
		r.setFlag(RuleFlag.DEFAULT_RULE);
		r.setGroupId(1L);
		r.setId(2);
		r.setName("rule1");
		r.setParentPath("/src");
		r.setRecursive(false);
		r.setStorageClassId(1);
		r.setStorageClassName("sc1");
		r.setType(ConfigDataType.RuleCfg);
		return r;
	}
}
