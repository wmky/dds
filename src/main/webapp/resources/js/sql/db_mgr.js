/**
 * 	预定义的变量
 */
var addwin = null;			//新增窗口
var editwin = null;			//修改窗口
var viewwin = null;			//查看窗口

var afp = null;				//新增表单panel
var efp = null;				//修改表单panel
var vp = null;				//详情panel
var store = null;			//全局store
var grid = null;			//全局grid

var viewgrid = null;		//查看grid
var viewstore = null;		//查看store
var record_start = 0;


var p1 = null; //查询的 form对象

var passflag = 0; //改sql是否测试通过的全局标识

var sqlform = null; //上传sql的form
var upload_view = null; //上传sql的window对象
var uploadsql_flag = 0;//上传sql的标识(0表示不上传，1表示上传)
var downloadsql_flag = 0;//下载sql文件的标识(0表示该条记录在数据库中没有sql文件，1表示有)

//------------------------------------------------------------------------------------
/*
 * 按钮触发函数
 */
var  database_value = "[" +
		"['mysql', 'mysql库', 'The database td']"  +
		"]";
database_value = eval(database_value);
var database_store = new Ext.data.ArrayStore({
        fields: ['id', 'discribe', 'content'],
        data : database_value
    });

    
//上传sql
saveSqlSubmit = function(id){
	//判断是否选择了一条记录
	//var id = sqlform.form.findField('uploadsql_id').getValue();						//id
	//alert(id);
	//return;
	//alert(mailstr);
		sqlform.form.findField('uploadsql_id').setValue(id);
		var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在上传sql...'});
		lm.show();
		sqlform.getForm().submit({   
            method : 'post',   
            url  : 'upLoad.itm?method=uploadSqlFile',   
              success: function(fp, o){   
              	 // alert(0);
              	  lm.hide();
              	  upload_view.hide();
                  Ext.Msg.alert('提示', o.result.msg);  
              },   
              failure: function(){   
              	 // alert(1);
              	  lm.hide();
                  Ext.Msg.alert('错误', '上传sql失败');   
              }   
            });  

				/*Ext.Ajax.request({
		            		method: 'post',
		                	url: 'upLoad.itm?method=uploadSqlFile',
		                	isUpload : true,   
                    		form : sqlform,   
		 					success:function(result){
		 							responseStr = result.responseText;
		 							alert(responseStr);
		 							lm.hide();
		 							if(responseStr=='9'){
		 								Ext.Msg.alert('提示','文件过大，请精简文件');
		 							}
		 							if(responseStr=='1'){
		 								Ext.Msg.alert('提示','上传sql成功');
		 								upload_view.hide();
		 							}else if(responseStr=='0'){
		 								Ext.Msg.alert('提示','上传sql失败');
		 							}
		 					},
		 					failure: function(result) {    
                             lm.hide(); 
                             Ext.Msg.alert('提示','上传sql失败');
                      }  
		      });*/
}
uploadSql = function (){

	//判断是否选择了一条记录
	/*if(grid.getSelectionModel().getCount()<1){
		Ext.MessageBox.alert('提示','请选择一条记录！');
	}
	else{*/
		
		//判断查看窗口是否已生成
		//var id = grid.getSelectionModel().getSelected().get('id');
		//var record = grid.getSelectionModel().getSelected();
		if(sqlform == null){
			sqlform = new Ext.form.FormPanel({
				fileUpload : true,
		        id:'sfp',
	        	frame: true,
		        labelWidth: 90,
		        labelAlign: 'right',
		        items:[ 
		        new Ext.form.Hidden({
			       							name:'uploadsql_id',
			       							id:'uploadsql_id'
				                	}),
	        	{
							 	  xtype: 'textfield',
							      fieldLabel: 'sql文件地址',
							      id: 'sql_file',
							      name: 'sql_file',
							      inputType:'file',
							      width:400 
						      }   
			        ],
		            bbar:[ {
		            			text:'确定',
		                		iconCls:'save',
		                		handler:function(){
		                			if(upload_view!=null&&upload_view.isVisible()){
		                					upload_view.hide();
											upload_view.setActive(true);
											uploadsql_flag = 1;
											var filePath = Ext.getCmp("sql_file").getValue(); 
											Ext.getCmp('dbip_add').setValue('sql文件名：'+filePath);
		                			}
		                		}
		                 },'-',{
		                		text:'取消',
		                		iconCls:'cancel',
		                		handler:function(){
		                				if(upload_view!=null&&upload_view.isVisible()){
		                					upload_view.hide();
											upload_view.setActive(true);
											//Ext.getCmp('dbip_add').setValue('');
		                				}
		                			}
		                  }]
		    	});
		}
			
	    	if (upload_view == null) {
				upload_view = new Ext.Window({
							id : 'upload_view',
							title : '上传sql文件',
							iconCls:'option',
				            layout:'fit',
							width : 550,
							height : 200,
							plain : true,
							autoScroll : true,
							modal : true, // 模态
							closeAction : 'hide', // 隐藏窗口；'close'为destroy窗口
							collapsible : true,
							items : sqlform
						});
			}
		
		
 	    
		var top = document.body.scrollTop
				+ (document.body.clientHeight / 2 - upload_view.height / 2);
		upload_view.setPosition(
				(document.body.clientWidth / 2 - upload_view.width / 2), top);
		upload_view.show();
		
	 
	//}

}

    
//定义查询条件
 var query_databaseId = new Ext.form.ComboBox({
											                        	fieldLabel: '连接数据库',
											                        	id: 'query_databaseId',
											                        	name: 'query_databaseId',
											                        	store: database_store,
												                        valueField:'id',				//combox id值
												                        displayField:'discribe',	 			//combox 显示值
												                        typeAhead: true,				//自动匹配。注：typeAheadDelay 参数--当typeAhead=true时 
												                        mode: 'local',					//采用本地数据加载，因combox在创建时，已加载了数据。这样可自动匹配数据
												                        triggerAction: 'all',
												                        emptyText:'-请选择连接的数据库-',
												                        width:60,
												                        value:'mysql',
												                        selectOnFocus:true
						                       					})

