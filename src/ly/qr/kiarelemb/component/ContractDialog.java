package ly.qr.kiarelemb.component;

import ly.qr.kiarelemb.MainWindow;
import swing.qr.kiarelemb.window.basic.QRDialog;

import java.awt.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className ContractDialog
 * @description TODO
 * @create 2024/7/11 上午11:20
 */
class ContractDialog extends QRDialog {

    private final int widths;

    public ContractDialog(int widths) {
        super(MainWindow.INSTANCE, false);
        this.widths = widths;
        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.add(ContractiblePanel.CONTRACTIBLE_PANEL, BorderLayout.CENTER);
        setSize(widths, MainWindow.INSTANCE.getHeight());
        setParentWindowNotFollowMove();
        setResizable(true);
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(widths, height);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(MainWindow.INSTANCE.getX() - widths - 10, MainWindow.INSTANCE.getY(), widths, height);
    }

    @Override
    public void ownerMoved() {
        setLocation(MainWindow.INSTANCE.getX() - widths - 10, MainWindow.INSTANCE.getY());
    }
}