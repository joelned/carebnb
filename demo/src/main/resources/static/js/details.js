document.addEventListener('DOMContentLoaded', function() {
    // 1. Get apartmentId from URL
    const urlParams = new URLSearchParams(window.location.search);
    const apartmentId = urlParams.get('apartmentId');

    if (!apartmentId) {
        console.error("Apartment ID not found in URL.");
        return; // Stop execution if no ID
    }

    // 2. Define Dummy Data
    const dummyApartment = {
        apartmentId: "default",
        name: "Loading...",
        description: "Fetching data...",
        address: "Loading address...",
        rating: 0.0,
        reviews: 0,
        images: ["bedroom.jpg"],
        amenities: ["Loading...", "Loading..."],
        guests: 1,
        about: "Loading about...",
        checkin: "Loading",
        checkout: "Loading"
    };

     // 3. Populate the page with dummy apartment data immediately
        document.getElementById('apartment-name').textContent = dummyApartment.name;
        document.getElementById('apartment-title').textContent = dummyApartment.name;
        document.getElementById('apartment-description').textContent = dummyApartment.description;
        document.getElementById('apartment-address').textContent = dummyApartment.address;
        document.getElementById('apartment-rating').textContent = dummyApartment.rating;
        document.getElementById('apartment-reviews').textContent = dummyApartment.reviews;
        document.getElementById('apartment-guests').textContent = dummyApartment.guests;
        document.getElementById('apartment-about').textContent = dummyApartment.about;

        // Images
        const imageGallery = document.getElementById('image-gallery');
        imageGallery.innerHTML = ''; // Clear existing images
        dummyApartment.images.forEach(image => {
            const img = document.createElement('img');
            img.src = image;
            img.alt = dummyApartment.name;
            imageGallery.appendChild(img);
        });

        // Amenities
        const amenitiesList = document.getElementById('amenities-list');
        amenitiesList.innerHTML = ''; // Clear existing amenities
        dummyApartment.amenities.forEach(amenity => {
            const li = document.createElement('li');
            li.textContent = amenity;
            amenitiesList.appendChild(li);
        });

    // 4. Fetch apartment data from API
    const apiUrl = `/api/apartments/${apartmentId}`; // Replace with your API endpoint

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(apartment => {
            // 5. Populate the page with API data
            document.getElementById('apartment-name').textContent = apartment.name;
            document.getElementById('apartment-title').textContent = apartment.name;
            document.getElementById('apartment-description').textContent = apartment.description;
            document.getElementById('apartment-address').textContent = apartment.address;
            document.getElementById('apartment-rating').textContent = apartment.rating;
            document.getElementById('apartment-reviews').textContent = apartment.reviews;
            document.getElementById('apartment-guests').textContent = apartment.guests;
            document.getElementById('apartment-about').textContent = apartment.about;

            // Images
            const imageGallery = document.getElementById('image-gallery');
            imageGallery.innerHTML = ''; // Clear existing images
            apartment.images.forEach(image => {
                const img = document.createElement('img');
                img.src = image;
                img.alt = apartment.name;
                imageGallery.appendChild(img);
            });

            // Amenities
            const amenitiesList = document.getElementById('amenities-list');
            amenitiesList.innerHTML = ''; // Clear existing amenities
            apartment.amenities.forEach(amenity => {
                const li = document.createElement('li');
                li.textContent = amenity;
                amenitiesList.appendChild(li);
            });
        })
        .catch(error => console.error("Error fetching apartment:", error));
});
