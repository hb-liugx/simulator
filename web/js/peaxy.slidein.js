var peaxy = peaxy || {};
peaxy.Slidein = kendo.Class.extend({
    // The `init` method will be called when a new instance is created
	_opt:{
		parent:null,
		container:'',
		width:'0',
		height:'0',
		positionX:'0',
		location:'down',
		content:'',
		hasTitle:true,
		titleHeight:'0',
		titleContent:'',
		titleClass:'',
		contentClass:''
	},
	slide: null,
	visible: true,
    init: function(options) {
 
	   $.extend(this._opt,options);
	   
	   this._setParentStyle(options.parent);
	   this._setSlideinStyle(options);
	   var titleID = this._setTitle(options);
	   //this._slideIn(this._setTitle);
	   this._setContent(options);
	   this._setSlideIn(options,titleID);	   
    },
	_setParentStyle:function(parent) {
	   parent.css({'position':'relative','overflow':'hidden'});
	   //parent.addClass('slidein-parent');
	},
	_setSlideinStyle:function(opt) {
	    if(opt.container) {
			opt.container.css({
				"width":opt.width,
				"height":opt.height,
				"position":"absolute",
				'z-index':3
			});
			opt.positionX = (opt.positionX ? opt.positionX :'0');
			parentWidth = opt.parent.css("width");
			parentHeight = opt.parent.css("height");
			switch(opt.location) {
				case "left":
					opt.container.css("margin-left","-"+opt.width);
					opt.container.css("margin-top",opt.positionX);
					break;
				case "right":
					opt.container.css("margin-left",parentWidth);
					opt.container.css("margin-top",opt.positionX);
					break;
				case "down":
					opt.container.css("margin-top",parentHeight);
					opt.container.css("margin-left",opt.positionX);
					break;
				case "up":
					opt.container.css("margin-top","-"+opt.height);
					opt.container.css("margin-left",opt.positionX);
					break;
			}
	    }
	},
	_setTitle:function(_opt) {
		var titleID;
		if(_opt.container && _opt.hasTitle) {
			var title = $('<div></div>');
			titleID = 'slidein_title';
			title.attr('id',titleID);
			title.css({
				"position":"absolute",
				"cursor":"pointer"
			});
			switch(_opt.location) {
				case "left":
					title.css("height",_opt.height);
					title.css("width",_opt.titleHeight);
					title.css("margin-left",_opt.width);					
					break;
				case "right":
					title.css("height",_opt.height);
					title.css("width",_opt.titleHeight);
					title.css("margin-left","-"+_opt.titleHeight);
					break;
				case "down":
					title.css("width",_opt.width);
					title.css("height",_opt.titleHeight);
					title.css("margin-top","-"+_opt.titleHeight);					
					break;
				case "up":
					title.css("width",_opt.width);
					title.css("height",_opt.titleHeight);
					title.css("margin-top",_opt.height);					
					break;
			}
			if(_opt.titleContent) {
				title.append(_opt.titleContent);
			}
			if(_opt.titleClass) {
				title.addClass(_opt.titleClass);
			}
			
			$('#'+_opt.id).append(title);
		}
		return titleID;
	},
	_setContent:function(_opt) {
		if(_opt.container) {
			var content = $('<div></div>');
			content.attr('id','slidein_content');
			content.css({
				"position":"absolute",
				"width":_opt.width,
				"height":_opt.height,
				"border":"solid 1px",
				"background":"#FFFFFF"
			});
			if(_opt.content) {
				content.append(_opt.content);
			}
			if(_opt.contentClass) {
				content.addClass(_opt.contentClass);
			}
			
			_opt.container.append(content);
		}
	},
	getContainer:function(content){
		return this._opt.container.find('[id="slidein_content"]');
	},
	_setSlideIn:function(_opt,titleID) {
		if(_opt.container) {
			this.slide = kendo.fx(_opt.container).slideIn(_opt.location);
			this.visible = true;
			if(titleID){
				_opt.container.find("#"+titleID).click(function(e) {
					//if (visible) {
						//slide.reverse();
					//} else {
						//slide.play();
					//}
					//visible = !visible;
					peaxy.Slidein.prototype.slidein();
					e.preventDefault();
				});
			}
		}
	},
	show:function(){
		if (this.visible) {
				this.slidein();
		}		
	},
	hide:function(){
		if (!this.visible) {
				this.slidein();
		}
	},
	slidein:function(){
		if(this.slide) {
			if (this.visible) {
				this.slide.reverse();
			} else {
				this.slide.play();
			}
			this.visible = !this.visible;
			//e.preventDefault();
		}
	}
});

