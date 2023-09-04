# Grupo 69420
# Henrique Catarino - 56278
# Miguel Nunes - 56338
# Pedro Lourenço - 56360

from collections import namedtuple
from copy import deepcopy
from jogos import *
from jogar import *

StateBT = namedtuple('StateBT', ['whites', 'blacks', 'to_move'])


class JogoBT_69420(Game):

    def __init__(self):
        letters = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h']
        self.board_positions = set((x, y) for x in letters for y in range(1, 9))

        self.initial = StateBT(set((x, y) for x in letters for y in range(1, 3)),
                               set((x, y) for x in letters for y in range(7, 9)),
                               1)  # 1 for white, 2 for black

    def actions(self, state):
        if not self.terminal_test(state):
            actions = list()
            if state.to_move == 1:
                for (x, y) in state.whites:
                    if x != 'a':
                        left = (chr(ord(x) - 1), y + 1)
                        if left not in state.whites:
                            actions.append(self.action_to_string((x, y), left))
                    center = (x, y + 1)
                    if center not in state.whites.union(state.blacks):
                        actions.append(self.action_to_string((x, y), center))
                    if x != 'h':
                        right = (chr(ord(x) + 1), y + 1)
                        if right not in state.whites:
                            actions.append(self.action_to_string((x, y), right))
            else:
                for (x, y) in state.blacks:
                    if x != 'a':
                        left = (chr(ord(x) - 1), y - 1)
                        if left not in state.blacks:
                            actions.append(self.action_to_string((x, y), left))
                    center = (x, y - 1)
                    if center not in state.whites.union(state.blacks):
                        actions.append(self.action_to_string((x, y), center))
                    if x != 'h':
                        right = (chr(ord(x) + 1), y - 1)
                        if right not in state.blacks:
                            actions.append(self.action_to_string((x, y), right))
            actions.sort()
            return actions
        else:
            return list()

    def terminal_test(self, state):
        if not state.whites or not state.blacks:
            return True
        for (x, y) in state.whites:
            if y == 8:
                return True
        for (x, y) in state.blacks:
            if y == 1:
                return True
        return False

    def result(self, state, move):
        (pos1, pos2) = self.string_to_action(move)
        if state.to_move == 1:
            result_whites = deepcopy(state.whites)
            result_whites.remove(pos1)
            result_whites.add(pos2)
            result_blacks = deepcopy(state.blacks)
            if pos2 in result_blacks:
                result_blacks.remove(pos2)
            return StateBT(result_whites, result_blacks, 2)
        else:
            result_blacks = deepcopy(state.blacks)
            result_blacks.remove(pos1)
            result_blacks.add(pos2)
            result_whites = deepcopy(state.whites)
            if pos2 in result_whites:
                result_whites.remove(pos2)
            return StateBT(result_whites, result_blacks, 1)

    def executa(self, state: StateBT, valid_move_list):
        result = state
        for move in valid_move_list:
            (pos1, pos2) = self.string_to_action(move)
            result = self.result(result, move)
        return result

    @staticmethod
    def action_to_string(pos1, pos2):
        return pos1[0] + str(pos1[1]) + '-' + pos2[0] + str(pos2[1])

    @staticmethod
    def string_to_action(move):
        return (move[0:1], int(move[1:2])), (move[3:4], int(move[4:5]))

    def utility(self, state, player):
        for (x, y) in state.whites:
            if y == 8 and player == 1:
                return 1
            elif y == 8:
                return -1
        for (x, y) in state.blacks:
            if y == 1 and player == 2:
                return 1
            elif y == 8:
                return -1
        return 0

    def display(self, state):
        print('-----------------')
        for y in reversed(range(1, 9)):
            line = str(y) + '|'
            for x in ['a', 'b', 'c', 'd', 'e', 'f', 'g']:
                if (x, y) in state.whites:
                    line += 'W '
                elif (x, y) in state.blacks:
                    line += 'B '
                else:
                    line += '. '
            if ('h', y) in state.whites:
                line += 'W'
            elif ('h', y) in state.blacks:
                line += 'B'
            else:
                line += '.'
            print(line)
        print('-+---------------')
        print(' |a b c d e f g h')
        finished = self.terminal_test(state)
        if not finished and state.to_move == 1:
            print('--NEXT PLAYER: W')
        elif not finished:
            print('--NEXT PLAYER: B')


class jogadorBT_69420_random(Jogador):
    def __init__(self, nome):
        self.nome = nome
        self.fun = random_player


class jogadorBT_69420_first(Jogador):
    def __init__(self, nome):
        self.nome = nome
        self.fun = first_player


