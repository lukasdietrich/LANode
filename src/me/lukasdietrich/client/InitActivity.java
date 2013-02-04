package me.lukasdietrich.client;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import me.lukasdietrich.Strings;
import me.lukasdietrich.commons.Network;

public class InitActivity extends JDialog {
	private static final long serialVersionUID = 961209622654182239L;
	
	private JRadioButton rdbtnHost;
	private JRadioButton rdbtnClient;
	private JTextField txtAdress;
	private ButtonGroup rdbtnGroup = new ButtonGroup();
	private JTextField txtUser;
	private JLabel lblName;
	private JButton btnStart;
	private JButton btnCancel;
	private JButton btnNachServernSuchen;

	/**
	 * Create the dialog.
	 */
	public InitActivity() {
		setTitle(Strings.getString(InitActivity.class, "this.title"));
		setIconImage(Toolkit.getDefaultToolkit().getImage(InitActivity.class.getResource("/me/lukasdietrich/assets/tray.png")));
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		this.rdbtnHost = new JRadioButton(Strings.getString(InitActivity.class, "0"));
		this.txtAdress = new JTextField(Strings.getString(InitActivity.class, "1"));
		this.txtAdress.setText(Network.getAddress().substring(0, Network.getAddress().lastIndexOf(".")+1));
		rdbtnGroup.add(this.rdbtnHost);
		this.btnStart = new JButton(Strings.getString(InitActivity.class, "4"));
		this.rdbtnClient = new JRadioButton(Strings.getString(InitActivity.class, "2"));
		this.rdbtnClient.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				txtAdress.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
				if(e.getStateChange() == ItemEvent.SELECTED) {
					btnStart.setText(Strings.getString(InitActivity.class, "4"));
				} else {
					btnStart.setText(Strings.getString(InitActivity.class, "4b"));
				}
			}
		});
		rdbtnGroup.add(this.rdbtnClient);
		this.rdbtnClient.setSelected(true);
		this.txtAdress.setColumns(10);
		
		this.txtUser = new JTextField(System.getProperty("user.name"));
		this.txtUser.setColumns(10);
		this.lblName = new JLabel(Strings.getString(InitActivity.class, "3"));
		this.btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				// stops break
				// continue main-method
			}
		});
		this.btnCancel = new JButton(Strings.getString(InitActivity.class, "5"));
		this.btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		this.btnNachServernSuchen = new JButton(Strings.getString(InitActivity.class, "btnNachServernSuchen.text"));
		this.btnNachServernSuchen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SearchHostActivity searchHost = new SearchHostActivity(InitActivity.this);
				searchHost.setVisible(true);
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.rdbtnHost)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.rdbtnClient)
							.addGap(6)
							.addComponent(this.txtAdress, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.lblName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.txtUser, GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.btnNachServernSuchen)
							.addPreferredGap(ComponentPlacement.RELATED, 268, Short.MAX_VALUE)
							.addComponent(this.btnCancel)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.btnStart)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.txtAdress, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.rdbtnHost)
						.addComponent(this.rdbtnClient))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblName)
						.addComponent(this.txtUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.btnStart)
						.addComponent(this.btnCancel)
						.addComponent(this.btnNachServernSuchen))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);

		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
			
		});
		
		this.pack();
		this.validate();
	}
	
	public String getUser() {
		return txtUser.getText();
	}
	
	public boolean isHost() {
		return !txtAdress.isEnabled();
	}
	
	public String getAdress() {
		if(txtAdress.isEnabled())
			return txtAdress.getText();
		return null;
	}
}
