const currentPath = window.location.pathname;
const navLinks = document.querySelectorAll('.sidebar nav a');

navLinks.forEach(link => {
    link.classList.remove('active');
    if (link.getAttribute('href') === currentPath) {
        link.classList.add('active');
    }
});

function searchTable(){
    let input = document.getElementById("searchInput").value.toLowerCase();

    let rows = document.querySelectorAll("#clients-table tbody tr");

   rows.forEach(row => {
       let cells = row.getElementsByTagName("td");
       let matchFound = false;

       for(let cell of cells){
           if(cell.textContent.toLowerCase().includes(input)){
               matchFound = true;
               break;
           }
       }
       row.style.display = matchFound ? "" : "none";
   });


}