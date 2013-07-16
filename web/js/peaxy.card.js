var peaxy = peaxy || {};
//toolbar:'<div class="classCard-toolbar"><a href="javascript:void(0)"><span id="tool_chart" class="k-icon k-i-restore"></span></a><a href="javascript:void(0)"><span id="tool_info" class="k-icon k-i-insert-m"></span></a></div>'
peaxy.Card = kendo.Class.extend({
	cardView:null,
	cardElement:null,
	title:null,
	content:null,
	header:null,
	toolbar:'',
	cardStatus:'Preview',
	status:null,
	close:'<div id="close" style="float:right;margin-right:5px"><a href="javascript:void(0);"><span class="k-icon k-i-close" >Close</span></a></div>',
	_options:{
		width:196,
		height:329,
		isClosed:false,
		type:'',
		parent:document.body,
		onClose:function(){},
		cardStates:'',
		id: ''
	},

    init: function(options) {
		jQuery.extend(this._options,options);
		this.cardView = this._options.parent;		
		this.cardElement = $('<div id='+ this._options.id +' class="card '+ this._options.type +'" style="position: relative; width:'+ this._options.width +'px; height:'+ this._options.height +'px"><div id="masking" class="v2"></div></div>');
		this.cardElement.data('card',this);
		// status bar
		this.cardElement.append(this._options.status); 
		//header
		this.cardElement.append(this._options.header);
		// close button
		if (this._options.isClosed){
			this.cardElement.find("#header").append(this.close);
			this.cardElement.find("#close").bind("click",this._options.onClose);
		}
		
		// tool bar
		this.cardElement.append(this._options.toolbar);
		// content
		this.cardElement.append(this._options.content);
    },
	disabled: function(){
		this.cardElement.find('#masking').show();
	},
	
	destroy: function(){
		this.cardElement.remove();
	},
	
	usabled: function(){
		this.cardElement.find('#masking').hide();
	},
	
	draw: function(){
		this.cardView.append(this.cardElement);
		
	},
	show:function(){
		this.cardView.show();
	},
	
	hide:function(){
		this.cardView.hide();
	}
});

