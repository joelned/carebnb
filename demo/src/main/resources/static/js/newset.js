//document.addEventListener("DOMContentLoaded", function () {
//    const profilePicture = document.getElementById("profilePicture");
//    const profilePictureInput = document.getElementById("profilePictureInput");
//    const settingsForm = document.getElementById("settingsForm");
//    const sidebarName = document.querySelector('.sidebar .user-name');
//    const nameInput = document.getElementById("name");
//
//    profilePicture.addEventListener("click", function () {
//        profilePictureInput.click();
//    });
//
//    profilePictureInput.addEventListener("change", function () {
//        const file = this.files[0];
//
//        if (file) {
//            const reader = new FileReader();
//
//            reader.addEventListener('load', function() {
//                profilePicture.style.backgroundImage = `url(${this.result})`;
//            });
//
//            reader.readAsDataURL(file);
//        } else {
//            profilePicture.style.backgroundImage = 'none';
//        }
//    });
//
//    // Update sidebar name on input change
//    nameInput.addEventListener('input', function() {
//        sidebarName.textContent = nameInput.value || 'Your Name';
//    });
//
//    settingsForm.addEventListener("submit", function (e) {
//        e.preventDefault();
//
//        const userData = {
//            name: document.getElementById("name").value,
//            age: document.getElementById("age").value,
//            email: document.getElementById("email").value,
//            gender: document.getElementById("gender").value,
//            dependents: document.getElementById("dependents").value,
//            origin: document.getElementById("origin").value,
//            flee: document.getElementById("flee").value,
//            language: document.getElementById("language").value,
//            location: document.getElementById("location").value,
//            needs: document.getElementById("needs").value,
//            visibility: document.getElementById("visibility").checked
//        };
//
//        console.log("Updated User Data:", userData);
//        alert("Settings updated successfully!");
//    });
//});
