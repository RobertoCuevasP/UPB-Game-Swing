package edu.upb.lp.game.internal;

import javax.swing.*;
import java.awt.*;

/**
 * 
 * @author Alexis Marechal
 * @author Roberto Cuevas
 */

public class CellPanel extends JPanel {

	private static final long serialVersionUID = 749476481296507838L;
	private Image backgroundImage;
	private Image objectImage;
	private String text = "";

	public CellPanel() {
		setPreferredSize(new Dimension(80, 80));
		setOpaque(false);
		setLayout(null);
	}

	public void setBackgroundIcon(ImageIcon icon) {
		this.backgroundImage = icon != null ? icon.getImage() : null;
		repaint();
	}

	public void setObjectIcon(ImageIcon icon) {
		this.objectImage = icon != null ? icon.getImage() : null;
		repaint();
	}

	public void clearObjectIcon() {
		this.objectImage = null;
		repaint();
	}

	public void setCellText(String text) {
		this.text = text != null ? text : "";
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int width = getWidth();
		int height = getHeight();

		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, width, height, this);
		}

		if (objectImage != null) {
			int objectSize = Math.min(width, height);

			int x = (width - objectSize) / 2;
			int y = (height - objectSize) / 2;

			g.drawImage(objectImage, x, y, objectSize, objectSize, this);
		}

		if (!text.isBlank()) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", Font.BOLD, 18));

			FontMetrics fm = g.getFontMetrics();
			int textWidth = fm.stringWidth(text);
			int textHeight = fm.getAscent();

			int x = (width - textWidth) / 2;
			int y = (height + textHeight) / 2;

			g.drawString(text, x, y);
		}

		// Borde de la celda
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
	}
}