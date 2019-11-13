import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Queue;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Metro{
    private class Node{
        Node pai;
        ArrayList <Node> filhos;
        int estacao;
        int distanciapercorrida;
        double tempoGastoMin;
        private Node(int estacao){
            pai = null;
            this.estacao = estacao;
            filhos = new ArrayList<>();
            this.distanciapercorrida=0;
        }
        private Node(Node pai,int estacao,int distp){
            this(estacao);
            this.pai = pai;
            this.distanciapercorrida = distp;
        }
        private void gerarFilhos(int[][] dists){
            for(int i=0;i<14;i++){
                if(i!=estacao){
                    filhos.add(new Node(this,i,this.distanciapercorrida + dists[i][estacao]));
                }
            }
        }
        private void printway(){
            if(pai!=null) pai.printway();
            System.out.print((this.estacao+1));
            if(filhos.size()>0) System.out.print(" , ");
        }
        @Override
        public String toString() {
            return Integer.toString(estacao+1);
        }
    }
    int [][]distances;
    int or,de;
    private void BFS(Node start,int de){
        ArrayList<Node> queue = new ArrayList<>();
        queue.add(start);
        while(!queue.isEmpty()){
            Node dequeued = queue.get(0);
            queue.remove(0);
            //System.out.println("Distancia percorrida entre " + start + " e " +dequeued +" = " + dequeued.distanciapercorrida);
            if(dequeued.estacao==de){
                System.out.println(dequeued);
                System.out.print('{');
                dequeued.printway();
                System.out.println('}');
                System.out.println(dequeued.distanciapercorrida);
                break;
            }
            dequeued.gerarFilhos(distances);
            queue.addAll(dequeued.filhos);
            Collections.sort(queue,new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    if(o1.distanciapercorrida < o2.distanciapercorrida) return -1;
                    else if(o1.distanciapercorrida > o2.distanciapercorrida) return 1;
                    else return 0;
                }
            });
        }

    }
    public int dist(int p1,int p2){
        return distances[p1][p2];
    }
    Metro(int [][]distances,int or,int de){
        this.distances = distances;
        this.de = de;
        this.or = or;
        Main();
    }
    private void Main(){
        Node n = new Node(or-1);
        BFS(n,de-1);
    }
    public static void main(String [] args){
        try{
        System.out.println("Diga as estações de origem e destino");
        Scanner input = new Scanner(System.in);
        int or = Integer.parseInt(JOptionPane.showInputDialog(null,"Diga a estação de origem"));
        int de = Integer.parseInt(JOptionPane.showInputDialog(null,"Diga a estação de destino"));
            Metro t = new Metro(new int[][]{
                {0,11,20,27,40,43,39,28,18,10,18,30,30,32},
                {11,0,9,16,29,32,28,18,11,4,17,23,21,24},
                {20,9,0,7,20,22,19,15,10,11,21,21,13,18},
                {27,16,7,0,13,16,12,13,13,18,26,21,11,17},
                {40,29,20,13,0,3,2,21,25,31,38,27,16,20},
                {43,32,22,16,3,0,4,23,27,33,41,30,17,20},
                {39,28,19,12,2,4,0,22,25,29,38,28,13,17},
                {28,19,15,13,21,23,22,0,9,22,18,7,25,30},
                {18,11,10,13,25,28,25,9,0,13,12,12,23,28},
                {10,4,11,18,31,33,29,22,13,0,20,27,20,23},
                {18,17,21,26,38,41,38,18,12,20,0,15,35,39},
                {30,23,21,21,27,30,28,7,12,27,15,0,31,37},
                {30,21,13,11,16,17,13,25,23,20,35,31,0,5},
                {32,24,18,17,20,20,17,30,28,23,39,37,5,0}},or,de);
            }catch(Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
     }
}
