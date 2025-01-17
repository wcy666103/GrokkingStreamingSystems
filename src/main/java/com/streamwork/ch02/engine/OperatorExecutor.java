package com.streamwork.ch02.engine;

import com.streamwork.ch02.api.Event;
import com.streamwork.ch02.api.Operator;

/**
 * The executor for operator components. When the executor is started, a new thread
 * is created to call the apply() function of the operator component repeatedly.
 * 定义了runOnece的执行流程： 在输入队列读取一个事件，对事件执行应用逻辑
 * 将时间从eventCollector输出到输出队列
 */
public class OperatorExecutor extends ComponentExecutor {
  private final Operator operator;

  public OperatorExecutor(Operator operator) {
    super(operator);
    this.operator = operator;
  }

  /**
   * Run process once.
   * @return true if the thread should continue; false if the thread should exit.
   */
  @Override
  boolean runOnce() {
    Event event;
    try {
      // Read input
      event = incomingQueue.take();
    } catch (InterruptedException e) {
      return false;
    }

    // Apply operation
    operator.apply(event, eventCollector);

    // Emit out
    try {
      for (Event output: eventCollector) {
        outgoingQueue.put(output);
      }
      eventCollector.clear();
    } catch (InterruptedException e) {
      return false;  // exit thread
    }
    return true;
  }
}
