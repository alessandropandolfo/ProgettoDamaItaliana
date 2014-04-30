
package PandolfoAlessandro.Dama.View;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;



/**
 * essentially is to choose the color
 * @author Alessandro
 */
public class Starter extends JFrame{
    private int colore;
    private JRadioButton nero;
    private JRadioButton bianco;
    private JButton play;
    private JLabel scelta;
    private JLabel ricorda;
    
    /**
     *
     */
    public Starter(){
        super("Gioco dama italiana - IMPOSTAZIONI");
        colore = 0;
        nero = new JRadioButton();
        bianco = new JRadioButton();
        nero.setBackground(Color.black);
        nero.setForeground(Color.white);
        bianco.setBackground(Color.white);
        nero.setText("NERO");
        bianco.setText("BIANCO");
        play = new JButton("Gioca");
        scelta = new JLabel("Scegliere con quale colore giocare:");
        ricorda = new JLabel("Si ricorda che iniziano sempre i bianchi.");
        initComponent();
        play.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                if(bianco.isSelected()){
                    colore = 1;
                }
                if(nero.isSelected()){
                    colore = 2;
                }
                if(!bianco.isSelected() && !nero.isSelected()){
                    JOptionPane.showMessageDialog(null, "Selezionare un colore");
                }else{
                    setVisible(false);
                    Gioco g = new Gioco(colore);
                    g.setVisible(true);
                }
            }
        });
        repaint();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void initComponent(){
        this.setSize(450, 300);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ricorda)
                    .addComponent(scelta))
                .addContainerGap(194, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(play)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(nero)
                        .addComponent(bianco)))
                .addGap(89, 89, 89))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scelta)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bianco)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nero)
                .addGap(26, 26, 26)
                .addComponent(ricorda)
                .addGap(57, 57, 57)
                .addComponent(play)
                .addContainerGap(107, Short.MAX_VALUE))
        );
    }
    
    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        Starter start = new Starter();
        start.setVisible(true);
    }
}
