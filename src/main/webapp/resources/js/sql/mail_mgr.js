/**
 * 	设备类型信息js
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


var sqlgrid = null;
var sqlstore = null;
var sqlwin = null;

var sfp = null;
var send_view = null;

var editFlag = 0;		//定义一个编辑的全局标记
//------------------------------------------------------------------------------------
/*
 * 按钮触发函数
 */
//定义查询条件
/*var  database_value = "[" +
		"['mysql', 'mysql库', 'The database mysql']," +
		"['td', 'TD库', 'The database td']," +
		"['mysql52', 'mysql52库', 'The database mysql52'] " +
		"]";
database_value = eval(database_value);
var database_store = new Ext.data.ArrayStore({
        fields: ['id', 'discribe', 'content'],
        data : database_value
    });*/


var database_store = new Ext.data.JsonStore({
    url: CTX + "/dbmgr/queryAllDbList.itm", //请求URL  
    storeId: "database_store",  //store的id  
    root: "rows",   //对应数据集的JSON KEY  
    autoLoad : true,    //自动加载  
    totalProperty: "totalCount",  
    fields : [  
        {name: 'id', mapping: 'id'},  
        {name: 'discribe', mapping: 'connName'},  
        {name: 'content', mapping: 'dbUrl'}
    ]  
});


var  isAvild_value = "[" +
		"['1', '有效']," +
		"['0', '无效'] " +
		"]";
isAvild_value = eval(isAvild_value);
var isAvild_store = new Ext.data.ArrayStore({
        fields: ['value', 'discribe'],
        data : isAvild_value
    });

var  dateBy_value = "[" +
		"['-2', '按月']," +
		"['-3', '按周']," +
		"['-1', '按日']" +
		"]";

dateBy_value = eval(dateBy_value);

var dateBy_store = new Ext.data.ArrayStore({
        fields: ['value', 'discribe'],
        data : dateBy_value
    });

var  isSuccess_value = "[" +
		"['1', '全部显示']," +
		"['0', '分页显示']" +
		"]";

isSuccess_value = eval(isSuccess_value);

var isSuccess_value = new Ext.data.ArrayStore({
        fields: ['value', 'discribe'],
        data : isSuccess_value
    });


var query_id = new Ext.form.TextField({
				id:'query_id',
				emptyText:'-ID-',
				width:40
			});
var query_isAvild = new Ext.form.ComboBox({
				id:'query_isAvild',
				store: isAvild_store,
				valueField:'value',				//combox id值
				displayField:'discribe',	 			//combox 显示值
				typeAhead: true,				//自动匹配。注：typeAheadDelay 参数--当typeAhead=true时 
				mode: 'local',	
				triggerAction: 'all',
				width:80,
				emptyText:'-请选择-',
				selectOnFocus:true
			});

var query_date = new Ext.form.ComboBox({
				id:'query_date',
				store: dateBy_store,
				valueField:'value',				//combox id值
				displayField:'discribe',	 			//combox 显示值
				typeAhead: true,				//自动匹配。注：typeAheadDelay 参数--当typeAhead=true时 
				mode: 'local',	
				triggerAction: 'all',
				width:80,
				emptyText:'-请选择-',
				selectOnFocus:true
			});

var query_isSuccess = new Ext.form.ComboBox({
				id:'query_isSuccess',
				store: isSuccess_value,
				valueField:'value',				//combox id值
				displayField:'discribe',	 			//combox 显示值
				typeAhead: true,				//自动匹配。注：typeAheadDelay 参数--当typeAhead=true时 
				mode: 'local',	
				triggerAction: 'all',
				width:80,
				emptyText:'-请选择-',
				selectOnFocus:true
			});

var query_subject = new Ext.form.TextField({
				id:'query_subject',
				emptyText:'-sql描述-',
				width:100
			});
var query_sqlIdList =  new Ext.form.TextField({
				id:'query_sqlIdList',
				emptyText:"-关联sql的Id,多个用','分隔-",
				width:100
			});
