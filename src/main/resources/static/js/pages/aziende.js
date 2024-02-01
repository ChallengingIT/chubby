$(document).ready(function () {
  checkCircleStatus();
});

function checkCircleStatus(){
  var items = document.getElementsByClassName("circleStatus");
  for (var i = 0; i < items.length; i++) {
    switch(items.item(i).innerHTML) {
      case "1":
        items.item(i).innerHTML = '<i class="fas fa-smile fa-lg" style="color: #00d200;"></i>';
        break;
      case "2":
        items.item(i).innerHTML = '<i class="fas fa-meh fa-lg" style="color: #ff9500;"></i>';
        break;
      case "3":
        items.item(i).innerHTML = '<i class="fas fa-sad-tear fa-lg" style="color: #ff0000;"></i>';
        break;
    }
  }
}

$('.list-unstyled li').removeClass('active');
$('.businessdev').click();
$('.aziende').addClass('active');

function Reset() {
    document.getElementById("ricercaRagione").value = "";
    document.getElementById("listaOwner").value = "";
    document.getElementById("tipologia").value = "";
    document.getElementById("status").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}