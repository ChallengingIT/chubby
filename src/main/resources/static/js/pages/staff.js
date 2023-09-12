function alertSt(message) {
    alertify.confirm("Note", message, null, null);
}

$('li').removeClass('active');
$('#hr').addClass('active');

$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});