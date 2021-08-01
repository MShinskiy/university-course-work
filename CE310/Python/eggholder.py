"""
This algorithm is intended to optimise
Eggholder function

*Minimisation
Global Minima at f(512, 404.2319) = -959.6407
"""
import random, math

# Define representation: string
n_bits = 22  # -512 -- +512 / -512 -- +512
n_pop = 1000  # population size
n_gen = 30  # generations
p_xo = 0.8  # crossover rate
p_mut = 0.05  # mutation rate
n_sel = 2

pop = []

for i in range(n_pop):
    pop.append(''.join(random.choice('01') for j in range(n_bits)))


def dec(chromosome):
    dec = 0
    for i in range(len(chromosome)):
        if chromosome[i] == '1':
            dec += 2 ** i
    return dec


def fitness(chromosome):
    # slice chromosome
    half_cut = int(len(chromosome)/2)
    x = dec(chromosome[0:half_cut]) - 512
    y = dec(chromosome[half_cut:]) - 512

    if x > 512 or x < -512:
        return math.inf

    if y > 512 or y < -512:
        return math.inf

    # split function in parts for easier reading
    sqrt1 = math.sqrt(abs(x/2 + (y + 47)))
    sqrt2 = math.sqrt(abs(x - (y + 47)))

    return -(y + 47) * math.sin(sqrt1) - x * math.sin(sqrt2)


def show_pop():
    print('---------------------')
    f_avg = 0

    for p in pop:
        # slice chromosome
        half_cut = int(len(p) / 2)
        x = dec(p[0:half_cut]) - 512
        y = dec(p[half_cut:]) - 512
        print(p + ' ' + str(x) + ' ' + str(y) + ' ' + str(fitness(p)))
        f_avg = + fitness(p)
    print(f_avg)


def tournament(inverse):
    index = 0
    f_max = -100000000000
    f_min = +111111111111
    for counter in range(n_sel):
        index_i = random.randint(0, len(pop) - 1)
        f_i = fitness(pop[index_i])
        if inverse == False:
            if f_i > f_max:
                f_max = f_i
                index = index_i
        else:
            if f_i < f_min:
                f_min = f_i
                index = index_i

    return index


print('Initial population')
show_pop()

for generation in range(n_gen):
    for individual in range(n_pop):
        if random.random() < p_xo:
            # crossover
            index_individual_1 = tournament(True)
            index_individual_2 = tournament(True)
            cut = random.randint(1, n_bits - 1)  # random cut position
            offspring = pop[index_individual_1][0:cut] + pop[index_individual_2][cut:]

        else:
            # cloning
            offspring = pop[tournament(True)]

        # mutation
        mutation = ''
        for index in range(len(offspring)):
            if random.random() < p_mut:
                if offspring[index] == '0':
                    mutation += '1'
                else:
                    mutation += '0'
            else:
                mutation += offspring[index]

        # steady-state GA, put individual in the current population
        pop[tournament(False)] = offspring

    print('Generation ' + str(generation))
    show_pop()
