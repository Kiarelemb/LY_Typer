package ly.qr.kiarelemb.text.send;

import ly.qr.kiarelemb.component.TextPane;
import ly.qr.kiarelemb.component.menu.send.ContinueSendTextItem;
import ly.qr.kiarelemb.component.menu.send.ForeParaTextItem;
import ly.qr.kiarelemb.component.menu.send.NewSendTextItem;
import ly.qr.kiarelemb.component.menu.send.NextParaTextItem;
import ly.qr.kiarelemb.res.Info;
import ly.qr.kiarelemb.text.send.data.TypedData;
import method.qr.kiarelemb.utils.QRSerializeUtils;

import java.io.IOException;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TextSendManager
 * @description TODO
 * @create 2024/4/16 19:32
 */
public class TextSendManager {
	private static TypedData data;

	public static TypedData data() {
		return data;
	}

	public static void setData(TypedData data) {
		TextSendManager.data = data;
	}

	public static void save() {
		TextSendManager.serializeData(TextSendManager.data);
	}

	public static boolean sendingText() {
		return TextSendManager.data != null;
	}

	private static void serializeData(TypedData data) {
		String filePath = Info.TYPE_DIRECTORY + data.fileName() + ".bin";
		try {
			QRSerializeUtils.writeObject(filePath, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param fileName 带拓展名的文件名
	 * @return 是否加载成功
	 */
	public static boolean loadSerializedData(String fileName) {
		String binPath = Info.TYPE_DIRECTORY + fileName;
		try {
			data = (TypedData) QRSerializeUtils.readObject(binPath);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void setTypedData(TypedData data) {
		setData(data);
		TextPane.TEXT_PANE.setTypeText(data.nextParaText());
		TextSendManager.control(true);
		//TODO 在此处添加跟打结束事件
	}

	/**
	 * 结束发文
	 */
	public static void endSendText() {
		control(false);
	}

	private static void control(boolean enable) {
		NewSendTextItem.NEW_SEND_TEXT_ITEM.setEnabled(!enable);
		ContinueSendTextItem.CONTINUE_SEND_TEXT_ITEM.setEnabled(!enable);
		ForeParaTextItem.FORE_PARA_TEXT_ITEM.setEnabled(enable);
		NextParaTextItem.NEXT_PARA_TEXT_ITEM.setEnabled(enable);
	}
}