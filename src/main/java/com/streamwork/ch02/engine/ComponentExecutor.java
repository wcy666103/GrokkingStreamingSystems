package com.streamwork.ch02.engine;

import java.util.ArrayList;
import java.util.List;

import com.streamwork.ch02.api.Component;
import com.streamwork.ch02.api.Event;

/**
 * The base class for executors of source and operator.
 * 执行器组件的一些重要属性
 */
public abstract class ComponentExecutor extends Process {
  private final Component component;
  // This list is used for accepting events from user logic.
  protected final List<Event> eventCollector = new ArrayList<Event>();
  // Data queues for the upstream processes 数据输入队列
  protected EventQueue incomingQueue = null;
  // Data queue for the downstream processes 数据输出队列
  protected EventQueue outgoingQueue = null;

  public ComponentExecutor(Component component) {
    this.component = component;
  }

  public Component getComponent() {
    return component;
  }

  public void setIncomingQueue(EventQueue queue) {
    incomingQueue = queue;
  }

  public void setOutgoingQueue(EventQueue queue) {
    outgoingQueue = queue;
  }
}
