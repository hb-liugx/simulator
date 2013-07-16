var peaxy = peaxy || {};

peaxy.OOB = {
	loadDomain:function(handler){
		if(peaxy.OobDomain){
			handler();
		} else {
			peaxy.Communication.getScript('js/peaxy.oobdomain.js',handler);			
		}
	},
	// String getCompletion() @GET
	completion:function(handler){
		peaxy.Communication.get('/oob/completion',function(data, httpcode){
			if(jQuery.isFunction(handler)) handler(data, httpcode);
		},'json');
	},
	// String setSystemConfig(SystemConfig config) @PUT
	setConfig:function(config,handler){
		this.loadDomain(function(){
			peaxy.Communication.put('/oob/config/set',$.toJSON(toModel(config)),function(data, httpcode){
				if(jQuery.isFunction(handler)) 
					handler(data, httpcode);
			},'json');			
		});
		function toModel(view){
			var model = new peaxy.OobDomain.SystemConfigModel();
			model.cluster_name = view.hyperfilerName;
			model.management_ip = view.managementIp;
			//model.cluster_id = view.;
		    //Time
			var ntpServers = view.ntpServer.split('\n');
			if(ntpServers.length > 0)
				model.ntp_server1 = ntpServers[0];
			if(ntpServers.length > 1)
				model.ntp_server2 = ntpServers[1];
			if(ntpServers.length > 2)
				model.ntp_server3 = ntpServers[2];
		    model.time_zone = view.timeZone;
		    // DNS
		    var dnsServers = view.dnsServer.split('\n');
		    if(ntpServers.length > 0){
		    	model.dns_search1 = dnsServers[0]; 
		    	model.dns_server1 = dnsServers[0];
		    }
		    if(ntpServers.length > 1){
		    	model.dns_search2 = dnsServers[1]; 
		    	model.dns_server2 = dnsServers[1];
		    }
		    if(ntpServers.length > 2){
		    	model.dns_search3 = dnsServers[2]; 
		    	model.dns_server3 = dnsServers[2];
		    }
		    // Net
		    model.gate_way = view.gateWay;
		    model.subnet_mask = view.subnetMask;
		    model.dns_domain = view.dnsDomain;
		    // Email setting
		    //model.email_protocol = view.;
		    //model.email_server = view.;
		    //model.email_port = view.;
		    //model.email_login = view.;
		    //model.email_password = view.;
		    //model.email_from = view.;
		    //AAA
		    model.user_name = view.userName;
		    model.password = view.password;
		    model.user_email = view.userEmail;
		    //Misc setting
		    model.cardinality = view.replicationValue;
		    model.spare_count = view.averageFileSize;
		    model.replacement_enabled = true;
		    model.syslogEnabled = true;
		    //model.stat_server_ip = view.;
		    //model.stat_server_port = view.;
		    //model.log_server_ip = view.;
		    //model.log_server_port = view.;
		    model.storage_classes = [];
		    for(var i = 0;i<view.scList.length;i++){
		    	model.storage_classes[i] = {};
		    	model.storage_classes[i].name = view.scList[i].className;
		    	model.storage_classes[i].description = view.scList[i].className;
		    	model.storage_classes[i].replication_factor = view.scList[i].replicas;
		    	model.storage_classes[i].capacity_gb = view.scList[i].usableSpace * 1024;
		    	model.storage_classes[i].device_type = 'LEVEL_'+view.scList[i].performanceLevel;
		    	model.storage_classes[i].default_sc = view.scList[i].defaultValue == 'DEFAULT' ? true:false;
		    }
		    model.ingest_polic = [];
		    for(var i = 0;i<view.ingestList.length;i++){
		    	model.ingest_policy[i] = {};
		    	model.ingest_policy[i].id = 0;
		    	model.ingest_policy[i].name = view.ingestList[i].name;
		    	model.ingest_policy[i].description = view.ingestList[i].description;
		    	for(var j =0;j<view.ingestList[i].ands.length;j++){
		    		if(view.ingestList[i].ands[j].key == 'has file extension'){
		    			model.ingest_policy[i].file_type = view.ingestList[i].ands[j].vals[0];
		    		} else if(view.ingestList[i].ands[j].key == 'created in directory'){
		    			model.ingest_policy[i].parent_path = view.ingestList[i].ands[j].vals[0];
		    			model.ingest_policy[i].recursive = view.ingestList[i].ands[j].vals[1];
		    		} else if(view.ingestList[i].ands[j].key == 'is created by user group'){
		    			model.ingest_policy[i].group_id = view.ingestList[i].ands[j].vals[0];
		    		}
		    	}
		    	for(var j =0;j<view.ingestList[i].others.length;j++){
		    		if(view.ingestList[i].others[j].key == 'Store it on'){
		    			model.ingest_policy[i].sc_id = 0;
				    	model.ingest_policy[i].storage_class_name = view.ingestList[i].others[j].val;
		    		}
		    	}
		    	model.ingest_policy[i].flag = 'DEFAULT_RULE';
		    }
			return model;
		}
	},
	// SystemConfig getSystemConfig() @GET
	getConfig:function(handler){
		var self = this;
		peaxy.Communication.get('/oob/config/get',function(sysData, httpcode1){
			peaxy.Communication.get('/oob/vms/get',function(vmData,httpcode2){
				self.loadDomain(function(){
					if(jQuery.isFunction(handler)) {
						handler(toViewModel(sysData,vmData), httpcode2);
					}
				});
			},'json');
		},'json');
		function toViewModel(sysData,vmData){
			var view = new peaxy.OobDomain.SystemConfigViewModel();
			view.userName = sysData.user_name;
			view.password = sysData.password;
			view.userEmail = sysData.user_email; 
			view.gateWay = sysData.gate_way;
			view.subnetMask = sysData.subnet_mask;
			view.dnsDomain = sysData.dns_domain;
			view.dnsServer = sysData.dns_server1 + '\n' + sysData.dns_server2 + '\n' + sysData.dns_server3;
			view.ntpServer = sysData.ntp_server1 + '\n' + sysData.ntp_server2 + '\n' + sysData.ntp_server3;
			view.hyperfilerName = sysData.cluster_name;
			view.encryption = 'No';
			view.managementIp = sysData.management_ip;
			view.replicationValue = sysData.cardinality;
			view.averageFileSize = sysData.spare_count;			
			view.timeZone = sysData.time_zone;
			
			var scAllocated = 0;
			var nscount = 0;
			var dsCount = 0;
			var totalCapacity = 0;
			view.maxCapacity = [];
			for(var i = 0;i < 17;i++){
				view.maxCapacity[i] = 0;
			}
			for(var i = 0;i < vmData.length;i++){
				var index = parseInt(vmData[i].drive_type.split('_')[1]);
				if(vmData[i].type == 'DS'){
					dsCount++;
					totalCapacity += parseInt(vmData[i].capacity/1024);
					view.maxCapacity[index] += parseInt(vmData[i].capacity/1024);
				} else if(vmData[i].type == 'NS'){
					nscount++;
				}				
			}
			view.scList = [];
			for(var i=0;i<sysData.storage_classes.length;i++){
				view.scList[i] = {};
				var level = parseInt(sysData.storage_classes[i].device_type.split('_')[1]);
				if(sysData.storage_classes[i].device_type)
					view.scList[i].performanceLevel = level;	//performance : 1-16
				else
					view.scList[i].performanceLevel = 0;
				view.scList[i].className = sysData.storage_classes[i].name;			//className
				view.scList[i].replicas = sysData.storage_classes[i].replication_factor;			//replicas : 1-4
				view.scList[i].usableSpace = parseInt(sysData.storage_classes[i].capacity_gb/1024);		//number
				view.scList[i].totalSpace = parseInt(view.scList[i].replicas * view.scList[i].usableSpace);		//number
				view.scList[i].usedSpace = 0;			//number
				view.scList[i].freeSpace = view.scList[i].usableSpace - view.scList[i].usedSpace;			//number
				view.scList[i].flexible = sysData.storage_classes[i].default_sc ? 'YES':'NO';			//flexible : YES/NO
				view.scList[i].defaultValue = sysData.storage_classes[i].default_sc ? 'DEFAULT':'';		//statesName: DEFAULT/''
				scAllocated += view.scList[i].totalSpace;
			}
			view.dataRatioValue = nscount + ':' + dsCount;
			view.totalSpace = totalCapacity;
			view.allocated = scAllocated;
			view.unallocated = view.totalSpace - view.allocated;
			view.ratio = view.dataRatioValue;
			view.redundancy = view.replicationValue;

			view.ingestList = [];
			for(var i=0;i<sysData.ingest_policy.length;i++){
				view.ingestList[i] = {};
				view.ingestList[i].condition = [];
				view.ingestList[i].condition[0] = 'is created';
				view.ingestList[i].others = [];
				view.ingestList[i].ands = [];
				view.ingestList[i].schedule = {};
				view.ingestList[i].id = sysData.ingest_policy[i].id;	//id
				view.ingestList[i].name = sysData.ingest_policy[i].name;			//name
				view.ingestList[i].description = sysData.ingest_policy[i].description;
				var j = 0;
				if(sysData.ingest_policy[i].group_id){
					view.ingestList[i].ands[j] = {};
					view.ingestList[i].ands[j].vals = [];
					view.ingestList[i].ands[j].key = 'is created by user group';
					view.ingestList[i].ands[j].vals[0] = sysData.ingest_policy[i].group_id;
					j++;
				}
				if(sysData.ingest_policy[i].file_type){
					view.ingestList[i].ands[j] = {};
					view.ingestList[i].ands[j].vals = [];
					view.ingestList[i].ands[j].key = 'has file extension';
					view.ingestList[i].ands[j].vals[0] = sysData.ingest_policy[i].file_type;
					j++;
				}
				if(sysData.ingest_policy[i].parent_path){
					view.ingestList[i].ands[j] = {};
					view.ingestList[i].ands[j].vals = [];
					view.ingestList[i].ands[j].key = 'created in directory';
					view.ingestList[i].ands[j].vals[0] = sysData.ingest_policy[i].parent_path;
					view.ingestList[i].ands[j].vals[1] = sysData.ingest_policy[i].recursive;
					j++;
				}
				j = 0;
				if(sysData.ingest_policy[i].storage_class_name){
					view.ingestList[i].others[j] = {};
					view.ingestList[i].others[j].key = 'Store it on';
					view.ingestList[i].others[j].val = sysData.ingest_policy[i].storage_class_name;
					//view.ingestList[i].others[j].vals[1] = sysData.ingest_policy[i].sc_id;
					j++;
				}
				view.ingestList[i].flag = sysData.ingest_policy[i].flag;		//flag
			}
            return view;
        }
	},
	// OobState getServiceState() @GET
	getServiceState:function(handler){
		peaxy.Communication.get('/oob/state/get',function(data, httpcode){
			if(jQuery.isFunction(handler)) 
				handler(toViewModel(data), httpcode);
		},'json');
		function toViewModel(data){
            return data;
        }
	},
	// Set<OobVM> getVMs() @GET
	getVms:function(handler){
		peaxy.Communication.get('/oob/vms/get',function(data, httpcode){
			if(jQuery.isFunction(handler)) 
				handler(toViewModel(data), httpcode);
		},'json');
		function toViewModel(data){
            return data;
        }
	},
	// void restart() @POST
	restart:function(handler){
		peaxy.Communication.post('/oob/service/restart',function(data, httpcode){
			if(jQuery.isFunction(handler)) 
				handler(data, httpcode);
		},'json');
	}
}