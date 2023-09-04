from searchPlus import *
from elEstadoMundo import *
import itertools
from copy import deepcopy


def manhattan(point1, point2):
    return abs(point2[0] - point1[0]) + abs(point2[1] - point1[1])


def planoImpaciente(mundo):
    bus = OBus(mundo=mundo)
    state = bus.initial

    if bus.goal_test(state):
        return [], 0

    elif not state[1] and not state[2]:
        pickups = uniform_cost_search(bus)
        return pickups.solution(), pickups.path_cost

    else:
        actions = []
        cost = 0
        pickup = set()
        for x in mundo.clientes:
            pickup.add(x)
        state = EstadoBus(mundo.estacao, pickup, set())
        while state.pickup or state.destinos:
            closest_obj = closest(mundo, state)
            world = MundoBus(mundo.dim, closest_obj, mundo.capacidade, mundo.pedonais, mundo.sentidoUnico,
                             mundo.intenso, mundo.ligeiro, mundo.tempos, mundo.tempoPassageiro, set())
            bus = OBus(initial=EstadoBus(state.pos, set(), set()), mundo=world)
            best = uniform_cost_search(bus)
            actions += best.solution()
            cost += best.path_cost()
            state.pos = closest_obj[0]
            diff = (set(), set())
            for x in state.pickup:
                if mundo.clientes.get(x)[0] == state.pos:
                    state.pickup.remove(x)
                    state.destinos.add(x)
                    cost += mundo.tempoPassageiro
                    diff[1].add(x)
            for x in state.destinos:
                if mundo.clientes.get(x)[1] == state.pos:
                    state.destinos.remove(x)
                    cost += mundo.tempoPassageiro
                    diff[0].add(x)
        return actions, cost


def closest(mundo, state):
    client = ""
    pos = (2147483647, 2147483647)
    dist = 2147483647
    for x in state.pickup:
        new_pos = mundo.clientes.get(x)[0]
        new_dist = manhattan(state.pos, new_pos)
        if new_dist < dist or (new_dist == dist and new_pos[0] < pos[0]) or \
                (new_dist == dist and new_pos[0] == pos[0] and new_pos[1] < pos[1]):
            client = x
            pos = new_pos
            dist = new_dist
    for x in state.destinos:
        new_pos = mundo.clientes.get(x)[1]
        new_dist = manhattan(state.pos, new_pos)
        if new_dist < dist or (new_dist == dist and new_pos[0] < pos[0]) or \
                (new_dist == dist and new_pos[0] == pos[0] and new_pos[1] < pos[1]):
            client = x
            pos = new_pos
            dist = new_dist
    return pos, client


# Mundo por defeito
estacao = (0, 0)
pedonais = {((4, 3), (4, 4))}
sentidoUnico = {((3, 3), (3, 2)), ((1, 2), (2, 2))}
intenso = {((3, 2), (3, 3))}
ligeiro = {}
tempos = {'ligeiro': 100, 'médio': 300, 'intenso': 1000}
tempoPassageiro = 10
clientes = {'Lou': ((1, 2), (3, 3)), 'Gagarin': ((1, 2), (2, 3)), 'Dubenka': ((1, 2), (2, 3))}

mundoDef = MundoBus(5, estacao, 2, pedonais, sentidoUnico, intenso, ligeiro, tempos, tempoPassageiro, clientes)


class OBus(Problem):
    tabMovs = {'e': (1, 0), 'n': (0, 1), 'o': (-1, 0), 's': (0, -1)}

    def __init__(self, initial=None,goal=None, mundo=None):
        self.mundo = mundo
        if initial is None:
            clients = set()
            for x in self.mundo.clientes.keys():
                clients.add(x)
            initial = EstadoBus(mundo.estacao,clients, set())
        if goal is None:
            goal= EstadoBus(mundo.estacao, set(), set())
        super().__init__(initial,goal)

    def move(pos, accao):
        x, y = pos
        incX, incY = OBus.tabMovs[accao]
        return ((x + incX), (y + incY))

    def actions(self, state):
        """ As acções aplicáveis
        """
        out = []
        for a in ["e", "n", "o", "s"]:
            x, y = state.pos
            incX, incY = OBus.tabMovs[a]
            new = ((x + incX), (y + incY))
            if 0 <= (x + incX) < self.mundo.dim and 0 <= (y + incY) < self.mundo.dim and \
                    (state.pos, new) not in self.mundo.pedonais and \
                    (new, state.pos) not in self.mundo.pedonais and \
                    (new, state.pos) not in self.mundo.sentidoUnico:
                out.append(a)
        ## all that should drop will drop
        droppingNum = 0
        drops = set()
        for d in state.destinos:
            if self.mundo.clientes[d][1] == state.pos:
                drops.add(d)
                droppingNum += 1

                ## the most that can be picked up will be picked up
        ## that depends on the bus capacity
        allPossiblePickups = [p for p in state.pickup if self.mundo.clientes[p][0] == state.pos]
        allPossiblePickups.sort()  # ordena

        freePlacesNum = self.mundo.capacidade - (len(state.destinos) - droppingNum)

        pickupNum = min(freePlacesNum, len(allPossiblePickups))
        if pickupNum != 0:
            acts = [(drops, set(x)) for x in (itertools.combinations(allPossiblePickups, pickupNum))]
        elif drops != set():
            acts = [(drops, set())]
        else:
            acts = []
        return acts + out

    def result(self, estado, accao):
        """Acções de movimento e de pickup and drop.
            "n", "s", "l", "o" e (pickupSet,DestinosSet)
        """

        if type(accao) == str:
            newPos = OBus.move(estado.pos, accao)
            return EstadoBus(newPos, estado.pickup, estado.destinos)
        else:
            drop, pickingUp = accao

            # dropping all people that go out of the bus here and
            # the pickupers will be destinies
            moreDestinos = pickingUp.copy()
            newDestinos = (estado.destinos.copy() - drop).union(moreDestinos)

            # update pickup
            newPickup = deepcopy(estado.pickup) - pickingUp
            newPos = estado.pos

        return EstadoBus(newPos, newPickup, newDestinos)

    def path_cost(self, c, state1, action, next_state):
        """ As acções de movimento têm custos associados ao tempo para a travessia que depende do tipo de rua
            As acções de apanhar/largar pessoas têm um custo que corresponde ao número de pessoas que saiem e entram
            a multiplicar pelo tempo médio que demora cada pessoa a entrar ou sair.
        """
        if type(action) == str:
            if (state1.pos, next_state.pos) in self.mundo.ligeiro or (next_state.pos, state1.pos) in self.mundo.ligeiro:
                custo = self.mundo.tempos['ligeiro']
            elif (state1.pos, next_state.pos) in self.mundo.intenso or (
                    next_state.pos, state1.pos) in self.mundo.intenso:
                custo = self.mundo.tempos['intenso']
            else:
                custo = self.mundo.tempos['médio']
            return c + custo
        return c + (len(action[0]) + len(action[1])) * self.mundo.tempoPassageiro

    def prettyAction(self, a):
        a1, a2 = a
        a1 = list(a1)
        a1.sort()
        a2 = list(a2)
        a2.sort()
        return ((a1, a2))

    def prettyPlan(self, plan):
        prettyP = []
        for a in plan:
            if type(a) == str:
                prettyP.append(a)
            else:
                prettyP.append(self.prettyAction(a))
        return prettyP