var query_sendto = new Ext.form.TextField({
				id:'query_sendto',
				emptyText:'-接收人-',
				width:100
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
//弹出窗口的查询条件定义
var query_databaseId = new Ext.form.ComboBox({
	                        	fieldLabel: '连接数据库',
	                        	id: 'query_databaseId',
	                        	name: 'query_databaseId',
	                        	store: database_store,
		                        valueField:'id',				//combox id值
		                        displayField:'discribe',	 			//combox 显示值
		                        editable:false,
		                        typeAhead: true,				//自动匹配。注：typeAheadDelay 参数--当typeAhead=true时 
		                        mode: 'local',					//采用本地数据加载，因combox在创建时，已加载了数据。这样可自动匹配数据
		                        triggerAction: 'all',
		                        emptyText:'-请选择连接的数据库-',
		                        width:100,
		                        //value:'mysql',
		                        selectOnFocus:true
       					})
var query_subject1 = new Ext.form.TextField({
				id:'query_subject1',
				emptyText:'-sql描述-',
				width:100
			});
var query_sql =  new Ext.form.TextField({
				id:'query_sql',
				emptyText:'-sql内容-',
				width:100
			});
var query_date3 = new Ext.form.DateField({
				id:'query_date3',
				emptyText:'-请选择-',
				format:'Y-m-d'
			});
var query_date4 = new Ext.form.DateField({
				id:'query_date4',
				emptyText:'-请选择-',
				format:'Y-m-d'
			});
//重置模糊查询
dataReset = function(){
	Ext.getCmp('query_id').setValue('');
	Ext.getCmp('query_subject').setValue('');
	Ext.getCmp('query_sqlIdList').setValue('');
	Ext.getCmp('query_sendto').setValue('');
	Ext.getCmp('query_date1').setValue('');
	Ext.getCmp('query_date2').setValue('');
	Ext.getCmp('query_isAvild').setValue('');
	
	Ext.getCmp('query_date').setValue('');
	Ext.getCmp('query_isSuccess').setValue('');
};			
dataReset2 = function(){
	Ext.getCmp('query_subject1').setValue('');
	Ext.getCmp('query_sql').setValue('');
	Ext.getCmp('query_date3').setValue('');
	Ext.getCmp('query_date4').setValue('');
};			
			
var query_date2 = new Ext.form.DateField({
				id:'query_date2',
				emptyText:'-请选择-',
				format:'Y-m-d'
			});


//新增表单提交方法
addSubmit = function(){
				//进度条
				var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在提交数据...'});
				//提交表单的信息 
				var sendto = afp.form.findField('sendto_add').getValue();	// 接收人
				var cc = afp.form.findField('cc_add').getValue();	// 抄送人
				var ruleTable = afp.form.findField('ruleTable_add').getValue();	// 关联表
				var ruleZeusTable = afp.form.findField('ruleZeusTable_add').getValue();
				var subject = afp.form.findField('subject_add').getValue();	// 主题
				var ruleIdList = afp.form.findField('ruleIdList_add').getValue();			//关联的sqlId
				//var isAvild = afp.form.findField('isAvild_add').getValue();			//是否有效
				var isAvild = afp.getForm().getValues()["isAvild_add"]
				var fen = afp.form.findField('fen_add').getValue();			//分
				if(fen==''){
					fen = '0';
				}
				var shi = afp.form.findField('shi_add').getValue();			//时
				if(shi==''){
					shi = '*';
				}
				var ri = afp.form.findField('ri_add').getValue();			//日
				if(ri==''){
					ri = '*';
				}
				var yue = afp.form.findField('yue_add').getValue();			//月
				if(yue==''){
					yue = '*';
				}
				var xingqi = afp.form.findField('xingqi_add').getValue();	//星期
				if(xingqi==''){
					xingqi = '?';
				}
				var crontab = fen+' '+shi+' '+ri+' '+yue+' '+xingqi;//表达式
				
				var ifSendMs = afp.getForm().getValues()['ifSendMs_add'];	// 是否发送短信
				var mobileNum = afp.form.findField('mobileNum_add').getValue();			//发送的手机号
				var msContent = afp.form.findField('msContent_add').getValue();//发送短信的内容
				var isSend   =  afp.getForm().getValues()['isSend_add'];	// 是否发送邮件
				var isAutomation   =  afp.getForm().getValues()['automation_add'];	// 是否发送邮件
				if(subject.trim().length<1){
					Ext.Msg.alert('提示','必须填写主题');
					return ;
				}
				if(sendto.trim().length<1){
					Ext.Msg.alert('提示','必须填写接收人信息');
					return ;
				}
				lm.show();
				var page = document.getElementById("ext-comp-1004").value;
				var index = (page-1)*10;
				Ext.Ajax.request({
		            		method: 'post',
		                	url: CTX + '/sqlmgr/addOrEditMailInfo.itm',
		                	params:{
		                		subject: subject,
		                		ruleIdList: ruleIdList,
		                		isAvild: isAvild,
		                		crontab: crontab,
		                		sendto:sendto,
		                		cc:cc,
		                		ruleTable:ruleTable,
		                		ifSendMs:ifSendMs,
		                		mobileNum:mobileNum,
		                		msContent:msContent,
		                		isSend:isSend,
		                		isAutomation:isAutomation,
		                		ruleZeusTable:ruleZeusTable
		                	},
		 					success:function(result){
		 							responseStr = result.responseText;
		 							lm.hide();
		 							if(responseStr=='1'){
		 								Ext.Msg.alert('提示','添加成功');
		 								addwin.hide();
		 								store.load({params:{start:index, limit:10}});
		 							}else{
		 								Ext.Msg.alert('提示','添加失败');
		 							}
		 					}
		             });
			};
//取消按钮触发函数
cancelfun = function(){
				if(addwin!=null&&addwin.isVisible()){
			    	addwin.hide();
					addwin.setActive(true);
			    }	           
};



//修改表单提交方法
editSubmit = function (){
				//进度条
				var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在提交数据...'});
				var id = efp.form.findField('id').getValue();						//id
				//提交表单的信息 
				var sendto = efp.form.findField('sendto_edit').getValue();	// 接收人
				var cc = efp.form.findField('cc_edit').getValue();	//抄送人
			 	var ruleTable = efp.form.findField('ruleTable_edit').getValue();	// 关联表
			 	var ruleZeusTable =  efp.form.findField('ruleZeusTable_edit').getValue();
				var subject = efp.form.findField('subject_edit').getValue();	// 主题
				var ruleIdList = efp.form.findField('ruleIdList_edit').getValue();			//关联的sqlId
				//var isAvild = efp.form.findField('isAvild_add').getValue();			//是否有效
				var isAvild = efp.getForm().getValues()["isAvild_edit"]
				var fen = efp.form.findField('fen_edit').getValue();			//分
				var isSend = efp.getForm().getValues()["isSend_edit"];
				var isAutomation = efp.getForm().getValues()['automation_edit'];	
				var ifSendMs = efp.getForm().getValues()['ifSendMs_edit'];	// 是否发送短信
				var mobileNum = efp.form.findField('mobileNum_edit').getValue();			//发送的手机号
				var msContent = efp.form.findField('msContent_edit').getValue();//发送短信的内容
				
				if(fen==''){
					fen = '*';
				}
				var shi = efp.form.findField('shi_edit').getValue();			//时
				if(shi==''){
					shi = '*';
				}
				var ri = efp.form.findField('ri_edit').getValue();			//日
				if(ri==''){
					ri = '*';
				}
				var yue = efp.form.findField('yue_edit').getValue();			//月
				if(yue==''){
					yue = '*';
				}
				var xingqi = efp.form.findField('xingqi_edit').getValue();	//星期
				if(xingqi==''){
					xingqi = '?';
				}
				var crontab = fen+' '+shi+' '+ri+' '+yue+' '+xingqi;//表达式
				
				if(subject.trim().length<1){
					Ext.Msg.alert('提示','必须填写主题');
					return ;
				}
				if(sendto.trim().length<1){
					Ext.Msg.alert('提示','必须填写接收人信息');
					return ;
				}
				//lm.show();
				//进度条
				lm.show();
				var page = document.getElementById("ext-comp-1004").value;
				var index = (page-1)*10;
				Ext.Ajax.request({
		            		method: 'post',
		            		url: CTX + '/sqlmgr/addOrEditMailInfo.itm',
		                	params:{
		                		id: id,
		                		subject: subject,
		                		ruleIdList: ruleIdList,
		                		isAvild: isAvild,
		                		crontab: crontab,
		                		sendto:sendto,
		                		cc:cc,
		                        ruleTable: ruleTable,
		                		ifSendMs:ifSendMs,
		                		mobileNum:mobileNum,
		                		msContent:msContent,
		                		isSend   : isSend,
		                		isAutomation: isAutomation,
		                		ruleZeusTable:ruleZeusTable
		                	},
		 					success:function(result){
		 							responseStr = result.responseText;
		 							lm.hide();
		 							if(responseStr=='2'){
		 								Ext.Msg.alert('提示','修改成功');
		 								editwin.hide();
		 								store.load({params:{start:index, limit:10}});
		 							}else{
		 								Ext.Msg.alert('提示','修改失败');
		 							}
		 					}
		             });
}

//删除记录方法
delfun = function (){
	//判断是否选择了一条记录
	if(grid.getSelectionModel().getCount()==0){
		Ext.MessageBox.alert('提示','请选择最少一条记录！');
	}
	else{
	   //获取grid record
	   var record = grid.getSelectionModel().getSelected();
	   if (grid.getSelectionModel().hasSelection()){
		   var records=grid.getSelectionModel().getSelections();
		   var mycars = '';
		   for(var i=0;i<records.length;i++){
		   	  var tempd ='';
		   	  tempd = records[i].data.id;

			  mycars = mycars + tempd;
			  if(i<records.length-1){
				  mycars = mycars + ",";
			  }
		   }
		   }
		var page = document.getElementById("ext-comp-1004").value;
		var index = (page-1)*10;
	   var id = mycars;				//id
	   Ext.Msg.confirm('提示','确定要删除吗？',function(btn){
  				if(btn=='yes'){
	  				Ext.Ajax.request({
	  					url: CTX + '/sqlmgr/updateDeletedDqcRule.itm',
	  					method: 'post',
	  					params:{
	  						id: id
	  					},
	  					success : function(result) {
	  						responseStr = result.responseText;
	  						if(responseStr == "1"){
	  							Ext.Msg.alert('提示','删除成功');	
	  							store.load({params:{start:index, limit:10}});
	  						}
	  						else{
	  							Ext.Msg.alert('提示','删除未成功');
	  						}
	  					}
	  				});
  				}		
  			});
	   
	}
}
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
							 	  xtype: 'textfield',
							      fieldLabel: '主题',
							      id: 'subject_add',
							      name: 'subject_add',
							      allowBlank : false,			//不允许为空
							      blankText : '主题不能为空',
							      width:650,
			                      maxLength:500
						      } ,
						      {
					            layout:'column',
					            items:[{
						                columnWidth:.8,
						                layout: 'form',
						                items: [{
						                	      xtype: 'textfield',
											      fieldLabel: '关联的规则',
											      id: 'ruleIdList_add',
											      name: 'ruleIdList_add',
											      width:500,
							                      maxLength:500 
						                } 
					                 ]
						            },{
						                columnWidth:.2,
						                layout: 'form',
						                items: [{
						                      buttons: [{
										            text: '点击关联sql',
										            handler: selectSqlFun
										        } ]
						                }]
						            }]
		                  		},{
								  xtype: 'textfield',
								  fieldLabel: '依赖zeus作业ID',
								  id: 'ruleZeusTable_add',
								  name: 'ruleZeusTable_add',
								  allowBlank : true,			//不允许为空
								  blankText : '',
								  width:650,
								  maxLength:500
							  },{
		                       	  xtype: 'textfield',
							      fieldLabel: '关联表 用;分隔',
							      id: 'ruleTable_add',
							      name: 'ruleTable_add',
							      allowBlank : false,			//不允许为空
							      blankText : '关联表信息不能为空',
							      width:650,
			                      maxLength:500
						      },
						      	{
		                       	  xtype: 'textarea',
							      fieldLabel: '接收人 用;分隔',
							      id: 'sendto_add',
							      name: 'sendto_add',
							      allowBlank : false,			//不允许为空
							      blankText : '接收人信息不能为空',
							      width:650,
			                      maxLength:500
						      },{
		                       	  xtype: 'textarea',
							      fieldLabel: '抄送人 用;分隔',
							      id: 'cc_add',
							      name: 'cc_add',
							      allowBlank : true,			//不允许为空
							      blankText : '抄送人信息不能为空',
							      width:650,
			                      maxLength:500
						      },{
	            				fieldLabel: '执行表达式',
					            layout:'column',
					            items:[{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '分',
					                    width:30,
					                    id: 'fen_add',
					                    minValue:0,
					                    maxValue:60
					                }  ]
					            },{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '时',
					                    width:30,
					                    id: 'shi_add',
					                    minValue:0,
					                    maxValue:24
					                } ]
					            },{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '日',
					                    width:30,
					                    id: 'ri_add',
					                    minValue:1,
					                    maxValue:31
					                } ]
					            },{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '月',
					                    width:30,
					                    id: 'yue_add',
					                    minValue:0,
					                    maxValue:12
					                } ]
					            },{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '星期',
					                    width:30,
					                    id: 'xingqi_add',
					                    minValue:0,
					                    maxValue:7
					                } ]
					            }]
				         },{
				            // Use the default, automatic layout to distribute the controls evenly
				            // across a single row
				            xtype: 'radiogroup',
				            fieldLabel: '是否有效',
				            width:120,
				            items: [
				                {boxLabel: '无效', name: 'isAvild_add',inputValue: 0},
				                {boxLabel: '有效', name: 'isAvild_add',inputValue: 1,checked: true}
				            ]
				        },{    xtype: 'radiogroup',
						            fieldLabel: '是否自动化',
						            hidden:true,
						            width:120,
						            items: [
						                {boxLabel: '否', name: 'automation_add',inputValue: 0,checked:true},
						                {boxLabel: '是', name: 'automation_add',inputValue: 1}
					                ]},{
			                	    xtype: 'radiogroup',
						            fieldLabel: '空值发送',
						            width:120,
						            items: [
						                {boxLabel: '否', name: 'isSend_add',inputValue: 0},
						                {boxLabel: '是', name: 'isSend_add',inputValue: 1,checked:true}
					                ]},{
				            xtype: 'radiogroup',
				            hidden:true,
				            fieldLabel: '是否发送短信',
				            items: [
				                {boxLabel: '否', name: 'ifSendMs_add',inputValue: 0,checked: true},
				                {boxLabel: '是', name: 'ifSendMs_add',inputValue: 1}
				            ]
				        },{
		                       	  xtype: 'textfield',
							      fieldLabel: '手机号 用;分隔',
							      hidden:true,
							      id: 'mobileNum_add',
							      name: 'mobileNum_add',
							      width:650,
							      allowBlank : true			//允许为空
							      
						},{
							 	  xtype: 'textarea',
							      fieldLabel: '短信内容',
							  	  hidden:true,
							      id: 'msContent_add',
							      name: 'msContent_add',
							      allowBlank : true,			//允许为空
							      width:650,
			                      maxLength:500
						 }
	            	],
	             bbar:['->',{
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
	
            	var  top = document.body.scrollTop+(document.body.clientHeight/2-addwin.height/2);
				addwin.setPosition((document.body.clientWidth/2-addwin.width/2) ,top);
            	addwin.show();
};
//sendMailFun = function (){
sendMailFun = function (value){

	//判断是否选择了一条记录
	//判断是否选择了一条记录
	if(grid.getSelectionModel().getCount()<1){
		Ext.MessageBox.alert('提示','请最少选择一条记录！');
		return false;
	}
//	else{*/
		
		//判断查看窗口是否已生成
		//var id = grid.getSelectionModel().getSelected().get('id');
		var id = value;
		//var record = grid.getSelectionModel().getSelected();
		if(sfp == null){
			sfp = new Ext.form.FormPanel({
		        id:'sfp',
	        	frame: true,
		        labelWidth: 90,
		        labelAlign: 'right',
		        items:[ 
		        new Ext.form.Hidden({
			       							name:'mail_id',
			       							id:'mail_id'
				                	}),
				                	{
									 	  xtype: 'textfield',
									      fieldLabel: 'sql参数',
									      id: 'sqlParam_now',
									      name: 'sqlParam_now',
									      width:400
								 },
				                {
							 	  xtype: 'textarea',
							      fieldLabel: '收件人地址,用;分隔',
							      id: 'mail_now',
							      name: 'mail_now',
							      width:400,
							      height : 150
						      }   
			        ],
		            bbar:[ {
		            			text:'发送',
		                		iconCls:'save',
		                		handler:mailnowSubmit
		                 },'-',{
		                		text:'取消',
		                		iconCls:'cancel',
		                		handler:function(){
		                				if(send_view!=null&&send_view.isVisible()){
		                					send_view.hide();
											send_view.setActive(true);
		                				}
		                			}
		                  }]
		    	});
		}
			
	    	if (send_view == null) {
				send_view = new Ext.Window({
							id : 'send_viewid',
							title : '填写收件人地址',
							iconCls:'option',
				            layout:'fit',
							width : 550,
							height : 250,
							plain : true,
							autoScroll : true,
							modal : true, // 模态
							closeAction : 'hide', // 隐藏窗口；'close'为destroy窗口
							collapsible : true,
							items : sfp
						});
			}
		
		
 	   //sfp.form.findField('mail_id').setValue(record.get('id'));
 	   sfp.form.findField('mail_id').setValue(id);
		var top = document.body.scrollTop
				+ (document.body.clientHeight / 2 - send_view.height / 2);
		send_view.setPosition(
				(document.body.clientWidth / 2 - send_view.width / 2), top);
		send_view.show();
		
	 
	//}

}

updateShell = function(){
	 
		var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在连接远程主机刷新crontab'});
		lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: 'sqlmgr.itm?method=updateShell',
		 					success:function(result){
		 							responseStr = result.responseText;
		 							lm.hide();
		 							if(responseStr=='1'){
		 								//Ext.Msg.alert('提示','连接远程服务器成功，未发送邮件成功，发送的后台功能还在开发中');
		 								
		 								Ext.Msg.alert('提示','刷新crontab成功');
		 								//send_view.hide();
		 							}else{
		 								Ext.Msg.alert('提示','刷新crontab失败');
		 							}
		 					},
		 					failure: function(result) {    
                             lm.hide(); 
                             Ext.Msg.alert('提示','刷新crontab失败');
                      }  
		      });
}

