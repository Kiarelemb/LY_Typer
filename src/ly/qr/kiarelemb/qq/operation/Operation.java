package ly.qr.kiarelemb.qq.operation;

public interface Operation {
    void start(int model, String nameOrId);

    boolean textCanSend(int model);
}