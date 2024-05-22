import java.util.Random;

public class Client {
    int idx;
    int demand;

    public Client(int i, int d)
    {
      this.idx=i;
      this.demand=d;
    }

    @Override
    public String toString() {
        return
                "idx: "+idx+"\n"+
                "demand: " + demand;
    }
    public int choose_warehouse(Warehouse[]warhouselist)
    {
        Random rand = new Random();
        double alpha=1.0;
        double beta=5.0;

        double[] ranges = new double[warhouselist.length+1];

        double propability;
        double denominator=0.0;
        double eta;

        //obliczanie mianownika
        for (Warehouse wr:warhouselist)
        {
            if(wr.remaining_capacity>=this.demand)
                eta=1.0/(alpha*wr.costs[this.idx]+beta*(1-wr.open)* wr.fixedcost) ;
            else
                eta=0;
            denominator+=wr.pheromone*eta;
        }
        //System.out.println(denominator);
        int help_idx=1;
        //tworzenie zakresów prawdopodobieństw
        for(Warehouse wr: warhouselist)
        {
            if(wr.remaining_capacity>=this.demand)
                eta=1/(alpha*wr.costs[this.idx]+beta*(1-wr.open)* wr.fixedcost) ;
            else
                eta=0;
            propability=wr.pheromone*eta/denominator;
            ranges[help_idx]=propability+ranges[help_idx-1];
            help_idx++;
        }
        double draw=rand.nextDouble(0,1);
//        System.out.println("draw: "+ draw);
//        System.out.print("ranges: ");
        for(int i=1;i<ranges.length;i++)
        {
//            System.out.print(ranges[i-1]+" ");
            if(draw>=ranges[i-1]&&draw<ranges[i])
                return i-1;
        }
        return 80085;
    }

}
