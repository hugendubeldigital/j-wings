/*
 * $Id$
 * (c) Copyright 2000 wingS development team.
 *
 * This file is part of wingS (http://wings.mercatis.de).
 *
 * wingS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 *
 * Please see COPYING for the complete licence.
 * @author <a href="mailto:andre@lison.de">Andre Lison</a>
 */

function WindowManager() {
    this.count=0;
    this.windows = new Array();
    this.windowssize = new Array();
    this.zindex = 1;
};

WindowManager.prototype.addWindow=function(win) {
    win.setZIndex(this.zindex++);
    this.windows[this.count++] = win;
    this.windowssize[win.id] = win.getWidth()+"x"+win.getHeight();
};

/**
  * Brings a window to top of the window stack
  */
WindowManager.prototype.toTop=function(win) {
    var max = 0;
    var l = this.windows.length;
    var tmpwin = new Array(l);
    
    // get maximum z-index
    for (var i=0;i<l;i++) {
        var w = this.windows[i];
        /*
        if (!w)
            break;
        */
        max = (w.getZIndex() > max)?w.getZIndex():max;
        tmpwin[w.getZIndex()] = w;
    }
    
    // already on top ?
    if (win.getZIndex() == max ) return;
    
    // delete active window from tmpwin
    tmpwin[win.getZIndex()] = null;
    
    // put active window on top
    win.setZIndex(max);
    
    while(tmpwin[max]) {
        tmpwin[max].setZIndex(max-1);
        max--;
    }
};

/**
  * Save changed window properties to cookie
  */
WindowManager.prototype.propertyChange=function(win) {
    
    var expiry = new Date(today.getTime() + 15 * 60 * 1000); // plus 15 minutes
    expiry = "; expires=" + expiry.toGMTString();

    // browser
    setCookie("wings.browser.size", 
        ""+
        DynAPI.document.getWidth()+
        "x"+
        DynAPI.document.getHeight());

    // the sub-window
    var s = win.id.split("-");
    var unifiedid = s[0];
    var nameprefix = s[1];
    setCookie("wings."+unifiedid + ".loc", 
        escape(nameprefix+".il"+win.getPageX()+"x"+win.getPageY()));

    var size = win.getWidth()+"x"+win.getHeight();
    if (this.windowssize[win.id] != size)
        setCookie("wings."+unifiedid + ".size", escape(nameprefix+".is"+size));
};

function DragWindow(windowname, wm) {
  this.browser = new Browser();
  var windowlayer = DynAPI.document.all[windowname];
  if (!windowlayer) return;
  
  wm.addWindow(windowlayer);
  
  var mListener=new EventListener(windowlayer);
  mListener.layer = windowlayer;
  mListener.zindex = windowlayer.getZIndex();
  mListener.wm = wm;
  
  mListener.onmouseover=function(e) {
    this.layer.setBgColor("yellow");
    this.wm.toTop(this.layer);
  }

  if (this.browser.b == "ns") {
    mListener.startx = 0;
    mListener.starty = 0;
    mListener.drag = false;

    mListener.onmousemove=function(e) {
        if (this.drag) {
            this.layer.moveTo(e.pageX - this.startx, e.pageY - this.starty);
            wm.propertyChange(this.layer);
        }
    }
    
    mListener.onmousedown=function(e) {
        if (e.y <= 20) {
            this.startx = e.x;
            this.starty = e.y;
            this.drag = true;
        }
        else
            this.drag = false;
    }

    mListener.onmouseup=function(e) {
        this.drag = false;
        wm.propertyChange(this.layer);
    }

	mListener.onmouseout=function(e) {
        this.drag = false;
        this.layer.setBgColor("#c0c0c0");
	}

  }
  else {
    mListener.onmouseout=function(e) {
        this.layer.setBgColor("#c0c0c0");
        wm.propertyChange(this.layer);
    };
  }

  windowlayer.addEventListener (mListener);
  if (this.browser.b != "ns") {
      DragEvent.setDragBoundary(windowlayer);
      DragEvent.enableDragEvents(windowlayer);
  }

};
