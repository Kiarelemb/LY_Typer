package ly.qr.kiarelemb.text.tip.data;

import javax.swing.text.SimpleAttributeSet;

public interface TipStyleData {
    boolean shortPhrase();

    void clearStyle();

    SimpleAttributeSet getStyle();

    String lastChar();

    int index();

    String code();

    int type();

    boolean bold();
}