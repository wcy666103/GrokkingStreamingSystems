package com.streamwork.ch02.engine;

/**
 * A util data class for connections between components.
 * 连接两个组件的工具数据结构
 */
class Connection {
  public final ComponentExecutor from;
  public final OperatorExecutor to;

  public Connection(ComponentExecutor from, OperatorExecutor to) {
    this.from = from;
    this.to = to;
  }
}
