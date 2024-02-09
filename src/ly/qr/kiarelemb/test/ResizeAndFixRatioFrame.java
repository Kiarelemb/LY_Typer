package ly.qr.kiarelemb.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ResizeAndFixRatioFrame extends JFrame {

	private int direction = 0;
	private Point startPoint = null;
	private int width;
	private int height;

	public ResizeAndFixRatioFrame(String title) {
		super(title);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = (JPanel) getContentPane();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				ResizeAndFixRatioFrame.this.startPoint = e.getPoint();
				ResizeAndFixRatioFrame.this.width = getWidth();
				ResizeAndFixRatioFrame.this.height = getHeight();
			}
		});
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				int borderWidth = getWidth() - getContentPane().getWidth() + 5;
				int borderHeight = getHeight() - getContentPane().getHeight() + 5;
				System.out.println(borderWidth + "--" + borderHeight);
				if (x >= 0 && x <= borderWidth && y >= 0 && y <= borderHeight) {
					if (x <= 5 && y <= 5) {
						setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
						ResizeAndFixRatioFrame.this.direction = 1;
					} else if (x >= ResizeAndFixRatioFrame.this.width - 5 - borderWidth && y <= 5) {
						setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
						ResizeAndFixRatioFrame.this.direction = 2;
					} else if (x >= ResizeAndFixRatioFrame.this.width - 5 - borderWidth && y >= ResizeAndFixRatioFrame.this.height - 5 - borderHeight) {
						setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
						ResizeAndFixRatioFrame.this.direction = 3;
					} else if (x <= 5 && y >= ResizeAndFixRatioFrame.this.height - 5 - borderHeight) {
						setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
						ResizeAndFixRatioFrame.this.direction = 4;
					} else if (x <= 5) {
						setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
						ResizeAndFixRatioFrame.this.direction = 5;
					} else if (x >= ResizeAndFixRatioFrame.this.width - 5 - borderWidth) {
						setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
						ResizeAndFixRatioFrame.this.direction = 6;
					} else if (y <= 5) {
						setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
						ResizeAndFixRatioFrame.this.direction = 7;
					} else if (y >= ResizeAndFixRatioFrame.this.height - 5 - borderHeight) {
						setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
						ResizeAndFixRatioFrame.this.direction = 8;
					} else {
						setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
						ResizeAndFixRatioFrame.this.direction = 0;
					}
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point endPoint = e.getPoint();
				int deltaX = endPoint.x - ResizeAndFixRatioFrame.this.startPoint.x;
				int deltaY = endPoint.y - ResizeAndFixRatioFrame.this.startPoint.y;
				switch (ResizeAndFixRatioFrame.this.direction) {
					case 1:
						setBounds(getX() + deltaX, getY() + deltaY, ResizeAndFixRatioFrame.this.width - deltaX, ResizeAndFixRatioFrame.this.height - deltaY);
						break;
					case 2:
						setBounds(getX(), getY() + deltaY, ResizeAndFixRatioFrame.this.width + deltaX, ResizeAndFixRatioFrame.this.height - deltaY);
						break;
					case 3:
						setBounds(getX(), getY(), ResizeAndFixRatioFrame.this.width + deltaX, ResizeAndFixRatioFrame.this.height + deltaY);
						break;
					case 4:
						setBounds(getX() + deltaX, getY(), ResizeAndFixRatioFrame.this.width - deltaX, ResizeAndFixRatioFrame.this.height + deltaY);
						break;
					case 5:
						setBounds(getX() + deltaX, getY(), ResizeAndFixRatioFrame.this.width - deltaX, ResizeAndFixRatioFrame.this.height);
						break;
					case 6:
						setBounds(getX(), getY(), ResizeAndFixRatioFrame.this.width + deltaX, ResizeAndFixRatioFrame.this.height);
						break;
					case 7:
						setBounds(getX(), getY() + deltaY, ResizeAndFixRatioFrame.this.width, ResizeAndFixRatioFrame.this.height - deltaY);
						break;
					case 8:
						setBounds(getX(), getY(), ResizeAndFixRatioFrame.this.width, ResizeAndFixRatioFrame.this.height + deltaY);
						break;
					default:
						break;
				}
				Rectangle bounds = getBounds();
				if (bounds.width * 3 != bounds.height * 4) {
					int newWidth = bounds.height * 4 / 3;
					int newHeight = bounds.width * 3 / 4;
					int x = getX();
					int y = getY();
					switch (ResizeAndFixRatioFrame.this.direction) {
						case 1:
							x += bounds.width - newWidth;
							y += bounds.height - newHeight;
							break;
						case 2:
							y += bounds.height - newHeight;
							break;
						case 4:
							x += bounds.width - newWidth;
							break;
						default:
							break;
					}
					setBounds(x, y, newWidth, newHeight);
				}
			}
		});
	}

	public static void main(String[] args) {
		ResizeAndFixRatioFrame frame = new ResizeAndFixRatioFrame("Resize And Fix Ratio Frame");
		frame.setBounds(100, 100, 300, 225);
		frame.setVisible(true);
	}
}
