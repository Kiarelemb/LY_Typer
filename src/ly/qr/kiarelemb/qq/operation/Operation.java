package ly.qr.kiarelemb.qq.operation;

import java.awt.*;

public interface Operation {
    void start(int model, String nameOrId, Robot robot);

    boolean textCanSend(int model);
}