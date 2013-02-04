package me.lukasdietrich.client;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;

import me.lukasdietrich.Strings;
import me.lukasdietrich.client.upload.UploadActivity;
import me.lukasdietrich.client.upload.UploadEntry;
import me.lukasdietrich.commons.Logger;
import me.lukasdietrich.transfer.ChatTransfer;
import me.lukasdietrich.transfer.ScreenShotTransfer;

public class ClientActivity extends JFrame {
	
	private static final long serialVersionUID = -2105934333710610496L;
	private JLabel lblPort;
	private JTextField txtFile;
	private JButton btnFile;
	private JButton btnSend;
	private JLabel lblCount;
	private Client client;
	private JLabel lblTask;
	private JButton btnScreenShot;
	private ChatView chatView;
	private UploadActivity uploadActivity;
	private ServerClientActivity serverActivity;

	public void setConnectedCount(int i) {
		lblCount.setText(Strings.getString(ClientActivity.class, "0")+ i);
	}
	
	public void addPortToChat(int port) {
		chatView.addConversation(port);
	}
	
	public void removePortFromChat(int port) {
		chatView.removeConversation(port);
	}
	
	public void setPort(int i) {
		lblPort.setText(Strings.getString(ClientActivity.class, "1")+ i);
	}
	
	public void updateChat(ChatTransfer transfer) {
		chatView.updateChat(transfer);
	}
	
	public void setClient(Client client) {
		this.client = client;
		this.chatView.setClient(client);
		this.uploadActivity.setClient(client);
		
		if(serverActivity != null)
			serverActivity.setClient(client);
	}
	
	public ClientActivity(final boolean isHost) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(ClientActivity.class.getResource(Strings.getString(ClientActivity.class, "2"))));
		this.setTitle(Strings.getString(ClientActivity.class, "3"));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.lblPort = new JLabel(Strings.getString(ClientActivity.class, "4"));
		this.txtFile = new JTextField(System.getProperty(Strings.getString(ClientActivity.class, "5")));
		this.txtFile.setPreferredSize(new Dimension(350, 20));
		this.txtFile.setEditable(false);
		this.btnFile = new JButton(Strings.getString(ClientActivity.class, "6"));
		this.btnFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				/**
				 * @author Lukas Dietrich
				 * TODO
				 */
				
				JFileChooser jfc = new JFileChooser(txtFile.getText());
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.setFileHidingEnabled(true);
				
				if(jfc.showOpenDialog(ClientActivity.this) == JFileChooser.APPROVE_OPTION) {
					txtFile.setText(jfc.getSelectedFile().getAbsolutePath());
				}
			}
		});
		this.btnSend = new JButton(Strings.getString(ClientActivity.class, "7"));
		this.btnSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				/**
				 * @author Lukas Dietrich
				 * TODO
				 */
				
				File file = new File(txtFile.getText());
				if(file.exists() && file.isFile() && file.canRead()) {
					SelectionActivity selection = new SelectionActivity(ClientActivity.this);
					selection.setVisible(true);
					
					int[] ports = selection.getSelectedPortsAsArray();
					
					if(ports.length > 0) {
						uploadActivity.setVisible(true);
						uploadActivity.addEntry(new UploadEntry(file, ports));
					}
					
				} else {
					JOptionPane.showMessageDialog(ClientActivity.this, "Datei existiert nicht\noder kann nicht gelesen werden!", "Fehler!", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		this.lblCount = new JLabel(Strings.getString(ClientActivity.class, "8"));
		this.lblTask = new JLabel();
		this.btnScreenShot = new JButton(Strings.getString(ClientActivity.class, "9"));
		this.btnScreenShot.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				toBack();
				try {
					BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
					
					toFront();
					
					SelectionActivity selection = new SelectionActivity(ClientActivity.this);
					selection.setVisible(true);
					
					int[] ports = selection.getSelectedPortsAsArray();
					
					client.sendObject(new ScreenShotTransfer(client.getPort(), image, ports));
				} catch (Exception e) {
					Logger.get().err(e, ClientActivity.this);
				} finally {
					toFront();
				}
			}
		});
		this.chatView = new ChatView();
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(this.chatView, GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(10)
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(this.lblTask)
									.addPreferredGap(ComponentPlacement.RELATED, 399, Short.MAX_VALUE)
									.addComponent(this.lblCount)
									.addGap(18)
									.addComponent(this.lblPort))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(this.btnScreenShot)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(this.txtFile, GroupLayout.DEFAULT_SIZE, 311, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(this.btnFile)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(this.btnSend)))))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.txtFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(this.btnSend)
						.addComponent(this.btnFile)
						.addComponent(this.btnScreenShot))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(this.chatView, GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(this.lblPort)
						.addComponent(this.lblCount)
						.addComponent(this.lblTask))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		
		this.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(isHost) {
					String[] options = new String[] {Strings.getString(ClientActivity.class, "12"), Strings.getString(ClientActivity.class, "13"), Strings.getString(ClientActivity.class, "14")};
					int i = JOptionPane.showOptionDialog(null, Strings.getString(ClientActivity.class, "15"), Strings.getString(ClientActivity.class, "16"), 0, 0, null, options, options[1]);
					
					switch(i) {
						case 0: // Alles beenden 
						{
							System.exit(0);
						} break;
						
						case 1: // nur Fenster schlieﬂen
						{
							setVisible(false);
						} break;
						
						default: // "abbrechen", "x" oder anderes
							return;
					}
				} else {
					dispose();
					client.close();
				}
			}
			
		});
		
		this.uploadActivity = new UploadActivity(this);
		
		if(isHost) {
			serverActivity = new ServerClientActivity(ClientActivity.this);
			serverActivity.setVisible(true);
		}
		
		this.pack();
		this.validate();
		this.setMinimumSize(this.getSize());
	}
}
