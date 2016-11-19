package com.github.wglanzer.redmine.gui;

import com.github.wglanzer.redmine.RManager;
import com.github.wglanzer.redmine.model.IProject;
import com.github.wglanzer.redmine.model.IServer;
import com.github.wglanzer.redmine.model.ITicket;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.util.List;

/**
 * Defines the component for the toolwindow
 *
 * @author w.glanzer, 19.11.2016.
 */
public class RedmineToolComponent extends JPanel
{
  private JPanel rootPanel;
  private JButton refreshButton;
  private JTree redmineTree;

  public RedmineToolComponent()
  {
    setLayout(new BorderLayout());
    SwingUtilities.invokeLater(() ->
    {
      add(rootPanel, BorderLayout.CENTER);
      refreshButton.addActionListener(e -> _refreshTree());
      _refreshTree();
    });
  }

  /**
   * Updates the tree
   */
  private void _refreshTree()
  {
    redmineTree.clearSelection();
    redmineTree.setModel(_createTreeModel(RManager.getInstance().getServerManager().getAvailableServers()));
    redmineTree.expandRow(0);
    redmineTree.setRootVisible(false);
  }

  /**
   * Creates the tree model do display all servers with projects and tickets
   *
   * @param pServerList List of all servers known
   * @return the TreeModel representing the list
   */
  private TreeModel _createTreeModel(List<IServer> pServerList)
  {
    DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    for(IServer currServer : pServerList)
      root.add(new _ServerNode(currServer));
    return new DefaultTreeModel(root);
  }

  {
    // GUI initializer generated by IntelliJ IDEA GUI Designer
    // >>> IMPORTANT!! <<<
    // DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$()
  {
    rootPanel = new JPanel();
    rootPanel.setLayout(new BorderLayout(0, 0));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new BorderLayout(0, 0));
    rootPanel.add(panel1, BorderLayout.NORTH);
    refreshButton = new JButton();
    refreshButton.setText("Refresh");
    panel1.add(refreshButton, BorderLayout.WEST);
    redmineTree = new JTree();
    rootPanel.add(redmineTree, BorderLayout.CENTER);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$()
  {
    return rootPanel;
  }

  /**
   * Node representing one single server
   */
  private static class _ServerNode extends DefaultMutableTreeNode
  {
    public _ServerNode(IServer pMyServer)
    {
      setUserObject(pMyServer.getURL());
      for(IProject currProject : pMyServer.getProjects())
        add(new _ProjectNode(currProject));
    }
  }

  /**
   * Node representing one single project
   */
  private static class _ProjectNode extends DefaultMutableTreeNode
  {
    public _ProjectNode(IProject pMyProject)
    {
      setUserObject(pMyProject.getName());
      for(ITicket currTicket : pMyProject.getTickets().values())
        add(new _TicketNode(currTicket));
    }
  }

  /**
   * Node representing one single ticket
   */
  private static class _TicketNode extends DefaultMutableTreeNode
  {
    public _TicketNode(ITicket pMyTicket)
    {
      setUserObject(pMyTicket.getID());
    }
  }


}
