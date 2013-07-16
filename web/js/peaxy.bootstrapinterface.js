var peaxy = peaxy || {};

peaxy.BootstrapInterface = {
	loadDomain:function(handler){
		if(peaxy.BootstrapDomain){
			handler();
		} else {
			peaxy.Communication.getScript('js/peaxy.bootstrapdomain.js',handler);
		}
	},
	// List<DiscoverInfo> discover()
	discover:function(handler){
        var self = this;
        peaxy.Communication.post('/bootstrap/discover',function(data,httpCode){
            self.loadDomain(function(){
                if(jQuery.isFunction(handler)){
                    handler(toViewModel(data),httpCode);
                }
            });
        },'json');
        function toViewModel(data){
        	var view = [];
        	for(var i = 0;i<data.length;i++){
        		view[i] = new peaxy.BootstrapDomain.DiscoverViewModel();
        		view[i].hasBonded = true;
            	view[i].hostname = data[i].hostname;
        		view[i].id = data[i].id;
        		view[i].manufacturer = data[i].manufacturer;
        		view[i].product = data[i].product;
        		view[i].serialNumber = data[i].serial_number;
        		view[i].memory = data[i].memory;
        		view[i].diskList = [];
    			for(var j = 0;j<data[i].disk_list.length;j++){
    				view[i].diskList[j] = {};
    				view[i].diskList[j].name = data[i].disk_list[j].name;
    				view[i].diskList[j].size = data[i].disk_list[j].size;
        		}
        		view[i].interfaceList = [];
        		for(var j = 0;j<data[i].interface_list.length;j++){
        			view[i].interfaceList[j] = {};
    				view[i].interfaceList[j].name = data[i].interface_list[j].interface_name;
    				view[i].interfaceList[j].speed = data[i].interface_list[j].speed;
    				view[i].interfaceList[j].isActive = data[i].interface_list[j].is_active;
    				view[i].interfaceList[j].isBonded = data[i].interface_list[j].is_bonded;
    				if(view.hasBonded && data[i].interface_list[j].is_bonded == false){
    					view[i].hasBonded = false;
    				}
        		}
        		view[i].ipCount = data[i].ip_count;
        	}
            return view;
        }
	},
	// void setBeacon(@PathParam("id") long id, @PathParam("on") boolean on)
	setBeacon:function(id, on, handler){
		peaxy.Communication.post('/bootstrap/beacon/set/'+id+'/'+on,function(data,httpCode){
			if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
		},'json');
	},
	// Map<Long, Boolean> getBeacons(List<Long> idList)   @POST
	getBeacons:function(idList, handler){
		peaxy.Communication.post('/bootstrap/beacons/get',$.toJSON(idList),function(data,httpCode){
			if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
		},'json');
		function toViewModel(data){
			var viewModel = [];
			var index = 0;
            for(var id in data){
            	viewModel[index] = new peaxy.BootstrapDomain.BeaconResponseViewModel();
            	viewModel[index].id = id;
            	viewModel[index].beacon = data[id];
                index++;
            }
        	return viewModel;
        }
	},
	// void setBondList(@PathParam("id") long id, List<String> interfaceList)
	setBondList:function(id, interfaceList, handler){
        peaxy.Communication.post('/bootstrap/bond_list/set/'+id,$.toJSON(toModel(interfaceList)), function(data,httpCode){
        	if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        function toModel(view){
        	return view;
        }
	},
	// FileSizeList getFileSizeList()
	getFileSizeList:function( handler){
		var self = this;
		peaxy.Communication.post('/bootstrap/file_size_list/get',function(data,httpCode){
			self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
		},'json');
		function toViewModel(data){
			var view = new peaxy.BootstrapDomain.FileListResponseViewModel();
			for(var i=0;i<data.fileSizes.length;i++){
				view.fileSizes[i] = data.fileSizes[i];
            }
			for(var i=0;i<data.nsdsRatio.length;i++){
				view.nsdsRatio[i] = data.nsdsRatio[i];
            }
            return view;
        }
	},
	// NetworkSettingsStatus validateNetworkSettings(NetworkSettings settings)
    validateNetworkSettings:function(settings,handler){
    	var self = this;
        peaxy.Communication.post('/bootstrap/network_settings/validate',$.toJSON(toModel(settings)), function(data,httpCode){
        	self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
        },'json');
        function toModel(view){
        	var model = {};
        	model.hyperfiler_name = view.hyperfilerName;
        	model.management_ip = view.managementIp;
        	model.gateway = view.gateway;
        	model.netmask = view.netmask;
        	model.dns_domain = view.dnsDomain;
        	if(view.dnsServer.length > 0)
        		model.dns_server1 = view.dnsServer[0];
        	if(view.dnsServer.length > 1)
        		model.dns_server2 = view.dnsServer[1];
        	if(view.dnsServer.length > 2)
        		model.dns_server3 = view.dnsServer[2];
        	if(view.ntpServer.length > 0)
        		model.ntp_server1 = view.ntpServer[0];
        	if(view.ntpServer.length > 1)
        		model.ntp_server2 = view.ntpServer[1];
        	if(view.ntpServer.length > 2)
        		model.ntp_server3 = view.ntpServer[2];
        	return model;
        }
        function toViewModel(data){
            var success = new peaxy.BootstrapDomain.ValidateResponseViewModel();
            success.hasError = false;
            if(data.management_ip_status!='NOT_PINGABLE'){
                success.managementIp = true;
                success.hasError |= true;
            }
            if(data.gateway_status!='PINGABLE'){
                success.gateway = true;
                success.hasError |= true;
            }
            if(!data.netmask_valid){                
                success.netmask = true;
                success.hasError |= true;
            }
            if(!data.dns_domain_valid){
                success.dnsDomain = true;
                success.hasError |= true;
            }
            if(data.dns_server1_status!='PINGABLE' || 
                (data.dns_server2_status!='PINGABLE' && data.dns_server2_status!='NULL' ) || 
                (data.dns_server3_status!='PINGABLE' && data.dns_server3_status!='NULL' ) ){
                success.dnsServer = true;
                success.hasError |= true;
            }
            if(data.ntp_server1_status!='PINGABLE' || 
                (data.ntp_server2_status!='PINGABLE' && data.ntp_server2_status!='NULL' ) || 
                (data.ntp_server3_status!='PINGABLE' && data.ntp_server3_status!='NULL' ) ){
                success.ntpServer = true;
                success.hasError |= true;
            }
            if(!data.hyperfiler_name_valid){
                success.hyperfilerName = true;
                success.hasError |= true;
            }
            return success;
        }
    },
    // IpAssignments setIpInfo(IpInfo ipInfo)
	setIpInfo:function(ipInfo, handler){
		var self = this;
		peaxy.Communication.post('/bootstrap/ipinfo/set',$.toJSON(toModel(ipInfo)), function(data,httpCode){
			self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
        },'json');
		function toModel(view){
			var model = {};
			model.id_list = ipInfo.idList;
			model.ip_range_list = ipInfo.ipRangeList;
        	return model;
        }
        function toViewModel(data){
            var viewModel = new peaxy.BootstrapDomain.IpinfoResponseViewModel();
            var index = 0;
            viewModel.ipList = [];            
            for(var id in data.ip_list){
            	viewModel.ipList[index] = {};
            	viewModel.ipList[index].id = id;
            	viewModel.ipList[index].ip = data.ip_list[id];
                index++;
            }
            viewModel.additionalNeeded = data.additional_needed;
            viewModel.inUse = [];
            for(var i=0;i<data.in_use.length;i++){
            	viewModel.inUse[i] = data.in_use[i];
            }
            viewModel.available = [];
            for(var i=0;i<data.available.length;i++){
            	viewModel.available[i] = data.available[i];
            }
            return viewModel;
        }
	},
	// Map<Long, String> startOsInstall() : @POST
	startOsInstall:function(handler){
		var self = this;
		peaxy.Communication.post('/bootstrap/os_install/start',function(data,httpCode){
			self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
        },'json');
		function toViewModel(data){
            var viewModel = [];
            var index = 0;
            for(var id in data){
            	viewModel[index] = new peaxy.BootstrapDomain.OsStartResponseViewModel();
            	viewModel[index].id = id;
            	viewModel[index].message = data[id];
                index++;
            }
            return viewModel;
        }
	},
	// Map<Long, OsInstallProgress> getOsInstallProgress()
	getOsInstallProgress:function(handler){
		var self = this;
		peaxy.Communication.post('/bootstrap/os_install/progress',function(data,httpCode){
			self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
        },'json');
        function toViewModel(data){
            var viewModel = [];
            var index = 0;
            for(var id in data){
            	viewModel[index] = new peaxy.BootstrapDomain.OsProgressResponseViewModel();
            	viewModel[index].id = id;
            	viewModel[index].progress.percent = data[id].percent_complete;
            	viewModel[index].progress.status = data[id].status;
                index++;
            }
            return viewModel;
        }
	},
	// String getOsInstallLog(@PathParam("id") long id)
	getOsInstallLog:function(id, handler){
		var self = this;
        peaxy.Communication.post('/bootstrap/os_install/log/'+id,function(logfile,httpCode){
            peaxy.Communication.get('/'+logfile,function(data,httpCode){
            	self.loadDomain(function(){
    				if(jQuery.isFunction(handler)){
    		            handler(toViewModel(data),httpCode);
    		        }
    			});
            });
        },'json');
        function toViewModel(data){
            return data.split('\n');
        }
    },
    // void cancelOsInstalls()
    cancelOsInstalls:function(handler){
        peaxy.Communication.post('/bootstrap/os_install/cancel_all',function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
    },
    // Map<Long, VmInstallProgress> getVmInstallProgress(List<Long> idList)
	getVmInstallProgress:function(idList, handler){
		var self = this;
		peaxy.Communication.post('/bootstrap/vm_install/progress',$.toJSON(toModel(idList)), function(data,httpCode){
			self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
        },'json');
		function toModel(view){
        	return view;
        }
        function toViewModel(data){
        	var viewModel = [];
            var index = 0;
            for(var id in data){
            	viewModel[index] = new peaxy.BootstrapDomain.VmProgressResponseViewModel();
            	viewModel[index].id = id;
            	viewModel[index].progress.percentComplete = data[id].percent_complete;
            	viewModel[index].progress.installsPending = data[id].installs_pending;
            	viewModel[index].progress.installsStarted = data[id].installs_started;
            	viewModel[index].progress.installsCompleted = data[id].installs_completed;
            	viewModel[index].progress.installsFailed = data[id].installs_failed;
            	viewModel[index].progress.failedList = data[id].failed_list;
                index++;
            }
            return viewModel;
        }
	},
	// List<String> getVmInstallLog(@PathParam("id") long id)
    getVmInstallLog:function(id, handler){
    	var self = this;
        peaxy.Communication.post('/bootstrap/vm_install/log/'+id, function(data,httpCode){
        	self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
        },'json');
        function toViewModel(data){
            return data.split("\n");
        }
    },
    // void cancelVmInstalls()
    cancelVmInstalls:function(handler){
        peaxy.Communication.post('/bootstrap/vm_install/cancel_all',function(data,httpCode){
			if(jQuery.isFunction(handler)){
	            handler(data,httpCode);
			}
        },'json');
    },
    // void initHf(List<Long> idList, @PathParam("file_size") int fileSize, @PathParam("ns_repfactor") long nsRepFactor)
	initHf:function(idList, fileSize, nsRepFactor, handler){
		peaxy.Communication.post('/bootstrap/init_hf/start/'+fileSize+'/'+nsRepFactor,$.toJSON(toModel(idList)),function(data,httpCode){
			if(jQuery.isFunction(handler)){
	            handler(data,httpCode);
			}
        },'json');		
		function toModel(view){
        	return view;
        }
	},
	// Map<Long, Integer> getInitHfProgress()
	getInitHfProgress:function(handler){
		var self = this;
		peaxy.Communication.post('/bootstrap/init_hf/progress', function(data,httpCode){
			self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
        },'json');
        function toViewModel(data){
        	var viewModel = [];
            var index = 0;
            for(var id in data){
            	viewModel[index] = new peaxy.BootstrapDomain.HfProgressResponseViewModel();
            	viewModel[index].id = id;
            	viewModel[index].progress.percentComplete = data[id].percent_complete;
            	viewModel[index].progress.status = data[id].status;
                index++;
            }
            return viewModel;
        }
	},
	// List<String> getInitHfLog(@PathParam("id") long id)
	getInitHfLog:function(id, handler){
		var self = this;
		peaxy.Communication.post('/bootstrap/init_hf/log/'+id, function(data,httpCode){
			self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
        },'json');
		function toViewModel(data){
            return data.split("\n");
        }
	},
	// Map<Long, HvInfo> getInitHfInfo()
	getInitHfInfo:function(handler){
		var self = this;
		peaxy.Communication.post('/bootstrap/init_hf/info', function(data,httpCode){
			self.loadDomain(function(){
				if(jQuery.isFunction(handler)){
		            handler(toViewModel(data),httpCode);
		        }
			});
        },'json');
        function toViewModel(data){
            return data;
        }
	},
	// void terminateBootstrap()
    terminateBootstrap:function(handler){
       peaxy.Communication.post('/bootstrap/app/exit', function(data,httpCode){
           if(jQuery.isFunction(handler)){
               handler(data,httpCode);
           }
       },'json');
    }
}