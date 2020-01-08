# -*- coding: utf-8 -*-

"""
Created on Mon Jul 17 09:18:19 2017
@author: XuGang
"""
from __future__ import absolute_import
from game.agent import Agent

from flask import Flask
from flask import request
from datetime import timedelta
from flask import make_response, request, current_app, send_from_directory
from functools import update_wrapper
import os
import json

os.environ["CUDA_VISIBLE_DEVICES"] = '0'

def crossdomain(origin=None, methods=None, headers=None,
                max_age=21600, attach_to_all=True,
                automatic_options=True):

    try:
        basestring
    except NameError:
        basestring = str
    
    if methods is not None:
        methods = ', '.join(sorted(x.upper() for x in methods))
    if headers is not None and not isinstance(headers, basestring):
        headers = ', '.join(x.upper() for x in headers)
    if not isinstance(origin, basestring):
        origin = ', '.join(origin)
    if isinstance(max_age, timedelta):
        max_age = max_age.total_seconds()

    def get_methods():
        if methods is not None:
            return methods
        options_resp = current_app.make_default_options_response()
        return options_resp.headers['allow']

    def decorator(f):
        def wrapped_function(*args, **kwargs):
            if automatic_options and request.method == 'OPTIONS':
                resp = current_app.make_default_options_response()
            else:
                resp = make_response(f(*args, **kwargs))
            if not attach_to_all and request.method != 'OPTIONS':
                return resp
            h = resp.headers
            h['Access-Control-Allow-Origin'] = origin
            h['Access-Control-Allow-Methods'] = get_methods()
            h['Access-Control-Max-Age'] = str(max_age)
            if headers is not None:
                h['Access-Control-Allow-Headers'] = headers
            return resp
        f.provide_automatic_options = False
        return update_wrapper(wrapped_function, f)
    return decorator

app = Flask(__name__, static_url_path='')

@app.route('/init_match',methods=['POST','GET'])
# @crossdomain(origin='*')
def init_match():
    global agent
    data = json.loads(request.data)
    agent.new_game_init(data['cards'], train=False)
    agent.game.get_next_moves()
    return ''

@app.route('/be_landlord',methods=['POST','GET'])
@crossdomain(origin='*')
def be_landlord():
    return '{"value": 2}'

@app.route('/landlord_info',methods=['POST','GET'])
@crossdomain(origin='*')
def landlord_info():
    global agent
    data = json.loads(request.data)
    if int(data['player_id']) == 0:
        agent.add_new_card(data['cards'], 0)
    return ''


@app.route('/play',methods=['POST','GET'])
@crossdomain(origin='*')
def play():
    global agent
    data = json.loads(request.data)
    agent.use_new_card(data['last_move'], data['last_move_type'])
    if data['your_turn'] == 'true':
        move, move_type = agent.new_next_move()
        result = {}
        result['move'] = move
        result['move_type'] = move_type
        return json.dumps(result)
    else:
        return ''


# @app.route('/',methods=['GET'])
# @crossdomain(origin='*')
# def next_move():
#     return app.send_static_file('index.html')

if __name__ == '__main__':
    global agent
    agent = Agent()
    app.run(host='127.0.0.1', port=5000, threaded=True)
