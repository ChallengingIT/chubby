$('.list-unstyled li').removeClass('active');
$('.amministrazione').click();
$('.fatturazioneattiva').addClass('active');

$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});