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
 */

package org.wings;

/**
 * Utility class to support ClickableRenderComponents. Since Renderables
 * are rendered sequentially within one thread, it is perfectly sufficient
 * to have once instance of a ThreadLocal variable that maintains the
 * current state of the requestURL and targetURL in a Stack.
 * This is what this utility
 * class provides. Just call the appropriate methods from your implementation
 * of the ClickableRenderComponent.
 *
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision$
 */
public class ClickableRenderUtil
{   
    /**
     * 10 should be sufficient, so that we never need resizing. Usually
     * this will not go over 2
     */
    private final static int INITIAL_STACK_DEPTH = 10;

    /*
     * state for the ClickableRenderComponent.
     */
    private final static ThreadLocal eventURLStack    = new ThreadLocal() {
	    protected synchronized Object initialValue() {
		return new Stack( INITIAL_STACK_DEPTH );
	    }
	};

    /**
     * reset the internal stacks. This should be done everytime a complete
     * frame is rendered (maybe in some finally { }) to make sure the internal
     * stacks do not fill up.
     */
    public static void reset() {
	((Stack) eventURLStack.get()).clear();
    }

    /**
     * Set the request URL. A non-null value pushes the value onto the
     * stack, while a null value pops the topmost element. So make sure,
     * that you are not accidently pushing 'null's on the stack.
     */
    public static void pushEventURL(RequestURL url, String target) {
	Stack s = (Stack) eventURLStack.get();
        URLTargetTuple tuple = new URLTargetTuple(url, target);
        s.push(tuple);
    }
    
    public static void popEventURL() {
	Stack s = (Stack) eventURLStack.get();
        s.pop();
    }

    /**
     * returns the topmost request URL on the stack.
     */
    public static RequestURL getEventURL() {
	Stack s = (Stack) eventURLStack.get();
        URLTargetTuple tuple = (URLTargetTuple) s.peek();
	return (tuple != null) ? tuple.url : null;
    }

    /**
     * returns the topmost request URL on the stack.
     */
    public static String getEventTarget() {
	Stack s = (Stack) eventURLStack.get();
        URLTargetTuple tuple = (URLTargetTuple) s.peek();
	return (tuple != null) ? tuple.target : null;
    }

    private final static class URLTargetTuple {
        public RequestURL url;
        public String     target;

        URLTargetTuple(RequestURL url, String target) {
            this.url = url;
            this.target = target;
        }
    }

    /**
     * Non synchronized, fast stack. This stack never shrinks its internal
     * data structure. If you get Exceptions within this class, it is likely,
     * that there is a programming error in one of the plafs. More precisely,
     * that the plaf does a setEventURL(something), but fails to reset it
     * to 'null': setEventURL(null). Or that 'something' is null.
     */
    private final static class Stack {
	private Object[] elements;
	private int pos;

	public Stack(int initialElements) {
	    pos = -1;
	    elements = new Object [ initialElements ];
	}
	
	public void clear() {
	    pos = -1;
	}

	public void push(Object o) {
	    ++pos;
	    if (pos == elements.length) resize();
	    /*
	     * debugging hint: if you get an IndexOutOfBoundException here, 
             * maybe the last pop() operation was bogus in the first place ?
             * It is not checked there for speed reasons.
	     */
	    elements[pos] = o;
	}

	public Object peek() {
	    if (pos == -1) return null;
	    return elements[pos];
	}

	/* we don't need the toplevel element, so don't bother returning it.*/
	public void pop() {
	    --pos;
	}

	/**
	 * we only increase the size.
	 */
	private void resize() {
	    Object[] newArray = new Object [ elements.length * 2 ];
	    System.arraycopy(elements, 0, newArray, 0, elements.length);
	    elements = newArray;
	}
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
