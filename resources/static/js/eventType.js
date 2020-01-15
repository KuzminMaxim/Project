function selectTypeOfEvent() {
    var typeOfEvent = selectType.event.options[selectType.event.selectedIndex];
    var elementOfHTML1;
    var elementOfHTML2;
    var elementOfHTML3;

    if (typeOfEvent.value === 'eventsWhereCreatorHidden'){
        elementOfHTML1 = document.querySelectorAll('.eventsWhereCreatorHidden');
        elementOfHTML2 = document.querySelectorAll('.eventsWhereParticipantNotHidden');
        elementOfHTML3 = document.querySelectorAll('.cancelledChatsNotHidden');

        if (elementOfHTML1.length !== 0){
            elementOfHTML1.item(0).classList.remove('eventsWhereCreatorHidden');
            elementOfHTML1.item(0).classList.add('eventsWhereCreatorNotHidden');
        }
        if (elementOfHTML2.length !== 0){
            elementOfHTML2.item(0).classList.remove('eventsWhereParticipantNotHidden');
            elementOfHTML2.item(0).classList.add('eventsWhereParticipantHidden');
        }
        if (elementOfHTML3.length !== 0){
            elementOfHTML3.item(0).classList.remove('cancelledChatsNotHidden');
            elementOfHTML3.item(0).classList.add('cancelledChatsHidden');
        }

    }
    if (typeOfEvent.value === 'eventsWhereParticipantHidden'){
        elementOfHTML1 = document.querySelectorAll('.eventsWhereCreatorNotHidden');
        elementOfHTML2 = document.querySelectorAll('.eventsWhereParticipantHidden');
        elementOfHTML3 = document.querySelectorAll('.cancelledChatsNotHidden');

        if (elementOfHTML1.length !== 0){
            elementOfHTML1.item(0).classList.remove('eventsWhereCreatorNotHidden');
            elementOfHTML1.item(0).classList.add('eventsWhereCreatorHidden');
        }
        if (elementOfHTML2.length !== 0){
            elementOfHTML2.item(0).classList.remove('eventsWhereParticipantHidden');
            elementOfHTML2.item(0).classList.add('eventsWhereParticipantNotHidden');
        }
        if (elementOfHTML3.length !== 0){
            elementOfHTML3.item(0).classList.remove('cancelledChatsNotHidden');
            elementOfHTML3.item(0).classList.add('cancelledChatsHidden');
        }
    }
    if (typeOfEvent.value === 'cancelledChatsHidden'){
        elementOfHTML1 = document.querySelectorAll('.eventsWhereCreatorNotHidden');
        elementOfHTML2 = document.querySelectorAll('.eventsWhereParticipantNotHidden');
        elementOfHTML3 = document.querySelectorAll('.cancelledChatsHidden');

        if (elementOfHTML1.length !== 0){
            elementOfHTML1.item(0).classList.remove('eventsWhereCreatorNotHidden');
            elementOfHTML1.item(0).classList.add('eventsWhereCreatorHidden');
        }
        if (elementOfHTML2.length !== 0){
            elementOfHTML2.item(0).classList.remove('eventsWhereParticipantNotHidden');
            elementOfHTML2.item(0).classList.add('eventsWhereParticipantHidden');
        }
        if (elementOfHTML3.length !== 0){
            elementOfHTML3.item(0).classList.remove('cancelledChatsHidden');
            elementOfHTML3.item(0).classList.add('cancelledChatsNotHidden');
        }
    }
}