"""
This algorithm is intended to solve
'Maximise the ones' problem
"""
import random

# Define representation: string
n_bits = 10  #
n_pop = 100  # population size
n_gen = 100  # generations
p_xo = 0.8  # crossover rate
p_mut = 0.05  # mutation rate
n_sel = 2

pop = []

# Create initial population
for i in range(n_pop):
    pop.append(''.join(random.choice('01') for j in range(n_bits)))


# Binary --> Decimal
def dec(chromosome):
    dec = 0
    for i in range(len(chromosome)):
        if chromosome[i] == '1':
            dec += 2 ** i
    return dec


# Fitness function
def fitness(chromosome):
    return str(chromosome).count('1')


# Print population
def show_pop():
    print('---------------------')
    f_max = -100000
    f_avg = 0
    f_min = +100000
    for p in pop:
        f = fitness(p)
        if f > f_max:
            f_max = f
        if f < f_min:
            f_min = f
        print(p + ' ' + str(f))
        f_avg = + f
    print("f max:", f_max, " f min:", f_min, " f avg:", f_avg)


# Do tournament selection
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

# Main loop
for generation in range(n_gen):
    for individual in range(n_pop):
        if random.random() < p_xo:
            # crossover
            index_individual_1 = tournament(False)
            index_individual_2 = tournament(False)
            cut = random.randint(1, n_bits - 1)  # random cut position
            offspring = pop[index_individual_1][0:cut] + pop[index_individual_2][cut:]

        else:
            # cloning
            offspring = pop[tournament(False)]

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
        pop[tournament(True)] = offspring

    print('Generation ' + str(generation))
    show_pop()
