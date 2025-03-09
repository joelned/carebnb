document.addEventListener("DOMContentLoaded", function () {
    const toggles = document.querySelectorAll(".toggle");
    const profilePicture = document.getElementById("profilePicture");
    const profilePictureInput = document.getElementById("profilePictureInput");
    const sidebarName = document.getElementById("sidebar-name");
    const nameInput = document.getElementById("name-input");

    // Handle profile image upload
    profilePicture.addEventListener("click", function () {
        profilePictureInput.click();
    });

    profilePictureInput.addEventListener("change", function () {
        if (profilePictureInput.files && profilePictureInput.files[0]) {
            const reader = new FileReader();
            reader.onload = function (e) {
                profilePicture.style.backgroundImage = `url(${e.target.result})`;
            };
            reader.readAsDataURL(profilePictureInput.files[0]);
        }
    });

    // Live update sidebar name
    nameInput.addEventListener("input", function () {
        sidebarName.textContent = nameInput.value || "Your Name";
    });

    // Ensure default toggle state reflects in console
    toggles.forEach(toggle => {
        console.log(`${toggle.previousElementSibling.innerText} set to ${toggle.checked}`);
    });
});
document.addEventListener('DOMContentLoaded', function () {
    // Select elements from the sidebar and the add apartment button
    const propertiesLink = document.querySelector('.sidebar nav a:first-child'); // First link (Properties)
    const clientLink = document.querySelector('.sidebar nav a:nth-child(2)'); // Second link (Client)
    const addApartmentButton = document.querySelector('.add-apartment'); // Add new apartment button

    // Add click event listeners to redirect to the appropriate pages
    propertiesLink.addEventListener('click', function (event) {
        event.preventDefault(); // Prevent default behavior
        window.location.href = 'prop.html'; // Redirect to prop.html
    });

    clientLink.addEventListener('click', function (event) {
        event.preventDefault(); // Prevent default behavior
        window.location.href = 'cli.html'; // Redirect to cli.html
    });

    addApartmentButton.addEventListener('click', function () {
        window.location.href = 'add.html'; // Redirect to add.html
    });
});
