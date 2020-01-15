function getFilterColumn() {
    var HtmlElement1 = document.querySelectorAll('.tableHeaderHidden');
    var HtmlElement2 = document.querySelectorAll('.tableHeaderNotHidden');
    if (HtmlElement1.length !== 0){
        for (let i = 0; i < HtmlElement1.length; i++){
            HtmlElement1.item(i).classList.remove('tableHeaderHidden');
            HtmlElement1.item(i).classList.add('tableHeaderNotHidden');
        }
    }
    if (HtmlElement2.length !== 0){
        for (let i = 0; i < HtmlElement2.length; i++){
            HtmlElement2.item(i).classList.remove('tableHeaderNotHidden');
            HtmlElement2.item(i).classList.add('tableHeaderHidden');
        }
    }
}