package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.PlainDocument;

import common.MyConstants;
import gui.ParamValue;
import gui.vectTableModel;
import jneat.Neat;

public class ParameterUpperPanel extends JPanel
{
	private JFrame frame;
	
	private vectTableModel model;
	private JTable parameter_table;
	public void setModel(vectTableModel model) {
		this.model = model;
	}

	public void setParameter_table(JTable parameter_table) {
		this.parameter_table = parameter_table;
	}

	private JScrollPane scrollPanel;
	private Neat netx;
	private JButton defaultParameters;
	
	public ParameterUpperPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
	}
	
	public void init() 
	{
		setLayout(new GridBagLayout());	
		
		setBorder(BorderFactory.createTitledBorder("Parameters"));
		
		GridBagConstraints gc = new GridBagConstraints();
		
		defaultParameters = new JButton("Restore default parameters");
		Dimension size = defaultParameters.getSize();
		size.width = 200;
		size.height = 25;
		defaultParameters.setMinimumSize(size);
		defaultParameters.setPreferredSize(size);
		
		netx = new Neat();
		netx.initbase();
		String name = MyConstants.PARAMETRI_NOMEFILE; //"parameters"; //MyConstants.PARAMETRI_NOMEFILE;
    	model = new vectTableModel(new Vector(), name, netx);
		boolean rc = netx.readParam(name);
		netx.getParam(model);
		
		model.fireTableDataChanged();
		model.setFileRead(true);	// Questo permette di scrivere su file le modifiche effettuate da ora in poi
		
		// TODO CELLE TABELLA CHE ACCETTANO SOLO INPUT VALIDI DALLA TASTIERA
		
    	parameter_table = new JTable(model);
    	parameter_table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    	parameter_table.setTableHeader(null);
    	parameter_table.setBackground(null);
//    	parameter_table.setBorder(BorderFactory.createLineBorder(Color.GRAY));
//    	parameter_table.setBorder(BorderFactory.createTitledBorder("Parameters"));
    	JTextField field = new JTextField();
    	PlainDocument doc = (PlainDocument)field.getDocument();
    	String pattern = "\\d{0,6}(\\.\\d{0,6})?";
    	doc.setDocumentFilter(new PatternFilter(pattern));
    	DefaultCellEditor editor = new DefaultCellEditor(field);
    	parameter_table.getColumnModel().getColumn(1).setCellEditor(editor);
    	
    	scrollPanel = new JScrollPane(parameter_table);
    	
    	gc.anchor = GridBagConstraints.EAST;

		gc.gridx = 0;
		gc.gridy = 1;

    	add(defaultParameters, gc);
		
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = 0;
    	add(scrollPanel, gc);
    	
		
	}
	
	public void setScrollPanel(JScrollPane scrollPanel) {
		this.scrollPanel = scrollPanel;
	}

	public JButton getDefaultParameters() {
		return defaultParameters;
	}
	

	public JScrollPane getScrollPanel() {
		return scrollPanel;
	}
	public vectTableModel getModel() {
		return model;
	}

	public JTable getParameter_table() {
		return parameter_table;
	}

	public Neat getNetx() {
		return netx;
	}

	public void setNetx(Neat neat) {
		// TODO Auto-generated method stub
		
	}
}
