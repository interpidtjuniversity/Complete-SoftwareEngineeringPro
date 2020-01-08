package com.landlords.controller;

import com.landlords.entity.Friend;
import com.landlords.entity.Player;
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
public class FriendlistController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @ResponseBody
    @PostMapping(value = "/getFriendlist", produces = "application/json;charset=UTF-8")
    public String getFriendlist(@RequestBody Map<String, String> httpRequest){
        JSONArray result = new JSONArray();
        String playerid = httpRequest.get("id");
        String sql = "select id2 from friend where id1 = " + playerid;
        RowMapper<Friend> rowMapper = new BeanPropertyRowMapper<>(Friend.class);
        List<Friend> friendList = jdbcTemplate.query(sql, rowMapper);
        sql = "select username, coins, win, lose from user1 where id = ?";
        for(Friend friend:friendList){
            JSONObject temp = new JSONObject();
            RowMapper<Player> rowMapper1 = new BeanPropertyRowMapper<>(Player.class);
            Player player = jdbcTemplate.queryForObject(sql, rowMapper1, friend.getId2());
            temp.put("username", player.getUsername());
            temp.put("coins", player.getCoins());
            temp.put("win", player.getWin());
            temp.put("lose", player.getLose());
            result.put(temp);
        }
        return result.toString();
    }
}
