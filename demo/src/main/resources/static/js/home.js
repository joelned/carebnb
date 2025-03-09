//document.addEventListener('DOMContentLoaded', function () {
//    const container = document.querySelector('.container');
//    const checkInInput = document.getElementById('check-in');
//    const checkOutInput = document.getElementById('check-out');
//    const searchButton = document.querySelector('#search-button');
//    const cards = document.querySelectorAll('.card'); // Get all card elements
//
//    // Sample API endpoint (replace with your actual endpoint)
//    const API_ENDPOINT = 'https://your-api.com/apartments';
//
//    // Initialize Flatpickr for date pickers
//    flatpickr(checkInInput, {
//        mode: "single",
//        dateFormat: "Y-m-d",
//        placeholder: "CHECK IN",
//        onChange: function (selectedDates, dateStr, instance) {
//            // You can add logic here to update the UI or store the selected date
//            console.log("Check-in date selected:", dateStr);
//        }
//    });
//
//    flatpickr(checkOutInput, {
//        mode: "single",
//        dateFormat: "Y-m-d",
//        placeholder: "CHECK OUT",
//        onChange: function (selectedDates, dateStr, instance) {
//            // You can add logic here to update the UI or store the selected date
//            console.log("Check-out date selected:", dateStr);
//        }
//    });
//
//    // Function to fetch apartments from the API
//    async function fetchApartments() {
//        try {
//            const response = await fetch(API_ENDPOINT);
//            const apartments = await response.json();
//            return apartments;
//        } catch (error) {
//            console.error('Error fetching apartments:', error);
//            return [];
//        }
//    }
//
//    // Function to create a carousel
//    function createCarousel(carouselElement) {
//        const carouselInner = carouselElement.querySelector('.carousel-inner');
//        const prevButton = carouselElement.querySelector('.carousel-control.prev');
//        const nextButton = carouselElement.querySelector('.carousel-control.next');
//        const items = Array.from(carouselInner.children);
//        let currentItem = 0;
//
//        // Initialize - Show the first item
//        showItem(currentItem);
//
//        // Functions
//        function showItem(index) {
//            // Hide all items
//            items.forEach(item => item.classList.remove('active'));
//
//            // Show the selected item
//            items[index].classList.add('active');
//        }
//
//        function goToNext() {
//            currentItem++;
//            if (currentItem >= items.length) {
//                currentItem = 0; // Go back to the beginning
//            }
//            showItem(currentItem);
//        }
//
//        function goToPrev() {
//            currentItem--;
//            if (currentItem < 0) {
//                currentItem = items.length - 1; // Go to the end
//            }
//            showItem(currentItem);
//        }
//
//        // Event listeners
//        nextButton.addEventListener('click', () => {
//            goToNext();
//        });
//        prevButton.addEventListener('click', () => {
//            goToPrev();
//        });
//    }
//
//    // Function to create an apartment card
//    function createApartmentCard(apartment) {
//        const card = document.createElement('div');
//        card.classList.add('card');
//        card.setAttribute('data-location', apartment.location); // Add location data
//
//        const carousel = document.createElement('div');
//        card.appendChild(carousel);
//        carousel.classList.add('carousel');
//
//        const carouselInner = document.createElement('div');
//        carouselInner.classList.add('carousel-inner');
//        apartment.images.forEach(image => {
//            const carouselItem = document.createElement('div');
//            carouselItem.classList.add('carousel-item');
//            const img = document.createElement('img');
//            img.src = image;
//            img.alt = 'Apartment Image';
//            carouselItem.appendChild(img);
//            carouselInner.appendChild(carouselItem);
//        });
//        carousel.appendChild(carouselInner);
//
//        const prevButton = document.createElement('button');
//        prevButton.classList.add('carousel-control', 'prev');
//        prevButton.innerHTML = '<i class="fas fa-chevron-left"></i>';
//        carousel.appendChild(prevButton);
//
//        const nextButton = document.createElement('button');
//        nextButton.classList.add('carousel-control', 'next');
//        nextButton.innerHTML = '<i class="fas fa-chevron-right"></i>';
//        carousel.appendChild(nextButton);
//
//        const cardBody = document.createElement('div');
//        cardBody.classList.add('card-body');
//
//        const cardTitle = document.createElement('h3');
//        cardTitle.classList.add('card-title');
//        cardTitle.textContent = apartment.location;
//
//        const cardRating = document.createElement('div');
//        cardRating.classList.add('card-rating');
//        cardRating.innerHTML = `<span class="rating"><i class="fas fa-star"></i> ${apartment.rating}</span>`;
//
//        const cardSubtitle = document.createElement('h4');
//        cardSubtitle.classList.add('card-subtitle');
//        cardSubtitle.textContent = `Stay with ${apartment.host}`;
//
//        cardBody.appendChild(cardTitle);
//        cardBody.appendChild(cardRating);
//        cardBody.appendChild(cardSubtitle);
//
//        card.appendChild(cardBody);
//
//        return card;
//    }
//
//    // Function to display apartments
//    function displayApartments(apartments) {
//        container.innerHTML = ''; // Clear existing cards
//
//        apartments.forEach(apartment => {
//            const card = createApartmentCard(apartment);
//            container.appendChild(card);
//        });
//
//        // Call after apartments are displayed
//        const carouselElements = document.querySelectorAll('.carousel');
//        carouselElements.forEach(carouselElement => {
//            createCarousel(carouselElement);
//        });
//    }
//
//    // Function to handle search
//    async function handleSearch() {
//        const where = document.getElementById('where').value.toLowerCase(); // to lowercase
//        const allCards = document.querySelectorAll('.card'); // Get all card elements
//
//        allCards.forEach(card => {
//            const location = card.dataset.location.toLowerCase(); // Get location attribute
//
//            if (location.includes(where)) {
//                card.style.display = 'block'; // Show card
//            } else {
//                card.style.display = 'none'; // Hide card
//            }
//        });
//    }
//
//    // Event listener for the search button
//    searchButton.addEventListener('click', handleSearch);
//
//    // Load apartments on page load
//    async function init() {
//
//        // Initial Dummy Data
//        const initialApartments = [
//            {
//                location: "Ikeja, Lagos", host: "Alex", rating: 4.95, images: [
//                    "livingroom.jpeg",
//                    "bedroom.jpg",
//                    "kitchen.jpeg"
//                ]
//            },
//            {
//                location: "Ikeja, Lagos", host: "Alex", rating: 4.95, images: [
//                    "livingroom.jpeg",
//                    "bedroom.jpg",
//                    "kitchen.jpeg"
//                ]
//            },
//            {
//                location: "Ikeja, Lagos", host: "Alex", rating: 4.95, images: [
//                    "livingroom.jpeg",
//                    "bedroom.jpg",
//                    "kitchen.jpeg"
//                ]
//            }
//        ];
//
//        displayApartments(initialApartments);
//
//    }
//
//    init();
//});
//let apartmentIdCounter = 1;
//
//document.addEventListener('DOMContentLoaded', function () {
//    // Select all cards
//    const cards = document.querySelectorAll('.card');
//
//    // Add click event listener to each card
//    cards.forEach(card => {
//        card.addEventListener('click', function (event) {
//            // Check if the clicked element is a carousel button
//            if (
//                event.target.classList.contains('prev') ||
//                event.target.classList.contains('next')
//            ) {
//                // Stop propagation of the click event to avoid triggering card click
//                event.stopPropagation();
//                return;
//            }
//
//            // Navigate to details page if it's not a carousel button
//            const apartmentId = Array.from(cards).indexOf(card) + 1; // Use index as ID
//            window.location.href = `details.html?apartmentId=${apartmentId}`;
//        });
//    });
//
//    // Carousel functionality (example)
//    const carousels = document.querySelectorAll('.carousel');
//    carousels.forEach(carousel => {
//        const prevButton = carousel.querySelector('.prev');
//        const nextButton = carousel.querySelector('.next');
//        const slides = carousel.querySelectorAll('.carousel-item');
//        let currentIndex = 0;
//
//        const updateCarousel = () => {
//            slides.forEach((slide, index) => {
//                slide.style.display = index === currentIndex ? 'block' : 'none';
//            });
//        };
//
//        prevButton.addEventListener('click', () => {
//            currentIndex = (currentIndex > 0) ? currentIndex - 1 : slides.length - 1;
//            updateCarousel();
//        });
//
//        nextButton.addEventListener('click', () => {
//            currentIndex = (currentIndex < slides.length - 1) ? currentIndex + 1 : 0;
//            updateCarousel();
//        });
//
//        updateCarousel(); // Initialize carousel display
//    });
//});
