package com.example.appsaldi.controller.poopupcontroller;

import java.util.function.Consumer;

public interface PopupSingleSelectController<T> {
    void setListener(Consumer<T> listener);
}
