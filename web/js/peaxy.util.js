(function($){'use strict';var escape=/["\\\x00-\x1f\x7f-\x9f]/g,meta={'\b':'\\b','\t':'\\t','\n':'\\n','\f':'\\f','\r':'\\r','"':'\\"','\\':'\\\\'},hasOwn=Object.prototype.hasOwnProperty;$.toJSON=typeof JSON==='object'&&JSON.stringify?JSON.stringify:function(o){if(o===null){return'null';}
var pairs,k,name,val,type=$.type(o);if(type==='undefined'){return undefined;}
if(type==='number'||type==='boolean'){return String(o);}
if(type==='string'){return $.quoteString(o);}
if(typeof o.toJSON==='function'){return $.toJSON(o.toJSON());}
if(type==='date'){var month=o.getUTCMonth()+1,day=o.getUTCDate(),year=o.getUTCFullYear(),hours=o.getUTCHours(),minutes=o.getUTCMinutes(),seconds=o.getUTCSeconds(),milli=o.getUTCMilliseconds();if(month<10){month='0'+month;}
if(day<10){day='0'+day;}
if(hours<10){hours='0'+hours;}
if(minutes<10){minutes='0'+minutes;}
if(seconds<10){seconds='0'+seconds;}
if(milli<100){milli='0'+milli;}
if(milli<10){milli='0'+milli;}
return'"'+year+'-'+month+'-'+day+'T'+
hours+':'+minutes+':'+seconds+'.'+milli+'Z"';}
pairs=[];if($.isArray(o)){for(k=0;k<o.length;k++){pairs.push($.toJSON(o[k])||'null');}
return'['+pairs.join(',')+']';}
if(typeof o==='object'){for(k in o){if(hasOwn.call(o,k)){type=typeof k;if(type==='number'){name='"'+k+'"';}else if(type==='string'){name=$.quoteString(k);}else{continue;}
type=typeof o[k];if(type!=='function'&&type!=='undefined'){val=$.toJSON(o[k]);pairs.push(name+':'+val);}}}
return'{'+pairs.join(',')+'}';}};$.evalJSON=typeof JSON==='object'&&JSON.parse?JSON.parse:function(str){return eval('('+str+')');};$.secureEvalJSON=typeof JSON==='object'&&JSON.parse?JSON.parse:function(str){var filtered=str.replace(/\\["\\\/bfnrtu]/g,'@').replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,']').replace(/(?:^|:|,)(?:\s*\[)+/g,'');if(/^[\],:{}\s]*$/.test(filtered)){return eval('('+str+')');}
throw new SyntaxError('Error parsing JSON, source is not valid.');};$.quoteString=function(str){if(str.match(escape)){return'"'+str.replace(escape,function(a){var c=meta[a];if(typeof c==='string'){return c;}
c=a.charCodeAt();return'\\u00'+Math.floor(c/16).toString(16)+(c%16).toString(16);})+'"';}
return'"'+str+'"';};}(jQuery));
/*mouse wheel*/
(function(a){function d(b){var c=b||window.event,d=[].slice.call(arguments,1),e=0,f=!0,g=0,h=0;return b=a.event.fix(c),b.type="mousewheel",c.wheelDelta&&(e=c.wheelDelta/120),c.detail&&(e=-c.detail/3),h=e,c.axis!==undefined&&c.axis===c.HORIZONTAL_AXIS&&(h=0,g=-1*e),c.wheelDeltaY!==undefined&&(h=c.wheelDeltaY/120),c.wheelDeltaX!==undefined&&(g=-1*c.wheelDeltaX/120),d.unshift(b,e,g,h),(a.event.dispatch||a.event.handle).apply(this,d)}var b=["DOMMouseScroll","mousewheel"];if(a.event.fixHooks)for(var c=b.length;c;)a.event.fixHooks[b[--c]]=a.event.mouseHooks;a.event.special.mousewheel={setup:function(){if(this.addEventListener)for(var a=b.length;a;)this.addEventListener(b[--a],d,!1);else this.onmousewheel=d},teardown:function(){if(this.removeEventListener)for(var a=b.length;a;)this.removeEventListener(b[--a],d,!1);else this.onmousewheel=null}},a.fn.extend({mousewheel:function(a){return a?this.bind("mousewheel",a):this.trigger("mousewheel")},unmousewheel:function(a){return this.unbind("mousewheel",a)}})})(jQuery)

var peaxy = peaxy || {};
peaxy.Validator = {
	checkNamePattern: function(username){
		username = $.trim(username);
		return new RegExp(/^([.@]|[a-zA-Z0-9]){3,32}$/).test(username);		
	},
	checkEmail: function(email){
		email = $.trim(email);
		var pattern = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-]{1,})+$/;
		return pattern.test(email);		
	},
	isEmpty:function(val){
		return $.trim(val || '').length > 0 ? false : true;
	},
	checkLengthRange:function(val,min,max){
		val = $.trim(val);
		if(val.length < min || val.length > max)
			return false
		return true;
	},
	containCapital:function(val){
		return new RegExp(/[A-Z]+/).test(val);
	},
	containLowercase:function(val){
		return new RegExp(/[a-z]+/).test(val);
	},
	containNumber:function(val){
		return new RegExp(/[0-9]+/).test(val);
	},
	checkFileType: function(file, requestedType){
		var fileName = file.name;
		var fileTypePos = fileName.lastIndexOf(".") + 1;
		var type = fileName.substring(fileTypePos);
		if(type === requestedType)
			return true;
		else
			return false;
	},
	checkFileSize: function(file, maximumSize, sizeUnit){
		var fileActualSize = file.size;
		
		for (var aMultiples = ["Byte", "KB", "MB", "GB"], nMultiple = 0; sizeUnit!= aMultiples[nMultiple] ; nMultiple++) {
    		fileActualSize = fileActualSize/1024;
  		}  		
  		return fileActualSize < maximumSize;
	},
	setCookie: function(name, value, expires, path, domain, secure){
		var today = new Date();
		today.setTime(today.getTime());
		if(expires) { expires *= 86400000; }
		var expires_date = new Date(today.getTime() + (expires));
		
		document.cookie = name + "=" + escape(value)
								+ (expires ? ";expires=" + expires_date.toGMTString() : "")
								+ (path ? ";path=" + path : "")
								+ (domain ? ";domain=" + domain : "")
								+ (secure ? ";secure" : "");
	},
	checkIPFormat: function(ipaddress) {
		var regip= /^(([0-1]?\d{1,2}|2[0-4]\d|25[0-5])\.){3}([0-1]?\d{1,2}|2[0-4]\d|25[0-5])$/;
		if(regip.test(ipaddress)){
			return true;
		}else{
			return false;
		}
	}
}

