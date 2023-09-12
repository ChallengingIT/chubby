document.getElementById('theDate').value = '1970-01-01';

document.getElementById('newDate').valueAsDate =  new Date();

$('li').removeClass('active');
$('.staffing').addClass('active');

$(document).ready(function () {
    setTimeout(function () {
        $("#success-alert").alert('close');
    }, 5000);
});

var expanded = false;

function showCheckboxes() {
    var checkboxes = document.getElementById("checkboxes");
    if (!expanded) {
        checkboxes.style.display = "block";
        expanded = true;
    } else {
        checkboxes.style.display = "none";
        expanded = false;
    }
}
