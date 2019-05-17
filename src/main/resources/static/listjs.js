let api_url = 'http://localhost:8080/api';

let isUsersReady = false;
let isRolesReady = false;

let usersJsonFromServer;
let rolesJsonFromServer;




$(document).ready(function () {

    rebuildPage();

    createModalCallBacks();
    createNewUserCallBacks();
});


function rebuildPage() {
    loadData();

    makeListTab();
    makeNewUserTab();
}


function loadData() {
    console.log("start loading users");

    $.ajax({
        url: api_url + "/users",
        type: "GET",
        async: false,
        contentType: "application/json",
        dataType: 'json',
        success: function (result) {
            usersJsonFromServer = result;
            isUsersReady = true;
            console.log(result);
        }
    });

    console.log("start loading roles");

    $.ajax({
        url: api_url + "/roles",
        type: "GET",
        async: false,
        contentType: "application/json",
        dataType: 'json',
        success: function (result) {
            rolesJsonFromServer = result;
            isRolesReady = true;
            console.log(result);
        }
    });
}


function makeListTab() {

    if (isRolesReady && isUsersReady) {

        console.log("build list");

        $('#usertablebody').html('');

        $.each(usersJsonFromServer, function (i, user) {

            let tdROLES = $('<div/>');

            $.each(user.roles, function (k, role) {
                tdROLES.append($('<p/>').html(role.authority));
            });


            let buttonUserEdit = $('<button/>');
            buttonUserEdit.attr("class", "btn btn-primary");
            buttonUserEdit.attr("id", "buttonUserEdit" + user.id);
            buttonUserEdit.attr("data-target", "#usereditmodaljs");
            buttonUserEdit.attr("data-toggle", "modal");
            buttonUserEdit.attr("type", "button");
            buttonUserEdit.html("Edit");
            buttonUserEdit.data('user', user);


            let buttonUserDelete = $('<button/>');
            buttonUserDelete.attr("class", "btn btn-danger");
            buttonUserDelete.attr("type", "button");
            buttonUserDelete.html("Delete");
            buttonUserDelete.click(function () {
                deleteUser(user.id);
                rebuildPage();
            });


            var newTr = $('<tr/>');
            newTr.append($('<td/>').html(user.id));
            newTr.append($('<td/>').html(user.username));
            newTr.append($('<td/>').html(user.firstName));
            newTr.append($('<td/>').html(user.email));
            newTr.append($('<td/>').html(tdROLES));
            newTr.append($('<td/>').html(user.password));
            newTr.append($('<td/>').append(buttonUserEdit));
            newTr.append($('<td/>').append(buttonUserDelete));


            $('#usertablebody').append(newTr);
        });
    }
}


function makeNewUserTab() {
    if (isRolesReady) {

        console.log("build new user tab");


        $("#adduserjs").find('select[id="roles"]').html('');

        $.each(rolesJsonFromServer, function (i, role) {

            var newOption = $('<option/>');
            newOption.attr('name', role.id);
            newOption.attr('value', role.id);
            newOption.html(role.authority);

            $("#adduserjs").find('select[id="roles"]').append(newOption);
        });


    }
}


function createModalCallBacks() {
    $('#usereditmodaljs').on('show.bs.modal', function (event) {
        let button = $(event.relatedTarget); // Button that triggered the modal


        let modalheader = $(event.currentTarget).find('.modal-header');
        modalheader.find('.modal-title').text('Edit user ' + button.data('firstname'));


        let modalbody = $(event.currentTarget).find('.modal-body');
        let user = button.data('user');

        modalbody.find('form[id="userform"]').attr('action', '/api/users/' + user.id);
        modalbody.find('input[id="id"]').val(user.id);
        modalbody.find('input[id="username"]').val(user.username);
        modalbody.find('input[id="firstName"]').val(user.firstName);
        modalbody.find('input[id="email"]').val(user.email);
        modalbody.find('input[id="password"]').val(user.password);

        modalbody.find('select[id="roles"]').html('');

        $.each(rolesJsonFromServer, function (i, role) {

            let newOption = $('<option/>');
            newOption.attr('name', role.id);
            newOption.attr('value', role.id);
            newOption.html(role.authority);
            if (user.roles.filter(function (r) {
                return r.id === role.id;
            }).length > 0) {
                newOption.attr('selected', '');
            }

            modalbody.find('select[id="roles"]').append(newOption);
        });


        let modalfooter = $(event.currentTarget).find('.modal-footer');
        modalfooter.find('button[id="usereditbutton"]').attr('action', '/api/users/' + button.data('id'));
    });


    $("#buttonUserUpdate").click(function () {

        let jsonData = JSON.stringify(getData($('#userform')));

        // console.log(jsonData);
        updateUser(jsonData);

        $('#usereditmodaljs').modal('hide');
        rebuildPage();
    });
}


function createNewUserCallBacks() {
    $("#buttonUserAdd").click(function () {

        var data = getData($('#adduserjs'));
        data['id'] = "0";

        var jsonData = JSON.stringify(data);

        // console.log(jsonData);
        addUser(jsonData);
        $("#adduserjs").trigger("reset");
        alert("user " + data['username'] + " was added!");
        rebuildPage();
    });
}


function addUser(jsonToSend) {

    console.log(jsonToSend);

    $.ajax(
        {
            url: api_url + '/users',
            async: false,
            type: "POST",
            data: jsonToSend,
            contentType: "application/json",
            dataType: 'json'

        })
        .done(function () {
            console.log("success: finish add user");
        })
        .fail(function () {
            console.log("===ERROR===: finish add user");
        });

}


function updateUser(jsonToSend) {

    console.log(jsonToSend);

    $.ajax(
        {
            url: api_url + '/users/' + JSON.parse(jsonToSend).id,
            async: false,
            type: "PUT",
            data: jsonToSend,
            contentType: "application/json",
            dataType: 'json'

        })
        .done(function (msg) {
            console.log("success: finish saving user: " + msg);
        })
        .fail(function () {
            console.log("===ERROR===: finish saving user");
        });

}


function deleteUser(id) {
    $.ajax(
        {
            url: api_url + '/users/' + id,
            async: false,
            type: "DELETE",
        })
        .done(function (msg) {
            console.log("success: finish deleting user: " + msg);
        })
        .fail(function () {
            console.log("===ERROR===: finish deleting user");
        });
}


function getData(obj) {

    let user = {};
    user['id'] = obj.find('input[id="id"]').val();
    user['username'] = obj.find('input[id="username"]').val();
    user['firstName'] = obj.find('input[id="firstName"]').val();
    user['email'] = obj.find('input[id="email"]').val();
    user['password'] = obj.find('input[id="password"]').val();

    user['roles'] = [];

    obj.find('option:selected').each(function () {

        let role = {};

        role['id']= parseInt(this.value);
        role['authority']= this.text;

        user['roles'].push(role);
    });

    return user;
}