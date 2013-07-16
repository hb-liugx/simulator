var peaxy = peaxy || {};

peaxy.MonitoringCard = kendo.Class.extend({
	parentDiv:null,
	cardView:null,
	defaultView:null,
	infoView:null,
	cardHeader:null,
	cardToolBar:null,
	statusBar:null,
	cardBody:null,
	_options:{
		width:308,
		height:479,
		parent:document.body,
		id:'',
		statusBar:'',
		cardToolBar:'',
		cardHeader:'',
		cardBody:'',
		cardType:''
	},

    init: function(options) {
		jQuery.extend(this._options,options);
		this.parentDiv = this._options.parent;
		if(this._options.cardType == "pvm")
			this.cardView = $('<div id='+ this._options.id +' style="background: #FFFFFF;position: relative; width:'+ this._options.width +'px; height:'+ this._options.height +'px">');
		else
			this.cardView = $('<div id='+ this._options.id +' class="card" style="position: relative; width:'+ this._options.width +'px; height:'+ this._options.height +'px">');
		this.cardView.data('card',this);
		// status bar
		this.cardView.append(this._options.statusBar); 
		//header
		this.cardView.append(this._options.cardHeader);
		// tool bar
		this.cardView.append(this._options.cardToolBar);
		// content
		this.cardView.append(this._options.cardBody);
    },
	
	destroy: function(){
		this.cardView.remove();
	},
	
	draw: function(){
		this.parentDiv.append(this.cardView);
		
		//infoStatus
		//defaultStatus
	},
	show:function(){
		this.parentDiv.show();
	},
	
	hide:function(){
		this.parentDiv.hide();
	}
});

peaxy.MonitoringNameSpaceCard = peaxy.MonitoringCard.extend({
	nameSpaceCardViewModel:null,
	_options:{
		dataSource:{
			totalCapacity: '%',
			freeCapacity: '00',
			usedCapacity: '00',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			bandwidth:'000.0',
			replicas: '1',
			dataRatio:'1:4',
			watchlist:false
		}
	},
	init: function(options) {
		jQuery.extend(this._options,options);
		this._options.id = 'card_' + (new Date().getTime()) + parseInt(Math.random()*100 + 1);
		this.id = this._options.id;
		this._options.statusBar = this.Template.statusBar;
		this._options.cardHeader = this.Template.cardHeader;
		this._options.cardToolBar = this.Template.cardToolBar;
		this._options.cardBody = this.Template.cardBody;
		peaxy.MonitoringCard.fn.init.call(this,this._options);
		
		this.nameSpaceCardViewModel = kendo.observable({
			totalCapacity: '%',
			freeCapacity: '00',
			usedCapacity: '00',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			bandwidth:'000.0',
			replicas: '1',
			dataRatio:'1:4',
			watchlist:false
		});
		kendo.bind(this.cardView, this.nameSpaceCardViewModel);
		
		var self = this;
		self.cardView.find("#infoStatus").hide();
		self.cardView.find("#switchbutton").kendoPeaxySwitch();
		
		self.cardView.find("#switchbutton").prop("checked",self.nameSpaceCardViewModel.watchlist);
		self.cardView.find('#switchbutton').click(function(){
			self.nameSpaceCardViewModel.set("watchlist",self.cardView.find("#switchbutton").prop("checked"));
		});
		
		self.cardView.find('#btn_bandwidth').click(function(){
			alert("AVG. BANDWIDTH");
		});
		
		var effect = kendo.fx("#"+self._options.id).flipHorizontal(self.cardView.find('#defaultStatus'), self.cardView.find('#infoStatus')).duration(1000),
            reverse = false;

		self.cardView.find('#tool_info').click(function(){
			effect.stop();
            reverse ? effect.reverse() : effect.play();
            reverse = !reverse;
			if(reverse)
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Light-Selected");
			else
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Dark");
		});
		
		self.cardView.find('#tool_analysisview').click(function(){
			alert("Analysis View");
		});
		
		self.cardView.find('#tool_history').click(function(){
			alert("History View");
		});
		
		self.cardView.find('#btn_notifications').click(function(){
			alert("btn_notifications");
		});
		self.cardView.find('#btn_edit').click(function(){
			alert("EDIT");
		});
		self.updataStatus();
    },
	
	updataStatus: function(){
		var f,c,d,s,self = this;
		f = this.nameSpaceCardViewModel.failedValue;
		c = this.nameSpaceCardViewModel.criticalValue;
		d = this.nameSpaceCardViewModel.degradedValue;
		s = this.nameSpaceCardViewModel.selfHealingValue;
		if(f == null || f.length == 0)
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed-Disabled");
		else
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed");
		if(c == null || c.length == 0)
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical-Disabled");
		else
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical");
		if(d == null || d.length == 0)
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded-Disabled");
		else
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded");
		if(s == null || s.length == 0)
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing-Disabled");
		else
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing");
	},

	setData: function(viewModel){
		var self = this;
		jQuery.extend(self._options.dataSource,viewModel);
		self.nameSpaceCardViewModel.set('totalCapacity', self._options.dataSource.totalCapacity);
		self.nameSpaceCardViewModel.set('freeCapacity', self._options.dataSource.freeCapacity);
		self.nameSpaceCardViewModel.set('usedCapacity', self._options.dataSource.usedCapacity);
		self.nameSpaceCardViewModel.set('bandwidth', self._options.dataSource.bandwidth);
		self.nameSpaceCardViewModel.set('notificationValue', self._options.dataSource.notificationValue);
		self.nameSpaceCardViewModel.set('failedValue', self._options.dataSource.failedValue);
		self.nameSpaceCardViewModel.set('criticalValue', self._options.dataSource.criticalValue);
		self.nameSpaceCardViewModel.set('degradedValue', self._options.dataSource.degradedValue);
		self.nameSpaceCardViewModel.set('selfHealingValue', self._options.dataSource.selfHealingValue);
		self.nameSpaceCardViewModel.set('replicas', self._options.dataSource.replicas);
		self.nameSpaceCardViewModel.set('dataRatio', self._options.dataSource.dataRatio);
		self.nameSpaceCardViewModel.set('watchlist', self._options.dataSource.watchlist);
		self.updataStatus();
	},
	
	draw: function(){
		peaxy.MonitoringCard.fn.draw.call(this);
	},
	
	Template:{
		statusBar:'<div id="namespace_status" class="cardStatus"></div>',
		cardHeader:'<div id="namespace_header" class="namespace_header"><span class="namespace_title_header">NAMESPACE</span><span class="p-secondary-icons p-si-White-Arrow-Right"></span></div>',
		cardToolBar:'<div id="namespace_toolbar" class="namespace_toolbar">\
					<div style="float:right;margin-right:10px;"><span id="tool_history" class="p-primary-icons p-i-Analysis-View-Dark" style="cursor: pointer;"></span>\
					<span id="tool_analysisview" class="p-primary-icons p-i-Analysis-View-Dark" style="cursor: pointer;"></span>\
					<span id="tool_info" value=0 class="p-primary-icons p-i-Info-Dark" style="cursor:pointer;"></span></div></div>',
		cardBody:   '<div id="defaultStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<table style="width:100%;margin-top:25px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="namespace_underline1" colspan="4" style="width:50%"><span class="n-title1" data-bind="html: totalCapacity"></span></td>\
								<td colspan="4" rowspan="3"></td>\
							</tr>\
							<tr>\
								<td class="n-title3 namespace_underline2" colspan="2">FREE</td>\
								<td class="namespace_default_value namespace_underline2"><span class="n-title4" data-bind="html: freeCapacity"></span></td>\
								<td class="namespace_default_value namespace_underline2"><span class="n-title3">%</span></td>\
							</tr>\
							<tr>\
								<td class="n-title3 namespace_underline2" colspan="2">USED</td>\
								<td class="namespace_default_value namespace_underline2"><span class="n-title4" data-bind="html: usedCapacity"></span></td>\
								<td class="namespace_default_value namespace_underline2"><span class="n-title3">%</span></td>\
							</tr>\
							<tr>\
								<td class="namespace_default_value namespace_underline1" colspan="8">&nbsp;</td>\
							</tr>\
							<tr class="p-fill-Wheat">\
								<td class="n-title3 namespace_underline2" colspan="4">AVG. BANDWIDTH</td>\
								<td class="namespace_default_value namespace_underline2" colspan="2"><span class="n-title4" data-bind="html: bandwidth"></span></td>\
								<td class="namespace_default_tb namespace_underline2"><span class="n-title3">MB/s</span></td>\
								<td class="namespace_default_tb namespace_underline2"><span id="btn_bandwidth" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="n-title3 namespace_underline2" colspan="4">REPLICAS</td>\
								<td class="namespace_default_value namespace_underline2" colspan="2"><span class="n-title4" data-bind="html: replicas"></span></td>\
								<td class="namespace_default_tb namespace_underline2"></td>\
								<td class="namespace_default_tb namespace_underline2"></td>\
							</tr>\
						</table>\
						<table style="width:100%;margin-top:25px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="card_default_tool_td card_underline1" style="text-align:left;"><span class="p-primary-icons p-i-Notifications"></span><span class="n-title4" data-bind="html: notificationValue"></span></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1" style="text-align:right"><span id="btn_notifications" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span id="icon_failed" class="p-primary-icons p-i-Failed"></span></td>\
								<td class="card_default_tool_td"><span id="icon_critical" class="p-primary-icons p-i-Critical"></span></td>\
								<td class="card_default_tool_td"><span id="icon_degraded" class="p-primary-icons p-i-Degraded"></span></td>\
								<td class="card_default_tool_td"><span id="icon_self-healing" class="p-primary-icons p-i-Self-Healing"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span class="c-notification1" data-bind="html: failedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification2" data-bind="html: criticalValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification3" data-bind="html: degradedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification4" data-bind="html: selfHealingValue"></span></td>\
							</tr>\
						</table>\
				    </div>\
					<div id="infoStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<div id="infoStatus_top">\
							<table style="width:100%;margin-top:25px;" border=0 cellspacing="0" cellpadding="3">\
								<tr>\
									<td class="n-title3 namespace_underline2">FREE</td>\
									<td class="namespace_default_value namespace_underline2"><span class="n-title4" data-bind="html: freeCapacity"></span></td>\
									<td class="namespace_default_tb namespace_underline2"><span class="n-title3">%</span></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">USED</td>\
									<td class="namespace_default_value namespace_underline2"><span class="n-title4" data-bind="html: usedCapacity"></span></td>\
									<td class="namespace_default_tb namespace_underline2"><span class="n-title3">%</span></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">REPLICAS</td>\
									<td class="namespace_default_value namespace_underline2"><span class="n-title4" data-bind="html: replicas"></span></td>\
									<td class="namespace_default_tb namespace_underline2"></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">NAME : DATA RATIO</td>\
									<td class="namespace_default_value namespace_underline2"><span class="n-title4" data-bind="html: dataRatio"></span></td>\
									<td class="namespace_default_tb namespace_underline2"></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">ADD TO WATCHLIST</td>\
									<td class="namespace_underline2" colspan="2"><div style="float:right"><input id="switchbutton" type="checkbox"/></div></td>\
								</tr>\
							</table>\
						</div>\
						<div style="margin-top:120px;position:absolute;">\
								<button id="btn_edit" style="width:288px" class="s-button">EDIT</button>\
						</div>\
					</div>'
		
	}
});

