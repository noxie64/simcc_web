package at.simcc.simcc_backend.api.ws.payload;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageUnpacker;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/21/26
 */
@Data
@NoArgsConstructor
public class ScreenshotPayload extends WSAwaitable implements MsgPackDeserialize<ScreenshotPayload> {
    private byte[] imageData;

    public ScreenshotPayload(String id, byte[] imageData) {
        super(id);
        this.imageData = imageData;
    }

    @Override
    public ScreenshotPayload deserialize(MessageUnpacker packer, int keys) {
        try {
            for (int i = 0; i < keys; i++) {
                switch (packer.unpackString()) {
                    case "id" -> setId(packer.unpackString());
                    case "imageData" -> {
                        int len = packer.unpackBinaryHeader();
                        setImageData(packer.readPayload(len));
                    }
                }
            }
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to deserialize msg-pack content."
            );
        }
        return this;
    }
}
