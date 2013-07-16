package net.peaxy.simulator.entity.domain;

import javax.xml.bind.annotation.XmlType;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonTypeName;

@XmlType(name = "DataPolicy")
@JsonTypeName(MigrationPolicy.TYPE)
public class MigrationPolicy extends PeaxyBaseDomain implements DataPolicy {
	public static final String TYPE = "migration";

	private Integer id;

	private String name;

	private String description;

	private Condition condition;

	private Integer destination;

	/**
	 * Internal tracking ID
	 * 
	 * @return
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * The namne of the policy
	 * 
	 * @return
	 */
	@JsonProperty("name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * The description of the policy. Max length 128
	 * 
	 * @return
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty
	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	/**
	 * The ID of the destination storage class
	 * 
	 * @return
	 */
	@JsonProperty("destination_sc")
	public Integer getDestination() {
		return destination;
	}

	public void setDestination(Integer destination) {
		this.destination = destination;
	}
}
