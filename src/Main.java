import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void insert_data(String file)
    {
        try{
            File myObj = new File(file);
            Scanner reader = new Scanner(myObj);

            int warehouse_number = Integer.parseInt(reader.nextLine());
            int client_number = Integer.parseInt(reader.nextLine());

            warhouselist = new Warehouse[warehouse_number];
            clientlist = new Client[client_number];

            //Tworzenie magazynów oraz klientów
            for(int i=0;i<warehouse_number;i++)
                warhouselist[i]=new Warehouse();


            //Przypisywanie kosztów transportu do magazynów
            double[] help = new double [client_number];
            for(int i=0;i<warehouse_number;i++)
            {
                for (int j = 0; j < client_number; j++)
                    help[j] = reader.nextInt();
                warhouselist[i].costs=help.clone();
            }

            //Przypisywanie kosztów otwarcia
            // magazynów
            for(int i=0;i<warehouse_number;i++){
                warhouselist[i].fixedcost=reader.nextInt();
                warhouselist[i].pheromone=1.0/(warehouse_number*client_number);
            }


            //Przypisanie koszów utrzymania magazynu
            for(int i=0;i<warehouse_number;i++)
                warhouselist[i].capacity=reader.nextInt();

            //przypisanie potrzeb klientom
            for(int i=0;i<client_number;i++)
                clientlist[i]=new Client(i,reader.nextInt());

        }
     catch (FileNotFoundException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
     }
    }
    public static void update_pheromones(int[]X){
        int m=warhouselist.length;
        int n=clientlist.length;
        double p = 0.5;
        for (int i=0;i<m;i++) {
            int counter=0;
            for(int j=0;j<X.length;j++)
                if(X[j]==i)
                    counter++;
            warhouselist[i].pheromone=Math.max((1-p)*  warhouselist[i].pheromone+p/n*counter,1/(m*n));
        }

    }
    public static void copy_arr(int[]b,int[]s){
        for(int i=0;i<b.length;i++)
            s[i]=b[i];
    }

    static public int[] ACO_ALGORITHM()
    {
        int[]best_vector = new int[clientlist.length];
        int lowest_cost = Integer.MAX_VALUE;


        int[]X= new int[clientlist.length];
        for(int k=0;k<MAX_ITER;k++){
            permutate_clients();
            int help,h_idx=0;

            //resetowanie magazynów
            for (Warehouse wr:warhouselist){
                wr.remaining_capacity=wr.capacity;
                wr.open=0;
            }

            for (Client client:clientlist)
            {
                help=client.choose_warehouse(warhouselist);
                if(help==80085)
                    break;
                if(warhouselist[help].open==0)
                    warhouselist[help].open=1;
                warhouselist[help].remaining_capacity-=client.demand;
                X[h_idx]=help;
                h_idx++;
            }
            if(calculate_cost(X)<lowest_cost)
            {
//              System.out.println("///"+calculate_cost(X));
                copy_arr(X,best_vector);
                lowest_cost = calculate_cost(best_vector);
            }
            update_pheromones(X);
        }

        open_warehouse(best_vector);
        return best_vector;
    }
    static public void open_warehouse(int[] X)
    {
        for (Warehouse wr:warhouselist){
            wr.remaining_capacity=wr.capacity;
            wr.open=0;
        }
        for(int i=0;i<X.length;i++)
            warhouselist[X[i]].open=1;
    }
    static public int calculate_cost(int[]X)
    {
        int cost=0;
        for (Warehouse wr:warhouselist)
            cost+=wr.fixedcost*wr.open;
        for(int i=0;i<X.length;i++)
            cost+=warhouselist[X[i]].costs[i];
        return cost;
    }
    static public void permutate_clients()
    {
        int a,b,help,h_idx;
        Random rand = new Random();
        for(int i=0;i<1000;i++)
        {
           a= rand.nextInt(clientlist.length);
           help= clientlist[a].demand;
           h_idx=clientlist[a].idx;
           b=rand.nextInt(clientlist.length);
           clientlist[a].demand=clientlist[b].demand;
           clientlist[a].idx=clientlist[b].idx;

           clientlist[b].demand=help;
           clientlist[b].idx=h_idx;
        }
    }
    static Warehouse[]warhouselist;
    static  Client[]clientlist;
    final static int MAX_ITER = 10000;
    public static void main(String[] args) {
        insert_data("data2.txt");
//        for (Warehouse warehouse : warhouselist)
//            System.out.println(warehouse);


            int X[] = ACO_ALGORITHM();
//        for(Client client : clientlist)
//            System.out.println(client);
        System.out.println(calculate_cost(X));



        for(int i=0;i<X.length;i++)
            System.out.println(X[i]);


    }
}