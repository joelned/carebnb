const profileInput = document.getElementById('profile-input');
const profileImage = document.getElementById('profile-image');

profileInput.addEventListener('change', function() {
    const file = this.files[0];

    if (file) {
        const reader = new FileReader();

        reader.addEventListener('load', function() {
            profileImage.setAttribute('src', this.result);
            profileImage.style.display = "block";
        });

        reader.readAsDataURL(file);
    } else {
        profileImage.setAttribute('src', '#');
        profileImage.style.display = "none";
    }
});