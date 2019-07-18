package com.sagar.android.chatapp.repository;

public class Event<T> {
    private T content;
    private boolean shouldReadOnlyOnce = true;
    private boolean dataHandled = false;

    public Event(T content) {
        this.content = content;
    }

    public Event(T content, boolean shouldReadOnlyOnce) {
        this.content = content;
        this.shouldReadOnlyOnce = shouldReadOnlyOnce;
    }

    public T getContent() {
        if (!shouldReadOnlyOnce)
            return content;
        if (!dataHandled) {
            dataHandled = true;
            return content;
        }
        return null;
    }

    public boolean shouldReadContent() {
        if (!shouldReadOnlyOnce)
            return true;
        //noinspection ConstantConditions
        if (shouldReadOnlyOnce && (!dataHandled))
            return true;
        return false;
    }

    public void readContent() {
        dataHandled = true;
    }
}
