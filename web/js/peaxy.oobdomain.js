var peaxy = peaxy || {};

peaxy.OobDomain = {
	SystemConfigModel : function(){
		this.cluster_name;
		this.management_ip;
	    this.cluster_id;	    
	    //Time
	    this.ntp_server1;
	    this.ntp_server2;
	    this.ntp_server3;
	    this.time_zone;	    
	    // DNS
	    this.dns_search1;
	    this.dns_search2;
	    this.dns_search3;
	    this.dns_server1;
	    this.dns_server2;
	    this.dns_server3;
	    // NET
	    this.gate_way;
	    this.subnet_mask;
	    this.dns_domain;
	    // Email setting
	    this.email_protocol;
	    this.email_server;
	    this.email_port;
	    this.email_login;
	    this.email_password;
	    this.email_from;	    
	    //AAA
	    this.user_name;
	    this.password;
	    this.user_email;	    
	    //Misc setting
	    this.cardinality;
	    this.spare_count;
	    this.replacement_enabled;
	    this.syslogEnabled;
	    
	    this.stat_server_ip;
	    this.stat_server_port;
	    
	    this.log_server_ip;
	    this.log_server_port;
	    
	    this.storage_classes = [ function(){
	    	this.name;	        
	        this.description;	        
	        this.replication_factor;
	        this.capacity_gb;
	        this.device_type;	            
	        this.default_sc;
	    }];	    
	    this.ingest_policy = [ function(){
	    	this.id;
	    	this.name;
	    	this.group_id;
	    	this.parent_path;
	    	this.file_type;
	    	this.recursive = true;
	    	this.sc_id;
	    	this.storage_class_name;
	    	this.flag = 'DEFAULT_RULE';
	    }];
	},
	SystemConfigViewModel :  function() {
	   this.userName;
	   this.password;
	   this.userEmail; 
	   this.gateWay;
	   this.subnetMask;
	   this.dnsDomain;
	   this.dnsServer;
	   this.ntpServer;
	   this.hyperfilerName;
	   this.encryption;  
	   this.managementIp;
	   this.replicationValue;
	   this.averageFileSize;/*e.g 1,2,3,4*/
	   this.dataRatioValue;
	   this.timeZone;
	   this.scList = [ function(){
			this.performanceLevel;	//performance : 1-16
			this.className;			//className
			this.replicas;			//replicas : 1-4
			this.usableSpace;		//number
			this.freeSpace;			//number
			this.totalSpace;		//number
			this.usedSpace;			//number
			this.flexible;			//flexible : YES/NO
			this.defaultValue;		//statesName: DEFAULT/''
	   } ];
	   this.ingestList = [ function(){
			this.id;
			this.name;
			this.condition = [];
			this.ands = [function(){
				this.key;
				this.vals = [];
			}];
			this.others = [function(){
				this.key;
				this.val;
				this.type;
			}];
			this.description;
			this.schedule = function(){
				this.start;
				this.time;
				this.execute;
				this.date;
				this.zone;
				this.repeat;
			};
	   } ];
	   this.totalSpace;
	   this.unallocated;
	   this.allocated;
	   this.ratio;
	   this.redundancy;
	   this.maxCapacity = [];
	}
}
