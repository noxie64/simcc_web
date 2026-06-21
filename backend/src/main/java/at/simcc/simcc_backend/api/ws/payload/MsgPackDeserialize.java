package at.simcc.simcc_backend.api.ws.payload;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/21/26
 */
public interface MsgPackDeserialize<T> {
    T deserialize(MessageUnpacker packer, int keys);
}