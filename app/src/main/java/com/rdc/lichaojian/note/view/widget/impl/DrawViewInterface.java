package com.rdc.lichaojian.note.view.widget.impl;

/**
 * Created by lichaojian on 16-8-28.
 */
public interface DrawViewInterface {

    void redo();

    void undo();

    void reset();

    String save(String path, String rootPath);
}
