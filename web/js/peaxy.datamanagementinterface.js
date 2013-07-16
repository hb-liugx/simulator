var peaxy = peaxy || {};
peaxy.Communication.getScript('js/peaxy.datamanagementdomain.js',function(){});

peaxy.DataManagementInterface = {
	// test for get migration
	getMigration:function(handler){
        peaxy.Communication.post('/dm/policy/migration/test',function(data,httpCode){
        	if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
	},
	// void createMigrationPolicy(List<MigrationPolicyAndSchedule> policyList) @PUT
	createMigrationPolicy:function(policyList,handler){
        peaxy.Communication.put('/dm/policy/migration/create',$.toJSON(toModel(policyList)),function(data,httpCode){
        	if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        function toModel(view){
        	//var model = new peaxy.ManagementDomain.MigrationRequestViewModel();
            return view;
        }
	},
	// List<DataPolicy> getPolicies(@PathParam("type") String type) @GET
    getPolicies:function(type,handler){
        var url = '/dm/policy/get/';
        if(type != '') {
            url = url + type;
         }
        peaxy.Communication.get(url, function(data,httpCode){
        	if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        function toViewModel(data){
            return data;
        }
	},
	// void deletePolicy(@PathParam("id") int id) @DELETE
    deletePolicy:function(id,handler){
        peaxy.Communication.delete('/dm/policy/delete/'+id,function(data,httpCode){
        	if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
	},
	// void createRuleConfig(List<RuleCfg> data) @POST
    createRuleConfig:function(ruleList,handler){
		peaxy.Communication.post('/dm/policy/ingest/create', $.toJSON(toModel(ruleList)), function(data,httpCode){
			if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
		
		function toModel(view){
			
			return view;
        }
	},
	// void setRuleConfig(List<RuleCfg> data)  @PUT
    setRuleConfig:function(ruleList,handler){ 
		peaxy.Communication.put('/dm/policy/ingest/set', $.toJSON(toModel(ruleList)), function(data,httpCode){
			if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
		
		function toModel(view){
            return view;
        }
	},
	// void deleteRule(RuleFilter filter) @DELETE
    deleteRule:function(ruleFilter,handler){
		peaxy.Communication.del('/dm/policy/ingest/delete', $.toJSON(toModel(ruleFilter)), function(data,httpCode){
			if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
		
		function toModel(view){
            return view;
        }
	},
	// List<RuleCfg> getRuleConfig(RuleFilter filter) @POST
    getRuleConfig:function(ruleFilter,handler){
		peaxy.Communication.post('/dm/policy/ingest/getfilter', $.toJSON(toModel(ruleFilter)),function(data,httpCode){
			if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
		
		function toModel(view){
            return view;
        }
		
        function toViewModel(data){
            return data;
        }
	},
	// List<RuleCfg> getAllRuleConfig() @GET
    getAllRuleConfig:function(handler){
        peaxy.Communication.get('/dm/policy/ingest/get',function(data,httpCode){
        	if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        function toViewModel(data){
            return data;
        }
	},
	// void createSCConfig(StorageClassCfg data) @POST
    createSCConfig:function(storageCfg,handler){
        peaxy.Communication.post('/dm/sc/create',$.toJSON(toModel(storageCfg)),function(data,httpCode){
        	if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
	// void setSCConfig(List<StorageClass> data) @PUT
	setSCConfig:function(storageClass,handler){
        peaxy.Communication.put('/dm/sc/set',$.toJSON(toModel(storageClass)),function(data,httpCode){
        	if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
	// void updateSCConfig(StorageClass data) @POST
    updateSCConfig:function(storageClass,handler){
        peaxy.Communication.post('/dm/sc/update',$.toJSON(toModel(storageClass)),function(data,httpCode){
        	if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
	// List<StorageClass> getSCConfig() @GET
    getSCConfig:function(handler){
		peaxy.Communication.get('/dm/sc/get', function(scConfig,httpCode){
			peaxy.Communication.get('/dm/sc/capacity/get',function(scCapacity,httpCode){
                if(jQuery.isFunction(handler)){
                    handler(toViewModel(scConfig,scCapacity),httpCode);
                }
            },'json');
        },'json');
        function toViewModel(scConfig,scCapacity){
        	var view = new peaxy.ManagementDomain.CardResponseViewModel();
        	var total=0,used=0,free=0;
        	for(var i=0;i<scCapacity.length;i++){
        		total += scCapacity[i].totalMB;
        		used += scCapacity[i].usedMB;
        		free += scCapacity[i].freeMB;
            }
        	view.hyperfilerName = '';
        	view.totalSpace = total;
        	view.unallocated = free;
        	view.allocated = used;
        	view.encryption = '';
        	view.storageClasses = {};
        	view.storageClasses.namespaceCardData = {};
        	view.storageClasses.namespaceCardData.ratio = '';
        	view.storageClasses.namespaceCardData.redundancy = '';
        	
        	view.storageClasses.classes = [];
        	for(var i=0;i<scConfig.length;i++){            
	        	view.storageClasses.classes[i] = {};
	        	view.storageClasses.classes[i].className = scConfig[i].name;
	        	view.storageClasses.classes[i].STATEName = '';
	        	view.storageClasses.classes[i].totalSpace = '';
	        	view.storageClasses.classes[i].usableSpace = '';
	        	view.storageClasses.classes[i].freeSpace = '';
	        	view.storageClasses.classes[i].freeUsableSpace = '';
	        	view.storageClasses.classes[i].usedSpace = '';
	        	view.storageClasses.classes[i].usedUsableSpace = '';
	        	view.storageClasses.classes[i].replicas = scConfig[i].cardinality;
	        	view.storageClasses.classes[i].performance = i;
	        	view.storageClasses.classes[i].flexible = false;
        	}
            return view;
        }
	},
	// List<StorageClassData> getStorageClassCapacity() @GET
    getStorageClassCapacity:function(handler){
		peaxy.Communication.get('/dm/sc/capacity/get', function(data,httpCode){
			if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        function toViewModel(data){
            return data;
        }
	}
}
