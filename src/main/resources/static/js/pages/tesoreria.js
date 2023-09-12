$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});

$('li').removeClass('active');
$('#tesoreria').addClass('active');