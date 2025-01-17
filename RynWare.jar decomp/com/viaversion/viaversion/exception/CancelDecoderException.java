// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.exception;

import com.viaversion.viaversion.api.Via;
import io.netty.handler.codec.DecoderException;

public class CancelDecoderException extends DecoderException implements CancelCodecException
{
    public static final CancelDecoderException CACHED;
    
    public CancelDecoderException() {
    }
    
    public CancelDecoderException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public CancelDecoderException(final String message) {
        super(message);
    }
    
    public CancelDecoderException(final Throwable cause) {
        super(cause);
    }
    
    public static CancelDecoderException generate(final Throwable cause) {
        return Via.getManager().isDebug() ? new CancelDecoderException(cause) : CancelDecoderException.CACHED;
    }
    
    static {
        CACHED = new CancelDecoderException("This packet is supposed to be cancelled; If you have debug enabled, you can ignore these") {
            public Throwable fillInStackTrace() {
                return (Throwable)this;
            }
        };
    }
}
