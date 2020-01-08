PG = {
    music: null,
    playerInfo: {},
    orientated: false
};

PG.getCookie = function (name) {
    let r = document.cookie.match("\\b" + name + "=([^;]*)\\b");
    return r ? r[1] : undefined;
};

PG.PW = 90;
PG.PH = 120;

PG.Boot = {
    preload: function () {
        this.load.image('preloaderBar', 'static/i/preload.png');
    },
    create: function () {
        this.input.maxPointers = 1;
        this.stage.disableVisibilityChange = true;
        this.scale.scaleMode = Phaser.ScaleManager.SHOW_ALL;
        this.scale.enterIncorrectOrientation.add(this.enterIncorrectOrientation, this);
        this.scale.leaveIncorrectOrientation.add(this.leaveIncorrectOrientation, this);
        this.onSizeChange();
        this.state.start('Preloader');
    },
    onSizeChange: function () {
        this.scale.minWidth = 480;
        this.scale.minHeight = 270;
        let device = this.game.device;
        if (device.android || device.iOS) {
            this.scale.maxWidth = window.innerWidth;
            this.scale.maxHeight = window.innerHeight;
        } else {
            this.scale.maxWidth = 960;
            this.scale.maxHeight = 540;
        }
        this.scale.pageAlignHorizontally = true;
        this.scale.pageAlignVertically = true;
        this.scale.forceOrientation(true);
    },
    enterIncorrectOrientation: function () {
        PG.orientated = false;
        document.getElementById('orientation').style.display = 'block';
    },
    leaveIncorrectOrientation: function () {
        PG.orientated = true;
        document.getElementById('orientation').style.display = 'none';
    }
};

PG.Preloader = {

    preload: function () {
        this.preloadBar = this.game.add.sprite(120, 200, 'preloaderBar');
        this.load.setPreloadSprite(this.preloadBar);

        this.load.audio('music_room', 'static/audio/bg_room.mp3');
        this.load.audio('music_game', 'static/audio/bg_game.ogg');
        this.load.audio('music_deal', 'static/audio/deal.mp3');
        this.load.audio('music_win', 'static/audio/end_win.mp3');
        this.load.audio('music_lose', 'static/audio/end_lose.mp3');
        this.load.audio('f_score_0', 'static/audio/f_score_0.mp3');
        this.load.audio('f_score_1', 'static/audio/f_score_1.mp3');
        this.load.audio('f_score_2', 'static/audio/f_score_2.mp3');
        this.load.audio('f_score_3', 'static/audio/f_score_3.mp3');
        this.load.atlas('btn', 'static/i/btn.png', 'static/i/btn.json');
        this.load.image('bg', 'static/i/bg.png');
        this.load.image('login', 'static/i/btn/login.jpg');
        this.load.image('friend', 'static/i/btn/friend.jpg');
        this.load.image('history', 'static/i/btn/history.jpg');
        this.load.image('listbg', 'static/i/listbg.jpg');
        this.load.image('task', 'static/i/task.png');
        this.load.spritesheet('poker', 'static/i/poker.png', 90, 120);
        this.load.json('rule', 'static/rule.json');
    },

    create: function () {
        PG.RuleList = this.cache.getJSON('rule');
        this.state.start('Login');
        PG.music = this.game.add.audio('music_room');
        PG.music.loop = true;
        PG.music.loopFull();
        PG.music.play();
    }
};

PG.Task = {
  create: function () {
      let task = this.game.add.button(320, 0, 'task', this.viewFriend, this, 'task.png', 'task.png', 'task.png');
      setting.anchor.set(0, 0);
      this.game.world.add(friend);

  }  
};

