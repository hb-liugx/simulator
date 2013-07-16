var peaxy = peaxy || {};
peaxy.FinalizeHyperfiler = kendo.Class.extend({
	view:null,
	parentDiv:null,
	detailView:null,
	viewModel:'',
	interval:'',
	_options:{
		parent:document.body,
		onComplete:function(bool){}
	},
    init: function(config, options) {
		jQuery.extend(this._options,options);
		var self = this;
		this.parentDiv = options.parent;//position: relative;
		this.view = $('<div></div>');
		this.detailView = '<div class="p-content">\
							<span class="p-title3">CONFIGURATION</span>&nbsp&nbsp&nbsp<span class="p-title3" data-bind="html: hyperfilerName"></span>\
							<hr class="p-hr-blank"/>\
							<hr class="p-hr-blank"/>\
							<div id="errorMessage"></div>\
							<hr class="p-hr-blank"/>\
							<hr class="p-hr-s">\
							<hr class="p-hr-blank"/>\
							<hr class="p-hr-blank"/>\
							<span class="p-title3">CONFIGURATION YOUR SYSTEM</span>\
							<hr class="p-hr-blank"/>\
							<div class="p-title3">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque\
										viverra ultricies lacus, at egestas elit accumsan eu. Vestibulum vitae diam\
										nibh. Quisque porttitor, ipsum non viverra malesuada, sem est aliquet\
										quam, in imperdiet turpis ante id neque.</div>\
						  </div>';
		this.view.append(this.detailView);		
		this.viewModel = kendo.observable({
			hyperfilerName: config.hyperfilerName,
		});
		kendo.bind(this.view, this.viewModel);
		this.draw();

        /* call setconfig api*/
        peaxy.Communication.getScript('js/peaxy.oob.js',function(){
			peaxy.OOB.setConfig(config, function(msg, httpCode){
				var bool = msg == 'success' ? true : false;
				if(msg == 'success')
					msg = 'Configuration Successfull';
			    else
					msg = 'Configuration Failed';
				self._options.parent.find('#errorMessage').text(msg);
				

				self._options.onComplete(bool);
			});
        });
    },
	_setData: function(data){
		var t = '<p class="p-title3">ERRORS / WARNINGS</p><hr class="p-hr-blank"/>';
		this.view.find("#errorMessage").append();
	},
	draw: function(){
		this.parentDiv.append(this.view);
	}
});