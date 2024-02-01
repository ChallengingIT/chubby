function myFunction(){
  history.go(-1);
}
const btn = document.getElementById("bottoneIndietro");

if (btn) {
    btn.addEventListener("click", myFunction);
}


