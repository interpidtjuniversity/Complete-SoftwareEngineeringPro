| 函数名         | 参数示例                                                     | 返回值                                 |
| -------------- | ------------------------------------------------------------ | -------------------------------------- |
| init_match     | {"cards": ["3","4","5","6","7","8","9","9","9","9","10","11","12","13","1","2","14"]} | none                                   |
| be_landlord    | none                                                         | {"value": 2}                           |
| landlord_info  | {"cards": ["3","5","7"], "player_id": “0”}                   | none                                   |
| play(别人出牌) | {"last_move": ["3","3"], "last_move_type": "dui" ,"player_id": "2", "your_turn": "false"} | none                                   |
| play(自己出牌) | {"last_move": "", "last_move_type": "" ,"player_id": "", "your_turn": "true"} | {“move”: ["5","5"], "move_type":"dui"} |
| play(自己跟牌) | {"last_move": ["3","3"], "last_move_type": "dui" ,"player_id": "2", "your_turn": "true"} | {“move”: ["5","5"], "move_type":"dui"} |

* play API的player_id字段为1或2，代表以“我”为视角的下家和下下家