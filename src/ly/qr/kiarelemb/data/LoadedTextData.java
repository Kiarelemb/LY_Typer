package ly.qr.kiarelemb.data;

import method.qr.kiarelemb.utils.QRStringUtils;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-25 15:00
 **/
public record LoadedTextData(String time, String user, String foreText, String content, String endText) {
	public String text() {
		return foreText + QRStringUtils.AN_ENTER + content + QRStringUtils.AN_ENTER + endText;
	}
}
