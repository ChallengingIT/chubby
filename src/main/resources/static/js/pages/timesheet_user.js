function alertSt(message) {
    alertify.confirm("Note", message, null, null);
}

$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});

$('.list-unstyled li').removeClass('active');
$('#timesheet').addClass('active');

$(".valori-tabella").on('click', 'td', function (event) {
$("#" + "dayModal" + this.id ).modal();
$("#" + "dayModal" + this.id ).modal('open');
});