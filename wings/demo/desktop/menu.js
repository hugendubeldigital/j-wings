function enter_item(item) {
   item.setAttribute('class', 'amenuitem');
}

function exit_item(item) {
   item.setAttribute('class', 'menuitem');
}

var active_popup = null;

function toggle_menu(popup) {
   var show = (popup.style.display == 'none');

   if (active_popup != null) {
      active_popup.parentNode.setAttribute('class', 'menu');
      active_popup.style.display = 'none'
      active_popup = null;
   }

   if (show == true) {
      popup.parentNode.setAttribute('class', 'amenu');
      popup.style.display = 'inline'
      active_popup = popup;
   }
}
