package ldap;

public class TisconUser 
{
    public static String [] attributes  = {"sn", "cn", "mail", "telephoneNumber","title" ,"objectClass"};
    public static String [] objectClasses  = {"person","organizationalPerson","inetOrgPerson"};
    
    public TisconUser() {
    }
}

/*
 * Local variables:
 * c-basic-offset: 4
 * indent-tabs-mode: nil
 * compile-command: "ant -emacs -find build.xml"
 * End:
 */
