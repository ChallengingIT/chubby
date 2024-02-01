$(document).ready(function() {
    setTimeout(function() {
        $("#success-alert").alert('close');
    }, 5000);
});

$('.list-unstyled li').removeClass('active');
$('#progetti').addClass('active');

function Reset() {
    document.getElementById("staff").value = "";
    document.getElementById("cliente").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}