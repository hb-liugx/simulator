var peaxy = peaxy || {};

peaxy.ConfigInterface = {
    getMessage:function(id,handler){
        peaxy.Communication.post('/conf/message/get/'+id,function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        
        function toViewModel(data){
            return data;
        }
	},
	setDns:function(dsnConfig,handler){
        peaxy.Communication.post('/conf/dns/set',$.toJSON(toModel(dsnConfig)),function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
    getDns:function(handler){
		peaxy.Communication.get('/conf/dns/get', function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        function toViewModel(data){
            return data;
        }
	},
    setEmail:function(emailConfig,handler){
        peaxy.Communication.post('/conf/email/set',$.toJSON(toModel(emailConfig)),function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
    getEmail:function(handler){
		peaxy.Communication.get('/conf/email/get', function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        function toViewModel(data){
            return data;
        }
	},
    setHs:function(hsConfig,handler){
        peaxy.Communication.post('/conf/hs/set',$.toJSON(toModel(hsConfig)),function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
    getHs:function(handler){
		peaxy.Communication.get('/conf/hs/get', function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        function toViewModel(data){
            return data;
        }
	},
    setLog:function(logConfig,handler){
        peaxy.Communication.post('/conf/log/set',$.toJSON(toModel(logConfig)),function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
    getLog:function(handler){
		peaxy.Communication.get('/conf/log/get', function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        function toViewModel(data){
            return data;
        }
	},
    setNtp:function(ntpConfig,handler){
        peaxy.Communication.post('/conf/ntp/set',$.toJSON(toModel(ntpConfig)),function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
    getNtp:function(handler){
		peaxy.Communication.get('/conf/ntp/get', function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        function toViewModel(data){
            return data;
        }
	},
    setStats:function(statsConfig,handler){
        peaxy.Communication.post('/conf/stats/set',$.toJSON(toModel(statsConfig)),function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
    getStats:function(handler){
		peaxy.Communication.get('/conf/stats/get', function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(toViewModel(data),httpCode);
            }
        },'json');
        function toViewModel(data){
            return data;
        }
	},
    setKv:function(kvConfig,handler){
        peaxy.Communication.post('/conf/kv/set',$.toJSON(toModel(kvConfig)),function(data,httpCode){
            if(jQuery.isFunction(handler)){
                handler(data,httpCode);
            }
        },'json');
        
        function toModel(view){
            return view;
        }
	},
    getKv:function(key,handler){
		peaxy.Communication.get('/conf/kv/get/'+key, function(data,httpCode){
            handler(toViewModel(data),httpCode);
        },'json');
        function toViewModel(data){
            return data;
        }
	}
}