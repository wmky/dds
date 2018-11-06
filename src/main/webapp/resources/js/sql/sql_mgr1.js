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
											Ext.getCmp('ruleSql_add').setValue('sql文件名：'+filePath);
		                			}
		                		}
		                 },'-',{
		                		text:'取消',
		                		iconCls:'cancel',
		                		handler:function(){
		                				if(upload_view!=null&&upload_view.isVisible()){
		                					upload_view.hide();
											upload_view.setActive(true);
											//Ext.getCmp('ruleSql_add').setValue('');
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

var query_subject = new Ext.form.TextField({
				id:'query_subject',
				emptyText:'-sql描述-',
				width:90
			});
var query_id = new Ext.form.TextField({
				id:'query_id',
				emptyText:'-ID-',
				width:35
			});
var query_sql =  new Ext.form.TextField({
				id:'query_sql',
				emptyText:'-sqlrule-',
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
	Ext.getCmp('query_subject').setValue('');
	Ext.getCmp('query_sql').setValue('');
	Ext.getCmp('query_date1').setValue('');
	Ext.getCmp('query_date2').setValue('');
};
//测试sql
testSql = function(){
	var sqlSentence = afp.form.findField('ruleSql_add').getValue();		//sql语句
	var databaseId = afp.form.findField('databaseId').getValue();		//数据库
	if(sqlSentence.trim().length<1){
		Ext.Msg.alert('提示','请填写sql语句');
		return ;
	}
	if(databaseId.trim().length<1){
		Ext.Msg.alert('提示','请选择测试连接数据库');
		return ;
	}
	var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在测试sql语句...'});
	lm.show();
	 Ext.Ajax.request({
		            		method: 'post',
		                	url: 'sqlmgr.itm?method=testSql',
		                	params:{
		                		sqlSentence: sqlSentence,
		                		databaseId: databaseId
		                	},
		 					success:function(result){
		 							responseStr = result.responseText;
		 							lm.hide();
		 							if(responseStr=='-1'){
		 								passflag = 0;
		 								Ext.Msg.alert('提示','测试不通过，sql执行报错');
		 							}else if(responseStr=='-2'){
		 								passflag = 0;
		 								Ext.Msg.alert('提示','测试不通过，不是select语句');
		 							}else{
		 								passflag = 1;
		 								Ext.Msg.alert('提示','测试通过');
		 								
		 							}
		 					}
		             });
}
//测试sql1
testSql1 = function(){
	var sqlSentence = efp.form.findField('ruleSql_edit').getValue();		//sql语句
	var databaseId = efp.form.findField('databaseId1').getValue();		//数据库
	if(sqlSentence.trim().length<1){
		Ext.Msg.alert('提示','请填写sql语句');
		return ;
	}
	if(databaseId.trim().length<1){
		Ext.Msg.alert('提示','请选择连接数据库');
		return ;
	}
	var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在测试sql语句...'});
	lm.show();
	 Ext.Ajax.request({
		            		method: 'post',
		                	url: 'sqlmgr.itm?method=testSql',
		                	params:{
		                		sqlSentence: sqlSentence,
		                		databaseId: databaseId
		                	},
		 					success:function(result){
		 							responseStr = result.responseText;
		 							lm.hide();
		 							if(responseStr=='-1'){
		 								passflag = 0;
		 								Ext.Msg.alert('提示','测试不通过，sql执行报错');
		 							}else if(responseStr=='-2'){
		 								passflag = 0;
		 								Ext.Msg.alert('提示','测试不通过，不是select语句');
		 							}else{
		 								passflag = 1;
		 								Ext.Msg.alert('提示','测试通过');
		 								
		 							}
		 					}
		             });
}


saveSubmit = function(){
	var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在提交数据...'});
				//提交表单的信息 
				var databaseId = afp.form.findField('databaseId').getValue();		//数据库
				var subject = afp.form.findField('subject_add').getValue();	// 主题
				var ruleSql = afp.form.findField('ruleSql_add').getValue();			//ruleSql
				var system    = afp.form.findField('system_add').getValue();	
				var proName   = afp.form.findField('proName_add').getValue();//存储过程名称
				var excelName = afp.form.findField('excelName_add').getValue();//excelName
				var sheetName = afp.form.findField('sheetName_add').getValue();//sheetName
				
				
				
				if(subject.trim().length<1){
					Ext.Msg.alert('提示','必须填写主题');
					return ;
				}
				
				lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: 'sqlmgr.itm?method=addOrEditSql',
		                	params:{
		                		subject: subject,
		                		ruleSql: ruleSql,
		                		system:system,
		                		proName:proName,
		                		excelName:excelName,
		                		sheetName:sheetName,
		                		databaseId:databaseId
		                	},
		 					success:function(result){
		 							var respText = Ext.util.JSON.decode(result.responseText);                                                 
                             		var r = respText.r; 
                             		var id = respText.id; 

		 							//responseStr = result.responseText;
		 							lm.hide();
		 							if(r=='1'){//添加成功
		 								passflag = 0;
		 								
		 								//alert(uploadsql_flag);
		 								
		 								//进入上传sql文件的方法
		 								/*if(uploadsql_flag==1&&id!='-1'){
		 									saveSqlSubmit(id);
		 								}
		 								uploadsql_flag==0;*/
		 								addwin.hide();
		 								//query_databaseId.setValue('td');
		 								Ext.Msg.alert('提示','添加成功');
		 								store.load({params:{start:0, limit:16}});
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
				var subject = efp.form.findField('subject_edit').getValue();	// 主题
				var ruleSql = efp.form.findField('ruleSql_edit').getValue();			//ruleSql
				var system =  efp.form.findField('system_edit').getValue();
				var proName =  efp.form.findField('proName_edit').getValue();
				var excelName =  efp.form.findField('excelName_edit').getValue();
				var sheetName =  efp.form.findField('sheetName_edit').getValue();
				
				
				
				if(subject.trim().length<1){
					Ext.Msg.alert('提示','必须填写主题');
					return ;
				}
				
				//进度条
				lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: 'sqlmgr.itm?method=addOrEditSql',
		                	params:{
		                		id: id,
		                		subject: subject,
		                		ruleSql: ruleSql,
		                		system:system,
		                		databaseId:databaseId,
		                		proName:proName,
		                		excelName:excelName,
		                		sheetName:sheetName
		                	},
		 					success:function(result){
		 							var resultStr = Ext.util.JSON.decode(result.responseText);
		 							var responseStr = resultStr.r;
		 							lm.hide();
		 							if(responseStr=='2'){
		 								Ext.Msg.alert('提示','修改成功');
		 								passflag = 0;
		 								editwin.hide();
		 								store.load({params:{start:0, limit:16}});
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
			id:'device_addform',
	        frame: true,
	        labelWidth: 90,
	        labelAlign: 'right',
	       // defaultType: 'textfield',
	        items:[ 	//使用fieldset将表单值包起来
	        			{
					            layout:'column',
					            items:[{
					                columnWidth:.5,
					                layout: 'form',
					                items: [
					                    new Ext.form.ComboBox({
									                        	fieldLabel: '连接数据库',
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
						                       					})
					                 ]
					            },{
					                columnWidth:.5,
					                layout: 'form',
					                items: [{
					                     xtype: 'textfield',
									      fieldLabel: '系统',
									      id: 'system_add',
									      name: 'system_add',
									      allowBlank : false,			//不允许为空
									      value:'BI',
									      width:300,
					                      maxLength:100
					                }]
					            }]
		                  },{
							 	  xtype: 'textfield',
							      fieldLabel: 'sql描述',
							      id: 'subject_add',
							      name: 'subject_add',
							      allowBlank : false,			//不允许为空
							      blankText : 'sql描述不能为空',
							      width:730,
			                      maxLength:500
						      } ,{
							 	  xtype: 'textarea',
							      fieldLabel: 'sql表达式',
							      id: 'ruleSql_add',
							      name: 'ruleSql_add',
							      allowBlank : false,			//不允许为空
							      blankText : 'sql表达式不能为空',
							      width:730,
							      height : 200,
			                      maxLength:500
						      } ,
						      	{
					            layout:'column',
					            items:[{
					                columnWidth:.7,
					                layout: 'form',
					                items: [{
					                      xtype: 'textfield',
									      fieldLabel: '存储过程名称',
									      id: 'proName_add',
									      name: 'proName_add',
									      width:500,
					                      maxLength:100
					                 }]
					                }]
		                          },
		                          {
		                       	  xtype: 'textfield',
							      fieldLabel: 'excel_name',
							      id: 'excelName_add',
							      name: 'excelName_add',
							      width:730,
			                      maxLength:255
						          },
		                       	{
		                       	  xtype: 'textfield',
							      fieldLabel: 'sheet_name',
							      id: 'sheetName_add',
							      name: 'sheetName_add',
							      width:730,
			                      maxLength:255
						      }
	            	         ],
	             bbar:[{
	                		text:'测试sql',
	                		iconCls:'view',
	                		handler: testSql		//测试sql
	                  },/*'-',{
	                		text:'上传sql',
	                		iconCls:'view',
	                		handler: uploadSql		//上传sql
	                  },*/'->',{
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
		                        width:900,
		                        height:500,
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
		lm.show();
		//判断修改表单panel
		if(efp==null){
	    	efp = new Ext.form.FormPanel({
			id:'device_editform',
	        frame: true,
	        labelWidth: 90,
	        labelAlign: 'right',
	        items:[ 	//id隐藏
	                	//使用fieldset将表单值包起来
	            		 
				                	new Ext.form.Hidden({
			       							name:'id',
			       							id:'id'
				                	}),{
					            layout:'column',
					            items:[{
					                columnWidth:.5,
					                layout: 'form',
					                items: [
					                    
											 new Ext.form.ComboBox({
											                        	fieldLabel: '连接数据库',
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
												                        value:query_databaseId.getValue(),
												                        selectOnFocus:true
						                       					})
					                 ]
					            },{
					                columnWidth:.5,
					                layout: 'form',
					                items: [{
					                     xtype: 'textfield',
									      fieldLabel: '系统',
									      id: 'system_edit',
									      name: 'system_edit',
									      allowBlank : false,			//不允许为空
									      value:'BI',
									      width:300,
					                      maxLength:100
					                }]
					            }]
		                  },
				                	
				                	
	                			{
							 	  xtype: 'textfield',
							      fieldLabel: 'sql描述',
							      id: 'subject_edit',
							      name: 'subject_edit',
							      allowBlank : false,			//不允许为空
							      blankText : 'sql描述不能为空',
							      width:730,
			                      maxLength:500
						      } ,{
							 	  xtype: 'textarea',
							      fieldLabel: 'sql表达式',
							      id: 'ruleSql_edit',
							      name: 'ruleSql_edit',
							      allowBlank : false,			//不允许为空
							      blankText : 'sql表达式不能为空',
							      width:730,
							      height : 200,
			                      maxLength:500
						      } ,
						       {
					            layout:'column',
					            items:[{
					                columnWidth:.7,
					                layout: 'form',
					                items: [{
					                      xtype: 'textfield',
									      fieldLabel: '存储过程名称',
									      id: 'proName_edit',
									      name: 'proName_edit',
									      width:500,
					                      maxLength:100
					                }]
					            }]
		                  },
		                       	{
		                       	  xtype: 'textfield',
							      fieldLabel: 'excel_name',
							      id: 'excelName_edit',
							      name: 'excelName_edit',
							      width:730,
			                      maxLength:255
						      },
		                       	{
		                       	  xtype: 'textfield',
							      fieldLabel: 'sheet_name',
							      id: 'sheetName_edit',
							      name: 'sheetName_edit',
							      width:730,
			                      maxLength:255
						      }
	                		 ],
		            bbar:[{
	                		text:'测试sql',
	                		iconCls:'view',
	                		handler: testSql1		//测试sql
	                     },/*'-',{
	                		text:'上传sql',
	                		iconCls:'view',
	                		handler: uploadSql		//上传sql
	                     },'-',{
	                		text:'下载sql',
	                		iconCls:'view',
	                		handler: function(){//下载sql
	                			if(downloadsql_flag==1){
	                				window.location.href = 'upLoad.itm?id='+record.get('id');
	                			}else{
	                				Ext.Msg.alert('提示','该条记录没有sql');
	                			}
								
	                		}
	                     },*/'->',{
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
			                    width:900,
			                    height:500,
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
	   efp.form.findField('subject_edit').setValue(record.get('subject'));
	   efp.form.findField('excelName_edit').setValue(record.get('excelName'));
	   efp.form.findField('sheetName_edit').setValue(record.get('sheetName'));
	   
	   Ext.getCmp('databaseId1').setValue(query_databaseId.getValue());
	   //efp.form.findField('ruleSql_edit').setValue(record.get('ruleSql'));
	   
	   Ext.Ajax.request({
					url : 'sqlmgr.itm?method=getOraDqcRule',
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
							/*if(resultStr.sql2size!=''&&resultStr.sql2size>30){//说明附件里面存有sql并且排除是一些空格或<clob>之类的字符
								  downloadsql_flag = 1;
							}*/
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
				});
	   efp.form.findField('system_edit').setValue(record.get('system'));
	}
	
}

//查看函数
viewfun = function (){

	//判断是否选择了一条记录
	if(grid.getSelectionModel().getCount()!=1){
		Ext.MessageBox.alert('提示','请选择一条记录！');
	}
	else{
		
		//判断查看窗口是否已生成
		var id = grid.getSelectionModel().getSelected().get('id');

		var lm = new Ext.LoadMask(Ext.getBody(), {
					msg : '正在获取数据...'
				});
		lm.show();
		var resultStr;
		var command;
		if(p1 == null){
			p1 = new Ext.form.FormPanel({
		        id:'perf_view',
	        	frame: true,
		        labelWidth: 90,
		        labelAlign: 'right',
		        items:[ 
	        	{
							 	  xtype: 'textfield',
							      fieldLabel: 'sql描述',
							      id: 'subject_view',
							      name: 'subject_view',
							      readOnly : true,
							      width:730,
			                      maxLength:500
						      } ,{
							 	  xtype: 'textarea',
							      fieldLabel: 'sql表达式',
							      id: 'ruleSql_view',
							      name: 'ruleSql_view',
							      readOnly : true,
							      width:730,
							      height : 200
						      } ,{
							 	  xtype: 'textfield',
							      fieldLabel: '创建时间',
							      id: 'createTime_view',
							      name: 'createTime_view',
							      readOnly : true,
							      width:730
						      },{
							 	  xtype: 'textfield',
							      fieldLabel: '存储过程名称',
							      id: 'proName_view',
							      name: 'proName_view',
							      readOnly : true,
							      width:730
						      },{
							 	  xtype: 'textfield',
							      fieldLabel: 'excel_name',
							      id: 'excelName_view',
							      name: 'excelName_view',
							      readOnly : true,
							      width:730
						      },{
							 	  xtype: 'textfield',
							      fieldLabel: 'sheet_name',
							      id: 'sheetName_view',
							      name: 'sheetName_view',
							      readOnly : true,
							      width:730
						      }
			        ]
		    	});
		}
			
	    	if (viewwin == null) {
				viewwin = new Ext.Window({
							id : 'viewwinid',
							title : '查看结果',
							iconCls:'option',
				            layout:'fit',
							width : 900,
							height : 500,
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

		// 通过id查询作业计划信息
		Ext.Ajax.request({
					url : 'sqlmgr.itm?method=getOraDqcRule',
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
								Ext.getCmp('proName_view').setValue(command2);
							}else{
								Ext.getCmp('proName_view').setValue(resultStr.proName);
							}
							
						}
						if(query_databaseId.getValue()=='td'){
							command = resultStr.ruleSql.replace(/huanhang/g, "\n").replace(/yinhao/g, "'");
						}
						Ext.getCmp('subject_view').setValue(resultStr.subject);
						Ext.getCmp('excelName_view').setValue(resultStr.excelName);
						Ext.getCmp('sheetName_view').setValue(resultStr.sheetName);
						Ext.getCmp('createTime_view').setValue(resultStr.createTime);
						// 设置结果值到textArea中
						Ext.getCmp('ruleSql_view').setValue(command);
						
						// 隐藏
						lm.hide();
					},
					failure : function() {
						Ext.Msg.alert('提示', '操作未完成！');
						viewwin.hide();
						lm.hide();
					}
				});
	}

}
function startJob(id,jobStatus){
	var theUser = document.getElementById("hideUserName").value;
	if(theUser!='bi_dev'){
		Ext.Msg.alert('提示','sorry,你没有该权限');
		return;
	}
	if(jobStatus==0){
		Ext.Msg.alert('提示','该job的状态是运行，要重新开始运行请先停止');
		return;
	}
	var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在开启job'});
	var db = 
		lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: 'jobmgr.itm?method=scheduleJob',
		                	params:{
		                		jobId: id,
		                		type: 'sql',
		                		db:query_databaseId.getValue()
		                	},
		 					success:function(result){
		 							var respText = Ext.util.JSON.decode(result.responseText);                                                 
                             		var r = respText.r; 
		 							lm.hide();
		 							if(r=='1'){
		 								//Ext.Msg.alert('提示','连接远程服务器成功，未发送邮件成功，发送的后台功能还在开发中');
		 								
		 								Ext.Msg.alert('提示','开启job成功');
		 								store.load({params:{start:0, limit:16}});
		 							}else{
		 								Ext.Msg.alert('提示','开启job失败');
		 							}
		 					},
		 					failure: function(result) {  
                             lm.hide(); 
                             Ext.Msg.alert('提示','开启job失败');
                      }  
		      });
}
function stopJob(id,jobStatus){
	var theUser = document.getElementById("hideUserName").value;
	if(theUser!='bi_dev'){
		Ext.Msg.alert('提示','sorry,你没有该权限');
		return;
	}
	if(jobStatus==-1){
		Ext.Msg.alert('提示','该job的状态是停止');
		return;
	}
	var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在停止job'});
	var db = 
		lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: 'jobmgr.itm?method=removeJob',
		                	params:{
		                		jobId: id,
		                		type: 'sql',
		                		db:query_databaseId.getValue()
		                	},
		 					success:function(result){
		 							var respText = Ext.util.JSON.decode(result.responseText);                                                 
                             		var r = respText.r; 
		 							lm.hide();
		 							if(r=='1'){
		 								//Ext.Msg.alert('提示','连接远程服务器成功，未发送邮件成功，发送的后台功能还在开发中');
		 								
		 								Ext.Msg.alert('提示','停止job成功');
		 								store.load({params:{start:0, limit:16}});
		 							}else{
		 								Ext.Msg.alert('提示','停止job失败');
		 							}
		 					},
		 					failure: function(result) {
                             lm.hide(); 
                             Ext.Msg.alert('提示','停止job失败');
                      }  
		      });
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
		 								store.load({params:{start:0, limit:16}});
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
Ext.BLANK_IMAGE_URL = 'resources/js/ext/resources/images/default/s.gif';
		
		//查询列表stroe 加载
    	store = new Ext.data.Store({
    	proxy: new Ext.data.HttpProxy({url:'sqlmgr.itm?method=querySqlInfo'}),
        reader: new Ext.data.JsonReader({totalProperty:'totalProperty',root:'root'},[
	           {name: 'id'},
	           {name: 'index'},
	           {name: 'subject'},
	           {name: 'ruleSql'},
	           {name: 'system'},
	           {name: 'createTime'},
	           {name: 'proName'},
	           {name: 'excelName'},
	           {name: 'sheetName'},
	           {name: 'jobState'}
        ])
    });
	if(theUser!='bi_dev'){
		grid = new Ext.grid.GridPanel({
	        store: store,
	        //layout:'boder',
	        region:'center',
	        layout:'fit',
	        iconCls:'head',
	        sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	        columns: [
	            {id:'id',header: "id", width: 20,hidden:true, sortable: true, dataIndex: 'id'},
	            
	            //{header:"操作",dataIndex:"id",width:140,renderer:showbutton},

	           // {header: "状态", id:"jobState",width: 40, sortable: true, dataIndex: 'jobState',renderer:function(value){
	//    
//	            		if(value == 0){
//	            			return '运行中'
//	            		}else if(value == -1){
//	            			return '停止'
//	            		}else if(value == 1){
//	            			return '暂停'
//	            		}else if(value == 3){
//	            			return '错误'
//	            		}else if(value == 2){
//	            			return '完成'
//	            		}else if(value == 4){
//	            			return '阻塞'
//	            		}else{
//	            			return '未知'
//	            		}
//	            	}
//	            },
	            {header: "ID", width: 40, sortable: true, dataIndex: 'id'},
	            {header: "sql描述", width: 200, sortable: true, dataIndex: 'subject'},
	            {header: "sql表达式", width: 300, sortable: true, dataIndex: 'ruleSql'},
	            {header: "系统", width: 100, sortable: true, dataIndex: 'system'},
	            {header: "创建时间", width: 150, sortable: true, dataIndex: 'createTime'},
	            {header: "存储过程名称", width: 150, sortable: true, dataIndex: 'proName',renderer:function(value){
	            		var newValue= value.replace(/huanhang/g, "\n")
									.replace(/yinhao/g, "'");
						return newValue;
	            }},
	            {header: "excel_name", width: 100, sortable: true, dataIndex: 'excelName'},
	            {header: "sheet_name", width: 100, sortable: true, dataIndex: 'sheetName'}
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
	            pageSize: 16,
	            emptyMsg: '未查询到记录',
	            displayInfo: true,
	            plugins: new Ext.ux.ProgressBarPager()
	        }),
	        autoExpandColumn: 'id',
	        height:500,
	        title:'sql语句信息列表',
	        loadMask:true
	    });
	} else {
		grid = new Ext.grid.GridPanel({
	        store: store,
	        iconCls:'head',
	        //region:'center',
	        layout:'fit',
	        sm: new Ext.grid.RowSelectionModel({singleSelect:true}),
	        columns: [
	            {id:'id',header: "id", width: 20,hidden:true, sortable: true, dataIndex: 'id'},
	            {header: "ID", width: 40, sortable: true, dataIndex: 'id'},
	            {header: "sql描述", width: 200, sortable: true, dataIndex: 'subject'},
	            {header: "sql表达式", width: 500, sortable: true, dataIndex: 'ruleSql'},
	            {header: "系统", width: 100, sortable: true, dataIndex: 'system'},
	            {header: "创建时间", width: 150, sortable: true, dataIndex: 'createTime'},
	            {header: "存储过程名称", width: 150, sortable: true, dataIndex: 'proName',renderer:function(value){
	            		var newValue= value.replace(/huanhang/g, "\n")
									.replace(/yinhao/g, "'");
						return newValue;
	            }},
	            {header: "excel_name", width: 100, sortable: true, dataIndex: 'excelName'},
	            {header: "sheet_name", width: 100, sortable: true, dataIndex: 'sheetName'}
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
	            pageSize: 16,
	            emptyMsg: '未查询到记录',
	            displayInfo: true,
	            plugins: new Ext.ux.ProgressBarPager()
	        }),
	        autoExpandColumn: 'id',
	        //height:458,
	        title:'sql语句信息列表',
	        loadMask:true
	    });
	}
    
	var searchBar =new Ext.Panel({
		tbar: [
	               	'数据库：',
	               	query_databaseId,
                   'ID：',
                   query_id,           
                   'sql描述：',
                   query_subject,
                   'sql语句片段：',
                   query_sql,
                   'sql创建时间起：',
                   query_date1,
                   'sql创建时间止：',
                   query_date2,
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
               ]
	});
	var panel_ = new Ext.Panel({
        renderTo: 'searchdiv',
        height:document.getElementById('searchdiv').clientHeight ,
        autoScroll:'auto',
		iconCls:'query',
		layout:'border',
        items:[{
        	region:'north',
        	height:30,
        	item:searchBar
        },{
        	region:'center',
        	layout:'fit',
        	items:grid}]
    });
    //panel_.render();
    store.on("beforeload", function() {
				store.baseParams = {
						start:0,
						limit:16,
						query_databaseId:query_databaseId.getValue(),
						query_sql:query_sql.getValue(),
						query_date1 : query_date1.getValue(),
						query_date2 : query_date2.getValue(),
						query_id:query_id.getValue(),
						query_subject:query_subject.getValue(),
						start:0,
						limit:16
				};
			});
    store.load({params:{start:0, limit:16}});
});