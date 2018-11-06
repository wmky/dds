/**
 * 	主框架js
 */
var changepwdwin = null;	 //修改密码窗口
var cpwdform = null;		 //修改密码表单

Ext.onReady(function(){
var tree_root = new Array(); //根节点数组
var tree = new Array();		 //树对象数组

var viewport = null;	     //viewport对象
var loadMask = null;		 //加载进度条
loadMask=new Ext.LoadMask('mainbody',{msg:'正在加载菜单数据...'});
loadMask.show();
//初始化菜单动态
/*
	Ext.Ajax.request({
		method:'post',
		url:'main.itm?method=getFirstMenuTree',
		success : function(result) {
			responseStr = result.responseText;
			if(responseStr.length>0){
				
				viewport = creatViewPort();		//创建viewport
				//模块菜单分解，以'@'连接
				var modelmenuarr = responseStr.split('@');
				for(var i=0;i<modelmenuarr.length;i++){
					//模块菜单与子菜单分解  以'-'连接		数组元素0为模块菜单值   数组元素1为子菜单值集合
					var menus = modelmenuarr[i].split('-');
					var pmenu = menus[0].split('+');	 //模块菜单值数组
					//子菜单值分解  以'+'连接
					var cmenuvalue = menus[1].split('+');//子菜单值数组
					//创建根节点对象
					tree_root[i] = new Ext.tree.AsyncTreeNode({
							id:cmenuvalue[0]+'_root',
							text:cmenuvalue[1],
							expanded:true,
							draggable:false
					});
					//创建菜单树对象
					tree[i] = new Ext.tree.TreePanel({
						    id:cmenuvalue[0]+'_tree',
						    root:tree_root[i],
						    useArrows:true,
						    //加载子菜单
						    loader: new Ext.tree.TreeLoader({url:'main.itm?method=getMenu&pid='+cmenuvalue[0]})
						    });
					
				    //将菜单加载到viewport中
				    Ext.getCmp('west-panel').add({
		       				id:pmenu[0]+'_panel',
		       				title:pmenu[1],
		                    autoScroll:true,
		                    border:false,
		                    iconCls:'menu_'+pmenu[0],
		                    items:[tree[i]]	
					});
					for(var j=0;j<two_menus.length;j++){
						var pmenu = menus[0].split('+'); //模块菜单值数组
						var cmenuvalue = two_menus[j].split('+');//子菜单值数组 
						//创建根节点对象
						tree_root[i] = new Ext.tree.AsyncTreeNode({
								id:cmenuvalue[0]+'_root',
								text:cmenuvalue[1],
								expanded:true,
								draggable:false
						});
						//创建菜单树对象
						tree[j] = new Ext.tree.TreePanel({
							    id:cmenuvalue[0]+'_tree',
							    root:tree_root[i],
							    useArrows:true,
							    //加载子菜单
							    loader: new Ext.tree.TreeLoader({url:'main.itm?method=getMenu&pid='+cmenuvalue[0]})
						});
						trees.push(tree[j]);//将菜单放入数组

					}
					
				}
				loadMask.hide();	//菜单加载完成
			}else{
				alert('您没有菜单权限，请联系管理员');
				window.location.href='login.itm';
			}
		//更新viewport
		Ext.getCmp('viewport').doLayout();
			
		//点击事件：点击叶子节点，在tabpanel中打开新页面
	     var showtabs = function(node,event){
	    		event.stopEvent();
	    		var tabpanel = Ext.getCmp('maintabpanel');
	    		var tab = Ext.getCmp('tabPanel_'+node.id);
	    		//是叶子节点才触发事件
	    		if(!node.leaf){
	    			return ;
	    		}
	    		if(!tab)
	    		{	
	    			if(tabpanel.items.getCount()>=5){
	    				Ext.Msg.alert('提示','您已打开5个菜单页面<br>如需要打开新页面,请先关闭不需要的页面!');
	    			}
	    			else{
		    			//如果这个菜单tab不存在，则新增一个tab
		    			tabpanel.add({
		    				border: false,
		    				title: node.text,
		    				id: 'tabPanel_'+node.id,
		    				iconCls:'',
		    				html:'<iframe id="frame_'+node.id+'" frameborder="0" style="width:100%; height:100%" src="' + node.attributes.href + '"></iframe>',
		                	closable : true
		    			})
	    			}
	    		}
	    		tab = Ext.getCmp('tabPanel_'+node.id);
				tabpanel.setActiveTab(tab);				//激活tab
	    	};
			//在tree对象中添加click事件
			for(var n=0;n<tree.length;n++){
				tree[n].on('click',showtabs);
			}
	    	
		}
		
	});

*/
	
//初始化菜单，静态，写死
					viewport = creatViewPort();		//创建viewport
					//创建根节点对象
					var tree_root1 = new Ext.tree.TreeNode({
							id:'root_1',
							text:'操作菜单项',
							expanded:true,
							draggable:false
					});
					//创建菜单树对象
					
					var tree1 = new Ext.tree.TreePanel({
						    id:'tree_1',
						    root:tree_root1 
						    });
					    
					var node1 = new Ext.tree.TreeNode({id:'301',text:'sql语句配置',leaf:true,href:'sqlmgr.itm?method=init&url=sql/sql_mgr'});
					var node2 = new Ext.tree.TreeNode({id:'302',text:'发送邮件配置',leaf:true,href:'sqlmgr.itm?method=goMail&url=sql/mail_mgr'});
					//var node3 = new Ext.tree.TreeNode({id:'303',text:'预警邮件监控',leaf:true,href:'sqlmgr.itm?method=goAlarm&url=sql/alarm_mgr'});
					
					tree_root1.appendChild(node1);
					tree_root1.appendChild(node2);
					//tree_root1.appendChild(node3);
				    //将菜单加载到viewport中
				    Ext.getCmp('west-panel').add({
		       				id:'panel_1',
		       				layout:'fit',
		       				title:'规则管理',
		                    autoScroll:true,
		                    border:false,
		                    iconCls:'menu_1',
		                    items:tree1	
					});
					 
					
				loadMask.hide();	//菜单加载完成
			 
		//更新viewport
		Ext.getCmp('viewport').doLayout();
			
		//点击事件：点击叶子节点，在tabpanel中打开新页面
	     var showtabs = function(node,event){
	    		event.stopEvent();
	    		var tabpanel = Ext.getCmp('maintabpanel');
	    		var tab = Ext.getCmp('tabPanel_'+node.id);
	    		//是叶子节点才触发事件
	    		if(!node.leaf){
	    			return ;
	    		}
	    		if(!tab)
	    		{	
	    			if(tabpanel.items.getCount()>=5){
	    				Ext.Msg.alert('提示','您已打开5个菜单页面<br>如需要打开新页面,请先关闭不需要的页面!');
	    			}
	    			else{
		    			//如果这个菜单tab不存在，则新增一个tab
		    			tabpanel.add({
		    				border: false,
		    				title: node.text,
		    				id: 'tabPanel_'+node.id,
		    				layout:'border',
		    				iconCls:'',
		    				//html:'<iframe id="frame_'+node.id+'" frameborder="0" style="width:100%; height:100%" src="' + node.attributes.href + '"></iframe>',
		    				html:'<iframe id="frame_'+node.id+'" frameborder="0" style="width:100%; height:100%" src="http://www.baidu.com"></iframe>',
		                	closable : true
		    			})
	    			}
	    		}
	    		tab = Ext.getCmp('tabPanel_'+node.id);
				tabpanel.setActiveTab(tab);				//激活tab
	    	};
			//在tree对象中添加click事件
			tree1.on('click',showtabs);
	
//创建viewport
function creatViewPort(){
		viewport = new Ext.Viewport({
	            id:'viewport',
	            layout:'border',
	            items:[{
	                region:'west',
	                id:'west-panel',
	                title:'导航菜单栏',
	                split:true,
	                width:180,
	                minSize: 150,
	                maxSize: 400,
	                collapsible: true,
	                margins:'58 0 5 5',
	                cmargins:'58 5 5 5',
	                //layout:'accordion',
	                layout:'fit',
	                layoutConfig:{
	                    animate:true
	                }
	            },{
	                region:'center',
	                margins:'58 5 5 0',
	                //layout:'column',
	                layout:'fit',
	                autoScroll:true,
	                items:
	               		new Ext.TabPanel({
	               			id:'maintabpanel',
	               			border:false,
	               			autoDestroy: true,
	  						resizeTabs:true,
	  						//height: 550,
	                        activeTab:0,
	                        enableTabScroll:true,
	                        items:[{
	                        	id:'tabPanel_301',
	                        	title:'sql语句配置',
	                        	html:'<iframe id="frame_fp" frameborder="0" style="width:100%; height:100%" src="sqlmgr.itm?method=init&url=sql/sql_mgr"></iframe>'
	                        	//html:'<iframe id="frame_f" frameborder="0" style="width:100%; height:100%" src="http://www.baidu.com"></iframe>'
	                        }]
	                        
	               		})
	            }]
	        });
	        
	  return viewport;
}

	   Ext.BLANK_IMAGE_URL = 'resources/js/ext/resources/images/default/s.gif';
       Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
	   
    });