def first_player(game, state):
    return game.actions(state)[0]


class jogadorBT_69420_o(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_o)


def func_aval_69420_o(state, player):
    value = 0
    if player == 1:
        for (_, y) in state.whites:
            value += y
    else:
        for (_, y) in state.blacks:
            value += 9 - y
    return value


class jogadorBT_69420_osqr(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_osqr)


def func_aval_69420_osqr(state, player):
    value = 0
    if player == 1:
        for (_, y) in state.whites:
            value += y ** 2
    else:
        for (_, y) in state.blacks:
            value += (9 - y) ** 2
    return value


class jogadorBT_69420_belarmino(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_belarmino)


def func_aval_69420_belarmino(state, player):
    value = 0
    if player == 1:
        for (_, y) in state.whites:
            value += pow(y, y)
    else:
        for (_, y) in state.blacks:
            value += pow((9 - y), (9 - y))
    return value


class jogadorBT_69420_d(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_d)


def func_aval_69420_d(state, player):
    value = 0
    if player == 1:
        for (_, y) in state.whites:
            if 9 - y > value:
                value = 9 - y
        for (_, y) in state.blacks:
            if y == 1:
                return -999
    else:
        for (_, y) in state.blacks:
            if y > value:
                value = y
        for (_, y) in state.whites:
            if y == 8:
                return -999
    return value


class jogadorBT_69420_dsqr(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_dsqr)


def func_aval_69420_dsqr(state, player):
    value = 0
    if player == 1:
        for (_, y) in state.whites:
            if (9 - y) ** 2 > value:
                value = (9 - y) ** 2
    else:
        for (_, y) in state.blacks:
            if y ** 2 > value:
                value = y ** 2
    return value


class jogadorBT_69420_m(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_m)


def func_aval_69420_m(state, player):
    value = 0
    if player == 1:
        for (_, y) in state.whites:
            value += y
        for (_, y) in state.blacks:
            value -= (9 - y)
    else:
        for (_, y) in state.blacks:
            value += (9 - y)
        for (_, y) in state.whites:
            value -= y
    return value


class jogadorBT_69420_msqr3(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_msqr3)


def func_aval_69420_msqr3(state, player):
    value = 0
    if player == 1:
        for (_, y) in state.whites:
            value += y * y * y
        for (_, y) in state.blacks:
            value -= (9 - y) * (9 - y) * (9 - y)
    else:
        for (_, y) in state.blacks:
            value += (9 - y) * (9 - y) * (9 - y)
        for (_, y) in state.whites:
            value -= y * y * y
    return value


class jogadorBT_69420_msqrn(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_msqrn)


def func_aval_69420_msqrn(state, player):
    value = 0
    if player == 1:
        for (_, y) in state.whites:
            value += pow(y, y)
        for (_, y) in state.blacks:
            value -= pow(9 - y, 9 - y)
    else:
        for (_, y) in state.blacks:
            value += pow(9 - y, 9 - y)
        for (_, y) in state.whites:
            value -= pow(y, y)
    return value


class jogadorBT_69420_fsqrn(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_fsqrn)


def func_aval_69420_fsqrn(state, player):
    value = 0
    enemy_value = 0
    if player == 1:
        for (_, y) in state.whites:
            if y * y > value:
                value = y * y
        for (_, y) in state.blacks:
            if (9 - y) * (9 - y) > enemy_value:
                enemy_value = (9 - y) * (9 - y)
    else:
        for (_, y) in state.blacks:
            if (9 - y) * (9 - y) > value:
                value = (9 - y) * (9 - y)
        for (_, y) in state.whites:
            if y * y > enemy_value:
                enemy_value = y * y
    return value - enemy_value


class jogadorBT_69420_bsqrn(JogadorAlfaBeta):
    def __init__(self, nome, depth):
        self.nome = nome
        self.fun = lambda game, state: alphabeta_cutoff_search_new(state, game, depth,
                                                                   eval_fn=func_aval_69420_bsqrn)


def func_aval_69420_bsqrn(state, player):
    value = 0
    if player == 1:
        for (_, y) in state.whites:
            if y >= 4:
                value += pow(y - 3, y - 3)
            elif y <= 3:
                value += pow(4 - y, 4 - y)
        for (_, y) in state.blacks:
            value -= pow(9 - y, 9 - y)
    else:
        for (_, y) in state.blacks:
            if y <= 5:
                value += pow(6 - y, 6 - y)
            elif y >= 6:
                value += pow(y - 5, y - 5)
        for (_, y) in state.whites:
            value -= pow(y, y)
    return value
