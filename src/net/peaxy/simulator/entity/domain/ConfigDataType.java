package net.peaxy.simulator.entity.domain;

import java.util.HashMap;

public enum ConfigDataType {

	DataVersionMap {
		public String toString() {
			return "ver_map";
		}
	},
	NTPCfg {
		public String toString() {
			return "ntp";
		}
	},
	EmailCfg {
		public String toString() {
			return "email";
		}
	},
	StatsCfg {
		public String toString() {
			return "stats";
		}
	},
	DNSCfg {
		public String toString() {
			return "dns";
		}
	},
	KVCfg {
		public String toString() {
			return "kv";
		}
	},
	ClusterCfg {
		public String toString() {
			return "cluster";
		}
	},
	HsCfg {
		public String toString() {
			return "hs";
		}
	},
	LogCfg {
		public String toString() {
			return "log";
		}
	},
	RuleCfg {
		public String toString() {
			return "rule";
		}
	},
	RulesBitmap {
		public String toString() {
			return "rulemap";
		}
	};

	private static HashMap<ConfigDataType, Class<? extends ConfigData>> classMap;
	static {
		classMap = new HashMap<ConfigDataType, Class<? extends ConfigData>>(
				ConfigDataType.values().length);
		classMap.put(ConfigDataType.DataVersionMap, DataVersionMap.class);
		classMap.put(ConfigDataType.ClusterCfg, ClusterCfg.class);
		classMap.put(ConfigDataType.NTPCfg, NTPCfg.class);
		classMap.put(ConfigDataType.EmailCfg, EmailCfg.class);
		classMap.put(ConfigDataType.LogCfg, LogCfg.class);
		classMap.put(ConfigDataType.StatsCfg, StatsCfg.class);
		classMap.put(ConfigDataType.DNSCfg, DnsCfg.class);
		classMap.put(ConfigDataType.KVCfg, JsonKeyValue.class);
		classMap.put(ConfigDataType.HsCfg, HsCfg.class);
		classMap.put(ConfigDataType.RuleCfg, RuleCfg.class);
		classMap.put(ConfigDataType.RulesBitmap, RulesBitmap.class);
	}

	public Class<? extends ConfigData> getTypeClass() {
		return classMap.get(this);
	}
}
