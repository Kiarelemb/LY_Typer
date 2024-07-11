package ly.qr.kiarelemb.component;

/**
 * @author Kiarelemb
 * @projectName LYTyper
 * @className HistoryGradePane
 * @description TODO
 * @create 2024/7/11 下午9:52
 */
public class HistoryGradePane extends TextPane {
    public static final HistoryGradePane HISTORY_GRADE_PANE = new HistoryGradePane();

    private HistoryGradePane() {
        setEditableFalseButCursorEdit();
        setLineWrap(false);
        setLineSpacing(0.5f);
    }
}