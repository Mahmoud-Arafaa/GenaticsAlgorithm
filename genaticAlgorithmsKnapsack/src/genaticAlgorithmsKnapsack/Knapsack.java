package genaticAlgorithmsKnapsack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Knapsack {

	private int candidates;
	private int maxWeight;
	private int weight [];
	private int value [];
	private ArrayList<String> population;
	private int GENERATION_NUM = 20000;
	private int POP_SIZE = 100;
	double fitness_sum;
	
	public Knapsack(int candidates, int maxWeight, int[] values, int[] weight) {
		this.candidates = candidates;
		this.maxWeight = maxWeight;
		this.value = values;
		this.weight = weight;
		GENERATION_NUM = candidates * 400;
		POP_SIZE = candidates * 100;
		population = new ArrayList<>();
	}
	
	public ArrayList<String> generatePop()
	{
		ArrayList<String> pop =new ArrayList<String>();
		for(int i = 0; i<POP_SIZE; i++) {
			String chromosom="";
			for(int j = 0; j<candidates; j++) {
				Random rand = new Random();
				chromosom += rand.nextInt(2);
			}
			pop.add(chromosom);
		}
		return pop;	
	}
	
	public double calcFitness(String chromosom) {
		double sumValue = 0;
		int sumWeight = 0;
		
		for(int i=0 ; i<candidates; i++) {
			if(chromosom.charAt(i) == '1') {
				sumValue += value[i];				
				sumWeight += weight[i];
			}
		}
		if(sumWeight > maxWeight) {
			sumValue = 1/sumValue;
		}
		return sumValue;
	}
	
	public void sumFitness(ArrayList<String> pop) {
		fitness_sum=0;
		for(int i=0 ; i<POP_SIZE; i++) {
			fitness_sum += calcFitness(pop.get(i));
		}
		
	}
	
	
	public double div(String chromosom, double fitness_sum) {
		return (calcFitness(chromosom)/fitness_sum);
	}
	
	public String roulettWheel() {
		double partial_sum = 0;
		
		Random rand = new Random();
		double r = rand.nextDouble();

		for(int i=0; i<POP_SIZE; i++) {
			partial_sum += div(population.get(i), fitness_sum);
			if(partial_sum >= r) {
				return population.get(i);
			}
		}

		return null;
	}
	
	public String[] crossOver(String individual1, String individual2) {
		String[] individuals = new String [2];
		int point = new Random().nextInt(candidates-1);
		String temp1 = individual1.substring(0, point+1);
		String temp2 = individual2.substring(0, point+1);
		temp1 += individual2.substring(point+1);
		temp2 += individual1.substring(point+1);
		
		individuals[0] = temp1;
		individuals[1] = temp2;
		
		return individuals;
	}
	
	public String mutation(String individual) {
		for(int i=0 ; i<candidates; i++) {
			float rand = new Random().nextFloat();
			if(rand > 0.9) {
				if(individual.charAt(i) == '1')
                    individual = individual.substring(0 , i) + '0' + individual.substring( i + 1);
				
				else
					individual = individual.substring(0 , i) + '1' + individual.substring( i + 1);
			}
		}
		return individual;
	}
	
	public String bestOfCandidates() {
		double best = -999.0;
		String bestCandidate="";
		for(int i=0; i<POP_SIZE; i++) {
			double fit = calcFitness(population.get(i));
			if(fit > best) {
				best = fit;
				bestCandidate = population.get(i);
			}
		}
		return bestCandidate;
	}
	
	public int worstOfCandidates(ArrayList<String> pop) {
		double worst = 99999;
		String worstCandidate = "";
		int idx = 0;
		for(int i=0; i<POP_SIZE; i++) {
			double fit = calcFitness(pop.get(i));
			if(fit < worst) {
				worst = fit;
				worstCandidate = pop.get(i);
				idx = i;
			}
		}
		return idx;
	}
	
	
	public int run() {
		
		population = generatePop();
		int maxVal = 0;
		for(int i=0; i<GENERATION_NUM; i++) {
			
			sumFitness(population);
			String best = bestOfCandidates();
			ArrayList<String> new_generation = new ArrayList<>();

			for(int j=0 ; j<POP_SIZE/2; j++) {
				String individual1 = roulettWheel();
				String individual2 = roulettWheel();
				while(individual1.equals(individual2))
				{
					individual2 = roulettWheel();
				}
				
				
				String[] result = crossOver(individual1, individual2);
				result[0] = mutation(result[0]);
				result[1] = mutation(result[1]);
				
				new_generation.add(result[0]);
				new_generation.add(result[1]);
			}
			int worst = worstOfCandidates(new_generation);
			new_generation.remove(worst);
			new_generation.add(best);
			
			population = new_generation;

			best = bestOfCandidates();
			maxVal = (int)calcFitness(best);
		}
		return maxVal;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
	
		Scanner scanner = new Scanner(new File("DataFile.txt"));

		
			int testCases = Integer.parseInt(scanner.nextLine());
			System.out.println(testCases);
			for(int i=0 ; i<testCases; i++) {
				scanner.nextLine();
				int candidates =Integer.parseInt( scanner.nextLine());//reader.readLine();

				int maxWeight =Integer.parseInt(scanner.nextLine());

				int [] values = new int[candidates];
				int [] weight= new int[candidates];
				
				for(int j=0 ; j< candidates; j++) {
					String[] line = scanner.nextLine().split(" ");
					values[j] = Integer.parseInt(line[1]);
					weight[j] = Integer.parseInt(line[0]);
				}
				
				scanner.nextLine();
				Knapsack knapSack = new Knapsack(candidates, maxWeight, values, weight);
				System.out.println("Case: "+(i+1)+"  "+knapSack.run());
			}
			}
		

}