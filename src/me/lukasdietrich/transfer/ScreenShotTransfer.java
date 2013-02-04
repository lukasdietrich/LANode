package me.lukasdietrich.transfer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import me.lukasdietrich.commons.Logger;

public class ScreenShotTransfer extends ChatTransfer {
	private static final long serialVersionUID = 6104569086051585062L;
	
	private byte[] image;
	
	public ScreenShotTransfer(int portSender, BufferedImage image, int[] ports) {
		super(portSender, null, ports);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		try {
			ImageIO.write(image, "png", stream);
			this.image = stream.toByteArray();
		} catch (IOException e) {
			Logger.get().err(e, this);
		} finally {
			try {
				stream.close();
			} catch (IOException e) {}
		}
	}
	
	public BufferedImage getImage() {
		try {
			return ImageIO.read(new ByteArrayInputStream(this.image));
		} catch (IOException e) {
			Logger.get().err(e, this);
			return null;
		}
	}

}
