var peaxy = peaxy || {};
peaxy.Communication.getScript('js/peaxy.bootstrapdomain.js',function(){});
peaxy.HfSettings = kendo.Class.extend({
	hfSettingsClassViewModel : null,
	generalSettingsViewModel : null,
	advancedSettingsViewModel : null,
/*	encryption : ["No", "Yes"],
	replication : ["1", "2", "3"],*/
	averageFileSize : null,
	init:function(options){
		this.options = {
			content:'',
			onConfig:function(data){

			},
			sysConfig:{}
		};
		self = this;
		this.setOptions(options);
		this.validator = new peaxy.Check();
	},
	setOptions:function(options){
		$.extend(this.options, options || {});
	},

	/**************hyperfiler initial Settings******************/
	initialSettings:function(options){
		this.setOptions(options);
		var self = this;
		/*init hfSettings layout*/
		var div = $('<div class="p-content"></div>');
		div.html(this.Template.initialHeader);
		div.append(this.Template.networkSetting);
		div.append($('<div  align="center" style="margin:13px;width:310px;float:left;border-left:1px solid gray;border-right:1px solid gray"><div>').html(this.Template.smallgreneralHeader + this.Template.greneralSetting + this.Template.managementIp));
		div.append(this.Template.nameSpace);
		this.options.content.html(div);

		this._getFileSizeList(function(fileSizeList, ratioList){
			self.hfSettingsClassViewModel = kendo.observable($.extend({
				gateWay : "",
				subnetMask : "",
				dnsDomain : "",
				dnsServer : "",
				hyperfilerName : "",
				encryption : 'No',
				managementIp : "",
				ntpServer : "",
				averageFileSize: 1,/*1,2,3,4*/
				fileSizeList : fileSizeList,
				dataRatioValue : '1:2',
				replication : 2,    
				replicationValue : 2,
				timeZone:'-8:00',
				TIMEZONES:peaxy.TIMEZONES		
			}, self.options.sysConfig));
			kendo.bind(self.options.content, self.hfSettingsClassViewModel);
			self.options.content.find('#averageFileSize').data('kendoDropDownList').setOptions({
	    		change: function(e){
	    			self.hfSettingsClassViewModel.set("dataRatioValue", ratioList[parseInt(self.hfSettingsClassViewModel.get('averageFileSize')-1)]);
	    		}
			});			
		});
		var gateWay = self.options.content.find("#gateWay"),
			subnetMask = self.options.content.find("#subnetMask"),
			dnsDomain = self.options.content.find("#dnsDomain"),
			dnsServers = self.options.content.find("#dnsServers"),
			managementIp = self.options.content.find("#managementIp"),
			hyperfilerName = self.options.content.find("#hyperfilerName"),
			ntpServers = self.options.content.find("#ntpServers");

		self.validator.register(gateWay, self.options.content.find('#gateWayErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a gate way IP address.";
			}else if(!peaxy.Validator.checkIPFormat(val)){
				return "Please input a correct gate way IP address.";
			}
			return true;
		});
		self.validator.register(subnetMask,self.options.content.find('#subnetMaskErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a subnet mask IP address.";
			}else if(!peaxy.Validator.checkIPFormat(val)){
				return "Please input a correct subnet mask IP address.";
			}
			return true;
		});
		self.validator.register(dnsDomain,self.options.content.find('#dnsDomainErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a dns domain IP address.";
			}else if(!peaxy.Validator.checkIPFormat(val)){
				return "Please input a correct dns domain IP address.";
			}
			return true;
		});
		self.validator.register(managementIp,self.options.content.find('#managementIpErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a management IP address.";
			}else if(!peaxy.Validator.checkIPFormat(val)){
				return "Please input a correct management IP address.";
			}
			return true;
		});
		self.validator.register(dnsServers,self.options.content.find('#dnsServersErrMsg'),function(val){
			var temp = val.split("\n");
			var length = temp.length;
			if(length != 3){
				return "Please input three correct DNS servers.";
			}
			for(i = 0; i < length; i ++){
				if(!peaxy.Validator.checkIPFormat(temp[i])){
					return "Please input a correct DNS server.";
				}
			}	
			return true;
		});

		self.validator.register(ntpServers,self.options.content.find('#ntpServersErrMsg'),function(val){
			var temp = val.split("\n");
			var length = temp.length;
			if(length < 1){
				return "Please input at least one NTP server.";
			}
			for(i = 0; i < length; i ++){
				if(!peaxy.Validator.checkIPFormat(temp[i])){
					return "Please input a correct NTP server.";
				}
			}	
			return true;
		});

		self.validator.register(hyperfilerName,self.options.content.find('#hyperfilerNameErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a hyperfiler name.";
			}else if(!peaxy.Validator.checkLengthRange(val, 1, 14)){
				return "The hyperfiler name must be 1 to 14 characters in length.";
			}
			return true;
		});	
	},
	processInitSetResultByServer:function(data){
		var gateWay = this.options.content.find("#gateWay"),
			subnetMask = this.options.content.find("#subnetMask"),
			dnsDomain = this.options.content.find("#dnsDomain"),
			dnsServers = this.options.content.find("#dnsServers"),
			managementIp = this.options.content.find("#managementIp"),
			hyperfilerName = this.options.content.find("#hyperfilerName"),
			ntpServers = this.options.content.find("#ntpServers"),
			validator = new peaxy.Check();

		validator.register(gateWay, this.options.content.find('#gateWayErrMsg'),function(val){
			if(data.gateway){
				return "Please input a correct gate way IP address.";
			}
			return true;
		});
		validator.register(subnetMask,this.options.content.find('#subnetMaskErrMsg'),function(val){
			if(data.netmask){
				return "Please input a correct subnet mask IP address.";
			}
			return true;
		});
		validator.register(dnsDomain,this.options.content.find('#dnsDomainErrMsg'),function(val){
			if(data.dnsDomain){
				return "Please input a correct dns domain IP address.";
			}
			return true;
		});
		validator.register(managementIp,this.options.content.find('#managementIpErrMsg'),function(val){
			if(data.managementIp){
				return "Please input a correct management IP address.";
			}
			return true;
		});
		validator.register(dnsServers,this.options.content.find('#dnsServersErrMsg'),function(val){
			if(data.dnsServer){
				return "Please input a correct dns server IP address.";
			}
			return true;
		});
		validator.register(ntpServers,this.options.content.find('#ntpServersErrMsg'),function(val){
			if(data.ntpServer){
				return "Please input a correct ntp server IP address.";
			}
			return true;
		});
		validator.register(hyperfilerName,this.options.content.find('#hyperfilerNameErrMsg'),function(val){
			if(data.hyperfilerName){
				return "Please input a correct hyperfiler name.";
			}
			return true;
		});
		validator.validator();
	},
	validateNetworkSettings:function(setting, handler){
		var nss = $.extend({
			hyperfilerName:'',
			managementIp:'',
			gateway:'',
			netmask:'',
			dnsDomain:'',
			dnsServer:[],
			ntpServer:[]
		}, setting || {});

		peaxy.Communication.getScript('js/peaxy.bootstrapinterface.js',function(){
			peaxy.BootstrapInterface.validateNetworkSettings(nss,function(result){
				if($.isFunction(handler))
					handler(result);
			});
		});	
	},
	validateInitSettingsByServer : function(){
		var dnsServer = this.hfSettingsClassViewModel.dnsServer.split("\n");
		var ntpServer = this.hfSettingsClassViewModel.ntpServer.split("\n");
		var data = {
			gateway : this.hfSettingsClassViewModel.gateWay,
			netmask : this.hfSettingsClassViewModel.subnetMask,
			dnsDomain : this.hfSettingsClassViewModel.dnsDomain,
			dnsServer : dnsServer,
			ntpServer : ntpServer,
			managementIp : this.hfSettingsClassViewModel.managementIp,
			hyperfilerName : this.hfSettingsClassViewModel.hyperfilerName
		};
		return data;
	},

	getInitSettings : function(){
		var hfSettings = {
			gateWay : this.hfSettingsClassViewModel.gateWay,
			subnetMask : this.hfSettingsClassViewModel.subnetMask,
			dnsDomain : this.hfSettingsClassViewModel.dnsDomain,
			dnsServer : this.hfSettingsClassViewModel.dnsServer,
			ntpServer : this.hfSettingsClassViewModel.ntpServer,
			hyperfilerName : this.hfSettingsClassViewModel.hyperfilerName,
			encryption : this.hfSettingsClassViewModel.encryptionValue,    		
			managementIp : this.hfSettingsClassViewModel.managementIp,
			averageFileSize : this.hfSettingsClassViewModel.averageFileSize,
			replicationValue :  this.hfSettingsClassViewModel.replicationValue,
			dataRatioValue : this.hfSettingsClassViewModel.dataRatioValue,
			timeZone:this.hfSettingsClassViewModel.timeZone.value
		};
		return hfSettings;
	},
	_getFileSizeList:function(handler){
		peaxy.Communication.getScript('js/peaxy.bootstrapinterface.js',function(){
			peaxy.BootstrapInterface.getFileSizeList(function(data,httpCode){
				var fileSizeList = [],
					ratioList = [];
				for(var i=0, l=data.fileSizes.length;i<l;i++){
					fileSizeList.push({'fileSize':data['fileSizes'][i],'ratio':(i+1)});
					ratioList.push(data['nsdsRatio'][i]);
				}
				if($.isFunction(handler))
					handler(fileSizeList, ratioList);
			});
		});
	},
	getInitData : function (handler){
		var fileSizeList;
		peaxy.Communication.getScript('js/peaxy.bootstrapinterface.js',function(){
			peaxy.BootstrapInterface.getFileSizeList(function(fileSizeList,httpCode){
				self.averageFileSize = self.setInitData(fileSizeList);
				handler();
			});
		});
	},

	setInitData : function(fileSizeList){
		var temp = [];
		if(fileSizeList != null){
			nsdsRatio = fileSizeList.nsdsRatio;
			fileSizes = fileSizeList.fileSizes;
			length = fileSizes.length;
			
			for(var i = 0; i < length; i ++){
				temp[i] = {ratio:nsdsRatio[i],fileSize:fileSizes[i]};
			}
		}
		return temp;
	},

	/*******************general Settings***************************/
	generalSettings:function(gsetting, options){
		this.setOptions(options);

		/*init generalSettings layout*/
		var self = this,
			cont = this.options.content,
			div = $('<div class="p-content"></div>').css({'padding-top':0,'padding-bottom':0});

		cont.html(this.Template.greneralHeader);
		div.append(this.Template.greneralSetting + this.Template.generalNameSpace);
		cont.append(div);

		peaxy.Communication.getScript('js/peaxy.bootstrapinterface.js',function(){
			/*{"fileSizes":["Small files (< 1MB)","Medium files (< 20MB)","Large files (> 20MB)","Mixed size files"],"nsdsRatio":["1:2","1:4","1:6","1:4"]}*/
			peaxy.BootstrapInterface.getFileSizeList(function(data,httpCode){
				var averageFileSize = [],
					nsdsRatio = [];
				for(var i=0,l=data.fileSizes.length;i<l;i++){
					averageFileSize.push({'fileSize':data['fileSizes'][i],'ratio':i+1});
					nsdsRatio.push(data['nsdsRatio'][i]);
				}
				var gsViewModel = kendo.observable($.extend({
					hyperfilerName : "",
					encryption:'No',
					averageFileSizeData:averageFileSize,
					averageFileSize:"",
					dataRatioValue:'',
					replicationValue:'1'
				}, gsetting || {}));
				kendo.bind(cont, gsViewModel);

				cont.find('select').data('kendoDropDownList').setOptions({
	           		change:function(){
		           		gsViewModel.set("dataRatioValue", nsdsRatio[parseInt(gsViewModel.get('averageFileSize'))-1]);
		           	}
				});
			});
			cont.find('input,select').prop({'disabled':true});
		});
	},
	/*********************advanced settings*******************************/
	advancedSettings : function(viewModel, options){
		this.setOptions(options);
		var self = this,
			dataStorage,
			cont = this.options.content,
			/*init advancedSettings layout*/
			div = $('<div class="p-content"></div>'),
			data = {gateWay:'', subnetMask:'', dnsDomain:'', dnsServer:'', managementIp:'', timeZone:'-08:00', ntpServer:'', TIMEZONES:peaxy.TIMEZONES},
			dataStorage = viewModel || {gateWay:'', subnetMask:'', dnsDomain:'', dnsServer:'', managementIp:'', timeZone:'-08:00', ntpServer:''},
			adVm;

		div.css({'padding-top':'0','padding-bottom':'0'});
		cont.html(this.Template.advancedHeader);
		div.append(this.Template.advancedSttings);
		cont.append(div);
		cont.append(this.Template.buttonGroup);

		/*resize height*/
		cont.find('.p-content').css('overflow','auto').height(cont.height()
			- cont.find('.p-title-header').height()
			- cont.find('.p-footer').height() - 33);

		var gateWay = cont.find("#gateway"),
			subnetMask = cont.find("#subnetmask"),
			dnsDomain = cont.find("#dnsDomain"),
			dnsServers = cont.find("#dns"),
			managementIp = cont.find("#managementIp"),
			ntpServers = cont.find("#ntp"),
			timeZone = cont.find('#timeZone');
		$.extend(data, viewModel || {});
		adVm = kendo.observable(data);
		kendo.bind(cont, adVm);

		/*add button click event*/
		cont.find("#cancel").click(function(){
        	kendo.bind(cont, dataStorage);
        	cont.find('.p-errmsg').text('');
        });
        cont.find("#saveChanges").click(function(){
        	if(self.validator.validator()){
				var setting = adVm.toJSON();
				setting.TIMEZONES = undefined;
				var nss = {
					managementIp:setting.managementIp,
					gateway:setting.gateWay,
					netmask:setting.subnetMask,
					dnsDomain:setting.dnsDomain,
					dnsServer:setting.dnsServer,
					ntpServer:setting.ntpServer,
					timeZone:setting.timeZone.value
				};
        		/*check  network settings*/
        		self.validateNetworkSettings(nss,function(result){
        			if(result.hasError){
						self.processInitSetResultByServer(result);
        			}else{
	        			self.options.onConfig(nss);
        			}
        		});				
        	}
        });

		this.validator.register(gateWay, this.options.content.find('#gateWayErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a gate way IP address.";
			}else if(!peaxy.Validator.checkIPFormat(val)){
				return "Please input a correct gate way IP address.";
			}
			return true;
		});
		this.validator.register(subnetMask,this.options.content.find('#subnetMaskErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a subnet mask IP address.";
			}else if(!peaxy.Validator.checkIPFormat(val)){
				return "Please input a correct subnet mask IP address.";
			}
			return true;
		});
		this.validator.register(dnsDomain,this.options.content.find('#dnsDomainErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a dns domain IP address.";
			}else if(!peaxy.Validator.checkIPFormat(val)){
				return "Please input a correct dns domain IP address.";
			}
			return true;
		});
		this.validator.register(managementIp,this.options.content.find('#managementIpErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a management IP address.";
			}else if(!peaxy.Validator.checkIPFormat(val)){
				return "Please input a correct management IP address.";
			}
			return true;
		});
		this.validator.register(dnsServers,this.options.content.find('#dnsServersErrMsg'),function(val){
			var temp = val.split("\n");
			var length = temp.length;
			if(length != 3){
				return "Please input three correct DNS servers.";
			}
			for(i = 0; i < length; i ++){
				if(!peaxy.Validator.checkIPFormat(temp[i])){
					return "Please input a correct DNS server.";
				}
			}	
			return true;
		});
		this.validator.register(ntpServers,this.options.content.find('#ntpServersErrMsg'),function(val){
			var temp = val.split("\n");
			var length = temp.length;
			if(length < 1){
				return "Please input at least one NTP server.";
			}
			for(i = 0; i < length; i ++){
				if(!peaxy.Validator.checkIPFormat(temp[i])){
					return "Please input a correct NTP server.";
				}
			}	
			return true;
		});
	},

	saveSetting: function(){
		if (this.validatorInput()) {
			/* call peaxy api */
			var data = {
				gateway : advancedSettingsViewModel.gateway,
				netmask : advancedSettingsViewModel.subnetmask,
				dnsServer : advancedSettingsViewModel.dns,
				ntpServer : advancedSettingsViewModel.ntp
			};
			return data;
		}
		return false;
	},

	setSettings:function(viewModel){
		var data = {dns : '',ntp : '',gateway : '',subnetmask : ''};
		this.dataStorage = viewModel || {dns : '',ntp : '',gateway : '',subnetmask : ''};
		this.advancedSettingsViewModel = kendo.observable($.extend(data, viewModel || {}));
		kendo.bind(this.options.content, this.advancedSettingsViewModel);
	},
	/*******util function************/
	checkInput:function(){
		if(this.validator)
			return	this.validator.validator();
		return false;
	},	
	Template:{
		initialHeader:'<div><p class="p-title1">HYPERFILER SETTINGS</p><hr class="p-hr-l"/>\
			<p class="p-desc">To begin configuring your Hyperfiler you will need to enter the information in the form to the left.</p></div>',
		greneralHeader:'<p class="p-title-header">GENERAL SETTINGS</p>',
		smallgreneralHeader:'<div style="width:280px;" align="left"><p class="p-title2">GENERAL SETTINGS</p><hr class="p-hr-l"/><div>',
		advancedHeader:'<div class="p-title-header">ADVANCED SETTINGS<hr class="p-hr-blank"/>\
			<p class="p-desc" style="font-weight:normal;">\
			<span class="p-error-color">Warning:</span> Editing these IP Addresses could take your Hyperfiler offline for a short time.</p>\
			</div>',
		greneralSetting:'<hr class="p-hr-blank"/><p class="p-title3" style="width:80%;">Name<span class ="p-label" style="float:right">Max 14 characters</span></p>\
			<input id="hyperfilerName" data-bind="value: hyperfilerName" type="text" class="p-input" style="width:80%" />\
			<p id="hyperfilerNameErrMsg" class="p-errmsg"></p><hr class="p-hr-blank"/><hr class="p-hr-s"/>\
			<!--<p style="width:100%;"><span class="p-title3">ENCRYPTION<span><span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">Do you need encryption (on the wire)?</p>\
			<p style="width:100%;">\
			<input name="encryption" value="No" type="radio" data-bind="checked: encryption"><label>No</label>\
			<input name="encryption" value="Yes" type="radio" data-bind="checked: encryption"><label>Yes</label>\
			</p><hr class="p-hr-blank"/>-->',
		networkSetting:'<div id="network_settings_panel" style="width:300px;float:left;">\
			<div style="height:13px;"></div>\
			<p class="p-title2">NETWORK SETTINGS</p>\
			<hr class="p-hr-l"/>\
			<p class="p-title3">GATEWAY</p>\
			<input type="text" id="gateWay" data-bind="value: gateWay" class="p-input" style="width:80%"/>\
			<p id="gateWayErrMsg" class="p-errmsg"></p>\
			<p style="height:10px;" />\
			<p class="p-title3">SUBNET MASK</p>\
			<input type="text" id="subnetMask" data-bind="value: subnetMask" class="p-input" style="width:80%"/>\
			<p id="subnetMaskErrMsg" class="p-errmsg"></p>\
			<p style="height:10px;" />\
			<hr class="p-hr-s"/>\
			<p class="p-title3">DNS<span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">The primary domain and upto three name servers are needed to configure the DNS.</p>\
			<input type="text" id="dnsDomain" data-bind="value: dnsDomain" class="p-input" style="width:80%"/>\
			<p id="dnsDomainErrMsg" class="p-errmsg"></p>\
			<p style="height:10px;" />\
			<textarea id="dnsServers" id="dnsServers" rows=3 style="rows:3; width:80%; resize: none;" data-bind="value: dnsServer"></textarea>\
			<p id="dnsServersErrMsg" class="p-errmsg"></p>\
			</div>',
		managementIp:'<div id="managerment_ip_panel" style="width:280px;float:left;">\
			<p class="p-title3">MANAGEMENT IP<span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">The IP address that is used to administer\
			the hyperfiler after it is configured.</p>\
			<input type="text" id="managementIp" data-bind="value: managementIp" class="p-input" style="width:80%"/><br>\
			<p id="managementIpErrMsg" class="p-errmsg"></p><hr class="p-hr-blank" />\
			<p class="p-title3">NTP</p>\
			<p class="p-desc">At least one NTP server is needed to configure\
			time settings for the hyperfiler.</p>\
			<select data-role="dropdownlist" data-text-field="name" data-value-field="value" data-bind="source: TIMEZONES, value: timeZone" style="width:82%"></select>\
			<textarea id="ntpServers" rows=3 style="rows:3; width:80%; resize: none;" data-bind="value: ntpServer"></textarea>\
			<p id="ntpServersErrMsg" class="p-errmsg"></p>\
			</div>',
		nameSpace:'<div id="namescape_panel" style="width:325px;float:left;">\
			<div style="height:13px;"></div>\
			<p class="p-title2">NAMESPACE</p>\
			<hr class="p-hr-l"/>\
			<p class="p-title3">AVERAGE FILE SIZE<span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">What is the expected average file size of the data you want to manage with this Hyperfiler?</p>\
			<select id="averageFileSize" data-role="dropdownlist" data-text-field="fileSize" data-value-field="ratio" data-bind="source: fileSizeList, value: averageFileSize" style="width:80%;"></select>\
			<hr class="p-hr-blank" />\
			<p class="p-title3">NAME:DATA RATIO <span data-bind="text:dataRatioValue" /><span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">Defining your average file size will, by default, define the ratio between name PVMs and data PVMs.</p>\
			<hr class="p-hr-s"/>\
			<p class="p-title3">REPLICATION FACTOR<span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">What replication factor do you need for your Namespace?</p>\
			<input name="replication" id="replication1" value=2 type="radio" data-bind="checked: replicationValue"><label for="replication1">2</label>\
			<input name="replication" id="replication2" value=3 type="radio" data-bind="checked: replicationValue"><label for="replication2">3</label>\
			<input name="replication" id="replication3" value=4 type="radio" data-bind="checked: replicationValue"><label for="replication3">4</label>\
			</div>',
		generalNameSpace:'<div id="namescape_panel">\
			<p class="p-title3">NAMESPACE</p>\
			<hr class="p-hr-l"/>\
			<p class="p-title3">AVERAGE FILE SIZE<span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">What is the expected average file size of the data you want to manage with this Hyperfiler?</p>\
			<select data-role="dropdownlist" data-text-field="fileSize" data-value-field="ratio" data-bind="source: averageFileSizeData, value: averageFileSize" style="width:80%;"></select>\
			<hr class="p-hr-blank"/>\
			<p class="p-title3"><span>NAME:DATA RATIO </span><span data-bind="text: dataRatioValue"></span><span class="help" data-help-url=""></span></p>\
			<p class="p-desc">Defining your average file size will, by default, define the ratio between name PVMs and data PVMs.</p>\
			<p class="p-title3">REPLICATION FACTOR<span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">What replication factor do you need for your Namespace?</p>\
			<input name="replication" value=2 type="radio" data-bind="checked: replicationValue"><label>2</label>\
			<input name="replication" value=3 type="radio" data-bind="checked: replicationValue"><label>3</label>\
			<input name="replication" value=4 type="radio" data-bind="checked: replicationValue"><label>4</label>\
			</div>',
		buttonGroup:'<div id="buttonGroup_panel" style="width:280px;float:left;">\
			<div class="p-footer" style="height:60px; width:100%;" align="center">\
			<div style="height:20px;"></div>\
			<button class="s-button" style="width:145px;" id="cancel">CANCEL</button>&nbsp;\
			<button class="cta-button" style="width:145px;" id="saveChanges">SAVE CHANGES</button></div>\
			</div>',
		advancedSttings:'<div id="advancedSttings_panel"><hr class="p-hr-blank"/>\
			<p class="p-title3">NETWORK SETTINGS</p><hr class="p-hr-blank"/><hr class="p-hr-l"/>\
			<p class="p-title3">GATEWAY</p>\
			<p><input id="gateway" placeholder="IP address of defaul gateway" data-bind="value: gateWay" type="text" class="p-input" style="width:90%"/></p>\
			<span id="gateWayErrMsg" class="p-errmsg"></span><hr class="p-hr-blank"/>\
			<p class="p-title3">SUBNET MASK</p>\
			<p><input id="subnetmask" placeholder="Subnet mask (e.g. 255.255.0.0)" data-bind="value: subnetMask" type="text" class="p-input" style="width:90%"/></p>\
			<span id="subnetMaskErrMsg" class="p-errmsg"></span><hr class="p-hr-blank"/><hr class="p-hr-s"/>\
			<p class="p-title3">DNS<span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">The primary domain and upto three name servers are needed to configure the DNS.</p>\
			<input type="text" id="dnsDomain" placeholder="Primary domain(e.g. company.com)" data-bind="value: dnsDomain" class="p-input" style="width:252px;"/>\
			<p id="dnsDomainErrMsg" class="p-errmsg"></p>\
			<hr class="p-hr-blank" />\
			<textarea id="dns" data-bind="value: dnsServer" rows=3 class="p-textbox" style="width:90%"></textarea>\
			<p><span id="dnsServersErrMsg" class="p-errmsg"></span></p><hr class="p-hr-blank"/><hr class="p-hr-s"/>\
			<p class="p-title3">MANAGEMENT IP<span class="help peaxy-icon peaxy-i-question-default" data-help-url=""></span></p>\
			<p class="p-desc">The IP address that is used to administer\
			the hyperfiler after it is configured.</p>\
			<input type="text" id="managementIp" placeholder="Management IP address" data-bind="value: managementIp" class="p-input" style="width:90%"/><br>\
			<p id="managementIpErrMsg" class="p-errmsg"></p><hr class="p-hr-blank"/><hr class="p-hr-s"/>\
			<select data-role="dropdownlist" data-text-field="name" data-value-field="value"  data-bind="source: TIMEZONES, value: timeZone" style="width:90%"></select><hr class="p-hr-blank"/>\
			<p><p class="p-title3">NTP</p><p class="p-desc">At least one NTP server is needed to configure\
			time settings for the hyperfiler.</p></p><textarea id="ntp" data-bind="value: ntpServer" rows=3 class="p-textbox" style="width:90%"></textarea></p>\
			<span id="ntpServersErrMsg" class="p-errmsg"></span><hr class="p-hr-blank"/>'
	}
});