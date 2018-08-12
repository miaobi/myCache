package netty;
import static org.junit.Assert.*;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;

public class FixedLengthFrameDecoderTest {
    @Test
    public void testFixedDecoder(){
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++){
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new FicedLengthFrameDecoder(3));
        assertTrue(channel.writeInbound(input.retain()));
    }
}
