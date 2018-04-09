package gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class MyTableInTreeCellRenderer extends DefaultTreeCellRenderer
{
	private JTable table = null;
	
	public MyTableInTreeCellRenderer()
	{
		super();
		
		table = new JTable();
		
		updateUI();
	}
	

	public Component getTreeCellRendererComponent(JTree tree, Object value,boolean selected, boolean expanded,boolean leaf, int row,boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
	
	    if ( leaf )     
	    {
	    	Object userObject = ((DefaultMutableTreeNode)value).getUserObject();
	    	if ( userObject instanceof JTable )         
	    	{               
	    		JTable pnlLeaf = (JTable)userObject;                              
	    		tree.setRowHeight(-1);                              
	    		pnlLeaf.setPreferredSize(new Dimension( 250, 544));     
	    		pnlLeaf.repaint();
	    		pnlLeaf.invalidate();
	            pnlLeaf.validate();
	            return pnlLeaf;
	    	}                              
	    }          
          
	    return this;                              
	}

}
