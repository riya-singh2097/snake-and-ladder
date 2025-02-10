document.addEventListener("DOMContentLoaded", function () {
    const logButton = document.getElementById("log");
    
    logButton.addEventListener("click", function () {
        fetchLogData();
    });
});

function fetchLogData() {
    fetch('LogServlet') // Calling the servlet to retrieve data from the database
        .then(response => response.text())
        .then(data => {
            showLogPopup(data);
        })
        .catch(error => console.error('Error fetching log data:', error));
}

function showLogPopup(logData) {
    // Create the popup container
    let popup = document.createElement("div");
    popup.id = "logPopup";
    popup.classList.add("popup-container");
    
    // Add title
    let title = document.createElement("h3");
    title.innerText = "Game Log";
    popup.appendChild(title);
    
    // Add log content
    let content = document.createElement("div");
    content.innerHTML = logData;
    popup.appendChild(content);
    
    // Add a close button
    let closeButton = document.createElement("button");
    closeButton.innerText = "Close";
    closeButton.classList.add("popup-close-button");
    closeButton.addEventListener("click", function () {
        document.body.removeChild(popup);
    });
    
    popup.appendChild(closeButton);
    
    // Append to body
    document.body.appendChild(popup);
}
