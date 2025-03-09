document.addEventListener('DOMContentLoaded', function() {
    const propertiesGrid = document.getElementById('properties-grid');
    const noPropertiesMessage = document.getElementById('no-properties');
    const addPropertyButton = document.getElementById('add-property-button');
    const locationSelect = document.getElementById('location-select');

    // Load properties from local storage
    let properties = JSON.parse(localStorage.getItem('properties')) || [];

    // **NEW: Array to store unique locations**
    let availableLocations = [];

    // Function to display properties
    function displayProperties(propertiesToDisplay = properties) {
        propertiesGrid.innerHTML = ''; // Clear existing properties

        if (propertiesToDisplay.length === 0) {
            noPropertiesMessage.classList.remove('hidden');
        } else {
            noPropertiesMessage.classList.add('hidden');
            propertiesToDisplay.forEach(property => {
                const propertyCard = createPropertyCard(property);
                propertiesGrid.appendChild(propertyCard);
            });
        }
    }

    // Function to create a property card element
    function createPropertyCard(property) {
        const propertyCard = document.createElement('div');
        propertyCard.classList.add('property-card');

        propertyCard.innerHTML = `
            <img src="${property.images && property.images[0] ? property.images[0] : 'placeholder-image.jpg'}" alt="Property Image">
            <h3>${property.title}</h3>
            <div class="actions">
                <button class="details">See Details</button>
                <button class="edit">Edit Apartment</button>
            </div>
            <div class="location">
            <span class="candy-location-icon">
            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 24 24" fill="#F44336">
                <path d="M12 2C8.13 2 5 5.13 5 9c0 4.52 6.47 12.15 6.75 12.47.06.07.14.11.21.11s.15-.04.21-.11C12.53 21.15 19 13.52 19 9c0-3.87-3.13-7-7-7zm0 2a5 5 0 1 1 0 10 5 5 0 0 1 0-10z"/>
            </svg>
            </span> ${property.address}
        </div>
        `;

        return propertyCard;
    }

    // Function to extract the city from the address (basic implementation)
    function extractCity(address) {
        const parts = address.split(',');
        if (parts.length > 1) {
            return parts[parts.length - 1].trim(); // Get the last part and trim whitespace
        }
        return address; // Return the full address if no comma
    }

    // Function to populate the location filter dropdown
    function populateLocationFilter() {
        locationSelect.innerHTML = '<option value="">Location</option>'; // Reset the options

        availableLocations.forEach(location => {
            const option = document.createElement('option');
            option.value = location;
            option.textContent = location;
            locationSelect.appendChild(option);
        });
    }

    // Function to add a location to the available locations (if not already there)
    function addLocation(location) {
        if (!availableLocations.includes(location)) {
            availableLocations.push(location);
            availableLocations.sort(); // Sort locations alphabetically
            populateLocationFilter(); // Update the dropdown
        }
    }

    // Check for a new property passed in the URL (query parameter)
    const urlParams = new URLSearchParams(window.location.search);
    const newPropertyJson = urlParams.get('newProperty');

    if (newPropertyJson) {
        try {
            const newProperty = JSON.parse(decodeURIComponent(newPropertyJson));

            // **Extract the city and add it to the available locations**
            const city = extractCity(newProperty.address);
            addLocation(city);

            properties.push(newProperty);
            localStorage.setItem('properties', JSON.stringify(properties));

            // Clear the URL parameter (optional, but good practice to avoid adding it again on refresh)
            window.history.replaceState({}, document.title, window.location.pathname);
        } catch (error) {
            console.error('Error parsing new property from URL:', error);
            // Handle the error (e.g., display an error message to the user)
        }
    }

    // Initial display of properties
    displayProperties();

    // **Populate the location filter on initial load**
    properties.forEach(property => {
        const city = extractCity(property.address);
        addLocation(city);
    });

    // Check if properties is empty, if so, show no properties message
    if (properties.length === 0) {
        noPropertiesMessage.classList.remove('hidden');
    } else {
        noPropertiesMessage.classList.add('hidden');
    }

    // Add property button click handler
    addPropertyButton.addEventListener('click', function() {
        // Redirect to the add-property.html page
        window.location.href = 'add.html';
    });

    // Location filter change handler
    locationSelect.addEventListener('change', function() {
        const selectedLocation = locationSelect.value;
        if (selectedLocation === '') {
            displayProperties(); // Show all properties
        } else {
            const filteredProperties = properties.filter(property => extractCity(property.address).toLowerCase().includes(selectedLocation.toLowerCase()));
            displayProperties(filteredProperties);
        }
    });
});
document.addEventListener('DOMContentLoaded', function () {
    // Select elements from the sidebar and the add property button
    const clientsLink = document.querySelector('.sidebar nav a:nth-child(2)'); // Second link (Clients)
    const settingsLink = document.querySelector('.sidebar nav a:nth-child(3)'); // Third link (Settings)
    const addPropertyButton = document.getElementById('add-property-button'); // Add New Apartment button

    // Add click event listeners to redirect to the appropriate pages
    clientsLink.addEventListener('click', function (event) {
        event.preventDefault(); // Prevent default behavior
        window.location.href = 'cli.html'; // Redirect to cli.html
    });

    settingsLink.addEventListener('click', function (event) {
        event.preventDefault(); // Prevent default behavior
        window.location.href = 'set.html'; // Redirect to set.html
    });

    addPropertyButton.addEventListener('click', function () {
        window.location.href = 'add.html'; // Redirect to add.html
    });
});
document.addEventListener('DOMContentLoaded', function () {
    // Select all "See Details" buttons
    const seeDetailsButtons = document.querySelectorAll('.details');

    // Add click event listener to each "See Details" button
    seeDetailsButtons.forEach((button, index) => {
        button.addEventListener('click', function () {
            // Redirect to details.html with an optional query parameter for the property ID
            window.location.href = `details.html?propertyId=${index + 1}`;
        });
    });

    // Select all "Edit Apartment" buttons
    const editApartmentButtons = document.querySelectorAll('.edit');

    // Add click event listener to each "Edit Apartment" button
    editApartmentButtons.forEach((button, index) => {
        button.addEventListener('click', function () {
            // Redirect to editapart.html with an optional query parameter for the property ID
            window.location.href = `editapart.html?propertyId=${index + 1}`;
        });
    });
});

