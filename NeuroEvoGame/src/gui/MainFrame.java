package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import common.MyConstants;

public class MainFrame extends JFrame
{
	public MainFrame()
	{
		setTitle(MyConstants.TITLE);
		pack();
		setSize(MyConstants.WIDTH, MyConstants.HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