var query_dbname = new Ext.form.TextField({
				id:'query_dbname',
				emptyText:'-连接名称-',
				width:90
			});
var query_id = new Ext.form.TextField({
				id:'query_id',
				emptyText:'-id-',
				width:35
			});
var query_dburl =  new Ext.form.TextField({
				id:'query_dburl',
				emptyText:'-连接字符串-',
				width:90
			});
var query_date1 = new Ext.form.DateField({
				id:'query_date1',
				emptyText:'-请选择-',
				format:'Y-m-d'
			});
var query_date2 = new Ext.form.DateField({
				id:'query_date2',
				emptyText:'-请选择-',
				format:'Y-m-d'
			});
//重置模糊查询
dataReset = function(){
	Ext.getCmp('query_id').setValue('');
	Ext.getCmp('query_dbname').setValue('');
	Ext.getCmp('query_dburl').setValue('');
	Ext.getCmp('query_date1').setValue('');
	Ext.getCmp('query_date2').setValue('');
};
//测试sql
testSql = function(){
	
	var databaseId = afp.form.findField('databaseId').getValue();		//数据库
	var connname = afp.form.findField('connname_add').getValue();	// 主题
	var dbip = afp.form.findField('dbip_add').getValue();			//ruleSql
	var dbport = afp.form.findField('dbport_add').getValue();
	var dbname = afp.form.findField('dbname_add').getValue();
	var username = afp.form.findField('username_add').getValue();//excelName
	var password = afp.form.findField('password_add').getValue();//sheetName
	
	if(afp.form.isValid()){
		var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在测试连接...'});
		lm.show();
		Ext.Ajax.request({
			method: 'post',
			url: CTX + '/dbmgr/testConn.itm',
			params:{
				connname: connname,
        		dbip: dbip,
        		dbport:dbport,
        		dbname:dbname,
        		username:username,
        		password:password,
        		databaseId:databaseId
			},
			success:function(result){
				responseStr=Ext.util.JSON.decode(result.responseText); 
				lm.hide();
				if(responseStr.success){
					passflag = 1;
					Ext.Msg.alert('提示','连接成功');
				}else{
					passflag = 0;
					Ext.Msg.show({
                         title : '错误',
                         msg :"连接失败,请检查输入信息...",
                         buttons : Ext.Msg.OK,
                         icon : Ext.Msg.ERROR
                     });
				}
			}
		});
	}else{
		Ext.Msg.alert('提示',"请填写完整信息");
		return;
	}
};
//测试sql1
testSql1 = function(){
	if(efp.form.isValid()){
		var databaseId = efp.form.findField('databaseId1').getValue();
		var connname = efp.form.findField('connname_edit').getValue();
		var dbip = efp.form.findField('dbip_edit').getValue();		
		var dbport = efp.form.findField('dbport_edit').getValue();
		var dbname = efp.form.findField('dbname_edit').getValue();
		var username = efp.form.findField('username_edit').getValue();
		var password = efp.form.findField('password_edit').getValue();
		var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在测试连接...'});
		lm.show();
		Ext.Ajax.request({
			method: 'post',
			url: CTX + '/dbmgr/testConn.itm',
			params:{
				connname: connname,
        		dbip: dbip,
        		dbport:dbport,
        		dbname:dbname,
        		username:username,
        		password:password,
        		databaseId:databaseId
			},
			success:function(result){
				responseStr=Ext.util.JSON.decode(result.responseText); 
				lm.hide();
				if(responseStr.success){
					passflag = 1;
					Ext.Msg.alert('提示','连接成功');
				}else{
					passflag = 0;
					Ext.Msg.show({
                         title : '错误',
                         msg :"连接失败,请检查输入信息...",
                         buttons : Ext.Msg.OK,
                         icon : Ext.Msg.ERROR
                     });
				}
			}
		});
	}else{
		Ext.Msg.alert('提示',"请填写完整信息");
		return;
	}
}


