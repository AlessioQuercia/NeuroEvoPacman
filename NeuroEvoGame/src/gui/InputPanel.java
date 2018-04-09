package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.PlainDocument;

public class InputPanel extends JPanel
{
	private JFrame frame;
	
	private JLabel xLabel;
	private JLabel yLabel;
	
	private JFormattedTextField xArea;
	private JFormattedTextField yArea;
	
	private JButton loadInputBtn;
	
	public InputPanel(JFrame frame)
	{
		this.frame = frame;
		
		init();
	}

	private void init()
	{
		setLayout(new GridBagLayout());
		
		setBorder(BorderFactory.createTitledBorder("Net inputs"));
    	
    	GridBagConstraints gc = new GridBagConstraints();
    	
    	xLabel = new JLabel("Target_x:");
    	yLabel = new JLabel("Target_y:");
    	
//        NumberFormat format = NumberFormat.getInstance();
//        format.setGroupingUsed(false);
//        NumberFormatter formatter = new NumberFormatter(format);
//        formatter.setValueClass(Integer.class);
//        formatter.setMinimum(0);
//        formatter.setMaximum(100);
//        formatter.setAllowsInvalid(false);
//        // If you want the value to be committed on each keystroke instead of focus lost
//        formatter.setCommitsOnValidEdit(true);
    	
    	xArea = new JFormattedTextField();
    	xArea.setEditable(true);
    	
    	
    	yArea = new JFormattedTextField();
    	yArea.setEditable(true);
    	
//    	xArea.getDocument().addDocumentListener(new DocumentListener() {
//    	    @Override
//    	    public void insertUpdate(DocumentEvent e) {
//    	        Runnable format = new Runnable() {
//    	            @Override
//    	            public void run() {
//    	                String text = xArea.getText();
//    	                if(!text.matches("\\d{0,3}(\\.\\d{0,5})?")){
//    	                    xArea.setText(text.substring(0,text.length()-1));
//    	                }
//    	            }
//    	        };
//    	        SwingUtilities.invokeLater(format);
//    	    }
//
//    	    @Override
//    	    public void removeUpdate(DocumentEvent e) {
//
//    	    }
//
//    	    @Override
//    	    public void changedUpdate(DocumentEvent e) {
//
//    	    }
//    	});
    	
    	String pattern = "\\d{0,3}(\\.\\d{0,6})?";
    	
    	PlainDocument xDoc = (PlainDocument) xArea.getDocument();
    	xDoc.setDocumentFilter(new PatternFilter(pattern));
    	
    	PlainDocument yDoc = (PlainDocument) yArea.getDocument();
    	yDoc.setDocumentFilter(new PatternFilter(pattern));
    	
//        xArea.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
//        		new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
//        yArea.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
//        		new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
    	loadInputBtn = new JButton("Load inputs");
		
		////// First column ////////
		gc.anchor = GridBagConstraints.LINE_START;
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		
		gc.gridx = 0;
		gc.gridy = 0;	
		add(xLabel, gc);
		
		gc.gridx = 0;
		gc.gridy = 1;		
		add(yLabel, gc);
		
		////// Second column ////////
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 0;	
		add(xArea, gc);
		
		gc.gridx = 1;
		gc.gridy = 1;		
		add(yArea, gc);
		
		// Final row
//		gc.weighty = 10;
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 1;
		gc.gridy = 2;
		add(loadInputBtn, gc);
	}

	public JLabel getxLabel() {
		return xLabel;
	}

	public JLabel getyLabel() {
		return yLabel;
	}

	public JFormattedTextField getxArea() {
		return xArea;
	}

	public JFormattedTextField getyArea() {
		return yArea;
	}

	public JButton getLoadInputBtn() {
		return loadInputBtn;
	}
}
