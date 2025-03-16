document.addEventListener('DOMContentLoaded', function() {
    const offeringsContainer = document.querySelector('.offerings-container');
    const addMoreButton = document.getElementById('add-more-button');
    const newOfferingInput = document.getElementById('new-offering-input');
    const addNewOfferingButton = document.getElementById('add-new-offering');
    const imageUpload = document.getElementById('image-upload');
    const fileInput = document.getElementById('file-input');
    const propertyImages = document.querySelector('.property-images');
    const addPropertyButton = document.querySelector('.add-property');
    const form = document.getElementById("create-apartment-form");

    let offeringItems = [];
    let filesToUpload = [];

    function toggleOffering(offering) {
        if (offering.classList.contains('selected')) {
            offering.classList.remove('selected');
            offeringItems = offeringItems.filter(item => item !== offering.textContent.trim());
            offering.innerHTML = `<span>+ ${offering.textContent.replace('✔️', '').trim()}</span>`;
        } else {
            offering.classList.add('selected');
            offering.innerHTML = `<span role="img" aria-label="check">✔️</span>${offering.textContent}`;
            offeringItems.push(offering.textContent.replace("✔", "").trim());
        }
    }

    offeringsContainer.querySelectorAll('.offering').forEach(offering => {
        offering.addEventListener('click', () => toggleOffering(offering));
    });

    addMoreButton.addEventListener('click', function() {
        newOfferingInput.style.display = 'block';
    });

    addNewOfferingButton.addEventListener('click', function() {
        const newOfferingText = document.getElementById('new-offering-text').value;
        if (newOfferingText) {
            const newOfferingDiv = document.createElement('div');
            newOfferingDiv.classList.add('offering');
            newOfferingDiv.innerHTML = `<span> ${newOfferingText}</span>`;
            newOfferingDiv.addEventListener('click', () => toggleOffering(newOfferingDiv));
            offeringsContainer.insertBefore(newOfferingDiv, addMoreButton);
            document.getElementById('new-offering-text').value = '';
            newOfferingInput.style.display = 'none';
        }
    });

    imageUpload.addEventListener('click', function() {
        fileInput.click();
    });

    fileInput.addEventListener('change', function() {
        for (let i = 0; i < fileInput.files.length; i++) {
            const file = fileInput.files[i];

            if (!filesToUpload.some(f => f.name === file.name)) {
                filesToUpload.push(file); // Add to the filesToUpload array
            }

            const reader = new FileReader();
            reader.onload = function(e) {
                const newContainer = document.createElement('div');
                newContainer.classList.add('image-container');
                newContainer.dataset.fileName = file.name;
                newContainer.innerHTML = `
                    <img src="${e.target.result}" alt="Property Image">
                    <div class="image-options">
                        <button class="replace-button">Replace</button>
                        <button class="remove-button">Remove</button>
                    </div>
                `;
                propertyImages.appendChild(newContainer);

                newContainer.querySelector('.replace-button').addEventListener('click', function() {
                    replaceImage(newContainer);
                });
                newContainer.querySelector('.remove-button').addEventListener('click', function() {
                    removeImage(newContainer);
                });
            };
            reader.readAsDataURL(file);
        }
    });

    function replaceImage(container) {
        const newFileInput = document.createElement('input');
        newFileInput.type = 'file';
        newFileInput.accept = 'image/*';
        newFileInput.addEventListener('change', function() {
            const file = newFileInput.files[0];
            if (file) {
                const oldFileName = container.dataset.fileName;
                filesToUpload = filesToUpload.filter(f => f.name !== oldFileName);

                filesToUpload.push(file);
                container.dataset.fileName = file.name;

                const reader = new FileReader();
                reader.onload = function(e) {
                    container.querySelector('img').src = e.target.result;
                };
                reader.readAsDataURL(file);
            }
        });
        newFileInput.click();
    }

    function removeImage(container) {
        const fileName = container.dataset.fileName;
        filesToUpload = filesToUpload.filter(f => f.name !== fileName);
        container.remove();
    }

    form.addEventListener('submit', function(event) {
        event.preventDefault();

        const formData = new FormData(form);

        formData.delete('images[]');

        filesToUpload.forEach(file => {
            formData.append('images[]', file);
        });

        formData.append('offerings', offeringItems.join(', '));

        fetch(form.action, {
            method: form.method,
            body: formData
        })
            .then(data => {
            console.log('Success:', data);
            window.location.href="/home"
        })
            .catch(error => {
            console.error('Error:', error);
        });
    });
    function disableButton(){
        const button = document.getElementById("submitButton");
        button.disabled = true;

        setTimeout(function() {
            button.disabled = false
        }, 2000);
    }
});
