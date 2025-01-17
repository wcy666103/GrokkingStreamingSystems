package com.streamwork.ch02.api;

/**
 * This is the base class for all the event classes.
 * Users should extend this class to implement all their own event classes.
 * event是一个需要我们自己实现的抽象类，它定义了我们在流系统中的事件概念，
 * 可以理解为一条数据就是一个事件。所有继承它的类都要实现一个getData方法。
 */
public abstract class Event {
  /**
   * Get data stored in the event.
   * @return The data stored in the event
   */
  public abstract Object getData();
}
