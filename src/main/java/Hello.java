import com.treemap.AbstractTreeMap;
import com.treemap.AlgorithmFactory;
import com.treemap.DefaultTreeMap;
import org.molap.swing.TableModelDataFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class Hello {
    public static void main(String[] args) {
        // Defining the data, column names and types
        Object[][] data = new Object[][]{
                {"Hello", 12, 3.0},
                {"from", 11, 4.0},
                {"the", 9, 5.0},
                {"TreeMap", 8, 6.0},
                {"World!", 7, 7.0},
        };
        Object[] columnNames = new Object[]{"Name", "Value", "Strength"};
        final Class[] columnTypes = new Class[]{String.class, Integer.class, Double.class};

        // Creating a standard Swing TableModel
        TableModel tableModel = new DefaultTableModel(data, columnNames) {
            public Class<?> getColumnClass(int columnIndex) {
                return columnTypes[columnIndex];
            }
        };

        // Creating the TreeMap
        AbstractTreeMap<Integer,String> treeMap = new DefaultTreeMap<>(new TableModelDataFrame(tableModel));

        // Tuning the appearance of the TreeMap
        treeMap.setAlgorithm(AlgorithmFactory.Companion.getSQUARIFIED());
        treeMap.setSizeByName("Value");
        treeMap.setColor(2);
        treeMap.setBackgroundByName("Name");
        treeMap.setLabels();

        // Creating a frame to display
        final JFrame frame = new JFrame("Hello from the TreeMap World!");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(treeMap.getComponent().getNativeComponent());
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}