peaxy.Check = kendo.Class.extend({
	init:function(){
		this.checkes = [];
	},
	register:function(input,output,validatorfunction){
		var checkfun = function(e){
			input = $(input);
			var returns = validatorfunction(input.val());
			if(typeof(returns) == "boolean" && returns == true){
				input.removeClass('invalidText').addClass('validText');
				$(output).text('');
				return true;
			} else {
				input.removeClass('validText').addClass('invalidText');
				$(output).text(returns);
				return false;
			}
		}
		this.checkes.push(checkfun);
		$(input).change(checkfun);
	},
	validator:function(){
		var validator = true;
		for(var i=0,l=this.checkes.length;i < l;i++){
			if(!this.checkes[i]()){
				validator = false;
			}
		}
		return validator;
	}
});

peaxy.tooltipDialog = function(target, options){
	var _opt = {
		content:'',/* String or jquery dom*/
		position:'top',
		contentCSS:{'background-color':'#505966','color':'white','margin':0},
		bottomCSS:{'background-color':'#232F3E','white-space':'nowrap','padding':'15px 0','text-align':'center'},
		width:'300px',
		actions:[]/*'action:{'name':'',action:function(e){},css:{},class:'' }*/
	}
	$.extend(_opt,options||{});

	var buttons=[], rootdiv=$('<p></p>'), buttonGroup=$('<ul></ul>');

    rootdiv.css(_opt.contentCSS);
	if(typeof(_opt.content) === 'string' && _opt.content.length > 0){
		rootdiv.append($('<p style="padding:10px;"></p>').html(_opt.content));
	}else{
		rootdiv.append(_opt.content);
	}
	if(_opt.actions.length > 0){
		buttonGroup.css(_opt.bottomCSS);
		for(var i = 0,l =_opt.actions.length;i<l;i++){
			var action = _opt.actions[i];
			action = _opt.actions[i] = $.extend({action:function(){}, class:'s-button'}, _opt.actions[i]);
			var button = $('<button></button>');
			button.addClass(action.class);
			button.css($.extend({margin:'0 10px','min-width':'130px'}, action.css));
			button.text(action.name);
			button.data('index',i);
			buttonGroup.append(button);
		}
		buttonGroup.find('button').click(function(e){
			_opt.actions[$(this).data('index')].action(e);
			tooltip.hide();
		});
	    rootdiv.append(buttonGroup);
    }
    /* create  kendo tooltip */
    var tooltip = $(target).data('kendoTooltip');
	if(!tooltip){
		tooltip = $(target).kendoTooltip({
			autoHide:false,
			showOn:'click',
			content:rootdiv,
			position:_opt.position,
			width:_opt.width
		}).data('kendoTooltip');
	}
	tooltip.show(target);
	return tooltip;
}

