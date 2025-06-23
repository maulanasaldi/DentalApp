package com.example.appsaldi.controller.poopupcontroller;

import java.util.List;
import java.util.function.Consumer;

public interface PopupMultiSelectController<T> {
    void setListener(Consumer<List<T>> listener);
}
