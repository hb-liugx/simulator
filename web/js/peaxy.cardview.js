var peaxy = peaxy || {};
peaxy.CardView = kendo.Class.extend({
	cardView:null,
	parentDiv:null,
	detailView:null,
	listView:null,
	cardViewModel:null,
	classList:[],
	flag:false,
	_options:{
		width:668,
		height:628,
		parent:document.body,
		dataSource:{
			hyperfilerName:'',
			totalSpace:'0',
			unallocated:'0',
			allocated:'0',
			encryption:'No',
			storageClasses:{}
		},
		onAddRes:function(){},
		onConfig:function(card){}
	},

    init: function(options) {
	
		jQuery.extend(this._options,options);
		this.parentDiv = options.parent;//position: relative;
		this.cardView = $('<div class="cardview" style="width:'+ this._options.width +'px; height:'+ this._options.height +'px"></div>');
		this.detailView = "<div class='detailview'>\
						<table border=0 cellpadding=0 cellspacing=0>\
							<tr><td class='r1t1' colspan='8'><span data-bind='html: hyperfilerName'></span><button id='addhyperfiler' class='s-button' style='float:right;font-size: 10px;width:249px;height:24px'>ADD MORE HYPERFILER RESOURCES</button></td></tr>\
							<tr><td rowspan='2' class='r2t1'><span data-bind='html: totalSpace'></span></td><td></td><td colspan='2' class='r2t3'>UNALLOCATED</td><td colspan='2' class='r2t3'>ALLOCATED</td><td class='r2t5'></td><td class='r2t6'>ENCRYPTION ON THE WIRE</td></tr>\
							<tr><td class='r3t2'>TB</td><td class='r3t3'><span data-bind='html: unallocated'></span></td><td class='r3t2'>TB</td><td class='r3t3'><span data-bind='html: allocated'></span></td><td class='r3t2'>TB</td><td class='r2t5'></td><td class='r3t6'><span data-bind='html: encryption'></span></td></tr>\
							<tr><td class='r4' colspan='8'>RAW CAPACITY VALUES</td></tr>\
							<tr><td class='r5' colspan='8'><hr class='p-hr-l'/></td></tr>\
							<tr><td class='r1t1' colspan='8'>STORAGE CLASSES<button id='addclass' class='s-button' style='float:right;font-size: 10px;width:148px;height:24px'>ADD STORAGE CLASS</button></td></tr>\
						</table>\
						</div>";
		this.cardView.append(this.detailView);
		this.listView = "<div id='listview' class='listview'></div>"
		this.cardView.append(this.listView);
		this.cardViewModel = kendo.observable({
			hyperfilerName:'',
			totalSpace:'0',
			unallocated:'0',
			allocated:'0',
			encryption:'No',
			storageClasses:{}
		});
		kendo.bind(this.cardView, this.cardViewModel);
		
		var self = this;
		this.cardView.find("#addhyperfiler").click(function(){
			self._options.onAddRes();
		});
		this.cardView.find("#addclass").click(function(){
			self._options.onConfig(self.addStorageClass());
		});
    },
	
	addHyperfiler: function(){
		peaxy.ModuleManager.get('Configuration').instalLocallResource();
	},
	
	initPreviewCard: function(){
		var classCard = new peaxy.ClassCard({
			parent:$(this.cardView.find("#listview")),
			width:196,
			height:329,
			cardStatus:"Preview"
		});
		this.classList.push(classCard);
		return classCard;
	},
	
	
	addStorageClass: function(){
		var self = this;
		if(self.cardView.find(".card-preview").length == 0 && self.cardView.find(".status-creation").length == 0){
			if(!peaxy.Card){
				peaxy.Communication.getScript('js/peaxy.card.js',function(){
					peaxy.Communication.getCSS('css/peaxy.card.css');
					self.initPreviewCard();	
					self.onCardClick();
			});
			} else{
				self.onCardClick();
				return self.initPreviewCard();
				
			}
			
		}
	},
	
	setHfData: function(data){
		var self = this;
		self.cardViewModel.set('hyperfilerName', data.hyperfilerName);
		self.cardViewModel.set('totalSpace',data.totalSpace);
		self.cardViewModel.set('unallocated',data.unallocated);
		self.cardViewModel.set('allocated',data.allocated);
		self.cardViewModel.set('encryption',data.encryption);
	},
	
	setData: function(viewModel){
		var that = this;
		that.classList = [];
		jQuery.extend(that._options.dataSource,viewModel);
		that.cardViewModel.set('hyperfilerName', that._options.dataSource.hyperfilerName);
		that.cardViewModel.set('totalSpace',that._options.dataSource.totalSpace);
		that.cardViewModel.set('unallocated',that._options.dataSource.unallocated);
		that.cardViewModel.set('allocated',that._options.dataSource.allocated);
		that.cardViewModel.set('encryption',that._options.dataSource.encryption);
		$(that.cardView.find("#listview")).empty();
		
		if(!peaxy.Card){
			peaxy.Communication.getScript('js/peaxy.card.js',function(){
				peaxy.Communication.getCSS('css/peaxy.card.css');
				that.initCard(viewModel);
			});
		} else
			that.initCard(viewModel);
	},
	
	initCard:function(viewModel){
		var that = this;
		var list = $(that.cardView.find("#listview"));
		var namespaceCard = new peaxy.NamespaceCard({
			parent:list,
			width:196,
			height:329
		});
		namespaceCard.setData(viewModel.storageClasses.namespaceCardData);
		namespaceCard.draw();
		
		$.each(viewModel.storageClasses.classes, function(i,n){
			that.classList[i] = new peaxy.ClassCard({
				parent:list,
				width:196,
				height:329,
				cardStatus:"Finished",
			});
			that.classList[i].setData(n);
			that.classList[i].draw();
		});
		that.classList.push(namespaceCard);
		that.onCardClick();
	},
	
	onCardClick: function(){
		var that = this;
		var j;
		that.cardView.find("#listview").find('.card').on('click',function (e) {
			if($(e.target).closest(".namespaceCard").prop("class") == "card namespaceCard")
				return;
			if(!that.flag){
				that.cardView.find("#addclass").prop("disabled",true);
				j = jQuery.inArray($(e.target).closest(".card").data('card'), that.classList);
				that._options.onConfig(that.classList[j]);
				$.each(that.classList, function(i,n){
					if(n != that.classList[j]){
						n.disabled();
					}	
				});
				that.flag = true;
			}
			else 
				return;
        });  
	},
	
	awakenAll: function(){
		var that = this;
		that.cardView.find("#addclass").prop("disabled",false);
		$.each(that.classList, function(i,n){
			n.usabled();
		});
		that.flag = false;
	},
	
	removeCard: function(){
	
	},
	
	draw: function(){
		this.parentDiv.append(this.cardView);
	}
});
