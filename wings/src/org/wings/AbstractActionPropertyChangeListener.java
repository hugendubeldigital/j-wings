package org.wings;

import java.beans.*;
import java.lang.ref.*;
import javax.swing.Action;
  
abstract class AbstractActionPropertyChangeListener
    implements PropertyChangeListener
{
    private static ReferenceQueue queue;

    private WeakReference target;
    private Action action;
    
    AbstractActionPropertyChangeListener(SComponent c, Action a) {
	super();
	setTarget(c);
	this.action = a;
    }

    public void setTarget(SComponent c) {
	if (queue == null) {
	    queue = new ReferenceQueue();
	}

	OwnedWeakReference r;
	while ((r = (OwnedWeakReference)queue.poll()) != null) {
	    AbstractActionPropertyChangeListener oldPCL = 
		(AbstractActionPropertyChangeListener)r.getOwner();
	    Action oldAction = oldPCL.getAction();
	    if (oldAction!=null)
		oldAction.removePropertyChangeListener(oldPCL);
	}
	this.target = new OwnedWeakReference(c, queue, this);
    }

    public SComponent getTarget() {
	return (SComponent)this.target.get();
    }

    public Action getAction() {
	return action;
    }

    private static class OwnedWeakReference extends WeakReference {
	private Object owner;

	OwnedWeakReference(Object target, ReferenceQueue queue, Object owner) {
	    super(target, queue);
	    this.owner = owner;
	}

	public Object getOwner() {
	    return owner;
	}
    }
}
