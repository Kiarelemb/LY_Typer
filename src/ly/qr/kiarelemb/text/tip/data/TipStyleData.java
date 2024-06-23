package ly.qr.kiarelemb.text.tip.data;

import javax.swing.text.SimpleAttributeSet;

public interface TipStyleData {
    SimpleAttributeSet getStyle();

    String lastChar();

    int index();

    String code();

    int type();

    boolean bold();
}