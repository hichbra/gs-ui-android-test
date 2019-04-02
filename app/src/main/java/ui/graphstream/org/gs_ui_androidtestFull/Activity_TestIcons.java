package ui.graphstream.org.gs_ui_androidtestFull;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import java.util.ArrayList;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestIcons extends FragmentActivity implements ViewerListener {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;
    private Graph graph ;
    protected boolean loop = true;
    private boolean direction = true ;

    public static ArrayList<Integer> iconAnim = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        graph = new MultiGraph("TestIcon");
        graph.setAttribute("ui.antialias");

        iconAnim.add(R.drawable.icon1);
        iconAnim.add(R.drawable.icon2);
        iconAnim.add(R.drawable.icon3);
        iconAnim.add(R.drawable.icon4);
        iconAnim.add(R.drawable.icon5);
        iconAnim.add(R.drawable.icon6);
        iconAnim.add(R.drawable.icon7);
        iconAnim.add(R.drawable.icon8);
        iconAnim.add(R.drawable.icon9);

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

        Node A    = graph.addNode( "A" );
        Node B    = graph.addNode( "B" );
        Node C    = graph.addNode( "C" );
        Node D    = graph.addNode( "D" );

        graph.addEdge( "AB", "A", "B" );
        graph.addEdge( "BC", "B", "C" );
        graph.addEdge( "CD", "C", "D" );
        graph.addEdge( "DA", "D", "A" );

        A.setAttribute("xyz", new double[] { 0.0, 1.0, 0 });
        B.setAttribute("xyz", new double[] { 3.2, 1.5, 0 });
        C.setAttribute("xyz", new double[] { 0.2, 0.0, 0 });
        D.setAttribute("xyz", new double[] { 3.0,-0.5, 0 });

        A.setAttribute("label", "Topic1");
        B.setAttribute("label", "Topic2");
        C.setAttribute("label", "Topic3");
        D.setAttribute("label", "Topic4");

        A.setAttribute("ui.icon", iconAnim.get(0).toString());

        new Thread( () -> {
            int i=0;
            while( loop ) {
                pipe.pump();
                sleep( 60 );

                if( i >= 9 )
                    i = 0;
                else if (i < 0)
                    i = 8 ;

                A.setAttribute("ui.icon", iconAnim.get(i).toString());

                if (direction)	i++ ;
                else			i--;

            }
            System.exit(0);
        }).start();
    }

    public void display(Bundle savedInstanceState, Graph graph, boolean autoLayout) {
        if (savedInstanceState == null) {
            FragmentManager fm = getSupportFragmentManager();

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
                    "canvas-color: white;"+
                    "fill-mode: gradient-radial;"+
                    "fill-color: white, #EEEEEE;"+
                    "padding: 60px;"+
                    "}"+
                    "node {"+
                    "shape: freeplane;"+
                    "size: 50px;"+
                    "size-mode: fit;"+
                    "fill-mode: none;"+
                    "stroke-mode: plain;"+
                    "stroke-color: grey;"+
                    "stroke-width: 3px;"+
                    "padding: 15px, 10px;"+
                    "shadow-mode: none;"+
                    "icon-mode: at-left;"+
                    "text-size: 30px;"+
                    "text-style: normal;"+
                    "text-font: 'Droid Sans';"+
                    "icon: dyn-icon;"+
                    "}"+
                    "node:clicked {"+
                    "stroke-mode: plain;"+
                    "stroke-color: red;"+
                    "}"+
                    "node:selected {"+
                    "stroke-mode: plain;"+
                    "stroke-color: blue;"+
                    "}"+
                    "edge {"+
                    "shape: freeplane;"+
                    "size: 5px;"+
                    "fill-color: grey;"+
                    "fill-mode: plain;"+
                    "shadow-mode: none;"+
                    "shadow-color: rgba(0,0,0,100);"+
                    "shadow-offset: 3px, -3px;"+
                    "shadow-width: 0px;"+
                    "arrow-shape: arrow;"+
                    "arrow-size: 20px, 15px;"+
                    "}";

    public void buttonPushed( String id ) {
        System.out.println(id);
        if( id.equals("quit") )
            loop = false;
        else if( id.equals("A") ) {
            Log.e("Debug", "Button A pushed" );
            direction = !direction;
        }
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
