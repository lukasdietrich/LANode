package me.lukasdietrich.client;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Hashtable;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import me.lukasdietrich.transfer.ChatTransfer;
import me.lukasdietrich.transfer.ScreenShotTransfer;

public class ChatView extends JPanel {
	private static final long serialVersionUID = 2127330778318842406L;
	private JTabbedPane tabbedPane;

	private Hashtable<Integer, ChatConversationView> conversations;
	private Client client;
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	/**
	 * Create the panel.
	 */
	public ChatView() {
		this.conversations = new Hashtable<Integer, ChatConversationView>();
		
		this.tabbedPane = new JTabbedPane(SwingConstants.TOP);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.tabbedPane, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(this.tabbedPane, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
					.addContainerGap())
		);
		setLayout(groupLayout);
	}
	
	public void updateChat(ChatTransfer trans) {
		if(trans instanceof ScreenShotTransfer) {
			BufferedImage image = ((ScreenShotTransfer) trans).getImage();
			conversations.get(trans.getPortSender()).addMessage(trans.getDateTime(), new JLabel(new ImageIcon(image)), false);
		} else {
			conversations.get(trans.getPortSender()).addMessage(trans.getDateTime(), new JLabel(trans.getMessage()), false);
		}
	}
	
	public void addConversation(int port) {
		ChatConversationView convo = new ChatConversationView(client, port);
		convo.setBackground(Color.WHITE);
		
		conversations.put(port, convo);
		this.tabbedPane.addTab(LogicalClientsContainer.getInstance().get(port).getName(), convo);
	}
	
	public void removeConversation(int port) {
		ChatConversationView convo = conversations.remove(port);
		this.tabbedPane.remove(convo);
	}
	
}
