var dom = document.getElementById?1:0;
var ns4 = (document.layers && !dom)?1:0;
var ns6 = (dom && !document.all)?1:0;
var ie5 = (dom && document.all)?1:0;
var konqueror = checkUserAgent('konqueror')?1:0;

function checkUserAgent(string) {
	return navigator.userAgent.toLowerCase().indexOf(string) + 1;
}

function concatArrays(array1, array2) {
    var result = new Array(array1.length+array2.length);
    var index = 0;
    for ( var i=0; i<array1.length; i++ ) {
        result[index++]=array1[i];
    }
    for ( var i=0; i<array2.length; i++ ) {
        result[index++]=array2[i];
    }

    return result;
}

var MENUS = new Array();

function Menu(parent, hookElement, element) {
    this.parent = parent;
    this.hookElement = hookElement;
    this.element = element;
    this.direction = (parent == null) ? 'vertical' : 'horizontal';
    this.depth = 1;

    var node = this;
	while (node.parent != null) {
	    node = node.parent;
	    this.depth = this.depth + 1;
    }
}

Menu.prototype.hideAll = function() {
    for (var id in MENUS) {
        var menu = MENUS[id];
        menu.element.style.display = 'none';
    }
}

Menu.prototype.printAll = function() {
    var string = "items: ";
    for (var id in MENUS) {
        menuItem = MENUS[id];
        string = string + menuItem.hookElement.id + " ";
    }
    alert(string);
}

Menu.prototype.toggle = function(parentId, hookId, menuId) {
    var menuElement = document.getElementById(menuId);

    if (menuElement.style.display == 'none') {
        var parent = MENUS[parentId];

        var menu = MENUS[menuId];
        if (menu == null) {
            var hookElement = document.getElementById(hookId);
            menu = new Menu(parent, hookElement, menuElement);
            MENUS[menuId] = menu;
        }

        menu.show();
    }
    else {
        var menu = MENUS[menuId];
        menu.hide();
    }

    this.toggleFormElements();
}

Menu.prototype.show = function() {
    this.applyPosition();
    this.element.style.zIndex = this.depth;
    this.element.style.display = 'inline';

    for (var id in MENUS) {
        var menuItem = MENUS[id];
        if (!this.isParent(menuItem))
            menuItem.element.style.display = 'none';
    }
}

Menu.prototype.hide = function() {
    for (var id in MENUS) {
        menuItem = MENUS[id];
        if (this.isChild(menuItem))
            menuItem.element.style.display = 'none';
    }
}

Menu.prototype.isParent = function(target) {
    if (target == this)
        return true;

    var object = this;
	while (object.parent != null) {
	    object = object.parent;
        if (target == object)
            return true;
	}

	return false;
}

Menu.prototype.isChild = function(target) {
    if (target == this)
        return true;

	while (target.parent != null) {
	    target = target.parent;
        if (target == this)
            return true;
	}

	return false;
}

Menu.prototype.applyPosition = function() {
    var bounds = new Bounds(this.hookElement);
    if (this.direction == 'vertical') {
        this.element.style.top = (bounds.y + bounds.height + 1) + "px";
        this.element.style.left = (bounds.x + 1) + "px";
        if ( ie5 ) {
	    this.element.posX = bounds.x + 1;
	    this.element.posY = bounds.y + bounds.height + 1;
        }
    }
    else if (this.direction == 'horizontal') {
        this.element.style.top = (bounds.y + 1) + "px";
        this.element.style.left = (bounds.x + bounds.width + 1) + "px";
        if ( ie5 ) {
	    this.element.posX = bounds.x + bounds.width + 1;
	    this.element.posY = bounds.y + 1;
        }
    }
}

Menu.prototype.toggleFormElements = function() {
    var menuBounds = new Array();
    var i = 0;
    for (var id in MENUS) {
        menuItem = MENUS[id];
        if (menuItem.element.style.display != 'none')
            menuBounds[i++] = new Bounds(menuItem.element);
    }
    toggleFormElements(menuBounds);
}

function toggleFormElements(elementBounds) {

//    var string = "";

    var selects = document.getElementsByTagName('select');

    if ( konqueror ) {
        // konqueror needs to hide all form elements
        var inputs = document.getElementsByTagName('input');
        selects = concatArrays(selects, inputs);
        var textAreas = document.getElementsByTagName('textarea');
        selects = concatArrays(selects, textAreas);
    }

    for ( var i=0; i<selects.length; i++ ) {
	var select = selects[i];
        var selectBounds = new Bounds(select);

//	var selectBoundsString = selectBounds.x + ":" + 
//                                 selectBounds.y + ":" + selectBounds.width + ":" + 
//                                 selectBounds.height;

        var hit = false;
        for (var b = 0; b < elementBounds.length; ++b) {
            var bounds = elementBounds[b];
            var elemBoundsString = bounds.x + ":" + bounds.y + ":" + bounds.width + ":" + bounds.height;

            if (selectBounds.intersect(bounds)) {
                hit = true;
//                string = string + elemBoundsString + " intersects " + selectBoundsString + " \n";		
  		break;
            }
//            else {
//                string = string + elemBoundsString + " not intersects " + selectBoundsString + " \n";		
//            }
        }
        setVisible(select, hit);
    }
//    alert(string);
}

function setVisible(element, visible) {
    if (visible)
        element.style.visibility = 'hidden';
    else
        element.style.visibility = 'visible';
}

function isVisible(element) {
    return element.style.visibility == 'visible';
}


function Bounds(object) {
    this.x = object.posX ? object.posX : 0;
    this.y = object.posY ? object.posY : 0;
    this.width = object.offsetWidth;
    this.height = object.offsetHeight;

    // only calc if not a fixed position
    if ( !object.posX ) {
        while (object.offsetParent != null) {
            this.x += object.offsetLeft;
            this.y += object.offsetTop;
            object = object.offsetParent;
        }
    }
}

Bounds.prototype.intersect = function(other) {
    x1 = Math.max(this.x, other.x);
    y1 = Math.max(this.y, other.y);
    x2 = Math.min(this.x + this.width, other.x + other.width);
    y2 = Math.min(this.y + this.height, other.y + other.height);
    if (x1 >= x2 || y1 >= y2)
        return false;
    else
        return true;
}

function setStyleClass(item, newClass) {
    item.style.visibility = 'hidden';
    item.className = newClass;
    item.style.visibility = 'visible';
}
