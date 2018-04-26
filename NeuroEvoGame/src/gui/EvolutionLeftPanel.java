package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import common.MyConstants;
import jNeatCommon.EnvConstant;
import jneat.Organism;
import newGui.actor.Ghost;

public class EvolutionLeftPanel extends JPanel
{		
	JFrame frame;
	
	private EvolutionOptionsPanel optionsPanel;
	private InputPanel inputPanel;
	private NetDetailsPanel netPanel;
	private SampleDetailsPanel throwPanel;
	private JLabel generationLabel;

	//	JTextPane infoRete;
//	JTextPane infoLancio;
	Document doc1;
	Document doc2;
    SimpleAttributeSet attributes;
    SimpleAttributeSet attr;
	String mask6d;
	DecimalFormat fmt6d;

	private JSplitPane splitPanel;
	
	private GridBagConstraints gc;
    
	public EvolutionLeftPanel(JFrame frame) 
	{
		this.frame = frame;
		
		init();
		
	}
	
	public EvolutionOptionsPanel getOptionsPanel()
	{
		return optionsPanel;
	}
	
	public void getInfoRete()//ArrayList<Double> array)
	{
		try 
		{
			doc1.insertString(doc1.getLength(),  "errore totale:  " + "\n", attr);
			doc1.insertString(doc1.getLength(),  "fitness totale:  " + "\n", attr);
			doc1.insertString(doc1.getLength(),  "fitness vecchia:  ", attr);
		} catch (BadLocationException e) 
		{
			e.printStackTrace();
		}
//		update(getGraphics());
//		infoRete.validate();
//		infoRete.repaint();
//		infoRete.update(infoRete.getGraphics());
//		String[] info_rete =
//			{
//				"errore totale:  " + "\n",
//				"fitness totale:  " + "\n",
//				"fitness vecchia:  ",
//			};
//		return info_rete;
	}
	
	public void getInfoLancio()//ArrayList<Double> array)
	{
//		infoLancio.setText("");
//		infoLancio.revalidate();
//		infoLancio.repaint();
		try 
		{
			doc2.insertString(doc2.getLength(),  "errore totale:  " + "\n", attributes);
			doc2.insertString(doc2.getLength(),  "fitness totale:  " + "\n", attributes);
			doc2.insertString(doc2.getLength(),  "errore totale:  " + "\n", attributes);
			doc2.insertString(doc2.getLength(),  "fitness totale:  " + "\n", attributes);
			doc2.insertString(doc2.getLength(),  "errore totale:  " + "\n", attributes);
			doc2.insertString(doc2.getLength(),  "fitness totale:  " + "\n", attributes);
			doc2.insertString(doc2.getLength(),  "errore totale:  " + "\n", attributes);
			doc2.insertString(doc2.getLength(),  "fitness totale:  " + "\n", attributes);
			doc2.insertString(doc2.getLength(),  "errore totale:  " + "\n", attributes);
			doc2.insertString(doc2.getLength(),  "fitness vecchia:  ", attributes);
			
		} 
		catch (BadLocationException e) 
		{
			e.printStackTrace();
		}
//		infoLancio.validate();
//		infoLancio.repaint();
//		infoLancio.update(infoRete.getGraphics());
//		String[] info_lancio = 
//			{
//				"x_target:  " + "\n",
//				"y_target:  " + "\n",
//				"y_lancio:  " + "\n",
//				"angolo:  " + "\n",
//				"velocità:  " + "\n",
//				"forza:  " + "\n",
//				"tempo:  " + "\n",	
//				"accelerazione:  " + "\n",
//				"massa:  " + "\n",
//				"errore:  ",
//			};
//		return info_lancio;
	}
	
