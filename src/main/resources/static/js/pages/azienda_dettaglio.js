$(".prospection").click(
    function(){
        $("#prospection").toggle();
    }
);

$(".information").click(
    function(){
        $("#information").toggle();
    }
);

$(function() {
    $('.information').click();
});

$(".qm").click(function(){
$("#qm").toggle();
});

$(document).ready(function(){
$(".confermaBox").click(function() {
      $("#myModal").modal("show");
  });
});