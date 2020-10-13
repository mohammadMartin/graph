package pso;

import net.sourceforge.jswarm_pso.Swarm;

public class PSO {
    private static SchedulerParticle particles[];
    private static SchedulerFitnessFunction ff = new SchedulerFitnessFunction();
    private static Swarm swarm = new Swarm(Constants.POPULATION_SIZE, new SchedulerParticle(), ff);
    public PSO() {
        initParticles();
    }


    public double[] run() {

        swarm.setMinPosition(0);
        swarm.setMaxPosition(Constants.NO_OF_DATA_CENTERS - 1);
        swarm.setMaxMinVelocity(0.5);
        swarm.setParticles(particles);
        swarm.setParticleUpdate(new SchedulerParticleUpdate(new SchedulerParticle()));

        for (int i = 0; i < 500; i++) {
            swarm.evolve();
            if (i % 10 == 0) {
                System.out.printf("Gloabl best at iteration (%d): %f\n", i, swarm.getBestFitness());
            }
        }

        System.out.println("\nThe best fitness value: " + swarm.getBestFitness() + " Best makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));
         
        System.out.println("The best solution is: ");
        SchedulerParticle bestParticle = (SchedulerParticle) swarm.getBestParticle();
        System.out.println(bestParticle.toString());
        
        return swarm.getBestPosition();
    }
    
    public void printBestFitness() {
    	System.out.println("\nThe best fitness value: " + swarm.getBestFitness() + " Best makespan: " + ff.calcMakespan(swarm.getBestParticle().getBestPosition()));
    }
    
    public double[][] getCommunTimeMatrix() { return ff.getCoumnTimeMatrix(); }
    
    public double[][] getExecTimeMatrix() { return ff.getExecTimeMatrix(); }
    
    private static void initParticles() {
        particles = new SchedulerParticle[Constants.POPULATION_SIZE];
        for (int i = 0; i < Constants.POPULATION_SIZE; ++i)
            particles[i] = new SchedulerParticle();
    }
}