PG.MainMenu = {
    create: function () {
        this.textGroup = new Array();
        this.stage.backgroundColor = '#182d3b';
        let bg = this.game.add.sprite(this.game.width / 2, 0, 'bg');
        bg.anchor.set(0.5, 0);

        let listbg = this.game.add.sprite(0, 60, 'listbg');
        listbg.anchor.set(0, 0);

        let aiRoom = this.game.add.button(this.game.world.width * 3 / 4, this.game.world.height / 4, 'btn', this.gotoAiRoom, this, 'quick.png', 'quick.png', 'quick.png');
        aiRoom.anchor.set(0.5);
        this.game.world.add(aiRoom);

        let humanRoom = this.game.add.button(this.game.world.width * 3 / 4, this.game.world.height / 2, 'btn', this.gotoRoom, this, 'start.png', 'start.png', 'start.png');
        humanRoom.anchor.set(0.5);
        this.game.world.add(humanRoom);

        let setting = this.game.add.button(this.game.world.width * 3 / 4 - 80, this.game.world.height * 3 / 4 - 30, 'btn', this.gotoSetting, this, 'setting.png', 'setting.png', 'setting.png');
        setting.anchor.set(0.5, 0.5);
        this.game.world.add(setting);

        let friend = this.game.add.button(0, 0, 'friend', this.viewFriend, this, 'friend.jpg', 'friend.jpg', 'friend.jpg');
        setting.anchor.set(0, 0);
        this.game.world.add(friend);

        let history = this.game.add.button(160, 0, 'history', this.viewHistory, this, 'history.jpg', 'history.jpg', 'history.jpg');
        setting.anchor.set(0, 0);
        this.game.world.add(history);

        let style = {font: "28px Arial", fill: "#fff", align: "right"};
        let text = this.game.add.text(this.game.world.width - 4, 4, "欢迎回来 " + PG.playerInfo.username + "  金币:" + PG.playerInfo.coins + "  胜率:" + (parseInt(PG.playerInfo.win)/(parseInt(PG.playerInfo.win) + parseInt(PG.playerInfo.lose))).toFixed(2), style);
        text.addColor('#cc00cc', 0);
        text.anchor.set(1, 0);
        this.viewFriend();
    },

    gotoAiRoom: function () {
        // start(key, clearWorld, clearCache, parameter)
        this.state.start('Game', true, false, 1);
        // this.music.stop();
    },

    gotoRoom: function () {
        this.state.start('Game', true, false, 2);
    },

    gotoSetting: function () {
        // let style = {font: "22px Arial", fill: "#fff", align: "center"};
        // let text = this.game.add.text(0, 0, "hei hei hei hei", style);
        // let tween = this.game.add.tween(text).to({x: 600, y: 450}, 2000, "Linear", true);
        // tween.onComplete.add(Phaser.Text.prototype.destroy, text);
        alert("想吃蛋蛋肉饼");
    },

    viewFriend: function () {
        let self = this;
        $.ajax({
            url: PG.IPAddress + "/getFriendlist",
            type: 'post',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
              'id': PG.playerInfo.userid
            }),
            success: function(msg){
                for(let i=0; i<self.textGroup.length; i++){
                    self.textGroup[i].text = "";
                }
                let style = {"fontSize": 20};
                let text = self.add.text(0, 60, "用户名", style);
                self.textGroup.push(text);
                text = self.add.text(160, 60, "金币", style);
                self.textGroup.push(text);
                text = self.add.text(220, 60, "胜场", style);
                self.textGroup.push(text);
                text = self.add.text(270, 60, "负场", style);
                self.textGroup.push(text);
                style = {"fontSize": 15};
                for(let i=0; i<msg.length; i++){
                    text = self.add.text(0, 80+i*15, msg[i]["username"], style);
                    self.textGroup.push(text);
                    text = self.add.text(160, 80+i*15, msg[i]["coins"], style);
                    self.textGroup.push(text);
                    text = self.add.text(220, 80+i*15, msg[i]["win"], style);
                    self.textGroup.push(text);
                    text = self.add.text(270, 80+i*15, msg[i]["lose"], style);
                    self.textGroup.push(text);
                }
            }
        })
    },

    viewHistory: function(){
        let self = this;
        $.ajax({
            url: PG.IPAddress + "/getHistory",
            type: 'post',
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify({
                'id': PG.playerInfo.userid
            }),
            success: function(msg){
                for(let i=0; i<self.textGroup.length; i++){
                    self.textGroup[i].text = "";
                }
                let style = {"fontSize": 20};
                let text = self.add.text(0, 60, "身份", style);
                self.textGroup.push(text);
                text = self.add.text(50, 60, "胜负", style);
                self.textGroup.push(text);
                text = self.add.text(100, 60, "奖励", style);
                self.textGroup.push(text);
                text = self.add.text(160, 60, "对战时间", style);
                self.textGroup.push(text);
                style = {"fontSize": 15};
                for(let i=0; i<msg.length; i++){
                    text = self.add.text(0, 80+i*15, msg[i]["identity"]==0?"农民":"地主", style);
                    self.textGroup.push(text);
                    text = self.add.text(50, 80+i*15, msg[i]["state"]==0?"失败":"胜利", style);
                    self.textGroup.push(text);
                    text = self.add.text(100, 80+i*15, msg[i]['coins'], style);
                    self.textGroup.push(text);
                    text = self.add.text(160, 80+i*15, msg[i]['gameDate'], style);
                    self.textGroup.push(text);
                }
            }

        })
    }


};

