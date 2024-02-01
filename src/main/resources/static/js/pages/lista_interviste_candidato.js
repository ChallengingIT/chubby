document.getElementById('theDate').value = new Date().toISOString().substring(0, 10);

$('.list-unstyled li').removeClass('active');
$('.staffing').addClass('active');

function Reset() {
    document.getElementById("stato").value = "";
    document.getElementById("owner").value = "";
    document.getElementById("theDate").value = "";
}

// Please do not use inline event handlers, use this instead:
document.getElementById("btnReset").onclick = function () {
    Reset();
}