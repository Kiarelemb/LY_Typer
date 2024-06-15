package ly.qr.kiarelemb.component;

/**
 * @author Kiarelemb QR
 * @program: 揽月跟打器
 * @description:
 * @create 2023-01-23 22:50
 **/
public class YesOrNoCheckBox extends CheckBox {

    public YesOrNoCheckBox(String key) {
        super("否", key, false);
        setText(this.checked() ? "是" : "否");
    }

    @Override
    public void setSelected(boolean b) {
        super.setSelected(b);
        setText(this.checked() ? "是" : "否");
    }
}