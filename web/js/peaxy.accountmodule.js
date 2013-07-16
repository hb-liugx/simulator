var peaxy = peaxy || {};

peaxy.AccountModule = kendo.Class.extend({
	init:function(){
		
	},
	login:function(handler){
		peaxy.Help.register('login/createinitialaccount/userlogin');
		var self = this;
		var userCount = 0;
		var passCount = 0;
		var dialog = this._createWindow();

		dialog.setOptions({
            height: "362px",
            width:'560px',
            resizable:false,
            modal:true
		});

		dialog.content(this.Template.getLoginPage());

		peaxy.UserAuthentication.getAdminEmail(function(email,httpcode){
			if(httpcode != 501){
				dialog.wrapper.find('#adminadress').attr('href','mailto:'+email).text(email);
			}
		});

		var validator = new peaxy.Check(),
			username = dialog.wrapper.find("#username"),
			password = dialog.wrapper.find("#password");

		validator.register(username, $("#userErrMsg"),function(value){
			if(peaxy.Validator.isEmpty(value)){
				return "Please enter user name.";
			}
			return true;
		});

		validator.register(password, $("#pwdErrMsg"),function(value){
			if(peaxy.Validator.isEmpty(value)){
				return "Please enter password.";
			}
			return true;
		});

		dialog.wrapper.find('button').click(function(){
			if(validator.validator()){
				var remembered = $("#remembered").prop("checked");
				peaxy.UserAuthentication.login({
					'name':encodeURIComponent($.trim(username.val()))
					,'password':encodeURIComponent($.trim(password.val()))},function(user, httpcode){
					if(httpcode == 200){
						$("#pwdErrMsg").text('Login success.');
						dialog.close();
						if($.isFunction(handler))
							handler(user);
					} else {
						$("#pwdErrMsg").text('Login failure.');
					}
				});
			}
			return;
		});

		dialog.open();
		dialog.center();
	},
	create:function(handler){
		peaxy.Help.register('login/createinitialaccount/createsuperuser');
		var self = this;
		var dialog = this._createWindow();
		dialog.content(this.Template.getCreatePage());

		dialog.setOptions({
            width:'560px',
            height:'428px',
            resizable :false,
            modal:true
		});
		
		var check  = new peaxy.Check(),
			username = dialog.wrapper.find('#username'),
			useremail = dialog.wrapper.find($('#useremail')),
			password = dialog.wrapper.find($('#password')),
			cpassword = dialog.wrapper.find($('#cpassword'));

		check.register(username,$('#userErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please enter user name.";
			}
			
			if(!peaxy.Validator.checkLengthRange(val,3,32)){
				return "The user name must be 3 to 32 characters in length.";
			}
			
			if(!peaxy.Validator.checkNamePattern(val)){
				return "Only letters [a-z, A-Z], numbers, periods and @ are valid characters.";
			}
			return true;
		});

		check.register(useremail,$('#emailErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please enter email.";
			}
			
			if(!peaxy.Validator.checkLengthRange(val,0,64)){
				return "The email address must be less then 64 characters in length.";
			}
			
			if(!peaxy.Validator.checkEmail(val)){
				return "Please enter valid email address.";
			}

			return true;
		});

		check.register(password, $('#pwdErrMsg'),function(val){
			if(peaxy.Validator.isEmpty(val)){
				return "Please enter password.";
			}
			
			if(!peaxy.Validator.checkLengthRange(val,8,64)){
				return "Password must contain at least eight characters.";
			}
			
			if(!peaxy.Validator.containCapital(val)
				|| !peaxy.Validator.containLowercase(val)
				|| !peaxy.Validator.containNumber(val)){
				return "Password must include a minimum of one capital letter, one lower case letter and a number digital";
			}

			return true;
		});

		check.register(cpassword,$('#cpwdErrMsg'),function(val){
			if($.trim(val) != $.trim(password.val()))
				return "Your Password entries do not match, please try again.";
			return true;
		});

		dialog.wrapper.find('button').click(function(){
			if(check.validator()){
				var user = {
					name: $.trim(username.val()),
					password: $.trim(password.val()),
					email: $.trim(useremail.val()),
					role: 'ADMIN'
				};
				peaxy.UserAuthentication.createUser(user/*,function(msg,httpcode){
					if(httpcode == 204){
						$('#cpwdErrMsg').text('Create user success.');
						if(jQuery.isFunction(handler)){
							handler({
								userName: $.trim(username.val()),
								password: $.trim(password.val()),
								userEmail: $.trim(useremail.val())
							},httpcode);
							dialog.close();
						}
					}else{
						$('#cpwdErrMsg').text('Create user failure.');
					}
				}*/);				
				if($.isFunction(handler)){
					handler({
						userName: $.trim(username.val()),
						password: $.trim(password.val()),
						userEmail: $.trim(useremail.val())
					});
				}
				dialog.close();
			}
		});
		dialog.open();
		dialog.center();
	},
	_createWindow:function(){
		var accountDialog = $('#accountDialog').length > 0 ? $('#accountDialog')
							:$('<div id="accountDialog"></div>').appendTo("body");
		if (!accountDialog.data("kendoWindow")){
			accountDialog.kendoWindow();
		}
		accountDialog.data("kendoWindow").setOptions({
			width:"560px",
			height:"448px",
			resizable:false,
			modal :true
		});
		accountDialog.data("kendoWindow").wrapper.find('.k-window-actions').empty();
		accountDialog.data("kendoWindow").center();
		return accountDialog.data("kendoWindow");
	},
	Template:(function(){
		var createPageContent = '<div id="createSuperAccount">\
			<div class="p-content">\
				<p class="p-title1">Create Architect Account</p>\
				<hr class="p-hr-l"/>\
				<div class="p-desc">You must create an Architect Account before setting up your first Hypefiler. Once you have created your first Hyperfiler, you can add users and assign roles in the Admin Tools section.</div>\
					<form>\
						<table width="100%">\
							<tr>\
								<td style="width:50%">\
									<label class="p-title3">Enter an Email Address</label>\
									<input type="text" id="useremail" class="p-input input-help" data-help-url="configuration/initialsetup/login/createinitialaccount/email"/>\
								</td>\
								<td>\
									<p id="emailErrMsg" class="p-errmsg"></p>\
								</td>\
							<tr>\
								<td>\
									<label class="p-title3">Enter a Username</label>\
									<input type="text" id="username" class="p-input input-help" data-help-url="configuration/initialsetup/login/createinitialaccount/username"/>\
								</td>\
								<td>\
									<p id="userErrMsg" class="p-errmsg"></p>\
								</td>\
							</tr>\
							<tr>\
								<td>\
									<label class="p-title3">Enter a Password</label>\
									<input type="password" id="password" class="p-input input-help" data-help-url="configuration/initialsetup/login/createinitialaccount/password"/>\
								</td>\
								<td>\
									<p id="pwdErrMsg" class="p-errmsg"></p>\
								</td>\
							</tr>\
							<tr>\
								<td>\
									<label class="p-title3">Confirm Password</label>\
									<input type="password" id="cpassword" class="p-input"/>\
								</td>\
								<td>\
									<p id="cpwdErrMsg" class="p-errmsg"></p>\
								</td>\
							</tr>\
							<tr>\
								<td>\
									<button type="button" class="s-button">SUBMIT</button>\
								</td>\
							</tr>\
						</table>\
					</form>\
			</div>\
		</div>';
		var loginPageContent = '<div id="login">\
			<div class="p-content">\
				<div class="p-title1">Hyperfiler Login</div>\
				<hr class="p-hr-l"/>\
				<div class="p-desc">Please contact the Architect Account holder for this Hyperfiler (<a id="adminadress" href="mailto:hyperfilerarchitectaccountholder@company.com">hyperfilerarchitectaccountholder@company.com</a>) to obtain login credentials or to reset your password.</div>\
					<form>\
						<table width="100%">\
							<tr>\
								<td colspan="2" class="tdforInput">\
									<p class="p-title3">Enter Username</p>\
									<input type="text" id="username" class="p-input input-help" data-help-url="configuration/accountsettings/accountinformation/username"/>\
								</td>\
								<td>\
									<p id="userErrMsg" class="p-errmsg"></p>\
								</td>\
							</tr>\
							<tr>\
								<td colspan="2">\
									<p class="p-title3">Enter Password</p>\
									<input type="password" id="password" class="p-input input-help" data-help-url="configuration/accountsettings/accountinformation/password"/>\
								</td>\
								<td>\
									<p id="pwdErrMsg" class="p-errmsg"></p>\
								</td>\
							</tr>\
							<tr>\
								<td class="beforeInput" colspan=2>\
									<input type="checkbox" id="remembered"/><label for="remembered" class="remLabel">Remember me on this computer</label>\
								</td>\
							</tr>\
							<tr>\
								<td colspan="2">\
									<button class="s-button" type="button" id="submit">SUBMIT</button>\
								</td>\
							</tr>\
						</table>\
					</form>\
			</div>\
		</div>';
		return {
			getCreatePage:function(){return createPageContent;},
			getLoginPage:function(){return loginPageContent;}
		}
	}()),
	destroy:function(){
		$("#accountDialog").data("kendoWindow").destroy();
	}
});