package net.peaxy.simulator.entity.domain;

import org.codehaus.jettison.json.JSONObject;

public interface JSONable {
	JSONObject toJSON();
}