saveSubmit = function(){
	var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在提交数据...'});
				//提交表单的信息 
				var databaseId = afp.form.findField('databaseId').getValue();		//数据库
				var connname = afp.form.findField('connname_add').getValue();	// 主题
				var dbip = afp.form.findField('dbip_add').getValue();			//ruleSql
				var dbport = afp.form.findField('dbport_add').getValue();
				var dbname = afp.form.findField('dbname_add').getValue();
				var username = afp.form.findField('username_add').getValue();//excelName
				var password = afp.form.findField('password_add').getValue();//sheetName
				
				lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: CTX + '/dbmgr/addOrEditDb.itm',
		                	params:{
		                		connname: connname,
		                		dbip: dbip,
		                		dbport:dbport,
		                		dbname:dbname,
		                		username:username,
		                		password:password,
		                		databaseId:databaseId
		                	},
		 					success:function(result){
		 							var respText = Ext.util.JSON.decode(result.responseText);                                                 
                             		var r = respText.success; 
                             		var id = respText.id; 
		 							lm.hide();
		 							if(r){//添加成功
		 								passflag = 0;
		 								addwin.hide();
		 								Ext.Msg.alert('提示','添加成功');
		 								store.load({params:{start:0, limit:10}});
		 							}else{
		 								Ext.Msg.alert('提示','添加失败');
		 							}
		 					}, 
		                    failure: function(resp,opts) { 
		                    		 //uploadsql_flag==0;
		                             Ext.Msg.alert('错误', "添加失败"); 
		                    }   

		             });
			};
//取消按钮触发函数
cancelfun = function(){
				if(addwin!=null&&addwin.isVisible()){
					uploadsql_flag = 0;
			    	addwin.hide();
					addwin.setActive(true);
			    }
};

//新增表单提交方法
addSubmit = function(){
	if(!afp.form.isValid()){
		Ext.Msg.alert('提示',"请填写完整信息");
		return;
	}
				if(passflag!=1){
					if(confirm("sql没有测试，或测试不通过，确认保存？ ")){
						saveSubmit();
					}
				}else{
					saveSubmit();
				}
				
					           
};
editSaveSubmit = function(){
	//进度条
				var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在提交数据...'});
				var id = efp.form.findField('id').getValue();						//id
				//提交表单的信息 
				var databaseId = efp.form.findField('databaseId1').getValue();		//数据库
				var connname = efp.form.findField('connname_edit').getValue();	// 主题
				var dbip = efp.form.findField('dbip_edit').getValue();			//ruleSql
				var dbport = efp.form.findField('dbport_edit').getValue();			
				var dbname = efp.form.findField('dbname_edit').getValue();
				var username = efp.form.findField('username_edit').getValue();//excelName
				var password = efp.form.findField('password_edit').getValue();//sheetName
				
				//进度条
				lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		            		url: CTX + '/dbmgr/addOrEditDb.itm',
		                	params:{
		                		id: id,
		                		connname: connname,
		                		dbip: dbip,
		                		dbport:dbport,
		                		dbname:dbname,
		                		username:username,
		                		password:password,
		                		databaseId:databaseId
		                	},
		 					success:function(result){
		 							var resultStr = Ext.util.JSON.decode(result.responseText);
		 							var responseStr = resultStr.success;
		 							lm.hide();
		 							if(responseStr){
		 								Ext.Msg.alert('提示','修改成功');
		 								passflag = 0;
		 								editwin.hide();
		 								store.load({params:{start:0, limit:10}});
		 							}else{
		 								Ext.Msg.alert('提示','修改失败');
		 							}
		 							//downloadsql_flag = 0;
		 					}, 
		                    failure: function(resp,opts) { 
		                    			lm.hide();
		                    		 //downloadsql_flag = 0;
		                             Ext.Msg.alert('错误', "修改失败"); 
		                    }
		             });
};
//修改表单提交方法
editSubmit = function (){
	if(!efp.form.isValid()){
		Ext.Msg.alert('提示',"请填写完整信息");
		return;
	}
				if(passflag!=1){
					if(confirm("sql没有测试，或测试不通过，确认保存？ ")){
						editSaveSubmit();
					}
				}else{
					editSaveSubmit();
				}
}

