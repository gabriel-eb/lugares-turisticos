package clientes.display;

import java.awt.Container;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author gaboeb
 */
public class Mapa extends JFrame {

    private ImageIcon li;

    public Mapa(String ciudad, ImageIcon li) {
        this.li = li;
        mostrar(ciudad);
    }

    private void mostrar(String ciudad) {
        //ImageIcon li = cargarImagen(ciudad);
        JLabel etiqueta = new JLabel(li);

        crearLayout(etiqueta);

        setTitle("Ciudad: " + ciudad.toUpperCase());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private ImageIcon cargarImagen(String ciudad) {
        ImageIcon ii = new ImageIcon("imagenes/" + ciudad + ".jpg");
        return ii;
    }

    private void crearLayout(JComponent... arg) {
        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);

        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addComponent(arg[0])
        );

        gl.setVerticalGroup(gl.createParallelGroup()
                .addComponent(arg[0])
        );

        pack();
    }

}