//-------------------------------------------------------------------------------------------   
//注销    
function logout(){
  		Ext.Msg.confirm('注销','确定要注销登录吗？',function(btn){
  				if(btn=='yes'){
	  				Ext.Ajax.request({
	  					url: 'logout.itm?method=logout',
	  					method: 'post',
	  					success : function(result) {
	  						responseStr = result.responseText;
	  						var para = responseStr.split('&');
	  						if(para[0]=='1'){
	  							alert(para[1]);
	  							window.location.href='login.itm';	//跳转到登录页面
	  						}
	  						else{
	  							alert(para[1]);
	  						}
	  					}
	  				});
  				}		
  		})			
}
//------------------------------------------------------------------------------------
//提交修改密码表单
cpwd_sumbit = function (){
	Ext.Msg.alert('提示','密码暂时无法修改');
	changepwdwin.hide();
	return;
	if(cpwdform!=null){
		var oldpwd = Ext.getCmp('oldpwd').getValue();
		var newpwd = Ext.getCmp('newpwd').getValue();
		var newpwd_ = Ext.getCmp('newpwd_').getValue();
		
		if(oldpwd.trim().length<1){
			Ext.Msg.alert('提示','请输入旧密码');
			return;
		}
		if(newpwd.trim().length<1){
			Ext.Msg.alert('提示','请输入新密码');
			return;
		}
		if(newpwd_.trim().length<1){
			Ext.Msg.alert('提示','请再次输入新密码');
			return;
		}
		if(newpwd!=newpwd_){
			Ext.Msg.alert('提示','2次输入的新密码不相同，请重新输入');
			Ext.getCmp('newpwd').setValue('');
			Ext.getCmp('newpwd_').setValue('');
			return;
		}
		//ajax 提交
		Ext.Ajax.request({
			url:'login.itm?method=changePwd',
			method: 'post',
			params:{
				oldpwd:oldpwd,		//旧密码
				newpwd:newpwd,		//新密码
				account:account		//用户账号
			},
			success:function(result){
				responseStr = result.responseText;
				//登录成功
				if(responseStr>0){
					Ext.Msg.alert('提示','密码修改成功');
					changepwdwin.hide();
				}
				//登录失败
				else if(responseStr == 0){
					Ext.Msg.alert('提示','用户不存在或密码错误');
				}
				//系统未完成操作
				else{
					Ext.Msg.alert('提示','未完成操作，请联系管理员');
				}
			}
			
		});
	}

}
//------------------------------------------------------------
//取消函数
cpwd_cancel = function (){
	if(changepwdwin!=null&&changepwdwin.isVisible()){
		changepwdwin.hide();
		changepwdwin.setActive(true);
	}
}

