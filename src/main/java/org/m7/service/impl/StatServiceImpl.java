package org.m7.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.m7.dao.PurchaseDao;
import org.m7.dto.PurchaseDto;
import org.m7.dto.StatDto;
import org.m7.model.Criteria;
import org.m7.model.entity.Customer;
import org.m7.service.StatService;
import org.m7.util.SumAvg;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        List<Customer> customerStatDto = purchaseDao.getStat(statDto.getStartDate(), statDto.getEndDate());
        Set<String> names = customerStatDto.stream()
                .map(customer -> customer.getFirstName() + " " + customer.getLastName())
                .collect(Collectors.toSet());
        SumAvg totalSum = new SumAvg();
        for (String name : names) {
            SumAvg customerSum = new SumAvg();
            ObjectNode customerNode = objectMapper.createObjectNode();
            customerNode.put("name", name);
            ArrayNode customerPurchases = customerNode.putArray("purchases");
            customerStatDto.stream()
                    .filter(customer ->
                            name.equals(customer.getFirstName() + " " + customer.getLastName()))
                    .forEach(customer ->
                            customer.getPurchases()
                                    .forEach(purchase -> {
                                        customerPurchases.addPOJO(
                                                new PurchaseDto(
                                                        purchase.getProduct().getProductName(),
                                                        purchase.getProduct().getPrice()
                                                ));
                                        customerSum.add(purchase.getProduct().getPrice());
                                    }));
            customerNode.put("totalExpenses", customerSum.getSum());
            resultNode.add(customerNode);
            totalSum.add(customerSum.getSum());
        }
        result.put("totalExpenses", totalSum.getSum());
        result.put("avgExpenses", BigDecimal.valueOf(totalSum.getAvg()).setScale(2, RoundingMode.HALF_UP));
        try {
            res = objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ApplicationServiceImpl.error("Внутренняя ошибка приложения");
        }
        return res;
    }

    private StatDto parseCriterias(String read) {
        List<Criteria> criterias = new ArrayList<>();
        StatDto statDto = null;
        try {
            JsonNode nodes = objectMapper.readTree(read);
            if (!nodes.has(NODE_START_DATE) || !nodes.has(NODE_END_DATE)) {
                ApplicationServiceImpl.error("Отсутствует одна из дат");
            }
            statDto = objectMapper.readValue(read, StatDto.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            ApplicationServiceImpl.error("JSON не распознан");
        }
        return statDto;
    }
}