//删除记录方法
/*
delfun = function (){
	//判断是否选择了一条记录
	if(grid.getSelectionModel().getCount()!=1){
		Ext.MessageBox.alert('提示','请选择一条记录！');
	}
	else{
	   //获取grid record
	   var record = grid.getSelectionModel().getSelected();
	   var id = record.get('id');
	   Ext.Msg.confirm('提示','确定要删除吗？',function(btn){
  				if(btn=='yes'){
	  				Ext.Ajax.request({
	  					url: 'deviceconfig.itm?method=delDeviceInfo',
	  					method: 'post',
	  					params:{
	  						id: id
	  					},
	  					success : function(result) {
	  						responseStr = result.responseText;
	  						if(responseStr == "1"){
	  							Ext.Msg.alert('提示','删除成功');	
	  						}
	  						else if(responseStr == "-1"){
	  							Ext.Msg.alert('提示','该设备有相关联的自动维护作业计划任务<br>请删除相关任务后再删除该设备');
	  						}
	  						else if(responseStr == "-2"){
	  							Ext.Msg.alert('提示','该设备有相关联的性能指标采集任务<br>请删除相关任务后再删除该设备');
	  						}
	  						else{
	  							Ext.Msg.alert('提示','操作未成功');
	  						}
	  					}
	  				});
  				}		
  			});
	   
	}
}
*/


//弹出新增表单
addfun = function(){
	//判断新增formpanel是否存在
	if(afp == null){
		
		//创建一个formpanel
		afp = new Ext.form.FormPanel({
			id:'db_addform',
	        frame: true,
	        labelWidth: 90,
	        labelAlign: 'right',
	       // defaultType: 'textfield',
	        items:[ 	//使用fieldset将表单值包起来
	                	{
						 	  xtype: 'textfield',
						      fieldLabel: '连接名称',
						      id: 'connname_add',
						      name: 'connname_add',
						      allowBlank : false,			//不允许为空
						      blankText : '连接名称不能为空',
						      width:300,
		                      maxLength:500
					      } ,
		                  new Ext.form.ComboBox({
	                        	fieldLabel: '数据库类型',
	                        	id: 'databaseId',
	                        	name: 'databaseId',
	                        	store: database_store,
		                        valueField:'id',				//combox id值
		                        displayField:'discribe',	 			//combox 显示值
		                        typeAhead: true,				//自动匹配。注：typeAheadDelay 参数--当typeAhead=true时 
		                        mode: 'local',					//采用本地数据加载，因combox在创建时，已加载了数据。这样可自动匹配数据
		                        triggerAction: 'all',
		                        emptyText:'-请选择连接的数据库-',
		                        width:300,
		                        value:'mysql',
		                        selectOnFocus:true
               					}),
						      {
							 	  xtype: 'textfield',
							      fieldLabel: '主机名或IP地址',
							      id: 'dbip_add',
							      name: 'dbip_add',
							      allowBlank : false,			//不允许为空
							      blankText : '主机名不能为空',
							      width:300,
			                      maxLength:500
						      },{
							 	  xtype: 'textfield',
							      fieldLabel: '端口',
							      id: 'dbport_add',
							      name: 'dbport_add',
							      allowBlank : false,			//不允许为空
							      blankText : '端口不能为空',
							      width:300,
			                      maxLength:500
						      },{
							 	  xtype: 'textfield',
							      fieldLabel: '数据库名',
							      id: 'dbname_add',
							      name: 'dbname_add',
							      allowBlank : false,			//不允许为空
							      blankText : '数据库名不能为空',
							      width:300,
			                      maxLength:500
						      },
						      {
		                       	  xtype: 'textfield',
							      fieldLabel: '用户名',
							      id: 'username_add',
							      name: 'username_add',
							      allowBlank : false,
							      width:300,
			                      maxLength:255
						          },
		                       	{
		                       	  xtype: 'textfield',
							      fieldLabel: '密码',
							      allowBlank : false,
							      id: 'password_add',
							      name: 'password_add',
							      width:300,
			                      maxLength:255
						      }
	            	         ],
	             bbar:[{
	                		text:'测试连接',
	                		iconCls:'view',
	                		handler: testSql		//测试sql
	                  },'->',{
	                		text:'保存',
	                		iconCls:'save',
	                		handler: addSubmit		//提交新增表单
	                  },'-',{
	                		text:'取消',
	                		iconCls:'cancel',
	                		handler: cancelfun
	             }]
	    });
					//判断新增表单window对象是否存在
	            	if(addwin==null){
	            		//新建一个window对象
	            		addwin = new Ext.Window({
	            				title: '新增',
	            				iconCls:'add',
		            			layout:'fit',			//单一项目布局,加载的panel自动扩大，以适应容器
		                        width:500,
		                        height:350,
		                        plain : true,			
		                        modal:true,				//模态
		                        closeAction : 'hide',	//隐藏窗口；'close'为destroy窗口
		                        collapsible:true,		//是否可收缩
		                        items:[afp]
	            		});
	            	}
            	}
            	//如果新增表单对象不为空，则重置form表单
            	else{
					afp.form.reset();
				}
				Ext.getCmp('databaseId').setValue(query_databaseId.getValue());
            	var  top = document.body.scrollTop+(document.body.clientHeight/2-addwin.height/2);
				addwin.setPosition((document.body.clientWidth/2-addwin.width/2) ,top);
            	addwin.show();
};

