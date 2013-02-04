package me.lukasdietrich.client;

import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;

import me.lukasdietrich.commons.LogEvent;
import me.lukasdietrich.commons.Logger;
import me.lukasdietrich.commons.LoggerListener;
import me.lukasdietrich.transfer.KickTransfer;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class ServerClientActivity extends JDialog {
	private static final long serialVersionUID = 2086856211965324022L;
	private JScrollPane scrlPane2;
	private DefaultListModel model;
	private JList lstClients;
	private JButton btnKick;
	private JButton btnBan;
	private JScrollPane scrlPane1;
	private JTextArea txtConsole;

	private Client client;
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	/**
	 * Create the dialog.
	 */
	public ServerClientActivity(JFrame parent) {
		super(parent);
		
		setTitle("ServerManager");
		setBounds(100, 100, 450, 300);
		this.scrlPane2 = new JScrollPane();
		this.btnKick = new JButton("Kick");
		this.btnKick.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					client.sendObject(new KickTransfer(client.getPort(), "Kicked!", Integer.parseInt(lstClients.getSelectedValue().toString().substring(1))));
				} catch (NumberFormatException e) {
					Logger.get().err(e, ServerClientActivity.this);
				} catch (IOException e) {
					Logger.get().err(e, ServerClientActivity.this);
				}
			}
		});
		this.btnBan = new JButton("Ban");
		
		this.scrlPane1 = new JScrollPane();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(this.scrlPane2, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.scrlPane1, GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.btnKick)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.btnBan)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.scrlPane1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE)
						.addComponent(this.scrlPane2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.btnKick)
						.addComponent(this.btnBan))
					.addContainerGap())
		);
		{
			this.txtConsole = new JTextArea();
			this.txtConsole.setEditable(false);
			
			Logger.get().addListener(new LoggerListener() {

				@Override
				public void logged(LogEvent e) {
					txtConsole.append(e.getMessage() + "\n");
				}
				
			});
			
			this.scrlPane1.setViewportView(this.txtConsole);
		}
		{
			this.model = new DefaultListModel();
			this.lstClients = new JList(model);
			
			LogicalClientsContainer.getInstance().addListener(new LogicalClientsContainerListener() {
				
				@Override
				public void clientRemoved(int port) {
					model.removeElement("@"+ port);
					lstClients.repaint();
				}
				
				@Override
				public void clientAdded(int port) {
					model.addElement("@"+ port);
					lstClients.repaint();
				}
			});
			
			this.scrlPane2.setViewportView(this.lstClients);
		}
		getContentPane().setLayout(groupLayout);

	}
}
