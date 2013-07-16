package net.peaxy.simulator.entity.domain;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Marker interface for data management policy: migration, ingest, replication,
 * ...etc.
 * 
 * @author mango
 * 
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(name = MigrationPolicy.TYPE, value = MigrationPolicy.class), @Type(name=RuleCfg.TYPE, value=RuleCfg.class) })
public interface DataPolicy {

}