package ly.qr.kiarelemb.component;

import method.qr.kiarelemb.utils.QRStringUtils;
import swing.qr.kiarelemb.basic.QRScrollBar;
import swing.qr.kiarelemb.basic.QRScrollPane;
import swing.qr.kiarelemb.basic.QRTextPane;
import swing.qr.kiarelemb.combination.QRTabbedContentPanel;
import swing.qr.kiarelemb.combination.QRTabbedPane;

import java.awt.*;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className TypeTabbedPane
 * @description TODO
 * @create 2024/7/11 下午10:05
 */
public class TypeTabbedPane extends QRTabbedPane {
    public static final TypeTabbedPane TYPE_TABBED_PANE = new TypeTabbedPane();

    private TypeTabbedPane() {
        super(BorderLayout.SOUTH);
        setOpaque(false);
        init_typerTextPane();
        init_historyGradePane();

        setSelectedTab(0);
    }

    private void init_typerTextPane() {
        QRTabbedContentPanel typer = new QRTabbedContentPanel(new BorderLayout());
        addTab("跟打区", typer);
        typer.add(TyperTextPane.TYPER_TEXT_PANE.addScrollPane(), BorderLayout.CENTER);
    }

    private void init_historyGradePane() {
        QRTabbedContentPanel history = new QRTabbedContentPanel(new BorderLayout());
        addTab("跟打历史", history);
        QRTextPane tip = new TextPane();
        tip.setLineWrap(false);

        String[] tips = {"段号", "速度", "击键", "码长", "字数", "回改", "退格", "回车", "错字", "键数", "键准", "键法", "用时", "正文  "};
        tip.setText(QRStringUtils.join(tips, "\t"));

        QRScrollPane tipScrollPane = tip.addScrollPane();
        QRScrollPane historyScrollPane = HistoryGradePane.HISTORY_GRADE_PANE.addScrollPane();
        ((QRScrollBar)tipScrollPane.getHorizontalScrollBar()).setExistButVisibleFalse();
        historyScrollPane.addFollowedToScrollPane(tipScrollPane);
        ((QRScrollBar)historyScrollPane.getHorizontalScrollBar()).addSynchronisedScrollBor(tipScrollPane.getHorizontalScrollBar());
        history.add(tipScrollPane, BorderLayout.NORTH);
        history.add(historyScrollPane, BorderLayout.CENTER);


    }
}