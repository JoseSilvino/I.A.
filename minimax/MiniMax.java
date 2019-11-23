/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minimax;
import java.util.*;


public final class MiniMax {
    public static  class Node{
        Node pai;
        ArrayList<Node> filhos;
        int depth;
        char[][] estado_matriz;
        int l,c,i,j;
        long util;
        char meu;
        char oponente;
        Node(char meu){
            pai = null;
            filhos = new ArrayList<>();
            depth =0;
            estado_matriz = new char[][]{{'_','_','_'},{'_','_','_'},{'_','_','_'}};
            this.meu = meu;
            this.oponente = (meu=='X') ? 'O':'X';
        }
        Node(Node pai,char[][] m){
            this(pai.meu);
            this.pai = pai;
            this.estado_matriz = m;
            getDepth();
        }
        private void getDepth(){
            if(pai!=null)
            this.depth=pai.depth+1;
            else this.depth=0;
        }
        boolean is_final(){
            int count=0;
            for(int it=0;it<3;it++)
                for(int k=0;k<3;k++) if(estado_matriz[it][k]!='_') count++;
            if(count==9) return true;
            for(int it=0;it<3;it++){
                if(estado_matriz[it][0]!='_'&&estado_matriz[it][0]==estado_matriz[it][1]&&estado_matriz[it][0]==estado_matriz[it][2]&&estado_matriz[it][1]==estado_matriz[it][2]) return true;
                if(estado_matriz[0][it]!='_'&&estado_matriz[0][it]==estado_matriz[1][it]&&estado_matriz[0][it]==estado_matriz[2][it]&&estado_matriz[1][it]==estado_matriz[2][it]) return true;
            }
            if(estado_matriz[0][0]!='_'&&estado_matriz[0][0]==estado_matriz[1][1]&&estado_matriz[0][0]==estado_matriz[2][2]&&estado_matriz[1][1]==estado_matriz[2][2] )return true;
            return estado_matriz[0][2]!='_'&&estado_matriz[0][2]==estado_matriz[1][1]&&estado_matriz[0][2]==estado_matriz[2][0]&&estado_matriz[1][1]==estado_matriz[2][0];
        }
        long Utilidade_final(){
            for(int it=0;it<3;it++){
                if(estado_matriz[it][0]==estado_matriz[it][1]&&estado_matriz[it][2] == estado_matriz[it][1]){
                    if(estado_matriz[it][0]==oponente)
                    return -20+depth;
                    else if(estado_matriz[it][0]==meu )return 20-depth;
                }
                if(estado_matriz[0][it]==estado_matriz[1][it]&&estado_matriz[2][it] == estado_matriz[0][it]){
                    if(estado_matriz[0][it]==oponente)
                    return -20+depth;
                    else if(estado_matriz[0][it]==meu )return 20-depth;
                }
            }
            if(estado_matriz[0][0]==estado_matriz[1][1]&& estado_matriz[2][2]==estado_matriz[0][0] ){
                if(estado_matriz[0][0]==oponente)
                    return -20+depth;
                else if(estado_matriz[0][0]==meu )return 20-depth;
            }
            if(estado_matriz[0][2]==estado_matriz[1][1] &&estado_matriz[2][0]==estado_matriz[1][1]){
                if(estado_matriz[1][1]==oponente)
                return -20+depth;
                else if(estado_matriz[1][1]==meu) return 20-depth;
            }
            return 0;
        }
        char [][]Criar_Matriz(int l,int c,boolean my){
            char [][]m = new char[3][3];
            for(int it=0;it<3;it++){
                for(int k=0;k<3;k++){
                    if(l==it&c==k){
                        if(my) m[it][k] =meu;
                        else m[it][k]=oponente;
                    }else m[it][k] = estado_matriz[it][k];
                }
            }
            return m;
        }
        long PiorPossib(){
            if(is_final() ) return Utilidade_final();
            long v = Integer.MAX_VALUE;
            for(int it=0;it<3;it++){
                for(int k=0;k<3;k++){
                    if(estado_matriz[it][k]=='_'){
                        Node s = new Node(this,Criar_Matriz(it,k,false));
                        s.i =it;
                        s.j = k;
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
            for(int it=0;it<3;it++){
                for(int k=0;k<3;k++){
                    if(estado_matriz[it][k]=='_'){
                        Node s = new Node(this,Criar_Matriz(it,k,true));
                        s.i  = it;
                        s.j = k;
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
            for(int it=0;it<3;it++){
                s = s + Arrays.toString(estado_matriz[it]);
                if(it<2)
                    s = s + "\n";
            }
            return s;
        }
     
       
    }
    /**
     * @param args the command line arguments
     */
     static boolean FIM_DE_JOGO(char[][] estado_matriz){
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
     static Node MINIMAX(Node estado){
         long escolha = estado.MelhorPossib();
         for(Node filho:estado.filhos){
             if(filho.util==escolha) return filho;
             if(filho.is_final()&&filho.Utilidade_final()>0)return filho;
         }
         return null;
    }
        //Antes da Interface Grafica
//      public static void main(String[] args){
//        try (java.util.Scanner scan = new Scanner(System.in)) {
//            System.out.println("Diga 0 se deseja ver as ias jogando, e outro numero se deseja jogar");
//            boolean value = scan.nextInt()!=0,value_f;
//            if(value){
//            System.out.println("Diga 0 se vc deseja jogar antes , e outro numero caso contário");
//                value_f = scan.nextInt()==0;
//            }else value_f = false;
//            MiniMax miniMax = new MiniMax(value_f,value);
//            //boolean value = Integer.parseInt(JOptionPane.showInputDialog("Diga 0 se vc deseja jogar antes , e outro numero caso contário"))==0;
//        }
 //    }   
    //Antes da Interface Grafica
//    Scanner input;
//    MiniMax(boolean human_f,boolean human){
//        input = new Scanner(System.in);
//        if(!human) Machine_VS_Machine();
//        else if(human_f)
//        Do_Game_Human_First();
//        else Do_Game_Machine_First();
//    }
//    void Machine_VS_Machine(){
//            Node n1 = new Node('X'),n2 = new Node('O');
//            System.out.println(n1.toString()+'\n');
//            char[][] MATRIZ = new char[3][3];
//            COPIAR(MATRIZ, n1);
//            while(!FIM_DE_JOGO(MATRIZ)){
//                COPIAR(MATRIZ, n1);
//                n1 = MINIMAX(n1);
//                COPIAR(MATRIZ, n1);
//                System.out.println(n1.toString()+'\n');
//                if(!FIM_DE_JOGO(MATRIZ)){
//                    n2 = new Node(n2, MATRIZ);
//                    n2 = MINIMAX(n2);
//                    COPIAR(MATRIZ, n2);
//                    System.out.println(n2.toString()+'\n');
//                    n1 = new Node(n1, MATRIZ);
//                }
//            }
//            if(n1.Utilidade_final()>0) System.out.println("MAQUINA 1 VENCEU");
//            else if(n1.Utilidade_final()==0) System.out.println("EMPATE");
//            else System.out.println("MAQUINA 2 VENCEU");
//    }
//    void Do_Game_Machine_First(){
//        Node n = new Node('X');
//        System.out.println(n);
//        System.out.println();
//        char[][] MATRIZ = new char[3][3];
//        COPIAR(MATRIZ, n);
//        int l,c;
//        while(!FIM_DE_JOGO(MATRIZ)){
//            COPIAR(MATRIZ,n);
//            n = MINIMAX(n);
//            COPIAR(MATRIZ,n);
//            System.out.println(n);            
//            if(!FIM_DE_JOGO(MATRIZ)){        
//                System.out.println("DIGA AS COORDENADAS DE O");
//                while(true){
//                    l = input.nextInt();
//                    c = input.nextInt();
////                    l = Integer.parseInt(JOptionPane.showInputDialog("DIGA A LINHA"));
////                    c = Integer.parseInt(JOptionPane.showInputDialog("DIGA A COLUNA"));
//                    if(l>=0&&l<3&&c>=0&&c<3&&MATRIZ[l][c]=='_') break;
//                    else System.out.println("POSICAO INVALIDA");
//                }
//                MATRIZ[l][c]='O';
//                n = new Node(n,MATRIZ);
//                System.out.println();
//                System.out.println(n);
//                System.out.println('\n');
//            }
//        }
//        if(n.Utilidade()==0) System.out.println("EMPATE");
//        else if(n.Utilidade()<0) System.out.println("PARABENS ! VOCE VENCEU");
//        else if(n.Utilidade()>0) System.out.println("VOCE PERDEU!");
//        input.close();
//    }
//    void Do_Game_Human_First(){
//        Node n = new Node('X');
//        System.out.println(n);
//        System.out.println();
//        char[][] MATRIZ = new char[3][3];
//        COPIAR(MATRIZ, n);
//        int l,c;
//        while(!FIM_DE_JOGO(MATRIZ)){
//            System.out.println("DIGA AS COORDENADAS DE O");
//            while(true){
//                l = input.nextInt();
//                c = input.nextInt();
////                l = Integer.parseInt(JOptionPane.showInputDialog("DIGA A LINHA"));
////                c = Integer.parseInt(JOptionPane.showInputDialog("DIGA A COLUNA"));
//                if(l>=0&&l<3&&1>=0&&c<3&&MATRIZ[l][c]=='_') break;
//                else System.out.println("POSICAO INVALIDA");
//            }
//            MATRIZ[l][c]='O';
//            n = new Node(n,MATRIZ);
//            System.out.println();
//            System.out.println(n);
//            System.out.println('\n');
//
//            
//            if(!FIM_DE_JOGO(MATRIZ)){
//                COPIAR(MATRIZ,n);
//                n = MINIMAX(n);
//                COPIAR(MATRIZ,n);
//                System.out.println(n);
//            }
//        }
//        if(n.Utilidade()==0) System.out.println("EMPATE");
//        else if(n.Utilidade()<0) System.out.println("PARABENS ! VOCE VENCEU");
//        else if(n.Utilidade()>0) System.out.println("VOCE PERDEU!");
//        input.close();
//    }
//    
}