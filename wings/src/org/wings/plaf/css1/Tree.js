
var TREE_NODES = new Array();


function Tree(treeId, eventId, expandIcon, collapseIcon, baseAddress, rootNodeId) {
    this.eventId = eventId;
    this.expandIcon = expandIcon;
    this.collapseIcon = collapseIcon;
    this.treeElement = document.getElementById(treeId);
    this.rootNodeId = rootNodeId;
    this.rootNode = null;
    this.baseAddress = baseAddress;
}

Tree.prototype.getRootNode = function() {
    if ( this.rootNode==null ) {
        this.rootNode = TREE_NODES[this.rootNodeId];
    } // end of if ()
    
    return this.rootNode;
}


function TreeNode(tree, nodeId, expanded, toggleSelectionEvent, toggleExpansionEvent) {
    this.tree = tree;
    this.nodeElement = document.getElementById(nodeId);
    this.icon = document.getElementById(nodeId + "_icon");
    this.originalExpanded = expanded;
    this.expanded = expanded;
    this.children = null;
    this.toggleSelectionEvent = toggleSelectionEvent;
    this.toggleExpansionEvent = toggleExpansionEvent;

    TREE_NODES[nodeId] = this;
}

TreeNode.prototype.addChild = function(child) {
    if ( this.children==null ) {
        this.children = new Array();
    }

    this.children[this.children.length] = child;
}

TreeNode.prototype.updateIcon = function() {
    this.icon.src = this.expanded ? this.tree.collapseIcon : this.tree.expandIcon;
}

TreeNode.prototype.getNode = function(nodeId) {
    return TREE_NODES[nodeId];
}

TreeNode.prototype.collapse = function() {
    this.expanded = false;
    this.updateDisplayRecursive(false);
    this.updateIcon();
}

TreeNode.prototype.expand = function() {
    this.expanded = true;
    this.updateDisplayRecursive(true);
    this.updateIcon();
}

TreeNode.prototype.updateDisplayRecursive = function(visible) {
    var display = visible ? "inline" : "none";

    if ( this.children!=null ) {
        for ( var i=0; i<this.children.length; i++ ) {
            this.children[i].nodeElement.style.display = display;
            this.children[i].updateDisplayRecursive(visible && this.children[i].expanded);
        }
    }
}

TreeNode.prototype.toggleExpansion2 = function() {
    if ( this.expanded ) {
        this.collapse();
    } else {
        this.expand();
    }
}

TreeNode.prototype.getRootNode = function() {
    return this.tree.rootNode;
}

TreeNode.prototype.appendToggleExpansionEvents = function(address) {
    if ( this.children!=null ) {
        if ( this.expanded!=this.originalExpanded ) {
            address += "&" + this.tree.eventId + "=" + this.toggleExpansionEvent;
        } // end of if ()
    
        for ( var i=0; i<this.children.length; i++ ) {
            address = this.children[i].appendToggleExpansionEvents(address);
        }
    }

    return address;
}

TreeNode.prototype.toggleSelection2 = function() {
    var address = this.tree.baseAddress;

    address += "?" + this.tree.eventId + "=" + this.toggleSelectionEvent;

    address = this.tree.getRootNode().appendToggleExpansionEvents(address);

    document.location.href = address;
}

TreeNode.prototype.toggleSelection = function(nodeId) {
    var node = TreeNode.prototype.getNode(nodeId);

    node.toggleSelection2()
}

TreeNode.prototype.toggleExpansion = function(nodeId) {
    var node = TreeNode.prototype.getNode(nodeId);

    node.toggleExpansion2()
}