	public void updateInfoRete(ArrayList<Double> array)
	{
//		infoRete.setText("");
//		infoRete.revalidate();
//		infoRete.repaint();
////		infoRete.update(infoRete.getGraphics());
////		infoRete.validate();
////		infoRete.repaint();
//		
//		try {
//			doc1.insertString(doc1.getLength(),  "errore totale:  " + fmt6d.format(array.get(MyConstants.ERRORE_TOTALE_INDEX)) + "\n", attr);
//			doc1.insertString(doc1.getLength(),  "fitness totale:  " + fmt6d.format(array.get(MyConstants.FITNESS_TOTALE_INDEX)) + "\n", attr);
//			doc1.insertString(doc1.getLength(),  "fitness vecchia:  " + fmt6d.format(array.get(MyConstants.FITNESS_VECCHIA_INDEX)), attr);
//		} catch (BadLocationException e) 
//		{
//			e.printStackTrace();
//		}
////		update(getGraphics());
//		infoRete.validate();
//		infoRete.repaint();
//		infoRete.update(infoRete.getGraphics());
		
//		String[] info_rete =
//			{
//				"errore totale:  " + array.get(MyConstants.ERRORE_TOTALE_INDEX) + "\n",
//				"fitness totale:  " + array.get(MyConstants.FITNESS_TOTALE_INDEX) + "\n",
//				"fitness vecchia:  " + array.get(MyConstants.FITNESS_VECCHIA_INDEX),
//			};
		String info_rete =
			"Total error:  " + fmt6d.format(array.get(MyConstants.ERRORE_TOTALE_INDEX)) + " m" + "\n" +
			"Fitness:  " + fmt6d.format(array.get(MyConstants.FITNESS_TOTALE_INDEX));
//			"fitness vecchia:  " + array.get(MyConstants.FITNESS_VECCHIA_INDEX);
		
		netPanel.getInfoRete().setText(info_rete);
		
		
//		};
//		infoRete.setText(info_rete);
////		for (String s : info_rete)
////			infoRete.append(s);
//		infoRete.revalidate();
//		infoRete.repaint();
	}
	
	public void updateInfoLancio(ArrayList<Double> array)
	{
//		System.out.println("pappetta");
//		infoLancio.setText("");
////		infoLancio.validate();
////		infoLancio.repaint();
//		try 
//		{
//			doc2.insertString(doc2.getLength(),  "errore totale:  " + fmt6d.format(array.get(MyConstants.X_TARGET_INDEX)) + "\n", attributes);
//			doc2.insertString(doc2.getLength(),  "fitness totale:  " + fmt6d.format(array.get(MyConstants.Y_TARGET_INDEX)) + "\n", attributes);
//			doc2.insertString(doc2.getLength(),  "errore totale:  " + fmt6d.format(array.get(MyConstants.Y_LANCIO_INDEX)) + "\n", attributes);
//			doc2.insertString(doc2.getLength(),  "fitness totale:  " + fmt6d.format(array.get(MyConstants.ANGOLO_INDEX)) + "\n", attributes);
//			doc2.insertString(doc2.getLength(),  "errore totale:  " + fmt6d.format(array.get(MyConstants.VELOCITA_INDEX)) + "\n", attributes);
//			doc2.insertString(doc2.getLength(),  "fitness totale:  " + fmt6d.format(array.get(MyConstants.FORZA_INDEX)) + "\n", attributes);
//			doc2.insertString(doc2.getLength(),  "errore totale:  " + fmt6d.format(array.get(MyConstants.TEMPO_INDEX)) + "\n", attributes);
//			doc2.insertString(doc2.getLength(),  "fitness totale:  " + fmt6d.format(array.get(MyConstants.ACCELERAZIONE_INDEX)) + "\n", attributes);
//			doc2.insertString(doc2.getLength(),  "errore totale:  " + fmt6d.format(array.get(MyConstants.MASSA_INDEX)) + "\n", attributes);
//			doc2.insertString(doc2.getLength(),  "fitness vecchia:  " + fmt6d.format(array.get(MyConstants.ERRORE_INDEX)), attributes);
//			
//		} 
//		catch (BadLocationException e) 
//		{
//			e.printStackTrace();
//		}
//		infoLancio.validate();
//		infoLancio.repaint();
//		infoLancio.update(infoLancio.getGraphics());
		
		
//		String info_lancio = 
//				"Target_x:  " + fmt6d.format(array.get(MyConstants.BEST_TARGET_X_INDEX)) + " m" + "\n" +
//				"Target_y:  " + fmt6d.format(array.get(MyConstants.BEST_TARGET_Y_INDEX)) + " m" + "\n" +
//				"Projectile_x:  " + fmt6d.format(array.get(MyConstants.X_MIGLIORE_INDEX)) + " m" + "\n" +
//				"Projectile_y:  " + fmt6d.format(array.get(MyConstants.Y_MIGLIORE_INDEX)) + " m" + "\n" +
////				"y(Target_x):  " + fmt6d.format(array.get(MyConstants.Y_LANCIO_INDEX)) + " m" + "\n" +
//				"Angle:  " + fmt6d.format(Math.toDegrees(array.get(MyConstants.ANGOLO_INDEX))) + "°" + "\n" +
//				"Velocity:  " + fmt6d.format(array.get(MyConstants.VELOCITA_INDEX)) + " m/s" + "\n" +
////				"forza:  " + array.get(MyConstants.FORZA_INDEX) + " N" + "\n" +
//				"Time:  " + fmt6d.format(array.get(MyConstants.TEMPO_INDEX)) + " s" + "\n" +	
////				"accelerazione:  " + array.get(MyConstants.ACCELERAZIONE_INDEX) + "\n" +
//				"Mass:  " + fmt6d.format(array.get(MyConstants.MASSA_INDEX)) + " kg" + "\n" +
//				"Error:  " + fmt6d.format(array.get(MyConstants.ERRORE_INDEX)) + " m";
//		
//		throwPanel.getInfoLancio().setText(info_lancio);
		
		
//		infoLancio.setText(info_lancio);
//		infoLancio.revalidate();
//		infoLancio.repaint();
	}
	
//	public JTextPane getInfoList1()
//	{
//		return infoRete;
//	}
//	public JTextPane getInfoList2()
//	{
//		return infoRete;
//	}
	
