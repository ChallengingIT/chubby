document.getElementById('theDate').value = new Date().toISOString().substring(0, 10);

$('li').removeClass('active');
$('.staffing').addClass('active');