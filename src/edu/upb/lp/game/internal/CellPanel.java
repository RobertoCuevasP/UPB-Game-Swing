package edu.upb.lp.game.internal;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 *
 * @author Alexis Marechal
 * @author Roberto Cuevas
 */

public class CellPanel extends JPanel {

	private static final long serialVersionUID = 749476481296507838L;
	private transient BufferedImage backgroundImage;
	private transient BufferedImage objectImage;
	private String text = "";
	private final boolean showBorder;

	// Images already resampled to the exact device-pixel size they are drawn at
	private transient BufferedImage scaledBackground;
	private transient BufferedImage scaledObject;

	public CellPanel(boolean showBorder) {
		this.showBorder = showBorder;
		setPreferredSize(new Dimension(80, 80));
		setOpaque(false);
		setLayout(null);
	}

	public void setBackgroundImage(BufferedImage image) {
		if (this.backgroundImage != image) {
			this.backgroundImage = image;
			this.scaledBackground = null;
			repaint();
		}
	}

	public void setObjectImage(BufferedImage image) {
		if (this.objectImage != image) {
			this.objectImage = image;
			this.scaledObject = null;
			repaint();
		}
	}

	public void clearObjectIcon() {
		setObjectImage(null);
	}

	public void setCellText(String text) {
		this.text = text != null ? text : "";
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		int width = getWidth();
		int height = getHeight();

		// Device scale (HiDPI): draw at physical pixel resolution
		AffineTransform tx = g2.getTransform();
		double sx = tx.getScaleX();
		double sy = tx.getScaleY();
		int deviceWidth = (int) Math.round(width * sx);
		int deviceHeight = (int) Math.round(height * sy);

		if (backgroundImage != null) {
			if (scaledBackground == null || scaledBackground.getWidth() != deviceWidth
					|| scaledBackground.getHeight() != deviceHeight) {
				scaledBackground = ImageScaler.scale(backgroundImage, deviceWidth, deviceHeight);
			}
			drawUnscaled(g2, scaledBackground, 0, 0);
		}

		if (objectImage != null) {
			// Fit inside the cell keeping the aspect ratio
			double ratio = Math.min((double) deviceWidth / objectImage.getWidth(),
					(double) deviceHeight / objectImage.getHeight());
			int objectWidth = Math.max(1, (int) Math.round(objectImage.getWidth() * ratio));
			int objectHeight = Math.max(1, (int) Math.round(objectImage.getHeight() * ratio));

			if (scaledObject == null || scaledObject.getWidth() != objectWidth
					|| scaledObject.getHeight() != objectHeight) {
				scaledObject = ImageScaler.scale(objectImage, objectWidth, objectHeight);
			}
			drawUnscaled(g2, scaledObject, (deviceWidth - objectWidth) / 2, (deviceHeight - objectHeight) / 2);
		}

		if (!text.trim().isEmpty()) {
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setColor(Color.WHITE);
			g2.setFont(new Font("Arial", Font.BOLD, 18));

			FontMetrics fm = g2.getFontMetrics();
			int textWidth = fm.stringWidth(text);
			int textHeight = fm.getAscent();

			int x = (width - textWidth) / 2;
			int y = (height + textHeight) / 2;

			g2.drawString(text, x, y);
		}

		// Borde de la celda
		if (showBorder) {
			g2.setColor(Color.BLACK);
			g2.drawRect(0, 0, width - 1, height - 1);
		}
	}

	/**
	 * Draws an image 1:1 in device pixels so Swing does not resample it again.
	 * x and y are in device pixels relative to this panel's origin.
	 */
	private static void drawUnscaled(Graphics2D g2, BufferedImage image, int x, int y) {
		AffineTransform saved = g2.getTransform();
		g2.setTransform(new AffineTransform(1, 0, 0, 1, Math.round(saved.getTranslateX()),
				Math.round(saved.getTranslateY())));
		g2.drawImage(image, x, y, null);
		g2.setTransform(saved);
	}
}
