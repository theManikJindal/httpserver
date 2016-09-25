package com.themanikjindal.utils;


import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class SharedRef<T extends Closeable> implements Closeable {
    private final AtomicReference<T> closeableAtomicReference;
    private final AtomicLong refCount;

    public SharedRef(T closeable) {
        this.closeableAtomicReference = new AtomicReference<>(closeable);
        this.refCount = new AtomicLong(1L);
    }

    public T get() {
        if(refCount.get() < 1) {
            throw new IllegalStateException();
        }

        return closeableAtomicReference.get();
    }

    public void makeCopy() {
        if(refCount.get() < 1) {
            throw new IllegalStateException();
        }

        refCount.incrementAndGet();
    }

    public void deleteCopy() throws IOException {
        if(refCount.get() < 1) {
            throw new IllegalStateException();
        }

        if(refCount.decrementAndGet() == 0) {
            closeableAtomicReference.getAndSet(null).close();
        }
    }

    public void deleteCopyNoThrow() {
        try {
            deleteCopy();
        } catch (IOException ignored) {}
    }

    @Override
    public void close() throws IOException {
        deleteCopy();
    }
}
