package com.streamwork.ch02.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The Job class is used by users to set up their jobs and run.
 * Example:
 *   Job job = new Job("my_job");
 *   job.addSource(mySource)
 *      .applyOperator(myOperator);
 * Job类是我们使用流计算框架的基本作业类，我们实际上只要实现一个自己定义好的job，就可以交由引擎去执行。
 * Job类有jobname, sourceSet(存放源的列表)两个属性，和添加源，获取name和获取源列表两个方法。
 */
public class Job {
  private final String name;
  private final Set<Source> sourceSet = new HashSet<Source>();

  public Job(String jobName) {
    this.name = jobName;
  }

  /**
   * Add a source into the job. A stream is returned which will be used to connect to
   * other operators.
   * 添加一个源到这个Job，返回一个可以被用来连接别的算子的流
   * @param source The source object to be added into the job
   * @return A stream that can be used to connect to other operators
   */
  public Stream addSource(Source source) {
    if (sourceSet.contains(source)) {
      throw new RuntimeException("Source " + source.getName() + " is added to job twice");
    }

    sourceSet.add(source);
    return source.getOutgoingStream();
  }

  /**
   * Get the name of this job.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the list sources in this job. This function is used by JobRunner to traverse the graph.
   *  得到这个Job的源列表，用来遍历图
   * @return The list of sources in this job
   */
  public Collection<Source> getSources() {
    return sourceSet;
  }
}
