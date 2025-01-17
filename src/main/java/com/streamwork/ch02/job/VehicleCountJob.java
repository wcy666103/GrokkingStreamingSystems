package com.streamwork.ch02.job;

import com.streamwork.ch02.api.Job;
import com.streamwork.ch02.api.Stream;
import com.streamwork.ch02.engine.JobStarter;

/**
 * 该类组装了自己定义的job逻辑，是程序执行的入口类，其新建了一个job对象并为job添加了一个数据源。
 * 监听9990端口的输入流。
 */
public class VehicleCountJob {

  public static void main(String[] args) {
    Job job = new Job("vehicle_count");

    // 为job增加一个数据源
    Stream bridgeStream = job.addSource(new SensorReader("sensor-reader", 9990));
    // 为流增加一个计数操作算子
    bridgeStream.applyOperator(new VehicleCounter("vehicle-counter"));

    System.out.println("This is a streaming job that counts vehicles in real time. " +
        "Please enter vehicle types like 'car' and 'truck' in the input terminal " +
        "and look at the output");
    JobStarter starter = new JobStarter(job);
    starter.start();
  }
}
