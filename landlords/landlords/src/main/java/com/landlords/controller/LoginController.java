package com.landlords.controller;

import com.landlords.entity.Player;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping(value = "/login", produces = "application/json;charset=UTF-8")
    public String login(@RequestBody Map<String, String> user){
        JSONObject result = new JSONObject();
        String loginId = user.get("username");
        String loginPassword = user.get("password");
        String sql = "select id, username, password, coins, win, lose from user1 where username = ?";
        RowMapper<Player> rowMapper = new BeanPropertyRowMapper<Player>(Player.class);
        Player loginUser = jdbcTemplate.queryForObject(sql, rowMapper, loginId);
        String correctPassword = loginUser.getPassword();
        if (correctPassword.equals(loginPassword)){
            result.put("status", true);
            result.put("id", loginUser.getId());
            result.put("username", loginUser.getUsername());
            result.put("win", loginUser.getWin());
            result.put("coins", loginUser.getCoins());
            result.put("lose", loginUser.getLose());
            return result.toString();
        }
        else{
            result.put("status", false);
            return result.toString();
        }
    }
}
