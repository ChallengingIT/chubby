function myFunction(){
  history.back();
}
const btn = document.getElementById("bottoneIndietro");

if (btn) {
    btn.addEventListener("click", myFunction);
}


