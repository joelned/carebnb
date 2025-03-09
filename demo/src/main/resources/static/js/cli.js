document.addEventListener('DOMContentLoaded', function() {
    const clientsTableBody = document.getElementById('clients-table-body');
    const bookingDetailsDiv = document.getElementById('booking-details');
    const bookingDetailsContent = document.getElementById('booking-details-content');
    const noClientsMessage = document.getElementById('no-clients-message');
    let highlightedRow = null; // Track the highlighted row
    let editingClient = null; // Track which client is being edited

    // Dummy clients data
    const clientsData = [
        {
            client: 'John Doe',
            checkInOut: '2024-01-01 to 2024-01-07',
            status: 'In Progress',
            propertyName: 'Apartment 101',
            request: 'like'
        },
        {
            client: 'Jane Smith',
            checkInOut: '2024-02-01 to 2024-02-14',
            status: 'In Progress',
            propertyName: 'Apartment 202',
            request: 'dislike'
        },
        {
            client: 'Bob Johnson',
            checkInOut: '2024-03-01 to 2024-03-31',
            status: 'Checked In',
            propertyName: 'Apartment 303',
            request: 'approved'
        },
        {
            client: 'Alice Brown',
            checkInOut: '2024-04-01 to 2024-04-30',
            status: 'Checked Out',
            propertyName: 'Apartment 404',
            request: 'closed'
        },
        {
            client: 'Mike Davis',
            checkInOut: '2024-05-01 to 2024-05-31',
            status: 'Booked',
            propertyName: 'Apartment 505',
            request: 'approved'
        },
        {
            client: 'Emily Taylor',
            checkInOut: '2024-06-01 to 2024-06-30',
            status: 'Booked',
            propertyName: 'Apartment 606',
            request: 'denied'
        }
    ];

    // Display initial data
    displayClients(clientsData);

    function displayClients(data) {
        if (data.length === 0) {
            noClientsMessage.style.display = 'block';
            clientsTableBody.innerHTML = ''; // Ensure table is empty
            return;
        } else {
            noClientsMessage.style.display = 'none';
        }

        clientsTableBody.innerHTML = ''; // Clear existing rows
        data.forEach(client => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${client.client}</td>
                <td>${client.checkInOut}</td>
                <td style="color: ${getStatusColor(client.status)}; opacity: 0.6;">${client.status}</td>
                <td>${client.propertyName}</td>
                <td>
                    <div class="request-buttons">
                        ${getRequestButtons(client)}
                    </div>
                </td>
            `;

            row.addEventListener('click', () => {
                // Remove highlight from previously highlighted row
                if (highlightedRow) {
                    highlightedRow.classList.remove('highlighted-row');
                }

                // Highlight the clicked row
                row.classList.add('highlighted-row');
                highlightedRow = row;

                showBookingDetails(client);
                editingClient = client; // Track the current client being edited
            });

            clientsTableBody.appendChild(row);
        });

        applySearchFilter();
    }

    function getStatusColor(status) {
        switch (status.toLowerCase()) {
            case 'in progress':
                return '#B426CD';
            case 'checked out':
                return '#C31717';
            case 'booked':
                return '#251EB2';
            case 'checked in':
                return '#49A64C';
            default:
                return '#333'; // Default color if status is not recognized
        }
    }

    function getRequestButtons(client) {
        let buttonsHTML = '';
        if (client.request === 'like' || client.request === 'dislike') {
            const likeButtonClass = client.request === 'like' ? 'like-button' : 'dislike-button';
            const dislikeButtonClass = client.request === 'dislike' ? 'like-button' : 'dislike-button';
            buttonsHTML = `
                <button class="${likeButtonClass}" data-action="approve"><i class="fas fa-thumbs-up"></i></button>
                <button class="${dislikeButtonClass}" data-action="deny"><i class="fas fa-thumbs-down"></i></button>
            `;
        } else if (client.request === 'approved') {
            buttonsHTML = `<button class="approved-button" disabled>Approved</button>`;
        } else if (client.request === 'denied') {
            buttonsHTML = `<button class="denied-button" disabled>Declined</button>`;
        } else if (client.request === 'closed') {
            buttonsHTML = `<button class="closed-button" disabled>Closed</button>`;
        }
        return buttonsHTML;
    }

    function showBookingDetails(client) {
        bookingDetailsContent.innerHTML = `
            <h3>${client.client}</h3>
            <p><strong>CHECK IN & OUT:</strong> <span id="check-in-out">${client.checkInOut}</span></p>
            <p><strong>STATUS:</strong> <span style="color:${getStatusColor(client.status)}; opacity: 0.6;" id="status">${client.status}</span></p>
            <p><strong>PROPERTY NAME:</strong> <span id="property-name">${client.propertyName}</span></p>
            <p><strong>REQUEST:</strong> <span id="request">${client.request}</span></p>
            <div class="update-booking">
                <button class="update-button">Update</button>
                <button class="cancel-button" style="display:none;">Cancel</button>
                <button class="save-button" style="display:none;">Save</button>
            </div>
        `;
        bookingDetailsDiv.style.display = "block";

        document.querySelector('.update-button').addEventListener('click', () => {
            makeEditable(client);
        });
        document.querySelector('.cancel-button').addEventListener('click', () => {
            showBookingDetails(client);
        });
        document.querySelector('.save-button').addEventListener('click', () => {
            saveBookingDetails(client);
        });
    }

    function makeEditable(client) {
        document.querySelector('.update-button').style.display = 'none';
        document.querySelector('.cancel-button').style.display = 'inline-block';
        document.querySelector('.save-button').style.display = 'inline-block';

        const checkInOutSpan = document.getElementById('check-in-out');
        const statusSpan = document.getElementById('status');
        const propertyNameSpan = document.getElementById('property-name');
        const requestSpan = document.getElementById('request');

        checkInOutSpan.outerHTML = `<input type="text" id="check-in-out" value="${client.checkInOut}">`;
        statusSpan.outerHTML = `<select id="status" style="color:${getStatusColor(client.status)}; opacity: 0.6;">
            <option value="In Progress" ${client.status === "In Progress" ? "selected" : ""}>In Progress</option>
            <option value="Checked In" ${client.status === "Checked In" ? "selected" : ""}>Checked In</option>
            <option value="Checked Out" ${client.status === "Checked Out" ? "selected" : ""}>Checked Out</option>
            <option value="Booked" ${client.status === "Booked" ? "selected" : ""}>Booked</option>
        </select>`;
        propertyNameSpan.outerHTML = `<input type="text" id="property-name" value="${client.propertyName}">`;
        requestSpan.outerHTML = `<select id="request">
            <option value="like" ${client.request === "like" ? "selected" : ""}>Like</option>
            <option value="dislike" ${client.request === "dislike" ? "selected" : ""}>Dislike</option>
            <option value="approved" ${client.request === "approved" ? "selected" : ""}>Approved</option>
            <option value="denied" ${client.request === "denied" ? "selected" : ""}>Denied</option>
            <option value="closed" ${client.request === "closed" ? "selected" : ""}>Closed</option>
        </select>`;
    }

    function saveBookingDetails(client) {
        const updatedCheckInOut = document.getElementById('check-in-out').value;
        const updatedStatus = document.getElementById('status').value;
        const updatedPropertyName = document.getElementById('property-name').value;
        const updatedRequest = document.getElementById('request').value;

        client.checkInOut = updatedCheckInOut;
        client.propertyName = updatedPropertyName;
        client.request = updatedRequest;

        // Update status to 'Booked' if it was 'In Progress' and request is updated
        if (client.status === 'In Progress' && updatedRequest !== 'like' && updatedRequest !== 'dislike') {
            client.status = 'Booked';
        } else {
            client.status = updatedStatus; // Otherwise, use the selected status
        }

        displayClients(clientsData);
        showBookingDetails(client);
    }

    function applySearchFilter() {
        const searchValue = document.querySelector('.location-filter input').value.toLowerCase();
        const rows = clientsTableBody.rows;

        for (let i = 0; i < rows.length; i++) {
            let found = false;
            const rowCells = rows[i].cells;

            for (let j = 0; j < rowCells.length; j++) {
                const cellText = rowCells[j].textContent.toLowerCase();
                if (cellText.includes(searchValue)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                rows[i].style.display = '';
            } else {
                rows[i].style.display = 'none';
            }
        }

        // Check if any rows are visible
        const visibleRows = Array.from(rows).filter(row => row.style.display !== 'none');

        if (visibleRows.length === 0) {
            clientsTableBody.innerHTML = `
                <tr>
                    <td colspan="5" style="text-align:center; font-style:italic;">Not found 😔</td>
                </tr>
            `;
        } else {
            // Do nothing if there are visible rows
        }
    }

    // Event delegation for handling button clicks within the table
    clientsTableBody.addEventListener('click', function(event) {
        if (event.target.tagName === 'BUTTON') {
            const button = event.target;
            const action = button.dataset.action;
            const row = button.closest('tr'); // Find the closest table row
            const clientName = row.querySelector('td:first-child').textContent; // Get the client name from the row

            // Find the corresponding client data in the clientsData array
            let clientIndex;
            if (window.clientsFromAPI) {
                clientIndex = window.clientsFromAPI.findIndex(c => c.client === clientName);
            } else {
                clientIndex = clientsData.findIndex(c => c.client === clientName);
            }

            if (clientIndex !== -1) {
                if (action === 'approve') {
                    if (window.clientsFromAPI) {
                        window.clientsFromAPI[clientIndex].request = 'approved';
                        window.clientsFromAPI[clientIndex].status = 'Checked In';
                    } else {
                        clientsData[clientIndex].request = 'approved';
                        clientsData[clientIndex].status = 'Checked In';
                    }
                } else if (action === 'deny') {
                    if (window.clientsFromAPI) {
                        window.clientsFromAPI[clientIndex].request = 'denied';
                    } else {
                        clientsData[clientIndex].request = 'denied';
                    }
                }
                if (window.clientsFromAPI) {
                    displayClients(window.clientsFromAPI); // Re-render the table to update the buttons
                } else {
                    displayClients(clientsData); // Re-render the table to update the buttons
                }
            }
        }
    });

    profileImageUpload.addEventListener('change', function(event) {
        const file = event.target.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                profileImage.src = e.target.result;
            }
            reader.readAsDataURL(file);
        }
    });

    // Menu Toggle Functionality
    menuToggle.addEventListener('click', function(e) {
        e.preventDefault();
        sidebar.classList.toggle('open');
    });

    // Add event listener to the search input
    document.querySelector('.location-filter input').addEventListener('input', applySearchFilter);
});

document.addEventListener('DOMContentLoaded', function () {
    // Select elements from the sidebar and the add apartment button
    const propertiesLink = document.querySelector('.sidebar nav a:first-child'); // First link (Properties)
    const settingsLink = document.querySelector('.sidebar nav a:nth-child(3)'); // Third link (Settings)
    const addApartmentButton = document.querySelector('.add-apartment-button'); // Add new apartment button

    // Add click event listeners to redirect to the appropriate pages
    propertiesLink.addEventListener('click', function (event) {
        event.preventDefault(); // Prevent default behavior
        window.location.href = 'prop.html'; // Redirect to prop.html
    });

    settingsLink.addEventListener('click', function (event) {
        event.preventDefault(); // Prevent default behavior
        window.location.href = 'set.html'; // Redirect to set.html
    });

    addApartmentButton.addEventListener('click', function () {
        window.location.href = 'add.html'; // Redirect to add.html
    });
});