peaxy.MonitoringHyperfilerCard = peaxy.MonitoringCard.extend({
	hyperfilerCardViewModel:null,
	_options:{
		dataSource:{
			hyperfilerName:'HYPERFILER1',
			location: 'San Jose,CA',
			totalCapacity: '000.00',
			freeCapacity: '000.00',
			usedCapacity: '000.00',
			classes : '3',
			bandwidth: '000.0',
			encryption: 'NO',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			clients: '6',
			dataPolicies: '26',
			watchlist:false
		}
	},

    init: function(options) {
		jQuery.extend(this._options,options);
		this._options.id = 'card_' + (new Date().getTime()) + parseInt(Math.random()*100 + 1);
		this.id = this._options.id;
		this._options.statusBar = this.Template.statusBar;
		this._options.cardHeader = this.Template.cardHeader;
		this._options.cardToolBar = this.Template.cardToolBar;
		this._options.cardBody = this.Template.cardBody;
		peaxy.MonitoringCard.fn.init.call(this,this._options);
		
		this.hyperfilerCardViewModel = kendo.observable({
			hyperfilerName:'HYPERFILER1',
			location: 'San Jose,CA',
			totalCapacity: '000.00',
			freeCapacity: '000.00',
			usedCapacity: '000.00',
			classes : '3',
			bandwidth: '000.0',
			encryption: 'NO',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			clients: '6',
			dataPolicies: '26',
			watchlist:false
		});
		kendo.bind(this.cardView, this.hyperfilerCardViewModel);
		
		var self = this;
		self.cardView.find("#infoStatus").hide();
		self.cardView.find("#switchbutton").kendoPeaxySwitch();
		
		self.cardView.find("#switchbutton").prop("checked",self.hyperfilerCardViewModel.watchlist);
		self.cardView.find('#switchbutton').click(function(){
			self.hyperfilerCardViewModel.set("watchlist",self.cardView.find("#switchbutton").prop("checked"));
		});
		
		self.cardView.find('#btn_notifications').click(function(){
			alert("notifications");
		});
		
		self.cardView.find('#btn_bandwidth').click(function(){
			alert("AVG. BANDWIDTH");
		});
		
		var effect = kendo.fx("#"+self._options.id).flipHorizontal(self.cardView.find('#defaultStatus'), self.cardView.find('#infoStatus')).duration(1000),
            reverse = false;

		self.cardView.find('#tool_info').click(function(){
			effect.stop();
            reverse ? effect.reverse() : effect.play();
            reverse = !reverse;
			if(reverse)
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Light-Selected");
			else
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Light");
		});
		
		self.cardView.find('#tool_analysisview').click(function(){
			alert("Analysis View");
		});
		
		self.cardView.find('#tool_history').click(function(){
			alert("History View");
		});
		
		self.cardView.find('#btn_clients').click(function(){
			alert("CLIENTS");
		});
		
		self.cardView.find('#btn_datapolicies').click(function(){
			alert("Data POLICIES");
		});
		
		self.cardView.find('#btn_delete').click(function(){
			alert("DELETE");
		});
		
		self.cardView.find("#btn_delete").click(function(e){
			peaxy.tooltipDialog($(e.target),{
				content:'Are you sure you want to delete\
				this Hyperfiler? All data on this \
				Hyperfiler will be lost if it is not \
				backed up.',
				contentCSS:{'background-color':'#EF354F','color':'#FFF','font-size':'14px'},
				width:'240px',
				actions:[
					{ name:'NO', action:function(){
							
						},
						css:{'width':'100px'}
					},
					{ name:'YES',action:function(){
							self.destroy();
						},
						css:{'width':'100px'}
					}
				]
			});      
        });
		
		self.cardView.find('#btn_shutdown').click(function(e){
			peaxy.tooltipDialog($(e.target),{
				content:'Are you sure you want to shut\
				down this Hyperfiler? All data on \
				this Hyperfiler will be \
				inaccessible.',
				contentCSS:{'background-color':'#EF354F','color':'#FFF','font-size':'14px'},
				width:'240px',
				actions:[
					{ name:'NO', action:function(){
							
						},
						css:{'width':'100px'}
					},
					{ name:'YES',action:function(){
							self.destroy();
						},
						css:{'width':'100px'}
					}
				]
			});      
		});
		
		self.cardView.find('#btn_edit').click(function(){
			alert("EDIT");
		});
		self.updataStatus();
    },
	
	updataStatus: function(){
		var f,c,d,s,self = this;
		f = this.hyperfilerCardViewModel.failedValue;
		c = this.hyperfilerCardViewModel.criticalValue;
		d = this.hyperfilerCardViewModel.degradedValue;
		s = this.hyperfilerCardViewModel.selfHealingValue;
		if(f == null || f.length == 0)
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed-Disabled");
		else
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed");
		if(c == null || c.length == 0)
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical-Disabled");
		else
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical");
		if(d == null || d.length == 0)
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded-Disabled");
		else
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded");
		if(s == null || s.length == 0)
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing-Disabled");
		else
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing");
	},

	setData: function(viewModel){
		var self = this;
		jQuery.extend(self._options.dataSource,viewModel);
		self.hyperfilerCardViewModel.set('hyperfilerName', self._options.dataSource.hyperfilerName);
		self.hyperfilerCardViewModel.set('location', self._options.dataSource.location);
		self.hyperfilerCardViewModel.set('totalCapacity', self._options.dataSource.totalCapacity);
		self.hyperfilerCardViewModel.set('freeCapacity', self._options.dataSource.freeCapacity);
		self.hyperfilerCardViewModel.set('usedCapacity', self._options.dataSource.usedCapacity);
		self.hyperfilerCardViewModel.set('classes', self._options.dataSource.classes);
		self.hyperfilerCardViewModel.set('bandwidth', self._options.dataSource.bandwidth);
		self.hyperfilerCardViewModel.set('encryption', self._options.dataSource.encryption);
		self.hyperfilerCardViewModel.set('notificationValue', self._options.dataSource.notificationValue);
		self.hyperfilerCardViewModel.set('failedValue', self._options.dataSource.failedValue);
		self.hyperfilerCardViewModel.set('criticalValue', self._options.dataSource.criticalValue);
		self.hyperfilerCardViewModel.set('degradedValue', self._options.dataSource.degradedValue);
		self.hyperfilerCardViewModel.set('selfHealingValue', self._options.dataSource.selfHealingValue);
		self.hyperfilerCardViewModel.set('clients', self._options.dataSource.clients);
		self.hyperfilerCardViewModel.set('dataPolicies', self._options.dataSource.dataPolicies);
		self.hyperfilerCardViewModel.set('watchlist', self._options.dataSource.watchlist);
		self.updataStatus();
	},
	
	draw: function(){
		peaxy.MonitoringCard.fn.draw.call(this);
	},
	
	Template:{
		statusBar:'<div id="hyperfiler_status" class="cardStatus"></div>',
		cardHeader:'<div id="hyperfiler_header" style="cursor:pointer;" class="card_header"><span class="card_title_header" data-bind="html: hyperfilerName"></span><span class="p-secondary-icons p-si-White-Arrow-Right"></span></div>',
		cardToolBar:'<div id="hyperfiler_toolbar" class="card_toolbar">\
					<div style="float:left">\
					<span class="p-secondary-icons p-si-Location-White"></span>\
					<span class="card_title_toolbar" data-bind="html: location"></span></div>\
					<div style="float:right;margin-right:10px;"><span id="tool_history" class="p-primary-icons p-i-Analysis-View-Light" style="cursor: pointer;"></span>\
					<span id="tool_analysisview" class="p-primary-icons p-i-Analysis-View-Light" style="cursor: pointer;"></span>\
					<span id="tool_info" value=0 class="p-primary-icons p-i-Info-Light p-i-Info-Light:hover" style="cursor:pointer;"></span></div></div>',
		cardBody:   '<div id="defaultStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<table style="width:100%;margin-top:25px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="card_underline1" colspan="2"><span class="c-title1" data-bind="html: totalCapacity"></span><span class="c-title2">TB</span></td>\
								<td class="card_underline1"></td>\
								<td colspan="4" rowspan="4" style="width:94px"></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2">FREE</td>\
								<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: freeCapacity"></span></td>\
								<td class="card_default_tb card_underline2"><span class="c-title3">TB</span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2">USED</td>\
								<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: usedCapacity"></span></td>\
								<td class="card_default_tb card_underline2"><span class="c-title3">TB</span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2" colspan="2">STORAGE CLASSES</td>\
								<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: classes"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_value card_underline1" colspan="6">&nbsp;</td>\
							</tr>\
							<tr class="p-fill-DarkSlate">\
								<td class="c-title3 card_underline2" colspan="2">AVG. BANDWIDTH</td>\
								<td class="card_default_value card_underline2" colspan="2"><span class="c-title4" data-bind="html: bandwidth"></span></td>\
								<td class="card_default_tb card_underline2"><span class="c-title3">MB/s</span></td>\
								<td class="card_default_tb card_underline2"><span id="btn_bandwidth" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2" colspan="3">ENCRYPTION ON THE WIRE</td>\
								<td class="card_default_value card_underline2" colspan="3"><span class="c-title4" data-bind="html: encryption"></span></td>\
							</tr>\
						</table>\
						<table style="width:100%;margin-top:5px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="card_default_tool_td card_underline1" style="text-align:left;"><span class="p-primary-icons p-i-Notifications"></span><span class="c-title4" data-bind="html: notificationValue"></span></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1" style="text-align:right"><span id="btn_notifications" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span id="icon_failed" class="p-primary-icons p-i-Failed"></span></td>\
								<td class="card_default_tool_td"><span id="icon_critical" class="p-primary-icons p-i-Critical"></span></td>\
								<td class="card_default_tool_td"><span id="icon_degraded" class="p-primary-icons p-i-Degraded"></span></td>\
								<td class="card_default_tool_td"><span id="icon_self-healing" class="p-primary-icons p-i-Self-Healing"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span class="c-notification1" data-bind="html: failedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification2" data-bind="html: criticalValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification3" data-bind="html: degradedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification4" data-bind="html: selfHealingValue"></span></td>\
							</tr>\
						</table>\
				    </div>\
					<div id="infoStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<div id="infoStatus_top">\
							<table style="width:100%;margin-top:25px;" border=0 cellspacing="0" cellpadding="3">\
								<tr>\
									<td class="c-title3 card_underline2">TOTAL</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: totalCapacity"></span></td>\
									<td class="card_default_tb card_underline2"><span class="c-title3">TB</span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">FREE</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: freeCapacity"></span></td>\
									<td class="card_default_tb card_underline2"><span class="c-title3">TB</span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">USED</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: usedCapacity"></span></td>\
									<td class="card_default_tb card_underline2"><span class="c-title3">TB</span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">STORAGE CLASSES</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: classes"></span></td>\
									<td class="card_default_tb card_underline2"></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">ENCRYPTION ON THE WIRE</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: encryption"></span></td>\
									<td class="card_default_tb card_underline2"></td>\
								</tr>\
								<tr class="p-fill-DarkSlate">\
									<td class="c-title3 card_underline2">CLIENTS</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: clients"></span></td>\
									<td class="card_default_tb card_underline2"><span id="btn_clients" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
								</tr>\
								<tr class="p-fill-DarkSlate">\
									<td class="c-title3 card_underline2">DATA POLICIES</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: dataPolicies"></span></td>\
									<td class="card_default_tb card_underline2"><span id="btn_datapolicies" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">ADD TO WATCHLIST</td>\
									<td class="card_underline2" colspan="2"><div style="float:right"><input id="switchbutton" type="checkbox"/></div></td>\
								</tr>\
							</table>\
						</div>\
						<div style="margin-top:30px;position:absolute;">\
								<button id="btn_delete" class="alarm-button">DELETE</button>\
								<button id="btn_shutdown" class="s-button" style="width:94px">SHUT DOWN</button>\
								<button id="btn_edit" class="s-button">EDIT</button>\
						</div>\
					</div>'		
	}
});

