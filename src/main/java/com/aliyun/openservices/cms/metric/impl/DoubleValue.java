package com.aliyun.openservices.cms.metric.impl;

import com.aliyun.openservices.cms.metric.Reservoir;
import com.aliyun.openservices.cms.metric.Snapshot;
import com.aliyun.openservices.cms.metric.registry.RecordLevel;
import com.aliyun.openservices.cms.metric.reservoir.ExponentiallyDecayingReservoir;

import java.util.HashMap;
import java.util.Map;

import static com.aliyun.openservices.cms.metric.MetricAttribute.*;
/**
 * Created by eli.lyj on 2017/10/31.
 */
public class DoubleValue implements Metric{

    protected Meter counter;
    protected DoubleMeter meter;
    protected Reservoir reservoir;

    public DoubleValue(RecordLevel level) {
        counter = new Meter(level);
        meter = new DoubleMeter(level);
        this.reservoir = new ExponentiallyDecayingReservoir(level);
    }

    public DoubleValue(RecordLevel level, Reservoir reservoir) {
        counter = new Meter(level);
        meter = new DoubleMeter(level);
        this.reservoir = reservoir;
    }



    public void update(double value) {
        counter.mark(1);
        meter.mark(value);
        reservoir.update(value);
    }

    @Override
    public Map<String, Number> resultMap() {
        Map<String, Number> ret = new HashMap<String, Number>();
        ret.put(COUNT.getCode(), counter.getCount());
        ret.put(COUNT_PS.getCode(), counter.getRate());
        ret.put(SUM.getCode(), meter.getCount());
        ret.put(SUM_PS.getCode(), meter.getRate());
        Snapshot snapshot = reservoir.getAndResetSnapshot();
        ret.put(MAX.getCode(), snapshot.getMax());
        ret.put(MIN.getCode(), snapshot.getMin());
        ret.put(AVG.getCode(), snapshot.getAvg());
        ret.put(P10.getCode(), snapshot.getValue(0.1));
        ret.put(P20.getCode(), snapshot.getValue(0.2));
        ret.put(P30.getCode(), snapshot.getValue(0.3));
        ret.put(P40.getCode(), snapshot.getValue(0.4));
        ret.put(P50.getCode(), snapshot.getValue(0.5));
        ret.put(P60.getCode(), snapshot.getValue(0.6));
        ret.put(P70.getCode(), snapshot.getValue(0.7));
        ret.put(P75.getCode(), snapshot.getValue(0.75));
        ret.put(P80.getCode(), snapshot.getValue(0.8));
        ret.put(P90.getCode(), snapshot.getValue(0.9));
        ret.put(P95.getCode(), snapshot.getValue(0.95));
        ret.put(P98.getCode(), snapshot.getValue(0.98));
        ret.put(P99.getCode(), snapshot.getValue(0.99));
        return ret;
    }
}
