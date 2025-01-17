package com.streamwork.ch02.engine;

/**
 * This is the base class of all processes (executors). When a process is started,
 * a new thread is created to call the runOnce() function of the derived class.
 * Each process also have an incoming event queue and an outgoing event queue.
 *
 * 这是所有进程（执行者）的基类。当一个进程启动时，
 * 创建一个新线程来调用派生类的runOnce（）函数。
 * *每个进程也有一个传入事件队列和一个传出事件队列。
 */
public abstract class Process {
  private final Thread thread;

  public Process() {
    this.thread = new Thread() {
      public void run() {
        while (runOnce());
      }
    };
  }

  /**
   * Start the process.
   */
  public void start() {
    thread.start();
  }

  /**
   * Run process once.
   * @return true if the thread should continue; false if the thread should exit.
   */
  abstract boolean runOnce();
}
