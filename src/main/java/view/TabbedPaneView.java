package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.JFXPanel;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;

import model.AbstractCompoundFeature;
import model.AbstractModel;
import model.FloorplanModel;
import model.floorplan.City;
import model.floorplan.Column2DModel;
import model.floorplan.Corner2DModel;
import model.floorplan.FloorplanResultModel;
import model.floorplan.GridLineModel;
import model.floorplan.RoomModel;
import model.floorplan.Space2DModel;
import model.floorplan.UtilityModel;
import model.floorplan.Wall2DModel;
import model.floorplan.function.Function;

import controller.AbstractController;
import controller.EngineeringController;
import view.data.TradeoffDataUtility;
import view.data.model.ScatterPlotDataModel;

public class TabbedPaneView extends AbstractView
{
    private static final long serialVersionUID = 1L;
    private Boolean DEBUG = false;
    private EngineeringController controller;
    private TableViewAdapterCenterLine    tvcl;
    private TableViewAdapterSpace tvs;
    private TableViewAdapterColumn tvc;
    private TableViewAdapterCorner tvcr;
    private TableViewAdapterWall tvw;
    private TableViewAdapterRoom tvr;
    private TableViewAdapterFloorplan tvf;
    private TableViewAdapterRequirements tvReq;
    private TableRowSorter   sorter;
    private JTable tableCL;
    private JTable tableS;
    private JTable tableC;
    private JTable tableCr;
    private JTable tableW;
    private JTable tableR;
    private JTable tableF;
    private JTable tableReq;
    private ListSelectionModel listSelectionModel;
    private int resultCount = 0;

    private static JFXPanel caseFxPanel;
    private static JFXPanel chartFxPanel;
    private static JFXPanel enFxPanel;
    private static JFXPanel costFxPanel;
    private static JFXPanel mesFxPanel;
    private static JFXPanel mcsFxPanel;
    private static JFXPanel elcFxPanel;
    private Pane caseSelector;

    //Electricity chart
    private Chart eChart;
    private ScatterChart<Number, Number> eScatterChart;

    //Single case
    private HashMap<String, Map<String, Double>> allTradeoffZonesTonsSeries =
            new HashMap<String, Map<String, Double>>();
    private HashMap<String, ArrayList<ScatterPlotDataModel>> oneCaseSeries =
            new HashMap<String, ArrayList<ScatterPlotDataModel>>();
    private LineChart<String, Double> lineChart;
    private ScatterChart<Number,Number> scatterChart;
    private ScatterChart<Number,Number> cScatterChart;

    private Chart lChart;
    private Chart sChart;
    private Chart cSChart;

    //Multi case
    private HashMap<String, ArrayList<ScatterPlotDataModel>> multiCaseSeries =
            new HashMap<String, ArrayList<ScatterPlotDataModel>>();
    private ScatterChart<Number,Number> multiEnergyScatterChart;
    private ScatterChart<Number,Number> multiCostScatterChart;

    private Chart mesChart;
    private Chart mcsChart;

    public TabbedPaneView( AbstractController controller ) {
        this.controller = (EngineeringController) controller;

        // Create blank table view adapter ...

        ArrayList<GridLineModel> centerlines = new ArrayList<GridLineModel>();
        tvcl = new TableViewAdapterCenterLine( centerlines );

        ArrayList<Space2DModel> spaces = new ArrayList<Space2DModel>();
        tvs = new TableViewAdapterSpace( spaces );

        ArrayList<Column2DModel> columns = new ArrayList<Column2DModel>();
        tvc = new TableViewAdapterColumn( columns );

        ArrayList<Corner2DModel> corners = new ArrayList<Corner2DModel>();
        tvcr = new TableViewAdapterCorner( corners );

        ArrayList<Wall2DModel> walls = new ArrayList<Wall2DModel>();
        tvw = new TableViewAdapterWall( walls );

        ArrayList<RoomModel> rooms = new ArrayList<RoomModel>();
        tvr = new TableViewAdapterRoom( rooms );

        ArrayList<FloorplanResultModel> results = new ArrayList<FloorplanResultModel>();
        tvf = new TableViewAdapterFloorplan( results );

        ArrayList<Function> requirements = new ArrayList<Function>();
        tvReq = new TableViewAdapterRequirements( requirements );

        TradeoffDataUtility.test();
    }

    public void addRow( Object f ) {

        if( f instanceof GridLineModel )
            tvcl.addRow( (GridLineModel)f );
        else if( f instanceof Space2DModel )
            tvs.addRow( (Space2DModel)f );
        else if( f instanceof Column2DModel )
            tvc.addRow( (Column2DModel)f );
        else if( f instanceof Corner2DModel )
            tvcr.addRow( (Corner2DModel)f );
        else if( f instanceof Wall2DModel )
            tvw.addRow( (Wall2DModel)f );
        else if( f instanceof RoomModel )
            tvr.addRow( (RoomModel)f );
        else if( f instanceof FloorplanResultModel )
            tvf.addRow( (FloorplanResultModel)f );
        else
            System.out.println( "ERROR: Can't handle "+ f.toString() );
    }

    public void changeStatus( AbstractCompoundFeature f ) {
        System.out.println("*** In TabbedPaneView.changeStatus() ... ");

        if( f instanceof GridLineModel )
            tvcl.changeStatus();
        else if( f instanceof Space2DModel )
            tvs.changeStatus();
        else if( f instanceof Column2DModel )
            tvc.changeStatus();
        else if( f instanceof Corner2DModel )
            tvcr.changeStatus();
        else if( f instanceof Wall2DModel )
            tvw.changeStatus();
        else
            System.out.println( "ERROR: Can't handle "+ f.toString() );
    }

    // Display frame ....