//弹出修改窗口
editfun = function (){
	//判断是否选择了一条记录
	if(grid.getSelectionModel().getCount()!=1){
		Ext.MessageBox.alert('提示','请选择一条记录！');
	}
	else{

	   //获取选中记录
	   var record = grid.getSelectionModel().getSelected();
		
		var lm = new Ext.LoadMask(Ext.getBody(), {
					msg : '正在获取数据...'
				});
		//lm.show();
		//判断修改表单panel
		if(efp==null){
	    	efp = new Ext.form.FormPanel({
			id:'db_editform',
	        frame: true,
	        labelWidth: 90,
	        labelAlign: 'right',
	        items:[ 	//id隐藏
	                	//使用fieldset将表单值包起来
	            		 
		                	new Ext.form.Hidden({
	       							name:'id',
	       							id:'id'
		                	}),{
							 	  xtype: 'textfield',
							      fieldLabel: '连接名称',
							      id: 'connname_edit',
							      name: 'connname_edit',
							      allowBlank : false,			//不允许为空
							      blankText : '连接名称不能为空',
							      width:300,
			                      maxLength:500
						      } ,
			                  new Ext.form.ComboBox({
		                        	fieldLabel: '数据库类型',
		                        	id: 'databaseId1',
		                        	name: 'databaseId1',
		                        	store: database_store,
			                        valueField:'id',				//combox id值
			                        displayField:'discribe',	 			//combox 显示值
			                        typeAhead: true,				//自动匹配。注：typeAheadDelay 参数--当typeAhead=true时 
			                        mode: 'local',					//采用本地数据加载，因combox在创建时，已加载了数据。这样可自动匹配数据
			                        triggerAction: 'all',
			                        emptyText:'-请选择连接的数据库-',
			                        width:300,
			                        value:'mysql',
			                        selectOnFocus:true
	               					}),
							      {
								 	  xtype: 'textfield',
								      fieldLabel: '主机名或IP地址',
								      id: 'dbip_edit',
								      name: 'dbip_edit',
								      allowBlank : false,			//不允许为空
								      blankText : '主机名不能为空',
								      width:300,
				                      maxLength:500
							      },{
								 	  xtype: 'textfield',
								      fieldLabel: '端口',
								      id: 'dbport_edit',
								      name: 'dbport_edit',
								      allowBlank : false,			//不允许为空
								      blankText : '端口不能为空',
								      width:300,
				                      maxLength:500
							      },{
								 	  xtype: 'textfield',
								      fieldLabel: '数据库名',
								      id: 'dbname_edit',
								      name: 'dbname_edit',
								      allowBlank : false,			//不允许为空
								      blankText : '数据库名不能为空',
								      width:300,
				                      maxLength:500
							      },
							      {
			                       	  xtype: 'textfield',
								      fieldLabel: '用户名',
								      id: 'username_edit',
								      name: 'username_edit',
								      allowBlank : false,
								      width:300,
				                      maxLength:255
							          },
			                       	{
			                       	  xtype: 'textfield',
								      fieldLabel: '密码',
								      allowBlank : false,
								      id: 'password_edit',
								      name: 'password_edit',
								      width:300,
				                      maxLength:255
							      }
	                		 ],
		            bbar:[{
	                		text:'测试连接',
	                		iconCls:'view',
	                		handler: testSql1		//测试sql
	                     },'->',{
		            			text:'保存',
		                		iconCls:'save',
		                		handler:editSubmit
		                 },'-',{
		                		text:'取消',
		                		iconCls:'cancel',
		                		handler:function(){
		                				if(editwin!=null&&editwin.isVisible()){
		                					//downloadsql_flag = 0;
		                					editwin.hide();
											editwin.setActive(true);
		                				}
		                			}
		                  }]
	    	})
	    	if(editwin==null){
		    	editwin = new Ext.Window({
		        				title: '修改',
		            			iconCls:'option',
			            		layout:'fit',
			                    width:500,
			                    height:350,
			                    plain : true,
			                    modal:true,
			                    closeAction : 'hide',
			                    collapsible:true,
			                    items:efp
		         });
	   		}
	    	
 		}
 		
 		//如果修改表单对象不为空，则重置form表单，并重新加载cs
       else{
	   		efp.form.reset();
	   }
	   
	   var  top = document.body.scrollTop+(document.body.clientHeight/2-editwin.height/2);
	   editwin.setPosition((document.body.clientWidth/2-editwin.width/2) ,top);
	   editwin.show();

	   //其他值赋值到表单
	    //其他值赋值到表单
	   efp.form.findField('id').setValue(record.get('id'));
	   efp.form.findField('connname_edit').setValue(record.get('connName'));
	   efp.form.findField('username_edit').setValue(record.get('dbUser'));
		efp.form.findField('password_edit').setValue(record.get('dbPassword'));
	   efp.form.findField('dbip_edit').setValue(record.get('dbHost'));
	   efp.form.findField('dbport_edit').setValue(record.get('dbPort'));
	   efp.form.findField('dbname_edit').setValue(record.get('dbName'));
	   
	   Ext.getCmp('databaseId1').setValue(query_databaseId.getValue());
	   //efp.form.findField('ruleSql_edit').setValue(record.get('ruleSql'));
	   
	   /*Ext.Ajax.request({
					url : CTX + '/sqlmgr/getOraDqcRule.itm',
					method : 'post',
					params : {
						id : grid.getSelectionModel().getSelected().get('id'),
						databaseId:query_databaseId.getValue()
					},
					success : function(result) {
						// 将后台的json字符串转成json对象
						resultStr = Ext.util.JSON.decode(result.responseText);
				 		// 转换换行和引号
						if(query_databaseId.getValue()=='mysql'){
							command = resultStr.ruleSql.replace(/huanhang/g, "\n")
								.replace(/yinhao/g, "'");
							if(resultStr.proName!=null&&resultStr.proName!=''&&resultStr.proName!=undefined){
								command2 = resultStr.proName.replace(/huanhang/g, "\n")
								.replace(/yinhao/g, "'");
								efp.form.findField('proName_edit').setValue(command2);
							}else{
								Ext.getCmp('proName_edit').setValue(resultStr.proName);
							}
						}
						if(query_databaseId.getValue()=='td'){
							command = resultStr.ruleSql.replace(/huanhang/g, "\n").replace(/yinhao/g, "'");
						}
						// 设置结果值到textArea中
						Ext.getCmp('ruleSql_edit').setValue(command);
						
						// 隐藏
						lm.hide();
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作未完成！');
						lm.hide();
					}
				});*/
	   //efp.form.findField('system_edit').setValue(record.get('system'));
	}
	
}

