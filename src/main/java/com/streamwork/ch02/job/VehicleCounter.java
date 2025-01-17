package com.streamwork.ch02.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.streamwork.ch02.api.Event;
import com.streamwork.ch02.api.Operator;

//继承Operator类，具有name和输出流两个属性，重写了apply方法，apply逻辑为获取数据，计数，打印结果并将事件传递给下游算子。
class VehicleCounter extends Operator {
  private final Map<String, Integer> countMap = new HashMap<String, Integer>();

  public VehicleCounter(String name) {  super(name);  }

//  apply逻辑为获取数据，计数，打印结果并将事件传递给下游算子  事实是并没有传递
  @Override
  public void apply(Event vehicleEvent, List<Event> eventCollector) {
    String vehicle = ((VehicleEvent)vehicleEvent).getData();
    Integer count = countMap.getOrDefault(vehicle, 0) + 1;
    countMap.put(vehicle, count);

    System.out.println("VehicleCounter --> ");
    printCountMap();

    // 将事件传递给下游算子 自己加的，感觉不像是这么用的？
    eventCollector.add(vehicleEvent);
  }

  private void printCountMap() {
    List<String> vehicles = new ArrayList<>(countMap.keySet());
    Collections.sort(vehicles);

    for (String vehicle: vehicles) {
      System.out.println("  " + vehicle + ": " + countMap.get(vehicle));
    }
  }
}
