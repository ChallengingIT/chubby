$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});

$('.list-unstyled li').removeClass('active');
$('.amministrazione').click();
$('.tesoreria').addClass('active');

function Reset() {
    document.getElementById("anno").value = "";
    document.getElementById("mese").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}