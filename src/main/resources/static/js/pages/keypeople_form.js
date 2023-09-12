$('li').removeClass('active');
$('.businessdev').addClass('active');

$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});

if($(".checkpa").is(":checked")) {
    $(".codice").show();
} else {
    $(".codice").hide();
}

$(".checkpa").click(function() {
    if($(this).is(":checked")) {
        $(".codice").show(300);
    } else {
        $(".codice").hide(200);
    }
});