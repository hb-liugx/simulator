var peaxy = peaxy || {};
peaxy.BootstrapDomain = {
	DiscoverViewModel : function(){
		this.hostname;
		this.id;
		this.manufacturer;
		this.product;
		this.serialNumber;
		this.memory;
		this.diskList = [function(){
			this.name;
			this.size;
		}];
		this.interfaceList = [function(){
			this.name;
			this.speed;
			this.isActive;
			this.isBonded;
		}];
		this.ipCount;
		this.hasBonded;
	},
	FileListResponseViewModel : function(){
		this.fileSizes = [];
		this.nsdsRatio = [];
	},
	ValidateRequestViewModel : function(){
		this.hyperfilerName;
		this.managementIp;
		this.gateway;
		this.netmask;
		this.dnsDomain;
		this.dnsServer = [];
		this.ntpServer = [];
	},
	ValidateResponseViewModel : function(){
		this.hasError;
		this.hyperfilerName;
		this.managementIp;
		this.gateway;
		this.netmask;
		this.dnsDomain;
		this.dnsServer;
		this.ntpServer;
	},
	BeaconResponseViewModel : function(){
		this.id;
		this.beacon;
	},
	IpinfoRequestViewModel : function(){
		this.ids = [];
		this.ips = [];
	},
	IpinfoResponseViewModel : function(){
		this.ipList =  [ function(){
			this.id;
			this.ip;
		} ];
		this.additionalNeeded;
		this.inUse = [];
		this.available = [];
	},
	OsStartResponseViewModel : function(){
		this.id;
		this.message;
	},
	OsProgressResponseViewModel : function(){
		this.id;
		this.progress = function(){
			this.percent;
			this.status;
		};
	},
	VmProgressResponseViewModel : function(){
		this.id;
		this.progress = function(){
			this.percentComplete;
			this.installsPending;
			this.installsStarted;
			this.installsCompleted;
			this.installsFailed;
			this.failedList = [];
		};
	},
	HfProgressResponseViewModel : function(){
		this.id;
		this.progress = function(){
			this.percentComplete;
			this.status;
		};
	},
	HvInfoResponseViewModel : function(){
		this.hvinfos = [function(){
			this.id;
			this.hv = function(){
				this.id;
				this.hostname;
				this.ip;
				this.type;
				this.vmList = [function(){
					this.id;
					this.name;
					this.os;
					this.captical;
				}];
			};
		}];
	}
}