peaxy.MonitoringStorageClassCard = peaxy.MonitoringCard.extend({
	storageClassCardViewModel:null,
	_options:{
		dataSource:{
			className: 'CLASS A',
			statusName: 'DEFAULT',
			totalCapacity: '000.00',
			freeCapacity: '000.00',
			usedCapacity: '000.00',
			bandwidth: '000.0',
			replicas: '3',
			performance: '2',
			flexible: 'Yes',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			watchlist:false
		}
	},

    init: function(options) {
		jQuery.extend(this._options,options);
		this._options.id = 'card_' + (new Date().getTime()) + parseInt(Math.random()*100 + 1);
		this.id = this._options.id;
		this._options.statusBar = this.Template.statusBar;
		this._options.cardHeader = this.Template.cardHeader;
		this._options.cardToolBar = this.Template.cardToolBar;
		this._options.cardBody = this.Template.cardBody;
		peaxy.MonitoringCard.fn.init.call(this,this._options);
		
		this.storageClassCardViewModel = kendo.observable({
			className: 'CLASS A',
			statusName: 'DEFAULT',
			totalCapacity: '000.00',
			freeCapacity: '000.00',
			usedCapacity: '000.00',
			bandwidth: '000.0',
			replicas: '3',
			performance: '2',
			flexible: 'Yes',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '10',
			degradedValue: '',
			selfHealingValue: '',
			watchlist:false
		});
		kendo.bind(this.cardView, this.storageClassCardViewModel);
		
		var self = this;
		self.cardView.find("#infoStatus").hide();
		self.cardView.find("#switchbutton").kendoPeaxySwitch();
		
		self.cardView.find("#switchbutton").prop("checked",self.storageClassCardViewModel.watchlist);
		self.cardView.find('#switchbutton').click(function(){
			self.storageClassCardViewModel.set("watchlist",self.cardView.find("#switchbutton").prop("checked"));
		});
		
		self.cardView.find('#btn_notifications').click(function(){
			alert("notifications");
		});
		
		self.cardView.find('#btn_bandwidth').click(function(){
			alert("AVG. BANDWIDTH");
		});
		
		var effect = kendo.fx("#"+self._options.id).flipHorizontal(self.cardView.find('#defaultStatus'), self.cardView.find('#infoStatus')).duration(1000),
            reverse = false;

		self.cardView.find('#tool_info').click(function(){
			effect.stop();
            reverse ? effect.reverse() : effect.play();
            reverse = !reverse;
			if(reverse)
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Light-Selected");
			else
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Light");
		});
		
		self.cardView.find('#tool_analysisview').click(function(){
			alert("Analysis View");
		});
		
		self.cardView.find('#tool_history').click(function(){
			alert("History View");
		});
		
		self.cardView.find("#btn_delete").click(function(e){
			peaxy.tooltipDialog($(e.target),{
				content:'Are you sure you want to delete\
					this Storage Class? All data on \
					this Storage Class will be lost if it \
					is not backed up.',
				contentCSS:{'background-color':'#EF354F','color':'#FFF','font-size':'14px'},
				width:'240px',
				actions:[
					{ name:'NO', action:function(){
							
						},
						css:{'width':'100px'}
					},
					{ name:'YES',action:function(){
							self.destroy();
						},
						css:{'width':'100px'}
					}
				]
			});      
        });

		self.cardView.find('#btn_edit').click(function(){
			alert("EDIT");
		});
		self.updataStatus();
    },
	
	updataStatus: function(){
		var f,c,d,s,self = this;
		f = this.storageClassCardViewModel.failedValue;
		c = this.storageClassCardViewModel.criticalValue;
		d = this.storageClassCardViewModel.degradedValue;
		s = this.storageClassCardViewModel.selfHealingValue;
		if(f == null || f.length == 0)
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed-Disabled");
		else
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed");
		if(c == null || c.length == 0)
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical-Disabled");
		else
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical");
		if(d == null || d.length == 0)
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded-Disabled");
		else
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded");
		if(s == null || s.length == 0)
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing-Disabled");
		else
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing");
		self.cardView.find("#imgLevel").attr("class",'p-level-icons p-level-'+self._options.dataSource.performance+'');
	},

	setData: function(viewModel){
		var self = this;
		jQuery.extend(self._options.dataSource,viewModel);
		self.storageClassCardViewModel.set('className', self._options.dataSource.className);
		self.storageClassCardViewModel.set('statusName', self._options.dataSource.statusName);
		self.storageClassCardViewModel.set('totalCapacity', self._options.dataSource.totalCapacity);
		self.storageClassCardViewModel.set('freeCapacity', self._options.dataSource.freeCapacity);
		self.storageClassCardViewModel.set('usedCapacity', self._options.dataSource.usedCapacity);
		self.storageClassCardViewModel.set('replicas', self._options.dataSource.replicas);
		self.storageClassCardViewModel.set('bandwidth', self._options.dataSource.bandwidth);
		self.storageClassCardViewModel.set('performance', self._options.dataSource.performance);
		self.storageClassCardViewModel.set('notificationValue', self._options.dataSource.notificationValue);
		self.storageClassCardViewModel.set('failedValue', self._options.dataSource.failedValue);
		self.storageClassCardViewModel.set('criticalValue', self._options.dataSource.criticalValue);
		self.storageClassCardViewModel.set('degradedValue', self._options.dataSource.degradedValue);
		self.storageClassCardViewModel.set('selfHealingValue', self._options.dataSource.selfHealingValue);
		self.storageClassCardViewModel.set('flexible', self._options.dataSource.flexible);
		self.storageClassCardViewModel.set('watchlist', self._options.dataSource.watchlist);
		self.updataStatus();
	},
	
	draw: function(){
		peaxy.MonitoringCard.fn.draw.call(this);
	},
	
	Template:{
		statusBar:'<div id="storageclass_status" class="cardStatus"></div>',
		cardHeader:'<div id="storageclass_header" style="cursor:pointer;" class="card_header"><span class="card_title_header" data-bind="html: className"></span></div>',
		cardToolBar:'<div id="storageclass_toolbar" class="card_toolbar">\
					<div style="float:left"><span class="card_title_toolbar" style="margin-left:10px;" data-bind="html: statusName"></span></div>\
					<div style="float:right;margin-right:10px;">\
					<span id="tool_history" class="p-primary-icons p-i-Analysis-View-Light" style="cursor: pointer;"></span>\
					<span id="tool_analysisview" class="p-primary-icons p-i-Analysis-View-Light" style="cursor: pointer;"></span>\
					<span id="tool_info" value=0 class="p-primary-icons p-i-Info-Light p-i-Info-Light:hover" style="cursor:pointer;"></span>\
					</div></div>',
		cardBody:   '<div id="defaultStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="card_underline1" colspan="2"><span class="c-title1" data-bind="html: totalCapacity"></span><span class="c-title2">TB</span></td>\
								<td class="card_underline1"></td>\
								<td colspan="4" rowspan="4" style="width:94px"></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2">FREE</td>\
								<td class="class_default_value card_underline2"><span class="c-title4" data-bind="html: freeCapacity"></span></td>\
								<td class="class_default_tb card_underline2"><span class="c-title3">TB</span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2">USED</td>\
								<td class="class_default_value card_underline2"><span class="c-title4" data-bind="html: usedCapacity"></span></td>\
								<td class="class_default_tb card_underline2"><span class="c-title3">TB</span></td>\
							</tr>\
							<tr>\
								<td class="class_default_value card_underline1" colspan="6">&nbsp;</td>\
							</tr>\
							<tr class="p-fill-DarkSlate">\
								<td class="c-title3 card_underline2" colspan="2">AVG. BANDWIDTH</td>\
								<td class="class_default_value card_underline2" colspan="2"><span class="c-title4" data-bind="html: bandwidth"></span></td>\
								<td class="class_default_tb card_underline2"><span class="c-title3">MB/s</span></td>\
								<td class="class_default_tb card_underline2"><span id="btn_bandwidth" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2" colspan="3">REPLICAS</td>\
								<td class="class_default_value card_underline2" colspan="3"><span class="c-title4" data-bind="html: replicas"></span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2" colspan="3">PERFORMANCE</td>\
								<td class="class_default_value card_underline2" colspan="3"><span id="imgLevel" class="p-level-icons p-level-0"></span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2" colspan="3">FLEXIBLE</td>\
								<td class="class_default_value card_underline2" colspan="3"><span class="c-title4" data-bind="html: flexible"></span></td>\
							</tr>\
						</table>\
						<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="card_default_tool_td card_underline1" style="text-align:left;"><span class="p-primary-icons p-i-Notifications"></span><span class="c-title4" data-bind="html: notificationValue"></span></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1" style="text-align:right"><span id="btn_notifications" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span id="icon_failed" class="p-primary-icons p-i-Failed"></span></td>\
								<td class="card_default_tool_td"><span id="icon_critical" class="p-primary-icons p-i-Critical"></span></td>\
								<td class="card_default_tool_td"><span id="icon_degraded" class="p-primary-icons p-i-Degraded"></span></td>\
								<td class="card_default_tool_td"><span id="icon_self-healing" class="p-primary-icons p-i-Self-Healing"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span class="c-notification1" data-bind="html: failedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification2" data-bind="html: criticalValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification3" data-bind="html: degradedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification4" data-bind="html: selfHealingValue"></span></td>\
							</tr>\
						</table>\
				    </div>\
					<div id="infoStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<div id="infoStatus_top">\
							<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
								<tr>\
									<td class="c-title3 card_underline2">TOTAL</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: totalCapacity"></span></td>\
									<td class="card_default_tb card_underline2"><span class="c-title3">TB</span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">FREE</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: freeCapacity"></span></td>\
									<td class="card_default_tb card_underline2"><span class="c-title3">TB</span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">USED</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: usedCapacity"></span></td>\
									<td class="card_default_tb card_underline2"><span class="c-title3">TB</span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">REPLICAS</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: replicas"></span></td>\
									<td class="card_default_tb card_underline2"></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">PERFORMANCE</td>\
									<td class="card_default_value card_underline2" colspan="2"><span id="imgLevel" class="p-level-icons p-level-0"></span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">FLEXIBLE</td>\
									<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: flexible"></span></td>\
									<td class="card_default_tb card_underline2"></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">ADD TO WATCHLIST</td>\
									<td class="card_underline2" colspan="2"><div style="float:right"><input id="switchbutton" type="checkbox"/></div></td>\
								</tr>\
							</table>\
						</div>\
						<div style="margin-top:100px;position:absolute;">\
								<button id="btn_delete" style="width:134px" class="alarm-button">DELETE</button>\
								<button id="btn_edit" style="width:134px;margin-left:15px" class="s-button">EDIT</button>\
						</div>\
					</div>'		
	}
});

