var peaxy = peaxy || {};

peaxy.UserAuthentication = {
    login:function(user,handler){
    	peaxy.Communication.post('/aaa/user/login/'+user.name+'/'+user.password,function(data, httpcode){
            if(httpcode != 401){
                peaxy.Communication.setHFToken(data.token || '');
            }
    		if(jQuery.isFunction(handler)) handler(toViewModel(data), httpcode);
    	},'json');
        function toViewModel(data){
            return data;
        }
    },
    createUser:function(user,handler){
		peaxy.Communication.put('/aaa/user/create',$.toJSON(user),function(data, httpcode){		
			if(jQuery.isFunction(handler)) handler(data, httpcode);
    	});
    },
    updateUserPassword:function(user,handler){
		peaxy.Communication.put('/aaa/user/updatepwd',$.toJSON(user),function(data, httpcode){
			if(jQuery.isFunction(handler)) handler(data, httpcode);
    	});
    },
    getAdminEmail:function(handler){
        peaxy.Communication.get('/aaa/adminaddress',function(data, httpcode){
            if(jQuery.isFunction(handler)) handler(toViewModel(data), httpcode);
        });
        function toViewModel(data){
            return data;
        }
    }
}