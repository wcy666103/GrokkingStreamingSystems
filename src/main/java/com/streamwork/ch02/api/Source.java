package com.streamwork.ch02.api;

import java.util.List;

/**
 * This Source class is the base class for all user defined sources.
 * source类继承Component类，有name和outgoingStream属性，此外它是一个抽象类，需要实现getEvents方法，
 * 参数eventCollector是一个输出队列收集器，用于存放收集到的事件。 它的定位是一个job中的数据源角色。
 */
public abstract class Source extends Component {
  public Source(String name) {
    super(name);
  }

  /**
   * Accept events from external into the system.
   * 相当于所有数据的一个集合
   * The function is abstract and needs to be implemented by users.
   * @param eventCollector The outgoing event collector 输出队列收集器
   */
  public abstract void getEvents(List<Event> eventCollector);
}
