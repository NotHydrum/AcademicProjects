from searchPlus import *
from elEstadoMundo import *

from itertools import combinations
from copy import deepcopy

# Mundo por defeito
estacao = (0, 0)
pedonais = {((4, 3), (4, 4))}
sentidoUnico = {((3, 3), (3, 2)), ((1, 2), (2, 2))}
intenso = {((3, 2), (3, 3))}
ligeiro = {((2, 1), (2, 2))}
tempos = {'ligeiro': 100, 'médio': 300, 'intenso': 1000}
tempoPassageiro = 10
clientes = {1: ((1, 2), (3, 3)), 2: ((1, 2), (2, 3))}

mundoDef = MundoBus(5, estacao, 2, pedonais, sentidoUnico, intenso, ligeiro, tempos, tempoPassageiro, clientes)


class OBus(Problem):

    def __init__(self, initial=None, goal=None, mundo=mundoDef):
        if initial is None:
            clients = set()
            for x in mundo.clientes.keys():
                clients.add(x)
            initial = EstadoBus(mundo.estacao, clients, set())
        if goal is None:
            goal = EstadoBus(mundo.estacao, set(), set())
        super().__init__(initial, goal)
        self.mundo = mundo

    def actions(self, state):
        actions = list()
        deliver_set = list()
        for x in state.destinos:
            if state.pos == self.mundo.clientes.get(x)[1]:
                deliver_set.append(x)
        if len(deliver_set) > 1:
            deliver_set.sort(key=lambda z: sorted(z))
        can_pickup = list()
        for x in state.pickup:
            if state.pos == self.mundo.clientes.get(x)[0]:
                can_pickup.append(x)
        pickup_sets = list()
        if 0 < len(can_pickup) <= (self.mundo.capacidade - len(state.destinos) + len(deliver_set)) > 0:
            pickup_sets.append(can_pickup)
        elif 0 < len(can_pickup) > (self.mundo.capacidade - len(state.destinos) + len(deliver_set)) > 0:
            pickup_sets = list(combinations(can_pickup, self.mundo.capacidade - len(state.destinos) + len(deliver_set)))
            pickup_sets.sort(key=lambda z: sorted(z))
        if len(pickup_sets) > 0:
            for x in pickup_sets:
                actions.append((set(deliver_set), set(x)))
        else:
            actions.append((deliver_set, set()))
        if state.pos[0] + 1 < self.mundo.dim:
            valid = True
            for x in self.mundo.pedonais:
                if state.pos == x[0] and (state.pos[0] + 1, state.pos[1]) == x[1]:
                    valid = False
            for x in self.mundo.sentidoUnico:
                if state.pos == x[1] and (state.pos[0] + 1, state.pos[1]) == x[0]:
                    valid = False
            if valid:
                actions.append('e')
        if state.pos[1] + 1 < self.mundo.dim:
            valid = True
            for x in self.mundo.pedonais:
                if state.pos == x[0] and (state.pos[0], state.pos[1] + 1) == x[1]:
                    valid = False
            for x in self.mundo.sentidoUnico:
                if state.pos == x[1] and (state.pos[0], state.pos[1] + 1) == x[0]:
                    valid = False
            if valid:
                actions.append('n')
        if state.pos[0] - 1 >= 0:
            valid = True
            for x in self.mundo.pedonais:
                if state.pos == x[1] and (state.pos[0] - 1, state.pos[1]) == x[0]:
                    valid = False
            for x in self.mundo.sentidoUnico:
                if state.pos == x[1] and (state.pos[0] - 1, state.pos[1]) == x[0]:
                    valid = False
            if valid:
                actions.append('o')
        if state.pos[1] - 1 >= 0:
            valid = True
            for x in self.mundo.pedonais:
                if state.pos == x[1] and (state.pos[0], state.pos[1] - 1) == x[0]:
                    valid = False
            for x in self.mundo.sentidoUnico:
                if state.pos == x[1] and (state.pos[0], state.pos[1] - 1) == x[0]:
                    valid = False
            if valid:
                actions.append('s')
        return actions

    def result(self, state, action):
        pos = (state.pos[0], state.pos[1])
        pickup = deepcopy(state.pickup)
        destinos = deepcopy(state.destinos)
        if action == 'n':
            pos = (pos[0], pos[1] + 1)
        elif action == 's':
            pos = (pos[0], pos[1] - 1)
        elif action == 'e':
            pos = (pos[0] + 1, pos[1])
        elif action == 'o':
            pos = (pos[0] - 1, pos[1])
        else:
            for x in action[0]:
                destinos.remove(x)
            for x in action[1]:
                pickup.remove(x)
                destinos.add(x)
        return EstadoBus(pos, pickup, destinos)

    def goal_test(self, state):
        return state.pos == self.goal.pos and state.pickup == self.goal.pickup and state.destinos == self.goal.destinos

    def path_cost(self, c, state1, action, state2):
        if action == 'e' or action == 'n' or action == 'o' or action == 's':
            for x in self.mundo.ligeiro:
                if (state1.pos == x[0] and state2.pos == x[1]) or (state1.pos == x[1] and state2.pos == x[0]):
                    return c + self.mundo.tempos.get('ligeiro')
            for x in self.mundo.intenso:
                if (state1.pos == x[0] and state2.pos == x[1]) or (state1.pos == x[1] and state2.pos == x[0]):
                    return c + self.mundo.tempos.get('intenso')
            return c + self.mundo.tempos.get('médio')
        else:
            return c + self.mundo.tempoPassageiro * (len(action[0]) + len(action[1]))