PG.Login = {
    create: function () {
        this.stage.backgroundColor = '#182d3b';
        let bg = this.game.add.sprite(this.game.width / 2, 0, 'bg');
        bg.anchor.set(0.5, 0);

        let style = {
            font: '24px Arial', fill: '#000', width: 300, padding: 12,
            borderWidth: 1, borderColor: '#c8c8c8', borderRadius: 2,
            textAlign: 'center', placeHolder: '用户名'
        };
        this.game.add.plugin(PhaserInput.Plugin);

        this.username = this.game.add.inputField((this.game.world.width - 300) / 2, this.game.world.centerY - 90, style);

        style.placeHolder = '密码';
        this.password = this.game.add.inputField((this.game.world.width - 300) / 2, this.game.world.centerY - 20, style);

        const errorStyle = {font: "22px Arial", fill: "#f00", align: "center"};
        this.errorText = this.game.add.text(this.game.world.centerX, this.game.world.centerY + 45, '', errorStyle);
        this.errorText.anchor.set(0.5, 0);

        let login = this.game.add.button(this.game.world.centerX + 100, this.game.world.centerY + 100, 'login', this.onLogin, this, 'login.jpg', 'login.jpg', 'login.jpg');
        login.anchor.set(0.5);
        let register = this.game.add.button(this.game.world.centerX - 100, this.game.world.centerY + 100, 'btn', this.onRegister, this, 'register.png', 'register.png', 'register.png');
        register.anchor.set(0.5);
    },

    onRegister:function(){
        this.state.start('Register');
    },

    onLogin: function () {
        var self = this;
        if (!this.username.value) {
            this.username.startFocus();
            this.errorText.text = '请输入用户名';
            return;
        }
        if (!this.password.value) {
            this.password.startFocus();
            this.errorText.text = '请输入密码';
            return;
        }
        $.ajax({
            url: PG.IPAddress + "/login",
            type: 'post',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                'username': this.username.value,
                'password': this.password.value
            }),
            success: function(msg){
                if(msg.status == true){
                    PG.playerInfo.username = msg.username;
                    PG.playerInfo.userid = msg.id;
                    PG.playerInfo.coins = msg.coins;
                    PG.playerInfo.win = msg.win;
                    PG.playerInfo.lose = msg.lose;
                    self.state.start("MainMenu");
                }
                else{
                    alert("输入的用户名或密码错误!");
                }
            }
        })
    }
};

PG.Register = {
    create: function () {
        this.stage.backgroundColor = '#182d3b';
        let bg = this.game.add.sprite(this.game.width / 2, 0, 'bg');
        bg.anchor.set(0.5, 0);

        let style = {
            font: '24px Arial', fill: '#000', width: 300, padding: 12,
            borderWidth: 1, borderColor: '#c8c8c8', borderRadius: 2,
            textAlign: 'center', placeHolder: '姓名'
        };
        this.game.add.plugin(PhaserInput.Plugin);

        this.username = this.game.add.inputField((this.game.world.width - 300) / 2, this.game.world.centerY - 160, style);

        style.placeHolder = '密码';
        this.password = this.game.add.inputField((this.game.world.width - 300) / 2, this.game.world.centerY - 90, style);

        style.placeHolder = '再次输入密码';
        this.passwordAgain = this.game.add.inputField((this.game.world.width - 300) / 2, this.game.world.centerY - 15, style);

        const errorStyle = {font: "22px Arial", fill: "#f00", align: "center"};
        this.errorText = this.game.add.text(this.game.world.centerX, this.game.world.centerY + 45, '', errorStyle);
        this.errorText.anchor.set(0.5, 0);

        let login = this.game.add.button(this.game.world.centerX, this.game.world.centerY + 100, 'btn', this.onLogin, this, 'register.png', 'register.png', 'register.png');
        login.anchor.set(0.5);
    },

    onLogin: function () {
        var self = this;
        if (!this.username.value) {
            this.username.startFocus();
            this.errorText.text = '请输入用户名';
            return;
        }
        if (!this.password.value) {
            this.password.startFocus();
            this.errorText.text = '请输入密码';
            return;
        }
        if (!this.passwordAgain.value) {
            this.passwordAgain.startFocus();
            this.errorText.text = '请再次输入密码';
            return;
        }
        if (this.password.value !== this.passwordAgain.value) {
            this.errorText.text = "两次输入的密码不一致";
            return;
        }
        $.ajax({
            url: PG.IPAddress + "/register",
            type: 'post',
            contentType: 'application/json; charset=UTF-8',
            data: JSON.stringify({
                'username': this.username.value,
                'password': this.password.value
            }),
            success: function(msg){
                if(msg.status == true){
                    PG.playerInfo.username = msg.username;
                    PG.playerInfo.userid = msg.id;
                    PG.playerInfo.coins = msg.coins;
                    PG.playerInfo.win = msg.win;
                    PG.playerInfo.lose = msg.lose;
                    self.state.start("MainMenu");
                }
                else{
                    alert("该用户名已存在!");
                }
            }
        })
    }
};

