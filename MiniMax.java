/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minimax;
import java.util.*;

import javax.swing.JOptionPane;

public final class MiniMax {
    private  class Node{
        Node pai;
        ArrayList<Node> filhos;
        int depth;
        char[][] estado_matriz;
        int l,c;
        long util;
        Node(){
            pai = null;
            filhos = new ArrayList<>();
            depth =0;
            estado_matriz = new char[][]{{'_','_','_'},{'_','_','_'},{'_','_','_'}};
        }
        Node(Node pai,char[][] m){
            this();
            this.pai = pai;
            if(pai!=null)
            this.depth=pai.depth+1;
            else this.depth=0;
            this.estado_matriz = m;
        }
        private long utilidade_min(){
            if(is_final()) return Utilidade_final();
            long v = Integer.MAX_VALUE;
            for(Node s :filhos) v = Long.min(v,s.utilidade_max());
            return v;
        }
        private long utilidade_max(){
            if(is_final()) return Utilidade_final();
            long v = Integer.MIN_VALUE;
            for(Node s:filhos) v = Long.max(v, s.utilidade_min());
            return v;
        }
        boolean is_final(){
            int count=0;
            for(int i=0;i<3;i++)
                for(int j=0;j<3;j++) if(estado_matriz[i][j]!='_') count++;
            if(count==9) return true;
            for(int i=0;i<3;i++){
                if(estado_matriz[i][0]!='_'&&estado_matriz[i][0]==estado_matriz[i][1]&&estado_matriz[i][0]==estado_matriz[i][2]&&estado_matriz[i][1]==estado_matriz[i][2]) return true;
                if(estado_matriz[0][i]!='_'&&estado_matriz[0][i]==estado_matriz[1][i]&&estado_matriz[0][i]==estado_matriz[2][i]&&estado_matriz[1][i]==estado_matriz[2][i]) return true;
            }
            if(estado_matriz[0][0]!='_'&&estado_matriz[0][0]==estado_matriz[1][1]&&estado_matriz[0][0]==estado_matriz[2][2]&&estado_matriz[1][1]==estado_matriz[2][2] )return true;
            return estado_matriz[0][2]!='_'&&estado_matriz[0][2]==estado_matriz[1][1]&&estado_matriz[0][2]==estado_matriz[2][0]&&estado_matriz[1][1]==estado_matriz[2][0];
        }
        long Utilidade_final(){
            for(int i=0;i<3;i++){
                if(estado_matriz[i][0]==estado_matriz[i][1]&&estado_matriz[i][2] == estado_matriz[i][1]){
                    if(estado_matriz[i][0]=='O')
                    return -20+depth;
                    else if(estado_matriz[i][0]=='X' )return 20-depth;
                }
                if(estado_matriz[0][i]==estado_matriz[1][i]&&estado_matriz[2][i] == estado_matriz[0][i]){
                    if(estado_matriz[0][i]=='O')
                    return -20+depth;
                    else if(estado_matriz[0][i]=='X' )return 20-depth;
                }
            }
            if(estado_matriz[0][0]==estado_matriz[1][1]&& estado_matriz[2][2]==estado_matriz[0][0] ){
                if(estado_matriz[0][0]=='O')
                    return -20+depth;
                else if(estado_matriz[0][0]=='X' )return 20-depth;
            }
            if(estado_matriz[0][2]==estado_matriz[1][1] &&estado_matriz[2][0]==estado_matriz[1][1]){
                if(estado_matriz[1][1]=='O')
                return -20+depth;
                else if(estado_matriz[1][1]=='X') return 20-depth;
            }
            return 0;
        }
        long Utilidade(){
            if(is_final()){
                return Utilidade_final();
            }
            //fazer algo parecido com o minimax
            return utilidade_max();
        }
        char [][]Criar_Matriz(int l,int c,boolean is_X){
            char [][]m = new char[3][3];
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(l==i&c==j){
                        if(is_X) m[i][j] ='X';
                        else m[i][j]='O';
                    }else m[i][j] = estado_matriz[i][j];
                }
            }
            return m;
        }
        long PiorPossib(){
            if(is_final() ) return Utilidade_final();
            long v = Integer.MAX_VALUE;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(estado_matriz[i][j]=='_'){
                        Node s = new Node(this,Criar_Matriz(i,j,false));
                        filhos.add(s);
                        v = Long.min(v,s.MelhorPossib());    
                    }
                }
            }
            util=v;
            return v;
        }
        long MelhorPossib(){
            if(is_final() ) return Utilidade_final();
            long v=Integer.MIN_VALUE;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(estado_matriz[i][j]=='_'){
                        Node s = new Node(this,Criar_Matriz(i,j,true));
                        filhos.add(s);
                        v = Long.max(v,s.PiorPossib());
                    }
                }
            }
            util  = v;
            return v;
        }


        @Override
        public String toString() {
            String s="";
            for(int i=0;i<3;i++){
                s = s + Arrays.toString(estado_matriz[i]);
                if(i<2)
                    s = s + "\n";
            }
            return s;
        }
     
       
    }
    /**
     * @param args the command line arguments
     */
     boolean FIM_DE_JOGO(char[][] estado_matriz){
        int count=0;
        for(int i=0;i<3;i++)
            for(int j=0;j<3;j++) if(estado_matriz[i][j]!='_') count++;
        if(count==9) return true;
        for(int i=0;i<3;i++){
            if(estado_matriz[i][0]!='_'&&estado_matriz[i][0]==estado_matriz[i][1]&&estado_matriz[i][0]==estado_matriz[i][2]&&estado_matriz[i][1]==estado_matriz[i][2]) return true;
            if(estado_matriz[0][i]!='_'&&estado_matriz[0][i]==estado_matriz[1][i]&&estado_matriz[0][i]==estado_matriz[2][i]&&estado_matriz[1][i]==estado_matriz[2][i]) return true;
        }
        if(estado_matriz[0][0]!='_'&&estado_matriz[0][0]==estado_matriz[1][1]&&estado_matriz[0][0]==estado_matriz[2][2]&&estado_matriz[1][1]==estado_matriz[2][2] )return true;
        return estado_matriz[0][2]!='_'&&estado_matriz[0][2]==estado_matriz[1][1]&&estado_matriz[0][2]==estado_matriz[2][0]&&estado_matriz[1][1]==estado_matriz[2][0];
    
    }
     void COPIAR(char[][]Matriz,Node n){
        for(int i=0;i<3;i++) System.arraycopy(n.estado_matriz[i], 0, Matriz[i], 0, 3);
    }
     Node MINIMAX(Node estado){
         long escolha = estado.MelhorPossib();
         System.out.println(escolha);
         for(Node filho:estado.filhos){
             if(filho.util==escolha) return filho;
             if(filho.is_final()&&filho.Utilidade_final()>0)return filho;
         }
         return null;
    }
    public static void main(String[] args) {
        System.out.println("Diga 0 se vc deseja jogar antes , e outro numero caso contário");
        try (java.util.Scanner scan = new Scanner(System.in)) {
            boolean value = scan.nextInt()==0;
            new MiniMax(value);
            //boolean value = Integer.parseInt(JOptionPane.showInputDialog("Diga 0 se vc deseja jogar antes , e outro numero caso contário"))==0;
        }
        
    }
    Scanner input;
    MiniMax(boolean human){
        input = new Scanner(System.in);
        if(human)
        Do_Game_Human_First();
        else Do_Game_Machine_First();
    }
    void Do_Game_Machine_First(){
        Node n = new Node();
        System.out.println(n);
        System.out.println();
        char[][] MATRIZ = new char[3][3];
        COPIAR(MATRIZ, n);
        int l,c;
        while(!FIM_DE_JOGO(MATRIZ)){
            COPIAR(MATRIZ,n);
            n = MINIMAX(n);
            COPIAR(MATRIZ,n);
            System.out.println(n);            
            if(!FIM_DE_JOGO(MATRIZ)){        
                System.out.println("DIGA AS COORDENADAS DE O");
                while(true){
                    l = input.nextInt();
                    c = input.nextInt();
//                    l = Integer.parseInt(JOptionPane.showInputDialog("DIGA A LINHA"));
//                    c = Integer.parseInt(JOptionPane.showInputDialog("DIGA A COLUNA"));
                    if(l>=0&&l<3&&c>=0&&c<3&&MATRIZ[l][c]=='_') break;
                    else System.out.println("LADRÂO");
                }
                MATRIZ[l][c]='O';
                n = new Node(n,MATRIZ);
                System.out.println();
                System.out.println(n);
                System.out.println('\n');
            }
        }
        if(n.Utilidade()==0) System.out.println("EMPATE");
        else if(n.Utilidade()<0) System.out.println("PARABENS , VOCE VENCEU");
        else if(n.Utilidade()>0) System.out.println("HAHA VOCE PERDEU PRA UM COMPUTADOR BURRO");
        input.close();
    }
    void Do_Game_Human_First(){
        Node n = new Node();
        System.out.println(n);
        System.out.println();
        char[][] MATRIZ = new char[3][3];
        COPIAR(MATRIZ, n);
        int l,c;
        while(!FIM_DE_JOGO(MATRIZ)){
            System.out.println("DIGA AS COORDENADAS DE O");
            while(true){
                l = input.nextInt();
                c = input.nextInt();
//                l = Integer.parseInt(JOptionPane.showInputDialog("DIGA A LINHA"));
//                c = Integer.parseInt(JOptionPane.showInputDialog("DIGA A COLUNA"));
                if(l>=0&&l<3&&1>=0&&c<3&&MATRIZ[l][c]=='_') break;
                else System.out.println("LADRÂO");
            }
            MATRIZ[l][c]='O';
            n = new Node(n,MATRIZ);
            System.out.println();
            System.out.println(n);
            System.out.println('\n');

            
            if(!FIM_DE_JOGO(MATRIZ)){
                COPIAR(MATRIZ,n);
                n = MINIMAX(n);
                COPIAR(MATRIZ,n);
                System.out.println(n);
            }
        }
        if(n.Utilidade()==0) System.out.println("EMPATE");
        else if(n.Utilidade()<0) System.out.println("PARABENS , VOCE VENCEU");
        else if(n.Utilidade()>0) System.out.println("HAHA VOCE PERDEU PRA UM COMPUTADOR BURRO");
        input.close();
    }
    
}
