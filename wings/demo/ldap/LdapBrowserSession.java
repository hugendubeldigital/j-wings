package ldap;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.naming.*;
import javax.naming.directory.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.swing.*;
import javax.swing.event.*; 
import javax.swing.table.*;

import org.wings.*;
import org.wings.io.Device;
import org.wings.io.ServletDevice;
import org.wings.servlet.*;
import org.wings.session.*;
import org.wings.util.*;


public class LdapBrowserSession 
    extends SessionServlet
    implements SConstants
{
    private DirContext context;

    private String[] overviewAttributes;
    private String[] overviewLabels;
    private String[] detailviewAttributes;
    private String[] detailviewLabels;

    private String searchLabel;
    private String searchAttribute;

    private String server;
    private String basedn;
    private String binddn;
    private String password;

    private SDesktopPane desktop;
    private SInternalFrame searchFrame;
    private SInternalFrame detailFrame;

    private STextField searchTextField;
    private STable table;
    private SPanel detailPanel;

    private OverviewModel overviewModel;

    /** @link dependency 
     * @stereotype use*/
    /*#CellSelectionListener lnkCellSelectionListener;*/

    /** @link dependency 
     * @stereotype use*/
    /*#AttributesCellRenderer lnkAttributesCellRenderer;*/

    public LdapBrowserSession(Session session) {
	super(session);
        System.out.println("launching session for ldap browser");
    }

    public void postInit(ServletConfig config) {
	PropertyService properties = (PropertyService)getSession();
	StringTokenizer attributes, labels;
	int i;

	server = properties.getProperty("ldap.server.host");
	basedn = properties.getProperty("ldap.server.basedn");
	binddn = properties.getProperty("ldap.server.binddn");
	password = properties.getProperty("ldap.server.password");

	Hashtable env = new Hashtable();
	env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
	env.put(Context.PROVIDER_URL, "ldap://" + server);
	env.put(Context.SECURITY_PRINCIPAL, binddn);
	env.put(Context.SECURITY_CREDENTIALS, password);

	try {
	    context = new InitialDirContext(env);
	}
	catch(NamingException e) {
	    System.err.println(e.getMessage());
	    e.printStackTrace(System.err);
	}

	attributes = new StringTokenizer(properties.getProperty("overview.attributes"), ",");
	labels = new StringTokenizer(properties.getProperty("overview.labels"), ",");
	overviewAttributes = new String[attributes.countTokens()];
	overviewLabels = new String[labels.countTokens()];
	i = 0;
	while (attributes.hasMoreTokens()) {
	    overviewAttributes[i] = attributes.nextToken();
	    overviewLabels[i] = labels.nextToken();
	    i++;
	}

	attributes = new StringTokenizer(properties.getProperty("detailview.attributes"), ",");
	labels = new StringTokenizer(properties.getProperty("detailview.labels"), ",");
	detailviewAttributes = new String[attributes.countTokens()];
	detailviewLabels = new String[labels.countTokens()];
	i = 0;
	while (attributes.hasMoreTokens()) {
	    detailviewAttributes[i] = attributes.nextToken();
	    detailviewLabels[i] = labels.nextToken();
	    i++;
	}

	this.searchAttribute = properties.getProperty("search.attribute");
	this.searchLabel = properties.getProperty("search.label");

        initGUI();
    }

    void initGUI() {
	desktop = new SDesktopPane();
	getFrame().getContentPane().add(desktop);

	searchFrame = new SInternalFrame();
	desktop.add(searchFrame);

	detailFrame = new SInternalFrame();
	desktop.add(detailFrame);

	SForm searchForm = new SForm(new SFlowLayout());
	searchForm.setBorder(new SBevelBorder());

	SLabel label = new SLabel(searchLabel);
	searchTextField = new STextField();

	SButton submit = new SButton("submit");
	submit.addActionListener(new SearchAction());

	searchForm.add(label);
	searchForm.add(searchTextField);
	searchForm.add(submit);

	searchFrame.getContentPane().add(searchForm, "North");

	overviewModel = new OverviewModel();
	table = new STable(overviewModel);
	table.setBorderLines(new Insets(2,2,2,2));
	AttributesCellRenderer renderer = new AttributesCellRenderer();
	renderer.setSelectableColumns(new int[] { 0 });
	renderer.addCellSelectionListener(new CellSelectionListener() {
		public void cellSelected(CellSelectionEvent e) {
		    System.out.println("select: x=" + e.getXPosition() + ", y=" + e.getYPosition());
		    showDetails(overviewModel.get(e.getYPosition()));
		}
	    });
	table.setDefaultRenderer(Attribute.class, renderer);

	searchFrame.getContentPane().add(table);

	detailPanel = new SPanel(new SGridLayout(2));
	detailFrame.getContentPane().add(detailPanel, "Center");

	SButton backButton = new SButton("ok");
	backButton.addActionListener(new BackAction());
	detailFrame.getContentPane().add(backButton, "South");

	searchFrame.show();
    }

    protected void showDetails(Attributes attributes) {
	System.err.println("show detailFrame");
	detailPanel.removeAll();

	for (int i=0; i < detailviewAttributes.length; i++) {
	    try {
		Attribute attribute = attributes.get(detailviewAttributes[i]);
		detailPanel.add(new SLabel(detailviewLabels[i]));

		if (attribute.get().getClass().getName().equals("[B")) {
		    if (attribute.getID().equals("jpegPhoto")) {
			SImageIcon icon = new SImageIcon(new ImageIcon((byte[])attribute.get()));
			detailPanel.add(new SLabel(icon));
		    }
		    else if (attribute.getID().equals("userPassword")) {
			detailPanel.add(new SLabel("-"));
		    }
		}
		else {
		    StringBuffer buffer = new StringBuffer();

		    for (int i2=0; i2 < attribute.size(); i2++) {
			if (i2 > 0)
			    buffer.append(", ");
			buffer.append(attribute.get(i2).toString());
		    }

		    detailPanel.add(new SLabel(buffer.toString()));
		}
	    }
	    catch (NullPointerException e) {
		detailPanel.add(new SLabel("-"));
	    }
	    catch (NamingException e) {
		detailPanel.add(new SLabel(e.getMessage()));
	    }
	}

	detailFrame.show();
    }

    class SearchAction
	extends AbstractAction
    {
	public void actionPerformed(ActionEvent evt) {
	    String filter = "(" + searchAttribute + "=*" + searchTextField.getText() + "*)";
	    System.out.println("filter: " + filter);
	    overviewModel.setFilter(filter);
	}
    }

    class BackAction
	extends AbstractAction
    {
	public void actionPerformed(ActionEvent evt) {
	    searchFrame.show();
	}
    }


    class OverviewModel
	extends AbstractTableModel
    {
	private String filter = null;
	private List data = new LinkedList();

	public void setFilter(String filter) {
	    this.filter = filter;
	    populateModel();
	}

	protected void populateModel() {
	    try {
		data.clear();
		NamingEnumeration enum = context.search(basedn, filter, new SearchControls());
		while (enum.hasMore()) {
		    SearchResult searchResult = (SearchResult)enum.next();
		    data.add(searchResult.getAttributes());
		}
		enum.close();
	    }
	    catch (NamingException e) {
		System.err.println(e.getMessage());
		e.printStackTrace(System.err);
	    }
	}

	public int getRowCount() {
	    return data.size();
	}

	public int getColumnCount() {
	    return overviewAttributes.length;
	}

	public Class getColumnClass(int columnIndex) {
	    return Attribute.class;
	}

	public Object getValueAt(int row, int column) {
	    Attributes attributes = (Attributes)data.get(row);
	    return attributes.get(overviewAttributes[column]);
	}

	public Attributes get(int row) {
	    return (Attributes)data.get(row);
	}

	public String getColumnName(int column) {
	    return overviewLabels[column];
	}
    }

    public String getServletInfo() {
        return "LdapBrowser $Revision$";
    }
}
