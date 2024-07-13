package ly.qr.kiarelemb.text.tip.data;

import javax.swing.text.SimpleAttributeSet;

public interface TipStyleData {
    boolean shortPhrase();

    SimpleAttributeSet getStyle();

    void clearStyle();

    String lastChar();

    int index();

    String code();

    int type();

    boolean bold();
}