$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});

$('.list-unstyled li').removeClass('active');
$('.amministrazione').click();
$('.fatturazioneattiva').addClass('active');

function Reset() {
    document.getElementById("cliente").value = "";
    document.getElementById("stato").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}