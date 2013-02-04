package me.lukasdietrich.client.upload;

import java.io.File;
import java.io.IOException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import me.lukasdietrich.client.Client;
import me.lukasdietrich.commons.Logger;
import me.lukasdietrich.commons.ProgressListener;
import me.lukasdietrich.transfer.FileTransfer;

public class UploadEntry extends JPanel implements Runnable {
	private static final long serialVersionUID = -2860806113193235029L;
	private JProgressBar progressBar;
	private JLabel lblStatus;
	private JLabel lblFile;
	private JLabel lblSize;
	private UploadActivity activity;
	private Client client;
	
	private File file;
	private int[] receiver;
	
	/**
	 * Create the panel.
	 */
	public UploadEntry(File file, int... ports) {
		this.file = file;
		this.receiver = ports;
		
		this.progressBar = new JProgressBar();
		this.lblStatus = new JLabel("einlesen...");
		this.lblFile = new JLabel(file.getAbsolutePath());
		this.lblSize = new JLabel("500mb");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(this.progressBar, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.lblFile, GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(this.lblSize, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(this.lblStatus, GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
							.addGap(87)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblFile)
						.addComponent(this.lblSize))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(this.progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
					.addComponent(this.lblStatus)
					.addContainerGap())
		);
		setLayout(groupLayout);
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public void setActivity(UploadActivity activity) {
		this.activity = activity;
	}

	@Override
	public void run() {
		ProgressListener listener = new ProgressListener() {
			
			@Override
			public void progressDone(boolean success, Object source) {
				if(success) {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							progressBar.setValue(100);
							progressBar.setIndeterminate(true);
							
							lblStatus.setText("lade hoch...");
							
							activity.repaint();
						}
						
					});
					
					try {
						client.sendObject((FileTransfer) source);
						lblStatus.setText("fertig.");
						
						activity.removeEntry(UploadEntry.this);
						
					} catch (IOException e) {
						Logger.get().err(e, UploadEntry.this);
						lblStatus.setText("etwas ist schief gelaufen :(");
					}
				}
			}
			
			@Override
			public boolean percentChanged(final int percent, String... args) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						progressBar.setValue(percent);
						lblStatus.setText("einlesen "+ percent +"%...");
						activity.repaint();
					}
					
				});
				
				return true;
			}
		};
		
		@SuppressWarnings("unused")
		FileTransfer transfer = new FileTransfer(client.getPort(), file, listener, receiver);
	}
	
}
