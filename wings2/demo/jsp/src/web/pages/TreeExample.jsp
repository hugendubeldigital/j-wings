<%@ page language="java"
import="org.wings.*,
        org.wings.jsp.WingsSession,
        jsp.HugeTreeModel,
        javax.swing.tree.DefaultTreeModel"
%>

<%
    // call the event dispatcher
    WingsSession.dispatchEvents(request, response);

    // every page needs its frame
    SFrame frame = WingsSession.getFrame(request, response, "TreeExample.jsp");
    STree tree;

    synchronized (session) {
        try {
            // before you can use the wingS API, you have to call the following method.
            // it will associate the wingS Session with the current thread
            WingsSession wingsSession = WingsSession.getSession(request, response);

            tree = (STree)wingsSession.getProperty("tree");
            if (tree == null) {
                tree = new STree();
                tree.setModel(new DefaultTreeModel(HugeTreeModel.ROOT_NODE));
                frame.getContentPane().add(tree, "tree");
                wingsSession.setProperty("tree", tree);
            }
        }
        finally {
            // release the thread association
            WingsSession.removeSession();
        }
    }
%>

<html>
<head>

<%
    // headers are required for proper operation
    WingsSession.writeHeaders(request, response, out, frame);
%>

<title>Test wingS JSP integration</title>
</head>
<body bgcolor="#f0f0f0">

<%
    // write a component
    WingsSession.writeComponent(request, response, out, tree);
%>

</body>
</html>
