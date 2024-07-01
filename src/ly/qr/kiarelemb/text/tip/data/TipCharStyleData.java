package ly.qr.kiarelemb.text.tip.data;


import method.qr.kiarelemb.utils.QRStringUtils;

import javax.swing.text.SimpleAttributeSet;
import java.util.Objects;

/**
 * @author Kiarelemb QR
 * @date 2021/8/26 上午9:39
 * @apiNote 词提单字的数据类
 */
public final class TipCharStyleData implements TipStyleData {
    private final int index;
    private final String word;
    private final String code;
    private final int type;
    private final boolean bold;
    private SimpleAttributeSet style;

    /**
     *
     */
    public TipCharStyleData(int index, String word, String code, int type, boolean bold) {
        this.index = index;
        this.word = word;
        this.code = code;
        this.type = type;
        this.bold = bold;
    }

    public String word() {
        return word;
    }

    public boolean shortPhrase() {
        return false;
    }

    @Override
    public void clearStyle() {
        style = null;
    }

    @Override
    public SimpleAttributeSet getStyle() {
        if (style == null) {
            style = new SimpleAttributeSet(TextStyleManager.getDefinedStyle(this.type, this.bold, this.word, false));
            return style;
        }
        return style;
    }

    @Override
    public String lastChar() {
        return QRStringUtils.lastChar(this.code);
    }

    @Override
    public int index() {
        return index;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public int type() {
        return type;
    }

    @Override
    public boolean bold() {
        return bold;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (TipCharStyleData) obj;
        return this.index == that.index &&
               Objects.equals(this.word, that.word) &&
               Objects.equals(this.code, that.code) &&
               this.type == that.type &&
               this.bold == that.bold;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, word, code, type, bold);
    }

    @Override
    public String toString() {
        return "TipCharStyleData[" +
               "index=" + index + ", " +
               "word=" + word + ", " +
               "code=" + code + ", " +
               "type=" + type + ", " +
               "bold=" + bold + ']';
    }

}