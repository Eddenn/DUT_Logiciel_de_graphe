package model;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class ResizeImage {
	private BufferedImage original;
	private BufferedImage scaled;

	public ResizeImage(BufferedImage biOriginalImage, String strImagePath, int iWidth, int iHeight) {
		try {
			original = biOriginalImage;
			scaled = getScaledInstanceToFit(original, new Dimension(iWidth, iHeight));

			ImageIO.write(scaled, "png", new File(strImagePath + "_tn.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public BufferedImage getScaledInstanceToFit(BufferedImage img, Dimension size) {
		float scaleFactor = getScaleFactorToFit(img, size);
		return getScaledInstance(img, scaleFactor);
	}

	public float getScaleFactorToFit(BufferedImage img, Dimension size) {
		float scale = 1f;
		if (img != null) {
			int imageWidth = img.getWidth();
			int imageHeight = img.getHeight();
			scale = getScaleFactorToFit(new Dimension(imageWidth, imageHeight), size);
		}
		return scale;
	}

	public float getScaleFactorToFit(Dimension original, Dimension toFit) {
		float scale = 1f;
		if (original != null && toFit != null) {
			float dScaleWidth = getScaleFactor(original.width, toFit.width);
			float dScaleHeight = getScaleFactor(original.height, toFit.height);
			scale = Math.min(dScaleHeight, dScaleWidth);
		}
		return scale;
	}

	public float getScaleFactor(int iMasterSize, int iTargetSize) {
		float scale = 1;
		if (iMasterSize > iTargetSize) {
			scale = (float) iTargetSize / (float) iMasterSize;
		} else {
			scale = (float) iTargetSize / (float) iMasterSize;
		}
		return scale;
	}

	public BufferedImage getScaledInstance(BufferedImage img, double dScaleFactor) {
		BufferedImage imgBuffer = null;
		imgBuffer = getScaledInstance(img, dScaleFactor, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
		return imgBuffer;
	}

	protected BufferedImage getScaledInstance(BufferedImage img, double dScaleFactor, Object hint,
			boolean higherQuality) {

		int targetWidth = (int) Math.round(img.getWidth() * dScaleFactor);
		int targetHeight = (int) Math.round(img.getHeight() * dScaleFactor);

		int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB
				: BufferedImage.TYPE_INT_ARGB;

		BufferedImage ret = (BufferedImage) img;

		if (targetHeight > 0 || targetWidth > 0) {
			int w, h;
			if (higherQuality) {
				w = img.getWidth();
				h = img.getHeight();
			} else {
				w = targetWidth;
				h = targetHeight;
			}

			do {
				if (higherQuality && w > targetWidth) {
					w /= 2;
					if (w < targetWidth) {
						w = targetWidth;
					}
				}

				if (higherQuality && h > targetHeight) {
					h /= 2;
					if (h < targetHeight) {
						h = targetHeight;
					}
				}

				BufferedImage tmp = new BufferedImage(Math.max(w, 1), Math.max(h, 1), type);
				Graphics2D g2 = tmp.createGraphics();
				g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
				g2.drawImage(ret, 0, 0, w, h, null);
				g2.dispose();

				ret = tmp;
			} while (w != targetWidth || h != targetHeight);
		} else {
			ret = new BufferedImage(1, 1, type);
		}
		return ret;
	}
}
