package me.lukasdietrich.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import me.lukasdietrich.Strings;

public class SelectionActivity extends JDialog {
	private static final long serialVersionUID = 9040774353241742403L;
	private JPanel contentPanel = new JPanel();
	private JPanel panel = new JPanel();

	private ArrayList<Integer> ports = new ArrayList<Integer>();
	
	/**
	 * Create the dialog.
	 */
	public SelectionActivity(JFrame parent) {
		super(parent);
		
		setModalityType(ModalityType.APPLICATION_MODAL);
		setModal(true);
		setTitle(Strings.getString(SelectionActivity.class, "0"));
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JScrollPane scrlPane = new JScrollPane();
			contentPanel.add(scrlPane);
			{
				scrlPane.setViewportView(panel);
				panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
				
				for(LogicalClient client : LogicalClientsContainer.getInstance()) {
					JCheckBox box = new JCheckBox(client.getName() +"@"+ client.getPort());
					
					ports.add(client.getPort());
					panel.add(box);
				}
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnOk = new JButton(Strings.getString(SelectionActivity.class, "2"));
				btnOk.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				btnOk.setActionCommand(Strings.getString(SelectionActivity.class, "3"));
				buttonPane.add(btnOk);
				getRootPane().setDefaultButton(btnOk);
			}
			{
				JButton btnCancel = new JButton(Strings.getString(SelectionActivity.class, "4"));
				btnCancel.setActionCommand("Cancel");
				buttonPane.add(btnCancel);
			}
		}
	}
	
	public int[] getSelectedPortsAsArray() {
		LinkedList<Integer> selected = getSelectedPorts();
		
		int[] ports = new int[selected.size()];
		int i = 0;
		
		for(int port : selected) {
			ports[i++] = port;
		}
		
		return ports;
	}
	
	public LinkedList<Integer> getSelectedPorts() {
		LinkedList<Integer> ports = new LinkedList<Integer>();
		
		int i = 0;
		
		for(Component comp : panel.getComponents()) {
			if(comp instanceof JCheckBox) {
				JCheckBox check = (JCheckBox) comp;
				if(check.isSelected()) {
					ports.add(this.ports.get(i));
				}
				i++;
			}
		}
		
		return ports;
	}

}
