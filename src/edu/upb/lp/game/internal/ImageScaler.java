package edu.upb.lp.game.internal;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * High-quality image resampling. Downscaling is done in successive halving
 * steps to avoid aliasing; upscaling uses a single bicubic pass.
 */
final class ImageScaler {

	private ImageScaler() {
	}

	static BufferedImage scale(BufferedImage src, int targetWidth, int targetHeight) {
		targetWidth = Math.max(1, targetWidth);
		targetHeight = Math.max(1, targetHeight);

		BufferedImage current = src;
		int w = src.getWidth();
		int h = src.getHeight();

		// Halve progressively while the image is more than twice the target size
		while (w / 2 >= targetWidth || h / 2 >= targetHeight) {
			w = Math.max(targetWidth, w / 2);
			h = Math.max(targetHeight, h / 2);
			current = resize(current, w, h, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		}

		if (w != targetWidth || h != targetHeight || current == src) {
			current = resize(current, targetWidth, targetHeight, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		}

		return current;
	}

	private static BufferedImage resize(BufferedImage src, int w, int h, Object interpolation) {
		BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = result.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2.drawImage(src, 0, 0, w, h, null);
		g2.dispose();
		return result;
	}
}
