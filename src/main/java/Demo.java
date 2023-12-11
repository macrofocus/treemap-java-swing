import com.treemap.*;
import org.jetbrains.annotations.NotNull;
import org.mkui.colormap.MutableColorMap;
import org.mkui.font.crossplatform.CPFont;
import org.mkui.labeling.EnhancedLabel;
import org.mkui.palette.FixedPalette;
import org.mkui.palette.PaletteFactory;
import org.mkui.swing.HierarchicalComboBox;
import org.mkui.swing.Orientation;
import org.mkui.swing.SingleSelectionComboBoxModel;
import org.molap.dataframe.DataFrame;
import org.molap.dataframe.JsonDataFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Demo {
    public static void main(String[] args) throws IOException {
//        TreeMap.setLicenseKey("My Company", "ABC12-ABC12-ABC12-ABC12-ABC12-ABC12");

        InputStream inputStream = Demo.class.getResourceAsStream("Forbes Global 2000 - 2021.json");
        DataFrame<Integer,String,Object> dataFrame = JsonDataFrame.fromInputStream(inputStream);
        AbstractTreeMap<Integer,String> treeMap = new DefaultTreeMap<>(dataFrame);

        TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model = treeMap.getModel();
        TreeMapSettings<String> settings = model.getSettings();

        // General
        settings.setRendering(RenderingFactory.getFLAT());

        // Group by
        settings.setGroupByColumns(Arrays.asList("Sector", "Industry"));

        // Size
        settings.setSizeColumn("Market Value");

        // Color
        settings.setColorColumn("Profits");
        TreeMapColumnSettings profitsSettings = settings.getColumnSettings("Profits");
        final FixedPalette negpos = PaletteFactory.Companion.getInstance().get("negpos").getPalette();
        final MutableColorMap colorMap = model.getColorMap("Profits");
        colorMap.setPalette(negpos);
        colorMap.getInterval().setValue(-63.93, 127.86);

        treeMap.getView().setShowTiming(true);

        // Label
        TreeMapColumnSettings companySettings = settings.getColumnSettings("Company");
        companySettings.setLabelingFont(new CPFont(new Font("Helvetica", Font.PLAIN, 9)).getNativeFont()); // 9 points is the minimum size that will be displayed
        companySettings.setLabelingMinimumCharactersToDisplay(5);
        companySettings.setLabelingResizeTextToFitShape(true);
        companySettings.setLabelingVerticalAlignment(EnhancedLabel.CENTER);
        companySettings.setLabelingHorizontalAlignment(EnhancedLabel.CENTER);

        JPanel configuration = createConfiguration(model, settings);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, configuration, treeMap.getComponent().getNativeComponent());

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createGroupBy(Orientation.Horizontal, model, settings), BorderLayout.NORTH);
        mainPanel.add(splitPane);

        JFrame frame = new JFrame("TreeMap");

        frame.getContentPane().add(mainPanel);
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @NotNull
    private static JPanel createConfiguration(TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model, TreeMapSettings<String> settings) {
        JPanel configuration = new JPanel();
        configuration.setLayout(new BoxLayout(configuration, BoxLayout.PAGE_AXIS));

        configuration.add(createGroupBy(Orientation.Vertical, model, settings));
        configuration.add(createSizeComboBox(model, settings));
        configuration.add(createColorComboBox(model, settings));
        configuration.add(createAlgorithmComboBox(settings.getDefaultColumnSettings()));
        configuration.add(createRenderingComboBox(settings));
        configuration.add(Box.createGlue());
        return configuration;
    }

    @NotNull
    private static HierarchicalComboBox<String> createGroupBy(Orientation orientation, TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model, TreeMapSettings<String> settings) {
        final HierarchicalComboBox<String> groupBy = new HierarchicalComboBox<String>(orientation, settings.getGroupByFieldsSelection(), model.getGroupByTreeMapColumns()) {
            @Override
            public Dimension getMaximumSize() {
                return super.getPreferredSize();
            }
        };
        groupBy.setBorder(BorderFactory.createTitledBorder("Group by"));
        groupBy.setAlignmentX(0);
        return groupBy;
    }

    private static JComboBox<String> createSizeComboBox(TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model, TreeMapSettings<String> settings) {
        final JComboBox<String> sizeComboBox = new JComboBox<String>(new SingleSelectionComboBoxModel<String>(settings.getSizeFieldSelection(), model.getSizeTreeMapColumns())) {
            @Override
            public Dimension getMaximumSize() {
                return super.getPreferredSize();
            }
        };
        sizeComboBox.setBorder(BorderFactory.createTitledBorder("Size"));
        sizeComboBox.setAlignmentX(0);
        return sizeComboBox;
    }

    private static JComboBox<String> createColorComboBox(TreeMapModel<AbstractTreeMapNode<Integer, String>, Integer, String> model, TreeMapSettings<String> settings) {
        final JComboBox<String> sizeComboBox = new JComboBox<String>(new SingleSelectionComboBoxModel<String>(settings.getColorColumnSelection(), model.getColorTreeMapColumns())) {
            @Override
            public Dimension getMaximumSize() {
                return super.getPreferredSize();
            }
        };
        sizeComboBox.setBorder(BorderFactory.createTitledBorder("Size"));
        sizeComboBox.setAlignmentX(0);
        return sizeComboBox;
    }

    private static JComboBox<Algorithm> createAlgorithmComboBox(TreeMapColumnSettings settings) {
        final JComboBox<Algorithm> algorithmComboBox = new JComboBox<Algorithm>(new SingleSelectionComboBoxModel<>(settings.getAlgorithmProperty(), AlgorithmFactory.Companion.getInstance().getAlgorithms())) {
            @Override
            public Dimension getMaximumSize() {
                return super.getPreferredSize();
            }
        };
        algorithmComboBox.setBorder(BorderFactory.createTitledBorder("Algorithm"));
        algorithmComboBox.setAlignmentX(0);
        return algorithmComboBox;
    }

    private static JComboBox<Rendering> createRenderingComboBox(TreeMapSettings<String> settings) {
        final JComboBox<Rendering> renderingComboBox = new JComboBox<Rendering>(new SingleSelectionComboBoxModel<>(settings.getRenderingSelection(), RenderingFactory.getInstance().getRenderings())) {
            @Override
            public Dimension getMaximumSize() {
                return super.getPreferredSize();
            }
        };
        renderingComboBox.setBorder(BorderFactory.createTitledBorder("Rendering"));
        renderingComboBox.setAlignmentX(0);
        return renderingComboBox;
    }
}
