package math4u2.util.io.file.xml;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DOMWriter {
    public DOMWriter() {
        super();
    }

    /**
     * Return a string containing this node serialized as XML.
     */
    public static String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        serializeAsXML(node, sw);
        return sw.toString();
    }

    /**
     * Serialize this node into the writer as XML.
     */
    public static void serializeAsXML(Node node, Writer writer) {
    	PrintWriter pw = new PrintWriter(writer);
        print(node, pw);
        pw.flush();
        pw.close();
    }

    private static void print(Node node, PrintWriter out) {
        if (node == null) {
            return;
        }
        boolean hasChildren = false;
        int type = node.getNodeType();
        switch (type) {
        case Node.DOCUMENT_NODE: {
            out.println("<?xml version=\"1.0\"?>");
            NodeList children = node.getChildNodes();
            if (children != null) {
                int numChildren = children.getLength();
                for (int i = 0; i < numChildren; i++) {
                    print(children.item(i), out);
                }
            }
            break;
        }

        case Node.ELEMENT_NODE: {
            out.print('<' + node.getNodeName());
            NamedNodeMap attrs = node.getAttributes();
            int len = (attrs != null) ? attrs.getLength() : 0;
            for (int i = 0; i < len; i++) {
                Attr attr = (Attr) attrs.item(i);
                out.print(' ' + attr.getNodeName() + "=\""
                        + normalize(attr.getValue()) + '\"');
            }
            NodeList children = node.getChildNodes();
            if (children != null) {
                int numChildren = children.getLength();
                hasChildren = (numChildren > 0);
                if (hasChildren) {
                    out.print('>');
                }
                for (int i = 0; i < numChildren; i++) {
                    print(children.item(i), out);
                }
            } else {
                hasChildren = false;
            }
            if (!hasChildren) {
                out.print("/>");
            }
            break;
        }
        case Node.ENTITY_REFERENCE_NODE: {
            out.print('&');
            out.print(node.getNodeName());
            out.print(';');
            break;
        }
        case Node.CDATA_SECTION_NODE: {
            out.print("<![CDATA[");
            out.print(node.getNodeValue());
            out.print("]]>");
            break;
        }
        case Node.TEXT_NODE: {
            out.print(normalize(node.getNodeValue()));
            break;
        }
        case Node.COMMENT_NODE: {
            out.print("<!--");
            out.print(node.getNodeValue());
            out.print("-->");
            break;
        }
        case Node.PROCESSING_INSTRUCTION_NODE: {
            out.print("<?");
            out.print(node.getNodeName());
            String data = node.getNodeValue();
            if (data != null && data.length() > 0) {
                out.print(' ');
                out.print(data);
            }
            out.println("?>");
            break;
        }
        }
        if (type == Node.ELEMENT_NODE && hasChildren == true) {
            out.print("</");
            out.print(node.getNodeName());
            out.print('>');
            hasChildren = false;
        }
    }

    private static String normalize(String s) {
        StringBuffer str = new StringBuffer();
        int len = (s != null) ? s.length() : 0;
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            switch (ch) {
            case '<': {
                str.append("<");
                break;
            }
            case '>': {
                str.append(">");
                break;
            }
            case '&': {
                str.append("&");
                break;
            }

            case '\"': {
                str.append("\"");
                break;
            }
            case '\n': {
                if (i > 0) {
                    char lastChar = str.charAt(str.length() - 1);
                    if (lastChar != '\r') {
                        str.append('\n');
                    } else {
                        str.append('\n');
                    }
                } else {
                    str.append('\n');
                }
                break;
            }
            default: {
                str.append(ch);
            }
            }
        }
        return (str.toString());
    }
}