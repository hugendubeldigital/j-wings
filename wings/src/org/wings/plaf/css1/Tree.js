
var TREES = new Array();
var TREE_NODES = new Array();


function Tree(expandIcon, collapseIcon, treeId) {
    this.expandIcon = expandIcon;
    this.collapseIcon = collapseIcon;
    this.treeElement = document.getElementById(treeId);

    TREES[treeId] = this;
}

function TreeNode(tree, nodeId, expanded) {
    this.tree = tree;
    this.nodeElement = document.getElementById(nodeId);
    this.icon = document.getElementById(nodeId + "_icon");
    this.expanded = expanded;
    this.children = null;

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


TreeNode.prototype.toggleExpansion = function(nodeId) {
    var node = TreeNode.prototype.getNode(nodeId);

    node.toggleExpansion2()
}

