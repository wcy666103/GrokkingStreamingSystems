package com.streamwork.ch08.api;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The Stream class represents a data stream coming out of a component.
 * Operators with the correct type can be applied to this stream.
 * Example:
 *   Job job = new Job("my_job");
 *   job.addSource(mySource)
 *      .applyOperator(myOperator);
 */
public class Stream implements Serializable {
  private static final long serialVersionUID = -4375024141519119762L;
  private static final String DEFAULT_CHANNEL = "default";

  // List of all operators to be applied to channels in this stream.
  private final Map<String, Map<Operator, String>> operatorMap =
    new HashMap<String, Map<Operator, String>>();

  public Stream applyOperator(Operator operator) {
    return applyOperator(DEFAULT_CHANNEL, operator, null);
  }

  Stream applyOperator(Operator operator, String streamName) {
    return applyOperator(DEFAULT_CHANNEL, operator, streamName);
  }

  /**
   * Apply an operator to this stream.
   * @param channel The channel to hook up the operator.
   * @param operator The operator to be connected to the current stream
   * @return The outgoing stream of the operator.
   */
  protected Stream applyOperator(String channel, Operator operator, String streamName) {
    if (operatorMap.containsKey(channel)) {
      Map<Operator, String> operatorToName = operatorMap.get(channel);
      if (operatorToName.containsKey(operator)) {
        throw new RuntimeException("Operator " + operator.getName() + " is added to job twice");
      }
      operatorToName.put(operator, streamName);
    } else {
      // This is a new channel.
      Map<Operator, String> operatorToName = new HashMap<Operator, String>();
      operatorToName.put(operator, streamName);
      operatorMap.put(channel, operatorToName);
    }

    return operator.getOutgoingStream();
  }

  protected Stream applyWindowOperator(WindowingStrategy strategy, WindowOperator operator) {
    WindowingOperator windowingOperator = new WindowingOperator(
        operator.getName(), operator.getParallelism(), strategy, operator, operator.getGroupingStrategy());
    applyOperator(windowingOperator);
    return operator.getOutgoingStream();
  }

  public StreamChannel selectChannel(String channel) {
    return new StreamChannel(this, channel);
  }

  public WindowedStream withWindowing(WindowingStrategy strategy) {
    return new WindowedStream(this, strategy);
  }

  /**
   * Get the channels in the stream. Note that the channel set
   * is collected from the downstream component's applyOperator() calls.
   * @return All the channel names registered by the downstream operator.
   */
  public Set<String> getChannels() {
    return operatorMap.keySet();
  }

  /**
   * Get the collection of operators applied to this stream.
   * @return The collection of operators applied to this stream.
   */
  public Map<Operator, String> getAppliedOperators(String channel) {
    return operatorMap.get(channel);
  }
}