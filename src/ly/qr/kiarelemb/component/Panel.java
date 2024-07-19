package ly.qr.kiarelemb.component;

import swing.qr.kiarelemb.basic.QRPanel;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className Panel
 * @description TODO
 * @create 2024/7/14 下午1:37
 */
public class Panel extends QRPanel {
    @Override
    public void componentFresh() {
        super.componentFresh();
        setBackground(null);
    }
}