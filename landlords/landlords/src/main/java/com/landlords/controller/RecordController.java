package com.landlords.controller;

import com.landlords.entity.Player;
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
public class RecordController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping(value = "/record", produces = "application/json;charset=UTF-8")
    public void record(@RequestBody Map<String, String> httpRequest){
        int playerId = Integer.parseInt(httpRequest.get("id"));
        int identity = Integer.parseInt(httpRequest.get("identity"));
        int state = Integer.parseInt(httpRequest.get("state"));
        int coins = Integer.parseInt(httpRequest.get("coins"));
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String sql = "insert into history value(?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, new Object[]{playerId, identity, state, coins, ft.format(new Date())});
        if(state == 1){
            sql = "select win, coins from user1 where id = ?";
            RowMapper<Player> rowMapper = new BeanPropertyRowMapper<Player>(Player.class);
            Player player = jdbcTemplate.queryForObject(sql, rowMapper, playerId);
            sql = "update user1 set win = ?, coins = ? where id = ?";
            jdbcTemplate.update(sql, new Object[]{Integer.parseInt(player.getWin()) + 1, Integer.parseInt(player.getCoins()) + coins, playerId});
        }
        else{
            sql = "select lose, coins from user1 where id = ?";
            RowMapper<Player> rowMapper = new BeanPropertyRowMapper<Player>(Player.class);
            Player player = jdbcTemplate.queryForObject(sql, rowMapper, playerId);
            sql = "update user1 set lose = ?, coins = ? where id = ?";
            jdbcTemplate.update(sql, new Object[]{Integer.parseInt(player.getLose()) + 1, Integer.parseInt(player.getCoins())  + coins, playerId});
        }
    }
}
