package me.lukasdietrich.client;

import java.awt.Color;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.MatteBorder;

public class ChatEntryView extends JPanel {
	private static final long serialVersionUID = 8633198664930205614L;
	private JLabel lblDateTime;
	private JLabel lblMessage;

	/**
	 * Create the panel.
	 */
	public ChatEntryView(String dateTime, JLabel message, boolean isSelf) {
		setBorder(new MatteBorder(0, 0, 1, 0, new Color(105, 105, 105)));
		setBackground(Color.WHITE);
		if(!isSelf)
			setBackground(new Color(220, 220, 220));
		this.lblDateTime = new JLabel(dateTime);
		this.lblMessage = message;
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(this.lblDateTime, GroupLayout.DEFAULT_SIZE, 377, Short.MAX_VALUE)
						.addComponent(this.lblMessage, GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(this.lblDateTime)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(this.lblMessage)
					.addGap(5))
		);
		setLayout(groupLayout);

	}
}
