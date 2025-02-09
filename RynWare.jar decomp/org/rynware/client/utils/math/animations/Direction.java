// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math.animations;

public enum Direction
{
    FORWARDS, 
    BACKWARDS;
    
    public Direction opposite() {
        if (this == Direction.FORWARDS) {
            return Direction.BACKWARDS;
        }
        return Direction.FORWARDS;
    }
}
