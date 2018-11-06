document.write("<script language='javascript' src='"+CONTEXT_PATH+"/js/calendar/WdatePicker.js'></script>");
document.write("<script language='javascript' src='"+CONTEXT_PATH+"/js/engine.js'></script>");
document.write("<script language='javascript' src='"+CONTEXT_PATH+"/js/util.js'></script>");
function FormEditer(form){
			this.form = form;
			this.form.className += " formEditer";
			this.elArr = this.form.elements;
			this.validateConf = new Object();
			this.msgObj;
			this.detailwin = window;
			this.win;
			this.detailwin;
			this.regionAutoTabOnEnter=true;
			this.editors = [];
		}
		FormEditer.prototype.init = function(){
			var mobj = this;
			this.initValidateConf();
			var arr = this.form.elements;
			for(var i =0 ;i < arr.length; i++){
				var el = arr[i];
				this.initEvent(el);
			}
		}
		FormEditer.prototype.initValidateConf = function(){
			var mobj = this;
			this.regionValidate(
				{attribute:"vtype", 
				 value:"number",
				 code:"001",
				 msg:function(){return "必须是数字!"}, 
				 func:function(el){
					if(isNaN(el.value)){		
						return false;
					}
				}});
			this.regionValidate(
					{attribute:"vtype", 
					 value:"phone",
					 code:"002",
					 msg:function(){return "号码必须是11位手机号码或者座机号码<br>（3~4位区号-7~8位号码:0769-12345678）!"}, 
					 func:function(el){
						 if(el.value.trim() == "") return;
						 var phone=el.value.replace(/^\s+|\s+$/g,"");
				    		var regTelPhone=/^0\d{2,3}\-\d{7,8}$/;
				    		var cellphone=/^(((1[0-9]{1}[0-9]{1}))+\d{8})$/;
				    		if((!regTelPhone.test(phone))&&(!cellphone.test(phone))){
								return false;
				    		}
							return true;
					}});
			this.regionValidate(
					{attribute:"vtype", 
					 value:"email",
					 code:"003",
					 msg:function(){return "请输入有效的email地址，如：a@a.com"}, 
					 func:function(el){
						 if(el.value.trim() == "") return;
						 var email=el.value.replace(/^\s+|\s+$/g,"");
				    		var regEmail=/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/;
				    		if(!regEmail.test(email)){
								return false;
				    		}
							return true;
					}});
			//不许为空
			this.regionValidate(
				{attribute:"isnull", 
				 code:"010",
				 initEnd:function(el){
					/**
					if(el.disabled)return;
					 var span = document.createElement("span");
					 span.className = "required";
					 var bo = el;
					 if(el.vtype == "date" || el.vtype =="editor" ){
						el = el.nextSibling;
					 }
					 if(el.vtype &&
						",SELECT,USER,DEPT,CONF,".indexOf(","+el.vtype.toUpperCase()+",") > -1){
						 el = el.mobj.initArea;
					 }
					el.parentNode.insertBefore(span, el.nextSibling);
					el.parentNode.style.whiteSpace = "nowrap";
					el.parentNode.style.paddingRigth = "20";
					**/
				 },
				 msg:function(){return "值不能为空!"}, 
				 func:function(el){
					 var value = el.value.trim();
					 if(el.valueCallBack) value = el.valueCallBack();
					if(value == ""){
						return false;
					}
				}});
			this.regionValidate(
					{attribute:"maxlen", 
					 code:"011",
					 msg:function(el){return "值必须少于"+$(el).attr('maxlen')+"个字节!<br/>(汉字输入状态下的字符，占1个字节；<br/>英文输入状态下的字符，占1个字节；)"}, 
					 func:function(el){
					 	//汉字算2个字符
						//if(el.value.trim().strLen() > parseInt(el.maxlen)){		
						//	return false;
						//}
						//汉字算1个字符
						if(el.value.trim().length > parseInt($(el).attr('maxlen'))){		
							return false;
						}
					}});
			this.regionValidate(
					{attribute:"minlen", 
					 code:"012",
					 msg:function(el){return "值必须至少存在"+$(el).attr('minlen')+"个字节!<br/>(汉字输入状态下的字符，占1个字节；<br/>英文输入状态下的字符，占1个字节；)"}, 
					 func:function(el){
						//if(el.value.trim().strLen() < parseInt(el.minlen)){		
						//	return false;
						//}
						if(el.value.trim().length < parseInt($(el).attr('minlen'))){		
							return false;
						}
					}});
			this.regionValidate(
					{attribute:"vtype", 
					 value:"selector",
					 code:"013",
					 msg:function(){return "请从下拉列表中选择!"}, 
					 initModule:function(el){
					 	if(!el.mobj){//如果下拉框已经被初始化则不重复初始化
						 	el.mobj = $(el);
							$(el).val($(el).attr('defaultVal'));
						}
					 }, 
					 func:function(el){
					}});
			this.regionValidate(
					{attribute:"vtype", 
					 value:"date",
					 code:"014",
					 msg:function(el){return "必须是正确日期类型(例如:"+($(el).attr("datefmt") || "yyyy-MM-dd")+")!"}, 
					 initModule:function(el){
						var datefmt = $(el).attr("datefmt") || "yyyy-MM-dd";
						$(el).focus(function(){
							WdatePicker({el:el,isShowWeek:true,dateFmt:datefmt});
						});
					 }, 
					 func:function(el){
					}});
		}
		FormEditer.prototype.initEvent = function(el){
			var initBefore = [];
			var initEnd = [];
			var initModule = [];
			
			if(el.nodeName == "SELECT" && !$(el).attr("vtype"))el.vtype = "selector";
			/****/
			for(var item in this.validateConf){
				var conf = this.validateConf[item];		
				if(typeof($(el).attr(item)) == "string"){
					if(conf["single"]){
						conf = conf["single"];						
					}
					else{
						conf = conf[$(el).attr(item)];
					}
					if(conf){
						if(conf.initBefore){
							initBefore[initBefore.length] = conf;
						}
						if(conf.initEnd){
							initEnd[initEnd.length] = conf;
						}
						if(conf.initModule){
							initModule[initModule.length] = conf;
						}
					};
				}
			}
			/**run init before**/
			this.initAttach(el, initBefore,"initBefore");
			switch(el.nodeName){
				case "INPUT": 
					if("BUTTON,SUBMIT,RESET".indexOf(el.type.toUpperCase()) > -1){
						this.regionBtn(el);
					}
					else if("RADIO,CHECKBOX".indexOf(el.type.toUpperCase()) == -1){
						this.regionText(el);						
					}				
					break;
				case "TEXTAREA":
					el.style.height = "auto";
					this.regionText(el);
					break;
				case "BUTTON": 
					this.regionBtn(el);
					break;
				case "SELECT":
					this.regionSelect(el);
					break;
			}
			if(el.type &&
				"SUBMIT,RESET,".indexOf(el.type.toUpperCase()) == -1
				&& ",BUTTON,TEXTAREA,".indexOf(el.nodeName) == -1
				&& el.vtype != "SELECT"
				&& this.regionAutoTabOnEnter){
				this.regionEnterEventer(el);
			}
			//this.initHelp(el);			
			/**init end**/
			this.initAttach(el,initModule,"initModule");
			this.initAttach(el, initEnd,"initEnd");
		}
		FormEditer.prototype.initAttach=function(el,funcs,funName){
			for(var i = 0; i < funcs.length;i++){
				funcs[i][funName](el);
			}
		}
		FormEditer.prototype.regionEnterEventer=function(el){
			//让回车键实现tab键功能
			//el.onkeydown = function(){
				//if (event.keyCode == 13) {   
		       	// 	event.keyCode = 9;   
		       // }   
				
			//}
		}
		
		FormEditer.prototype.regionText = function(el){
			var mobj = this;
			el.className += " TextCom";
			$(el).focus(function(){
				el.select();
				el.className += " TextComFocus";
				//mobj.showTips(el,true);		
				//mobj.showMsg(el,true);
			});
			$(el).blur(function(){
				el.className = el.className.replace(/\s*(TextComFocus)/g,"" );	
				mobj.validate(el);
				mobj.showTips(el,true);			
				//mobj.showMsg(el,false);
			});
			
		}
		
		FormEditer.prototype.regionBtn = function(el){
			el.className += " FuncBtn"; 
//			el.attachEvent("onmouseover",function(){
//				el.remCss = el.className;
//				el.className = "FuncBtnHover";
//			});
			$(el).mouseover(function(){
				el.remCss = el.className;
				el.className = "FuncBtnHover";
			});
			$(el).mouseout(function(){
				el.className = el.remCss;
			});
//			el.attachEvent("onmouseout",function(){
//				el.className = el.remCss;
//			});
		}
		FormEditer.prototype.regionSelect = function(el){
			var mobj = this;
			el.className += " TextCom";
			$(el).focus(function(){
				
				mobj.showTips(el,true);
				//mobj.showMsg(el,true);
			});
			$(el).blur(function(){
				el.className = el.className.replace(/\s*(TextComFocus)/g,"" );	
				mobj.validate(el);
				if(false){
					//边框变红
					this.testdiv = document.createElement("div");
					this.testdiv.style.border = "1px solid red";
					el.style.border = "1px solid red";
					this.testdiv.style.width = el.style.width;
					this.testdiv.style.height = el.style.height;
					el.style.margin = -1;
					el.parentNode.insertBefore(this.testdiv,el);
					this.testdiv.appendChild(el);
				}
				mobj.showTips(el,true);
				//mobj.showMsg(el,false);
			});
		}
		FormEditer.prototype.addEl = function(el){
			this.elArr = this.elArr.concat(el);
		}
		FormEditer.prototype.chk = function(){	
			var errorNum = 0;
			for(var i =0 ;i < this.elArr.length; i++){
				var el = this.elArr[i];
				if(!this.validate(el)){
					errorNum++;
				}
			}
			return errorNum > 0 ? false : true;
			
		}
	
		FormEditer.prototype.clearError= function(el){
			el.className = el.className.replace(/\s*(Form_Validate_error)/g,"" );
			this.showMsg(el,false);
		}
		FormEditer.prototype.showMsg= function(el,flag){
			var mobj = this;
			if(flag){
				var sp = $p(el);				
				
				if(!this.msgObj ){
					this.msgObj = document.createElement("div");
					document.body.appendChild(this.msgObj);
					this.msgObj.className="Form_Validate_msg";
					this.msgObj.style.zIndex = 10;
					var cont = document.createElement("a");
					cont.className = "Form_msg_controller";
					cont.href="javascript:;";
					cont.onclick = function(){
						mobj.showMsg(false);
						event.cancelBubble = true;
					}
					this.msgObj.appendChild(cont);
					this.msgObj.inner = document.createElement("div");
					this.msgObj.appendChild(this.msgObj.inner);
					this.msgObj.inner.className = "Form_Validate_msg_inner";
					
				}
				this.msgObj.inner.innerHTML =  function(){
					/**发生的错误**/
					var str ="";
					if(el.validateError && el.validateError.length > 0){
						str += "<div class='Form_Validate_msg_title'>有如下'"+el.validateError.length+"'个错误需要纠正!</div>";
						str+="<ul  class='Form_Validate_msg_list'>";
						for(var i = 0; i < el.validateError.length; i++){
							str += "<li>"+el.validateError[i].message+"</li>";
						}
						str+="</ul>";
					}					
					return str;
				}();
				try{
					with(this.msgObj){
						if(el.msgPositionCallBack){
							el.msgPositionCallBack(this.msgObj);
						}else{
							style.top=sp.y+el.offsetHeight;
							style.left=sp.x;
						}
						style.display = this.msgObj.inner.innerHTML.trim() == "" ? "none":"";					
					}
				}catch(ex){
					alert(ex.message);
				}
			}
			else{
				if(!this.msgObj) return;
				this.msgObj.style.display = "none";
			}
		}
		FormEditer.prototype.showTips = function(el,flag,tipStr){
			
			var top_left = '<div class="line1"><!-- --></div>'
							+'<div class="line2"><!-- --></div>'
							+'<div class="line3"><!-- --></div>'
							+'<div class="line4"><!-- --></div>'
							+'<div class="line5"><!-- --></div>'
							+'<div class="line6"><!-- --></div>'
							+'<div class="line7"><!-- --></div>'
							+'<div class="line8"><!-- --></div>'
							+'<div class="line9"><!-- --></div>'
							+'<div class="line10"><!-- --></div>';
			var down_left = '<div class="line10"><!-- --></div>'
							+'<div class="line9"><!-- --></div>'
							+'<div class="line8"><!-- --></div>'
							+'<div class="line7"><!-- --></div>'
							+'<div class="line6"><!-- --></div>'
							+'<div class="line5"><!-- --></div>'
							+'<div class="line4"><!-- --></div>'
							+'<div class="line3"><!-- --></div>'
							+'<div class="line2"><!-- --></div>'
							+'<div class="line1"><!-- --></div>';
			if(el.validateError && el.validateError.length > 0){
				if(this.msgObj)
				$(this.msgObj).animate({"opacity":'hide'},function(){});
				
				var str ="";
				for(var i = 0; i < el.validateError.length; i++){
					str += "<li>"+el.validateError[i]+"</li>";
				}
				this.msgObj = document.createElement('div');
				var formErrorContent = document.createElement('div');
				$(this.msgObj).addClass("formError redPopup");
				//$(this.msgObj).addClass("greenPopup");
				$(formErrorContent).addClass("formErrorContent");
				$("body").append(this.msgObj);
				$(this.msgObj).append(formErrorContent);
				var arrow = document.createElement('div');
				$(arrow).addClass("formErrorArrow");
				$(this.msgObj).append(arrow);
				$(arrow).addClass("formErrorArrowBottom")
				$(arrow).html(top_left);
				callerTopPosition = 0;
				callerleftPosition = 0;
				callerTopPosition += $(el).offset().top +20;
				callerleftPosition += $(el).offset().left + 100;
				$(this.msgObj).css({
					top:callerTopPosition,
					left:callerleftPosition,
					opacity:0
				})
				$(formErrorContent).css('marginTop','10px');
				$(arrow).css('top','10px');
				$(formErrorContent).html(str);
				$(this.msgObj).animate({"opacity":0.87},function(){});
			}else{
				if(!this.msgObj) return;
				$(this.msgObj).animate({"opacity":'hide'},function(){});
			}
		}
		
		FormEditer.prototype.validate = function(el){
				if(el.validateError && el.validateError.length > 0){
					this.clearError(el);
				}
				el.validateError = [];
				if(el.disabled || el.style.display == "none"
					||(el.nodeName == "INPUT" && el.type.toUpperCase() == "HIDDEN")) return true;
				
				for(var item in this.validateConf){
					var conf = this.validateConf[item];	
					if(typeof($(el).attr(item)) == "string"){
						if(conf["single"]){
							conf = conf["single"];						
						}
						else{
							//conf = conf[el[item]];
							conf = conf[$(el).attr(item)]
						}
						if(conf && conf.func(el) == false){
							var msg = conf.msg(el);
							el.validateError[el.validateError.length] = msg;
						};
					}
				}
				
				if(el.validateError.length > 0){		
					el.className += " Form_Validate_error";
				}
				return el.validateError.length == 0 ? true : false;
		}

		FormEditer.prototype.regionValidate = function(conf){
			if(!this.validateConf[conf.attribute]) {
				this.validateConf[conf.attribute] = new Object();
			}
			if(!conf.value)conf.value = "single";
			this.validateConf[conf.attribute][conf.value] = conf;
		}
		FormEditer.prototype.confirm = function(){
			for(var i = 0; i < this.editors.length; i++){
				var conf = this.editors[i];
				conf.textarea.value = conf.frm.contentWindow.getHTML();
			}
		}
