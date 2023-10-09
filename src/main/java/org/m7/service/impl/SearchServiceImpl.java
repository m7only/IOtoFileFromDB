package org.m7.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.m7.dao.CustomerDao;
import org.m7.dto.CustomerDto;
import org.m7.model.*;
import org.m7.service.SearchService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class SearchServiceImpl implements SearchService {

    private static final String CRITERIA_TITLE = "criterias";
    private static final String CRITERIA_LASTNAME = "lastName";
    private static final String CRITERIA_PRODUCT_NAME = "productName";
    private static final String CRITERIA_MIN_TIMES = "minTimes";
    private static final String CRITERIA_MIN_EXPENSES = "minExpenses";
    private static final String CRITERIA_MAX_EXPENSES = "maxExpenses";
    private static final String CRITERIA_BAD_CUSTOMERS = "badCustomers";

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CustomerDao customerDao = new CustomerDao();


    @Override
    public String search(String read) {
        String res = null;
        ObjectNode result = objectMapper.createObjectNode();
        result.put("type", "search");
        ArrayNode resultNode = result.putArray("results");

        List<Criteria> criterias = parseCriterias(read);
        Iterator<Criteria> iterator = criterias.iterator();
        while (iterator.hasNext()) {
            Criteria criteria = iterator.next();
            ObjectNode criteriaResultNode = objectMapper.createObjectNode();
            criteriaResultNode.putPOJO("criteria", criteria);
            if (criteria instanceof CriteriaLastName) {
                resultNode.add(getNodeByLastName(criteria, criteriaResultNode));
            }
            if (criteria instanceof CriteriaBadCustomers) {
                resultNode.add(getNodeByBadCustomers(criteria, criteriaResultNode));
            }
            if (criteria instanceof CriteriaMinMaxExpenses) {
                resultNode.add(getNodeByMinMaxExpenses(criteria, criteriaResultNode));
            }
            if (criteria instanceof CriteriaProductTimes) {
                resultNode.add(getNodeByProductTimes(criteria, criteriaResultNode));
            }
        }
        try {
            res = objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ApplicationServiceImpl.error("Внутренняя ошибка приложения");
        }
        return res;
    }

    private ObjectNode getNodeByLastName(Criteria criteria, ObjectNode criteriaResultNode) {
        List<CustomerDto> customerDtoList =
                customerDao
                        .findByLastName(((CriteriaLastName) criteria).getLastName())
                        .stream()
                        .map(customer -> new CustomerDto(
                                customer.getFirstName(),
                                customer.getLastName()))
                        .collect(Collectors.toList());
        criteriaResultNode.putPOJO("results", customerDtoList);
        return criteriaResultNode;
    }

    private ObjectNode getNodeByBadCustomers(Criteria criteria, ObjectNode criteriaResultNode) {
        List<CustomerDto> customerDtoList =
                customerDao
                        .findBadCustomers(((CriteriaBadCustomers) criteria).getBadCustomers())
                        .stream()
                        .map(customer -> new CustomerDto(
                                customer.getFirstName(),
                                customer.getLastName()))
                        .collect(Collectors.toList());
        criteriaResultNode.putPOJO("results", customerDtoList);
        return criteriaResultNode;
    }

    private ObjectNode getNodeByMinMaxExpenses(Criteria criteria, ObjectNode criteriaResultNode) {
        CriteriaMinMaxExpenses criteriaProductTimes = (CriteriaMinMaxExpenses) criteria;
        List<CustomerDto> customerDtoList =
                customerDao
                        .findByMinMaxExpenses(
                                criteriaProductTimes.getMinExpenses(),
                                criteriaProductTimes.getMaxExpenses())
                        .stream()
                        .map(customer -> new CustomerDto(
                                customer.getFirstName(),
                                customer.getLastName()))
                        .collect(Collectors.toList());
        criteriaResultNode.putPOJO("results", customerDtoList);
        return criteriaResultNode;
    }

    private ObjectNode getNodeByProductTimes(Criteria criteria, ObjectNode criteriaResultNode) {
        CriteriaProductTimes criteriaProductTimes = (CriteriaProductTimes) criteria;
        List<CustomerDto> customerDtoList =
                customerDao
                        .findByProductTimes(
                                criteriaProductTimes.getProductName(),
                                criteriaProductTimes.getMinTimes())
                        .stream()
                        .map(customer -> new CustomerDto(
                                customer.getFirstName(),
                                customer.getLastName()))
                        .collect(Collectors.toList());
        criteriaResultNode.putPOJO("results", customerDtoList);
        return criteriaResultNode;
    }

    private List<Criteria> parseCriterias(String read) {
        List<Criteria> criterias = new ArrayList<>();
        try {
            JsonNode nodes = objectMapper.readTree(read);
            if (!nodes.has(CRITERIA_TITLE) || !nodes.get(CRITERIA_TITLE).isArray()) {
                ApplicationServiceImpl.error("Критерии поиска не распознаны");
            }
            JsonNode arrayNode = nodes.get(CRITERIA_TITLE);
            Iterator<JsonNode> iterator = arrayNode.elements();
            while (iterator.hasNext()) {
                JsonNode jsonNode = iterator.next();
                if (jsonNode.has(CRITERIA_LASTNAME)) {
                    criterias.add(new CriteriaLastName(jsonNode.get(CRITERIA_LASTNAME).asText()));
                }
                if (jsonNode.has(CRITERIA_PRODUCT_NAME) && jsonNode.has(CRITERIA_MIN_TIMES)) {
                    criterias.add(new CriteriaProductTimes(
                            jsonNode.get(CRITERIA_PRODUCT_NAME).asText(),
                            jsonNode.get(CRITERIA_MIN_TIMES).asInt()
                    ));
                }
                if (jsonNode.has(CRITERIA_MIN_EXPENSES) && jsonNode.has(CRITERIA_MAX_EXPENSES)) {
                    criterias.add(new CriteriaMinMaxExpenses(
                            jsonNode.get(CRITERIA_MIN_EXPENSES).asInt(),
                            jsonNode.get(CRITERIA_MAX_EXPENSES).asInt()
                    ));
                }
                if (jsonNode.has(CRITERIA_BAD_CUSTOMERS)) {
                    criterias.add(new CriteriaBadCustomers(jsonNode.get(CRITERIA_BAD_CUSTOMERS).asInt()));
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ApplicationServiceImpl.error("Критерии поиска не распознаны");
        }
        return criterias;
    }
}
