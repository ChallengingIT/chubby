$(".prospection").click(
    function(){
        $("#prospection").toggle();
    }
);

//$(".information").click(
//    function(){
//        $("#information").toggle();
//    }
//);

//$(function() {
//    $('.information').click();
//});

$(".qm").click(function(){
$("#qm").toggle();
});

$(document).ready(function(){
$(".confermaBox").click(function() {
      $("#myModal").modal("show");
  });
});

function Reset() {
    document.getElementById("ricercaOwner").value = "";
    document.getElementById("ricercaTipologia").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}

$('.list-unstyled li').removeClass('active');
$('.businessdev').click();
$('.aziende').addClass('active');
