// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.util;

import java.util.Iterator;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.Set;
import com.google.common.collect.ForwardingSet;

public class SetWrapper<E> extends ForwardingSet<E>
{
    private final Set<E> set;
    private final Consumer<E> addListener;
    
    public SetWrapper(final Set<E> set, final Consumer<E> addListener) {
        this.set = set;
        this.addListener = addListener;
    }
    
    public boolean add(final E element) {
        this.addListener.accept(element);
        return super.add((Object)element);
    }
    
    public boolean addAll(final Collection<? extends E> collection) {
        for (final E element : collection) {
            this.addListener.accept(element);
        }
        return super.addAll((Collection)collection);
    }
    
    protected Set<E> delegate() {
        return this.originalSet();
    }
    
    public Set<E> originalSet() {
        return this.set;
    }
}
