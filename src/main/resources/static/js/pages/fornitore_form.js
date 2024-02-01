$('.list-unstyled li').removeClass('active');
$('.amministrazione').click();
$('.fornitori').addClass('active');

$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});