//-----------------------------------------------------------

//修改密码
function changepwd(){
	//判断表单是否存在				
	if(cpwdform==null){
		cpwdform = new Ext.form.FormPanel({
			id:'changepwdform',
	        frame: true,
	        labelAlign: 'right',
			items:[ //使用fieldset将表单值包起来
	            	new Ext.form.FieldSet({
	                		title: '修改密码',
	                		defaultType: 'textfield',
	                		items: [{
			                	fieldLabel: '旧密码',
			                	id: 'oldpwd',
			                    name: 'oldpwd',
			                    allowBlank : false,			//不允许为空
			                    blankText : '旧密码不能为空',
			                    inputType : 'password',
			                    width:110
			                },{
			                    fieldLabel: '新密码',
			                    id: 'newpwd',
			                    name: 'newpwd',
			                    allowBlank : false,			//不允许为空
			                    blankText : '新密码不能为空',
			                    inputType : 'password',
			                    width:110
			                },{
			                   	fieldLabel: '重复输入',
			                    id: 'newpwd_',
			                    name: 'newpwd_',
			                    allowBlank : false,			//不允许为空
			                    blankText : '重复输入不能为空',
			                    inputType : 'password',
			                    width:110
			                }]
	            	})
	      ],
	      bbar:['->',{
	                	text:'保存',
	                	iconCls:'save',
	                	handler: cpwd_sumbit		//提交修改密码表单
	           },'-',{
	                	text:'取消',
	                	iconCls:'cancel',
	                	handler: cpwd_cancel		//取消
	           }]
		});
		//判断修改密码窗口是否存在
		if(changepwdwin == null){
			changepwdwin =new Ext.Window({
	            				title: '修改密码',
	            				iconCls:'menu_8',
		            			layout:'fit',			//单一项目布局,加载的panel自动扩大，以适应容器
		                        width:270,
		                        height:190,
		                        plain : true,			
		                        modal:true,				//模态
		                        closeAction : 'hide',	//隐藏窗口；'close'为destroy窗口
		                        collapsible:true,		//是否可收缩
		                        items:[cpwdform]
	        });
		}
	}
	else{
		cpwdform.form.reset();
	}
	var  top = document.body.scrollTop+(document.body.clientHeight/2-changepwdwin.height/2);
	changepwdwin.setPosition((document.body.clientWidth/2-changepwdwin.width/2) ,top);
    changepwdwin.show();
	
}
//-----------------------------------------------------------