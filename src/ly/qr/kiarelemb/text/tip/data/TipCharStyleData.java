package ly.qr.kiarelemb.text.tip.data;


import javax.swing.text.SimpleAttributeSet;

/**
 * @author Kiarelemb QR
 * @date 2021/8/26 上午9:39
 * @apiNote 词提单字的数据类
 */
public record TipCharStyleData(int index, String word, String code, int type, boolean bold) {
	public SimpleAttributeSet getStyle() {
		return TextStyleManager.getDefinedStyle(this.type, this.bold, this.word, false);
	}
}
