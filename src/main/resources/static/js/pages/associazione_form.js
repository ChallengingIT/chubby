$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});

$('.list-unstyled li').removeClass('active');
$('.need').addClass('active');