peaxy.MonitoringHyperserverNamespaceCard = peaxy.MonitoringCard.extend({
	hyperserverNamespaceCardViewModel:null,
	_options:{
		dataSource:{
			namespace:'NAMESPACE',
			totalCapacity: '%',
			freeCapacity: '00',
			usedCapacity: '00',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			bandwidth:'000.0',
			replicas: '1',
			dataRatio:'1:4',
			watchlist:false
		}
	},
	init: function(options) {
		jQuery.extend(this._options,options);
		this._options.height = 449;
		this._options.id = 'card_' + (new Date().getTime()) + parseInt(Math.random()*100 + 1);
		this.id = this._options.id;
		this._options.statusBar = this.Template.statusBar;
		this._options.cardHeader = this.Template.cardHeader;
		this._options.cardToolBar = this.Template.cardToolBar;
		this._options.cardBody = this.Template.cardBody;
		peaxy.MonitoringCard.fn.init.call(this,this._options);
		
		this.hyperserverNamespaceCardViewModel = kendo.observable({
			namespace:'NAMESPACE',
			totalCapacity: '%',
			freeCapacity: '00',
			usedCapacity: '00',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			bandwidth:'000.0',
			replicas: '1',
			dataRatio:'1:4',
			watchlist:false
		});
		kendo.bind(this.cardView, this.hyperserverNamespaceCardViewModel);
		
		var self = this;
		self.cardView.find("#infoStatus").hide();
		self.cardView.find("#switchbutton").kendoPeaxySwitch();
		
		self.cardView.find("#switchbutton").prop("checked",self.hyperserverNamespaceCardViewModel.watchlist);
		self.cardView.find('#switchbutton').click(function(){
			self.hyperserverNamespaceCardViewModel.set("watchlist",self.cardView.find("#switchbutton").prop("checked"));
		});
		
		self.cardView.find('#btn_bandwidth').click(function(){
			alert("AVG. BANDWIDTH");
		});
		
		var effect = kendo.fx("#"+self._options.id).flipHorizontal(self.cardView.find('#defaultStatus'), self.cardView.find('#infoStatus')).duration(1000),
            reverse = false;

		self.cardView.find('#tool_info').click(function(){
			effect.stop();
            reverse ? effect.reverse() : effect.play();
            reverse = !reverse;
			if(reverse)
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Light-Selected");
			else
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Dark");
		});
		
		self.cardView.find('#tool_analysisview').click(function(){
			alert("Analysis View");
		});
		
		self.cardView.find('#tool_history').click(function(){
			alert("History View");
		});
		
		self.cardView.find('#btn_notifications').click(function(){
			alert("btn_notifications");
		});
	
		self.cardView.find('#btn_shutdown').click(function(e){
			peaxy.tooltipDialog($(e.target),{
				content:'Are you sure you want to shut\
					down this Hyperserver? All data \
					on this Hyperserver will be \
					inaccessible.',
				contentCSS:{'background-color':'#EF354F','color':'#FFF','font-size':'14px'},
				width:'240px',
				actions:[
					{ name:'NO', action:function(){
							
						},
						css:{'width':'100px'}
					},
					{ name:'YES',action:function(){

						},
						css:{'width':'100px'}
					}
				]
			});      
		});
		self.updataStatus();
    },
	
	updataStatus: function(){
		var f,c,d,s,self = this;
		f = this.hyperserverNamespaceCardViewModel.failedValue;
		c = this.hyperserverNamespaceCardViewModel.criticalValue;
		d = this.hyperserverNamespaceCardViewModel.degradedValue;
		s = this.hyperserverNamespaceCardViewModel.selfHealingValue;
		if(f == null || f.length == 0)
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed-Disabled");
		else
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed");
		if(c == null || c.length == 0)
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical-Disabled");
		else
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical");
		if(d == null || d.length == 0)
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded-Disabled");
		else
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded");
		if(s == null || s.length == 0)
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing-Disabled");
		else
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing");
	},

	setData: function(viewModel){
		var self = this;
		jQuery.extend(self._options.dataSource,viewModel);
		self.hyperserverNamespaceCardViewModel.set('namespace', self._options.dataSource.namespace);
		self.hyperserverNamespaceCardViewModel.set('totalCapacity', self._options.dataSource.totalCapacity);
		self.hyperserverNamespaceCardViewModel.set('freeCapacity', self._options.dataSource.freeCapacity);
		self.hyperserverNamespaceCardViewModel.set('usedCapacity', self._options.dataSource.usedCapacity);
		self.hyperserverNamespaceCardViewModel.set('bandwidth', self._options.dataSource.bandwidth);
		self.hyperserverNamespaceCardViewModel.set('notificationValue', self._options.dataSource.notificationValue);
		self.hyperserverNamespaceCardViewModel.set('failedValue', self._options.dataSource.failedValue);
		self.hyperserverNamespaceCardViewModel.set('criticalValue', self._options.dataSource.criticalValue);
		self.hyperserverNamespaceCardViewModel.set('degradedValue', self._options.dataSource.degradedValue);
		self.hyperserverNamespaceCardViewModel.set('selfHealingValue', self._options.dataSource.selfHealingValue);
		self.hyperserverNamespaceCardViewModel.set('replicas', self._options.dataSource.replicas);
		self.hyperserverNamespaceCardViewModel.set('dataRatio', self._options.dataSource.dataRatio);
		self.hyperserverNamespaceCardViewModel.set('watchlist', self._options.dataSource.watchlist);
		self.updataStatus();
	},
	
	draw: function(){
		peaxy.MonitoringCard.fn.draw.call(this);
	},
	
	Template:{
		statusBar:'<div id="hyperservernamespace_status" class="cardStatus"></div>',
		cardHeader:'<div id="hyperservernamespace_header" class="hyperserver_header"><span class="hyperserver_title_header" data-bind="html: namespace"></span></div>',
		cardToolBar:'<div id="hyperservernamespace_toolbar" class="hyperserver_namespace_toolbar">\
					<div style="float:right;margin-right:10px;"><span id="tool_history" class="p-primary-icons p-i-Analysis-View-Dark" style="cursor: pointer;"></span>\
					<span id="tool_analysisview" class="p-primary-icons p-i-Analysis-View-Dark" style="cursor: pointer;"></span>\
					<span id="tool_info" value=0 class="p-primary-icons p-i-Info-Dark" style="cursor:pointer;"></span>\
					</div>\
					</div>',
		cardBody:   '<div id="defaultStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="namespace_underline1" colspan="4" style="width:50%"><span class="n-title1" data-bind="html: totalCapacity"></span></td>\
								<td colspan="4" rowspan="3"></td>\
							</tr>\
							<tr>\
								<td class="n-title3 namespace_underline2" colspan="2">FREE</td>\
								<td class="hyperserver_default_value namespace_underline2"><span class="n-title4" data-bind="html: freeCapacity"></span></td>\
								<td class="hyperserver_default_value namespace_underline2"><span class="n-title3">%</span></td>\
							</tr>\
							<tr>\
								<td class="n-title3 namespace_underline2" colspan="2">USED</td>\
								<td class="hyperserver_default_value namespace_underline2"><span class="n-title4" data-bind="html: usedCapacity"></span></td>\
								<td class="hyperserver_default_value namespace_underline2"><span class="n-title3">%</span></td>\
							</tr>\
							<tr>\
								<td class="hyperserver_default_value namespace_underline1" colspan="8">&nbsp;</td>\
							</tr>\
							<tr class="p-fill-Wheat">\
								<td class="n-title3 namespace_underline2" colspan="4">AVG. BANDWIDTH</td>\
								<td class="hyperserver_default_value namespace_underline2" colspan="2"><span class="n-title4" data-bind="html: bandwidth"></span></td>\
								<td class="hyperserver_default_tb namespace_underline2"><span class="n-title3">MB/s</span></td>\
								<td class="hyperserver_default_tb namespace_underline2"><span id="btn_bandwidth" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="n-title3 namespace_underline2" colspan="4">REPLICAS</td>\
								<td class="hyperserver_default_value namespace_underline2" colspan="2"><span class="n-title4" data-bind="html: replicas"></span></td>\
								<td class="hyperserver_default_tb namespace_underline2"></td>\
								<td class="hyperserver_default_tb namespace_underline2"></td>\
							</tr>\
						</table>\
						<table style="width:100%;margin-top:15px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="card_default_tool_td card_underline1" style="text-align:left;"><span class="p-primary-icons p-i-Notifications"></span><span class="n-title4" data-bind="html: notificationValue"></span></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1" style="text-align:right"><span id="btn_notifications" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span id="icon_failed" class="p-primary-icons p-i-Failed"></span></td>\
								<td class="card_default_tool_td"><span id="icon_critical" class="p-primary-icons p-i-Critical"></span></td>\
								<td class="card_default_tool_td"><span id="icon_degraded" class="p-primary-icons p-i-Degraded"></span></td>\
								<td class="card_default_tool_td"><span id="icon_self-healing" class="p-primary-icons p-i-Self-Healing"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span class="c-notification1" data-bind="html: failedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification2" data-bind="html: criticalValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification3" data-bind="html: degradedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification4" data-bind="html: selfHealingValue"></span></td>\
							</tr>\
						</table>\
				    </div>\
					<div id="infoStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<div id="infoStatus_top">\
							<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
								<tr>\
									<td class="n-title3 namespace_underline2">FREE</td>\
									<td class="hyperserver_default_value namespace_underline2"><span class="n-title4" data-bind="html: freeCapacity"></span></td>\
									<td class="hyperserver_default_tb namespace_underline2"><span class="n-title3">%</span></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">USED</td>\
									<td class="hyperserver_default_value namespace_underline2"><span class="n-title4" data-bind="html: usedCapacity"></span></td>\
									<td class="hyperserver_default_tb namespace_underline2"><span class="n-title3">%</span></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">REPLICAS</td>\
									<td class="hyperserver_default_value namespace_underline2"><span class="n-title4" data-bind="html: replicas"></span></td>\
									<td class="hyperserver_default_tb namespace_underline2"></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">NAME : DATA RATIO</td>\
									<td class="hyperserver_default_value namespace_underline2"><span class="n-title4" data-bind="html: dataRatio"></span></td>\
									<td class="hyperserver_default_tb namespace_underline2"></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">ADD TO WATCHLIST</td>\
									<td class="namespace_underline2" colspan="2"><div style="float:right"><input id="switchbutton" type="checkbox"/></div></td>\
								</tr>\
							</table>\
						</div>\
						<div style="margin-top:120px;position:absolute;">\
								<button id="btn_shutdown" style="width:288px" class="s-button">SHUT DOWN</button>\
						</div>\
					</div>'
		
	}
});

