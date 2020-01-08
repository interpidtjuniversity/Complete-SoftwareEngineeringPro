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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
public class RegisterController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping(value = "/register", produces = "application/json;charset=UTF-8")
    public String register(@RequestBody Map<String, String> registrant){
        JSONObject result = new JSONObject();
        String registerName = registrant.get("username");
        String registerPassword = registrant.get("password");
        String sql = "select count(*) from user1 where username = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, registerName);
        if(count == 0){             //用户名尚未被注册
            sql = "insert into user1(username, password, coins, win, lose, registerDate) values(?, ?, ?, ?, ?, ?)";
            SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
            jdbcTemplate.update(sql, new Object[]{registerName, registerPassword, 1000, 0, 0, ft.format(new Date())});

            sql = "insert into task(username, current_progress) values(?,?) ";
            jdbcTemplate.update(sql, new Object[]{registerName, 0});

            sql = "select id, username, password, coins, win, lose from user1 where username = ?";
            RowMapper<Player> rowMapper = new BeanPropertyRowMapper<Player>(Player.class);
            Player loginUser = jdbcTemplate.queryForObject(sql, rowMapper, registerName);
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