	public void updateInfoRete(Organism o)
	{
		String info_rete =
				"errore totale:  " + o.getError() + "\n" +
				"fitness totale:  " + o.getOrig_fitness();
//				"fitness vecchia:  " + array.get(MyConstants.FITNESS_VECCHIA_INDEX);
			
			netPanel.getInfoRete().setText(info_rete);
	}
	
	public void updateInfoLancio(Organism o, int lancio, int timestep)
	{
		ArrayList<Double> array = o.getMap().get(lancio);
		String info_lancio = 
				"Pacman_x:  " + o.getPacmanPositions().get(timestep).x + "\n" +
				"Pacman_y:  " + o.getPacmanPositions().get(timestep).y + "\n" +
				"Pacman_direction:  " + o.getPacmanDirections().get(timestep) + "\n" +
				"Ghost_1_x:  " + o.getGhostsPositions().get(0).get(timestep).x + "\n" +
				"Ghost_1_y:  " + o.getGhostsPositions().get(0).get(timestep).y + "\n" +
				"Ghost_1_direction:  " + Ghost.getDirection(o.getGhostsDirections().get(0).get(timestep)) + "\n" +
				"Ghost_2_x:  " + o.getGhostsPositions().get(1).get(timestep).x + "\n" +
				"Ghost_2_y:  " + o.getGhostsPositions().get(1).get(timestep).y + "\n" +
				"Ghost_2_direction:  " + Ghost.getDirection(o.getGhostsDirections().get(1).get(timestep)) + "\n" +
				"Ghost_3_x:  " + o.getGhostsPositions().get(2).get(timestep).x + "\n" +
				"Ghost_3_y:  " + o.getGhostsPositions().get(2).get(timestep).y + "\n" +
				"Ghost_3_direction:  " + Ghost.getDirection(o.getGhostsDirections().get(2).get(timestep)) + "\n" +
				"Ghost_4_x:  " + o.getGhostsPositions().get(3).get(timestep).x + "\n" +
				"Ghost_4_y:  " + o.getGhostsPositions().get(3).get(timestep).y + "\n" +
				"Ghost_4_direction:  " + Ghost.getDirection(o.getGhostsDirections().get(3).get(timestep)) + "\n" +
				"timestep:  " + timestep;
		
		throwPanel.getInfoLancio().setText(info_lancio);
	}
	
