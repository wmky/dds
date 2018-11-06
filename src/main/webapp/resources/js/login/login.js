
Ext.QuickTips.init();   
LoginWindow=Ext.extend(Ext.Window,{   
        title : '系统登录',           
        width : 265,               
        height : 170,
        iconCls:'login',
        collapsible : true,   
        defaults : {               
            border : false  
        },   
        buttonAlign : 'center',       
        createFormPanel :function() {   
                return new Ext.form.FormPanel( {
                	id : 'formid',
                    bodyStyle : 'padding-top:6px',   
                    defaultType : 'textfield',   
                    labelAlign : 'right',   
                    labelWidth : 55,   
                    labelPad : 0,
                    loadMask:true,
                    frame : true,   
                    defaults : {   
                    allowBlank : false,   
                    width : 158   
                    },
                    listeners:{
                    	'render':function() {
                    		this.findByType('textfield')[0].focus(true,true);// 用户名获取焦点
                    	}
                    },
                    items : [{   
                            cls : 'user',
                            id : 'user_id',
                            name : 'username', 
                            value:'bi_dev',
                            fieldLabel : '用户名',   
                            blankText : '用户名不能为空'  
                        }, {   
                            cls : 'key',
                            id : 'pwd_id',
                            name : 'password',   
                            value:'bi_dev',
                            fieldLabel : '密码',   
                            blankText : '密码不能为空',   
                            inputType : 'password'  
                        }, {    
                            name:'randCode',   
                            id:'randCode',   
                            fieldLabel:'验证码',   
                            width:70, 
                            grow:true,
                            growMin:50,
                            growMax:100,
                            minLength:4, // 最小字符数设置
                            minLengthText:'最小请输入4位数字验证码',
                            maxLength:4,
                            maxLengthText:'请不要输入超过4位数字验证码',
                            blankText : '验证码不能为空'
                    }]   
                });   
        },   
        ///登录   
        login:function() {   
             var user = Ext.fly('user_id').getValue();
             var pwd = Ext.fly('pwd_id').getValue();
             var rand = Ext.fly('randCode').getValue();
             if(user.trim()==''){
             	Ext.Msg.alert('提示','请填写用户名');
             	return ;
             }
             else if(pwd.trim()==''){
             	Ext.Msg.alert('提示','请填写密码');
             	return ;
             }
             else if(rand.trim()==''){
             	Ext.Msg.alert('提示','请填写验证码');
             	return ;
             }
             else{
	             Ext.Ajax.request({
						url : CTX + '/loginctrl/login.itm',
						method : 'post',
						params:{
							userTag:user,
							passWord:pwd,
							rand:rand,
							screenw:screen.width,	//登录用户的屏幕宽
							screenh:screen.height	//登录用户的屏幕高
						},
						success : function(result) {
							responseStr = result.responseText; // 得到服务器的反馈信息
							var result = responseStr.split('&');//解析返回结果
							//登录成功
							if(result[0] == '1'){
								Ext.Msg.alert('登录信息',result[1]);
								window.location.href=CTX + '/mainctrl/init.itm';
							}
							
							//登录失败
							else {
								Ext.Msg.alert('登录信息',result[1]);
								
								//验证码错误
								if(result[0]=='2'){
									//Ext.getDom('imgid').src='login.itm?method=getImage';
									RefreshMcode();
								}	
							}
						}	
				});
             }
        },   
           
        initComponent : function(){   
            this.keys = {   
                    key : Ext.EventObject.ENTER,   
                    fn : this.login,   
                    scope : this  
            };     
            LoginWindow.superclass.initComponent.call(this);          
            this.fp=this.createFormPanel();   
            this.add(this.fp);   
            this.addButton('登录',this.login,this);   
            this.addButton('重置', function(){this.fp.form.reset();},this);      
        }        
});

Ext.onReady(function(){                 
	var w = new LoginWindow();   
	w.show();       
	var bd = Ext.getDom('randCode');
	var bd2 = Ext.get(bd.parentNode);     
	bd2.createChild([{tag:'span',html:'<img id="imgid" src="'+CTX+'/loginctrl/getImage.itm" align="absbottom"/><a href="javascript:RefreshMcode();" height="150px">看不清？</a>'}]);
});  

// IE8,IE9支持
function RefreshMcode() {
	var num = Math.random();  
	var getimagecode = document.getElementById("imgid");
	getimagecode.src = CTX + "/loginctrl/getImage.itm?num"+num;
}
