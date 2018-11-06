$(document).ready(function(){
	$("div").each(function(i){
		if(i != 0 && i != $("div").size()-1){
			var l = 109*(i-1) +3*i;
			$(this).css({position: "absolute",top: "3px",left: l});
			$(this).click(function(){
				$("div").removeClass('option_f');
				$(this).addClass('option_f');
			});
		}
	});
	$("#dis_div").height($(document).height()-27);
	$("#option1").click();
});