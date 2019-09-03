package com.gss.ch02.engine;

import com.gss.ch02.api.IComponent;
import com.gss.ch02.api.Job;
import com.gss.ch02.api.Operator;
import com.gss.ch02.api.Source;
import com.gss.ch02.api.Stream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class JobRunner {
  private class Connection {
    private final ComponentExecutor from;
    private final ComponentExecutor to;

    public Connection(ComponentExecutor from, ComponentExecutor to) {
      this.from = from;
      this.to = to;
    }

    public ComponentExecutor getFrom() { return from; }
    public ComponentExecutor getTo() { return to; }
  }

  private final Job job;
  private final List<ComponentExecutor> runnerList;
  private final Map<ComponentExecutor, List<OperatorExecutor>> connectionMap;
  private final List<StreamManager> streamManagerList;

  public JobRunner(Job job) {
    this.job = job;
    this.runnerList = new ArrayList<ComponentExecutor>();
    this.connectionMap = new HashMap<ComponentExecutor, List<OperatorExecutor>>();
    this.streamManagerList = new ArrayList<StreamManager>();
  }

  public void addConnection(ComponentExecutor from, OperatorExecutor to) {
    if (!connectionMap.containsKey(from)) {
      connectionMap.put(from, new ArrayList<OperatorExecutor>());
    }
    connectionMap.get(from).add(to);
  }

  public void start() {
    // Set up runners for all the components.
    setupComponentRunners();

    // All components are created now. Build the stream managers for the connections to
    // connect the component together.
    setupStreamManagers();

    // Start all stream managers first.
    startStreamManagers();

    // Start all component runners.
    startComponentRunners();
  }

  private void setupComponentRunners() {
    // Start from sources in the job.
    for (Map.Entry<String, Source> entry : job.getSourceMap().entrySet()) {
      // For each source, traverse the the operations connected to it.
      List<OperatorExecutor> operatorRunners = traverseComponent(entry.getValue());

      // Start the current component.
      SourceExecutor runner = setupSourceRunner(entry.getValue());

      for (OperatorExecutor operatorRunner : operatorRunners) {
        addConnection(runner, operatorRunner);
      }
    }
  }

  private void setupStreamManagers() {
    // All components are created now. Build the stream managers for the connections to
    // connect the component together.
    for (Map.Entry<ComponentExecutor, List<OperatorExecutor>> entry: connectionMap.entrySet()) {
      List<BlockingQueue> targetQueues = new ArrayList<BlockingQueue>();
      for (OperatorExecutor target: entry.getValue()) {
        targetQueues.add(target.getIncomingQueue());
      }
      // 1 stream manager per "from" component
      StreamManager manager = new StreamManager(entry.getKey().getOutgoingQueue(), targetQueues);
      streamManagerList.add(manager);
    }
  }

  private <T> SourceExecutor<T> setupSourceRunner(Source<T> source) {
    SourceExecutor<T> runner = new SourceExecutor<T>(source);
    runnerList.add(runner);

    return runner;
  }

  private <I, T> OperatorExecutor<I, T> setupOperationRunner(Operator<I, T> operation) {
    OperatorExecutor<I, T> runner = new OperatorExecutor<I, T>(operation);
    runnerList.add(runner);

    return runner;
  }

  private List<OperatorExecutor> traverseComponent(IComponent component) {
    Stream stream = component.getOutgoingStream();

    Map<String, Operator> operationMap = stream.getOperationMap();
    List<OperatorExecutor> operatorRunners = new ArrayList<OperatorExecutor>();

    for (Map.Entry<String, Operator> entry: operationMap.entrySet()) {
      Operator op = entry.getValue();
      // Setup runners for the downstrea operators first.
      List<OperatorExecutor> downstreamRunners = traverseComponent(op);
      // Start the current component.
      OperatorExecutor runner = setupOperationRunner(entry.getValue());

      for (OperatorExecutor downstreamRunner : downstreamRunners) {
        addConnection(runner, downstreamRunner);
      }
      operatorRunners.add(runner);
    }

    return operatorRunners;
  }

  private void startComponentRunners() {
    for (ComponentExecutor runner: runnerList) {
      runner.start();
    }
  }

  private void startStreamManagers() {
    for (StreamManager streamManager: streamManagerList) {
      streamManager.start();
    }
  }
}