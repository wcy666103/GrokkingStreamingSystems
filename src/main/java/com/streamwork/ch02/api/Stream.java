package com.streamwork.ch02.api;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The Stream class represents a data stream coming out of a component.
 * Operators with the correct type can be applied to this stream.
 * Example:
 *   Job job = new Job("my_job");
 *   job.addSource(mySource)
 *      .applyOperator(myOperator);
 * stream是流系统的核心数据结构，表示一个数据流，具有一个属性operatorSet存储了所有应用在这个流上的操作算子集合。
 * 具有一个applyOperator方法，可以为这个流增加增加一个算子，并返回这个算子的输出流。
 */
public class Stream {
  // List of all operators to be applied to this stream.
  private final Set<Operator> operatorSet = new HashSet<Operator>();

  /**
   * Apply an operator to this stream.
   * @param operator The operator to be connected to the current stream
   * @return The outgoing stream of the operator.
   */
  public Stream applyOperator(Operator operator) {
    if (operatorSet.contains(operator)) {
      throw new RuntimeException("Operator " + operator.getName() + " is added to job twice");
    }

    operatorSet.add(operator);
    return operator.getOutgoingStream();
  }

  /**
   * Get the collection of operators applied to this stream.
   * @return The collection of operators applied to this stream
   */
  public Collection<Operator> getAppliedOperators() {
    return operatorSet;
  }
}
