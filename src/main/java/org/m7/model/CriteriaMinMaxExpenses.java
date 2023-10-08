package org.m7.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CriteriaMinMaxExpenses extends Criteria{


    private Integer minExpenses;

    private Integer maxExpenses;

}
