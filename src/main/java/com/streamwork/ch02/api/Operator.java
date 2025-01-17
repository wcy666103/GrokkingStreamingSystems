package com.streamwork.ch02.api;

import java.util.List;

/**
 * This Operator class is the base class for all user defined operators.
 * Operator继承Component类，具有name和输出流两个属性，该抽象类有一个需要重写的抽象方法apply，记操作算子对事件的处理逻辑。
 * 有两个参数，Event event是接收到的event，List<Event> eventCollector是保存收到的事件方便后续输出到输出队列。
 */
public abstract class Operator extends Component {
  public Operator(String name) {
    super(name);
  }

  /**
   * Apply logic to the incoming event and generate results.
   * The function is abstract and needs to be implemented by users.
   * 将逻辑应用于传入事件并生成结果。该函数是抽象的，需要由用户实现。
   * @param event The incoming event
   * @param eventCollector The outgoing event collector
   */
  public abstract void apply(Event event, List<Event> eventCollector);
}
