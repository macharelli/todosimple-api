const url = "http://localhost:8080/tasks/user/1";

async function getAPI(url) {
    const response = await fetch(url, { method: "GET" });
    const data = await response.json();
    console.log(data);
    return data;
}

function hideLoader() {
    document.getElementById("loading").style.display = "none";
}

function show(tasks) {
    let tab = `<thead>
            <th scope="col">#</th>
            <th scope="col">Description</th>
            <th scope="col">Username</th>
            <th scope="col">User id</th>
         </thead>`;

    for (let task of tasks) {
        tab += `
            <tr>
                <td scope="row">${task.id}</td>
                <td>${task.description}</td>
                <td>${task.user.username}</td>
                <td>${task.user.id}</td>
            </tr>
        `;
    }

    document.getElementById("tasks").innerHTML = tab;
    hideLoader();
}

getAPI(url).then(show);