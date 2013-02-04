package me.lukasdietrich.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import me.lukasdietrich.LANode;
import me.lukasdietrich.commons.Network;
import me.lukasdietrich.commons.ProgressListener;

public class SearchHostActivity extends JDialog implements Runnable {
	private static final long serialVersionUID = 3227186002297993888L;
	private final JPanel contentPanel = new JPanel();
	private JScrollPane scrollPane;
	private JList list;
	private DefaultListModel model;
	private JProgressBar progressBar;

	private boolean cancel = false;
	
	/**
	 * Create the dialog.
	 */
	public SearchHostActivity(InitActivity initActivity) {
		super(initActivity);
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		this.contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(this.contentPanel, BorderLayout.CENTER);
		this.scrollPane = new JScrollPane();
		this.progressBar = new JProgressBar();
		GroupLayout gl_contentPanel = new GroupLayout(contentPanel);
		gl_contentPanel.setHorizontalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addComponent(this.progressBar, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
				.addComponent(this.scrollPane, GroupLayout.DEFAULT_SIZE, 424, Short.MAX_VALUE)
		);
		gl_contentPanel.setVerticalGroup(
			gl_contentPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPanel.createSequentialGroup()
					.addComponent(this.scrollPane, GroupLayout.DEFAULT_SIZE, 193, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(this.progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		{
			this.model = new DefaultListModel();
			this.list = new JList(model);
			this.scrollPane.setViewportView(this.list);
		}
		contentPanel.setLayout(gl_contentPanel);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnOk = new JButton("OK");
				btnOk.setEnabled(false);
				buttonPane.add(btnOk);
				getRootPane().setDefaultButton(btnOk);
			}
			{
				JButton btnCancel = new JButton("Abbrechen");
				btnCancel.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						cancel = true;
						((JButton) arg0.getSource()).setEnabled(false);
					}
				});
				buttonPane.add(btnCancel);
			}
		}
		
		new Thread(this).start();
	}

	@Override
	public void run() {
		Network.searchHosts(LANode.PORT, new ProgressListener() {

			@Override
			public boolean percentChanged(int percent, String... args) {
				progressBar.setValue(percent);
				
				if(args.length > 0)
					model.addElement(args[0]);
				
				return !cancel;
			}

			@Override
			public void progressDone(boolean success, Object source) {
				progressBar.setValue(100);
				if(!success)
					dispose();
			}
			
		});
	}
}
