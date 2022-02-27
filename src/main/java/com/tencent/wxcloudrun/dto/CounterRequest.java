package com.tencent.wxcloudrun.dto;

public class CounterRequest {

  // `action`：`string` 类型，枚举值
  // 等于 `"inc"` 时，表示计数加一
  // 等于 `"clear"` 时，表示计数重置（清零）
  private String action;


  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }
}
