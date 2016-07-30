// This script makes style of select the same as the option style selected
// uses jQuery. It is taken from here:
// http://jsfiddle.net/614c6cxz/8/
// original discussion on stackoverflow:
// http://stackoverflow.com/questions/15755770/change-text-color-of-selected-option-in-a-select-box
$("select").change(function(){
    var origBGColor=$(this).attr("class");

    $(this).removeClass($(this).attr('class'))
           .addClass($(":selected",this).attr('class'));

    $("#output").html("original bg color = "+origBGColor+"<br/>Selected BG Color = "+$(":selected",this).attr('class')+"<br/>Current Select BG color = " + $(this).attr('class'));
});

// if there is an anchor with id="focused-link" we'll focus on it
$("#focused-link").focus();
