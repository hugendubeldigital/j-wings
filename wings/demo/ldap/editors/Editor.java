package ldap.editors;

import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;

import org.wings.*;

public interface Editor
{
    SComponent createComponent(Attributes attributes);
    void setValue(SComponent component, Attribute attribute)
	throws NamingException;
    Attribute getValue(SComponent component, String id) throws NamingException;
}
