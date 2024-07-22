package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.res.Info;
import method.qr.kiarelemb.utils.QRFontUtils;
import swing.qr.kiarelemb.basic.QRLabel;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-21 23:15
 **/
public class LCDNumberLabel extends QRLabel {
	private static final Font LCD_FONT;

	static {
		URI uri = Info.loadURI(Info.LCD_FONT_NAME);
		if (uri != null) {
			try {
				LCD_FONT = QRFontUtils.loadFontFromFile(32, uri.toURL());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			LCD_FONT = null;
		}
	}

	public LCDNumberLabel() {
		super("0.0");
	}

	@Override
	public void setText(String text) {
		super.setText("Infinity".equals(text) || "Infinity/Infinity".equals(text) || "NaN".equals(text) ? "0.0" : text);
	}

	@Override
	public void clear() {
		setText("0.0");
	}

	@Override
	public void componentFresh() {
		super.componentFresh();
		setFont(LCD_FONT);
	}
}