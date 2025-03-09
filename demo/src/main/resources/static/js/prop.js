const currentPath = window.location.pathname;
const navLinks = document.querySelectorAll('.sidebar nav a');

navLinks.forEach(link => {
    link.classList.remove('active');
    if (link.getAttribute('href') === currentPath) {
        link.classList.add('active');
    }
});