peaxy.MonitoringHyperserverCard = peaxy.MonitoringCard.extend({
	hyperserverCardViewModel:null,
	_options:{
		dataSource:{
			className: 'CLASS A',
			statusName: 'DEFAULT',
			totalCapacity: '000.00',
			freeCapacity: '000.00',
			usedCapacity: '000.00',
			bandwidth: '000.0',
			replicas: '3',
			performance: '2',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			watchlist:false
		}
	},

    init: function(options) {
		jQuery.extend(this._options,options);
		this._options.height = 449;
		this._options.id = 'card_' + (new Date().getTime()) + parseInt(Math.random()*100 + 1);
		this.id = this._options.id;
		this._options.statusBar = this.Template.statusBar;
		this._options.cardHeader = this.Template.cardHeader;
		this._options.cardToolBar = this.Template.cardToolBar;
		this._options.cardBody = this.Template.cardBody;
		peaxy.MonitoringCard.fn.init.call(this,this._options);
		
		this.hyperserverCardViewModel = kendo.observable({
			className: 'CLASS A',
			statusName: 'DEFAULT',
			totalCapacity: '000.00',
			freeCapacity: '000.00',
			usedCapacity: '000.00',
			bandwidth: '000.0',
			replicas: '3',
			performance: '2',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '10',
			degradedValue: '',
			selfHealingValue: '',
			watchlist:false
		});
		kendo.bind(this.cardView, this.hyperserverCardViewModel);
		
		var self = this;
		self.cardView.find("#infoStatus").hide();
		self.cardView.find("#switchbutton").kendoPeaxySwitch();
		
		self.cardView.find("#switchbutton").prop("checked",self.hyperserverCardViewModel.watchlist);
		self.cardView.find('#switchbutton').click(function(){
			self.hyperserverCardViewModel.set("watchlist",self.cardView.find("#switchbutton").prop("checked"));
		});
		
		self.cardView.find('#btn_notifications').click(function(){
			alert("notifications");
		});
		
		self.cardView.find('#btn_bandwidth').click(function(){
			alert("AVG. BANDWIDTH");
		});
		
		var effect = kendo.fx("#"+self._options.id).flipHorizontal(self.cardView.find('#defaultStatus'), self.cardView.find('#infoStatus')).duration(1000),
            reverse = false;

		self.cardView.find('#tool_info').click(function(){
			effect.stop();
            reverse ? effect.reverse() : effect.play();
            reverse = !reverse;
			if(reverse)
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Light-Selected");
			else
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Light");
		});
		
		self.cardView.find('#tool_analysisview').click(function(){
			alert("Analysis View");
		});
		
		self.cardView.find('#tool_history').click(function(){
			alert("History View");
		});
		
		self.cardView.find('#btn_shutdown').click(function(e){
			peaxy.tooltipDialog($(e.target),{
				content:'Are you sure you want to shut\
					down this Hyperserver? All data\
					on this Hyperserver will be\
					inaccessible.',
				contentCSS:{'background-color':'#EF354F','color':'#FFF','font-size':'14px'},
				width:'240px',
				actions:[
					{ name:'NO', action:function(){
							
						},
						css:{'width':'100px'}
					},
					{ name:'YES',action:function(){

						},
						css:{'width':'100px'}
					}
				]
			});
		});
		self.updataStatus();
    },
	
	//No Issues, Degraded, Critical, and Failed
	upadataStatusColors: function(status){
		var self = this;
		if(status == "NoIssues")
			self.cardView.find("#hyperserver_header").attr("class","hyperserver_header_noissues");
		else if(status == "Degraded")
			self.cardView.find("#hyperserver_header").attr("class","hyperserver_header_degraded");
		else if(status == "Critical")
			self.cardView.find("#hyperserver_header").attr("class","hyperserver_header_critical");
		else if (status == "Failed")
			self.cardView.find("#hyperserver_header").attr("class","hyperserver_header_failed");
	},
	
	updataStatus: function(){
		var f,c,d,s,self = this;
		f = this.hyperserverCardViewModel.failedValue;
		c = this.hyperserverCardViewModel.criticalValue;
		d = this.hyperserverCardViewModel.degradedValue;
		s = this.hyperserverCardViewModel.selfHealingValue;
		if(f == null || f.length == 0)
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed-Disabled");
		else
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed");
		if(c == null || c.length == 0)
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical-Disabled");
		else
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical");
		if(d == null || d.length == 0)
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded-Disabled");
		else
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded");
		if(s == null || s.length == 0)
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing-Disabled");
		else
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing");
		self.cardView.find("#imgLevel").attr("class",'p-level-icons p-level-'+self._options.dataSource.performance+'');
	},

	setData: function(viewModel){
		var self = this;
		jQuery.extend(self._options.dataSource,viewModel);
		self.hyperserverCardViewModel.set('className', self._options.dataSource.className);
		self.hyperserverCardViewModel.set('statusName', self._options.dataSource.statusName);
		self.hyperserverCardViewModel.set('totalCapacity', self._options.dataSource.totalCapacity);
		self.hyperserverCardViewModel.set('freeCapacity', self._options.dataSource.freeCapacity);
		self.hyperserverCardViewModel.set('usedCapacity', self._options.dataSource.usedCapacity);
		self.hyperserverCardViewModel.set('replicas', self._options.dataSource.replicas);
		self.hyperserverCardViewModel.set('bandwidth', self._options.dataSource.bandwidth);
		self.hyperserverCardViewModel.set('performance', self._options.dataSource.performance);
		self.hyperserverCardViewModel.set('notificationValue', self._options.dataSource.notificationValue);
		self.hyperserverCardViewModel.set('failedValue', self._options.dataSource.failedValue);
		self.hyperserverCardViewModel.set('criticalValue', self._options.dataSource.criticalValue);
		self.hyperserverCardViewModel.set('degradedValue', self._options.dataSource.degradedValue);
		self.hyperserverCardViewModel.set('selfHealingValue', self._options.dataSource.selfHealingValue);
		self.hyperserverCardViewModel.set('watchlist', self._options.dataSource.watchlist);
		self.updataStatus();
	},
	
	draw: function(){
		peaxy.MonitoringCard.fn.draw.call(this);
	},
	
	Template:{
		statusBar:'',
		cardHeader:'<div id="hyperserver_header" style="cursor:pointer;" class="hyperserver_header_noissues"><span class="hyperserver_title_header" data-bind="html: className"></span></div>',
		cardToolBar:'<div id="storageclass_toolbar" class="card_toolbar">\
					<div style="float:left"><span class="card_title_toolbar" style="margin-left:10px;" data-bind="html: statusName"></span></div>\
					<div style="float:right;margin-right:10px;">\
					<span id="tool_history" class="p-primary-icons p-i-Analysis-View-Light" style="cursor: pointer;"></span>\
					<span id="tool_analysisview" class="p-primary-icons p-i-Analysis-View-Light" style="cursor: pointer;"></span>\
					<span id="tool_info" value=0 class="p-primary-icons p-i-Info-Light p-i-Info-Light:hover" style="cursor:pointer;"></span>\
					</div></div>',
		cardBody:   '<div id="defaultStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="card_underline1" colspan="2"><span class="c-title1" data-bind="html: totalCapacity"></span><span class="c-title2">TB</span></td>\
								<td class="card_underline1"></td>\
								<td colspan="4" rowspan="4" style="width:94px"></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2">FREE</td>\
								<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: freeCapacity"></span></td>\
								<td class="class_default_tb card_underline2"><span class="c-title3">TB</span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2">USED</td>\
								<td class="card_default_value card_underline2"><span class="c-title4" data-bind="html: usedCapacity"></span></td>\
								<td class="class_default_tb card_underline2"><span class="c-title3">TB</span></td>\
							</tr>\
							<tr>\
								<td class="card_default_value card_underline1" colspan="6">&nbsp;</td>\
							</tr>\
							<tr class="p-fill-DarkSlate">\
								<td class="c-title3 card_underline2" colspan="2">AVG. BANDWIDTH</td>\
								<td class="card_default_value card_underline2" colspan="2"><span class="c-title4" data-bind="html: bandwidth"></span></td>\
								<td class="class_default_tb card_underline2"><span class="c-title3">MB/s</span></td>\
								<td class="class_default_tb card_underline2"><span id="btn_bandwidth" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2" colspan="3">REPLICAS</td>\
								<td class="card_default_value card_underline2" colspan="3"><span class="c-title4" data-bind="html: replicas"></span></td>\
							</tr>\
							<tr>\
								<td class="c-title3 card_underline2" colspan="3">PERFORMANCE</td>\
								<td class="card_default_value card_underline2" colspan="3"><span id="imgLevel" class="p-level-icons p-level-0"></span></td>\
							</tr>\
						</table>\
						<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="card_default_tool_td card_underline1" style="text-align:left;"><span class="p-primary-icons p-i-Notifications"></span><span class="c-title4" data-bind="html: notificationValue"></span></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1" style="text-align:right"><span id="btn_notifications" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span id="icon_failed" class="p-primary-icons p-i-Failed"></span></td>\
								<td class="card_default_tool_td"><span id="icon_critical" class="p-primary-icons p-i-Critical"></span></td>\
								<td class="card_default_tool_td"><span id="icon_degraded" class="p-primary-icons p-i-Degraded"></span></td>\
								<td class="card_default_tool_td"><span id="icon_self-healing" class="p-primary-icons p-i-Self-Healing"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span class="c-notification1" data-bind="html: failedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification2" data-bind="html: criticalValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification3" data-bind="html: degradedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification4" data-bind="html: selfHealingValue"></span></td>\
							</tr>\
						</table>\
				    </div>\
					<div id="infoStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<div id="infoStatus_top">\
							<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
								<tr>\
									<td class="c-title3 card_underline2">TOTAL</td>\
									<td class="hyperserver_default_value card_underline2"><span class="c-title4" data-bind="html: totalCapacity"></span></td>\
									<td class="hyperserver_default_tb card_underline2"><span class="c-title3">TB</span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">FREE</td>\
									<td class="hyperserver_default_value card_underline2"><span class="c-title4" data-bind="html: freeCapacity"></span></td>\
									<td class="hyperserver_default_tb card_underline2"><span class="c-title3">TB</span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">USED</td>\
									<td class="hyperserver_default_value card_underline2"><span class="c-title4" data-bind="html: usedCapacity"></span></td>\
									<td class="hyperserver_default_tb card_underline2"><span class="c-title3">TB</span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">REPLICAS</td>\
									<td class="hyperserver_default_value card_underline2"><span class="c-title4" data-bind="html: replicas"></span></td>\
									<td class="hyperserver_default_tb card_underline2"></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">PERFORMANCE</td>\
									<td class="hyperserver_default_value card_underline2" colspan="2"><span id="imgLevel" class="p-level-icons p-level-0"></span></td>\
								</tr>\
								<tr>\
									<td class="c-title3 card_underline2">ADD TO WATCHLIST</td>\
									<td class="card_underline2" colspan="2"><div style="float:right"><input id="switchbutton" type="checkbox"/></div></td>\
								</tr>\
							</table>\
						</div>\
						<div style="margin-top:100px;position:absolute;">\
								<button id="btn_shutdown" style="width:288px" class="s-button">SHUT DOWN</button>\
						</div>\
					</div>'		
	}
});

