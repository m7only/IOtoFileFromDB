package org.m7.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CriteriaProductTimes extends Criteria {


    private String productName;

    private Integer minTimes;

}
