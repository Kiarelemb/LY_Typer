package ly.qr.kiarelemb.qq;

import ly.qr.kiarelemb.MainWindow;
import ly.qr.kiarelemb.component.contract.state.DiveCheckBox;
import ly.qr.kiarelemb.component.contract.state.GroupButton;
import ly.qr.kiarelemb.data.Keys;
import ly.qr.kiarelemb.res.Info;
import method.qr.kiarelemb.utils.QRStringUtils;
import method.qr.kiarelemb.utils.QRSystemUtils;

/**
 * @author Kiarelemb QR
 * @date 2021/11/14 23:08
 * @apiNote
 */
public class SendText {

    /**
     * 加密或解密文本
     *
     * @param text     文本
     * @param encoding 为 <code>true</code> 则加密，否则则是解密
     * @return 操作后的文本
     */
    public static String textCoding(String text, boolean encoding) {
        if (QRStringUtils.markCount(text, QRStringUtils.AN_ENTER_CHAR) == 2) {
            String[] split = text.split(QRStringUtils.AN_ENTER);
            String content = split[1];
            char[] chars = content.toCharArray();
            if (encoding) {
                for (int i = 0, maxLen = chars.length; i < maxLen; i++) {
                    chars[i] += i + 2;
                }
                split[0] = "\u200B" + split[0];
            } else {
                if (text.charAt(0) != '\u200B') {
                    return text;
                }
                for (int i = 0, maxLen = chars.length; i < maxLen; i++) {
                    chars[i] -= i + 2;
                }
                split[0] = split[0].substring(1);
            }
            return split[0] + QRStringUtils.AN_ENTER + new String(chars) + QRStringUtils.AN_ENTER + split[2];
        }
        return text;
    }

    /**
     * 发送成绩
     */
    public static void gradeSend() {
//        tpe.execute(() -> {
        if (!Info.IS_WINDOWS || !GroupButton.groupBtn.groupLinked()) {
            return;
        }
        if (DiveCheckBox.diveCheckBox.checked()) {
            return;
        }
        //TODO 如果有发送成绩的限制
//		if (ssd.gradeSendLimit()) {
//			if (gd.speed() >= ssd.speedLimit() && gd.keyStroke() >= ssd.keyStrokeLimit() && gd.keyAccuracyNum() - ssd.accuracyLimit() > -0.1) {
//				//发送成绩
//				QqOperation.start(QqOperation.SEND_ACHIEVEMENT_MODEL, MainWindow.currentGroupName);
//			}
//		} else {
        //发送成绩
        QqOperation.start(QqOperation.SEND_ACHIEVEMENT_MODEL, GroupButton.groupBtn.groupName());
//		}
        MainWindow.INSTANCE.grabFocus();
//        });
    }

    /**
     * 这方法一般与小R模式有关
     *
     * @param message 要发送的文本
     */
    public static void sendMessage(String message) {
        if (QqOperation.textCanSend()) {
            QRSystemUtils.putTextToClipboard(message);
            QqOperation.start(QqOperation.SEND_ACHIEVEMENT_MODEL, GroupButton.groupBtn.groupName());
        }
    }

    public static void sendMessage(MainWindow w, String texts) {
        if (Keys.boolValue(Keys.SEND_CRYPTOGRAPHIC)) {
            texts = textCoding(texts, true);
        }
        QRSystemUtils.putTextToClipboard(texts);
        QqOperation.start(QqOperation.SEND_ACHIEVEMENT_MODEL, GroupButton.groupBtn.groupName());
    }
}
