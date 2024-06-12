package ly.qr.kiarelemb.text.tip.data;

import method.qr.kiarelemb.utils.QRStringUtils;

import javax.swing.text.SimpleAttributeSet;

/**
 * @author Kiarelemb QR
 * @date 2021/8/26 上午9:40
 * @apiNote 最短码长的数据类
 */
public final class TipPhraseStyleData {
	private final int index;
	private final String phrase;
	private final String code;
	private final int type;
	private final boolean bold;
	private final boolean shortPhrase;

	/**
	 *
	 */
	public TipPhraseStyleData(int index, String phrase, String code, int type, boolean bold, boolean shortPhrase) {
		this.index = index;
		this.phrase = phrase;
		this.code = code;
		this.type = type;
		this.bold = bold;
		this.shortPhrase = shortPhrase;
	}


	public SimpleAttributeSet getStyle() {
		return TextStyleManager.getDefinedStyle(this.type, this.bold, this.phrase, this.shortPhrase);
	}

	public String lastChar() {
		return QRStringUtils.lastChar(this.code);
	}

	@Override
	public int hashCode() {
		int result = this.index;
		result = 31 * result + this.phrase.hashCode();
		result = 31 * result + this.code.hashCode();
		result = 31 * result + this.type;
		result = 31 * result + (this.bold ? 1 : 0);
		result = 31 * result + (this.shortPhrase ? 1 : 0);
		return result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		TipPhraseStyleData that = (TipPhraseStyleData) o;

		if (this.index != that.index || this.type != that.type || this.bold != that.bold || this.shortPhrase != that.shortPhrase || !this.phrase.equals(that.phrase)) {
			return false;
		}
		return this.code.equals(that.code);
	}

	public int index() {
		return this.index;
	}

	public String phrase() {
		return this.phrase;
	}

	public String code() {
		return this.code;
	}

	public int type() {
		return this.type;
	}

	public boolean bold() {
		return this.bold;
	}

	public boolean shortPhrase() {
		return this.shortPhrase;
	}

	@Override
	public String toString() {
		return "TipPhraseStyleData[" + "index=" + this.index + ", " + "phrase=" + this.phrase + ", " + "code=" + this.code + ", " + "type=" + this.type + ", " + "bold=" + this.bold + ", " + "shortPhrase=" + this.shortPhrase + ']';
	}

}