'use strict';

document.getElementById('chatId').style.display='none';
document.getElementById('username').style.display='none';

var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('#connecting');



var stompClient = null;
var username = null;
var chatName = null;
var chatId = null;

function connect() {
    username = document.querySelector('#username').innerText.trim();
    chatName = document.getElementById('chatName').innerText;
    chatId = document.getElementById('chatId').innerText;

    var socket = new SockJS('/ws/'+chatId);

    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnected, onError);
}

connect();

function onConnected() {
    stompClient.subscribe('/topic/'+chatId+'Room', onMessageReceived);
    stompClient.send("/app/"+ chatId +".addUser",
        {},
        JSON.stringify({sender: username, chatName:chatName, chatId:chatId, usersOnline: [] , type: 'JOIN'})
    );

    connectingElement.classList.add('hidden');

}


function onError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            chatName: chatName,
            usersOnline: [],
            chatId: chatId,
            currentDate: ''
        };
        stompClient.send("/app/"+ chatId +".sendMessage", {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);


    var messageElement = document.createElement('li');

    if(message.type === 'JOIN') {
        message.content = message.usersOnline;
        for (let i = 0; i < message.content.length ; i++){
            var  namesOfParticipant = document.querySelectorAll('#nameOfParticipant');
            for (let j = 0; j < namesOfParticipant.length; j++){
                if (message.content[i] === namesOfParticipant.item(j).innerHTML){
                    namesOfParticipant.item(j).classList.remove('nameOfParticipant');
                    namesOfParticipant.item(j).classList.add('nicknameOfParticipantOnline');
                }
            }
        }

    } else if (message.type === 'LEAVE') {
        var  nicknameOfParticipantOnline = document.querySelectorAll('.nicknameOfParticipantOnline');

        var arr = message.usersOnline;
        let countOfWindow = 0;
        for (let i = 0; i < arr.length; ++i)
        {
            if (arr[i] === message.sender){
                countOfWindow++
            } else {countOfWindow = 0}
        }

        for (let j = 0; j < nicknameOfParticipantOnline.length; j++){
            if (message.sender === nicknameOfParticipantOnline.item(j).innerHTML){
                if (countOfWindow === 0){
                    nicknameOfParticipantOnline.item(j).classList.remove('nicknameOfParticipantOnline');
                    nicknameOfParticipantOnline.item(j).classList.add('nameOfParticipant');
                }
            }
        }
    } else {
        messageElement.classList.add('chat-message');
        var usernameElement = document.createElement('strong');
        usernameElement.classList.add('nickname');
        var usernameText = document.createTextNode(message.sender);

        var now = new Date();
        var currentDate = document.createElement('i');
        var currentDateText = document.createTextNode(' ' + now.getFullYear()+'-' + (now.getMonth()+1)+'-' + now.getDate() + ' '
            + now.getHours() + ':' + now.getMinutes() + ' ');
        currentDate.appendChild(currentDateText);
        currentDate.classList.add('currentDate');

        usernameElement.appendChild(usernameText);
        messageElement.appendChild(usernameElement);
        messageElement.appendChild(currentDate);
    }

    if (message.type === "CHAT"){
        var textElement = document.createElement('span');
        var messageText = document.createTextNode(message.content);

        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);

        messageArea.appendChild(messageElement);
    }
    messageArea.scrollTop = messageArea.scrollHeight;
}

messageForm.addEventListener('submit', sendMessage, true);