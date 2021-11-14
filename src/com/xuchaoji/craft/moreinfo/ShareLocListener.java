package com.xuchaoji.craft.moreinfo;

public interface ShareLocListener {
    /**
     * 创建成功
     */
    void onSuccess();

    /**
     * 命名重复
     */
    void onNameRepeated();
}