//查看函数
viewfun = function (){

	//判断是否选择了一条记录
	if(grid.getSelectionModel().getCount()!=1){
		Ext.MessageBox.alert('提示','请选择一条记录！');
	}
	else{
		var record = grid.getSelectionModel().getSelected();
		//判断查看窗口是否已生成
		var id = grid.getSelectionModel().getSelected().get('id');

		var lm = new Ext.LoadMask(Ext.getBody(), {
					msg : '正在获取数据...'
				});
		//lm.show();
		var resultStr;
		var command;
		if(p1 == null){
			p1 = new Ext.form.FormPanel({
		        id:'perf_view',
	        	frame: true,
		        labelWidth: 90,
		        labelAlign: 'right',
		        items:[ 	//id隐藏
	                	//使用fieldset将表单值包起来
		                	{
							 	  xtype: 'textfield',
							      fieldLabel: '连接名称',
							      readOnly : true,
							      id: 'connname_view',
							      name: 'connname_view',
							      allowBlank : false,			//不允许为空
							      blankText : '连接名称不能为空',
							      width:300,
			                      maxLength:500
						      } ,
			                  new Ext.form.ComboBox({
		                        	fieldLabel: '数据库类型',
		                        	 readOnly : true,
		                        	id: 'databaseId_view',
		                        	name: 'databaseId_view',
		                        	store: database_store,
			                        valueField:'id',				//combox id值
			                        displayField:'discribe',	 			//combox 显示值
			                        typeAhead: true,				//自动匹配。注：typeAheadDelay 参数--当typeAhead=true时 
			                        mode: 'local',					//采用本地数据加载，因combox在创建时，已加载了数据。这样可自动匹配数据
			                        triggerAction: 'all',
			                        emptyText:'-请选择连接的数据库-',
			                        width:300,
			                        value:'mysql',
			                        selectOnFocus:true
	               					}),
							      {
								 	  xtype: 'textfield',
								      fieldLabel: '主机名或IP地址',
								      readOnly : true,
								      id: 'dbip_view',
								      name: 'dbip_view',
								      allowBlank : false,			//不允许为空
								      blankText : '主机名不能为空',
								      width:300,
				                      maxLength:500
							      },{
								 	  xtype: 'textfield',
								      fieldLabel: '端口',
								      readOnly : true,
								      id: 'dbport_view',
								      name: 'dbport_view',
								      allowBlank : false,			//不允许为空
								      blankText : '端口不能为空',
								      width:300,
				                      maxLength:500
							      },{
								 	  xtype: 'textfield',
								      fieldLabel: '数据库名',
								      readOnly : true,
								      id: 'dbname_view',
								      name: 'dbname_view',
								      allowBlank : false,			//不允许为空
								      blankText : '数据库名不能为空',
								      width:300,
				                      maxLength:500
							      },
							      {
			                       	  xtype: 'textfield',
								      fieldLabel: '用户名',
								      readOnly : true,
								      id: 'username_view',
								      name: 'username_view',
								      width:300,
				                      maxLength:255
							          },
			                       	{
			                       	  xtype: 'textfield',
								      fieldLabel: '密码',
								      readOnly : true,
								      id: 'password_view',
								      name: 'password_view',
								      width:300,
				                      maxLength:255
							      }]
		    	});
		}
			
	    	if (viewwin == null) {
				viewwin = new Ext.Window({
							id : 'viewwinid',
							title : '查看结果',
							iconCls:'option',
				            layout:'fit',
							width : 500,
							height : 350,
							plain : true,
							autoScroll : true,
							modal : true, // 模态
							closeAction : 'hide', // 隐藏窗口；'close'为destroy窗口
							collapsible : true,
							items : p1
						});
			}
		
		

		var top = document.body.scrollTop
				+ (document.body.clientHeight / 2 - viewwin.height / 2);
		viewwin.setPosition(
				(document.body.clientWidth / 2 - viewwin.width / 2), top);
		viewwin.show();
		   Ext.getCmp('connname_view').setValue(record.get('connName'));
		   Ext.getCmp('username_view').setValue(record.get('dbUser'));
		   Ext.getCmp('password_view').setValue(record.get('dbPassword'));
		   Ext.getCmp('dbip_view').setValue(record.get('dbHost'));
		   Ext.getCmp('dbport_view').setValue(record.get('dbPort'));
		   Ext.getCmp('dbname_view').setValue(record.get('dbName'));
		   Ext.getCmp('databaseId_view').setValue(query_databaseId.getValue());
	}

}
function exceSql(id){
	var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在执行sql'});
	var db = 
		lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: 'jobmgr.itm?method=exceSql',
		                	params:{
		                		id: id,
		                		type: 'sql',
		                		db:query_databaseId.getValue()
		                	},
		 					success:function(result){
		 							var respText = Ext.util.JSON.decode(result.responseText);                                                 
                             		var r = respText.r; 
		 							lm.hide();
		 							if(r=='1'){
		 								//Ext.Msg.alert('提示','连接远程服务器成功，未发送邮件成功，发送的后台功能还在开发中');
		 								
		 								Ext.Msg.alert('提示','执行sql成功');
		 								store.load({params:{start:0, limit:10}});
		 							}else{
		 								Ext.Msg.alert('提示','执行sql失败');
		 							}
		 					},
		 					failure: function(result) {
                             lm.hide(); 
                             Ext.Msg.alert('提示','执行sql失败');
                      }  
		      });
}
 
