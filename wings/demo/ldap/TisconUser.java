package ldap;

public class TisconUser 
{
    public static String [] attributes  = {"sn", "cn", "mail", "telephoneNumber","title" ,"objectClass"};
    public static String [] objectClasses  = {"person","organizationalPerson","inetOrgPerson"};
    
    public TisconUser() {
    }
}
