package ui.graphstream.org.gs_ui_androidtestFull;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestFreePlane extends Activity implements ViewerListener {

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
        graph.setAttribute( "ui.quality" );

        Node root = graph.addNode( "root" );
        Node A    = graph.addNode( "A" );
        Node B    = graph.addNode( "B" );
        Node C    = graph.addNode( "C" );
        Node D    = graph.addNode( "D" );
        Node E    = graph.addNode( "E" );
        Node F    = graph.addNode( "F" );
        Node G    = graph.addNode( "G" );
        Node H    = graph.addNode( "H" );

        graph.addEdge( "rA", "root", "A" );
        graph.addEdge( "rB", "root", "B" );
        graph.addEdge( "rC", "root", "C" );
        graph.addEdge( "rD", "root", "D" );
        graph.addEdge( "rE", "root", "E" );
        graph.addEdge( "AF", "A", "F" );
        graph.addEdge( "CG", "C", "G" );
        graph.addEdge( "DH", "D", "H" );

        root.setAttribute("xyz", new double[] { 0, 0, 0});
        A.setAttribute("xyz"   , new double[] {1, 1, 0 });
        B.setAttribute("xyz"   , new double[] { 1, 0, 0 });
        C.setAttribute("xyz"   , new double[] {-1, 1, 0 });
        D.setAttribute("xyz"   , new double[] {-1, 0, 0 });
        E.setAttribute("xyz"   , new double[] {-1,-1, 0 });
        F.setAttribute("xyz"   , new double[] { 2, 1.2, 0 });
        G.setAttribute("xyz"   , new double[] {-2, 1.2, 0 });
        H.setAttribute("xyz"   , new double[] {-2,-.5, 0 });

        root.setAttribute("label", "Idea");
        A.setAttribute("label", "Topic1");
        B.setAttribute("label", "Topic2");
        C.setAttribute("label", "Topic3");
        D.setAttribute("label", "Topic4");
        E.setAttribute("label", "Topic5");
        F.setAttribute("label", "SubTopic1");
        G.setAttribute("label", "SubTopic2");
        H.setAttribute("label", "Very Long Sub Topic ...");

        new Thread( () ->  {
            while( loop ) {
                pipe.pump();
                sleep( 10 );
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
                DefaultFragment.autoLayout = autoLayout ;
                fragment = new DefaultFragment();
                fragment.init(graph);
            }

            // Add the fragment in the layout and commit
            FragmentTransaction ft = fm.beginTransaction() ;
            ft.add(CONTENT_VIEW_ID, fragment).commit();
        }
    }

    private String styleSheet = ""
            + "graph {"
            + "	canvas-color: white; "
            + "	fill-mode: gradient-radial; "
            + "	fill-color: white, #EEEEEE; "
            + "	padding: 60px; "
            + "}"
            + ""
            + "node {"
            + "	shape: freeplane;"
            + "	size: 50px;"
            + "	size-mode: fit;"
            + "	fill-mode: none;"
            + "	stroke-mode: plain;"
            + "	stroke-color: grey;"
            + "	stroke-width: 5px;"
            + "	padding: 5px, 1px;"
            + "	shadow-mode: none;"
            + "	icon-mode: at-left;"
            + "	text-style: normal;"
            + "	text-font: 'Droid Sans';"
            + "	icon: url('"+URL_IMAGE+"');"
            + "}"
            + ""
            + "node:clicked {"
            + "	stroke-mode: plain;"
            + "	stroke-color: red;"
            + "}"
            + ""
            + "node:selected {"
            + "	stroke-mode: plain;"
            + "	stroke-color: blue;"
            + "}"
            + ""
            + "edge {"
            + "	shape: freeplane;"
            + "	size: 5px;"
            + "	fill-color: grey;"
            + "	fill-mode: plain;"
            + "	shadow-mode: none;"
            + "	shadow-color: rgba(0,0,0,100);"
            + "	shadow-offset: 3px, -3px;"
            + "	shadow-width: 0px;"
            + "	arrow-shape: arrow;"
            + "	arrow-size: 20px, 6px;"
            + "}";

    public void buttonPushed( String id ) {
        Log.e("Debug", id);
        if( id.equals("quit") )
            loop = false;
        else if( id.equals("A") )
            Log.e("Debug","Button A pushed" );
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
