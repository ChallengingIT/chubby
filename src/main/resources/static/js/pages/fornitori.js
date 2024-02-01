$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});

$('.list-unstyled li').removeClass('active');
$('.amministrazione').click();
$('.fornitori').addClass('active');

function Reset() {
    document.getElementById("ragione").value = "";
    document.getElementById("referente").value = "";
    document.getElementById("email").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}