    public void display() {
        tableCL = new JTable( tvcl );
        tableCL.setRowHeight(45);
        tableCL.setRowSelectionAllowed(true);
        tableCL.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        tableCL.addMouseListener( new MouseListener() {

           // Click once to highlight appropriate entry in table view ...
           // Click twice to trigger rule checking on a selected item ...
           public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 1) {
                 int row = tableCL.getSelectedRow();
                 GridLineModel s = (GridLineModel) tvcl.getRowEntry(row);
                 controller.changeFloorplanComponentStatus ( s );
              }

              if (e.getClickCount() == 2) {
              }
           }

           // Clear the highlighting from above
           public void mouseExited(MouseEvent e) {
              tableCL.clearSelection();
           }
           public void mouseEntered(MouseEvent e) {}
           public void mousePressed(MouseEvent e) {}
           public void mouseReleased(MouseEvent e) {}
        });
        tableCL.getModel().addTableModelListener( new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
               if( e.getColumn() == 3 ) {
                   controller.updateFloorplanProperty( e.getColumn() );
                   resultCount = resultCount + 1;
                   for( City city: City.getDefaultCities() ){
                       FloorplanResultModel model = new FloorplanResultModel( resultCount, city );
                       controller.setFloorplanSummary( model );
                   }
               }
            }
          });

        tableS = new JTable( tvs );
        tableS.setRowHeight(45);
        tableS.setRowSelectionAllowed(true);
        tableS.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        tableS.addMouseListener( new MouseListener() {

               // Click once to highlight appropriate entry in table view ...
               // Click twice to trigger rule checking on a selected item ...
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 1) {
                     int row = tableS.getSelectedRow();
                     Space2DModel s = (Space2DModel) tvs.getRowEntry(row);
                     controller.changeFloorplanComponentStatus ( s );
                  }

                  if (e.getClickCount() == 2) {
                  }
               }

               // Clear the highlighting from above
               public void mouseExited(MouseEvent e) {
                  tableS.clearSelection();
               }
               public void mouseEntered(MouseEvent e) {}
               public void mousePressed(MouseEvent e) {}
               public void mouseReleased(MouseEvent e) {}
            });

        tableC = new JTable( tvc );
        tableC.setRowHeight(45);
        tableC.setRowSelectionAllowed(true);
        tableC.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        tableC.addMouseListener( new MouseListener() {

               // Click once to highlight appropriate entry in table view ...
               // Click twice to trigger rule checking on a selected item ...
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 1) {
                     int row = tableC.getSelectedRow();
                     Column2DModel s = (Column2DModel) tvc.getRowEntry(row);
                     controller.changeFloorplanComponentStatus ( s );
                  }

                  if (e.getClickCount() == 2) {
                  }
               }

               // Clear the highlighting from above
               public void mouseExited(MouseEvent e) {
                  tableC.clearSelection();
               }
               public void mouseEntered(MouseEvent e) {}
               public void mousePressed(MouseEvent e) {}
               public void mouseReleased(MouseEvent e) {}
            });

        tableCr = new JTable( tvcr );
        tableCr.setRowHeight(45);
        tableCr.setRowSelectionAllowed(true);
        tableCr.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        tableCr.addMouseListener( new MouseListener() {

               // Click once to highlight appropriate entry in table view ...
               // Click twice to trigger rule checking on a selected item ...
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 1) {
                     int row = tableCr.getSelectedRow();
                     Corner2DModel s = (Corner2DModel) tvcr.getRowEntry(row);
                     controller.changeFloorplanComponentStatus ( s );
                  }

                  if (e.getClickCount() == 2) {
                  }
               }

               // Clear the highlighting from above
               public void mouseExited(MouseEvent e) {
                  tableCr.clearSelection();
               }
               public void mouseEntered(MouseEvent e) {}
               public void mousePressed(MouseEvent e) {}
               public void mouseReleased(MouseEvent e) {}
            });

        tableW = new JTable( tvw );
        tableW.setRowHeight(45);
        tableW.setRowSelectionAllowed(true);
        tableW.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        tableW.addMouseListener( new MouseListener() {

               // Click once to highlight appropriate entry in table view ...
               // Click twice to trigger rule checking on a selected item ...
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 1) {
                     int row = tableW.getSelectedRow();
                     Wall2DModel s = (Wall2DModel) tvw.getRowEntry(row);
                     controller.changeFloorplanComponentStatus ( s );
                  }

                  if (e.getClickCount() == 2) {
                  }
               }

               // Clear the highlighting from above
               public void mouseExited(MouseEvent e) {
                  tableW.clearSelection();
               }
               public void mouseEntered(MouseEvent e) {}
               public void mousePressed(MouseEvent e) {}
               public void mouseReleased(MouseEvent e) {}
            });

        tableR = new JTable( tvr );
        tableR.setRowHeight(45);
        tableR.setRowSelectionAllowed(true);
        tableR.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        tableR.addMouseListener( new MouseListener() {

               // Click once to highlight appropriate entry in table view ...
               // Click twice to trigger rule checking on a selected item ...
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 1) {
                  }

                  if (e.getClickCount() == 2) {
                  }
               }

               // Clear the highlighting from above
               public void mouseExited(MouseEvent e) {
                  tableR.clearSelection();
               }
               public void mouseEntered(MouseEvent e) {}
               public void mousePressed(MouseEvent e) {}
               public void mouseReleased(MouseEvent e) {}
            });

        tableF = new JTable( tvf );
        tableF.setRowHeight(45);
        tableF.setRowSelectionAllowed(true);
        tableF.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);
        listSelectionModel = tableF.getSelectionModel();
        listSelectionModel.addListSelectionListener(new SharedListSelectionHandler());
        tableF.setSelectionModel(listSelectionModel);

        /*
        tableF.addMouseListener( new MouseListener() {

               // Click once to highlight appropriate entry in table view ...
               // Click twice to trigger rule checking on a selected item ...
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 1) {
                  }

                  if (e.getClickCount() == 2) {
                  }
               }

               // Clear the highlighting from above
               public void mouseExited(MouseEvent e) {
                   tableF.clearSelection();
               }
               public void mouseEntered(MouseEvent e) {}
               public void mousePressed(MouseEvent e) {}
               public void mouseReleased(MouseEvent e) {}
            });
        */

        tableReq = new JTable( tvReq );
        tableReq.setRowHeight(45);
        tableReq.setRowSelectionAllowed(true);
        tableReq.setSelectionMode( ListSelectionModel.MULTIPLE_INTERVAL_SELECTION );

        tableReq.addMouseListener( new MouseListener() {

               // Click once to highlight appropriate entry in table view ...
               // Click twice to trigger rule checking on a selected item ...
               public void mouseClicked(MouseEvent e) {
                  if (e.getClickCount() == 1) {
                  }

                  if (e.getClickCount() == 2) {
                  }
               }

               // Clear the highlighting from above
               public void mouseExited(MouseEvent e) {
                   tableReq.clearSelection();
               }
               public void mouseEntered(MouseEvent e) {}
               public void mousePressed(MouseEvent e) {}
               public void mouseReleased(MouseEvent e) {}
            });

        // Create gui components and layout ....

        JTabbedPane tabbedPane = new JTabbedPane();

        JScrollPane scrollPaneCL = new JScrollPane( tableCL );
        scrollPaneCL.setPreferredSize(new java.awt.Dimension(600, 600));
        tabbedPane.addTab( "Center Line", scrollPaneCL );

        JScrollPane scrollPaneS = new JScrollPane( tableS );
        scrollPaneS.setPreferredSize(new java.awt.Dimension(600, 300));
        JPanel spaceChildPanel = createExtraFunctionPanel();
        spaceChildPanel.setPreferredSize(new java.awt.Dimension(600, 300));
        JSplitPane spaceSplitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, true,
                scrollPaneS, spaceChildPanel );

        tabbedPane.addTab( "Space", spaceSplitPane );

        JScrollPane scrollPaneC = new JScrollPane( tableC );
        scrollPaneC.setPreferredSize(new java.awt.Dimension(600, 600));
        tabbedPane.addTab( "Column", scrollPaneC );

        JScrollPane scrollPaneCr = new JScrollPane( tableCr );
        scrollPaneCr.setPreferredSize(new java.awt.Dimension(600, 600));
        tabbedPane.addTab( "Corner", scrollPaneCr );

        JScrollPane scrollPaneW = new JScrollPane( tableW );
        scrollPaneW.setPreferredSize(new java.awt.Dimension(600, 600));
        tabbedPane.addTab( "Wall", scrollPaneW );

        JScrollPane scrollPaneR = new JScrollPane( tableR );
        scrollPaneW.setPreferredSize(new java.awt.Dimension(600, 600));
        tabbedPane.addTab( "Room", scrollPaneR );

        JPanel floorPane = createFloorPane();
        JScrollPane scrollPaneFloorplan = new JScrollPane( floorPane );
        scrollPaneFloorplan.setPreferredSize(new java.awt.Dimension(600, 600));
        tabbedPane.addTab( "Floorplan", scrollPaneFloorplan );

        JScrollPane scrollPaneF = new JScrollPane( tableF );
        scrollPaneF.setPreferredSize(new java.awt.Dimension(600, 300));

        JScrollPane reqChildPanel = new JScrollPane( tableReq );
        reqChildPanel.setPreferredSize(new java.awt.Dimension(450, 300));

        caseFxPanel = new JFXPanel();
        JPanel casePanel = new JPanel();
        casePanel.setPreferredSize(new java.awt.Dimension(150, 300));
        casePanel.setLayout(new BorderLayout());
        casePanel.add(caseFxPanel, BorderLayout.CENTER);

        JSplitPane reqSplitPane = new JSplitPane( JSplitPane.HORIZONTAL_SPLIT, true, reqChildPanel, casePanel );

        JSplitPane resultSplitPane = new JSplitPane( JSplitPane.VERTICAL_SPLIT, true, scrollPaneF, reqSplitPane );
        tabbedPane.addTab( "Summary", resultSplitPane );

        //JaveFX Plotting
        chartFxPanel = new JFXPanel();
        enFxPanel    = new JFXPanel();
        costFxPanel  = new JFXPanel();
        mesFxPanel   = new JFXPanel();
        mcsFxPanel   = new JFXPanel();
        elcFxPanel   = new JFXPanel();
        
        JTabbedPane tradeoffTabbedPane = new JTabbedPane();
        
        JPanel elcChartPanel = new JPanel();
        elcChartPanel.setLayout(new BorderLayout());
        elcChartPanel.add(elcFxPanel, BorderLayout.CENTER);
        tradeoffTabbedPane.addTab("Electricity", elcChartPanel);
        
        JPanel chartTablePanel = new JPanel();
        chartTablePanel.setLayout(new BorderLayout());
        chartTablePanel.add(chartFxPanel, BorderLayout.CENTER);
        tradeoffTabbedPane.addTab("Tons", chartTablePanel);
        
        JPanel enChartPanel = new JPanel();
        enChartPanel.setLayout(new BorderLayout());
        enChartPanel.add(enFxPanel, BorderLayout.CENTER);
        tradeoffTabbedPane.addTab("Energy Consumption", enChartPanel);
        
        JPanel cosChartPanel = new JPanel();
        cosChartPanel.setLayout(new BorderLayout());
        cosChartPanel.add(costFxPanel, BorderLayout.CENTER);
        tradeoffTabbedPane.addTab("Cost", cosChartPanel);
        
        JPanel meChartPanel = new JPanel();
        meChartPanel.setLayout(new BorderLayout());
        meChartPanel.add(mesFxPanel, BorderLayout.CENTER);
        tradeoffTabbedPane.addTab("Multi-energy Consumption", meChartPanel);
        
        JPanel mcChartPanel = new JPanel();
        mcChartPanel.setLayout(new BorderLayout());
        mcChartPanel.add(mcsFxPanel, BorderLayout.CENTER);
        tradeoffTabbedPane.addTab("Multi-cost", mcChartPanel);
        
        tabbedPane.addTab("Tradeoff", tradeoffTabbedPane);
        
        JFrame frame = new JFrame("Floorplan Features Table View");
        frame.getContentPane().add( tabbedPane );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // create JavaFX scene
        Platform.runLater(new Runnable() {
            public void run() {
                createScene();
                eScatterChart.setData( getElectricityData() );
            }
        });
    }

    @Override
    public void modelPropertyChange( PropertyChangeEvent e ) {
        System.out.println("*** Enter TabbedPaneView.modelPropertyChange() ... ");

        if (e.getPropertyName().equals( EngineeringController.FLOORPLAN_HASHMAP ) ) {
            if( e.getNewValue() != null && e.getNewValue() instanceof HashMap )
            {
                ArrayList<GridLineModel> addedLines = new ArrayList<GridLineModel>();
                ArrayList<String> keys = new ArrayList<String>();
                for( AbstractModel model : controller.getRegisteredModels() )
                {
                    if( model instanceof FloorplanModel )
                    {
                        keys.addAll( ((FloorplanModel)model).addedKeys );
                        ((FloorplanModel)model).addedKeys.clear();
                        addedLines.addAll( ((FloorplanModel)model).getGridSystem().addedLines );
                        ((FloorplanModel)model).getGridSystem().addedLines.clear();
                    }
                }

                for( int i = 0; i < keys.size(); i++ )
                {
                    AbstractCompoundFeature newObject =
                            (AbstractCompoundFeature)((HashMap)e.getNewValue()).get( keys.get(i) );
                    addRow( newObject );
                }

                for( int i = 0; i < addedLines.size(); i++ )
                {
                    GridLineModel newLine = addedLines.get( i );
                    addRow( newLine );
                }
            }
        }

        if (e.getPropertyName().equals( EngineeringController.FLOORPLAN_SELECTED_PROPERTY_STATUS ) ) {
            AbstractCompoundFeature newObject = (AbstractCompoundFeature) (e.getNewValue());
            changeStatus( newObject );
        }

        if ( e.getPropertyName().equals( EngineeringController.FLOORPLAN_DEFINE_ROOM_PROPERTY ) ) {
            RoomModel newObject = (RoomModel) ( e.getNewValue() );
            addRow( newObject );
        }

        if ( e.getPropertyName().equals( EngineeringController.FLOORPLAN_SUMMARY ) ) {
            FloorplanResultModel newObject = (FloorplanResultModel) (e.getNewValue() );
            addRow( newObject );
        }

        if ( e.getPropertyName().equals( EngineeringController.FLOORPLAN_CASE_CITY ) ) {
            @SuppressWarnings("unchecked")
            ArrayList<FloorplanResultModel> newObject
                = (ArrayList<FloorplanResultModel>)(e.getNewValue() );

            if( DEBUG )
                System.out.println("TABBED PANEL FIRE CASE CITY");

            createTradeoffData( newObject );
        }

        if ( e.getPropertyName().equals( EngineeringController.FLOORPLAN_MULTICASES ) ) {
            @SuppressWarnings("unchecked")
            ArrayList<FloorplanResultModel> newObject
                = (ArrayList<FloorplanResultModel>)(e.getNewValue() );

            if( DEBUG )
                System.out.println("TABBED PANEL FIRE MULTI CASE CITY");

            createMultiTradeoffData( newObject );
        }
    }

    /**
     * Create the JPanel inside Space tab in TabbedPaneView
     * @return JPanel the lower panel inside Space tab.
     */
    protected JPanel createExtraFunctionPanel()
    {
        final RoomModel room = new RoomModel();
        JPanel childPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();
        JLabel childLabel = new JLabel("**Select sapce/spaces to define room**");
        c.gridx = 0;
        c.gridy = 0;
        childPanel.add( childLabel, c );
        
        JLabel childLabel2 = new JLabel("Name of the room :");
        c.gridx = 0;
        c.gridy = 1;
        childPanel.add( childLabel2, c );
        
        final JTextField nameField = new JTextField( 10 );
        nameField.setEditable( true );
        nameField.getDocument().addDocumentListener( new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent arg0) {
                room.setName( nameField.getText() );

            }

            @Override
            public void insertUpdate(DocumentEvent arg0) {
                room.setName( nameField.getText() );

            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {
            }
        });
        c.gridx = 1;
        c.gridy = 1;
        childPanel.add( nameField, c );
        
        JLabel childLabel3 = new JLabel("Role of the room:");
        c.gridx = 0;
        c.gridy = 2;
        childPanel.add( childLabel3, c );
        
        ArrayList<String> selections = new ArrayList<String>();
        selections.add( "" );
        selections.add( RoomModel.LIVING_ROOM );
        selections.add( RoomModel.BED_ROOM );
        selections.add( RoomModel.KITCHEN );
        selections.add( RoomModel.OFFICE );
        selections.add( RoomModel.UTILITY );
        final JComboBox roles = new JComboBox( selections.toArray() );
        roles.addItemListener(new ItemListener() {
            public void itemStateChanged( ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    if( item instanceof String)
                    {
                        if( ((String)item).length() > 0 )
                            room.setRole( (String)item );
                    }
                }
            }
        });
        c.gridx = 1;
        c.gridy = 2;
        childPanel.add( roles, c );
        
        JLabel childLabel4 = new JLabel("Define Zone :");
        c.gridx = 0;
        c.gridy = 3;
        childPanel.add( childLabel4, c );
        
        ArrayList<String> items = new ArrayList<String>();
        items.add( "" );
        items.add( RoomModel.ZONE_1 );
        items.add( RoomModel.ZONE_2 );
        final JComboBox zoneField = new JComboBox( items.toArray() );
        zoneField.addItemListener(new ItemListener() {
            public void itemStateChanged( ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    if( item instanceof String)
                    {
                        if( ((String)item).length() > 0 )
                            room.setZone( (String)item );
                    }
                }
            }
        });
        c.gridx = 1;
        c.gridy = 3;
        childPanel.add( zoneField, c );
        
        JLabel occupantsLabel = new JLabel("Number of occupants:");
        c.gridx = 0;
        c.gridy = 4;
        childPanel.add( occupantsLabel, c );
        
        final JTextField occupantsField = new JTextField( 10 );
        occupantsField.setEditable( true );
        occupantsField.getDocument().addDocumentListener( new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent arg0) {
                if( occupantsField.getText() != null && occupantsField.getText().length() > 0 )
                    room.setOccupants( (int)Double.parseDouble( occupantsField.getText() ));
            }

            @Override
            public void insertUpdate(DocumentEvent arg0) {
                if( occupantsField.getText() != null && occupantsField.getText().length() > 0 )
                    room.setOccupants( (int)Double.parseDouble( occupantsField.getText() ) );
            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {
            }
        });
        c.gridx = 1;
        c.gridy = 4;
        childPanel.add( occupantsField, c );
        
        JButton button = new JButton("Define Room");
        //Add action listener to button
        button.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                if( DEBUG == true )
                    System.out.println( "TabbedPaneView.createExtraFunctionPanel() Define room clicked" );
                
                if( tableS.getModel() instanceof TableViewAdapterSpace )
                {
                    TableViewAdapterSpace spaceModel = (TableViewAdapterSpace) tableS.getModel();
                    ArrayList<Space2DModel> selectedSpaces = new ArrayList<Space2DModel>();
                    for( int i = 0; i < spaceModel.getSpaces().size(); i++ )
                    {
                        Space2DModel space = spaceModel.getSpaces().get(i);
                        if( space.getSelection() )
                        {
                            selectedSpaces.add( space );
                            if( DEBUG == true )
                                System.out.println( "TabbedPaneView.createExtraFunctionPanel() selectedSpaces includes space : " + space );
                        }
                    }
                    if( selectedSpaces.size() != 0 )
                    {
                        if( room.getName().length() > 0 && room.getName() != null )
                        {
                            if( room.getRole().length() > 0 && room.getRole() != null )
                            {
                                room.setSpaces( selectedSpaces );
                                RoomModel definedRoom = room.clone();
                                controller.defineFloorplanRoomComponent( definedRoom );
                                nameField.setText( "" );
                                roles.setSelectedItem( "" );
                                zoneField.setSelectedItem( "" );
                                occupantsField.setText( "" );
                                room.initialModel();
                            }
                            else
                                System.out.println( "TabbedPaneView.createExtraFunctionPanel() room role is not valid " );
                        }
                        else
                            System.out.println( "TabbedPaneView.createExtraFunctionPanel() room name is not valid " );
                    }

                }
            }
        }); 
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 5;
        childPanel.add( button, c );
        
        return childPanel;
    }

    private JPanel createFloorPane() {
        final UtilityModel  model = new UtilityModel();
        JPanel childPanel = new JPanel( new GridBagLayout() );
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets( 1, 1, 1, 1);
        JLabel childLabel = new JLabel("**Enter general information for the Floorplan system**");
        c.gridwidth = GridBagConstraints.REMAINDER;
        childPanel.add( childLabel, c );

        JLabel childLabel2 = new JLabel("Square pixels to square foot ratio :");
        c.gridwidth = 1;
        childPanel.add( childLabel2, c );
        
        final JTextField ratio = new JTextField( 10 );
        ratio.setEditable( true );
        ratio.getDocument().addDocumentListener( new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent arg0) {
                if( ratio.getText() != null && ratio.getText().length() > 0 )
                    model.setRatio( Double.parseDouble( ratio.getText() ) );
            }

            @Override
            public void insertUpdate(DocumentEvent arg0) {
                if( ratio.getText() != null && ratio.getText().length() > 0 )
                    model.setRatio( Double.parseDouble( ratio.getText() ) );
            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {
            }
        });
        c.gridwidth = GridBagConstraints.REMAINDER;
        childPanel.add( ratio, c );

        JLabel groupLabel = new JLabel("Building Type:");
        c.gridwidth = 1;
        childPanel.add( groupLabel, c );

        ArrayList<String> group = new ArrayList<String>();
        group.add( "" );
        group.add( UtilityModel.BUSINESS );
        group.add( UtilityModel.EDUCATION );
        group.add( UtilityModel.R_2 );
        group.add( UtilityModel.R_3 );
        
        final JComboBox groupType = new JComboBox( group.toArray() );
        groupType.addItemListener(new ItemListener() {
            public void itemStateChanged( ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    if( item instanceof String)
                    {
                        if( ((String)item).length() > 0 )
                            model.setGroup( (String)item );
                    }
                }
            }
        });
        c.gridwidth = GridBagConstraints.REMAINDER;
        childPanel.add( groupType, c );

        JLabel childLabel3 = new JLabel("Construction Type:");
        c.gridwidth = 1;
        childPanel.add( childLabel3, c );

        ArrayList<String> selections = new ArrayList<String>();
        selections.add( "" );
        selections.add( UtilityModel.TYPE_I_A );
        selections.add( UtilityModel.TYPE_I_B );
        selections.add( UtilityModel.TYPE_II_A );
        selections.add( UtilityModel.TYPE_II_B );
        selections.add( UtilityModel.TYPE_III_A );
        selections.add( UtilityModel.TYPE_III_B );
        selections.add( UtilityModel.TYPE_IV );
        selections.add( UtilityModel.TYPE_V_A );
        selections.add( UtilityModel.TYPE_V_B );
        
        final JComboBox constructionType = new JComboBox( selections.toArray() );
        constructionType.addItemListener(new ItemListener() {
            public void itemStateChanged( ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    Object item = event.getItem();
                    if( item instanceof String)
                    {
                        if( ((String)item).length() > 0 )
                            model.setConstructionType( (String)item );
                    }
                }
            }
        });
        c.gridwidth = GridBagConstraints.REMAINDER;
        childPanel.add( constructionType, c );
        
        JLabel priceLabel = new JLabel("Price per square pixels :");
        c.gridwidth = 1;
        childPanel.add( priceLabel, c );
        
        final JTextField priceField = new JTextField( 10 );
        priceField.setEditable( true );
        priceField.getDocument().addDocumentListener( new DocumentListener(){
            @Override
            public void changedUpdate(DocumentEvent arg0) {
                if( priceField.getText() != null && priceField.getText().length() > 0 )
                    model.setPriceRatio( Double.parseDouble( priceField.getText() ) );
            }

            @Override
            public void insertUpdate(DocumentEvent arg0) {
                if( priceField.getText() != null && priceField.getText().length() > 0 )
                    model.setPriceRatio( Double.parseDouble( priceField.getText() ) );
            }

            @Override
            public void removeUpdate(DocumentEvent arg0) {
            }
        });
        c.gridwidth = GridBagConstraints.REMAINDER;
        childPanel.add( priceField, c );

        JButton button = new JButton("Set values");
        //Add action listener to button
        button.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                //Execute when button is pressed
                if( DEBUG )
                    System.out.println( "TabbedPaneView.createFloorPane() Set values clicked" );
                controller.setFloorplanUtility( model );
                resultCount = resultCount + 1;
                   for( City city: City.getDefaultCities() ){
                       FloorplanResultModel result = new FloorplanResultModel( resultCount, city );
                       controller.setFloorplanSummary( result );
                   }
            }
        });
        c.gridwidth = GridBagConstraints.REMAINDER;
        childPanel.add( button, c );

        return childPanel;
    }

    private void createScene() {
        lChart = createLineChart();
        StackPane root1 = new StackPane();
        root1.getChildren().add(lChart);
        chartFxPanel.setScene( new Scene(root1) );
        
        sChart = createScatterChart();
        StackPane root2 = new StackPane();
        root2.getChildren().add(sChart);
        enFxPanel.setScene( new Scene(root2) );
        
        cSChart = createCostScatterChart();
        StackPane root3 = new StackPane();
        root3.getChildren().add(cSChart);
        costFxPanel.setScene( new Scene(root3) );
        
        mesChart = createMultiEnergyScatterChart();
        StackPane root4 = new StackPane();
        root4.getChildren().add(mesChart);
        mesFxPanel.setScene( new Scene(root4) );
        
        mcsChart = createMultiCostScatterChart();
        StackPane root5 = new StackPane();
        root5.getChildren().add(mcsChart);
        mcsFxPanel.setScene( new Scene(root5) );
        
        eChart = createElectricityScatterChart();
        StackPane root6 = new StackPane();
        root6.getChildren().add(eChart);
        elcFxPanel.setScene( new Scene(root6) );
        
        caseSelector = createCaseSelector();
        caseFxPanel.setScene( new Scene(caseSelector) );
    }

    /**
     * Creates the selection panel for the case data that will be
     * using to draw inside the tradeoff tab
     */
    private Pane createCaseSelector() {
        Pane selector = new Pane();
        ListView<String> listView = new ListView<String>();
        ObservableList<String> list = FXCollections.observableArrayList();
        listView.setItems(list);

        list.add("Case 1");
        list.add("Case 2");
        list.add("Case 3");
        list.add("Case 4");
        list.add("Case 5");
        list.add("Case 6");
        list.add("Case 7");
        list.add("Case 8");
        list.add("Case 9");
        list.add("Case 10");

        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        final ListView<String> listV = listView;
        listView.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                ObservableList<String> selectedItems =  listV.getSelectionModel().getSelectedItems();

                int[] caseNum = new int[] { 0, 0, 0, 0 };
                if( selectedItems.size() == 1 ) //one case data for City trade off
                {
                    String caseString = selectedItems.get( 0 );
                    String[] splited = caseString.split("\\s+");
                    caseNum[0] = Integer.parseInt( splited[1] );
                    controller.setFloorplanCityTradeOffData( caseNum );
                }
                else if( selectedItems.size() >= 2 &&  selectedItems.size() <= 3 )
                {
                    for( int i = 0; i < selectedItems.size(); i++ )
                    {
                        String caseString = selectedItems.get( i );
                        String[] splited = caseString.split("\\s+");
                        caseNum[i] = Integer.parseInt( splited[1] );
                    }
                    controller.setFloorplanCityTradeOffData( caseNum );
                }
                else
                    System.out.println( "Can't handle too many cases..." );
            }
        }); 

        selector.getChildren().add(listView);
        
        return selector;
    }

    /**
     * Creates tradeoff data compare chart data from the given list
     * @param list selected case FloorplanResultModels
     */
    private void createTradeoffData( ArrayList<FloorplanResultModel> list ) {
        for( FloorplanResultModel model : list )
        {
            allTradeoffZonesTonsSeries.put( model.getCity().getLocation(), getZoneTons( model ) );
            ArrayList<ScatterPlotDataModel> scatterlist = TradeoffDataUtility.getEnergyComsumptionData( model );
            oneCaseSeries.put( model.getCity().getLocation(), scatterlist );
        }

        if( DEBUG )
        {
            System.out.println( "TRADE_OFF TONS data size :" + allTradeoffZonesTonsSeries.size() );
            System.out.println( "TRADE_OFF ONE CASE data size :" + oneCaseSeries.size() );
        }

        lineChart.setData( getHeatPumpData( allTradeoffZonesTonsSeries ) );
        scatterChart.setData( getTradeoffData( oneCaseSeries, false ) );
        cScatterChart.setData( getTradeoffData( oneCaseSeries, true ) );

        installTooltips( scatterChart, oneCaseSeries, false );
        installTooltips( cScatterChart, oneCaseSeries, true );
    }

    /**
     * Creates multiple tradeoff data compare chart data from the given list
     * @param list selected case FloorplanResultModels
     */
    private void createMultiTradeoffData( ArrayList<FloorplanResultModel> list )
    {
        for( FloorplanResultModel model : list )
        {
            ArrayList<ScatterPlotDataModel> scatterlist = TradeoffDataUtility.getEnergyComsumptionData( model );

            if( multiCaseSeries.get(model.getCity().getLocation() ) != null )
            {
                ArrayList<ScatterPlotDataModel> d1 = multiCaseSeries.get( model.getCity().getLocation() );
                scatterlist.addAll( d1 );
            }

            multiCaseSeries.put( model.getCity().getLocation(), scatterlist );
        }
        if( DEBUG )
            System.out.println( "TRADE_OFF ENERGY data size :" + multiCaseSeries.size() );

        multiEnergyScatterChart.setData( getTradeoffData( multiCaseSeries, false ) );
        multiCostScatterChart.setData( getTradeoffData( multiCaseSeries, true ) );

        installTooltips( multiEnergyScatterChart, multiCaseSeries, false );
        installTooltips( multiCostScatterChart, multiCaseSeries, true );
    }

    /**
     * Install tool tips data
     * Mouse over a point for pop-up info implementation
     * @param chart that will be installing the tool tips
     * @param data tradeoff case data
     * @param isCost true if install for cost data
     */
    private void installTooltips( ScatterChart<Number,Number> chart,
            HashMap<String, ArrayList<ScatterPlotDataModel>> data, boolean isCost )
    {
        for (XYChart.Series<Number, Number> s :  chart.getData() ) {
            ArrayList<ScatterPlotDataModel> mlist = data.get( s.getName() );
            for (XYChart.Data<Number, Number> d : s.getData()) {
                for( ScatterPlotDataModel m : mlist )
                {
                    if( isCost )
                    {
                        if( d.getXValue().doubleValue() == m.getAreaSize()
                                && d.getYValue().doubleValue() == m.getCost() )
                        {
                            Tooltip.install( d.getNode(), new Tooltip(
                                    String.format( m.getHeatPump().getModelName() + "/ %2.1f USD", 
                                            d.getYValue().doubleValue() ) ) );
                            if( DEBUG )
                                System.out.println( "INSTALL TOOLTIP:" + d.getXValue() + "/" + d.getYValue() );
                        }
                    }
                    else // energy consumption
                    {
                        if( d.getXValue().doubleValue() == m.getAreaSize()
                                && d.getYValue().doubleValue() == m.getEnergyConsumption() )
                        {
                            Tooltip.install( d.getNode(), new Tooltip(
                                    String.format( m.getHeatPump().getModelName() + "/ %2.1f kWh", 
                                            d.getYValue().doubleValue() ) ) );
                            if( DEBUG )
                                System.out.println( "INSTALL TOOLTIP:" + d.getXValue() + "/" + d.getYValue() );
                        }
                    }
                }
            }
        }
    }

    private ObservableList<XYChart.Series<Number, Number>> getElectricityData(){
        ObservableList<XYChart.Series<Number, Number>> tradeoffData =
                FXCollections.observableArrayList();

        HashMap<String,ArrayList<ScatterPlotDataModel>> data = TradeoffDataUtility.getElectricityData();

        if( data.size() != 0 ) {
            for (Map.Entry<String, ArrayList<ScatterPlotDataModel>> entry : data.entrySet()) {
                String key = entry.getKey(); // City name
                ArrayList<ScatterPlotDataModel> value = entry.getValue();

                Series<Number, Number> series = new Series<Number, Number>();
                series.setName( key );

                for( ScatterPlotDataModel model : value )
                {
                    series.getData().add(
                            new XYChart.Data<Number, Number>( model.getElectricityCost(), model.getCost() ) );
                }
                if( DEBUG )
                    System.out.println( "TRADE_OFF ELECTRICITY series " + series.getName() + " data size : " + series.getData().size() );

                tradeoffData.add( series );
            }
        }

        return tradeoffData;
    }

    /**
     * Returns tradeoff data for line chart
     */
    private ObservableList<XYChart.Series<String, Double>> getHeatPumpData( HashMap<String,Map<String, Double>> data ) {
        ObservableList<XYChart.Series<String, Double>> tradeoffData =
                FXCollections.observableArrayList();

        if( data.size() != 0 ) {
            for (Map.Entry<String, Map<String, Double>> entry : data.entrySet()) {
                String key = entry.getKey(); // City name
                Map<String, Double> value = entry.getValue(); // Zone/Tons or Zone/Energy Cost

                Series<String, Double> series = new Series<String, Double>();
                series.setName( key );

                for ( Map.Entry<String, Double> datum : value.entrySet())
                {
                    series.getData().add(
                            new XYChart.Data<String, Double>(datum.getKey(), datum.getValue()));
                }

                if( DEBUG )
                    System.out.println( "TRADE_OFF TONS series " + series.getName() + " data size :" + series.getData().size() );

                tradeoffData.add( series );
            }
        }

        return tradeoffData;
    }

    private ObservableList<XYChart.Series<Number, Number>> getTradeoffData( HashMap<String,ArrayList<ScatterPlotDataModel>> data, boolean cost ){
        ObservableList<XYChart.Series<Number, Number>> tradeoffData =
                FXCollections.observableArrayList();

        if( data.size() != 0 ) {
            for (Map.Entry<String, ArrayList<ScatterPlotDataModel>> entry : data.entrySet()) {
                String key = entry.getKey(); // City name
                ArrayList<ScatterPlotDataModel> value = entry.getValue();

                Series<Number, Number> series = new Series<Number, Number>();
                series.setName( key );

                for( ScatterPlotDataModel model : value )
                {
                    if( !cost )
                    {
                        series.getData().add(
                                new XYChart.Data<Number, Number>( model.getAreaSize(), model.getEnergyConsumption() ) );
                    }
                    else
                        series.getData().add(
                                new XYChart.Data<Number, Number>( model.getAreaSize(), model.getCost() ) );
                }

                if( DEBUG )
                    System.out.println( "TRADE_OFF ENERGY series " + series.getName() + " data size : " + series.getData().size() );

                tradeoffData.add( series );
            }
        }

        return tradeoffData;
    }

    /**
     * Return each zone required tons
     */
    private Map<String, Double> getZoneTons( FloorplanResultModel model ) {
       Map<String, Double> tonsPerZone = new HashMap<String, Double>();

       if( model.getCoolingLoadZone1() != 0 )
           tonsPerZone.put( "Zone 1", model.getCoolingLoadZone1() );

       if( model.getCoolingLoadZone2() != 0 )
           tonsPerZone.put( "Zone 2", model.getCoolingLoadZone2() );

       if( model.getCoolingLoadWhole() != 0 )
           tonsPerZone.put( "Whole", model.getCoolingLoadWhole() );

       return tonsPerZone;
    }

    private ScatterChart<Number,Number> createElectricityScatterChart() {
        NumberAxis xAxis = new NumberAxis(0, 0.55, 0.01);
        NumberAxis yAxis = new NumberAxis(0, 9000, 1000);
        xAxis.setLabel("Electricity (USD)");
        yAxis.setLabel("Initial Cost plus Annual Operating Cost (USD)");
        
        eScatterChart = new ScatterChart<Number,Number>( xAxis,yAxis );
        eScatterChart.setTitle( "Heat Pump / Electricity Chart" );
        
        return eScatterChart;
    }

    private LineChart<String, Double> createLineChart() {
        Axis<String> xAxis = new CategoryAxis();
        xAxis.setLabel("System Size");
        Axis<? extends Number> yAxis = new NumberAxis();
        yAxis.setLabel("Required Tons");

        lineChart = new LineChart<String, Double>( xAxis, (Axis<Double>) yAxis );
        lineChart.setTitle("Heat Pump Chart");
        return lineChart;
    }

    private ScatterChart<Number,Number> createScatterChart() {
        NumberAxis xAxis = new NumberAxis(0, 4000, 500);
        NumberAxis yAxis = new NumberAxis(0, 20000, 2000);
        xAxis.setLabel("Area Size (Sq ft)");
        yAxis.setLabel("Energy Consumption (kWh)");
        
        scatterChart = new ScatterChart<Number,Number>( xAxis,yAxis );
        scatterChart.setTitle( "Energy Consumption Chart" );
        
        return scatterChart;
    }

    private ScatterChart<Number,Number> createCostScatterChart() {
        NumberAxis xAxis = new NumberAxis(0, 4000, 500);
        NumberAxis yAxis = new NumberAxis(0, 50000, 2000);
        xAxis.setLabel("Area Size (Sq ft)");
        yAxis.setLabel("Cost (USD)");
        
        cScatterChart = new ScatterChart<Number,Number>( xAxis,yAxis );
        cScatterChart.setTitle( "Life Cycle Cost Chart" );
        
        return cScatterChart;
    }

    private ScatterChart<Number,Number> createMultiEnergyScatterChart() {
        NumberAxis xAxis = new NumberAxis(0, 4000, 500);
        NumberAxis yAxis = new NumberAxis(0, 20000, 2000);
        xAxis.setLabel("Area Size (Sq ft)");
        yAxis.setLabel("Energy Consumption (kWh)");
        
        multiEnergyScatterChart = new ScatterChart<Number,Number>( xAxis,yAxis );
        multiEnergyScatterChart.setTitle( "Multi-Energy Consumption Chart" );
        
        return multiEnergyScatterChart;
    }

    private ScatterChart<Number,Number> createMultiCostScatterChart() {
        NumberAxis xAxis = new NumberAxis(0, 4000, 500);
        NumberAxis yAxis = new NumberAxis(0, 50000, 2000);
        xAxis.setLabel("Area Size (Sq ft)");
        yAxis.setLabel("Energy Cost (USD)");
        
        multiCostScatterChart = new ScatterChart<Number,Number>( xAxis,yAxis );
        multiCostScatterChart.setTitle( "Multi-Life Cycle Cost Chart" );
        
        return multiCostScatterChart;
    }

    class SharedListSelectionHandler implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
          ListSelectionModel lsm = (ListSelectionModel) e.getSource();
          System.out.println( "TabbedPaneView.SharedListSelectionHandler() Selection changed..." );
          //int firstIndex = e.getFirstIndex();
          //int lastIndex = e.getLastIndex();
          //boolean isAdjusting = e.getValueIsAdjusting();

          if (lsm.isSelectionEmpty()) {
              System.out.println( "TabbedPaneView.SharedListSelectionHandler() Selection is empty..." );
          } else {
            // Find out which indexes are selected.
            int minIndex = lsm.getMinSelectionIndex();
            int maxIndex = lsm.getMaxSelectionIndex();
            for (int i = minIndex; i <= maxIndex; i++) {
              if (lsm.isSelectedIndex(i)) {
                System.out.println("**TabbedPaneView.SharedListSelectionHandler() getting index....");
                  if( tvf.getRowEntry(i) instanceof FloorplanResultModel){
                      FloorplanResultModel model = (FloorplanResultModel)tvf.getRowEntry(i);
                      tvReq = new TableViewAdapterRequirements( model.getRquirements() );
                      tableReq.setModel( tvReq );
                      /*
                      firePropertyChange( EngineeringController.FLOORPLAN_SELECTED_SUMMARY,
                              (FloorplanResultModel) null, model );
                      System.out.println("**TabbedPaneView.SharedListSelectionHandler() after fire property change....");
                      */
                      controller.setFloorplanResultSelected( model );
                      System.out.println("**TabbedPaneView.SharedListSelectionHandler() after set floorplan result select....");
                  }
              }
            }
          }
        }
      }

    private static class DecimalFormatRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        private static final DecimalFormat formatter = new DecimalFormat("#.0");

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            value = formatter.format((Number) value);
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
}
