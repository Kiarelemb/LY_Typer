package ly.qr.kiarelemb.res;

import method.qr.kiarelemb.utils.QRSystemUtils;

import javax.swing.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import static java.io.File.separator;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-12 23:04
 **/
public class Info {
	public static final String ARTICLES_DIRECTORY = "res" + separator + "articles" + separator;
	public static final String PICK_DIRECTORY = "res" + separator + "pick" + separator;
	public static final String THEME_DIRECTORY = "theme" + separator;
	public static final String TMP_DIRECTORY = "tmp" + separator;
	/**
	 * 存放当量的文件夹
	 */
	public static final String DL_DIRECTORY = "dl" + separator;
	public static final String SOFTWARE_VERSION = "v24.04";
	public static final String SYSTEM_NAME = QRSystemUtils.getSystemName();
	//    public static final boolean isWindows = SYSTEM_NAME.contains("Windows");
	public static final boolean IS_WINDOWS = SYSTEM_NAME.contains("Windows");
	public static final boolean IS_MAC = SYSTEM_NAME.toLowerCase().contains("mac");
	public static final String FLASH_PATH = PICK_DIRECTORY + "flash.png";
	public static final String ICON_PNG_PATH = "icon.png";
	public static final String ICON_TRAY_PATH = "tray.png";
	public static final String SPEED_ICON = "speed.png";
	public static final String KEYSTROKE_PNG = "keystroke.png";
	public static final String CODE_LEN_PNG = "codeLen.png";
	public static final String STANDARD_LEN_PNG = "standardLen.png";
	public static final String TIME_PNG = "time.png";

	public static final String LCD_FONT_NAME = "DigitalNumbers.ttf";
	public static final String ALIBABA_FONT_NAME = "alibaba.ttf";

	/**
	 * 加载资源
	 *
	 * @param fileName 文件名
	 * @return ImageIcon
	 */
	public static ImageIcon loadImage(String fileName) {
		return new ImageIcon(Objects.requireNonNull(Info.class.getResource(fileName)));
	}

	/**
	 * 加载资源
	 *
	 * @param fileName 文件名
	 * @return URL
	 */
	public static URL loadURL(String fileName) {
		return Objects.requireNonNull(Info.class.getResource(fileName));
	}

	/**
	 * 加载资源
	 *
	 * @param fileName 文件名
	 * @return URI
	 */
	public static URI loadURI(String fileName) {
		try {
			URL url = Objects.requireNonNull(Info.class.getResource(fileName));
			return url.toURI();
		} catch (URISyntaxException e) {
			return URI.create(".");
		}
	}
}