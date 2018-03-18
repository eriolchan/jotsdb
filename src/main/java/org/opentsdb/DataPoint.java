package org.opentsdb;

import java.util.List;
import java.util.Map;

public class DataPoint {
    public String metric;
    public Map<String, String> tags;
    public List<String> aggregateTags;
    public Map<String, Double> dps;
}
