public class Warehouse
{
    double fixedcost;
    int capacity;
    int remaining_capacity=1000;
    int open;
    double costs[];
    double pheromone;
    public Warehouse()
    {
        open=0;
    }

    @Override
    public String toString() {
        String cst = "";
        for(int i=0;i<costs.length;i++)
            cst+=costs[i]+" ";
        return "fixed cost: "+ fixedcost+"\n"+
                "pheromone: "+pheromone+"\n"+
                "capacity: "+capacity+"\n"+
                "costs: "+cst + "\n";
    }
}
