import itertools
from copy import deepcopy
from elEstadoMundo import *
from searchPlus import *

# Mundo por defeito
estacao = (0, 0)
pedonais = {((4, 3), (4, 4))}
sentidoUnico = {((3, 3), (3, 2)), ((1, 2), (2, 2))}
intenso = {((3, 2), (3, 3))}
ligeiro = {}
tempos = {'ligeiro': 100, 'médio': 300, 'intenso': 1000}
tempoPassageiro = 10
clientes = {'Lou': ((1, 2), (3, 3)), 'Gagarin': ((1, 2), (2, 3)), 'Dubenka': ((1, 2), (2, 3))}

mundoDef = MundoBus(6, estacao, 4, pedonais, sentidoUnico, intenso, ligeiro, tempos, tempoPassageiro, clientes)


def manhattan(point1, point2):
    return abs(point2[0] - point1[0]) + abs(point2[1] - point1[1])


class OBus(Problem):
    tabMovs = {'e': (1, 0), 'n': (0, 1), 'o': (-1, 0), 's': (0, -1)}

    def __init__(self, initial=None, goal=None, mundo=mundoDef):
        """O objectivo é o tuplo ordenado por ordem crescente e o estado inicial é uma permutação de range(1, n+1).
        """
        self.mundo = mundo
        self.initial = EstadoBus(mundo.estacao, set(mundo.clientes.keys()), set())
        self.goal = EstadoBus(mundo.estacao, set(), set())

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

    def h1(self, node):
        street_time = 0
        if self.mundo.ligeiro:
            street_time = self.mundo.tempos.get('ligeiro')
        elif len(self.mundo.intenso) < (self.mundo.dim * (self.mundo.dim - 1) * 2) - len(self.mundo.pedonais):
            street_time = self.mundo.tempos.get('médio')
        else:
            street_time = self.mundo.tempos.get('intenso')
        passengers_time = (len(node.state.pickup) * 2 + len(node.state.destinos)) * self.mundo.tempoPassageiro
        distances = []
        for x in self.mundo.clientes:
            if x in node.state.pickup:
                distances.append(manhattan(node.state.pos, self.mundo.clientes[x][0]) +
                                 manhattan(self.mundo.clientes[x][0], self.mundo.clientes[x][1]) +
                                 manhattan(self.mundo.clientes[x][1], self.mundo.estacao))
            elif x in node.state.destinos:
                distances.append(manhattan(node.state.pos, self.mundo.clientes[x][1]) +
                                 manhattan(self.mundo.clientes[x][1], self.mundo.estacao))
        if not distances:
            distances.append(manhattan(node.state.pos, self.mundo.estacao))
        return max(distances) * street_time + passengers_time
