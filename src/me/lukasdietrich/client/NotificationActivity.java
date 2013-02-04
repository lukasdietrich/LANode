package me.lukasdietrich.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import me.lukasdietrich.Strings;

public class NotificationActivity extends JDialog {
	private static final long serialVersionUID = 1103080732549930421L;
	private JLabel lblHead;
	private JLabel lblNameTag;
	private JLabel lblSizeTag;
	private JLabel lblName;
	private JLabel lblSize;
	private JLabel lblDismiss;
	private JLabel lblSave;
	private JLabel lblByTag;
	private JLabel lblBy;

	/**
	 * Create the dialog.
	 */
	public NotificationActivity(final NotificationClicked clicked, String user, String name, String size) {
		getContentPane().setBackground(Color.DARK_GRAY);
		this.lblHead = new JLabel(Strings.getString(NotificationActivity.class, "0"));
		this.lblHead.setFont(new Font(Strings.getString(NotificationActivity.class, "1"), Font.BOLD, 14));
		this.lblHead.setForeground(Color.WHITE);
		this.lblNameTag = new JLabel(Strings.getString(NotificationActivity.class, "2"));
		this.lblNameTag.setFont(new Font(Strings.getString(NotificationActivity.class, "3"), Font.ITALIC, 11));
		this.lblNameTag.setForeground(Color.WHITE);
		this.lblSizeTag = new JLabel(Strings.getString(NotificationActivity.class, "4"));
		this.lblSizeTag.setFont(new Font("Tahoma", Font.ITALIC, 11));
		this.lblSizeTag.setForeground(Color.WHITE);
		this.lblName = new JLabel(name);
		this.lblName.setForeground(Color.WHITE);
		this.lblSize = new JLabel(size);
		this.lblSize.setForeground(Color.WHITE);
		
		this.lblDismiss = new JLabel(Strings.getString(NotificationActivity.class, "6"));
		this.lblDismiss.setFont(new Font("Dialog", Font.BOLD, 14));
		this.lblDismiss.setBorder(new EmptyBorder(5, 8, 5, 8));
		this.lblDismiss.setBackground(new Color(105, 105, 105));
		this.lblDismiss.setForeground(new Color(178, 34, 34));
		
		this.lblSave = new JLabel(Strings.getString(NotificationActivity.class, "8"));
		this.lblSave.setForeground(new Color(154, 205, 50));
		this.lblSave.setFont(new Font("Dialog", Font.BOLD, 14));
		this.lblSave.setBorder(new EmptyBorder(5, 8, 5, 8));
		this.lblSave.setBackground(SystemColor.controlDkShadow);
		this.lblByTag = new JLabel(Strings.getString(NotificationActivity.class, "10"));
		this.lblByTag.setFont(new Font("Tahoma", Font.ITALIC, 11));
		this.lblByTag.setForeground(Color.WHITE);
		this.lblBy = new JLabel(user);
		this.lblBy.setFont(new Font("Tahoma", Font.ITALIC, 11));
		this.lblBy.setForeground(Color.WHITE);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.lblSizeTag)
								.addComponent(this.lblNameTag)
								.addComponent(this.lblByTag))
							.addGap(59)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(this.lblSize, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(this.lblName, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 131, Short.MAX_VALUE)
								.addComponent(this.lblBy, GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE)))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.lblHead, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
							.addGap(189)
							.addComponent(this.lblSave)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(this.lblDismiss)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblDismiss, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(this.lblSave)
						.addComponent(this.lblHead))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
							.addComponent(this.lblByTag)
							.addGap(51))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(15)
							.addComponent(this.lblBy)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.lblNameTag)
								.addComponent(this.lblName))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(this.lblSizeTag)
								.addComponent(this.lblSize))
							.addContainerGap())))
		);
		getContentPane().setLayout(groupLayout);
		setBounds(0, 0, 360, 120);
		setUndecorated(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		
		lblSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				dispose();
				clicked.clicked();
			}
		});
		
		lblDismiss.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				dispose();
				clicked.dismissed();
			}
		});
	}
	
	public static interface NotificationClicked {
		public void clicked();
		public void dismissed();
	}
}