peaxy.MonitoringPVMNamespaceCard = peaxy.MonitoringCard.extend({
	hyperserverNamespaceCardViewModel:null,
	_options:{
		dataSource:{
			namespace:'NAMESPACE-0000-0',
			designation:'PRIMARY',
			totalCapacity: '%',
			freeCapacity: '00',
			usedCapacity: '00',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			bandwidth:'000.0',
			dataRatio:'1:4',
			watchlist:false,
			beacon:true
		}
	},
	init: function(options) {
		jQuery.extend(this._options,options);
		this._options.height = 418;
		this._options.id = 'card_' + (new Date().getTime()) + parseInt(Math.random()*100 + 1);
		this.id = this._options.id;
		this._options.statusBar = this.Template.statusBar;
		this._options.cardHeader = this.Template.cardHeader;
		this._options.cardToolBar = this.Template.cardToolBar;
		this._options.cardBody = this.Template.cardBody;
		this._options.cardType = "pvm";
		peaxy.MonitoringCard.fn.init.call(this,this._options);
		
		
		this.hyperserverNamespaceCardViewModel = kendo.observable({
			namespace:'NAMESPACE-0000-0',
			designation:'PRIMARY',
			totalCapacity: '%',
			freeCapacity: '00',
			usedCapacity: '00',
			notificationValue: '000',
			failedValue: '',
			criticalValue: '',
			degradedValue: '',
			selfHealingValue: '',
			bandwidth:'000.0',
			dataRatio:'1:4',
			watchlist:false,
			beacon:true
		});
		kendo.bind(this.cardView, this.hyperserverNamespaceCardViewModel);
		
		var self = this;
		self.cardView.find("#infoStatus").hide();
		self.cardView.find("#watchlist").kendoPeaxySwitch();
		self.cardView.find("#beacon").kendoPeaxySwitch();
		self.cardView.find("#watchlist").prop("checked",self.hyperserverNamespaceCardViewModel.watchlist);
		self.cardView.find("#beacon").prop("checked",self.hyperserverNamespaceCardViewModel.beacon);
		self.cardView.find('#watchlist').click(function(){
			self.hyperserverNamespaceCardViewModel.set("watchlist",self.cardView.find("#watchlist").prop("checked"));
		});
		self.cardView.find('#beacon').click(function(){
			self.hyperserverNamespaceCardViewModel.set("beacon",self.cardView.find("#beacon").prop("checked"));
		});
		self.cardView.find('#btn_bandwidth').click(function(){
			alert("AVG. BANDWIDTH");
		});
		
		var effect = kendo.fx("#"+self._options.id).flipHorizontal(self.cardView.find('#defaultStatus'), self.cardView.find('#infoStatus')).duration(1000),
            reverse = false;

		self.cardView.find('#tool_info').click(function(){
			effect.stop();
            reverse ? effect.reverse() : effect.play();
            reverse = !reverse;
			if(reverse)
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Light-Selected");
			else
				self.cardView.find("#tool_info").attr("class","p-primary-icons p-i-Info-Dark");
		});
		
		self.cardView.find('#tool_physicalserver').click(function(){
			alert("physical server");
		});
		
		self.cardView.find('#tool_analysisview').click(function(){
			alert("Analysis View");
		});
		
		self.cardView.find('#tool_history').click(function(){
			alert("History View");
		});
		
		self.cardView.find('#btn_notifications').click(function(){
			alert("btn_notifications");
		});
		
		self.cardView.find('#btn_restart').click(function(e){
			peaxy.tooltipDialog($(e.target),{
				content:'Are you sure you want to restart\
						this PVM? This PVM will be\
						inaccessible while it is being\
						restarted.',
				width:'240px',
				actions:[
					{ name:'NO', action:function(){
							
						},
						css:{'width':'100px'}
					},
					{ name:'YES',action:function(){

						},
						css:{'width':'100px'}
					}
				]
			});      
		});
	
		self.cardView.find('#btn_shutdown').click(function(e){
			peaxy.tooltipDialog($(e.target),{
				content:'Are you sure you want to shut\
					down this PVM? All data on this \
					PVM will be inaccessible.',
				contentCSS:{'background-color':'#EF354F','color':'#FFF','font-size':'14px'},
				width:'240px',
				actions:[
					{ name:'NO', action:function(){
							
						},
						css:{'width':'100px'}
					},
					{ name:'YES',action:function(){

						},
						css:{'width':'100px'}
					}
				]
			});      
		});
		self.updataStatus();
    },
	
	updataStatus: function(){
		var f,c,d,s,self = this;
		f = this.hyperserverNamespaceCardViewModel.failedValue;
		c = this.hyperserverNamespaceCardViewModel.criticalValue;
		d = this.hyperserverNamespaceCardViewModel.degradedValue;
		s = this.hyperserverNamespaceCardViewModel.selfHealingValue;
		if(f == null || f.length == 0)
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed-Disabled");
		else
			self.cardView.find("#icon_failed").attr("class","p-primary-icons p-i-Failed");
		if(c == null || c.length == 0)
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical-Disabled");
		else
			self.cardView.find("#icon_critical").attr("class","p-primary-icons p-i-Critical");
		if(d == null || d.length == 0)
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded-Disabled");
		else
			self.cardView.find("#icon_degraded").attr("class","p-primary-icons p-i-Degraded");
		if(s == null || s.length == 0)
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing-Disabled");
		else
			self.cardView.find("#icon_self-healing").attr("class","p-primary-icons p-i-Self-Healing");
	},

	setData: function(viewModel){
		var self = this;
		jQuery.extend(self._options.dataSource,viewModel);
		self.hyperserverNamespaceCardViewModel.set('namespace', self._options.dataSource.namespace);
		self.hyperserverNamespaceCardViewModel.set('designation', self._options.dataSource.designation);
		self.hyperserverNamespaceCardViewModel.set('totalCapacity', self._options.dataSource.totalCapacity);
		self.hyperserverNamespaceCardViewModel.set('freeCapacity', self._options.dataSource.freeCapacity);
		self.hyperserverNamespaceCardViewModel.set('usedCapacity', self._options.dataSource.usedCapacity);
		self.hyperserverNamespaceCardViewModel.set('bandwidth', self._options.dataSource.bandwidth);
		self.hyperserverNamespaceCardViewModel.set('notificationValue', self._options.dataSource.notificationValue);
		self.hyperserverNamespaceCardViewModel.set('failedValue', self._options.dataSource.failedValue);
		self.hyperserverNamespaceCardViewModel.set('criticalValue', self._options.dataSource.criticalValue);
		self.hyperserverNamespaceCardViewModel.set('degradedValue', self._options.dataSource.degradedValue);
		self.hyperserverNamespaceCardViewModel.set('selfHealingValue', self._options.dataSource.selfHealingValue);
		self.hyperserverNamespaceCardViewModel.set('dataRatio', self._options.dataSource.dataRatio);
		self.hyperserverNamespaceCardViewModel.set('watchlist', self._options.dataSource.watchlist);
		self.hyperserverNamespaceCardViewModel.set('beacon', self._options.dataSource.beacon);
		self.updataStatus();
	},
	
	draw: function(){
		peaxy.MonitoringCard.fn.draw.call(this);
	},
	
	Template:{
		statusBar:'',
		cardHeader:'<div id="pvmnamespace_header" class="hyperserver_header"><span class="hyperserver_title_header" data-bind="html: namespace"></span></div>',
		cardToolBar:'<div id="hyperservernamespace_toolbar" class="hyperserver_namespace_toolbar">\
					<div style="float:left"><span class="card_title_toolbar" style="margin-left:10px;" data-bind="html: designation"></span></div>\
					<div style="float:right;margin-right:10px;">\
					<span id="tool_physicalserver" class="p-primary-icons p-i-Server-View-Dark" style="cursor: pointer;"></span>\
					<span id="tool_history" class="p-primary-icons p-i-Analysis-View-Dark" style="cursor: pointer;"></span>\
					<span id="tool_analysisview" class="p-primary-icons p-i-Analysis-View-Dark" style="cursor: pointer;"></span>\
					<span id="tool_info" value=0 class="p-primary-icons p-i-Info-Dark" style="cursor:pointer;"></span>\
					</div>\
					</div>',
		cardBody:   '<div id="defaultStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="namespace_underline1" colspan="4" style="width:50%"><span class="n-title1" data-bind="html: totalCapacity"></span></td>\
								<td colspan="4" rowspan="3"></td>\
							</tr>\
							<tr>\
								<td class="n-title3 namespace_underline2" colspan="2">FREE</td>\
								<td class="card_default_value namespace_underline2"><span class="n-title4" data-bind="html: freeCapacity"></span></td>\
								<td class="card_default_value namespace_underline2"><span class="n-title3">%</span></td>\
							</tr>\
							<tr>\
								<td class="n-title3 namespace_underline2" colspan="2">USED</td>\
								<td class="card_default_value namespace_underline2"><span class="n-title4" data-bind="html: usedCapacity"></span></td>\
								<td class="card_default_value namespace_underline2"><span class="n-title3">%</span></td>\
							</tr>\
							<tr>\
								<td class="card_default_value namespace_underline1" colspan="8">&nbsp;</td>\
							</tr>\
							<tr class="p-fill-Wheat">\
								<td class="n-title3 namespace_underline2" colspan="4">AVG. BANDWIDTH</td>\
								<td class="card_default_value namespace_underline2" colspan="2"><span class="n-title4" data-bind="html: bandwidth"></span></td>\
								<td class="class_default_tb namespace_underline2"><span class="n-title3">MB/s</span></td>\
								<td class="class_default_tb namespace_underline2"><span id="btn_bandwidth" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
						</table>\
						<table style="width:100%;margin-top:40px;" border=0 cellspacing="0" cellpadding="3">\
							<tr>\
								<td class="card_default_tool_td card_underline1" style="text-align:left;"><span class="p-primary-icons p-i-Notifications"></span><span class="n-title4" data-bind="html: notificationValue"></span></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1"></td>\
								<td class="card_default_tool_td card_underline1" style="text-align:right"><span id="btn_notifications" class="p-secondary-icons p-si-Dark-Arrow-Right" style="cursor: pointer;"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span id="icon_failed" class="p-primary-icons p-i-Failed"></span></td>\
								<td class="card_default_tool_td"><span id="icon_critical" class="p-primary-icons p-i-Critical"></span></td>\
								<td class="card_default_tool_td"><span id="icon_degraded" class="p-primary-icons p-i-Degraded"></span></td>\
								<td class="card_default_tool_td"><span id="icon_self-healing" class="p-primary-icons p-i-Self-Healing"></span></td>\
							</tr>\
							<tr>\
								<td class="card_default_tool_td"><span class="c-notification1" data-bind="html: failedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification2" data-bind="html: criticalValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification3" data-bind="html: degradedValue"></span></td>\
								<td class="card_default_tool_td"><span class="c-notification4" data-bind="html: selfHealingValue"></span></td>\
							</tr>\
						</table>\
				    </div>\
					<div id="infoStatus" style="width:288px;padding-left:10px;padding-right:10px;padding-top:0px;position:absolute;margin:0 auto;">\
						<div id="infoStatus_top">\
							<table style="width:100%;margin-top:10px;" border=0 cellspacing="0" cellpadding="3">\
								<tr>\
									<td class="n-title3 namespace_underline2">FREE</td>\
									<td class="hyperserver_default_value namespace_underline2"><span class="n-title4" data-bind="html: freeCapacity"></span></td>\
									<td class="hyperserver_default_tb namespace_underline2"><span class="n-title3">%</span></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">USED</td>\
									<td class="hyperserver_default_value namespace_underline2"><span class="n-title4" data-bind="html: usedCapacity"></span></td>\
									<td class="hyperserver_default_tb namespace_underline2"><span class="n-title3">%</span></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">NAME : DATA RATIO</td>\
									<td class="hyperserver_default_value namespace_underline2"><span class="n-title4" data-bind="html: dataRatio"></span></td>\
									<td class="hyperserver_default_tb namespace_underline2"></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">ADD TO WATCHLIST</td>\
									<td class="namespace_underline2" colspan="2"><div style="float:right"><input id="watchlist" type="checkbox"/></div></td>\
								</tr>\
								<tr>\
									<td class="n-title3 namespace_underline2">BEACON</td>\
									<td class="namespace_underline2" colspan="2"><div style="float:right"><input id="beacon" type="checkbox"/></div></td>\
								</tr>\
							</table>\
						</div>\
						<div style="margin-top:100px;position:absolute;">\
								<button id="btn_shutdown" style="width:134px" class="s-button">SHUT DOWN</button>\
								<button id="btn_restart" style="width:134px;margin-left:15px" class="s-button">RESTART</button>\
						</div>\
					</div>'
		
	}
});
