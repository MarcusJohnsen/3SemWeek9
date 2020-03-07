fetchFunction("http://localhost:8080/jpareststarter/api/person/all", insertAllUsersInTable);

function fetchFunction(fetchUrl, callback) {
    fetch(fetchUrl)
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            callback(data);
        });
};

function insertAllUsersInTable(dataArray) {
    let printString = createTableFromArray(dataArray);
    document.getElementById("div").innerHTML = printString;
};

function createTableFromArray(array) {
    let tableHead = "<tr><th>ID</th>" + "<th>First Name</th>" + "<th>Last Name</th>" + "<th>Phone Number</th>";
    let htmlRows = "";
    console.log(array.all);

    array.all.forEach(element => {
        let temp = "<tr>" +
            "<td>" + element.id + "</td>" +
            "<td>" + element.firstName + "</td>" +
            "<td>" + element.lastName + "</td>" +
            "<td>" + element.phone + "</td>" +
            "<tr>"
        htmlRows += temp;
    });

    return "<table border='1'>" + tableHead + htmlRows + "</table>";
};

function reloadUsers(dataArray) {
    let printString = createTableFromArray(dataArray);
    document.getElementById("div").innerHTML = printString;
}

document.getElementById("reloadButton").addEventListener('click', (event) => {
    fetchFunction("http://localhost:8080/jpareststarter/api/person/all", insertAllUsersInTable);
});

document.getElementById("addButton").addEventListener('click', (event) => {
    addPerson();
});

function addPerson() {
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let phoneNumber = document.getElementById("phoneNumber").value;

    let options = {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            firstName: firstName,
            lastName: lastName,
            phone: phoneNumber
        })
    }
    fetch("http://localhost:8080/jpareststarter/api/person/add", options);
}