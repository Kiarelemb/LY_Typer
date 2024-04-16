package ly.qr.kiarelemb.text.send;

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

	public static boolean loadSerializedData(String fileName) {
		String binPath = Info.TYPE_DIRECTORY + fileName + ".bin";
		try {
			data = (TypedData) QRSerializeUtils.readObject(binPath);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}