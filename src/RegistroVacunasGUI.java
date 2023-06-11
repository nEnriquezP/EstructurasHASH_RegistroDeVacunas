import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
public class RegistroVacunasGUI extends JFrame{
    private JTextField cuiField;
    private JTextArea registroArea;
    private HashMap<String, String> registroVacunas;

    public RegistroVacunasGUI () {
        super("Registro de Vacunas");
        registroVacunas = new HashMap<>();

        // Configuración de la interfaz gráfica
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de búsqueda
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new FlowLayout());
        JLabel cuiLabel = new JLabel("CUI:");
        cuiField = new JTextField(10);
        JButton buscarButton = new JButton("Buscar");
        panelBusqueda.add(cuiLabel);
        panelBusqueda.add(cuiField);
        panelBusqueda.add(buscarButton);
        add(panelBusqueda, BorderLayout.NORTH);

        // Área de texto para mostrar el registro de vacunas
        registroArea = new JTextArea();
        registroArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(registroArea);
        add(scrollPane, BorderLayout.CENTER);

        // Evento del botón de búsqueda
        buscarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cui = cuiField.getText();
                mostrarRegistroVacunas(cui);
            }
        });
    }

    private void mostrarRegistroVacunas(String cui) {
        if (registroVacunas.containsKey(cui)) {
            registroArea.setText(registroVacunas.get(cui));
        } else {
            registroArea.setText("No existe la persona que se busca.");
        }
    }

    public void cargarRegistroVacunas() {
        try {
            File archivo = new File("registro_vacunas.txt");
            if (archivo.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(archivo));
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(":");
                    String cui = partes[0];
                    String registro = partes[1];
                    registroVacunas.put(cui, registro);
                }
                br.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void guardarRegistroVacunas() {
        try {
            File archivo = new File("registro_vacunas.txt");
            BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
            for (String cui : registroVacunas.keySet()) {
                String registro = registroVacunas.get(cui);
                bw.write(cui + ":" + registro);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        RegistroVacunasGUI registroVacunasGUI = new RegistroVacunasGUI();
        registroVacunasGUI.cargarRegistroVacunas();
        registroVacunasGUI.setVisible(true);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                registroVacunasGUI.guardarRegistroVacunas();
            }
        }));
    }
}
