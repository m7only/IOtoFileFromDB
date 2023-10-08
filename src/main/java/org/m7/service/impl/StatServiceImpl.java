package org.m7.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.m7.dao.PurchaseDao;
import org.m7.dto.StatDto;
import org.m7.model.Criteria;
import org.m7.model.entity.Customer;
import org.m7.service.StatService;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class StatServiceImpl implements StatService {

    private static final String NODE_START_DATE = "startDate";
    private static final String NODE_END_DATE = "endDate";

    private final PurchaseDao purchaseDao = new PurchaseDao();
    private final ObjectMapper objectMapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    @Override
    public String stat(String read) {
        StatDto statDto = parseCriterias(read);
        String res = null;
        ObjectNode result = objectMapper.createObjectNode();
        result.put("type", "stat");
        result.put("totalDays", statDto.getStartDate().until(statDto.getEndDate(), ChronoUnit.DAYS));
        ArrayNode resultNode = result.putArray("customers");

        List<Customer> customers = purchaseDao.getStat(statDto.getStartDate(), statDto.getEndDate());
        //purchases.stream().map(purchase -> purchase.getId())
        for (Customer customer : customers) {
            System.out.println(customer.getPurchases());
        }

//        List<CustomerDto> customerDtoList =
//                customerDao
//                        .findByLastName(((CriteriaLastName) criteria).getLastName())
//                        .stream()
//                        .map(customer -> new CustomerDto(
//                                customer.getFirstName(),
//                                customer.getLastName()))
//                        .collect(Collectors.toList());
//        criteriaResultNode.putPOJO("results", customerDtoList);
//        return criteriaResultNode;


//        List<Criteria> criterias = parseCriterias(read);
//        Iterator<Criteria> iterator = criterias.iterator();
//        while (iterator.hasNext()) {
//            Criteria criteria = iterator.next();
//            ObjectNode criteriaResultNode = objectMapper.createObjectNode();
//            criteriaResultNode.putPOJO("criteria", criteria);
//            if (criteria instanceof CriteriaLastName) {
//                resultNode.add(getNodeByLastName(criteria, criteriaResultNode));
//            }
//            if (criteria instanceof CriteriaBadCustomers) {
//                resultNode.add(getNodeByBadCustomers(criteria, criteriaResultNode));
//            }
//            if (criteria instanceof CriteriaMinMaxExpenses) {
//                resultNode.add(getNodeByMinMaxExpenses(criteria, criteriaResultNode));
//            }
//            if (criteria instanceof CriteriaProductTimes) {
//                resultNode.add(getNodeByProductTimes(criteria, criteriaResultNode));
//            }
//        }
        try {
            res = objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    private StatDto parseCriterias(String read) {
        List<Criteria> criterias = new ArrayList<>();
        StatDto statDto = null;
        try {
            JsonNode nodes = objectMapper.readTree(read);
            if (!nodes.has(NODE_START_DATE) || !nodes.has(NODE_END_DATE)) {
                //error
                System.out.println("error1");
            }
            statDto = objectMapper.readValue(read, StatDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //error
            System.out.println("error2");
        }
        return statDto;
    }
}
