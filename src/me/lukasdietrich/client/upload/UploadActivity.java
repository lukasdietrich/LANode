package me.lukasdietrich.client.upload;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import me.lukasdietrich.client.Client;

public class UploadActivity extends JDialog {
	private static final long serialVersionUID = -6706419747800939729L;
	private JScrollPane scrollPane;
	private DefaultListModel model;
	private JList list;
	private Client client;

	public UploadActivity(JFrame parent){
		super(parent);
		setTitle("UploadManager");
		
		this.setBounds(100, 100, 500, 400);
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		
		{
			this.scrollPane = new JScrollPane();
			getContentPane().add(this.scrollPane, BorderLayout.CENTER);
			{
				this.model = new DefaultListModel();
				this.list = new JList(model);
				this.list.setCellRenderer(new UploadEntryRenderer());
				this.scrollPane.setViewportView(this.list);
			}
		}
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public void addEntry(UploadEntry entry) {
		entry.setClient(this.client);
		entry.setActivity(this);
		this.model.addElement(entry);
		new Thread(entry).start();
	}
	
	public void removeEntry(UploadEntry entry) {
		this.model.removeElement(entry);
	}
	
	private class UploadEntryRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 8337410690382103875L;

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			return (UploadEntry) value;
		}
		
	}
	
}
