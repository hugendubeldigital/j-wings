package org.wings.template;

import bsh.Interpreter;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.wings.SComponent;
import org.wings.session.SessionManager;

/**
 * DefaultPropertyManager.java
 *
 *
 * Created: Tue Aug  6 16:43:03 2002
 *
 * @author (c) mercatis information systems gmbh, 1999-2002
 * @author <a href="mailto:armin.haaf@mercatis.de">Armin Haaf</a>
 * @version $Revision$
 */
public class DefaultPropertyManager implements PropertyManager {
    
    static final Class[] classes = { SComponent.class };

    public final HashMap propertyValueConverters = new HashMap();

    public static final DefaultPropertyValueConverter 
	DEFAULT_PROPERTY_VALUE_CONVERTER = new DefaultPropertyValueConverter();

    /**
     * 
     */
    public DefaultPropertyManager() {
    }

    protected Interpreter createInterpreter() {
        return new Interpreter();
    }

    public void setProperty(SComponent comp, String name, String value) {
	if ( "SCRIPT".equals(name) ) {
	    Interpreter interpreter = createInterpreter();

	    try {
		System.out.println("eval script " + value);

		interpreter.set("component", comp);
		interpreter.set("session", SessionManager.getSession());

		interpreter.eval(value);
	    } catch ( Exception ex ) {
		ex.printStackTrace();
		// ignore it, maybe log it
	    } // end of try-catch
	    
            // reset interpreter
	    
	} // end of if ()
	

	Method[] methods = comp.getClass().getMethods();

	for (int i=0; i<methods.length; i++) {
	    Method method = methods[i];
	    
	    if ( method.getName().startsWith("set") &&
		 name.equals(method.getName().substring(3).toUpperCase()) &&
		 method.getParameterTypes().length==1 ) {

		Class paramType = method.getParameterTypes()[0];

		PropertyValueConverter valueConverter = getValueConverter(paramType);
		
		if ( valueConverter!=null ) {
		    try {
			//System.out.println("invoke " + method);
			method.invoke(comp, 
				      new Object[] {valueConverter.convertPropertyValue(value, paramType)});
			return;
		    } catch ( Exception ex ) {
			// ignore it, maybe log it...
		    } // end of try-catch
		    
		} // end of if ()
	    } // end of if ()
	} // end of for (int i=0; i<; i++)
    }

    public void addPropertyValueConverter(PropertyValueConverter valueConverter, 
					  Class clazz) {
	
	propertyValueConverters.put(clazz, valueConverter);
    }

    protected PropertyValueConverter getValueConverter(Class clazz) {
	if ( clazz==null ) {
	    return DEFAULT_PROPERTY_VALUE_CONVERTER;
	} // end of if ()
	
	if ( propertyValueConverters.containsKey(clazz) ) {
	    return (PropertyValueConverter)propertyValueConverters.get(clazz);
	} // end of if ()
	
	return getValueConverter(clazz.getSuperclass());
    }

    public Class[] getSupportedClasses() {
        return classes;
    }

}// DefaultPropertyManager

/*
   $Log$
   Revision 1.2  2002/09/04 14:26:35  ahaaf
   o SComponent now have a EventListenerList (not allocated by default)
   o every component which manages listeners uses now the SComponent EventListener

   Revision 1.1  2002/08/06 16:45:55  ahaaf
   add DefaultPropertyManager using reflection and bean shell scripting support

*/
