function iframeAutoHeight(obj){
	var win=obj;
	if (document.getElementById){
	       if (win && !window.opera){
	        if (win.contentDocument && win.contentDocument.body.offsetHeight) 
	
	         win.height = win.contentDocument.body.offsetHeight; 
	        else if(win.Document && win.Document.body.scrollHeight)
	         win.height = win.Document.body.scrollHeight;
	       }
	}
}

String.prototype.trim = function() { 
    return this.replace(/(^[\s]*)|([\s]*$)/g, ""); 
} 
String.prototype.lTrim = function() { 
    return this.replace(/(^[\s]*)/g, ""); 
} 
String.prototype.rTrim = function() { 
    return this.replace(/([\s]*$)/g, ""); 
} 

String.prototype.strLen=function(){
	var sString = this;
	var sStr,iCount,i,strTemp ; 

	iCount = 0 ;
	sStr = sString.split("");
	for (i = 0 ; i < sStr.length ; i ++){
		strTemp = escape(sStr[i]); 
		if (strTemp.indexOf("%u",0) == -1){ 
			iCount = iCount + 1 ;
		}else{
			iCount = iCount + 2 ;
		}
	}
	return iCount ;
}

function $p(el) {
	var ua = navigator.userAgent.toLowerCase();
	var isOpera = (ua.indexOf('opera') != -1);
	var isIE = (ua.indexOf('msie') != -1 && !isOpera); // not opera spoof
	if(el.parentNode === null || el.style.display == 'none') {
		return false;
	}      
	var parent = null;
	var pos = [];     
	var box;     
	if(el.getBoundingClientRect){    //IE      
	  box = el.getBoundingClientRect();
	  var scrollTop = Math.max(document.documentElement.scrollTop, document.body.scrollTop);
	  var scrollLeft = Math.max(document.documentElement.scrollLeft, document.body.scrollLeft);
	  return {x:box.left + scrollLeft, y:box.top + scrollTop};
	}else if(document.getBoxObjectFor){    // gecko    
	 
	  box = document.getBoxObjectFor(el); 
	  var borderLeft = (el.currentStyle.borderLeftWidth)?parseInt(el.currentStyle.borderLeftWidth):0; 
	  var borderTop = (el.currentStyle.borderTopWidth)?parseInt(el.currentStyle.borderTopWidth):0; 
	  pos = [box.x - borderLeft, box.y - borderTop];
	} else{    // safari & opera    
		pos = [el.offsetLeft, el.offsetTop];  
		parent = el.offsetParent;     
		if (parent != el) { 
			while (parent) {  
				pos[0] += parent.offsetLeft; 
				pos[1] += parent.offsetTop; 
				parent = parent.offsetParent;
			}  
		}   
		if (ua.indexOf('opera') != -1 || ( ua.indexOf('safari') != -1 
				&& el.style.position == 'absolute' )) { 
			pos[0] -= document.body.offsetLeft;
			pos[1] -= document.body.offsetTop;         
		}    
	}              
	if (el.parentNode) { 
		parent = el.parentNode;
	} else {
		parent = null;
	}
	while (parent && parent.tagName != 'BODY' && parent.tagName != 'HTML') { // account for any scrolled ancestors
		pos[0] -= parent.scrollLeft;
		pos[1] -= parent.scrollTop;
		if (parent.parentNode) {
			parent = parent.parentNode;
		} else {
			parent = null;
		}
	}
	return {x:pos[0], y:pos[1]};
}

var Container = function(obj){
	this.obj = obj;
	obj.className = "Container";
}
Container.prototype.init = function(){
	this.right = document.createElement("div");
	this.right.className = "Container_right";
	document.body.appendChild(this.right);
	var mobj = this;
	this.obj.parentNode.onresize = function(){
		mobj.resize();
	}
	this.resize();
}
Container.prototype.resize= function(){
	var p = $p(this.obj);
	with(this.right){
		style.left = p.x + this.obj.offsetWidth;
		style.top = p.y;
	}
}
