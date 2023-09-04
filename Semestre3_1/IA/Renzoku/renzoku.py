from csp import *


def CSP_renzoku(dimension: int, initials: dict, dots):

    def renzoku_constraint(variable_a, value_a, variable_b, value_b):
        if value_a != value_b:
            if variable_a[0] + 1 != variable_b[0] and variable_a[0] - 1 != variable_b[0] and \
                    variable_a[1] + 1 != variable_b[1] and variable_a[1] - 1 != variable_b[1]:
                return True
            elif ((variable_a, variable_b) in dots or (variable_b, variable_a) in dots) and \
                    value_b in [value_a - 1, value_a + 1]:
                return True
            elif (variable_a, variable_b) not in dots and (variable_b, variable_a) not in dots and \
                    value_b not in [value_a - 1, value_a + 1]:
                return True
        return False

    variables = [(l, c) for l in range(1, dimension + 1) for c in range(1, dimension + 1)]
    domains = dict()
    for l in range(1, dimension + 1):
        for c in range(1, dimension + 1):
            if (l, c) in initials:
                domains[(l, c)] = [initials.get((l, c))]
            else:
                domains[(l, c)] = range(1, dimension + 1)
    neighbors = dict()
    for l in range(1, dimension + 1):
        for c in range(1, dimension + 1):
            neighbors[(l, c)] = [(l, c2) for c2 in range(1, c)] + [(l, c2) for c2 in range(c + 1, dimension + 1)] + \
                                [(l2, c) for l2 in range(1, l)] + [(l2, c) for l2 in range(l + 1, dimension + 1)]
    constraints = renzoku_constraint
    return CSP(variables, domains, neighbors, constraints)
