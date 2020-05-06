import random

input = open(r"DataFile.txt")

test_case_num = int(input.readline())
candidates = 0
maxWeight = 0
weight = []
value = []
pop = []
fitness = []
fitnessSum = 0
GEERATION_NUM = 20000
POP_SIZE = 100


print("Test cases: ", test_case_num)


def generatePOP():
    population = []
    for i in range(POP_SIZE):
        chromosom = ""
        for j in range(candidates):
            r = random.randint(0,1)
            chromosom = chromosom + str(r)
        population.append(chromosom)
    
    return population


def calcFitness(population):
    fitness = []
    for j in range(POP_SIZE):
        idx = 0
        sumValues = 0
        sumWeight = 0
        for x in population[j]:
            if (x == "1"):
                sumValues = value[idx] + sumValues
                sumWeight = weight[idx] + sumWeight
            idx = idx + 1
        if sumWeight > maxWeight:
            fitness.append(1 / sumValues)
        else:
            fitness.append(sumValues)
    return fitness


def fitnessSum(fitness):
    index = 0
    fitnessSum = 0
    for i in fitness:
        fitnessSum = fitnessSum + i
    for i in fitness:
        fitness[index] = float(i / fitnessSum)
        index = index + 1
    return fitness


def roulettWheel():
    individuals = []
    while len(individuals) < 2:
        idx = 0
        ran = random.random() * 1
        partial_sum = 0
        for i in fitness:
            partial_sum = partial_sum + i
            if partial_sum > ran:
                if pop[idx] in individuals:
                    break
                individuals.append(pop[idx])
                break
            idx = idx + 1
    return individuals


def bestOfCandidates(fitness):
    best = -99
    idx = -1
    for i in range(len(fitness)):
        if fitness[i] > best:
            idx = i
            best = fitness[i]
    return idx

def worstOfCandidates(fitness):
    worst = 99999999999
    idx = -1
    for i in range(len(fitness)):
        if fitness[i] < worst:
            idx = i
            best = fitness[i]
    return idx


def cross_over(individuals):
    point = random.randint(1, candidates-1)
    temp1 = individuals[0][0:point]
    temp2 = individuals[1][0:point]
    temp1 = temp1 + individuals[1][point:candidates]
    temp2 = temp2 + individuals[0][point:candidates]
    individuals[0] = temp1
    individuals[1] = temp2
    return individuals


def mutaion(generation):
    idx = -1
    for i in generation:
        idx = idx + 1
        for j in range(len(i)):
            rand = random.uniform(0, 1)
            if rand > 0.6:
                temp = list(generation[idx])
                if temp[j] == '0':
                    temp[j] = '1'
                else:
                    temp[j] = '0'
                generation[idx] = "".join(temp)
    return generation



for i in range(test_case_num):
    string = input.readline()
    if "\n" is string:
        string = input.readline()
    candidates = int(string)

    string = input.readline()
    if "\n" is string:
        string = input.readline()
    maxWeight = int(string)

    value = []
    weight = []
    for j in range(candidates):
        string = input.readline()
        if "\n" is string:
            string = input.readline()
        s = string.split(" ")
        value.append(int(s[1]))
        weight.append(int(s[0]))

    
    
    input.readline()

    pop = generatePOP()

    
    for x in range(GEERATION_NUM):
        fitness = calcFitness(pop)
        best = pop[bestOfCandidates(fitness)]
        fitnessSummation = fitnessSum(fitness)
        new_generation = []
        for z in range(int(POP_SIZE/2)):
            individuals = roulettWheel()
            new_generation.append(cross_over(individuals)[0])
            new_generation.append(cross_over(individuals)[1])

        new_generation = mutaion(new_generation)
        fit = calcFitness(new_generation)
        new_generation.remove(new_generation[worstOfCandidates(fit)])
        new_generation.append(best)
        pop = new_generation
        fit = calcFitness(pop)
        best = bestOfCandidates(fit)
    
    print("case", i+1, ": ", fit[best],"   /  ", pop[best])