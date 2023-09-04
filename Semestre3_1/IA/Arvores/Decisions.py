from copy import deepcopy
from math import log2


def transformaPontos(dots: dict, intervals: dict):
    for dot in dots:
        data = list(dots[dot])
        for i in range(0, len(dots[dot]) - 1):
            interval = deepcopy(intervals[i])
            done = False
            if data[i] < interval[0]:
                data[i] = 1
                done = True
            for f in range(1, len(interval)):
                if not done and data[i] < interval[f]:
                    data[i] = f + 1
                    done = True
            if not done:
                data[i] = len(interval) + 1
        dots[dot] = tuple(data)
    return dots


def escolheAtributo(data: dict, select_from: list, values: list, classes: list, verbose=False):
    divided_lists = list()
    entropies_list = list()
    for attribute in deepcopy(select_from):
        if verbose:
            print('---> Vamos verificar o atributo com índice: ', attribute, sep='')
        divided = list()
        for value in deepcopy(values[attribute]):
            if verbose:
                print('filtro os dados para o atributo ', attribute, ' = ', value, sep='')
            filtered = dict()
            for dot in deepcopy(data):
                if data[dot][attribute] == value:
                    filtered[dot] = data[dot]
            if verbose:
                print(filtered)
            divided.append(filtered)
        divided_lists.append(divided)
        distributions = list()
        for filtered in divided:
            dist = list()
            for i in range(0, len(classes)):
                dist.append(0)
            for dot in filtered:
                for i in range(0, len(classes)):
                    if filtered[dot][-1] == classes[i]:
                        dist[i] += 1
            distributions.append(dist)
        if verbose:
            print('Distribuição dos pontos pelas classes: ', distributions, sep='')
        entropies = list()
        for dist in distributions:
            entropy_parts = list()
            if verbose:
                print('entropia(', dist, ')=', end='', sep='')
            for i in dist:
                if i != 0:
                    entropy_parts.append((-i / sum(dist)) * log2(i / sum(dist)))
                else:
                    entropy_parts.append(0.0)
                if verbose:
                    print('-', i, '/', sum(dist), '.log2(', i, '/', sum(dist), ')', end='', sep='')
            entropy = sum(entropy_parts)
            if verbose:
                print('=', rounded_string(entropy), sep='')
            entropies.append(entropy)
        avg_entropy = 0
        for i in range(0, len(entropies)):
            avg_entropy += (sum(distributions[i]) / len(data)) * entropies[i]
        if verbose:
            print('entropiaMédia(', distributions, ')=', end='', sep='')
            for i in range(0, len(entropies) - 1):
                print(sum(distributions[i]), '/', len(data), 'x', rounded_string(entropies[i]), '+', end='', sep='')
            print(sum(distributions[-1]), '/', len(data), 'x', rounded_string(entropies[-1]), '=',
                  rounded_string(avg_entropy), sep='')
        entropies_list.append((attribute, round(avg_entropy, 4)))
    best_entropy_index = 0
    for i in range(1, len(entropies_list)):
        if entropies_list[i][1] < entropies_list[best_entropy_index][1]:
            best_entropy_index = i
    return entropies_list, entropies_list[best_entropy_index], divided_lists[best_entropy_index]


def rounded_string(number):
    result = format(number, '.4f')
    if result[-1] == '0':
        result = result[0:-1]
        if result[-1] == '0':
            result = result[0:-1]
            if result[-1] == '0':
                result = result[0:-1]
    return result