function showbutton(value, cellmeta, record ){
//var jobStatus = record.data["jobState"];
var jobStatus = record.data.jobState;
//alert(value); +"<INPUT type=button value=执行sql onclick=exceSql('"+value+"')>"
var returnStr = "<INPUT type=button value=开始job onclick=startJob('"+value+"',"+"'"+jobStatus+"')>"
+"<INPUT type=button value=停止job onclick=stopJob('"+value+"',"+"'"+jobStatus+"')>";
return returnStr;
}
Ext.onReady(function(){
	var theUser = document.getElementById("hideUserName").value;
//--------------------------------------------
Ext.BLANK_IMAGE_URL = CTX + '/resources/js/ext/resources/images/default/s.gif';
		
		//查询列表stroe 加载
    	store = new Ext.data.SimpleStore({
            url:CTX+'/dbmgr/queryDbInfo.itm', 
            totalProperty : 'totalCount', 
            root:'rows',  
            //baseParams:{action:'get_all_models'}
            fields:[{name: 'id',mapping:"id"},
		           {name: 'connName',mapping:"connName"},
		           {name: 'dbUrl',mapping:"dbUrl"},
		           {name: 'dbUser',mapping:"dbUser"},
		           {name: 'dbPassword',mapping:"dbPassword"},
		           {name: 'dbDriver',mapping:"dbDriver"},
		           {name: 'dbHost',mapping:"dbHost"},
		           {name: 'dbName',mapping:"dbName"},
		           {name: 'dbPort',mapping:"dbPort"},
		           {name: 'dbType',mapping:"dbType"}
		           ]
    });
	if(theUser!='bi_dev'){
		grid = new Ext.grid.GridPanel({
	        store: store,
	        iconCls:'head',
	        sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	        columns: [
	            {id:'id',header: "id", width: 20,hidden:true, sortable: true, dataIndex: 'id'},
	            {header: "连接名称", width:200, sortable: true, dataIndex: 'connName'},
	            {header: "DB url", width:400, sortable: true, dataIndex: 'dbUrl'},
	            {header: "DB driver",width:200, sortable: true, dataIndex: 'dbDriver'},
	            {header: "DB user",width:100, sortable: true, dataIndex: 'dbUser'},
	            {header: "DB password", width:100, sortable: true, dataIndex: 'dbPassword'}
	        ],
	        tbar:[{
	            text:'查看',
	            tooltip:'查看',
	            iconCls:'view',
	            handler: viewfun	//查看函数	
	        },'-',{
	        	text:'刷新',
	            tooltip:'刷新',
	            iconCls:'refresh',
	            handler:function(){	//刷新
	            	store.reload();
	            }
	        }],
	        
	         plugins: new Ext.ux.PanelResizer({
	            minHeight: 100
	        }),
	        
	        bbar: new Ext.PagingToolbar({
	            store: store,
	            pageSize: 10,
	            emptyMsg: '未查询到记录',
	            displayInfo: true,
	            plugins: new Ext.ux.ProgressBarPager()
	        }),
	        autoExpandColumn: 'id',
	        height:458,
	        title:'sql语句信息列表',
	        loadMask:true
	    });
	} else {
		grid = new Ext.grid.GridPanel({
	        store: store,
	        iconCls:'head',
	        region:'center',
	        layout:'fit',
	        sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	        columns: [
	            {id:'id',header: "id", hidden:true, sortable: true, dataIndex: 'id'},
	            new Ext.grid.RowNumberer({header : "序号",width : 40,
	            	renderer: function (value, metadata, record, rowIndex) {
	            	return record_start + 1 + rowIndex;
	            	} 
	            }),
	            {header: "连接名称", width:200, sortable: true, dataIndex: 'connName'},
	            {header: "DB url", width:400, sortable: true, dataIndex: 'dbUrl'},
	            {header: "DB driver",width:200, sortable: true, dataIndex: 'dbDriver'},
	            {header: "DB user",width:100, sortable: true, dataIndex: 'dbUser'},
	            {header: "DB password", width:100, sortable: true, dataIndex: 'dbPassword'}
	        ],
	        tbar:[{
	            text:'新增',
	            tooltip:'新增',
	            iconCls:'add',
	            handler: addfun		//新增函数
	        }, '-', {
	            text:'修改',
	            tooltip:'修改',
	            iconCls:'option',
	            handler: editfun	//修改函数
	        },'-',{
	            text:'查看',
	            tooltip:'查看',
	            iconCls:'view',
	            handler: viewfun	//查看函数	
	        }/*,'-',{
	            text:'删除',
	            tooltip:'删除',
	            iconCls:'remove',
	            handler:delfun		//删除函数
	        }*/,'-',{
	        	text:'刷新',
	            tooltip:'刷新',
	            iconCls:'refresh',
	            handler:function(){	//刷新
	            	store.reload();
	            }
	        }/*,'-',{
	        	text:'上传sql',
	            tooltip:'上传sql',
	            iconCls:'add',
	            handler:uploadSql
	        }*/],
	        
	         plugins: new Ext.ux.PanelResizer({
	            minHeight: 100
	        }),
	        
	        bbar: new Ext.PagingToolbar({
	            store: store,
	            pageSize: 10,
	            emptyMsg: '未查询到记录',
	            displayInfo: true,
	            plugins: new Ext.ux.ProgressBarPager()
	        }),
	        autoExpandColumn: 'id',
	        //height:458,
	        title:'数据库列表',
	        loadMask:true
	    });
	}
    
	var panel_ = new Ext.Panel({
        renderTo: 'searchdiv',
        //height:520,
        //autoHeight:true,
        height:document.getElementById('searchdiv').clientHeight,
        //height:400,
        width:document.getElementById('searchdiv').clientWidth,
        //autoScroll:true,
        //autoScroll:false,
		iconCls:'query',
		layout:'fit',
        tbar: [
            'ID：',
            query_id,           
            '连接名称：',
            query_dbname,
            'url：',
            query_dburl,
            {
	           text:'查询',
	           tooltip:'提交查询',
	           iconCls:'view',
	           handler: function() {
				    store.load();//点击执行函数
			   }		
	        },
	        {
	           text:'重置',
	           tooltip:'内容重置',
	           iconCls:'view',
	           handler: dataReset		//点击执行函数
	        }
        ],
        items:[grid]
    });
    panel_.render(document.body);
    store.on("beforeload", function() {
				store.baseParams = {
						start:0,
						limit:10,
						query_dburl:query_dburl.getValue(),
						query_id:query_id.getValue(),
						query_dbname:query_dbname.getValue()
				};
			});
    store.load({params:{start:0, limit:10}});
});