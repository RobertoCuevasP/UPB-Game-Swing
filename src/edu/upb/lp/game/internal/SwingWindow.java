package edu.upb.lp.game.internal;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import edu.upb.lp.game.core.GameController;
import edu.upb.lp.game.core.GraphicsLibrary;
import edu.upb.lp.game.core.MessagesLibrary;

public class SwingWindow extends JFrame implements GraphicsLibrary, MessagesLibrary {

	/**
	 * 
	 */
	private static final long serialVersionUID = 156933677961172524L;

	private CellPanel[][] cells;

	private JPanel gridPanel;
	private JPanel buttonPanel;
	private JPanel infoPanel;

	private JLabel temporaryMessageLabel;

	private GameController controller;

	private final Map<String, JButton> buttons = new HashMap<>();
	private final Map<String, JLabel> labels = new HashMap<>();
	private final Map<String, BufferedImage> imageCache = new HashMap<>();

	public SwingWindow() {
		setTitle("UPBGame Swing Edition 2026");
		setSize(900, 760);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		infoPanel = new JPanel();
		buttonPanel = new JPanel();
		gridPanel = new JPanel();

		infoPanel.setLayout(new GridLayout(2, 4));
		buttonPanel.setLayout(new FlowLayout());
		gridPanel.setLayout(new GridLayout(8, 8, 0, 0));

		add(infoPanel, BorderLayout.NORTH);
		add(gridPanel, BorderLayout.CENTER);

		temporaryMessageLabel = new JLabel(" ");
		temporaryMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		temporaryMessageLabel.setFont(new Font("Arial", Font.BOLD, 14));

		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(temporaryMessageLabel, BorderLayout.NORTH);
		southPanel.add(buttonPanel, BorderLayout.SOUTH);

		add(southPanel, BorderLayout.SOUTH);

		setLocationRelativeTo(null);
		setVisible(true);
	}

	public void setController(GameController controller) {
		this.controller = controller;
	}

	@Override
	public void configureGrid(int rows, int cols, int sizeX, int sizeY, boolean showCellBorders) {
		gridPanel.removeAll();
		gridPanel.setLayout(new GridLayout(rows, cols));

		cells = new CellPanel[rows][cols];

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				CellPanel cellPanel = new CellPanel(showCellBorders);

				int currentRow = row;
				int currentCol = col;

				cellPanel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (controller != null) {
							controller.onCellPressed(currentRow, currentCol);
						}
					}
				});

				cells[row][col] = cellPanel;
				gridPanel.add(cellPanel);
			}
		}

		setSize(sizeX, sizeY);
		setLocationRelativeTo(null);

		revalidate();
		repaint();
	}

	@Override
	public void setCellText(int row, int col, String text) {
		if (isValidCell(row, col)) {
			cells[row][col].setCellText(text);
		}
	}

	@Override
	public void setCellBackgroundImage(int row, int col, String imageName) {
		if (isValidCell(row, col)) {
			cells[row][col].setBackgroundImage(loadImage(imageName));
		}
	}

	@Override
	public void setCellObjectImage(int row, int col, String imageName) {
		if (isValidCell(row, col)) {
			cells[row][col].setObjectImage(loadImage(imageName));
		}
	}

	@Override
	public void clearCellObjectImage(int row, int col) {
		if (isValidCell(row, col)) {
			cells[row][col].clearObjectIcon();
		}
	}

	@Override
	public void addButton(String name) {
		if (!buttons.containsKey(name)) {
			JButton button = new JButton(name);

			button.addActionListener(e -> {
				if (controller != null) {
					controller.onButtonPressed(name);
				}
			});

			buttons.put(name, button);
			buttonPanel.add(button);

			revalidate();
			repaint();
		}
	}

	@Override
	public void removeButton(String name) {
		JButton button = buttons.remove(name);

		if (button != null) {
			buttonPanel.remove(button);
			revalidate();
			repaint();
		}
	}

	@Override
	public void setLabel(String key, String value) {
		if (!labels.containsKey(key)) {
			JLabel label = new JLabel(value);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			labels.put(key, label);
			infoPanel.add(label);
		} else {
			labels.get(key).setText(value);
		}

		revalidate();
		repaint();
	}

	@Override
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}

	@Override
	public String askText(String title) {
		return JOptionPane.showInputDialog(this, title);
	}

	private BufferedImage loadImage(String imageName) {
		if (imageName == null || imageName.isBlank()) {
			return null;
		}

		if (imageCache.containsKey(imageName)) {
			return imageCache.get(imageName);
		}

		String[] extensions = { ".png", ".jpg", ".jpeg" };

		for (String ext : extensions) {
			String path = "/images/" + imageName + ext;

			java.net.URL resource = getClass().getResource(path);

			if (resource != null) {
				try {
					// Keep the original resolution; CellPanel scales it to the exact drawn size
					BufferedImage image = ImageIO.read(resource);

					if (image != null) {
						imageCache.put(imageName, image);
						return image;
					}
				} catch (java.io.IOException e) {
					System.out.println("Error reading image: " + path);
					e.printStackTrace();
				}
			}
		}

		System.out.println("Image not found in resources: " + imageName);
		return null;
	}

	private boolean isValidCell(int row, int col) {
		return cells != null && row >= 0 && row < cells.length && col >= 0 && col < cells[row].length;
	}

	@Override
	public void showTemporaryMessage(String msg) {
		temporaryMessageLabel.setText(msg);

		Timer timer = new Timer(2500, e -> temporaryMessageLabel.setText(" "));
		timer.setRepeats(false);
		timer.start();
	}
}