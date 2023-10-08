package org.m7.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CriteriaProductTimes extends Criteria{


    private String productName;

    private Integer minTimes;

}
