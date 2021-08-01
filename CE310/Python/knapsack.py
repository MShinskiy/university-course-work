"""
This algorithm is intended to solve
'Knapsack' problem
"""
import random
# Knapsack configuration
value_i = [1, 5, 4, 7, 6, 3, 8, 10, 12, 4, 6, 7, 5, 7, 3, 1]     # sum = 89
weight_i = [2, 3, 6, 2, 12, 3, 5, 2, 20, 5, 8, 7, 12, 10, 2, 1]    # sum = 100

weight_max = 50

# Define representation: string
n_bits = len(weight_i)  # length of a bit-string
n_pop = 1000  # population size
n_gen = 30   # generations
p_xo = 0.8  # crossover rate
p_mut = 0.05  # mutation rate
n_sel = 2

pop = []

# Create initial population
for i in range(n_pop):
    pop.append(''.join(random.choice('01') for j in range(n_bits)))


# Fitness function
def fitness(chromosome):
    w_chromosome = 0    # weight of a chromosome
    v_chromosome = 0    # value of a chromosome
    n_items = str(chromosome).count('1')

    # Sum of weight
    for w in range(len(weight_i)):
        w_chromosome += weight_i[w]*int(chromosome[w])

    # Sum of value
    for v in range(len(value_i)):
        v_chromosome += value_i[v]*int(chromosome[v])

    # Do not allow overweight
    if w_chromosome > weight_max:
        return 0
    else:
        # Equally weighted fitness
        return v_chromosome**2 * w_chromosome
        # Prioritise fewer items
        # return int(v_chromosome**2 * w_chromosome / n_items)


def show_stats(f_max):
    w_chromosome = 0
    v_chromosome = 0
    w_array = []
    v_array = []
    for c in pop:
        if fitness(c) == f_max:
            # Sum of weight
            for w in range(len(weight_i)):
                wt = weight_i[w] * int(c[w])
                w_chromosome += wt
                if wt > 0:
                    w_array.append(wt)

            # Sum of value
            for v in range(len(value_i)):
                vl = value_i[v] * int(c[v])
                v_chromosome += vl
                if vl > 0:
                    v_array.append(vl)
        break
    print("(Max) Value: %d   Weight: %d/%d" % (v_chromosome, w_chromosome, weight_max))
    print("Weights:", w_array)
    print("Values: ", v_array)


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
    show_stats(f_max)


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
