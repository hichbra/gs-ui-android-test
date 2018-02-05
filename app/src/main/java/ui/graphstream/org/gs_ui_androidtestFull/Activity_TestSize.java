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
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestSize extends Activity implements ViewerListener {

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

        graph = new MultiGraph("TestSize");
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

        Edge AB = graph.addEdge( "AB", "A", "B", true );
        Edge BC = graph.addEdge( "BC", "B", "C", true );
        Edge CD = graph.addEdge( "CD", "C", "D", true );
        Edge DA = graph.addEdge( "DA", "D", "A", true );
        Edge BB = graph.addEdge( "BB", "B", "B", true );

        A.setAttribute("xyz", new double[] { 0, 1, 0 });
        B.setAttribute("xyz", new double[] { 1, 1, 0 });
        C.setAttribute("xyz", new double[] { 1, 0, 0 });
        D.setAttribute("xyz", new double[] { 0, 0, 0 });

        AB.setAttribute("ui.label", "AB");
        BC.setAttribute("ui.label", "A Long label ...");
        CD.setAttribute("ui.label", "CD");
        BB.setAttribute("ui.label", "BB");

        SpriteManager sm = new SpriteManager( graph );
        Sprite S1 = sm.addSprite("S1");

        S1.attachToNode( "C" );
        S1.setPosition( StyleConstants.Units.PX, 40, 45, 0 );

        new Thread( () ->  {
            double size = 100f;
            double sizeInc = 1f;

            while( loop ) {
                pipe.pump();
                sleep( 40 );
                A.setAttribute( "ui.size", size );
//			A.setAttribute( "ui.size", "%spx".format( size ) )
                BC.setAttribute( "ui.size", size );
                S1.setAttribute( "ui.size", size );

                size += sizeInc;

                if( size > 50 ) {
                    sizeInc = -1f; size = 50f;
                } else if( size < 20 ) {
                    sizeInc = 1f; size = 20f;
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

    private String styleSheet =
            "graph {"+
                    "	canvas-color: white;"+
                    "		fill-mode: gradient-radial;"+
                    "		fill-color: white, #EEEEEE;"+
                    "		padding: 60px;"+
                    "	}"+
                    "node {"+
                    "	shape: circle;"+
                    "	size: 60px;"+
                    "	fill-mode: plain;"+
                    "	fill-color: #CCC;"+
                    "	stroke-mode: plain;"+
                    "	stroke-color: black;"+
                    "	stroke-width: 3px;"+
                    "}"+
                    "node:clicked {"+
                    "	stroke-mode: plain;"+
                    "	stroke-color: red;"+
                    "}"+
                    "node:selected {"+
                    "	stroke-mode: plain;"+
                    "	stroke-color: blue;"+
                    "}"+
                    "node#A {"+
                    "	size-mode: dyn-size;"+
                    "	size: 30px;"+
                    "}"+
                    "node#D {"+
                    "	shape: box;"+
                    "	size-mode: fit;"+
                    "	padding: 5px;"+
                    "}"+
                    "edge {"+
                    "	shape: blob;"+
                    "	size: 3px;"+
                    "	fill-color: grey;"+
                    "	fill-mode: plain;"+
                    "	arrow-shape: arrow;"+
                    "	arrow-size: 10px, 3px;"+
                    "}"+
                    "edge#BC {"+
                    "	size-mode: dyn-size;"+
                    "	size: 3px;"+
                    "}"+
                    "sprite {"+
                    "	shape: circle;"+
                    "	fill-color: #FCC;"+
                    "	stroke-mode: plain;"+
                    "	stroke-color: black;"+
                    "}"+
                    "sprite:selected {"+
                    "	stroke-color: red;"+
                    "}"+
                    "sprite#S1 {"+
                    "	size-mode: dyn-size;"+
                    "}";

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
