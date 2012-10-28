package math4u2.exercises.ui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.dnd.DropTarget;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import math4u2.exercises.EIssue;
import math4u2.exercises.Exercise;
import math4u2.exercises.Folder;
import math4u2.view.dragAndDrop.target.DropTargetOnFrameListener;
import math4u2.view.gui.Math4U2Win;

/**
 * Komponente zum Darstellen von Themen (<code>EIssue</code> s), Übungen (
 * <code>Exercise</code>s) und Ornder( <code>Folder</code> s)als Baum
 * 
 * @version 0.1
 * @author Erich Seifert / Michael Lichtenstern
 */
public class ETree extends JPanel implements TreeSelectionListener,
        TreeExpansionListener {

    public static final boolean EXPAND_TREE = true;
    public static final boolean COLLAPSE_TREE = false;

    private Set treeListeners = new HashSet();

    /**
     * Die Liste mit Themen (<code>EIssues</code>)
     */
    protected List issues;

    /**
     * Die Liste der Ordner (<code>Folder</code>)
     */
    protected List folders;

    /**
     * Die <code>JTree</code> -Komponente
     */
    protected JTree jTree;

    protected DefaultMutableTreeNode top = new DefaultMutableTreeNode("math4u2");

    protected JScrollPane scrollPane;

    protected Object actualIssueOrExercise;

    /**
     * Konstruktor der ein neues ETree-Objekt mit den übergebenen Themen
     * erzeugt.
     * 
     * @param issues
     *            Themen die dargestellt werden sollen
     */
    public ETree(List issues, List folders, boolean expandTree) {
        this.issues = issues;
        this.folders = folders;
        createNodes();
        jTree = new JTree(top);
        new DropTarget(jTree, new DropTargetOnFrameListener(
                Math4U2Win.getInstance()));
        // Wurzelknoten nicht einblenden
        jTree.setRootVisible(false);
        // Alle Knoten des Baums aufklappen
        if (expandTree)
            expandTree();
        // Erstes Element auswählen
        jTree.setSelectionRow(0);
        jTree.addTreeSelectionListener(this);
        jTree.addTreeExpansionListener(this);
        ETreeCellRenderer renderer = new ETreeCellRenderer();
        jTree.setCellRenderer(renderer);
        jTree.setRowHeight(23);
        setLayout(new BorderLayout());
        scrollPane = new JScrollPane(jTree);
        jTree.setBorder(new EmptyBorder(new Insets(10, 5, 0, 5)));
        add(scrollPane);
        this.expand();
        this.scrollTo();
    } //Konstruktor
    

    private void scrollTo() {
        this.makeTreeItemVisible(Math4U2Win.getInstance().actualTreeSelection,
                false);
    }

    /**
     * Oeffnet eine Ast des Baumes.
     * 
     * @param location
     *            Pfad
     */
    private void makeTreeItemVisible(String location, boolean show) {
        String[] path = (location + "/").split("/");
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) jTree.getModel()
                .getRoot();
        LinkedList ancestors = new LinkedList();
        ancestors.add(root);
        _RekMakeTreeItemVisible(path, 0, ancestors, show);
    }//makeTreeItemVisible

    private void _RekMakeTreeItemVisible(String[] path, int index,
            LinkedList ancestors, boolean show) {
        Enumeration enuma = ((DefaultMutableTreeNode) ancestors.getLast())
                .children();
        while (enuma.hasMoreElements()) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) enuma
                    .nextElement();
            if (path.length > 0) {
                if (child.toString().equals(path[index])) {
                    ancestors.add(child);
                    if (show) {
                        jTree.expandPath(new TreePath(ancestors.toArray()));
                    } else {
                        jTree.scrollPathToVisible(new TreePath(ancestors
                                .toArray()));
                        jTree
                                .setSelectionPath(new TreePath(ancestors
                                        .toArray()));
                    }
                    if (!(index == path.length - 1)) {
                        _RekMakeTreeItemVisible(path, ++index, ancestors, show);
                    }
                }//if
            }
        }//while enum
    }//_RekMakeTreeItemVisible

    /**
     * Oeffnet alle gespeicherten Knoten.
     */
    private synchronized void expand() {
        LinkedHashSet tmp = new LinkedHashSet(Math4U2Win.getInstance().actualTreePaths);
        Iterator foldersToShow = tmp.iterator();
        while (foldersToShow.hasNext()) {
            String location = (String) foldersToShow.next();
            this.makeTreeItemVisible(location, true);
        }
    }


    /**
     * Legt die Tehmen fest die angezeigt werden sollen.
     * 
     * @param issues
     *            Die neue Liste mit den Themen
     */
    public void setIssues(List issues) {
        this.issues = issues;
    }

    /**
     * Fügt ein neues Thema hinzu.
     * 
     * @param issue
     *            Das Thema, das hinzugefügt werden soll
     */
    public void addIssue(EIssue issue) {
        issues.add(issue);
        java.util.Collections.sort(issues);
    }

    /**
     * Legt die Ordner fest die angezeigt werden sollen.
     * 
     * @param folders
     *            Die neue Liste mit den Ordnern
     */
    public void setFolders(List folders) {
        this.folders = folders;
    } //setFolders

    /**
     * Fügt einen neuen Ordner hinzu.
     * 
     * @param folder
     *            Der Ordner, der hinzugefügt werden soll
     */
    public void addFolder(Folder folder) {
        folders.add(folder);
    } //addFolder

    /**
     * Erzeugt alle Knoten für den neuen Baum
     * 
     * @param top
     *            Oberster Wurzelknoten an den alle anderen Knoten angehängt
     *            werden sollen
     */
    protected void createNodes() {
        int row = 0;
        Iterator iterator = issues.iterator();
        while (iterator.hasNext()) {
            EIssue issue = (EIssue) iterator.next();
            DefaultMutableTreeNode iNode = new DefaultMutableTreeNode(
                    new ETreeNodeInfo(issue.getStep(0), issue.getTitle()
                            .toString()));
            Iterator iter = issue.getExerciseIterator();
            while (iter.hasNext()) {
                Exercise exercise = (Exercise) iter.next();
                DefaultMutableTreeNode eNode = new DefaultMutableTreeNode(
                        new ETreeNodeInfo(exercise, exercise.getTitle()
                                .toString()));
                iNode.add(eNode);
            }
            top.add(iNode);
        } //for issues
        createFolderNodes(top, folders, row);
    } //createNodes

    /**
     * Erzeugt rekursiv alle Ordner-Knoten für den neuen Knoten
     * 
     * @param dmtn
     *            aktueller Knoten
     * @param folders
     *            Unterordner des Knoten
     */
    protected void createFolderNodes(DefaultMutableTreeNode dmtn, List folders,
            int row) {
        row++;
        Iterator iterator = folders.iterator();
        while (iterator.hasNext()) {
            Folder folder = (Folder) iterator.next();
            DefaultMutableTreeNode iNode = new DefaultMutableTreeNode(
                    new ETreeNodeInfo(folder, folder.getName()));
            if (folder.getIssues() != null)
                createIssueNodes(iNode, folder.getIssues());
            if (folder.getFolders() != null)
                createFolderNodes(iNode, folder.getFolders(), row);
            dmtn.add(iNode);
        } //while
    } //createFoldersNodes

    /**
     * Erzeugt rekursiv alle Themen-Knoten für den neuen Knoten
     * 
     * @param dmtn
     *            aktueller Knoten
     * @param folders
     *            alle Themen des Knoten
     */
    protected void createIssueNodes(DefaultMutableTreeNode dmtn, List issues) {
        Iterator iterator = issues.iterator();
        while (iterator.hasNext()) {
            EIssue issue = (EIssue) iterator.next();
            DefaultMutableTreeNode iNode = new DefaultMutableTreeNode(
                    new ETreeNodeInfo(issue, issue.getTitle().toString()));
            Iterator iter = issue.getExerciseIterator();
            while (iter.hasNext()) {
                Exercise exercise = (Exercise) iter.next();
                DefaultMutableTreeNode eNode = new DefaultMutableTreeNode(
                        new ETreeNodeInfo(exercise, exercise.getTitle()
                                .toString()));
                iNode.add(eNode);
            }
            dmtn.add(iNode);
        } //while
    } //createIssuesNodes

    /**
     * Blendet alle Knoten des Baums ein.
     */
    protected final void expandTree() {
        for (int i = 0; i < jTree.getRowCount(); i++) {
            jTree.expandRow(i);
        }
    }

    /**
     * Blendet alle Knoten des Baums aus.
     */
    protected final void collapseTree() {
        for (int i = 0; i < jTree.getRowCount(); i++) {
            jTree.collapseRow(i);
        }
    }

    /**
     * Wird aufgerufen, wenn ein Baum-Element angewählt wird.
     * 
     * @param e
     *            TreeSelectionEvent, das alle Informationen über den Aufruf
     *            enthält
     */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree
                .getLastSelectedPathComponent();
        if (node == null)
            return;
        ETreeNodeInfo nodeInfo = (ETreeNodeInfo) node.getUserObject();
        Math4U2Win.getInstance().actualTreeSelection = this.getTreePath(node);
        TreeSelectionEvent ne = (TreeSelectionEvent) e.cloneWithSource(nodeInfo
                .getObject());
        Iterator iter = treeListeners.iterator();
        while (iter.hasNext()) {
            ((TreeSelectionListener) iter.next()).valueChanged(ne);
        }
    }

    /**
     * Fügt einen neuen Aufpasser (Listener) hinzu, der benachrichtigt wird,
     * wenn ein Element angewählt wird.
     * 
     * @param l
     *            TreeSelectionListener der hinzugefügt werden soll
     */
    public void addTreeSelectionListener(TreeSelectionListener l) {
        treeListeners.add(l);
    }

    /**
     * Entfernt einen Aufpasser (Listener). Dieser wird danach nciht mehr
     * benachrichtigt, wenn ein Element angewählt wird.
     * 
     * @param l
     *            TreeSelectionListener der hinzugefügt werden soll
     */
    public void removeTreeSelectionListener(TreeSelectionListener l) {
        treeListeners.remove(l);
    }

    /**
     * Setzt den aktuell ausgewählten Issue/Exercise.
     * 
     * @param actualTempIssue
     *            aktuell ausgewählter Issue/Exercise
     */
    public void setActualIssueOrExercise(Object actualTempIssue) {
        this.actualIssueOrExercise = actualTempIssue;
    }

    /**
     * Gibt den aktuell ausgewählten Issue/Exercise zurueck.
     * 
     * @return aktuell ausgewählter Issue/Exercise
     */
    public Object getActualIssueOrExcercise() {
        return actualIssueOrExercise;
    }

    /**
     * Gibt den JTree zurueck.
     * 
     * @return JTree
     */
    public JTree getJTree() {
        return jTree;
    }

    /**
     * Wird aufgerufen, wenn ein Ast des Baumes zugeklappt wird.
     */
    public void treeCollapsed(TreeExpansionEvent event) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath()
                .getLastPathComponent();
        this.removeAllChilds(node);
        Math4U2Win.getInstance().actualTreeSelection = this.getTreePath(node);
    }

    /**
     * Entfernt alle Einträge unterhalb des Baumknotens aus der Liste der
     * aufzuklappenden Baumäste.
     * 
     * @param treeNode
     *            Baumknoten
     */
    private void removeAllChilds(TreeNode treeNode) {
        int childCount = treeNode.getChildCount();
        for (int i = 0; i < childCount; i++) {
            this.removeAllChilds(treeNode.getChildAt(i));
        }
        Math4U2Win.getInstance().actualTreePaths.remove(this
                .getTreePath(treeNode));
    }

    /**
     * Wird aufgerufen, wenn ein Ast des Baumes aufgeklappt wird.
     */
    public void treeExpanded(TreeExpansionEvent event) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath()
                .getLastPathComponent();
        Math4U2Win.getInstance().actualTreePaths.add(this.getTreePath(node));
        Math4U2Win.getInstance().actualTreeSelection = this.getTreePath(node);
    }

    /**
     * Erstellt eine Stringrepresentation (Pfad) vom Rootknoten bis zum
     * angegebenen Baumknoten
     * 
     * @param treeNode
     *            Baumknoten
     * @return Stringrepresentation (Pfad)
     */
    private String getTreePath(TreeNode treeNode) {
        String tmp = "";
        Object[] folders = ((DefaultMutableTreeNode) treeNode)
                .getUserObjectPath();
        for (int i = 1; i < folders.length; i++) {
            tmp += folders[i] + "/";
        }
        return tmp;
    }

}