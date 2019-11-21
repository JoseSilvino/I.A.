import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Metro{
    private static class Node{
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
            this.tempoGastoMin =0;
        }
        private Node(Node pai,int estacao,int distp){
            this(estacao);
            this.pai = pai;
            this.distanciapercorrida = distp + pai.distanciapercorrida;
            double tempoGastoHr = distp/30d;
            this.tempoGastoMin = pai.tempoGastoMin  + 60*tempoGastoHr;
        }
        private void gerarFilhos(int[][] dists){
            for(int i=0;i<14;i++){
                if(dists[i][estacao]>0){
                    Node node = new Node(this,i, dists[i][estacao]);
                    if(pai!=null){
                        if(dists[pai.estacao][i]==-1)
                            node.tempoGastoMin+=4;
                    }
                    filhos.add(node);
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
            return Double.toString(tempoGastoMin) + " minutos";
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
                dequeued.tempoGastoMin-=4;
                System.out.println(dequeued);
                System.out.print('{');
                dequeued.printway();
                System.out.println('}');
                System.out.println(dequeued.distanciapercorrida + " km (em linha reta)");
                break;
            }
            dequeued.gerarFilhos(distances);
            queue.addAll(dequeued.filhos);
            Collections.sort(queue,new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    if(o1.tempoGastoMin < o2.tempoGastoMin) return -1;
                    else if(o1.tempoGastoMin > o2.tempoGastoMin) return 1;
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
        System.out.println("Diga as estacoes de origem e destino");
        //Scanner input = new Scanner(System.in);
        int or = Integer.parseInt(JOptionPane.showInputDialog(null,"Diga a estacao de origem"));
        int de = Integer.parseInt(JOptionPane.showInputDialog(null,"Diga a estacao de destino"));
            Metro t = new Metro(new int[][]{
                { 0, 11, -2, -2, -2, -2, -1, -1, -1, -1, -1, -1, -1, -1},
                {11,  0,  9, -2, -2, -2, -2, -2, 11,  4, -1, -1, -1, -1},
                {-2,  9,  0,  7, -2, -2, -1, -1, 10, -1, -2, -1, 13, -1},
                {-2, -2,  7,  0, 13, -2, -1, 13, -1, -1, -1, -2, 11, -2},
                {-2, -2, -2, 13,  0,  3,  2, 21, -2, -2, -1, -1, -1, -1},
                {-2, -2, -2, -2,  3,  0, -1, -1, -1, -1, -1, -1, -1, -1},
                {-1, -2, -1, -1,  2, -1,  0, -2, -2, -2, -1, -1, -1, -1,},
                {-1, -2, -1, -1, 21, -1, -2,  0,  9, -2, -1, -2, -2, -2},
                {-1, 11, 10, -2, -2, -2, -2,  9,  0, -2, 12, -1, -1, -2},
                {-1,  4, -1, -1, -1, -1, -2, -2, -2,  0, -2, -2, -2, -2},
                {-1, -1, -2, -1, -1, -1, -1, -1, 12, -1,  0, -1, -1, -2},
                {-1, -1, -1, -2, -1, -1, -1,  7, -1, -1, -1,  0, -2, -2},
                {-1, -1, 13, 11, -1, -1, -1, -2, -2, -1, -2, -2,  0,  5},
                {-1, -1, -1, -2, -1, -1, -1, -2, -1, -1, -1, -2,  5,  0}},or,de);
            }catch(HeadlessException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
     }
}
