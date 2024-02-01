$('.list-unstyled li').removeClass('active');
$('.dipendenti').addClass('active');

$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});