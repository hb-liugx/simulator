var peaxy = peaxy || {};
peaxy.PoliciesManagement = kendo.Class.extend({
	init:function(opts){
		var self = this;
		self._ri = -1;
		//init base on 
		$.extend(this._options,opts);
		
		this._panel = this._options.l_panel;
		this._right = this._options.r_panel;
		
		var _tmp = $(self._tpl.html);
		this._panel.append(_tmp);

        _tmp.find('.policies_count').html(self._options.policies.length);
		if(self._options.policies.length){
			_tmp.find('.a-si-Data-Policies').addClass('p-si-Data-Policies-Drawer-Selected');
		}else{
			_tmp.find('.a-si-Data-Policies').addClass('p-si-Data-Policies-Drawer-Disabled');
		}
		// alert(this._panel.height());
		this._panel.find('.ccenter').css('height',(self._panel.height()- 244)+'px'); // dynamic height 
		//_tmp.find('.ccenter').css('height',(this._panel.height()- 224)+'px');
		_tmp.find('.a-si-Data-Policies').bind({
			click:function(e){
				self._sh ? self._right.show() :self._right.hide();
				self._sh = !self._sh;
			}
		});
		this._ctrl_panel = _tmp.find('.p-control-panel');
		this._adds_panel = _tmp.find('.p-addition-panel');


		//filter data to init base on 
		var bases = ['New Policy'];
		$.map(self._options.policies,function(elem){bases.push(elem.name); });
		self.switchpolicy(_tmp.find('.base_on'),bases);
		self._act_create(); 
		self._act_create_and();
		self.keys =[]; // reset order list   

		//right list box ----------------------------------------------------
		self.rendertable();
		//	----------------------------------------------------------------------------------------------
		// add save button
		_tmp.find('.save-button').bind({
			click:function(e){
				var obj = {};
				obj.id  =  0 ; 
				obj.name = $.trim(self._panel.find('input.policy_name').val());
				//check name is null ?? and set some error msg 
				self._panel.find('.p-errmsg').remove();
				if($.trim(obj.name).length == 0){
					$('<span class="p-errmsg"><br/>Please input a correct policy name. </span>').insertAfter(self._panel.find('input.policy_name'));
					return;
				}


				obj.condition=[];
				obj.ands = [];
				obj.others=[];
				//base on get ok  
				// 
				var _when_data_tmp = self._ctrl_panel.find('input._when_data').data('kendoDropDownList').value(); 
				if('is created' == _when_data_tmp){
					//when data begin 
					obj.condition.push(_when_data_tmp);
					//when data end 
					self._ctrl_panel.find('input._create_and').each(function(e){
						var _m = $(this).data('kendoDropDownList').value() ;
						var _t = $(this).closest('div.p-title3');
						obj.ands.push({
							key:_m,
							vals:[_t.find('input.p-input').val(), 'created in directory' == _m ? _t.find('input[name="sub_directory"]').prop('checked'):'']
						});
					});
					// ands end 
					obj.others.push({
						key:'Store it on',
						type:'dropdown',
						val:self._adds_panel.find('input._store_it').val()
					});
					//end others 
					obj.description = self._adds_panel.find('.p-textbox').val();
				}else{
					//when data begin
					obj.condition.push(_when_data_tmp,[self._ctrl_panel.find('input.p-input:first').val()]);
					//when data end 
					self._ctrl_panel.find('input._no_modify_and').each(function(e){
						var _m = $(this).data('kendoDropDownList').value() ;
						var _t = $(this).closest('div.p-title3');
						obj.ands.push({
							key:_m,
							vals:[_t.find('input.p-input').val()]
						});
					});
					//ands end 
					self._calender.execute =  self._adds_panel.find('input[name="execute_radio"]:checked').val();
					obj.schedule = self._calender;
					//others
					// 		others:[				
					// 	{key:'And is in',type:'dropdown',val:'C'},
					// ]
					// self.dropdown(_tmp.find('._no_modify_and_in') , self._options.store_class_lst , self._options.store_class_lst.indexOf(obj.others[0].val)); 
					// self.dropdown(_tmp.find('._no_modify_move_to') , self._options.store_class_lst);
					var t1 = self._adds_panel.find('input._no_modify_and_in').data('kendoDropDownList').value(); 
					//var t2 = self._adds_panel.find('input._no_modify_move_to').data('kendoDropDownList').value(); 

					obj.others.push({
						key:'And is in',
						type:'dropdown',
						val:t1
					});
					//end schedule
					obj.description = self._adds_panel.find('.p-textbox').val(); 
					// end description
				}
				//console.log(obj);

				//check is modify or create 
				//self._options.store_class_lst.push(obj.name); // no add to store it on 

				if(self._ri > -1){
					obj.id = self._options.policies[self._ri].id;
					self._options.policies[self._ri] = obj;
				}else{
					self._options.policies.push(obj);
				}				
				//reload policies management
				self.render();
				self._options.onConfig(self._options.policies);
				// //console.log(self._options.policies);
			}
		});
	},
	noPolicies:function(){
		var self = this ; 		
		var _tmp = $(self._tpl._wd);
		self._ctrl_panel.append(_tmp);
		self.dropdown(_tmp.find('._when_data'),self._lst._when_data)
		self._act_create_and();
	},
	setPolicy:function(viewMode,edit){
		var self = this; 
		//add buttons and change some text  
		if(edit){
			self._panel.find('.cancel_btn').remove();
			self._panel.find('.save-button').html('SAVE CHANGES');
			self._panel.find('.status').html('EDIT DATA POLICY  <hr class="p-hr-blank">'+viewMode.name);
			$('<button class="s-button cancel_btn" style="margin-right:10px;">CANCEL</button>').insertBefore(self._panel.find('.save-button'));
		}
		//

		self._panel.find('.policy_name').val(viewMode.name);
		if('is created' == viewMode.condition[0]){
			self._act_create(viewMode);			
			$.map(viewMode.ands,function(item){
				if('created in directory' ==  item.key){
					self._act_create_and_dir(self._lst._creat_and.indexOf(item.key),item.vals[0],item.vals[1]);
				}else{
					self._act_create_and(self._lst._creat_and.indexOf(item.key),item.vals.join(''));
				}
			});
		}else{
			self._act_no_modify(viewMode);
		}
	},
	dispatch:function(obj){
		var self = this;
		var tmp = $.type(obj) == 'string' ?  obj :obj.val();
		//console.log(tmp);

		switch(tmp){
			case 'is created':
				self._act_create();
			case '_opt_creat_and':
				self._act_create_and();
				break;
			case 'created in directory':
				self._act_create_directory(obj);
				break;
			case 'is created by user group':				
			case 'has file extension':
				self._act_create_no_directory(obj);
				break;
			case 'has not accssed in':
			case 'has a file size(int kb)':
			case 'has not been modified in':
				// self._act_no_modify(obj);
				break;
			case '_act_and_modify':
				// self._act_and_modify();
				break;
		}
	},
	render:function(){
		var self = this ; 		
		self._panel.empty();
		self.init();
	},
	
	rendertable:function(){
		var self  = this; 
		var lists = [];
		var _tbl  = $(self._tpl._tables);
		self._right.getContainer().html(_tbl);
		_tbl.find('.policies_count').html(self._options.policies.length) ;//find('.policies_count').html(self._options.policies.length);
		var opt = _tbl.find('.policies_list');

		_tbl.find('.isdone').bind({
			click:function(e){
				self._right.hide();
			}
		});
		_tbl.find('.isdelete').bind({
			click:function(e){
				self._ri  =  -1;
				var count = -1 ; 
				opt.find('input[name="policies_list_checkbox"]:checked').each(function(e){
					// //console.log(self._options.policies);
					var c =  $(this).closest('tr').attr('id') ;
					if(count == -1 || c < count ){ //first time 
						self._options.policies.splice(c,1);
						count = c ; 
					}else{
						self._options.policies.splice(c-1,1);
						count = c-1 ; 
					}
				});
				self.render();
			}
		});
		//generate html templates   
		
		 $.map(self._options.policies,function(elem,i){
		 	self.keys.push(elem.name);
		 	lists.push({ id:elem.id, name:elem.name,
		 		detail:function(){
		 			var html = 'when data ';
		 			html +=  'is created' ==  elem.condition[0] ? elem.condition[0] : elem.condition[0] +' <strong>'+ elem.condition[1]+'</strong>';
		 			$.map(elem.ands,function(item){
		 					if('created in directory' ==  item.key){
		 						html += ' and ' + item.key +' <strong>'+ item.vals[0] +'</strong>'+ (item.vals[1] ? ' and apply to subdirectories ' :'');
		 					}else{
			 					html += ' and ' + item.key +' <strong>'+ item.vals.join('')+'</strong>';
		 					}
		 				});
		 			// schedule
		 			if('is created' != elem.condition[0] ){ 
		 				html += ', schedule: ';
		 				for( k in elem.schedule){
		 					html += elem.schedule[k] ? ' '+k + ' <strong>'+elem.schedule[k]+'</strong>'  : '';
		 				}
		 			}	
		 			html += ',';
		 			$.map(elem.others,function(item){
		 				html += ' and '+ item.key + ' <strong>' + item.val+'</strong>' ; 
		 			});
		 			html += ', '+elem.description;
		 			return html;
		 		}()
		 	});
		 });

		self.keys.sort(); // sort it 
		$.map(self.keys,function(key){
			$.map(lists,function(item,i){
				if (key == item.name){
					opt.append('<tr id="'+i+'" ><td style="width:20px"><input  type="checkbox" name="policies_list_checkbox"><label for="checkbox-1"></label></td><td class="p-grey-repeater" style="width:80px;padding:0 10px;">'+item.name+'</td><td style="padding: 0 10px;">'+item.detail+'</td></tr>');
				}
			})
		});

		// $.map(lists,function(list,i){
		// 	opt.append('<tr id="'+i+'"><td style="width:10px"><input  type="checkbox" name="policies_list_checkbox"><label for="checkbox-1"></label></td><td style="width:80px">'+list.name+'</td><td>'+list.detail+'</td></tr>');
		// });
		opt.find('tr').each(function(){
			$(this).bind({
				click:function(e){
					var that = this; 
					opt.find('tr').removeClass('p-blue-repeater');
					$(this).addClass('p-blue-repeater');
					self._ri = $(this).attr('id');
					var po  =  self._options.policies[self._ri];
					self._panel.find('input.base_on').data('kendoDropDownList').value(po.name);
					self.setPolicy(po,true);

					// _tbl.find('.policies_list_head').find('input').prop("checked", false); // uncheck all checkbox 
					// opt.find('input').each(function(e){$(this).prop("checked", false);});  // uncheck other checkbox 
					// $(that).find('input').prop("checked", true); // check this checkbox
					// self._ri = $(that).attr('id');
					// var p = self._options.policies[self._ri];
					// self._panel.find('input.base_on').data('kendoDropDownList').value(p.name);	
					// self.setPolicy(p);	
				}
			});
		});
		_tbl.find('.policies_list_head').find('input').bind({
			change:function(e){
				opt.find('input').prop('checked',$(this).prop('checked'));
			}
		});

		_tbl.find('.sortit').bind({
			click:function(e){
				self.keys.reverse();
				if( _tbl.find('.p-sort').hasClass('p-si-Dark-Arrow-Down')){
					_tbl.find('.p-sort').removeClass('p-si-Dark-Arrow-Down');
					_tbl.find('.p-sort').addClass('p-si-Dark-Arrow-Up');
				}else{
					_tbl.find('.p-sort').removeClass('p-si-Dark-Arrow-Up');
					_tbl.find('.p-sort').addClass('p-si-Dark-Arrow-Down');
				}
				opt.empty();
				$.map(self.keys,function(key){
					$.map(lists,function(item,i){
						if (key == item.name){
							opt.append('<tr id="'+i+'" ><td style="width:20px"><input  type="checkbox" name="policies_list_checkbox"><label for="checkbox-1"></label></td><td class="p-grey-repeater" style="width:80px;padding: 0px 10px;">'+item.name+'</td><td style="padding: 0 10px;">'+item.detail+'</td></tr>');
						}
					})
				});

				opt.find('tr').each(function(){
					$(this).bind({
						click:function(e){
							var that = this; 
							opt.find('tr').removeClass('p-blue-repeater');
							$(this).addClass('p-blue-repeater');
							self._ri = $(this).attr('id');
							var po  =  self._options.policies[self._ri];
							self._panel.find('input.base_on').data('kendoDropDownList').value(po.name);
							self.setPolicy(po,true);

						}
					});
				});

			}
		});

	},
	dropdown:function(el,db,id){
		var self = this ;
		el.kendoDropDownList({dataSource: db, index: id,change:function(){self.dispatch(el); }});
	},
	switchpolicy:function(el,db,id){
		var self = this ;
		el.kendoDropDownList({dataSource: db, index: id,change:function(){
			if('New Policy' == el.val()){
				self._ri = -1 ; // new policy 
				self._act_create();
				self._act_create_and();
				$('#policy_name',self._panel).val('');
				self._panel.find('.cancel_btn').remove();
				self._panel.find('.save-button').html('CREATE DATA POLLICY');
				//
				self._panel.find('.status').html('CREATE NEW DATA POLICY');

			}else{
				
				$.map(self._options.policies,function(policy,i){
					if(el.val() == policy.name){
						self._ri = -1;
						
						self.setPolicy(policy);
					}
				});
			}
		}});	
	},
	_act_no_modify:function(obj){
		var self = this; 
		if(obj instanceof $ && '_when_data' == obj.attr('class')){ // when swith from no create here execute 
			if(self._ctrl_panel.find('.p-title3:first:has(.p-input)').length == 0){
				self._adds_panel.empty();
				self._ctrl_panel.find('div.p-title3:gt(0)').remove();
				var _tmp = $(self._tpl._no_modify);
				self._ctrl_panel.find('div.p-title3:eq(0)').append(_tmp);
				self._act_sub('_act_and_modify');
				_tmp = $(self._tpl._no_modify_inmove);
				self._adds_panel.html(_tmp);				
				self.dropdown(_tmp.find('._no_modify_and_in'),self._options.store_class_lst);
				// in order to change modify value  
				// el.kendoDropDownList({dataSource: db, index: id,change:function(){self.dispatch(el); }});
				_tmp.find('._no_modify_move_to').kendoDropDownList({
					dataSource:self._options.store_class_lst,
					id:0,
					change:function(){
						_tmp.find('input._no_modify_and_in').data('kendoDropDownList').value(this.value());
					}
				});
				// self.dropdown(_tmp.find('._no_modify_move_to'),self._options.store_class_lst);
				_tmp.find('.p-si-Calender').bind({
					click:function(e){self._wrap_tooltip(e.target); }
				});
			}
		}
		if('condition' in obj){// view model
			self._ctrl_panel.find('div.p-title3:gt(0)').remove();
			self._ctrl_panel.find('div.p-title3:eq(0)>.k-dropdown~').remove(); // if already select view model via dropdown list  ,and slelect view model again  .execute here 
			
			var _tmp = $(self._tpl._no_modify);
			self._ctrl_panel.find('div.p-title3:eq(0)').append(_tmp);
			self._ctrl_panel.find('input._when_data').data("kendoDropDownList").value(obj.condition[0]);

			self._ctrl_panel.find('.p-input:last').val(obj.condition[1] ? obj.condition[1]:'');
			//ands 
			$.map(obj.ands,function(elem){
				self._act_and_modify(elem);
			});

			_tmp = $(self._tpl._no_modify_inmove);
			self._adds_panel.html(_tmp);



			// set execute radio button   
			_tmp.find('input[name="execute_radio"][value="'+obj.schedule.execute+'"]').prop('checked',true);
			_tmp.find('.p-textbox').val(obj.description); // set description informations 
			// sche   schedule // start:true, // time: // execute:true, // date:'10/11/2000', // zone:'PDT', // repeat:'Never'

			//set and is in  value   
			if(obj.others.length){
				self.dropdown(_tmp.find('._no_modify_and_in') , self._options.store_class_lst , self._options.store_class_lst.indexOf(obj.others[0].val)); 
			}
			//self.dropdown(_tmp.find('._no_modify_move_to') , self._options.store_class_lst);
			_tmp.find('._no_modify_move_to').kendoDropDownList({
					dataSource:self._options.store_class_lst,
					id:0,
					change:function(){
						_tmp.find('input._no_modify_and_in').data('kendoDropDownList').value(this.value());
					}
			});
			
			//set schedule to calender 
			self._calender =	obj.schedule;

			_tmp.find('.p-si-Calender').bind({
				click:function(e){self._wrap_tooltip(e.target,obj.schedule); }
			});
		}
	},
	_wrap_tooltip:function(elem,schedule){
		var self = this; 

		//console.log(schedule);
		var tmp = $(self._tpl._calender);
		tmp.find('.calender').kendoCalendar();
		if(schedule){			
			tmp.find('.calender').data('kendoCalendar').value(new Date(schedule.date));
		}

		var _tmp = schedule ? self._lst._time.indexOf(schedule.time) : 0 ;

		tmp.find('._calender_time').kendoDropDownList({
			dataSource:self._lst._time,
			index:_tmp
		});

		_tmp =  schedule ? self._lst._zone.indexOf(schedule.zone) : 0 ; 
		tmp.find('._calender_zone').kendoDropDownList({
			dataSource:self._lst._zone,
			index:_tmp
		});

		_tmp = schedule ? self._lst._repeat.indexOf(schedule.repeat) : 0 ; 
		tmp.find('._calender_Repeat').kendoDropDownList({
			dataSource:self._lst._repeat,
			index:_tmp
		});
		peaxy.tooltipDialog($(elem),{
				position:'right',
          		content:tmp,
      			actions:[
                {name:'No',action:function(){

                }},
                {name:'Yes',action:function(){
                	self._calender.start =  tmp.find('input[name="start_radio"]:checked').val();
                	self._calender.time = tmp.find('input._calender_time').val();
                	self._calender.zone = tmp.find('input._calender_zone').val();
                	self._calender.repeat = tmp.find('input._calender_Repeat').val();
                	self._calender.date =  tmp.find('.calender').data('kendoCalendar').value().toUTCString();
                }}
              ]
        	});      	
	},
	_act_and_modify:function(elem){
		var	self = this;
		var _tmp = $(self._tpl._no_modify_and);
		self._ctrl_panel.append(_tmp);
		self.dropdown(_tmp.find('._no_modify_and'),self._lst._not_modify, elem ? self._lst._not_modify.indexOf(elem.key) : 0);
		if(elem){
			self._ctrl_panel.find('.p-input:last').val(elem.vals.join());			
		}
		self._act_sub('_act_and_modify');
	},
	_act_create:function(viewMode){

		var	 self = this;
		// check exist _when_data
		if($('.p-title3:first:has(._when_data)',self._ctrl_panel).length == 0){ // if set policy and there is no when_data html 
			var _tmp = $(self._tpl._wd);
			self._ctrl_panel.append(_tmp);
			self.dropdown(_tmp.find('._when_data'),self._lst._when_data);
		}
		//check view model and set value to it 		
		self._ctrl_panel.find('input._when_data').data("kendoDropDownList").value(viewMode ? viewMode.condition[0]:'is created');
		self._ctrl_panel.find('.p-title3:gt(0)').remove();
		self._ctrl_panel.find('.p-title3:first>.k-dropdown~').remove();
		// add store it and descrptions 
		var _tmp = $(self._tpl._wd_des);
		self._adds_panel.html(_tmp);
		self.dropdown(_tmp.find('._store_it'),self._options.store_class_lst,self._options.store_class_lst.indexOf(viewMode ? viewMode.others[0].val :self._options.store_class_lst[0]));
		// add description informations
		_tmp.find('.p-textbox').val(viewMode ? viewMode.description : '');

	},
	_act_create_and:function(id,val){
		var self = this ; 
		var _tmp = $(self._tpl._wd_and);
		_tmp.find('.p-input').val(val);
		self._ctrl_panel.append(_tmp);
		self.dropdown(_tmp.find('._create_and'),self._lst._creat_and,id);
		self._act_sub('_opt_creat_and');
	},
	_act_create_and_dir:function(id,val,checked){
		var self = this ; 
		var _tmp = $(self._tpl._wd_and);
		_tmp.find('.p-input').val(val);
		self._ctrl_panel.append(_tmp);
		self.dropdown(_tmp.find('._create_and'),self._lst._creat_and,id);
		self._act_sub('_opt_creat_and');
		self._act_create_directory(_tmp,checked);

	},
	//obj is dropdown element input
	_act_create_directory:function(obj,val){
		var	self = this;
		var root = obj.closest('div.p-title3');
		root.find('.p-input~').remove();
		root.append(self._tpl._wd_and_directory);	
		root.find('input[name="sub_directory"]').prop('checked',val);
	},
	_act_create_no_directory:function(obj){
		var self = this; 
		obj.closest('div.p-title3').find('.p-input~').remove();
	}
	,
	_act_sub:function(cmd){
		var self = this;
		// add some action to filter some special events
		self._ctrl_panel.find('.p-si-Add:not(:last)').hide();
		// end this options 
		self._ctrl_panel.find('.p-si-Remove:last').bind({
			click:function(e){
				$(this).closest('div.p-title3').remove();
				self._ctrl_panel.find('.p-si-Add:last').show();
			}
		});
		self._ctrl_panel.find('.p-si-Add:last').bind({
			click:function(e){
				self.dispatch(cmd);
				$(this).hide();
			}
		});
	},
	_options:{},
	_ctrl_panel:null,
	_adds_panel:null,
	_panel:null,
	_when_data:null,
	_calender:{
		start:false,
		date:null,
		time:null,
		zone:null,
		repeat:'Never',
		execute:'Automatically'
	},
	_ri: -1,
	_sh:true,
	keys:[],
	_lst:{
		_when_data:['is created'/*,'has not been modified in','has not accssed in','has a file size(int kb)'*/],
		_creat_and:['has file extension','created in directory','is created by user group'],
		_not_modify:['has not been modified in','has not accssed in','has a file size(int kb)'],
		_time:['1:00 AM', '1:15 AM', '1:30 AM', '1:45 AM', '2:00 AM', '2:15 AM', '2:30 AM', '2:45 AM', '3:00 AM', '3:15 AM', '3:30 AM', '3:45 AM', '4:00 AM', '4:15 AM', '4:30 AM', '4:45 AM', '5:00 AM', '5:15 AM', '5:30 AM', '5:45 AM', '6:00 AM', '6:15 AM', '6:30 AM', '6:45 AM', '7:00 AM', '7:15 AM', '7:30 AM', '7:45 AM', '8:00 AM', '8:15 AM', '8:30 AM', '8:45 AM', '9:00 AM', '9:15 AM', '9:30 AM', '9:45 AM', '10:00 AM', '10:15 AM', '10:30 AM', '10:45 AM', '11:00 AM', '11:15 AM', '11:30 AM', '11:45 AM', '12:00 AM', '12:15 AM', '12:30 AM', '12:45 AM', '1:00 PM', '1:15 PM', '1:30 PM', '1:45 PM', '2:00 PM', '2:15 PM', '2:30 PM', '2:45 PM', '3:00 PM', '3:15 PM', '3:30 PM', '3:45 PM', '4:00 PM', '4:15 PM', '4:30 PM', '4:45 PM', '5:00 PM', '5:15 PM', '5:30 PM', '5:45 PM', '6:00 PM', '6:15 PM', '6:30 PM', '6:45 PM', '7:00 PM', '7:15 PM', '7:30 PM', '7:45 PM', '8:00 PM', '8:15 PM', '8:30 PM', '8:45 PM', '9:00 PM', '9:15 PM', '9:30 PM', '9:45 PM', '10:00 PM', '10:15 PM', '10:30 PM', '10:45 PM', '11:00 PM', '11:15 PM', '11:30 PM', '11:45 PM', '12:00 PM', '12:15 PM', '12:30 PM', '12:45 PM'],
		_zone:['PDT','PST'],
		_repeat:['Never','Always']
	},
	_tpl:{
		_wd:
			'<div class="p-title3">'+
				'<hr class="p-hr-blank">'+
				'<span class="p-label">When data:</span>'+
				'<span class="help"></span>'+
				'<hr class="p-hr-blank"/>'+
				'<input class="_when_data"  style="width: 223px;"/>'+
			'</div>'
		,
		_wd_and:
			'<div class="p-title3">'+
					'<hr class="p-hr-blank">'+
					'<span class="p-label">AND:</span>'+
					'<hr class="p-hr-blank"/>'+
					'<input class="_create_and" style="width: 223px;"　/>'+
					'<span class="p-secondary-icons p-si-Remove" style=""></span>'+
					'<span class="p-secondary-icons p-si-Add" style="" ></span>'+
					'<input  class="p-input"  type="text">'+
			'</div>',
		_wd_and_directory:
			'<br/>'+
			'<input  name="sub_directory" type="checkbox">'+
			'<label>Apply to subdirectories</label>'
			,
		_wd_des:
			'<div class="p-title3" >'+
                '<hr class="p-hr-blank">'+
                '<span class="p-label">Store it on:</span>'+
                '<hr class="p-hr-blank"/>'+
                '<input   class="_store_it" name="store_it"   style="width: 223px;"/>'+
            '</div>'+
			'<div class="p-title3">'+
				'<hr class="p-hr-blank">'+
				'<div class="p-title3">DESCRIPTION<textarea  class="p-textbox"  ></textarea></div>'+
				'<hr class="p-hr-blank">'+
			'</div>'
			,
		_no_modify:
			'<span class="p-secondary-icons p-si-Remove" style=""></span>'+
			'<span class="p-secondary-icons p-si-Add" style="" ></span>'+
			'<input  class="p-input"  type="text">'
			,
		_no_modify_and:
			'<div class="p-title3">'+
				'<hr class="p-hr-blank">'+
				'<span class="p-label">AND:</span>'+
				'<hr class="p-hr-blank"/>'+
				'<input class="_no_modify_and"/>'+
				'<span class="p-secondary-icons p-si-Remove" style=""></span>'+
				'<span class="p-secondary-icons p-si-Add" style="" ></span>'+
				'<input  class="p-input"  type="text">'+
			'</div>'
			,
		_no_modify_inmove:
			'<div class="p-title3">'+
				'<hr class="p-hr-blank">'+
				'<span class="p-label">And is in:</span>'+
				'<hr class="p-hr-blank"/>'+
				'<input class="_no_modify_and_in"/>'+
			'</div>'+
			'<div class="p-title3">'+
				'<hr class="p-hr-blank">'+
				'<span class="p-label">Move to:</span>'+
				'<hr class="p-hr-blank"/>'+
				'<input class="_no_modify_move_to"/>'+
			'</div>'+
			'<hr class="p-hr-blank">'+
			'<div class="p-hr-s"></div>'+
			'<div class="p-title3">'+
				'<hr class="p-hr-blank">'+
				'<span class="p-label">SCHEDULING</span>'+
				'<hr class="p-hr-blank"/>'+
				'<span class="p-label">Start Policy:</span>'+
				'<span class="p-label">Immediately</span>'+
				'<span class="p-secondary-icons p-si-Calender" ></span>'+
				'<hr class="p-hr-blank"/>'+
				'<span class="p-label">Execute:</span>'+
				'<input name="execute_radio"   checked="checked" type="radio" value="Automatically"><label for="radio-1">Automatically</label>'+
				'<input name="execute_radio"   type="radio" value="Manually"><label for="radio-1">Manually</label>'+
			'</div>'+
			'<hr class="p-hr-blank">'+
			'<div class="p-hr-s"></div>'+
			'<div class="p-title3">'+
				'<hr class="p-hr-blank">'+
				'<div class="p-title3">DESCRIPTION</div>'+
				'<textarea  class="p-textbox" ></textarea>'+
			'</div>',
		_calender:
			'<style>.k-calendar{width:300px;}</style>'+
			'<div class="p-title3 " style="background:#FFF;">'+
				'<span class="p-title3">Start Immediately ?</span>'+
				'<hr class="p-hr-blank">'+
				'<input name="start_radio" value="false" checked="checked" type="radio"><label for="radio-1">No</label>'+
				'<input name="start_radio" value="true"  type="radio"><label for="radio-1">Yes</label>'+
				'<div class="p-hr-s"></div>'+
				'<div class="calender"></div>'+
				'<hr class="p-hr-blank">'+
				'<div class="p-hr-s"></div>'+
				'<span class="p-title3">Set Time:</span>'+
				'<input class="_calender_time" style="width:87px"/>  '+
				'<input class="_calender_zone" style="width:80px"/>'+
				'<hr class="p-hr-blank">'+
				'<span class="p-title3">Repeat:&nbsp;&nbsp;</span>'+
				'<input class="_calender_Repeat"/>'+
				'<hr class="p-hr-blank">'+
			'</div>',
		_tables:
			'<div class="p-content">'+
			'<div class="p-title3">'+
			'DATA POLICIES <span class="policies_count"></span>'+
			'<span style="float:right"><button class="s-button isdone">DONE</button></span>'+
			'</div>'+
			'<div class="p-title3" style="clear:both;margin-top:20px;">'+
			'<span style="float:right">For Selected Items　'+
			'<button class="s-button isdelete">DELETE</button></span>'+
			'</div>'+
			'<hr class="p-hr-blank">'+
			'<hr class="p-hr-blank">'+
			'<style>.policies_td td{ border-bottom:1px solid gray; padding:10px 0; }</style>'+
			'<table style="width:99%;" cellspacing="0" class="policies_td">'+
				'<thead class="policies_list_head">'+
					'<tr>'+
						'<td><input type="checkbox"><label for="checkbox-1"></label></td>'+
						'<td class="sortit" style="padding:0 10px; cursor:pointer;">Name<span class="p-secondary-icons p-sort p-si-Dark-Arrow-Down"></span></td>'+
						'<td style="padding: 0 10px;">DETAILS</td>'+
					'</tr>'+
				'</thead>'+
				'<tbody class="policies_list">'+
				'</tbody>'+
			'</table>'+
			'</div>',
		html:'<style>.p-title1,.p-title2,.p-hr-l,.p-hr-s,.p-hr-blank{margin-bottom:8px;}</style>'+
			'<div >'+
				'<div class="p-title-header">'+
					'DATA MANAGEMENT POLICIES'+
					'<hr class="p-hr-blank">'+
					'<div class="p-desc" style="font-weight:normal;">'+
						'Specify whch data goes to which storage class by setting up data management policies.If you don'+'t create any data policies all of your data will be stored in your default storage class.'+
					'</div>'+
				'</div>'+
			'</div>'+
			'<!-- policies -->'+
			'<div style="margin: 0 15px;clear:both;line-height:24px;">'+
				'<span class="p-title3" >'+
					'POLICIES'+
				'</span>'+
				'<span class="p-secondary-icons a-si-Data-Policies" style="float:right;"></span>'+
				'<span class="p-title3 policies_count" style="float:right;"></span>'+

			'</div>'+
			'<hr class="p-hr-s">'+
			'<!-- center -->'+
			'<div class="ccenter" style="margin:2px 0px 2px 15px; overflow: scroll; overflow-x:hidden;" >'+
				'<div class="p-title3 status">'+
					'CREATE NEW DATA POLICY'+
				'</div>'+
				'<hr class="p-hr-l">'+
				'<div class="p-title3">'+
					'BASED ON'+
					'<span class="help"></span>'+
					'<hr class="p-hr-blank">'+
					'<!-- select element -->'+
					'<input  class="base_on" name="base_on"  style="width: 223px;" />'+
					'<hr class="p-hr-blank">'+
				'</div>'+
				'<hr class="p-hr-s">'+
				'<div class="p-title3">'+
					'POLLICY NAME'+
					'<hr class="p-hr-blank">'+
					'<input type="text" name="policy_name" class="policy_name" id="policy_name" class="p-input"  style="width: 217px;">'+
					'<hr class="p-hr-blank">'+
				'</div>'+
				'<hr class="p-hr-s">'+
				'<div class="p-title3">'+
					'DATA ASSIGNMENT'+
				'</div>'+
				'<div class="p-control-panel" >'+
				'</div>'+
				'<div class="p-addition-panel" >'+
				'</div>'+
			'</div>'+
			'<div class=" policies_manager" style="position:absolute; bottom:0; z-index:99999;  width:100%;height:60px;  background:#ECEDEE;text-align:center;"><div style="height:18px;"></div>'+
				'<button class="save-button">CREATE DATA POLLICY</button>'+
			'</div>'
	}
});