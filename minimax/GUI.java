/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package minimax;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 *
 * @author INFOR
 */
public class GUI extends JFrame{
    GUI(){
        super("MiniMax");
        initComponents();
    }    
    private void initComponents(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,400);
        setContentPane(new Board());
        pack();
    }
    private class Board extends JDesktopPane{
        JButton[][] buttons;
        MiniMax.Node n;
        char[][] MATRIZ;
        public Board() {initButtons();}
        final void initButtons(){
            MATRIZ = new char[3][3];
            n = new MiniMax.Node('X');
            buttons = new JButton[3][3];
            setPreferredSize(new Dimension(150*3,46*3));
            setLayout(new GridLayout(3,3));
            for(int i=0;i<3;i++) for(int j=0;j<3;j++){
                buttons[i][j] = new JButton();
                buttons[i][j].setText(" ");
                buttons[i][j].setFont(new Font(Font.SANS_SERIF,Font.BOLD,40));
                buttons[i][j].addActionListener(this::ButtonClick);
                buttons[i][j].setPreferredSize(new Dimension(150,46));
                add(buttons[i][j]);
            }
            COPY();
            pack();
        }
        void COPY(){
            for(int i=0;i<3;i++) for(int j=0;j<3;j++) {
                if(buttons[i][j].getText().equals(" ")) MATRIZ[i][j]='_';
                else MATRIZ[i][j] = buttons[i][j].getText().charAt(0);
            }
        }
        void ButtonClick(ActionEvent e){
            JButton botaoclicado = (JButton) e.getSource();
            if(botaoclicado.getText().equals(" ")&&!MiniMax.FIM_DE_JOGO(MATRIZ)){
                botaoclicado.setText("O");
                botaoclicado.setForeground(Color.BLUE);
                COPY();
                if(!MiniMax.FIM_DE_JOGO(MATRIZ)){
                    n = new MiniMax.Node(n, MATRIZ);
                    n = MiniMax.MINIMAX(n);
                    buttons[n.i][n.j].setText("X");
                    buttons[n.i][n.j].setForeground(Color.red);
                    COPY();
                }else{
                    Dialogo_Fim();
                }
            }else{
                if(MiniMax.FIM_DE_JOGO(MATRIZ)){
                    Dialogo_Fim();
                }else{
                    JOptionPane.showMessageDialog(null,"JOGADA INVALIDA","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        void Dialogo_Fim(){
            String m;
                    if(n.Utilidade_final()==0) m = "EMPATE";
                    else if(n.Utilidade_final()>0) m = "VOCE PERDEU!";
                    else m="VOCE VENCEU";
                        JOptionPane.showMessageDialog(null,m,"O JOGO ACABOU",JOptionPane.INFORMATION_MESSAGE  );
                        int dec = JOptionPane.showConfirmDialog(null,"DESEJA COMECAR DO ZERO?","PERGUNTA",JOptionPane.YES_NO_OPTION);
                        switch(dec){
                           case(0):
                                for(int i=0;i<3;i++) for(int j=0;j<3;j++) {
                                    buttons[i][j].setText(" ");
                                    buttons[i][j].setForeground(buttons[i][j].getBackground());
                                }
                                COPY();
                                break;
                           case(1):
                                    System.exit(0);
                                    break;
                            default:
                                break;
                        }
                           n = new MiniMax.Node('X');
                           n.estado_matriz= MATRIZ;
                           int dec2 = JOptionPane.showConfirmDialog(null, "DESEJA JOGAR ANTES DA I.A.?","PERGUNTA",JOptionPane.YES_NO_OPTION);
                           if(dec2!=0){
                             n = MiniMax.MINIMAX(n);
                             buttons[n.i][n.j].setText("X");
                             buttons[n.i][n.j].setForeground(Color.red);
                             COPY();
                        }
        }
    }
    public static void main(String[] args) {
                try {
                        String OS = System.getProperty("os.name").toLowerCase();
                        if(OS.contains("win"))
                        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                        else
                        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                System.err.println(ex);
            }
        
        EventQueue.invokeLater(() -> {
            new GUI().setVisible(true);
        });
    }
}
