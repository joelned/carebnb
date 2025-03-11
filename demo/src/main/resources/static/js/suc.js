
// Example: Add an alert when the "Join as Refugee" button is clicked
// const refugeeButton = document.querySelector('.refugee-button');

// if (refugeeButton) {
//   refugeeButton.addEventListener('click', () => {
//     alert('You clicked the Join as Refugee button!');
//   });
// }

// Example: Add an alert when the "Join as Host" button is clicked
// const hostButton = document.querySelector('.host-button');

// if (hostButton) {
//   hostButton.addEventListener('click', () => {
//     alert('You clicked the Join as Host button!');
//   });
// }

// function showCarebnbPopup() {
//   document.getElementById("carebnbSuccessPopup").style.display = "flex";
// }

// function closeCarebnbPopup() {
//   document.getElementById("carebnbSuccessPopup").style.display = "none";
// }
// Example: Add an alert when the "Join as Refugee" button is clicked
const refugeeButton = document.querySelector('.refugee-button');

if (refugeeButton) {
  refugeeButton.addEventListener('click', () => {
    alert('You clicked the Join as Refugee button!');
  });
}

// Example: Add an alert when the "Join as Host" button is clicked
const hostButton = document.querySelector('.host-button');

if (hostButton) {
  hostButton.addEventListener('click', () => {
    alert('You clicked the Join as Host button!');
  });
}

function showCarebnbPopup() {
  document.getElementById("carebnbSuccessPopup").style.display = "flex";
}

function closeCarebnbPopup() {
  document.getElementById("carebnbSuccessPopup").style.display = "none";
}

// Automatically show the popup when the page loads
document.addEventListener('DOMContentLoaded', function() {
  showCarebnbPopup();
});