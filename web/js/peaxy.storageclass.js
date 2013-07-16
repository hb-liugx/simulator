var peaxy = peaxy || {};
peaxy.StorageClass = kendo.Class.extend({
	controlPanel:'',
	parentDiv:'',
	mode:'',
	performanceLevel:'',
	storageClassViewModel:'',
	numerictextbox:'',
	basedOnList:'',
	levelList:'',
	defaultData:{
		id:0,
		performanceLevel:'0',
		className: '',
		replicas:'1',
		usableSpace:'0',
		freeSpace:0,
		totalSpace:'0',
		usedSpace:'0',
		flexible:'NO',
		defaultValue: '',
	},
	_options:{
		width:356,
		height:628,
		parent:document.body,
		scList:[],
		maxCapacity:[],
		dataSource:{
			id:0,
			performanceLevel:'0',//performance
			className: '',//className
			replicas:'1',//replicas
			usableSpace:'0',//
			freeSpace:0,
			totalSpace:'0',//raw storage space
			usedSpace:'0',//used
			flexible:'NO',//flexible
			maxValue:60,  //max
			defaultValue: '',//statesName
			maxCapacity:[]
		},
		onChange:function(){},
		onConfig:function(){},
		onDefault:function(){}
	},

    init: function(options) {
		var self = this;
		jQuery.extend(self._options,options);
		this.validator = new peaxy.Check();
		self.parentDiv = options.parent;//position: relative;
		self.controlPanel = $('<div id="controlPanel" style="overflow:hidden;width:'+ self._options.width +'px; height:'+ self._options.height +'px"></div>');
    },
	
	draw: function(){
		this.parentDiv.append(this.controlPanel);
	},
	
	_save: function(){
		this.storageClassViewModel.set('className',this.controlPanel.find("#txtClassName").prop('value'));
		this._options.onConfig(this.storageClassViewModel);
	},
	
	_loadBasedOn: function(){
		var self = this; 
		var data = [];
		data.push({text: "New Storage Class", value: -1 });
		for(var i = 0; i < self._options.scList.length; i++ ){
			data.push({text: self._options.scList[i].className,value:i});
		}
		function onChange() {
			if(self.basedOnList.value() == "-1"){
				self._setData(self.defaultData);
				return;
			}
			for(var i = 0; i < self._options.scList.length; i++ ){
				if(self._options.scList[i].className == self.basedOnList.text()){
					self._setData(self._options.scList[i]);
				}
			}
		};

		function onSelect(e) {
			var dataItem = this.dataItem(e.item.index());
		};
		// create DropDownList from input HTML element
		self.controlPanel.find("#basedOn").kendoDropDownList({
			dataTextField: "text",
			dataValueField: "value",
			dataSource:data,
			index: 0,
			change: onChange,
			select: onSelect
		
		});
		self.basedOnList = self.controlPanel.find("#basedOn").data("kendoDropDownList");
	},
	 
	_loadPerformanceLevel: function(){
		var self = this; 
		$("#performanceLevel-list").remove();
		var data = [
			{ text: "Select Performance Level", value: "0" },
			{ text: "Seek: High - Throughput: Low", value: "1" },
			{ text: "Seek: High - Throughput: Medium", value: "2" },
			{ text: "Seek: High - Throughput: High", value: "3" },
			{ text: "Seek: High - Throughput: Very High", value: "4" },
			{ text: "Seek: Medium - Throughput: Low", value: "5" },
			{ text: "Seek: Medium - Throughput: Medium", value: "6" },
			{ text: "Seek: Medium - Throughput: High", value: "7" },
			{ text: "Seek: Medium - Throughput: Very High", value: "8" },
			{ text: "Seek: Low - Throughput: Low", value: "9" },
			{ text: "Seek: Low - Throughput: Medium", value: "10" },
			{ text: "Seek: Low - Throughput: High", value: "11" },
			{ text: "Seek: Low - Throughput: Very High", value: "12" },
			{ text: "Seek: None - Throughput: Low", value: "13" },
			{ text: "Seek: None - Throughput: Medium", value: "14" },
			{ text: "Seek: None - Throughput: High", value: "15" },
			{ text: "Seek: None - Throughput: Very High", value: "16" }
		];
		function onChange() {
			self.controlPanel.find("#imgLevel").attr("class",'p-level-icons p-level-'+self.levelList.value()+'');
			self.storageClassViewModel.set("performanceLevel",self.levelList.value());
			self.storageClassViewModel.set("maxValue",self._options.maxCapacity[self.levelList.value()]);
			self.numerictextbox.max(self._options.maxCapacity[self.levelList.value()]);
			var m = self.storageClassViewModel.get('maxValue');
			var c = self.storageClassViewModel.get('usableSpace');
			if(c > m){
				self.storageClassViewModel.set("usableSpace",m);
			}
			self.calculateRawStorageSpace();
			self._onChange();
		};
		
		function onOpen(){
			$("#performanceLevel_listbox").parent().width(300);
		};

		function onSelect(e) {
			var dataItem = this.dataItem(e.item.index());
		};
		// create DropDownList from input HTML element
		self.controlPanel.find("#performanceLevel").kendoDropDownList({
			dataTextField: "text",
			dataValueField: "value",
			dataSource:data,
			index: 0,
			open: onOpen,
			change: onChange,
			select: onSelect,
			template: '<span>${ data.text }</span><span style="float:right" class="p-level-icons p-level-${ data.value }"></span>'
			    
		});
		self.levelList = self.controlPanel.find("#performanceLevel").data("kendoDropDownList");
		self.controlPanel.find("#imgLevel").attr("class",'p-level-icons p-level-'+self._options.dataSource.performanceLevel+'')
		self.levelList.value(self._options.dataSource.performanceLevel);
		self.storageClassViewModel.set("maxValue",self._options.maxCapacity[self.levelList.value()]);
		self.numerictextbox.max(self._options.maxCapacity[self.levelList.value()]);
	},
	
	_onChange: function(){
		this.controlPanel.find("#s-create-button").prop('disabled',false);
		this.storageClassViewModel.set('className',this.controlPanel.find("#txtClassName").prop('value'));
		this._options.onChange(this.storageClassViewModel);
	},
	
	_dataBind: function(){
		var self = this;
		self.storageClassViewModel = kendo.observable({
			id:0,
			performanceLevel:'0',//performance
			className: '',//className
			replicas:'1',//replicas
			usableSpace:'0',//
			freeSpace:0,
			totalSpace:'0',//raw storage space
			usedSpace:'0',//used
			flexible:'NO',//flexible
			maxValue:60,  //max
			defaultValue: '',//statesName
			maxCapacity:[]
		});
		kendo.bind(self.controlPanel, self.storageClassViewModel);
	},
	
	calculateRawStorageSpace: function(){
		var n = this.storageClassViewModel.replicas;
		this.storageClassViewModel.set("usableSpace",this.controlPanel.find("#usableSpace").prop("value")) ;
		var u = this.storageClassViewModel.usableSpace;
		var m = this.storageClassViewModel.maxValue;
		if(u > m)
			u = m;
		var r = (parseInt(n)) * u;
		this.storageClassViewModel.set('totalSpace',r);
	},
	
	_disabledAll: function(){
		var self = this;
		self.numerictextbox.enable(false);
		
		//self.levelList.enable(false);
		self.controlPanel.find('input,select,button').prop({'disabled':true});
	},
	
	_eventBind: function(){
		var self = this;
		self.controlPanel.find("#txtClassName").change(function(){
			self.storageClassViewModel.set('className', self.storageClassViewModel.className);
			self._onChange();
        });
		self.controlPanel.find("#usableSpace").change(function(){
			self.storageClassViewModel.set('freeSpace',self.controlPanel.find("#usableSpace").prop("value"));
			self.calculateRawStorageSpace();
			self._onChange();
        });
		self.controlPanel.find(":radio[name='allocation']").change(function(e){
			self._onChange();
		});
		self.controlPanel.find(":radio[name='factor']").change(function(e){
			self.calculateRawStorageSpace();
			self._onChange();
		});
		var txtClassName = self.controlPanel.find('#txtClassName');
		self.validator.register(txtClassName,self.controlPanel.find('#classNameErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please input a class name.";
			}
			for(var i = 0; i < self._options.scList.length; i++ ){
				if(self._options.scList[i].className.trim() == self.controlPanel.find('#txtClassName').prop("value").trim()){
					return "The class name already exists.";
				}
			}
			return true;
		});
		self.controlPanel.find(":radio[name='default']").change(function(e){
			if(self.oData.defaultValue == "DEFAULT" && $(e.target).prop("value") == ''){
				var html = $('<div style="padding:10px;color:#FFFFFF;"><p>You are required to have a default<br/> Storage Class?</p>\
								<p>Please Select one below.</p><br/><br/>\
								<input style="width:246px" id="classList"/>\
						</div>');
					peaxy.tooltipDialog($(e.target),{
						content:html,
						actions:[
							{ name:'CANCEL', action:function(e){
									self.storageClassViewModel.set('defaultValue', "DEFAULT");
								}
							},
							{ name:'SET AS DEFAULT', action:function(){

									if(self.storageClassViewModel.get('className') != dropdownlist.text()){
										self.storageClassViewModel.set('defaultValue','');
										self._onChange();
										self._options.onDefault(dropdownlist.text());
									} else
										self.storageClassViewModel.set('defaultValue', "DEFAULT");
								}
							}
						]
					});
					var data = [];
					for(var i = 0; i < self._options.scList.length; i++ ){
						//if(self._options.scList[i].className != self._options.dataSource.className)
							data.push({text: self._options.scList[i].className,value:i});
					}
					// create DropDownList from input HTML element
					html.find("#classList").kendoDropDownList({
						dataTextField: "text",
						dataValueField: "value",
						dataSource:data,
						index: 0,	
					});
					var dropdownlist = html.find("#classList").data("kendoDropDownList");
			} else 
				self._onChange();
        });
		
		self.controlPanel.find("#s-save-button").click(function(e){
			if(self.storageClassViewModel.usedSpace > 0){
				peaxy.tooltipDialog($(e.target),{
					content:'Would you like Peaxy to create a data migration\
						workflow for you?<br/><br/>\
						Lowering the storage space of an existing\
						storage class requires the migration of data\
						before the storage space can be used by the\
						rest of the Hyperfiler. If you choose to create\
						this workflow you will find it in your workflows\
						panel. You will need to manually initiate the\
						workflow.',
					actions:[
						{ name:'CANCEL', action:function(){
							
							}
						},
						{ name:'CREATE WORKFLOW',action:function(){
							self._save();
							}
						}
					]
				});      
			} else {
				if(self.controlPanel.find('#classNameErrMsg').text() == ""){
					self.levelList.enable(false);
					self._disabledAll();
					self._save();
					self._setData(self.oData);
				}
			}
        });
		self.controlPanel.find("#s-cancel-button").click(function(){
			self.levelList.enable(false);
			self._disabledAll();
			self._options.onConfig(self.oData);
			self._setData(self.oData);
        });
		self.controlPanel.find("#s-create-button").click(function(){
			if(self.validator.validator()){
				self._save();
				self.basedOnList.enable(false);
				self.levelList.enable(false);
				self._disabledAll();
				//self._setData(self.oData);
			}
        });
	},
	
	_setData: function(viewModel){
		var self = this;
		jQuery.extend(self._options.dataSource, viewModel || {});
		//self.performanceLevel = self._options.dataSource.performanceLevel;
		self._loadBasedOn();
		self._dataBind();
		self._eventBind();
		self._loadPerformanceLevel();
		self.storageClassViewModel.set('id', self._options.dataSource.id);
		self.storageClassViewModel.set('className', (self._options.dataSource.className).trim());
		self.storageClassViewModel.set('replicas', self._options.dataSource.replicas);
		self.storageClassViewModel.set('usableSpace', self._options.dataSource.usableSpace);
		self.storageClassViewModel.set('usedSpace', self._options.dataSource.usedSpace);
		self.storageClassViewModel.set('freeSpace', self._options.dataSource.freeSpace);
		self.storageClassViewModel.set('totalSpace', self._options.dataSource.totalSpace);
		self.storageClassViewModel.set('flexible',self._options.dataSource.flexible);
		self.storageClassViewModel.set('defaultValue', self._options.dataSource.defaultValue);
		self.storageClassViewModel.set('performanceLevel', self._options.dataSource.performanceLevel);
		self.calculateRawStorageSpace();
	},
	
	createMode: function(viewModel){
		this.oData = $.extend({
			id:0,
			performanceLevel:'0',//performance
			className: '',//className
			replicas:'1',//replicas
			usableSpace:'0',//
			freeSpace:0,
			totalSpace:'0',//raw storage space
			usedSpace:'0',//used
			flexible:'NO',//flexible
			maxValue:60,  //max
			defaultValue: '',//statesName
			maxCapacity:[]
		},viewModel);
		var self = this;
		if(self.mode != 'create'){
			$(self.parentDiv.find("#controlPanel")).empty();
			self.controlPanel.append(this.Template.panelHeader);
			self.controlPanel.append(self.Template.createPanel);
			self.controlPanel.find("#createPanel").append(self.Template.createTitle);
			self.controlPanel.find("#createPanel").append(self.Template.className);
			self.controlPanel.find("#createPanel").append(self.Template.createPerformance);
			self.controlPanel.find("#createPanel").append(self.Template.editMode);
			self.controlPanel.append(self.Template.createControl);
			function onSpin() {
				self.calculateRawStorageSpace();
				self.storageClassViewModel.set('freeSpace',self.storageClassViewModel.usableSpace);
				self._onChange();
			}
			self.controlPanel.find("#usableSpace").kendoNumericTextBox({
				spin: onSpin,
				min: 0,
				step: 1,
				format: "n0"
			});
			self.numerictextbox = self.controlPanel.find("#usableSpace").data("kendoNumericTextBox");
			self._setData(self.oData);
			self.mode = 'create';
		}
		self.controlPanel.find("#s-create-button").prop('disabled',true); 
	},
	
	editMode: function(viewModel){
		this.oData = $.extend({
			id:0,
			performanceLevel:'0',//performance
			className: '',//className
			replicas:'1',//replicas
			usableSpace:'0',//
			freeSpace:0,
			totalSpace:'0',//raw storage space
			usedSpace:'5',//used
			flexible:'NO',//flexible
			maxValue:60,  //max
			defaultValue: '',//statesName
			maxCapacity:[]
		},viewModel);
		var self = this;
		if(self.mode != 'edit'){
			$(self.parentDiv.find("#controlPanel")).empty();
			self.controlPanel.append(self.Template.panelHeader);
			self.controlPanel.append(self.Template.editTitle);
			self.controlPanel.append(self.Template.editPanel);
			self.controlPanel.find("#editPanel").append(self.Template.className);
			self.controlPanel.find("#editPanel").append(self.Template.createPerformance);
			self.controlPanel.find("#editPanel").append(self.Template.editMode);
			self.controlPanel.append(self.Template.editControl);
			//self.controlPanel.find("#usableSpace").kendoNumericTextBox();
			var m = viewModel.usedSpace;
			self.controlPanel.find("#usableSpace").kendoNumericTextBox({
				format: "n0",
				min: m,
				step: 1,
				spin: function(){
					self.storageClassViewModel.set('freeSpace',self.controlPanel.find("#usableSpace").prop("value"));
					self.calculateRawStorageSpace();
					self._onChange();
				}
			});
			self.numerictextbox = self.controlPanel.find("#usableSpace").data("kendoNumericTextBox");
			self._setData(viewModel);
			self.mode = 'edit';
		}
	},
	
	Template:{
		panelHeader:'<p class="p-title-header" style="padding-left:18px">STORAGE CLASSES</p><hr class="p-hr-s" style="margin-bottom:0px">',
		createPanel:'<div id="createPanel" style="height:510px;overflow:auto;padding:18px;padding-top:0px;padding-bottom:0px"></div>',
		editPanel:'<div id="editPanel"  style="height:446px;overflow:auto;padding:18px;padding-top:0px;padding-bottom:0px"></div>',
		createTitle:'<div id="createTitle">\
						<hr class="p-hr-blank"/>\
						<p class="p-title3">CREATE NEW STORAGE CLASS</p>\
						<hr class="p-hr-blank"/>\
						<hr class="p-hr-1">\
						<hr class="p-hr-blank"/>\
						<p class="p-title3">BASED ON:<span class="help"></span></p>\
						<hr class="p-hr-blank"/>\
						<input style="width:240px" id="basedOn"/>\
						<hr class="p-hr-blank"/><hr class="p-hr-s">\
					</div>',
		editTitle:  '<div id="editTitle" style="border-bottom:1px solid #8EA5AE;padding:18px;background-color: #E5F6FC">\
					<p class="p-title3">EDIT STORAGE CLASS</p>\
					<p><span data-bind="html: className"></span></p>\
					</div>',
		className: '<div id="className">\
						<hr class="p-hr-blank"/>\
						<p style="width:75%" class="p-title3">CLASS NAME\
						<span style="float:right" class="p-label">Max 9 characters</span>\
						<hr class="p-hr-blank"/>\
						<input type="text" data-bind="value: className" id="txtClassName" maxlength="9" style="width:240px" class="p-input" placeholder=""></p>\
						<p id="classNameErrMsg" class="p-errmsg"></p>\
						<hr class="p-hr-blank"/><hr class="p-hr-s">\
					</div>',
		createPerformance:'<div id="createPerformance">\
							<p class="p-title3">PERFORMANCE LEVEL<span class="help"></span></p>\
							<hr class="p-hr-blank"/>\
							<input style="width:240px;height:24px;font-size:12px" id="performanceLevel"  value="" /><span id="imgLevel" style="margin-left:5px" class="p-level-icons p-level-0"></span>\
							<hr class="p-hr-blank"/>\
							<hr class="p-hr-s">\
							</div>',
		editPerformance:'<div id="createPerformance">\
						<p>PERFORMANCE LEVEL</p>\
						<p><span data-bind="html: performanceLevel"></span></p>\
						<hr class="p-hr-s">\
						</div>',
		editMode :  '<div id="editMode">\
						<p class="p-title3">REPLICATION FACTOR<span class="help"></span></p>\
						<span class="p-desc">What replication factor do you need? </span>\
						<input id="factor-1" type="radio" checked="checked" value="1" data-bind="checked: replicas" name="factor">\
						<label for="factor-0">1</label>\
						<input id="factor-2" type="radio" value="2" data-bind="checked: replicas" name="factor">\
						<label style="margin-left:20px" for="factor-2">2</label>\
						<input id="factor-3" type="radio" value="3" data-bind="checked: replicas" name="factor">\
						<label style="margin-left:20px" for="factor-3">3</label>\
						<input id="factor-4" type="radio" value="4" data-bind="checked: replicas" name="factor">\
						<label style="margin-left:20px" for="factor-4">4</label>\
						<hr class="p-hr-blank"/>\
						<hr class="p-hr-s">\
						<p class="p-title3">DEDICATED USABLE STORAGE SPACE<span class="help"></span></p>\
						<p class="p-desc">How much usable storage space would you like to add to this storage class?</p>\
						<p class="p-label" style="width:95%;display:block;text-align:right;">Max <span data-bind="html: maxValue"></span> TB</p>\
						<p class="p-title4" style="height:36px;line-height:36px;"><span>Usable Storage Space</span><span style="float:right"><input id="usableSpace" data-bind="value: usableSpace" style="width:100px;" type="number" value="0" step="1" />TB</span></p>\
						<hr class="p-hr-blank"/>\
						<p class="p-title4" style="height:36px;line-height:36px;"><span>Raw Storage Space</span><span style="float:right"><span style="font-size:30px;color: #7197AE;width:100px;" data-bind="html: totalSpace"></span><span style="color: #7197AE;">TB</span></p>\
						<hr class="p-hr-blank"/>\
						<hr class="p-hr-s">\
						<p class="p-title4">FLEXIBLE STORAGE SPACE ALLOCATION<span class="help"></span></p>\
						<p class="p-desc">Would you like to use flexible storage space allocation?</p>\
						<input id="allocation-0" type="radio" value="NO" data-bind="checked: flexible" name="allocation">\
						<label for="allocation-0">No</label>\
						<input  id="allocation-1" type="radio" value="YES" data-bind="checked: flexible" name="allocation">\
						<label style="margin-left:30px" for="allocation-1">Yes(Recommended)</label>\
						<hr class="p-hr-blank"/>\
						<hr class="p-hr-s">\
						<p class="p-title3">DEFAULT STORAGE CLASS<span class="help"></span></p>\
						<p class="p-desc">Make this the default storage class?</p>\
						<input id="default-0" type="radio" value="" data-bind="checked: defaultValue" name="default">\
						<label for="default-0">No</label>\
						<input  id="default-1" type="radio" value="DEFAULT" data-bind="checked: defaultValue" name="default">\
						<label style="margin-left:30px" for="default-1">Yes</label>\
					</div>',
		createControl: '<div style="height:66px;text-align:center;background-color: #A1A9AC"><button id="s-create-button" style="margin-top:21px;font-size:12px;width:296px;height:24px" class="cta-button">CREATE STORAGE CLASS</button></div>',
		editControl:'<div style="height:66px;text-align:center;background-color: #A1A9AC"><button id="s-cancel-button" style="margin-top:21px;font-size:12px;width:143px;height:24px" class="save-button">CANCEL</button><button style="margin-top:21px;font-size:12px;width:143px;margin-left:10px;height:24px" id="s-save-button" class="save-button">SAVE CHANGES</button></div>'
	}
});
