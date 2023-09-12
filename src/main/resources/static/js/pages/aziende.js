$(document).ready(function () {
  checkCircleStatus();
});

function checkCircleStatus(){
  var items = document.getElementsByClassName("circleStatus");
  for (var i = 0; i < items.length; i++) {
    switch(items.item(i).innerHTML) {
      case "1":
        items.item(i).innerHTML = '<i class="fa fa-circle" style="color:#008000"></i>';
        break;
      case "2":
        items.item(i).innerHTML = '<i class="fa fa-circle" style="color:#FFD700"></i>';
        break;
      case "3":
        items.item(i).innerHTML = '<i class="fa fa-circle" style="color:#FF0000"></i>';
        break;
    }
  }
}

$('li').removeClass('active');
$('.businessdev').addClass('active');