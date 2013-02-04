package me.lukasdietrich.client;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import me.lukasdietrich.Strings;
import me.lukasdietrich.commons.DateTime;
import me.lukasdietrich.commons.Network;
import me.lukasdietrich.transfer.ChatTransfer;

public class ChatConversationView extends JPanel {
	private static final long serialVersionUID = 1114313660381218158L;
	private JScrollPane scrlPane;
	private JList list;
	private DefaultListModel model;
	private JTextField txtInput;

	public void addMessage(String dateTime, JLabel message, boolean isSelf) {
		model.addElement(new ChatEntryView(dateTime, message, isSelf));
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JScrollBar bar = scrlPane.getVerticalScrollBar();
				bar.setValue(bar.getMaximum()-bar.getVisibleAmount());
			}
			
		});
	}
	
	public void addLocalMessage(String message) {
		addMessage(DateTime.format("%d.%M %h:%m"), new JLabel(message), true);
	}
	
	/**
	 * Create the panel.
	 */
	public ChatConversationView(final Client client, final int port) {
		this.model = new DefaultListModel();
		this.scrlPane = new JScrollPane();
		this.scrlPane.setBorder(null);
		this.txtInput = new JTextField();
		this.txtInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				JTextField field = ((JTextField) arg0.getSource());
				String text = field.getText();
				
				if(arg0.getKeyCode() == KeyEvent.VK_ENTER && !text.trim().equals("")) {
					
					if(text.startsWith("/")) { // command
						text = text.substring(1); // trim slash
						
						if(text.equalsIgnoreCase("help")) {
							
						} else if(text.equalsIgnoreCase("info")) {
							addLocalMessage("Name: "+ client.getNetworkName() +", IP: "+ Network.getAddress());
						} else if(text.equalsIgnoreCase("clear")) {
							model.clear();
						} else {
							addLocalMessage(Strings.getString(ChatConversationView.class, "0"));
						}
						
					} else { // message
						try {
							ChatTransfer trans = new ChatTransfer(client.getPort(), field.getText(), port);
							client.sendObject(trans);
							addMessage(trans.getDateTime(), new JLabel(trans.getMessage()), true);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					field.setText("");
				}
			}
		});
		this.txtInput.setColumns(10);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(this.scrlPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
						.addComponent(this.txtInput, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(this.scrlPane, GroupLayout.DEFAULT_SIZE, 10, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(this.txtInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		{
			this.list = new JList(model);
			this.list.setCellRenderer(new ChatListRenderer());
			this.scrlPane.setViewportView(this.list);
		}
		setLayout(groupLayout);
	}
	
	private class ChatListRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 5931805506624575131L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			
			return (ChatEntryView) value;
		}
		
	}
	
}
