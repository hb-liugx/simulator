var peaxy = peaxy || {};
peaxy.Configuration = kendo.Class.extend({
	STATE : {'CREATE':0,'CONFIG':1,'EDIT':2},
	OPERATE : {'GeneralSetting':0,'StorageClass':1,'DataPolicies':2,'AdvancedSetting':3,'ReviewConfig':4},
	init:function(sysConfig){
		this.curState = this.STATE.CREATE;
		this.sysConfig = {
			userName:'',
			password:'',
   			userEmail:'', 
			gateWay : '',
			subnetMask : '',
			dnsDomain : '',
			dnsServer : '',
			ntpServer : '',
			hyperfilerName : '',
			encryption : 'No',
			managementIp : '',
			replicationValue : '2',
			averageFileSize:'1',/*e.g 1,2,3,4*/
			dataRatioValue : '1:2',
			timeZone:'-8:00',
			scList:[],
			maxCapacity:[],
			ingestList:[],
			totalSpace:0,
			unallocated:0,
			allocated:0,
			ratio : '',
			redundancy : ''
		};
		peaxy.ModuleManager.register('Configuration',this);
	},
	setSysConfig:function(config){
		$.extend(this.sysConfig, config || {});
	},
	getSysConfig:function(handler){
		/* call api /oob/config/get return system config */
		var self = this;
		peaxy.Communication.getScript('js/peaxy.oob.js',function(){
			peaxy.OOB.getConfig(function(config){
				self.setSysConfig(config);
				if($.isFunction(handler))
					handler(config);
			});
		})
	},
	createHf:function(){
		this.curState = this.STATE.CREATE;
		this.importLicense();
	},
	editHf:function(Hyperfiler){
		this.curState = this.STATE.CONFIG;

	},
	importLicense:function(){
		var self = this;
		var dialog = this._createWindow({width:'620px',height:'364px'});

		dialog.open();
		dialog.center();

		if(!peaxy.LiscenseManager){
			peaxy.Communication.getScript('js/peaxy.licensemanagement.js',execLicense);
		} else {
			execLicense();
		}

		function execLicense(){
			dialog.content(self._getSimpleView());
			self.LayoutManager.resize(dialog.wrapper.find('#configurationDialog'));

			var liscense = new peaxy.LicenseManager({
				parent:dialog.wrapper.find('#primaryArea'),
				onSelect:function(data){

				},
				onSuccess:function(data){
					buttonGroup['IMPORT'].off().text('CONTINUE').click(function(e){
						/*if first create open instructions else open initialSettings*/
						if(data == true){
							self.instructions();
						}else{
							self.initialSettings();
						}
					});
				}
			});
			liscense.loadImportLicensePage();
			var buttonGroup = self.LayoutManager.setAction(dialog.wrapper.find('#bottomArea'),
				[{
					name:'IMPORT',/* success change -> 'CONTINUE' */
					action:function(e){
						dialog = self._createWindow({height:'450px'});
						liscense.loadReviewLicensePage();
						buttonGroup['IMPORT'].off().text('CONTINUE').click(function(e){
							if(peaxy.ModuleManager.get('first')){
								self.instructions();
							}else{
								self.initialSettings();
							}
						});
					}
				},{
					name:'CANCEL',
					action:function(e){
						peaxy.tooltipDialog($(e.target),{
								content:'Are you sure you want to cancel?',
								actions:[
									{name:'No'},
									{name:'Yes',action:function(){dialog.close();}}
								]
							}
						);		
					},
					css:{'float':'left'},
					class:'s-button'
				}]
			);
		};
	},
	initialSettings:function(){
		var dialog = this._createWindow({width:'1024px',height:'628px'});
		var self = this,settings;
		dialog.content(self._getSimpleView());
		dialog.center();
		self.LayoutManager.resize(dialog.wrapper.find('#configurationDialog'));
		var buttonGroup = self.LayoutManager.setAction(dialog.wrapper.find('#bottomArea'),
			[{
				name:'CONTINUE',/* success change -> 'CONTINUE'*/
				action:function(e){
					/*No.1 validate input */
					if(settings.checkInput()){
						/*****validate data by server****/
						var data = settings.validateInitSettingsByServer();
						peaxy.Communication.getScript('js/peaxy.bootstrapinterface.js',function(){
							peaxy.BootstrapInterface.validateNetworkSettings(data,function(data){
								if(data.hasError){
									/******show error info by page **********/
									settings.processInitSetResultByServer(data);
								} else {
									/******next page**********/
									self.setSysConfig(settings.getInitSettings());
									self.instalLocallResource(function(){peaxy.ModuleManager.get('Configuration').initialSettings()});
								}
							});
						});										
					}								
				}
			},{
				name:'CANCEL',
				action:function(e){
					peaxy.tooltipDialog($(e.target),{
							content:'Are you sure you want to cancel?',
							actions:[
								{name:'No'},
								{name:'Yes',action:function(){dialog.close();}}
							]
						}
					);
				},
				css:{'float':'left'},
				class:'s-button'
			}]
		);
		buttonGroup['CANCEL'].prop('disabled',true);

		if(!peaxy.HfSettings){
			peaxy.Communication.getScript('js/peaxy.hfsettings.js',execSettings);
		} else {
			execSettings();
		}

		function execSettings(){
			settings = new peaxy.HfSettings();
			settings.getInitData(function(){
				settings.initialSettings({
					sysConfig:self.sysConfig,
					content:dialog.wrapper.find('#primaryArea'),
					onChange:function(data){
						if(!data){
							buttonGroup['CONTINUE'].prop('disabled',true);
						}else{
							buttonGroup['CONTINUE'].prop('disabled',false);
							buttonGroup['CONTINUE'].data('settings',data);
						}
					}
				});
			});			
		}
	},
	instalLocallResource:function(goBack){		
		function cancelWindow(target, handler){
			if($(target).data('kendoTooltip'))
				$(target).data('kendoTooltip').destroy();
			peaxy.tooltipDialog($(target),{
				content:'Are you sure you want to cancel?',
				actions:[
					{name:'No'},
					{name:'Yes',action:function(){dialog.close();if($.isFunction(handler)) handler();}}
				]
			});
		}

		function exitInstall(target, handler){
			if($(target).data('kendoTooltip'))
				$(target).data('kendoTooltip').destroy();
			peaxy.tooltipDialog($(target),{
				content:'Are you sure you want to cancel all installs?',
				actions:[
					{name:'No'},
					{name:'Yes',action:function(e){
						res.cancelInstalls();
						if($.isFunction(handler)) 
							handler();
						$(target).off().click(function(e){
							cancelWindow($(target));
						});
					}}
				]
			});
		}
		var self = peaxy.ModuleManager.get('Configuration'),
		    dialog = self._createWindow({width:'1025px',height:'714px'}),
			res,
			actions,
			initLocRes = [{
			name:'CANCEL',
			action:function(e){
					cancelWindow($(e.target));
				},
				css:{'float':'left'},
				class:'s-button'
			},{
				name:'CONTINUE',
				action:function(e){
					registerAssignIP();
					res.initAssignIpAddress();
				}
			},{
				name:'BACK',
				action:function(e){
					if($.isFunction(goBack)) goBack();
				},
				class:'s-button'	
			}],
			assignIP = [{
				name:'BACK',
				action:function(e){
					res.backToResourceConfig();
					initRes();
				},
				css:{'float':'left'},
				class:'s-button'
			},{
				name:'CONTINUE',
				action:function(e){
					res.setIpInfo(function(ipAssignments){
						registerStartInstall();
						res.initLocalResourceInstall(ipAssignments);				
					});
				}
			}],
			startInstall=[{
				name:'CANCEL',
				action:function(e){
					cancelWindow($(e.target));
				},
				css:{'float':'left'},
				class:'s-button'
			},{
				name:'START INSTALLATION'
			},{
				name:'BACK',
				action:function(e){
					res.backToAssignIpAddress();
					actions = self.LayoutManager.setAction(bottomArea, assignIP);
				},
				class:'s-button'
			}],
			VmInstall=[{
				name:'CANCEL',
				action:function(e){
					exitInstall($(e.target),function(){});
				},
				css:{'float':'left'},
				class:'s-button'
			},{
				name:'INITIALIZE HYPERFILER',
				action:function(e){
					res.startHfInstall(self.sysConfig.averageFileSize, self.sysConfig.replicationValue);
					actions = self.LayoutManager.setAction(bottomArea, installHf);
					actions['CONTINUE'].prop('disabled', true);
					res.setOptions({
						/*when init hyperfile success*/
						onChange:function(bool){
							actions['CANCEL'].off().click(function(e){
								cancelWindow(actions['CANCEL']);
							});
							actions['CONTINUE'].prop('disabled', false);
						}
					});
				}
			}],
			installHf=[{
				name:'CANCEL',
				action:function(e){
					exitInstall($(e.target),function(){dialog.close();});
				},
				css:{'float':'left'},
				class:'s-button'
			},{
				name:'CONTINUE',
				action:function(e){
					self.reviewConfig();
				}
			}];
		function executeRes(){
			/* register actions */
			res = new peaxy.ResourceConfig({
				parent:primaryArea
			});
			initRes();
		}		
		function initRes(){
			var actions = self.LayoutManager.setAction(bottomArea, initLocRes);
			actions['CONTINUE'].prop('disabled', res.getSelectedIdList().length > 0 ? false: true);
			res.setOptions({
				onChange:function(bool){
					actions['CONTINUE'].prop('disabled',!bool);
				}
			});
		}
		function registerAssignIP(){
			actions = self.LayoutManager.setAction(bottomArea, assignIP);
			actions['CONTINUE'].prop('disabled', !res.checkIPAddresses());
			res.setOptions({
				onChange:function(bool){
					/*VALIDATE IP ADDRESSES*/
					actions['CONTINUE'].prop('disabled', !bool);
				}
			});
		}
		function registerStartInstall(){
			var actions = self.LayoutManager.setAction(bottomArea, startInstall);
			res.setOptions({
				onChange:function(bool){
					/* when os install complated successfully*/	
					if(bool){
						actions['START INSTALLATION']
							.text('CONTINUE')
							.prop('disabled', false)
							.off().click(function(e){
								registerVmProgress();
								res.startVmInstall();
							});
						actions['CANCEL'].off().click(function(e){
							cancelWindow($(e.target));
						});
					}else{
						/* when os install error*/
						registerStartInstall();
					}
				}
			});
			actions['START INSTALLATION'].off().click(function(e){
				res.startOsInstall();
				actions['BACK'].prop('disabled', true);
				actions['START INSTALLATION'].text('INSTALLATION IN PROGRESS').prop('disabled', true);
				actions['CANCEL'].off().click(function(e){
					exitInstall($(e.target),function(){
						actions['BACK'].prop('disabled', false);
						actions['START INSTALLATION'].prop('disabled', false).text('START INSTALLATION');
					});
				});
			});
		}

		function registerVmProgress(){
			actions = self.LayoutManager.setAction(bottomArea, VmInstall);
			actions['CANCEL'].off().click(function(e){
				exitInstall($(e.target),function(){
					dialog.close();
				});
			});
			actions['INITIALIZE HYPERFILER'].prop('disabled',true).text('INSTALLATION IN PROGRESS');

			res.setOptions({
				onChange:function(bool){
					/* when install vm  success */
					if(bool){
						actions['CANCEL'].off().click(function(e){
							cancelWindow($(e.target));
						});
						actions['INITIALIZE HYPERFILER']
							.prop('disabled',false)
							.text('INITIALIZE HYPERFILER');
					}
				}
			});
		}

		dialog.content(self._getSimpleView());
		dialog.center();		
		self.LayoutManager.resize(dialog.wrapper.find('#configurationDialog'));
		dialog.center();
		
		var cont = dialog.wrapper.find('#configurationDialog'),
			primaryArea = cont.find('#primaryArea'),
			bottomArea = cont.find('#bottomArea');

		if(!peaxy.ResourceConfig){
			peaxy.Communication.getScript('js/peaxy.resourceconfig.js',executeRes);
		} else {
			executeRes();
		}
	},
	instructions:function(){
		peaxy.Communication.getCSS('css/peaxy.video.style.css');
		var dialog = this._createWindow({width:'700px',height:'570px'});
		var self = this; 
		dialog.content(self._getSimpleView());
		dialog.center();
		self.LayoutManager.resize(dialog.wrapper.find('#configurationDialog'));		
		if(!peaxy.GeneralSetting){
			peaxy.Communication.getScript('js/peaxy.video.js',execute);
		} else {
			execute();
		}
		function execute(){
			new peaxy.Video(dialog.wrapper.find('#primaryArea'));
		}
		var buttonGroup = self.LayoutManager.setAction(dialog.wrapper.find('#bottomArea'),
			[{
				name:'SKIP & GET STARTED',
				action:function(e){
					self.initialSettings();
				}
			},{
				name:'CANCEL',
				action:function(e){
					peaxy.tooltipDialog($(e.target),{
						content:'Are you sure you want to cancel?',
						actions:[
							{name:'No'},
							{name:'Yes',action:function(){dialog.close();}}
						]
					});
				},
				css:{'float':'left'},
				class:'s-button'
			}]
		);
	},
	reviewConfig:function(){
		this.curState = this.STATE.CONFIG;
		this.curOperate = this.OPERATE.ReviewConfig;
		var self = this,
			buttonGroup;			
		/* get systemconfig */
		self.getSysConfig(function(){
			self._initConfLayout(self.OPERATE.ReviewConfig,function(cont){
			 	cont.leftPanel.html(self.Template.reviewConfigArea);
				buttonGroup = self.LayoutManager.setAction(cont.bottomPanel,[{
					name:'SKIP & FINALIZE',
					action:function(e){
						self._finalizeHf($(e.target));
					}
				}]);
			});			
		});
	},
	generalSettings:function(gSettings){
		this.curOperate = this.OPERATE.GeneralSetting;
		var settings,
			self = this;
		this._initConfLayout(this.OPERATE.GeneralSetting,function(cont){
			var buttonGroup = self.LayoutManager.setAction(cont.bottomPanel,[{
				name:'FINALIZE HYPERFILER',
				action:function(e){	self._finalizeHf($(e.target)); }
			}]);
			if(!peaxy.HfSettings){
				peaxy.Communication.getScript('js/peaxy.hfsettings.js',execSettings);
			} else {
				execSettings();
			}
			function execSettings(){
				settings = new peaxy.HfSettings();
				settings.generalSettings(gSettings || self.sysConfig,{
					content:cont.leftPanel
				});
			}
		});
	},
	editStorageClass:function(sconfig){
		this.curState = this.STATE.EDIT;
		this.storageClass({sconfig:sconfig});
	},
	storageClass:function(options){
		this.curOperate = this.OPERATE.StorageClass;		
		var	self = this,
			buttonGroup,
			storageClass,
			scIndex = -1,
			SCName='';

		this._initConfLayout(this.OPERATE.StorageClass,function(cont){
			opt = $.extend({
				sconfig:{},
				onSelect:function(sc){},
				onConfig:function(sc){
					var card = cont.cardView.addStorageClass();
					card.updateStatus('Creation');
					card.updateStatus('');
					card.setData(sc);
					cont.cardView.onCardClick();
					cont.cardView.awakenAll();
				}
			}, options || {});

			opt.sconfig = $.extend({
				id:0,
				performanceLevel:0,//performance
		        className:'',//className
		        replicas:1,//replicas
		        usableSpace:0,//freeSpace
		        totalSpace:0,//totalSpace
		        usedSpace:0,//used
		        flexible:'NO',//flexible
		        defaultValue:'',//statesName
			}, opt.sconfig);

			if(!peaxy.StorageClass){
				peaxy.Communication.getScript('js/peaxy.storageclass.js',initStorageClass);
			}else{
				initStorageClass();
			}

			function initStorageClass(){
			/* check sconfig data parem ? call createMode or editMode */
				if(opt.sconfig.className != ''){
					for(var i=0,l=self.sysConfig.scList.length;i<l;i++){
						if(opt.sconfig.className == self.sysConfig.scList[i].className){
							scIndex = i;
							break;
						}
					}
				}

				/* check curState */			
				if(self.curState == self.STATE.CONFIG){
					buttonGroup = self.LayoutManager.setAction(cont.bottomPanel,[{
						name:'FINALIZE HYPERFILER',
						action:function(e){
							self._finalizeHf($(e.target));
						}
					}]);						
				}else if(self.curState == self.STATE.EDIT){
					buttonGroup = self.LayoutManager.setAction(cont.bottomPanel,[{
						name:'COMMIT CHANGES',
						action:function(e){
							alert('COMMIT CHANGES');
						}
					}]);
				}
				storageClass = new peaxy.StorageClass({
					maxCapacity:self.sysConfig.maxCapacity,
					scList:self.sysConfig.scList,
					parent:cont.leftPanel,
					onDefault:function(scName){
						SCName = scName;
					},
					onChange:function(sc){
						/*changing card data*/
						opt.onSelect(sc);
					},
					onConfig:function(sc){
						function _getHf(sysconfig){
							return {
								hyperfilerName:sysconfig.hyperfilerName,
								totalSpace:sysconfig.totalSpace,
								allocated:function(){
									var count = 0;
									for(var i =0,l=sysconfig.scList.length;i<l;i++){
										count += sysconfig.scList[i].totalSpace
									}
									return count;
								}(),
								unallocated:parseInt(sysconfig.totalSpace) - parseInt(sysconfig.allocated),
								encryption:sysconfig.encryption
							}
						}
						/*check overflow*/
						if((self.sysConfig.totalSpace - self.sysConfig.allocated - parseInt(sc.usableSpace) > 0)){
							/*Config(create or edit success) cardview & catch data */
							opt.onConfig(sc);

							if(sc.defaultValue.indexOf('DEFAULT') > -1){
								for(var i=0,l=self.sysConfig.scList.length;i<l;i++){
									self.sysConfig.scList[i].defaultValue = '';
								}
							}else if(SCName != ''){
								for(var i=0,l=self.sysConfig.scList.length;i<l;i++){
									self.sysConfig.scList[i].defaultValue = 
										(SCName.indexOf(self.sysConfig.scList[i].className) > -1) ? 'DEFAULT':'';
								}
							}

							if(scIndex > -1){
								self.sysConfig.scList[scIndex] = sc;
								/*peaxy.popupMsg(sc.className + ' Edit.',{
							        parent:cont.content.find('#vertical'),
							        class:'p-saved-bg',
							        location:'down',
							        autoHide:true
						        });	*/							
							} else {
								self.sysConfig.scList.push(sc);
								/*peaxy.popupMsg(sc.className + ' Create.',{
							        parent:cont.content.find('#vertical'),
							        class:'p-saved-bg',
							        location:'down',
							        autoHide:true
						        });	*/
							}

							self.setSysConfig(_getHf(self.sysConfig));
							cont.cardView.setData(self._getCardViewData(self.sysConfig));
						} else {

						}
					}
				});
				(opt.sconfig.className == '') ? storageClass.createMode() : storageClass.editMode(opt.sconfig);
				storageClass.draw();
			}
		});
	},
	editPolicy:function(policy){
		this.curState = this.STATE.EDIT;
		this.configPolicy(policy);
	},
	configPolicy:function(policy){
		this.curOperate = this.OPERATE.DataPolicies;
		var policiesManager,
			self = this,
			buttonGroup;

		this._initConfLayout(this.OPERATE.DataPolicies,function(cont){
			buttonGroup = self.LayoutManager.setAction(cont.bottomPanel,[{
				name:'FINALIZE HYPERFILER',
				action:function(e){
					self._finalizeHf($(e.target));
				}
			}]);

			if(!peaxy.PoliciesManagement){
				peaxy.Communication.getScript('js/peaxy.policiesmanagement.js',initPolicy);
			}else{
				initPolicy();
			}

			function initPolicy(){
				var scList = [];
				for(var i=0,l=self.sysConfig.scList.length;i<l;i++){
					scList.push(self.sysConfig.scList[i].className);
				}
				policiesManager = new peaxy.PoliciesManagement({
					 l_panel: cont.leftPanel,
					 r_panel: cont.slidein,
					 store_class_lst: scList,
					 policies: self.sysConfig.ingestList,
					 onConfig:function(policies){self.sysConfig.ingestList = policies}
				});
			}
		});
	},
	advancedSettings:function(){
		this.curOperate = this.OPERATE.AdvancedSetting;
		var buttonGroup,
			settings,
			self = this;
		this._initConfLayout(this.OPERATE.AdvancedSetting, function(cont){
			buttonGroup = self.LayoutManager.setAction(cont.bottomPanel,[{
				name:'FINALIZE HYPERFILER',
				action:function(e){
					self._finalizeHf($(e.target));
				}
			}]);
			if(!peaxy.GeneralSetting){
				peaxy.Communication.getScript('js/peaxy.hfsettings.js',execSettings);
			} else {
				execSettings();
			}
			function execSettings(){
				settings = new peaxy.HfSettings();
				/* init data*/
				var adss = {
					gateWay:self.sysConfig.gateWay, 
					subnetMask:self.sysConfig.subnetMask, 
					dnsDomain:self.sysConfig.dnsDomain, 
					dnsServer:self.sysConfig.dnsServer, 
					managementIp:self.sysConfig.managementIp, 
					timeZone:'-8:00',//self.sysConfig.timeZone, 
					ntpServer:self.sysConfig.ntpServer
				};
				settings.advancedSettings(adss,{
					content:cont.leftPanel,
					onConfig:function(data){
						self.setSysConfig(data);
					}
				});
			}
		});
	},
	finalizeHf:function(goBack){
		var dialog = this._createWindow({width:'600',height:'328px'}),
			self = this,
			fhf,
			buttonGroup1 = [{
				name:'BACK',
				action:function(e){
					if($.isFunction(goBack)) 
						goBack();
				},
				class:'s-button'
			},{
				name:'CANCEL',
				action:function(e){
					peaxy.tooltipDialog($(e.target),{
						content:'Are you sure you want to cancel?',
						actions:[
							{name:'No'},
							{name:'Yes',action:function(){dialog.close();}}
						]
					});
				},
				css:{'float':'left'},
				class:'s-button'
			}],
			buttonGroup2 = [{
				name:'OK',
				action:function(e){
					dialog.close();
				}
			}];

		dialog.content(self._getSimpleView());
		dialog.center();
		self.LayoutManager.resize(dialog.wrapper.find('#configurationDialog'));
		peaxy.Communication.getScript('js/peaxy.finalizehf.js', execFHF);
		function execFHF(){
			fhf = new peaxy.FinalizeHyperfiler(self.sysConfig,{
				parent:dialog.wrapper.find('#primaryArea'),
				onComplete:function(bool){
					self.LayoutManager.setAction(dialog.wrapper.find('#bottomArea'),bool ? buttonGroup2:buttonGroup1);
				}
			});
		}
	},
	_initConfLayout:function(operate, handler){
		var self = this,
			dialog = this._createWindow({width:'1025px',height:'714px'}),
			content = dialog.wrapper.find('#configurationDialog'),
			cardView,
			slidein;
		operate = parseInt(operate);
		dialog.open();
		/* test data*/
		var cardViewVM = {
			hyperfilerName:'',
			totalSpace:'',
			unallocated:'',
			allocated:'',
			encryption:'',
			storageClasses:{
				namespaceCardData:{
					ratio:'',
					redundancy:''
				},
				classes:[]
			}
		};
		function getCont(){
			return{
				'content':content,
				'menu':content.find('#configmenu').data('kendoMenu'),
				'leftPanel':content.find("#left-pane").empty(),
				'rightPanel':content.find("#right-pane"),
				'bottomPanel':content.find('#bottomArea'),
				'cardView':content.find('#right-pane').data('cardview'),
				'dialog': dialog,
				'slidein':content.find('#c_slideleft').data('slidein')
			}
		};
		if(!content.find('#vertical').data('isinited')){
			this.curOperate = operate;
			content.html(this._getConfigView());
			content.find('#vertical').data('isinited',true);
			content.find('#configmenu').kendoMenu({
		        select:function(e){
		        	if($(e.item).data('operate') == self.OPERATE.StorageClass
		        		&& $('#configurationDialog').find('#right-pane').data('sconfig')){
		        		return;
		        	}
	            	setPosition(content, $(e.item));
	            	setMenuAction($(e.item).data('operate'));
		        }
		    });
			var items = content.find('li');
	    	setPosition(content, $(items.get(operate)));
		    peaxy.Communication.getScript('js/peaxy.cardview.js',function(){
		    	peaxy.Communication.getScript('js/peaxy.slidein.js',function(){
		    		initCardView();
		    		handler(getCont());
		    	});
		    });		
			dialog.center();
	    } else {
	    	var cc = getCont();
			if (content.find('#right-pane').data('sconfig') && operate != self.OPERATE.StorageClass){
				content.find('#right-pane').removeData('sconfig');
				cc.cardView.awakenAll();
				if(content.find('#right-pane').data('mode')){
					content.find('#right-pane').removeData('mode');
					content.find('#right-pane').data('card').destroy();
					content.find('#right-pane').removeData('card');
				}
			}
			var items = content.find('li');
	    	setPosition(content, $(items.get(operate)));
	    	cc.slidein.hide();
	    	cc.slidein.getContainer().empty();
	    	handler(getCont());
		}
		function initCardView(){
			peaxy.Communication.getCSS('css/peaxy.cardview.css'); 
			cardView = new peaxy.CardView({
				parent:content.find('#right-pane'),
				onAddRes:function(){
					switch(self.curOperate){
						case self.OPERATE.GeneralSetting :
							peaxy.ModuleManager.get('Configuration').instalLocallResource(function(){
								peaxy.ModuleManager.get('Configuration').generalSettings();
							});
						break;
						case self.OPERATE.StorageClass :
							peaxy.ModuleManager.get('Configuration').instalLocallResource(function(){
								peaxy.ModuleManager.get('Configuration').storageClass();
							});
						break;
						case self.OPERATE.DataPolicies :
							peaxy.ModuleManager.get('Configuration').instalLocallResource(function(){
								peaxy.ModuleManager.get('Configuration').configPolicy();
							});
						break;
						case self.OPERATE.AdvancedSetting :
							peaxy.ModuleManager.get('Configuration').instalLocallResource(function(){
								peaxy.ModuleManager.get('Configuration').advancedSettings();
							});
						break;
						case self.OPERATE.ReviewConfig :
							peaxy.ModuleManager.get('Configuration').instalLocallResource(function(){
								peaxy.ModuleManager.get('Configuration').reviewConfig();
							});
						break;
					}
				},
				onConfig:function(card){
					/*check create || edit */
					content.find('#right-pane').data('sconfig',1);
					if(card.getData().className == ''){
						content.find('#right-pane').data('mode','new');
						content.find('#right-pane').data('card',card);
					}else{
						/*edit card state*/
						if(content.find('#right-pane').data('mode')){
							content.find('#right-pane').data('card').destroy();
							content.find('#right-pane').removeData('card');
							content.find('#right-pane').removeData('mode');
						}
					}
					/*call stoargeClass*/
					self.storageClass({
						sconfig:card.getData() || {},
						onSelect:function(sc){
							content.find('#right-pane').data('sconfig',1);
							card.updateStatus('Creation');
							card.setData(sc);							
						},
						onConfig:function(sc){
							if(content.find('#right-pane').data('mode')){
								content.find('#right-pane').removeData('mode');
								content.find('#right-pane').removeData('card');
							}
							card.updateStatus('');
							card.setData(sc);
							cardView.awakenAll();
							/* if reset default  storageclass  reset cardview data  do this */
						}
					});
				 }
			});
			content.find('#right-pane').data('cardview', cardView);
			/*init cardview data*/
			if(self.sysConfig.hyperfilerName != ''){
				cardView.setData(self._getCardViewData(self.sysConfig));
			}else{
				self.getSysConfig(function(config){
					cardView.setData(self._getCardViewData(self.sysConfig));
				});
			}
			cardView.draw();
			/*add slidein */
			content.find('#right-pane').prepend($('<div id="c_slideleft" style="position:relative;z-index:9999;"></div>'));			
			slidein = new peaxy.Slidein({
				parent:content.find('#right-pane'),
				container:content.find('#c_slideleft'),
				width:content.find('#right-pane').width()+'px',
			    height:content.find('#right-pane').height()+'px',
			    location:'left',
			    hasTitle:false
			});
			slidein.getContainer().css({'border':0});
			content.find('#c_slideleft').data('slidein',slidein);
			slidein.hide();
		}
		function setPosition(cont,target){
	    	if(operate == self.OPERATE.ReviewConfig) {
	    		cont.find("#menuMark").css({'padding-left':0});
		    	cont.find('#menuMarkBar').width(0);
	    	}else{
			    cont.find("#menuMark").css({'padding-left':(target.position().left )+'px'});
			    cont.find('#menuMarkBar').width(target.innerWidth() - 22);
		    }
		}
		/*set meun action */
		function setMenuAction(operate){
			switch(operate){
				case self.OPERATE.GeneralSetting :
					self.generalSettings();					
				break;
				case self.OPERATE.StorageClass :
					self.storageClass();
				break;
				case self.OPERATE.DataPolicies :
					self.configPolicy();
				break;
				case self.OPERATE.AdvancedSetting :
					self.advancedSettings();
				break;
			}
		};
	},
	_createWindow:function(options){
		var configDialog;
		if($('#configurationDialog').length > 0){
			configDialog = $('#configurationDialog');
		}else{
			configDialog = $('<div id="configurationDialog"></div>').appendTo("body");
		}
		configDialog.css({'overflow':'hidden','position':'relative'});		
		var self = this;
		var _opt = {
			width: "600px",
			height:"300px",
			resizable:false,
			modal :true
		};
		$.extend(_opt, options || {});

		if (!configDialog.data("kendoWindow")){
			configDialog.empty();
			configDialog.kendoWindow(_opt);
			configDialog.data("kendoWindow").wrapper.find('.k-i-close')
				.parent().html('<span class="p-secondary-icons p-si-close"></span>');

			configDialog.data("kendoWindow").wrapper.find('.p-si-close').click(function(){
				peaxy.tooltipDialog($(this),{
					content:'Would you like to commit your changes first?',
					actions:[
						{name:'No'},
						{name:'Yes',action:function(){configDialog.data("kendoWindow").close();}}
					]
				});
			});
			configDialog.data("kendoWindow").center();
		}
		configDialog.data("kendoWindow").setOptions(_opt);

		/*set title 0= 'create Hyperfiler' , 1= 'config Hyperfiler' 2= 'edit Hyperfiler'*/
		var title='';
		switch(this.curState){
			case this.STATE.CREATE:
				title = 'CREATE HYPERFILER';
				configDialog.data("kendoWindow").wrapper.find('.p-si-close').parent().hide();
				break;
			case this.STATE.CONFIG:
				title = 'CONFIGURE HYPERFILER';
				configDialog.data("kendoWindow").wrapper.find('.p-si-close').parent().hide();
				break;
			case this.STATE.EDIT:
				title = 'EDIT HYPERFILER';
				configDialog.data("kendoWindow").wrapper.find('.p-si-close').parent().show();
				break;
		}
		this.dialog = configDialog.data("kendoWindow");
		configDialog.data("kendoWindow").title(title);
		return configDialog.data("kendoWindow");
	},
	_getSimpleView:function(){
		return (this.Template.primaryArea + this.Template.bottomArea);
	},
	_getConfigView:function(){
		return (this.Template.workflowArea + this.Template.bottomArea);
	},
	_exitApp:function(handler){
		if(!peaxy.BootstrapInterface){
			peaxy.Communication.getScript('js/peaxy.bootstrapinterface.js',function(){
				peaxy.BootstrapInterface.terminateBootstrap();
				if(jQuery.isFunction(handler)){	handler(); }
			});
		}else{
			peaxy.BootstrapInterface.terminateBootstrap();
			if(jQuery.isFunction(handler)){	handler(); }
		}
	},
	_finalizeHf:function(target){
		var self = this;
		peaxy.tooltipDialog(target,{
			content:'Are you sure you want to finalze these changes to Hyperfiler?',
			actions:[
				{name:'No'},
				{name:'Yes',action:function(){self.finalizeHf();}}
			]
		});	
	},
	_getCardViewData:function(config){
		var totalSpace = 0, allocated = 0;
		return {
			hyperfilerName : config.hyperfilerName,
			totalSpace : config.totalSpace,
			unallocated : config.unallocated,
			allocated : config.allocated,
			encryption : config.encryption,
			storageClasses : {
				namespaceCardData:{
					ratio : config.ratio,
					redundancy : config.redundancy
				},
				classes: config.scList
			}
		};
	},
	close:function(){
		if(this.dialog)
			this.dialog.close();
	},
	LayoutManager:{
		resize:function(content){
			var navigate = content.find('#navigateArea'),
				primary = content.find('#primaryArea'),
				bottom = content.find('#bottomArea');
			if(primary.length > 0){
				primary.height(content.height()
	 			   - (navigate.length > 0 ? navigate.height() : 0)
				   - (bottom.length > 0 ? bottom.height() : 0)
			    );
			}
		},
		setAction:function(content, actions){
			/* action json = {name:'',action:function(e){}, class:"", css:{}} } */
			content.empty();
			var buttons={}, buttonGroup=$('<ul></ul>');
			buttonGroup.css({'white-space':'nowrap','padding':'0 10px','text-align':'center'});
			for(var i = 0,l =actions.length;i<l;i++){
				action = actions[i] = $.extend({name:'Button',action:function(e){},class:'save-button'}, actions[i]);
				var button = $('<button></button>');
				button.addClass(action.class);
				button.css($.extend({margin:'12px 5px 5px','min-width':'100px','float':'right'}, action.css));
				button.text(action.name);
				button.data('index',i);
				buttonGroup.append(button);
				buttons[action.name] = button;
			}
			buttonGroup.find('button').click(function(e){
				actions[$(this).data('index')].action(e);
			});
			content.html(buttonGroup);
			return buttons;
		}
	},
	Template:{
		reviewConfigArea :'<p class="p-title-header">REVIEW AND CUSTOMIZE<br/>YOUR CONFIGURATION</p><div class="p-content">\
			<p class="p-desc">We have created your Hyperfiler for you. At this time you\
			can further customize the configuration of your Hyperfiler.</p><hr class="p-hr-blank">\
			<p class="p-desc">You can add additional resources, update the general\
			settings, edit and add your storage classes and add custom\
			data management policies.</p></div>',
		primaryArea : '<div id="primaryArea" style="width:100%;margin:0 auto;color:black;overflow:hideen;"></div>',
		bottomArea : '<div id="bottomArea" class="p-bottom"></div>',
		workflowArea:'<div class="p-menu">\
			<div style="height:30px;overflow:hidden;">\
	            <ul id="configmenu">\
	                <li class="p-title3" style="color:white;" data-operate="0">GENERAL SETTINGS<span class="p-secondary-icons p-si-nav-divider"></span></li>\
	                <li class="p-title3" style="color:white;" data-operate="1">STORAGE CLASSES<span class="p-secondary-icons p-si-nav-divider"></span></li>\
	                <li class="p-title3" style="color:white;" data-operate="2">DATA MANAGEMENT POLICIES<span class="p-secondary-icons p-si-nav-divider"></span></li>\
	                <li class="p-title3" style="color:white;" data-operate="3">ADVANCED SETTINGS<span class="p-secondary-icons p-si-blank"></span></li>\
	            </ul></div>\
	            <div id="menuMark" style="height:8px;">\
	                <div id="menuMarkBar" style="height:100%;width:0;background:#00A3E0;"></div>\
	            </div>\
	        </div>\
		    <div id="primaryarea" class="p-cp-area">\
	            <div id="vertical" style="height:100%;">\
	                <div id="left-pane" class="p-left-panel p-shadow"></div>\
	                <div id="right-pane" class="p-right-panel"></div>\
	            </div>\
	        </div>'
	}
});