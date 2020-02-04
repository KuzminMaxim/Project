function selectTypeOfEvent() {
    var typeOfEvent = selectType.event.options[selectType.event.selectedIndex];
    var elementOfHTML1;
    var elementOfHTML2;
    var elementOfHTML3;
    var elementOfHTML4;

    if (typeOfEvent.value === 'eventsWhereCreatorHidden'){
        elementOfHTML1 = document.querySelectorAll('.eventsWhereCreatorHidden');
        elementOfHTML2 = document.querySelectorAll('.eventsWhereParticipantNotHidden');
        elementOfHTML3 = document.querySelectorAll('.cancelledChatsNotHidden');
        elementOfHTML4 = document.querySelectorAll('.completedEventsNotHidden');

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
        if (elementOfHTML4.length !== 0){
            elementOfHTML4.item(0).classList.remove('completedEventsNotHidden');
            elementOfHTML4.item(0).classList.add('completedEventsHidden');
        }

    }
    if (typeOfEvent.value === 'eventsWhereParticipantHidden'){
        elementOfHTML1 = document.querySelectorAll('.eventsWhereCreatorNotHidden');
        elementOfHTML2 = document.querySelectorAll('.eventsWhereParticipantHidden');
        elementOfHTML3 = document.querySelectorAll('.cancelledChatsNotHidden');
        elementOfHTML4 = document.querySelectorAll('.completedEventsNotHidden');

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
        if (elementOfHTML4.length !== 0){
            elementOfHTML4.item(0).classList.remove('completedEventsNotHidden');
            elementOfHTML4.item(0).classList.add('completedEventsHidden');
        }
    }
    if (typeOfEvent.value === 'cancelledChatsHidden'){
        elementOfHTML1 = document.querySelectorAll('.eventsWhereCreatorNotHidden');
        elementOfHTML2 = document.querySelectorAll('.eventsWhereParticipantNotHidden');
        elementOfHTML3 = document.querySelectorAll('.cancelledChatsHidden');
        elementOfHTML4 = document.querySelectorAll('.completedEventsNotHidden');

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
        if (elementOfHTML4.length !== 0){
            elementOfHTML4.item(0).classList.remove('completedEventsNotHidden');
            elementOfHTML4.item(0).classList.add('completedEventsHidden');
        }
    }
    if (typeOfEvent.value === 'completedEventsHidden'){
        elementOfHTML1 = document.querySelectorAll('.eventsWhereCreatorNotHidden');
        elementOfHTML2 = document.querySelectorAll('.eventsWhereParticipantNotHidden');
        elementOfHTML3 = document.querySelectorAll('.cancelledChatsNotHidden');
        elementOfHTML4 = document.querySelectorAll('.completedEventsHidden');

        if (elementOfHTML1.length !== 0){
            elementOfHTML1.item(0).classList.remove('eventsWhereCreatorNotHidden');
            elementOfHTML1.item(0).classList.add('eventsWhereCreatorHidden');
        }
        if (elementOfHTML2.length !== 0){
            elementOfHTML2.item(0).classList.remove('eventsWhereParticipantNotHidden');
            elementOfHTML2.item(0).classList.add('eventsWhereParticipantHidden');
        }
        if (elementOfHTML3.length !== 0){
            elementOfHTML3.item(0).classList.remove('cancelledChatsNotHidden');
            elementOfHTML3.item(0).classList.add('cancelledChatsHidden');
        }
        if (elementOfHTML4.length !== 0){
            elementOfHTML4.item(0).classList.remove('completedEventsHidden');
            elementOfHTML4.item(0).classList.add('completedEventsNotHidden');
        }
    }
}