	public void init()
	{
		Dimension size = getPreferredSize();
		mask6d = "  0.000000";
		fmt6d = new DecimalFormat(mask6d);
		
		size.width = MyConstants.OPTIONS_WIDTH;
		setPreferredSize(size);	
		
    	setLayout(new GridBagLayout());
    	
    	gc = new GridBagConstraints();
		
		optionsPanel = new EvolutionOptionsPanel(frame);
		inputPanel = new InputPanel(frame);
		netPanel = new NetDetailsPanel(frame);
		throwPanel = new SampleDetailsPanel(frame);
		generationLabel = new JLabel();
		
		///  STILE SCRITTURA
		attributes = new SimpleAttributeSet();
	    StyleConstants.setBold(attributes, true);
	    StyleConstants.setItalic(attributes, true);
	    
		attr = new SimpleAttributeSet();
	    StyleConstants.setBold(attr, true);
	    StyleConstants.setItalic(attr, true);
		
//		infoRete = new JTextPane();
////		infoRete.setBorder(BorderFactory.createTitledBorder("Dettagli rete"));
//		infoRete.setFont(getFont());
////		infoRete.setLineWrap(true);
//		infoRete.setEditable(false);
//		infoRete.setOpaque(false);
////		infoRete.setWrapStyleWord(true);
//		infoRete.setVisible(true);
//		doc1 = infoRete.getDocument();
////		getInfoRete();
////		for (String s : getInfoRete())
////			infoRete.append(s);
//		
//		infoLancio = new JTextPane();
////		infoLancio.setBorder(BorderFactory.createTitledBorder("Dettagli lancio"));
//		infoLancio.setFont(getFont());
////		infoLancio.setLineWrap(true);
//		infoLancio.setEditable(false);
//		infoLancio.setOpaque(false);
////		infoLancio.setWrapStyleWord(true);
//		infoLancio.setVisible(true);
//		doc2 = infoLancio.getDocument();
//		getInfoLancio();
//		for (String s : getInfoLancio())
//			infoLancio.append(s);
	
	    UIManager.put("SplitPaneDivider.border", BorderFactory.createEmptyBorder()); 
	    
		splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPanel.setTopComponent(throwPanel);
		splitPanel.setBottomComponent(inputPanel);
		splitPanel.setOneTouchExpandable(true);
		splitPanel.setContinuousLayout(true);
		
		throwPanel.setMinimumSize(new Dimension(MyConstants.OPTIONS_WIDTH, 15));
		inputPanel.setMinimumSize(new Dimension(MyConstants.OPTIONS_WIDTH, 100));
		
		splitPanel.setDividerLocation(optionsPanel.getHeight()/2);
		splitPanel.setDividerSize(10);
		
		splitPanel.setPreferredSize(new Dimension(MyConstants.OPTIONS_WIDTH, throwPanel.getHeight()));
	    
		splitPanel.setBorder(
			BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(), 
			BorderFactory.createEmptyBorder())); 
		
//		gc.anchor = GridBagConstraints.LINE_START;
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 0.5;
		gc.weighty = 1;
		
		gc.gridx = 0;
		gc.gridy = 0;	
		add(optionsPanel, gc);	
		
		gc.weightx = 0.5;
		gc.weighty = 0.01;
		
		gc.gridx = 0;
		gc.gridy = 1;	
		add(netPanel, gc);
		
		gc.weighty = 20;
		
		gc.gridx = 0;
		gc.gridy = 2;	
		add(throwPanel, gc);
//		add(splitPanel, gc);
		
		gc.weighty = 0.01;
		
		gc.gridx = 0;
		gc.gridy = 3;		
		add(generationLabel, gc);
	}
	
	public JLabel getGenerationLabel() 
	{
		return generationLabel;
	}
//	public JTextPane getRete()
//	{
//		return infoRete;
//	}

	public JSplitPane getSplitPanel() 
	{
		return splitPanel;
	}
	
	public GridBagConstraints getGC()
	{
		return gc;
	}

	public SampleDetailsPanel getThrowPanel()
	{
		return throwPanel;
	}

	public InputPanel getInputPanel() 
	{
		return inputPanel;
	}
	
	public void setNormalLayout()
	{
		remove(splitPanel);
		
		gc.weighty = 10;
		
		gc.gridx = 0;
		gc.gridy = 2;	
		add(throwPanel, gc);
	}
	
	public void setLoadLayout()
	{
		remove(throwPanel);
		
		splitPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPanel.setTopComponent(throwPanel);
		splitPanel.setBottomComponent(inputPanel);
		splitPanel.setOneTouchExpandable(true);
		splitPanel.setContinuousLayout(true);
		
		throwPanel.setMinimumSize(new Dimension(MyConstants.OPTIONS_WIDTH, 15));
		inputPanel.setMinimumSize(new Dimension(MyConstants.OPTIONS_WIDTH, 100));
		
		splitPanel.setDividerLocation(optionsPanel.getHeight()/2);
		splitPanel.setDividerSize(10);
		
		splitPanel.setPreferredSize(new Dimension(MyConstants.OPTIONS_WIDTH, throwPanel.getHeight()));
	    
		splitPanel.setBorder(
			BorderFactory.createCompoundBorder(
			BorderFactory.createEmptyBorder(), 
			BorderFactory.createEmptyBorder())); 
		
		
		gc.weighty = 10;
		
		gc.gridx = 0;
		gc.gridy = 2;	
		add(splitPanel, gc);
	}
}