mailnowSubmit = function(){
	
	if (grid.getSelectionModel().hasSelection()){
		   var records=grid.getSelectionModel().getSelections();
		   var mycars = '';
		   for(var i=0;i<records.length;i++){
		   	  var tempd ='';
		   	  tempd = records[i].data.id;

			  mycars = mycars + tempd;
			  if(i<records.length-1){
				  mycars = mycars + ",";
			  }
		   }
		   }
		var id = mycars;				//id
	//提交表单的信息 
	var mailstr = sfp.form.findField('mail_now').getValue();	// 接收人
	var sqlParam =sfp.form.findField('sqlParam_now').getValue();
	//alert(mailstr);
		var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在发送邮件...'});
		lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: CTX + '/sqlmgr/sendMail.itm',
		                	params:{
		                		id: id,
		                		mailstr:mailstr,
		                		sqlParam : sqlParam
		                	},
		 					success:function(result){
		 							lm.hide();
		 							responseStr = result.responseText;
		 							if(responseStr=='1'){
		 								//Ext.Msg.alert('提示','连接远程服务器成功，未发送邮件成功，发送的后台功能还在开发中');
		 								
		 								Ext.Msg.alert('提示','发送邮件成功');
		 								send_view.hide();
		 							}else if(responseStr=='-999'){
		 								Ext.Msg.alert('提示','数据为空不发送邮件');
		 								send_view.hide();
		 							}else{
		 								//Ext.Msg.alert('提示','发送邮件失败');
		 								Ext.Msg.alert('提示','已经增加进入线程队列,立即执行,请稍后!');
		 								send_view.hide();
		 							}
		 							//Ext.Msg.alert('提示','操作执行成功');
		 					},
		 					failure: function(result) {    
                             lm.hide(); 
                            // Ext.Msg.alert('提示','发送邮件失败');
                             Ext.Msg.alert('提示','操作执行结束,该邮件关联的sql执行时间较长，因邮件要在sql执行完成后才能发送，所以要等比较长的时间才能收到邮件');
                      }  
		      });
}
//弹出修改窗口
editfun = function (){
	//判断是否选择了一条记录
	if(grid.getSelectionModel().getCount()!=1){
		Ext.MessageBox.alert('提示','请选择一条记录！');
	}
	else{
		editFlag = 1;
	   //获取选中记录
	   var record = grid.getSelectionModel().getSelected();
		
	   var id = record.get('id');				//id

		
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
	                		}),
	                			{
							 	  xtype: 'textfield',
							      fieldLabel: '主题',
							      id: 'subject_edit',
							      name: 'subject_edit',
							      allowBlank : false,			//不允许为空
							      blankText : '主题不能为空',
							      width:650,
			                      maxLength:500
						      }   ,{
					            layout:'column',
					            items:[{
						                columnWidth:.8,
						                layout: 'form',
						                items: [{
						                	    xtype: 'textfield',
											      fieldLabel: '关联的规则',
											      id: 'ruleIdList_edit',
											      name: 'ruleIdList_edit',
											      width:500,
							                      maxLength:500 
						                } 
					                 ]
						            },{
						                columnWidth:.2,
						                layout: 'form',
						                items: [{
						                      buttons: [{
										            text: '点击关联sql',
										            handler: selectSqlFun
										        } ]
						                }]
						            }]
		                  		}
								,{
								  xtype: 'textfield',
								  fieldLabel: '依赖zeus作业ID',
								  id: 'ruleZeusTable_edit',
								  name: 'ruleZeusTable_edit',
								  allowBlank : true,			//不允许为空
								  blankText : '',
								  width:650,
								  maxLength:500
							   },{
		                       	  xtype: 'textfield',
							      fieldLabel: '关联表',
							      id: 'ruleTable_edit',
							      name: 'ruleTable_edit',
							      allowBlank : true,			//不允许为空
							      blankText : '接收人信息不能为空',
							      width:650,
			                      maxLength:500
						      },{
		                       	  xtype: 'textarea',
							      fieldLabel: '接收人',
							      id: 'sendto_edit',
							      name: 'sendto_edit',
							      allowBlank : false,			//不允许为空
							      blankText : '接收人信息不能为空',
							      width:650,
			                      maxLength:500
						      },{
		                       	  xtype: 'textarea',
							      fieldLabel: '抄送人',
							      id: 'cc_edit',
							      name: 'cc_edit',
							      allowBlank : false,			//不允许为空
							      blankText : '接收人信息不能为空',
							      width:650,
			                      maxLength:500
						      },{
	            				fieldLabel: '执行表达式',
					            layout:'column',
					            items:[{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '分',
					                    width:30,
					                    id: 'fen_edit',
					                    minValue:0,
					                    maxValue:60
					                }  ]
					            },{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '时',
					                    width:30,
					                    id: 'shi_edit',
					                    minValue:0,
					                    maxValue:24
					                } ]
					            },{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '日',
					                    width:30,
					                    id: 'ri_edit',
					                    minValue:1,
					                    maxValue:31
					                } ]
					            },{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '月',
					                    width:30,
					                    id: 'yue_edit',
					                    minValue:0,
					                    maxValue:12
					                } ]
					            },{
					                columnWidth:.2,
					                layout: 'form',
					                items: [{
					                    xtype:'textfield',
					                    fieldLabel: '星期',
					                    width:30,
					                    id: 'xingqi_edit',
					                    minValue:0,
					                    maxValue:7
					                } ]
					            }]
				         },{
				            // Use the default, automatic layout to distribute the controls evenly
				            // across a single row
				            xtype: 'radiogroup',
				            fieldLabel: '是否有效',
				            width:120,
				            items: [
				                {boxLabel: '无效', name: 'isAvild_edit',inputValue: 0},
				                {boxLabel: '有效', name: 'isAvild_edit',inputValue: 1}
				            ]
						},{    xtype: 'radiogroup',
						            fieldLabel: '是否自动化',
						            hidden:true,
						            width:120,
						            items: [
						                {boxLabel: '否', name: 'automation_edit',inputValue: 0},
						                {boxLabel: '是', name: 'automation_edit',inputValue: 1}
					                ]},{
			                	    xtype: 'radiogroup',
						            fieldLabel: '空值发送',
						            width:120,
						            items: [
						                {boxLabel: '否', name: 'isSend_edit',inputValue: 0},
						                {boxLabel: '是', name: 'isSend_edit',inputValue: 1}
							     ]},
							     {
						            xtype: 'radiogroup',
						           	hidden:true,
						            fieldLabel: '是否发送短信',
						            items: [
						                {boxLabel: '否', name: 'ifSendMs_edit',inputValue: 0},
						                {boxLabel: '是', name: 'ifSendMs_edit',inputValue: 1}
						            ]
						        },{
		                       	  xtype: 'textfield',
		                       	  hidden:true,
							      fieldLabel: '手机号 用;分隔',
							      id: 'mobileNum_edit',
							      name: 'mobileNum_edit',
							      width:650,
							      allowBlank : true			//允许为空
							      
						},{
							 	  xtype: 'textarea',
							 	  hidden:true,
							      fieldLabel: '短信内容',
							      id: 'msContent_edit',
							      name: 'msContent_edit',
							      allowBlank : true,			//允许为空
							      width:650,
			                      maxLength:500
						 }
					],
		            bbar:['->',{
		            			text:'保存',
		                		iconCls:'save',
		                		handler:editSubmit
		                 },'-',{
		                		text:'取消',
		                		iconCls:'cancel',
		                		handler:function(){
		                				if(editwin!=null&&editwin.isVisible()){
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
			                    width:780,
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
	   efp.form.findField('id').setValue(record.get('id'));
	   efp.form.findField('subject_edit').setValue(record.get('subject'));
	   efp.form.findField('isAvild_edit').setValue(record.get('isAvild'));
	   var crontabValue = record.get('crontab');
	   c_values = crontabValue.split(' ');
	   if(c_values[0]=='*'){
	   	  c_values[0] = '';
	   }
	   if(c_values[1]=='*'){
	   	  c_values[1] = '';
	   }
	   if(c_values[2]=='*'){
	   	  c_values[2] = '';
	   }
	   if(c_values[3]=='*'){
	   	  c_values[3] = '';
	   }
	   if(c_values[4]=='*'){
	   	  c_values[4] = '';
	   }
	   efp.form.findField('fen_edit').setValue(c_values[0]);
	   efp.form.findField('shi_edit').setValue(c_values[1]);
	   efp.form.findField('ri_edit').setValue(c_values[2]);
	   efp.form.findField('yue_edit').setValue(c_values[3]);
	   efp.form.findField('xingqi_edit').setValue(c_values[4]);
	   efp.form.findField('sendto_edit').setValue(record.get('sendto'));
	   efp.form.findField('cc_edit').setValue(record.get('cc'));
	   efp.form.findField('ruleTable_edit').setValue(record.get('ruleTable'));
	   efp.form.findField('ruleZeusTable_edit').setValue(record.get('ruleZeusTable'));
	   efp.form.findField('ruleIdList_edit').setValue(record.get('ruleIdList'));
	   //efp.findByType('radiogroup')[0].setValue(record.get('isAvild'));
	   efp.form.findField('automation_edit').setValue(record.get('isAutomation'));
	  // efp.findByType('radiogroup')[1].setValue(record.get('isAutomation'));
	    efp.form.findField('isSend_edit').setValue(record.get('isSend'));
	  // efp.findByType('radiogroup')[2].setValue(record.get('isSend'));
	  // efp.findByType('radiogroup')[3].setValue(record.get('ifSendMs'));
	    efp.form.findField('ifSendMs_edit').setValue(record.get('ifSendMs'));
	   efp.form.findField('mobileNum_edit').setValue(record.get('mobileNum'));
	   efp.form.findField('msContent_edit').setValue(record.get('msContent'));
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
		if(viewwin == null){
			//初始化viewstore
			viewstore = new Ext.data.GroupingStore({
    					proxy: new Ext.data.HttpProxy({url:'deviceconfig.itm?method=viewDeviceInfo'}),
        				reader: new Ext.data.JsonReader(
        					{root:'root'},[
	           								{name: 'name'},
	           								{name: 'value'},
	           								{name: 'group'}
       	 				]),
       	 				groupField:'group'
    		});
			//初始化viewgrid
			viewgrid = new Ext.grid.GridPanel({
			        store: viewstore,
			        columns: [
			            {id:'name',header: "名称", width: 30,dataIndex: 'name',align:'center'},
			            {header: "值", width: 100, dataIndex: 'value',align:'left'},
			            {header: "设备信息", width: 400, hidden:true, dataIndex: 'group'}
			            
			        ],
			        view: new Ext.grid.GroupingView({
            			forceFit:true
        			}),
			        height:458,
			        width:1144,
			        title:'设备详情列表',
			        loadMask:true
			    });

			    viewwin = new Ext.Window({
			        		title: '查询',
			            	iconCls:'view',
				            layout:'fit',
				            width:530,
				            height:327,
				            plain : true,
				            modal:true,
				            closeAction : 'hide',
				            collapsible:true,
				            items:viewgrid
			   });
		   	
		}
		
		var top = document.body.scrollTop+(document.body.clientHeight/2-viewwin.height/2);
		viewwin.setPosition((document.body.clientWidth/2-viewwin.width/2) ,top);
		viewwin.show();
	}
	var record = grid.getSelectionModel().getSelected();
	viewstore.load({params:{id:record.get('id')}});

}
//选择sql的函数
selectSqlFun = function (){
		  
		var s = '';
		if(editFlag==1){
			var lastValue = efp.form.findField('ruleIdList_edit').getValue();
			s = lastValue;
			editFlag = 0;
		}
		
		if(sqlwin!=null){
			sqlwin.show();
			sqlgrid.setTitle("sql列表(<font color='red'>关联的sqlId："+s+")</font>");
		}
		
		                                       
		
		
		//判断是否已生成
		if(sqlwin == null){
			 var sm = new Ext.grid.CheckboxSelectionModel();  
			  
		     var cm = new Ext.grid.ColumnModel( 
		        [ 
		            sm, 
		            {id:'name',header: "id", width: 50,dataIndex: 'id'},
			        {header: "描述", width: 150, dataIndex: 'subject'},
			        {header: "sql语句", width: 560,  dataIndex: 'ruleSql'} 
		        ] 
		     ); 

			//初始化viewstore
			sqlstore = new Ext.data.GroupingStore({
    					proxy: new Ext.data.HttpProxy({ url:CTX+'/sqlmgr/querySqlInfo.itm'}),
        				reader: new Ext.data.JsonReader(
        					{totalProperty:'totalCount',root:'rows'},[
				            {name: 'id'},
				            {name: 'subject'},
				            {name: 'ruleSql'} 
       					 ]) 
    		});
			//初始化sqlgrid
			sqlgrid = new Ext.grid.GridPanel({
			        store: sqlstore,
			        iconCls:'head',
			        region:'center',
			        layout:'fit',
			        /*columns: [
			            {id:'name',header: "id", width: 50,dataIndex: 'id'},
			            {header: "描述", width: 100, dataIndex: 'subject'},
			            {header: "sql语句", width: 400,  dataIndex: 'ruleSql'}
			            
			        ],*/
			        cm:cm,
			        selModel:sm,  
			        view: new Ext.grid.GroupingView({
            			forceFit:true
        			}),
			        //height:330,
			        //width:850,
			        title:"sql列表 (<font color='red'>关联的sqlId："+s+")</font>",
			        loadMask:true,
			        bbar: new Ext.PagingToolbar({
			            store: sqlstore,
			            pageSize: 10,
			            emptyMsg: '未查询到记录',
			            displayInfo: true
			        })
			    });
				var sql_panel = new Ext.Panel({
				        title:'搜索sql',
				        height:400,
				        width:830,
				        autoScroll:true,
						iconCls:'query',
						layout:'border',
				        tbar: [
				        	'数据库:',
				        	query_databaseId,
				            '描述：',
				            query_subject1,
				            '内容:',
				            query_sql,
				            '创建时间:',
				            query_date3,
				            '至',
				            query_date4,
				            {
					           text:'查询',
					           tooltip:'提交查询',
					           iconCls:'view',
					           handler: function() {
								    sqlstore.load();//点击执行函数
							   }		
					        },
					        {
					           text:'重置',
					           tooltip:'内容重置',
					           iconCls:'view',
					           handler: dataReset2		//点击执行函数
					        },
					        {
						           text:'确定',
						           tooltip:'确定',
						           iconCls:'view',
						           handler: getCheckGridValue		//点击执行函数
						     }
				        ]
				        ,
				         items:[sqlgrid]
				    });
				    sqlstore.on("beforeload", function() {
						sqlstore.baseParams = {
								start:0,
								limit:10,
								query_databaseId:query_databaseId.getValue(),
								query_sql:query_sql.getValue(),
								query_date1 : query_date3.getValue(),
								query_date2 : query_date4.getValue(),
								query_subject:query_subject1.getValue()
						};
					});
			    sqlwin = new Ext.Window({
			        		title: 'sql搜索',
			            	iconCls:'view',
				            layout:'fit',
				            width:850,
				            height:450,
				            plain : true,
				            modal:true,
				            closeAction : 'hide',
				            hideMode : 'offsets',
				            collapsible:true,
				            items:sql_panel
			   });
		   	
		
		var top = document.body.scrollTop+(document.body.clientHeight/2-sqlwin.height/2);
		sqlwin.setPosition((document.body.clientWidth/2-sqlwin.width/2) ,top);
		sqlwin.show();
	}

}
//获得checkgrid中id的值
function getCheckGridValue(){
	  if (sqlgrid.getSelectionModel().hasSelection()){
		   var records=sqlgrid.getSelectionModel().getSelections();
		   var mycars = '';
		   for(var i=0;i<records.length;i++){
		   	  var tempd ='';
		   	 /* if(Ext.getCmp('query_databaseId').getValue()=='mysql'){
		   	  	tempd = 'ora'+records[i].data.id;
		   	  }
		   	  if(Ext.getCmp('query_databaseId').getValue()=='td'){
		   	  	tempd ='td'+records[i].data.id;
		   	  }*/
		   	  tempd = records[i].data.id;
			  mycars = mycars + tempd;
			  if(i<records.length-1){
				  mycars = mycars + ",";
			  }
		   }
		   var before_value = '';
		   if(addwin!=null&&addwin.isVisible()){
		   		before_value = afp.form.findField('ruleIdList_add').getValue();
		   }
		   if(editwin!=null&&editwin.isVisible()){
		   		before_value = efp.form.findField('ruleIdList_edit').getValue();
		   }
		   if(mycars != ''&&before_value!=''){//选择了值
		   		before_value = before_value + ",";
		   }
		   if(addwin!=null&&addwin.isVisible()){
		   		Ext.getCmp('ruleIdList_add').setValue(before_value+mycars);
		   }
		   if(editwin!=null&&editwin.isVisible()){
		   		Ext.getCmp('ruleIdList_edit').setValue(before_value+mycars);
		   }
		   sqlwin.hide();
		   sqlgrid.getSelectionModel().clearSelections() ;
		   return mycars;
	 }else{
		   alert('请选中要操作的记录!');
	 }
};
function startJob(id,jobStatus,jobIsAvild){
	var theUser = document.getElementById("hideUserName").value;
	if(theUser!='bi_dev'){
		Ext.Msg.alert('提示','sorry,你没有该权限');
		return;
	}
	if(jobStatus==0){
		Ext.Msg.alert('提示','该job的状态是运行，要重新开始运行请先停止');
		return;
	}
	/*if(jobIsAvild==0){
		Ext.Msg.alert('提示','该job目前是无效的，要开始运行请先修改其属性为有效');
		return;
	}*/
	var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在开启job'});
	var page = document.getElementById("ext-comp-1004").value;
	var index = (page-1)*10;
	var db = 
		lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: CTX + '/sqlmgr/scheduleJob.itm',
		                	params:{
		                		jobId: id,
		                		type: 'mail',
		                		db:'mysql'
		                	},
		 					success:function(result){
		 							var respText = Ext.util.JSON.decode(result.responseText);                                                 
                             		var r = respText.r; 
		 							lm.hide();
		 							if(r=='1'){
		 								//Ext.Msg.alert('提示','连接远程服务器成功，未发送邮件成功，发送的后台功能还在开发中');
		 								
		 								Ext.Msg.alert('提示','开启job成功');
		 								store.load({params:{start:index, limit:10}});
		 								
//		 								store.load();
		 								//send_view.hide();
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
function stopJob(id,jobStatus,jobIsAvild){
	var theUser = document.getElementById("hideUserName").value;
	if(theUser!='bi_dev'){
		Ext.Msg.alert('提示','sorry,你没有该权限');
		return;
	}
	if(jobStatus==-1){
		Ext.Msg.alert('提示','该job的状态是停止');
		return;
	}
	/*if(jobIsAvild==1){
		Ext.Msg.alert('提示','该job目前是有效的，要停止请先修改其属性为无效');
		return;
	}*/
	var lm = new Ext.LoadMask(Ext.getBody(),{msg:'正在停止job'});
	
	var page = document.getElementById("ext-comp-1004").value;
	var index = (page-1)*10;
	var db = 
		lm.show();
				Ext.Ajax.request({
		            		method: 'post',
		                	url: CTX + '/sqlmgr/removeJob.itm',
		                	params:{
		                		jobId: id,
		                		type: 'mail',
		                		db:'mysql'
		                	},
		 					success:function(result){
		 							var respText = Ext.util.JSON.decode(result.responseText);                                                 
                             		var r = respText.r; 
		 							lm.hide();
		 							if(r=='1'){
		 								//Ext.Msg.alert('提示','连接远程服务器成功，未发送邮件成功，发送的后台功能还在开发中');
		 								
		 								Ext.Msg.alert('提示','停止job成功');
		 								
//		 								store.load();
		 								store.load({params:{start:index, limit:10}});
		 								//send_view.hide();
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
function showbutton(value, cellmeta, record){
	//var jobStatus = record.data["jobState"];
	var jobStatus = record.data.jobState;
	//var jobIsAvild = record.data["isAvild"];
	var jobIsAvild = record.data.isAvild;
//alert(value); +"<INPUT type=button value=执行sql onclick=exceSql('"+value+"')>"
var returnStr = "<INPUT type=button value=开始job onclick=startJob('"+value+"',"+"'"+jobStatus+"',"+"'"+jobIsAvild+"')>"
+"<INPUT type=button value=停止job onclick=stopJob('"+value+"',"+"'"+jobStatus+"',"+"'"+jobIsAvild+"')>"
+"<INPUT type=button value=立即发送邮件  onclick=sendMailFun('"+value+"')>";
return returnStr;
}

Ext.onReady(function(){
	var theUser = document.getElementById("hideUserName").value;
//--------------------------------------------
	Ext.BLANK_IMAGE_URL = CTX + '/resources/js/ext/resources/images/default/s.gif';
	var sm = new Ext.grid.CheckboxSelectionModel();  
	var cm = new Ext.grid.ColumnModel( 
		        [ 
		            sm, 
		            new Ext.grid.RowNumberer({header : "序号",width : 40,
		            	renderer: function (value, metadata, record, rowIndex) {
		            	return record_start + 1 + rowIndex;
		            	} 
		            }),
		    {header:"操作",dataIndex:"id",width:400,renderer:showbutton},
            {id:'id',header: "id", width: 20, sortable: true, dataIndex: 'id'},
            {header: "sql描述", width: 200, sortable: true, dataIndex: 'subject'},
            {header: "邮件所关联的规则id", width: 150,sortable: true, dataIndex: 'ruleIdList'},
            {header: "接收人", width: 300, sortable: true, dataIndex: 'sendto'},
            {header: "抄送人", width: 300, sortable: true, dataIndex: 'cc'},
            {header: "执行表达式", width: 100, sortable: true, dataIndex: 'crontab'},
           /* {header: "邮件发送状况", id:"isSuccess",width: 150, sortable: true, dataIndex: 'isSuccess',renderer:function(value){
            	if(value=='0'){
            		return '未执行';
            	}else if(value=='1'){
            		return '执行中';
            	}else if(value=='2'){
            		return '失败';
            	}else{
            		return '成功';
            	}
            }},*/
            {header: "是否有效", id:"isAvild",width: 150, sortable: true, dataIndex: 'isAvild',renderer:function(value){
            	if(value=='1'){
            		return '有效';
            	}else{
            		return '无效';
            	}
            }},

           {header: "空的excel是否发送", id:"isSend",width: 150, sortable: true, dataIndex: 'isSend',renderer:function(value){
            	if(value=='1'){
            		return '是';
            	}else{
            		return '否';
            	}
            }}    /**
            {header: "是否发送短信", id:"ifSendMs",width: 150, sortable: true, dataIndex: 'ifSendMs',renderer:function(value){
            	if(value=='1'){
            		return '发送';
            	}else{
            		return '不发送';
            	}
            }},
            {header: "手机号码", width: 300, sortable: true, dataIndex: 'mobileNum'}, */
             /*{header: "状态",  id:"jobState",width: 80, sortable: true, dataIndex: 'jobState',renderer:function(value){
            		if(value == 0){
            			return '运行中'
            		}else if(value == -1){
            			return '停止'
            		}else if(value == 1){
            			return '暂停'
            		}else if(value == 3){
            			return '错误'
            		}else if(value == 2){
            			return '完成'
            		}else if(value == 4){
            			return '阻塞'
            		}else{
            			return '未知'
            		}
            	}
            }*/
		        ] 
		     ); 

		//查询列表stroe 加载
	store = new Ext.data.GroupingStore({
		proxy: new Ext.data.HttpProxy({url:CTX + '/sqlmgr/queryMailInfo.itm'}),
		reader: new Ext.data.JsonReader({totalProperty:'totalCount',root:'rows'},[
	           {name: 'id',mapping:'id'},
	           {name: 'index'},
	           {name: 'subject'},
	           {name: 'ruleTable'},
	           {name: 'ruleIdList'},
	           {name: 'sendto',mapping:'sendMail'},
	           {name: 'cc',mapping:'ccMail'},
	           {name: 'crontab'},
	           {name: 'isAvild'},
	           {name: 'ifSendMs',mapping:'isSendMs'},
	           {name: 'mobileNum'},
	           {name: 'isSend',mapping:'isSendEmpty'},
	           {name: 'isSuccess'},
	           {name: 'isAutomation'},
	           {name: 'msContent'},
	           {name: 'ruleZeusTable'}
        ])
    });
    if(theUser!='bi_dev'){
    	grid = new Ext.grid.GridPanel({
            store: store,
            iconCls:'head',
            cm : cm,
            selModel :sm, //这里必须有
            view: new Ext.grid.GroupingView({
                			forceFit:true
            			}),
             tbar:[{
            	text:'刷新',
                tooltip:'刷新',
                iconCls:'refresh',
                handler:function(){	//刷新
                	store.reload();
                }
            
            },'-',{
            	text:'一键重发',
                tooltip:'一键重发',
                iconCls:'view',
                handler:sendMailFun
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
            width:1450,
            viewConfig: {
    			forceFit: true
    		},
            title:'邮件信息列表',
            loadMask:true
            });
    } else {
    	grid = new Ext.grid.GridPanel({
            store: store,
            iconCls:'head',
            layout:'fit',
            cm : cm,
            selModel :sm, //这里必须有
            view: new Ext.grid.GroupingView({
                			forceFit:true
            			}),
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
            },/*'-',{
                text:'查看',
                tooltip:'查看',
                iconCls:'view',
                handler: viewfun	//查看函数	
            },*/'-',{
                text:'删除',
                tooltip:'删除',
                iconCls:'remove',
                handler:delfun		//删除函数
            },'-',{
            	text:'刷新',
                tooltip:'刷新',
                iconCls:'refresh',
                handler:function(){	//刷新
                	store.reload();
                }
            
            },'-',{
            	text:'一键重发',
                tooltip:'一键重发',
                iconCls:'view',
                handler:sendMailFun
            }/*,'-',{
            	text:'刷新shell',
                tooltip:'刷新shell',
                iconCls:'view',
                handler:updateShell
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
            //autoExpandColumn: 'id',
            viewConfig: {
    			forceFit: true
    		},
            title:'邮件信息列表',
            loadMask:true
            });
    }
    
	var panel_ = new Ext.Panel({
        renderTo: 'searchdiv',
        //height:520,
        //width:1450,
        height:document.getElementById('searchdiv').clientHeight,
        width:document.getElementById('searchdiv').clientWidth,
        layout: 'fit',
        autoScroll:true,
		iconCls:'query',
        tbar: [
	            'ID：',
	            query_id,
	            '有效否：',
            	query_isAvild,
	            '邮件描述：',
	            query_subject,
	            '接收人：',
	            query_sendto,
	            '关联sql的Id：',
	            query_sqlIdList,
	            '按周期类型',
	            query_date,
	            '是否分页',
	            query_isSuccess,
//	            'sql创建时间起：',
//	            query_date1,
//	            'sql创建时间止：',
//	            query_date2,
	            {
		           text:'查询',
		           tooltip:'提交查询',
		           iconCls:'view',
		           handler: function() {
		        	    if(query_isSuccess.getValue() == 1){
		        	    	
		        	    	document.getElementById("ext-gen36").style.display="none";
		        	    	store.load({params:{start:0, limit:200}});//点击执行函数
		        	    	
//		        	    	store.bbar.pageSize = 100;
		        	    }else{
		        	    	document.getElementById("ext-gen36").style.display="";
		        	    	store.load();//点击执行函数
		        	    }
		        	    
		        	    
					    
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
     store.on("beforeload", function() {
    	 		
				store.baseParams = {
						start:0,
						limit:10,
						query_sendto : query_sendto.getValue(),
						query_sqlIdList:query_sqlIdList.getValue(),
						query_date : query_date.getValue(),
						query_isSuccess : query_isSuccess.getValue(),
//						query_date1 : query_date1.getValue(),
//						query_date2 : query_date2.getValue(),
						query_id:query_id.getValue(),
						query_isAvild:query_isAvild.getValue(),
						query_subject:query_subject.getValue()
				};
	});
    panel_.render(document.body);
    store.load({params:{start:0, limit:10}});
  
    ;
});