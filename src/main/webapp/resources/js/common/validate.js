Ext.apply(Ext.form.VTypes,{
	//判断整数
	integer:function(val,field){
		try  
        {   
            if(/^[-+]?[\d]+$/.test(val))   
                return true;   
            return false;   
        }   
        catch(e)   
        {   
            return false;   
        }   
	},
	integerText:'请输入正确的整数',
	
	//ip地址
	ip:function(val,field){   
        try  
        {   
            if((/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(val)))   
                return true;   
            return false;   
        }   
        catch(e)   
        {   
            return false;   
        }   
  	},
  	ipText:'请输入正确的IP地址',
  
  	//手机号
  	mobilephone:function(){
  		try  
        {   
            if((/^(13|15|18)\d{9}$/.test(val)))   
                return true;   
            return false;   
        }   
        catch(e)   
        {   
            return false;   
        }
  	},
  	mobilephoneText:'请输入正确的手机号'
});