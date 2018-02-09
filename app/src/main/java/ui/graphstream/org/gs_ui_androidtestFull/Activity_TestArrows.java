package ui.graphstream.org.gs_ui_androidtestFull;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestArrows extends Activity implements ViewerListener {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;
    private Graph graph ;
    protected boolean loop = true;

    public static final int URL_IMAGE = R.drawable.icon ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        graph = new MultiGraph("Test Arrows");
        graph.setAttribute("ui.antialias");

        display(savedInstanceState, graph, false);
    }

    /**
     * In onStart, the AndroidViewer is already created
     */
    @Override
    protected void onStart() {
        super.onStart();

        ViewerPipe pipe = fragment.getViewer().newViewerPipe();

        pipe.addAttributeSink( graph );
        pipe.addViewerListener( this );
        pipe.pump();

        graph.setAttribute( "ui.stylesheet", styleSheet );
        graph.setAttribute( "ui.antialias" );

        Node A = graph.addNode( "A" );
        Node B = graph.addNode( "B" );
        Node C = graph.addNode( "C" );
        Node D = graph.addNode( "D" );
        Node E = graph.addNode( "E" );
        Node F = graph.addNode( "F" );

        Edge AB = graph.addEdge( "AB", "A", "B", true );
        Edge BC = graph.addEdge( "BC", "B", "C", true );
        Edge CD = graph.addEdge( "CD", "C", "D", true );
        Edge DA = graph.addEdge( "DA", "D", "A", true );
        Edge BB = graph.addEdge( "BB", "B", "B", true );
        Edge DE = graph.addEdge( "DE", "D", "E", true );
        Edge DF = graph.addEdge( "DF", "D", "F", true );
        Edge CF = graph.addEdge( "CF", "C", "F", true );

        A.setAttribute("xyz", new double[] { 0, 1, 0 });
        B.setAttribute("xyz", new double[] { 1, 0.9, 0 });
        C.setAttribute("xyz", new double[] { 0.8, 0.3, 0 });
        D.setAttribute("xyz", new double[] { 0, 0, 0 });
        E.setAttribute("xyz", new double[] { 0.5, 0.5, 0 });
        F.setAttribute("xyz", new double[] { 0.5, 0.25, 0 });

        A.setAttribute("label", "A");
        B.setAttribute("label", "Long label ...");
        C.setAttribute("label", "C");
        D.setAttribute("label", "A long label ...");
        E.setAttribute("label", "Another very long label");
        F.setAttribute("label", "F");

        new Thread( () ->  {
            double size = 100f;
            double sizeInc = 1f;

            while( loop ) {
                pipe.pump();
                sleep( 40 );
                A.setAttribute( "ui.size", size );

                size += sizeInc;

                if( size > 150 ) {
                    sizeInc = -1f; size = 150f;
                }
                else if( size < 100 ) {
                    sizeInc = 1f; size = 100f;
                }
            }

            System.exit(0);
        }).start();
    }

    public void display(Bundle savedInstanceState, Graph graph, boolean autoLayout) {
        if (savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();

            // find fragment or create him
            fragment = (DefaultFragment) fm.findFragmentByTag("fragment_tag");
            if (null == fragment) {
                fragment = new DefaultFragment();
                fragment.init(graph, autoLayout);
            }

            // Add the fragment in the layout and commit
            FragmentTransaction ft = fm.beginTransaction() ;
            ft.add(CONTENT_VIEW_ID, fragment).commit();
        }
    }

    private String styleSheet = ""
            + "graph {"
            + "	canvas-color: white;  "
            + "	fill-mode: gradient-radial; "
            + "	fill-color: white, #EEEEEE; "
            + "	padding: 60px; "
            + "}"
            + ""
            + "node {"
            + " shape: circle;"
            + " fill-mode: plain;"
            + " size: 60px;"
            + " fill-color: #CCCC;"
            + " stroke-mode: plain; "
            + " stroke-color: black; "
            + " stroke-width: 9px; "
            + " text-size: 15px;"
            + "} "
            + ""
            + "node:clicked { "
            + "	stroke-mode: plain;"
            + "	stroke-color: red;"
            + "}"
            + ""
            + "node:selected { "
            + "	stroke-mode: plain; "
            + "	stroke-color: blue; "
            + "}"
            + ""
            + "node#A { "
            + "	shape: rounded-box; "
            + "	size-mode: dyn-size;"
            + "text-size: 50px; } "
            + ""
            + "node#B { "
            + "	shape: circle;"
            + " size-mode: fit; "
            + " size: 100,100;"
            + "	padding: 35px; "
            + "} "
            + ""
            + "node#C { 	"
            + " shape: box; 	"
            + " size: 150,150;"
            + "	text-size: 50px; "
            + "} "
            + ""
            + "node#D { "
            + " shape: box; "
            + " size-mode: fit; "
            + " size: 100,100;"
            + " padding: 20px;"
            + "}"
            + ""
            + "node#E {"
            + "	shape: circle; "
            + "	size-mode: fit;"
            + "	size: 70px, 50px;"
            + " size: 100,100;"
            + "	padding: 30px;"
            + " }"
            + ""
            + "edge { 	shape: line; size: 5px; fill-color: grey; 	fill-mode: plain; 	arrow-shape: arrow; arrow-size: 20px, 15px; } "
            + "edge#BC { 	shape: cubic-curve; }"
            + "edge#AB { 	shape: cubic-curve; }"
            + "edge#CF { 	shape: cubic-curve; arrow-shape: image; arrow-image: url('"+URL_IMAGE+"'); } "
            + "edge#DF { 	arrow-shape: image; arrow-image: url('"+URL_IMAGE+"'); } " ;

    public void buttonPushed(String id) {
    }

    public void buttonReleased(String id) {
    }

    public void mouseOver(String id) {
    }

    public void mouseLeft(String id) {
    }

    public void viewClosed(String viewName) {
        loop = false;
    }

    protected void sleep( long ms ) {
        try {
            Thread.sleep( ms );
        } catch (InterruptedException e) { e.printStackTrace(); }
    }
}
