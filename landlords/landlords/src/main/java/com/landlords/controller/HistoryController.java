package com.landlords.controller;

import com.landlords.entity.History;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HistoryController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ResponseBody
    @PostMapping(value = "/getHistory", produces = "application/json;charset=UTF-8")
    public String getHistory(@RequestBody Map<String, String> httpRequest) {
        JSONArray result = new JSONArray();
        String playerId = httpRequest.get("id");
        String sql = "select identity, state, coins, gameDate from history where id = " + playerId;
        RowMapper<History> rowMapper = new BeanPropertyRowMapper<>(History.class);
        List<History> historyList = jdbcTemplate.query(sql, rowMapper);
        for(History history:historyList){
            JSONObject temp = new JSONObject();
            temp.put("identity", history.getIdentity());
            temp.put("state", history.getState());
            temp.put("coins", history.getCoins());
            temp.put("gameDate", history.getGameDate());
            result.put(temp);
        }
        return result.toString();
    }
}
