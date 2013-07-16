var peaxy = peaxy || {};
peaxy.Communication=(function(eventservice){
	/*
	var messageHandlers = [],w;
    var wsURLs = ['',''];
    if(typeof(w) == "undefined"){
      w=new Worker("js/peaxy.workers.js");
      w.onmessage = function(event){
        for(var i=0,l=messageHandlers.length;i<l;i++){
            messageHandlers[i](event.data);
        }
      }
    }
    */
    var hf_token = '', scriptList = [], cssList = [], root = '';
    function ajaxRequest(method, url, data, callback, type){
		if(jQuery.isFunction(data)){
			type = type || callback;
			callback = data;
			data = undefined;
		}
		return jQuery.ajax({
			url: url,
			type: method,
			contentType:'application/json',
			dataType: type || 'text',
			data: data,
			headers:{'hf-token':hf_token},
			error:function(xhr,textStatus,errorThrown){
				if($.isFunction(callback)) callback(xhr.responseText, xhr.status);
			},
			success:function(data,textStatus,xhr){
				if($.isFunction(callback)) callback(data, xhr.status);
			}
		});
    };
    return {
        get:function(url, data, callback, type){
            return ajaxRequest('get',root+url, data, callback, type);
        },
        post:function(url, data, callback, type){
            return ajaxRequest('post',root+url, data, callback, type);
        },
        put:function(url, data, callback, type){
            return ajaxRequest('put',root+url, data, callback, type);
        },
        del:function(url, data, callback, type){
            return ajaxRequest('delete',root+url, data, callback, type);
        },
        getScript:function(url,handler){
        	return ajaxRequest('get',root+url, handler,'script');
        },
        getFile:function(url, handler){
			return ajaxRequest('get',root+url, handler);
        },
        getCSS:function(cssUrl){
        	if(jQuery.inArray(cssUrl, cssList) > 0)				
        		return;     		
     		cssList.push(cssUrl);
			var link = $('<link />');
			link.attr('rel','stylesheet')
				.attr('type','text/css')
				.attr('href',cssUrl).appendTo($('head'));
        },
        registerListener:function(Listener){
            messageHandlers.push(Listener);
        },
        setHFToken:function(hftoken){
			hf_token = hftoken;
			$.ajaxSetup({
				headers:{'hf-token':hf_token}
			});
        },
        submitForm:function(uri, formdata, options){
        	if(options){
        		$.extend(options.headers, {'hf-token':'hf_token'});
        	}
        	var opt = {
        		headers:{},
        		onProgress:function(data, httpCode){},
        		onSuccess:function(data, httpCode){}
        	};
			opt = $.extend(opt,options || {});
            var xhr = new XMLHttpRequest();
            xhr.open("POST", uri, true);
            /* set header */
            for(var header in opt.headers){
            	xhr.setRequestHeader(header, opt.headers[header]);
            }
            xhr.onreadystatechange = function(){
                if (xhr.readyState == 4){
					opt.onSuccess(xhr.responseText, xhr.status);
                }
            };
            xhr.upload.addEventListener("progress", function(e) {
				opt.onProgress(e);
			});
            xhr.send(formdata);
        }
    };
}());

peaxy.LayoutManager = (function(){
	return {
		init:function(){
			$("#peaxymenu").kendoMenu({
				select: function(e) {
			        if($(e.item).text() === 'Login'){
			         	account.login();
			        }else if($(e.item).text() === 'Create'){
			         	account.create();
			        }
			    }
			});
			this.resize();
			$(window).resize(peaxy.LayoutManager.resize);
			var menu = this.getMenu();
		},
		resize:function(){
			$('#workarea').height($(window).height() - $("#peaxy_header").height());
			$('#peaxy_primaryarea').height($('#workarea').height() - $('#peaxy_dashboardpanel').height());
		},
		getPrimaryArea:function(){
			return $('#peaxy_primaryarea');
		},
		getDashboardPanel:function(){
			return $('#peaxy_dashboardpanel');
		},
		getMenu:function(){
			return $("#peaxy_menu").data("kendoMenu");
		},
		setMenu:function(){

		},
		setting:function(){
			peaxy.Communication.getScript('js/peaxy.configuration.js',function(){
				var config = new peaxy.Configuration();
				config.initialSettings();
			});
		}
	}
}());

peaxy.Help = (function(){
	peaxy.Communication.getFile('help/Hyperfiler_tool_tips.xml',function(data){
		var root = $(data);
		$('*').on('mouseover','.help, .input-help', function(event) {
		    var self = $(this), tooltip;
		    if(!self.data("help-url")) return;
		    if(!$(this).data('kendoTooltip')){
			    $(this).kendoTooltip({
			    	position:'right',
				    content: function(e){
				    	var p = $('<p></p>');
				    	p.attr('style','width:240px;padding:5px;');
				    	var des = root.find(self.data("help-url").split('/').join(' > '));
				    	for(var i = 0,l = des.length - 1;i<l;i++){
				    		p.append($(des[i]).html()).append($('<br/>'));
				    	}
						p.append($(des[des.length - 1]).html());
				    	return p;
				    }
				}).data("kendoTooltip");
			}
			$(this).data('kendoTooltip').show($(this));
		});
	});
	return {
		register:function(helpUrl){
			$('#peaxy_current_help').addClass('help').data('help-url', helpUrl);
		}
	}
}());

peaxy.ModuleManager = (function(){
	var modules = {};
	return{
		register:function(moduleName, instance){
			modules[moduleName] = instance;
		},
		get:function(moduleName){
			return modules[moduleName];
		},
		del:function(moduleName){
			if(modules[moduleName])
				delete modules[moduleName];
		}
	}
}());

peaxy.app = (function(){
    return {
        main:function(){
        	function createConfig(handler){
				peaxy.Communication.getScript('js/peaxy.configuration.js',function(){
					handler(new peaxy.Configuration());
				});	
        	};
        	peaxy.LayoutManager.init();
			var account = new peaxy.AccountModule();
			peaxy.OOB.completion(function(data, httpcode){
        		if(data == true){ // not login and no user
        			peaxy.ModuleManager.register('first',true);
					account.create(function(user){
						//start config hf
						createConfig(function(config){
							config.setSysConfig({
					  			userName:user.userName,
					  			password:user.password,
					  			userEmail:user.userEmail
					  		});
					  		config.importLicense();
						});					
					});
				} else if (data == false){ // not login and has user
					peaxy.ModuleManager.register('first',false);
	        		account.login(function(user){
	        			createConfig(function(config){
					  		config.setSysConfig({
					  			userName:user.userName,
					  			password:user.password,
					  			userEmail:user.userEmail
					  		});
					  		config.importLicense();
	        			});
					});
	        	} else {
					peaxy.ModuleManager.register('first',false);
					createConfig(function(config){
						config.importLicense();
					});
	        	}
        	});
        }
    };
}());

$(function(){
	peaxy.app.main();
})