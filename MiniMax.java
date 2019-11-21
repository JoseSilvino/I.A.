/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minimax;
import java.util.*;
/**
 *
 * @author JOAO&
 */
public class MiniMax {
    private static class Node{
        Node pai;
        ArrayList<Node> filhos;
        int depth;
        char[][] estado_matriz;
        int x,y;
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
            if(is_final()) return Utilidade();
            long v = Integer.MAX_VALUE;
            for(var s :filhos) v = Long.min(v,s.utilidade_max());
            return v;
        }
        private long utilidade_max(){
            if(is_final()) return Utilidade();
            long v = Integer.MIN_VALUE;
            for(var s:filhos) v = Long.max(v, s.utilidade_min());
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
        long Utilidade(){
            if(is_final()){
            for(int i=0;i<3;i++){
                if(estado_matriz[i][0]=='O' && estado_matriz[i][1]=='O'&&estado_matriz[i][2] == 'O') return -1;
                if(estado_matriz[0][i]=='O'&&estado_matriz[1][i]=='O'&&estado_matriz[2][i] == 'O') return -1;
            }
            if(estado_matriz[0][0]=='O' && estado_matriz[1][1]=='O'&& estado_matriz[2][2]=='O' )return -1;
            if(estado_matriz[0][2]=='O' &&estado_matriz[1][1]=='O'&&estado_matriz[2][0]=='O')return -1;
            for(int i=0;i<3;i++){
                if(estado_matriz[i][0]=='X' && estado_matriz[i][1]=='X'&&estado_matriz[i][2] == 'X') return 1;
                if(estado_matriz[0][i]=='X'&&estado_matriz[1][i]=='X'&&estado_matriz[2][i] == 'X') return 1;
            }
            if(estado_matriz[0][0]=='X' && estado_matriz[1][1]=='X'&& estado_matriz[2][2]=='X' )return 1;
            if(estado_matriz[0][2]=='X' &&estado_matriz[1][1]=='X'&&estado_matriz[2][0]=='X')return 1;
            return 0;
            }
            //fazer algo parecido com o minimax
            return utilidade_max();
        }
        long VALUE_MIN(){
            if(this.is_final()) return this.Utilidade();
            long v = Integer.MAX_VALUE;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(estado_matriz[i][j]=='_'){
                        Node s = new Node(this,Criar_Matriz(i,j,false));
                        s.x =i;
                        s.y =j;
                        filhos.add(s);
                        v = Long.min(v, s.VALUE_MAX());
                    }
                }
            }
            return v;
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
        long VALUE_MAX(){ 
            if(this.is_final()) return this.Utilidade();
            long v = Integer.MIN_VALUE;
            for(int i=0;i<3;i++){
                for(int j=0;j<3;j++){
                    if(estado_matriz[i][j]=='_'){
                        Node s = new Node(this,Criar_Matriz(i,j,true));
                        s.x =i;
                        s.y =j;
                        filhos.add(s);
                        v = Long.max(v, s.VALUE_MIN());
                    }
                }
            }
            return v;
        }
        int MelhorPossib(){
            return 1;
        }
        static Node MINIMAX(Node estado){
            long v = estado.VALUE_MAX();
            //ArrayList<Node> Solucoes = new ArrayList<>();
            for(Node next:estado.filhos){
                if(next.Utilidade()==v) return next;
            }
            return null;
            /*
            long l = Integer.MIN_VALUE;
            Node melhor_filho=null;
            for(Node n:Solucoes){
                long mel = n.MelhorPossib();
                if(l<mel){
                    melhor_filho = n;
                    l = n.MelhorPossib();
                }
            }
            return melhor_filho;*/
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
    static boolean FIM_DE_JOGO(char[][] c){
        var tmp = new Node(null, c);
        return tmp.is_final();
    }
    static void COPIAR(char[][]Matriz,Node n){
        for(int i=0;i<3;i++) System.arraycopy(n.estado_matriz[i], 0, Matriz[i], 0, 3);
    }
    public static void main(String[] args) {
        Node n = new Node();
        System.out.println(n);
        System.out.println();
        char[][] MATRIZ = new char[3][3];
        COPIAR(MATRIZ, n);
        int x,y;
        Scanner input=new Scanner(System.in);
        while(!FIM_DE_JOGO(MATRIZ)){
            COPIAR(MATRIZ,n);
            n = Node.MINIMAX(n);
            COPIAR(MATRIZ,n);
            System.out.println(n);
            if(!FIM_DE_JOGO(MATRIZ)){
            System.out.println("DIGA AS COORDENADAS DE O");
            while(true){
                x = input.nextInt();
                y = input.nextInt();
                if(x>=0&&x<3&&y>=0&&y<3&&MATRIZ[x][y]=='_') break;
                else System.out.println("LADRÂO");
            }
            MATRIZ[x][y]='O';
            n = new Node(n,MATRIZ);
            System.out.println();
            System.out.println(n);
            System.out.println('\n');
            }
        }
        if(n.Utilidade()==0) System.out.println("EMPATE");
        else if(n.Utilidade()==-1) System.out.println("PARABENS , VOCE VENCEU");
        else if(n.Utilidade()==1) System.out.println("HAHA VOCE PERDEU PRA UM COMPUTADOR BURRO");
    }
    
}