peaxy.popupMsg = function(content, options){
	var opt = {
		parent:$('body'),
		class:'p-def-bg',
		location:'up',/* up or down */
		autoHide:true,
		delay:5000,
		width:'100%',
		height:'24px'
	}
	$.extend(opt, options || {});
	if(!opt.parent.data("slidein")){
		initSlideIn();
	}else{
		resetOption();
	}
	function resetOption(){
		var slidein = opt.parent.data("popupMsg");
		var cont = opt.parent.data('cont');
		if(opt.parent.data('delayF')){
			clearTimeout(opt.parent.data('delayF'));
			opt.parent.removeData('delayF');
		}
		cont.addClass(opt.class);
		cont.html(content);
		slidein.show();
		if(opt.autoHide){
			var f = setTimeout(function(){ slidein.hide(); },opt.delay);
			opt.parent.data('delayF',f);
		}
		if(cont.find('.p-i-close').length == 0){
			var close = $('<span class="p-secondary-icons p-si-close" style="position:relative;z-index:999999999;float:right;"></span>').click(function(){
				slidein.hide();
				if(opt.parent.data('delayF')){
					clearTimeout(opt.parent.data('delayF'));
					opt.parent.removeData('delayF');
				}
			});
			cont.append(close);
		}
	}
	function initSlideIn(){
		if(!peaxy.Slidein){
			peaxy.Communication.getScript('js/peaxy.slidein.js',init);
		}else{
			init();
		}
		function init(){
			var slideddiv = $('<div style="position:relative;z-index:999999999;"></div>');
			slideddiv.css({'position':'relative','z-index':'999999999','color':'white','font-weight':'bold'});
			opt.parent.prepend(slideddiv);
			var slidein = new peaxy.Slidein({
				parent:opt.parent,
				container:slideddiv,
				width:opt.width,
			    height:opt.height,
			    location:opt.location,
			    hasTitle:false
			});
			slidein.getContainer().css({'border':0,'vertical-align':'middle','text-align':'center','line-height':'24px'});
			opt.parent.data("popupMsg",slidein);
			opt.parent.data("cont",slidein.getContainer());
			resetOption();
		}
	}
}

peaxy.TIMEZONES=[
{name:'PDT',value:'-7:00'}
,{name:'PST',value:'-8:00'}
,{name:'IDLE',value:'+12:00'}
,{name:'NZDT',value:'+13:00'}
,{name:'AESST',value:'+11:00'}
,{name:'ACSST',value:'+10:30'}
,{name:'AEST',value:'+10:00'}
];