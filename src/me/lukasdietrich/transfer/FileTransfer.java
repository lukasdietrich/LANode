package me.lukasdietrich.transfer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import me.lukasdietrich.client.LogicalClientsContainer;
import me.lukasdietrich.client.NotificationActivity;
import me.lukasdietrich.commons.ProgressListener;

public class FileTransfer extends Transfer implements Runnable {
	private static final long serialVersionUID = -3605465130455061019L;

	private byte[] content;
	private String filename;
	
	private transient int i, j;
	private transient int lp = 0;
	
	public FileTransfer(int portSender, final File file, final ProgressListener listener, int... portReceiver) {
		super(portSender, portReceiver);
	
		this.filename = file.getName();
		
		content = new byte[(int) file.length()];
		
		new Thread() {
			
			@Override
			public void run() {
				 try {
					FileInputStream fin = new FileInputStream(file);
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					
					byte[] buffer = new byte[1024];
					i = 0;
					
					while((i = fin.read(buffer)) > 0) {
						bout.write(buffer, 0, i);
						
						j += i;
						
						SwingUtilities.invokeLater(new Runnable() {

							@Override
							public void run() {
								int np = (int)(((double)j/content.length)*100);
								
								if(np != lp)
									listener.percentChanged(lp = np);
							}
							
						});
						
					}
					
					content = bout.toByteArray();
					
					bout.close();
					fin.close();
					listener.progressDone(true, FileTransfer.this);
				} catch (FileNotFoundException e) {
					listener.progressDone(false, FileTransfer.this);
				} catch (IOException e) {
					listener.progressDone(false, FileTransfer.this);
				}
			}
			
		}.start();
       
	}
	
	public String getFileName() {
		return this.filename;
	}
	
	public void store() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		
		NotificationActivity notification = new NotificationActivity(new NotificationActivity.NotificationClicked() {
			
			@Override
			public void clicked() {
				
				try {
					JFileChooser jfc = new JFileChooser();
					jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
					jfc.setSelectedFile(new File(filename));
					
					if(jfc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						FileOutputStream fileOutputStream = new FileOutputStream(jfc.getSelectedFile());
						fileOutputStream.write(content);
						fileOutputStream.close();
					}
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

			@Override
			public void dismissed() {
				// TODO Auto-generated method stub
				
			}
			
		}, LogicalClientsContainer.getInstance().get(getPortSender()).getName(), filename, content.length/1024.0 +"kb"); //$NON-NLS-1$
		
		notification.setVisible(true);
		
	}

}
