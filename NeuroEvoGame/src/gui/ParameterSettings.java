package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gui.ParamValue;
import gui.vectTableModel;
import jneat.Neat;

public class ParameterSettings extends JPanel implements ListSelectionListener
{
	private JFrame frame;
	private ParameterUpperPanel upperPanel;
	private JScrollPane scrollPanel;
	private JTextArea description;
	
	private vectTableModel model;
	private JTable parameter_table;
	public JTextArea getDescription() {
		return description;
	}

	private Neat netx;
	
	public ParameterSettings(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}
	
	public ParameterUpperPanel getUpperPanel() {
		return upperPanel;
	}

	public void init()
	{
//		setBorder(BorderFactory.createTitledBorder("Parameters"));
		
		setLayout(new GridBagLayout());	
		
		GridBagConstraints gc = new GridBagConstraints();
		
		upperPanel = new ParameterUpperPanel(frame);
		
		description = new JTextArea();
		description.setBorder(BorderFactory.createTitledBorder("Description"));
		Dimension size = description.getSize();
		size.height = 110;
		description.setMinimumSize(size);
		description.setPreferredSize(size);
		description.setBackground(null);
		description.setEditable(false);
		
//		defaultParameters = new JButton("Restore default parameters");
//		
//		netx = new Neat();
//		netx.initbase();
//		String name = "parameters"; //MyConstants.PARAMETRI_NOMEFILE;
//    	model = new vectTableModel(new Vector(), name, netx);
//		boolean rc = netx.readParam(name);
//		netx.getParam(model);
//		
//		model.fireTableDataChanged();
//		model.setFileRead(true);	// Questo permette di scrivere su file le modifiche effettuate da ora in poi
//		
//		// TODO CELLE TABELLA CHE ACCETTANO SOLO INPUT VALIDI DALLA TASTIERA
//		
//    	parameter_table = new JTable(model);
//    	parameter_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//    	parameter_table.setTableHeader(null);
//    	parameter_table.setBackground(null);
////    	parameter_table.setBorder(BorderFactory.createLineBorder(Color.GRAY));
////    	parameter_table.setBorder(BorderFactory.createTitledBorder("Parameters"));
//    	JTextField field = new JTextField();
//    	PlainDocument doc = (PlainDocument)field.getDocument();
//    	String pattern = "\\d{0,6}(\\.\\d{0,6})?";
//    	doc.setDocumentFilter(new PatternFilter(pattern));
//    	DefaultCellEditor editor = new DefaultCellEditor(field);
//    	parameter_table.getColumnModel().getColumn(1).setCellEditor(editor);
		ListSelectionModel lsm = upperPanel.getParameter_table().getSelectionModel();
		lsm.addListSelectionListener(this);
		
		
//    	parameter_table.setCellEditor(editor);
//    	parameter_table.getColumn(1).setCellEditor(editor);
//    	TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(parameter_table.getModel());
//    	parameter_table.setRowSorter(sorter);
//    	int column = parameter_table.getEditingColumn();
//    	int row = parameter_table.getEditingRow();
//    	parameter_table.getCellRenderer(row, column);
//    	parameter_table.getCellEditor(row, column);
		
    	scrollPanel = new JScrollPane(upperPanel);
    	scrollPanel.setBorder(BorderFactory.createTitledBorder("Parameters"));
    	
//		gc.anchor = GridBagConstraints.FIRST_LINE_START;
//    	gc.anchor = gc.EAST;
//    	
//    	gc.gridx = 0;
//    	gc.gridy = 0;
//    	add(defaultParameters, gc);
    	
		gc.fill = GridBagConstraints.BOTH;
		
		gc.gridx = 0;
		gc.gridy = 1;
    	add(description, gc);
    	
		gc.weightx = 0.5;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = 0;
    	add(upperPanel, gc);
		
	}
	
	public void updateDescritpion(String text)
	{
		description.setText(text);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		 int irow = 0;
		 Object s_descr = null;
		 Object s2 = null;
		 ParamValue ox = null;
		 String r2 = null;
		 String r3 = null;
		 String tipo = null;
	  
	  
		 if (e.getValueIsAdjusting())
		 {
			return;
		 }
	  
	  
		 ListSelectionModel lsm = (ListSelectionModel) e.getSource();
	  
	  
		 if (!lsm.isSelectionEmpty()) 
		 {
			irow = lsm.getMinSelectionIndex();
			ox = (ParamValue) upperPanel.getModel().data.elementAt(irow);
			s_descr = upperPanel.getNetx().getDescription((String) ox.o1);
			s2 = ox.o2;
		 
			if ( s2 instanceof Integer)
			{
			   tipo = new String(" integer ");
			}
			if ( s2 instanceof Double )
			{
			   tipo = new String(" double");
			}
		 
			r2 = "\n Current setting is " + s2;
			r3 = s_descr+r2+tipo;
			updateDescritpion(r3);
		 
			upperPanel.getScrollPanel().revalidate();
			upperPanel.getScrollPanel().validate();
		 }		
	}
}
