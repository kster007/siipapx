// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better
// to create separate JavaScript files as needed.
//
//= require jquery-2.2.0.min
//= require bootstrap

//= require inspinia.js
//= require pace/pace.min.js
//= require slimscroll/jquery.slimscroll.js
//= require dataTables.js


//= require jquery-ui/jquery-ui.js
//= require datepicker/bootstrap-datepicker.js
//= require metisMenu/jquery.metisMenu.js
//= require chartJs/Chart.min.js
//= require sugar.js
//= require chosen/chosen.jquery.js

//Mask
//= require mask/jquery.mask.min.js

//= require_self
// require_tree .
$(function(){
	var datepicker = $.fn.datepicker.noConflict(); // return $.fn.datepicker to previously assigned value
	$.fn.bootstrapDP = datepicker;                 // give $().bootstrapDP the bootstrap-datepicker functionality


});


