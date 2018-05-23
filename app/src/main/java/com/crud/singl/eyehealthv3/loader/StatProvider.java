package com.crud.singl.eyehealthv3.loader;

import com.crud.singl.eyehealthv3.model.Period;
import com.crud.singl.eyehealthv3.model.StatEntry;

import java.util.List;

public interface StatProvider {

    List<StatEntry> loadStats(Period period);

    List<StatEntry> loadStatsWithLimit(Period period, int maxCount);

}
