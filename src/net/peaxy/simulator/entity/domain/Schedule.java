package net.peaxy.simulator.entity.domain;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonSubTypes.Type;
import org.codehaus.jackson.annotate.JsonTypeInfo;

/**
 * Marker interface for a schedule
 * 
 * @author mango
 * 
 */
@XmlRootElement
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.WRAPPER_OBJECT, property="type")
@JsonSubTypes({@Type(name=CronSchedule.TYPE, value=CronSchedule.class), @Type(name=SimpleSchedule.TYPE, value=SimpleSchedule.class)})
public interface Schedule {

}