peaxy.ClassCard = peaxy.Card.extend({
	classCardViewModel:null,
    init: function(options) {
		this._options.id = 'card_' + (new Date().getTime()) + parseInt(Math.random()*100 + 1);
		this.id = this._options.id;
		this._options.parent = options.parent;
		this._options.type = 'classCard';
		this._options.header = this.Template.header_finished;
		this._options.status = this.Template.status_finished;
		this._options.toolbar = this.Template.toolbar_finished,
		this._opt = {
			isClosed: options.isClosed,
			onClose:options.onClose,
			cardStatus: options.cardStatus,
			dataSource:{
				id:0,
				performanceLevel:'0',//performance
				className: '',//className
				replicas:'0',//replicas
				usableSpace:'0',//
				freeSpace:0,
				totalSpace:'0',//raw storage space
				usedSpace:'0',//used
				flexible:'YES',//flexible
				defaultValue: ''//statesName
			}
		}
		if(this._opt.cardStatus == "Preview"){
			this.cardView = this._options.parent;
			this.cardElement = $('<div id='+ this._options.id +' class="card preview" style="position: relative; width:'+ this._options.width +'px; height:'+ this._options.height +'px"><div id="masking" class="v2"></div></div>');
			this.cardElement.data('card',this);
			this.cardView.append(this.cardElement);
		} else {
			this._options.content = this.Template.content;
			peaxy.Card.fn.init.call(this,this._opt);
		}
		this.classCardViewModel = kendo.observable({
			id:0,
			performanceLevel:'0',//performance
			className: '',//className
			replicas:'0',//replicas
			usableSpace:'0',//
			freeSpace:0,
			totalSpace:'0',//raw storage space
			usedSpace:'0',//used
			flexible:'NO',//flexible
			defaultValue: ''//statesName
		});
		kendo.bind(this.cardElement, this.classCardViewModel);
    },
	
	draw:function(){
		peaxy.Card.fn.draw.call(this);
	},
	
	updateStatus:function(status){
		if(status == "Creation"){
			this.cardView.find("#"+this.id).empty();
			this.cardView.find("#"+this.id).attr("class","card classCard");
			this.cardView.find("#"+this.id).append('<div id="masking" class="v2"></div>');
			this.cardElement.append(this.Template.status_creation); 
			//header
			this.cardElement.append(this.Template.header_creation);
			// tool bar
			this.cardElement.append(this.Template.toolbar_creation);
			// content
			this.cardElement.append(this.Template.content);
			
		//	this.cardView.append(this.cardElement);
			this.classCardViewModel = kendo.observable({
				performanceLevel:'0',//performance
				className: '',//className
				replicas:'0',//replicas
				usableSpace:'0',
				freeSpace:0,
				totalSpace:'0',//raw storage space
				usedSpace:'0',//used
				flexible:'YES',//flexible
				defaultValue: ''//statesName
			});
			kendo.bind(this.cardElement, this.classCardViewModel);
		} else {
			this.cardElement.find("#header").attr("class","header-finished");
			this.cardElement.find("#status").attr("class","status-finished");
			this.cardElement.find("#toolbar").attr("class","toolbar-finished");
		}
	},
	
	getData: function(){
		return this.classCardViewModel.toJSON();
	},

	setData:function(viewModel){
		jQuery.extend(this._opt.dataSource,viewModel);
		this.classCardViewModel.set('className', this._opt.dataSource.className);
		this.classCardViewModel.set('defaultValue',this._opt.dataSource.defaultValue);
		this.classCardViewModel.set('totalSpace',this._opt.dataSource.totalSpace);
		this.classCardViewModel.set('usableSpace',this._opt.dataSource.usableSpace);
		this.classCardViewModel.set('usedSpace',this._opt.dataSource.usedSpace);
		this.classCardViewModel.set('freeSpace',parseInt(this._opt.dataSource.usableSpace) - parseInt(this._opt.dataSource.usedSpace));
		this.classCardViewModel.set('replicas',this._opt.dataSource.replicas);
		this.classCardViewModel.set('performanceLevel',this._opt.dataSource.performanceLevel);
		this.classCardViewModel.set('flexible',this._opt.dataSource.flexible);
		this.cardElement.find("#imgLevel").attr("class",'p-level-icons p-level-'+this._opt.dataSource.performanceLevel+'');
	},
	
	Template:{
		header_finished:'<div id="header-finished" class="header-finished"><span class="header-font" data-bind="html: className"></span></div>',
		status_finished:'<div id="status-finished" class="status-finished"></div>',
		toolbar_finished:'<div  id="toolbar-finished" class="toolbar-finished"><span class="toolbar-font" data-bind="html: defaultValue"></span></div>',
		header_preview:'<div id="header" class="header-preview"><span class="header-font" data-bind="html: className"></span></div>',
		status_preview:'<div id="status" class="status-preview"></div>',
		toolbar_preview:'<div  id="toolbar" class="toolbar-preview"><span class="toolbar-font" data-bind="html: defaultValue"></span></div>',
		header_creation:'<div id="header" class="header-creation"><span class="header-font" data-bind="html: className"></span></div>',
		status_creation:'<div id="status" class="status-creation"></div>',
		toolbar_creation:'<div  id="toolbar" class="toolbar-creation"><span class="toolbar-font" data-bind="html: defaultValue"></span></div>',
		content: "<div id='content' class='content'>\
			<table 	border=0 cellpadding=0 cellspacing=0>\
				<tr><td></td><td class='r1t2'><span data-bind='html: usableSpace'></span></td><td class='r1t3'>TB</td></tr>\
				<tr><td class='r2'  colspan='3'><HR style='FILTER: alpha(opacity=100,finishopacity=0,style=3)' width='100%' color=#002337 SIZE=2></td></tr>\
				<tr><td class='underline2'>FREE</td><td class='r3t2'><span data-bind='html: freeSpace'></span></td><td class='r3t3'>TB</td></tr>\
				<tr><td class='underline2'>Used</td><td class='r3t2'><span data-bind='html: usedSpace'></span></td><td class='r3t3'>TB</td></tr>\
				<tr><td class='r5' colspan='3'>USABLE CAPACITY VALUES</td></tr>\
				<tr><td class='underline2'>REPLICATION FACTOR</td><td class='underline2'></td><td class='r7t3'><span data-bind='html: replicas'></span></td></tr>\
				<tr><td class='underline2'>PERFORMANCE</td><td class='underline2' style='vertical-align:middle' colspan='2'><span id='imgLevel' class='p-level-icons p-level-0'></span></tr>\
				<tr><td class='underline2'>FLEXIBLE</td><td colspan='2' class='r7t3'><span data-bind='html: flexible'></span></td></tr>\
			</table>\
			</div>"
	}
});


peaxy.NamespaceCard = peaxy.Card.extend({
	nameSpaceCardViewModel:null,
    init: function(options) {
		this._options.id = 'card_' + (new Date().getTime()) + parseInt(Math.random()*100 + 1);
		this.id = this._options.id;
		this._options.parent = options.parent;
		//this._opt= {cardStatus : 'ddddd'}
		this._options.type = 'namespaceCard';
		this._options.header = this.Template.header;
		this._options.status = this.Template.status;
		this._options.toolbar = this.Template.toolbar;
		this._options.content = this.Template.content;
	
		peaxy.Card.fn.init.call(this,options);
		this.nameSpaceCardViewModel = kendo.observable({
			ratio : "1:4",
			redundancy : "1"
		});
		kendo.bind(this.cardElement, this.nameSpaceCardViewModel);
	},
	
	setData:function(viewModel){
		this.nameSpaceCardViewModel.set('ratio', viewModel.ratio);
		this.nameSpaceCardViewModel.set('redundancy',viewModel.redundancy);
	},
	
	Template:{
		header:'<div class="header"><span class="header-font">NAMESPACE</span></div>',
		status:'<div class="status"></div>',
		toolbar:'<div class="toolbar"></div>',
		content:'<div class="content">\
				<table 	border=0 cellpadding=0 cellspacing=0>\
					<tr><td>NAME:DATA RATIO</td><td class="r1t2"><span data-bind="html: ratio"></span></td></tr>\
					<tr><td>REPLICATION FACTOR</td><td class="r1t2"><span data-bind="html: redundancy"></span></td></tr>\
				</table>\
				</div>'
	}
	
});

