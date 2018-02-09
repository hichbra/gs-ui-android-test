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
import org.graphstream.ui.graphicGraph.stylesheet.Values;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteFactory;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestStars extends Activity implements ViewerListener {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;
    private Graph graph ;
    protected boolean loop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        graph = new MultiGraph("Test Stars");
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

        graph.addEdge( "AB", "A", "B" );
        graph.addEdge( "BC", "B", "C" );
        graph.addEdge( "CD", "C", "D" );
        graph.addEdge( "DA", "D", "A" );
        graph.addEdge( "DE", "D", "E" );
        graph.addEdge( "EB", "E", "B" );

        A.setAttribute("xyz", new double[] { 0, 1, 0 });
        B.setAttribute("xyz", new double[] { 1.5, 1, 0 });
        C.setAttribute("xyz", new double[] { 1, 0, 0 });
        D.setAttribute("xyz", new double[] { 0, 0, 0 });
        E.setAttribute("xyz", new double[] { 0.4, 0.6, 0 });

        SpriteManager sman = new SpriteManager( graph );

        sman.setSpriteFactory( new MySpriteFactory() );

        MySprite s1 = (MySprite)sman.addSprite( "S1" );
        MySprite s2 = (MySprite)sman.addSprite( "S2" );
        MySprite s3 = (MySprite)sman.addSprite( "S3" );

        s1.attachToEdge( "AB" );
        s2.attachToEdge( "CD" );
        s3.attachToEdge( "DA" );

        new Thread( () ->  {
            while( loop ) {
                pipe.pump();
                s1.move();
                s2.move();
                s3.move();
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
                    "	canvas-color: black;"+
                    "	fill-mode: gradient-vertical;"+
                    "	fill-color: black, #004;"+
                    "	padding: 60px;"+
                    "}"+
                    "node {"+
                    "	shape: circle;"+
                    "	size: 60px;"+
                    "	fill-mode: gradient-radial;"+
                    "	fill-color: #FFFA, #FFF0;"+
                    "	stroke-mode: none;"+
                    "	shadow-mode: gradient-radial;"+
                    "	shadow-color: #FFF9, #FFF0;"+
                    "	shadow-width: 24px;"+
                    "	shadow-offset: 0px, 0px;"+
                    "}"+
                    "node:clicked {"+
                    "	fill-color: #F00A, #F000;"+
                    "}"+
                    "node:selected {"+
                    "	fill-color: #00FA, #00F0;"+
                    "}"+
                    "edge {"+
                    "	shape: line;"+
                    "	size: 3px;"+
                    "	fill-color: #FFF3;"+
                    "	fill-mode: plain;"+
                    "	arrow-shape: none;"+
                    "}"+
                    "sprite {"+
                    "	shape: circle;"+
                    "   size: 170px;"+
                    "	fill-mode: gradient-radial;"+
                    "	fill-color: white, #FFF0;"+
                    "}";

    class MySpriteFactory extends SpriteFactory {

        @Override
        public Sprite newSprite(String identifier, SpriteManager manager, Values position) {
            if( position != null )
                return new MySprite( identifier, manager, position );
            else
                return new MySprite( identifier, manager );
        }
    }

    class MySprite extends Sprite {
        public MySprite(String id, SpriteManager manager, Values pos) {
            super(id, manager, pos);
        }

        public MySprite(String id, SpriteManager manager) {
            this(id, manager, new Values(StyleConstants.Units.GU, 0, 0, 0));
        }

        double SPEED = 0.005f;
        double speed = SPEED;
        double off = 0f;
        StyleConstants.Units units = StyleConstants.Units.PX;

        public void setOffsetPx( float offset ) { off = offset; units = StyleConstants.Units.PX ;}

        public void move() {
            double p = getX();

            p += speed;

            if( p < 0 || p > 1 ) {
                Edge edge = (Edge) getAttachment();

                if( edge != null ) {
                    Node node = edge.getSourceNode();
                    if( p > 1 )
                        node = edge.getTargetNode();

                    Edge other = randomOutEdge( node );

                    if( node.getOutDegree() > 1 ) {
                        while( other == edge )
                            other = randomOutEdge( node );
                    }

                    attachToEdge( other.getId() );
                    if( node == other.getSourceNode() ) {
                        setPosition( units, 0, off, 0 );
                        speed = SPEED;
                    } else {
                        setPosition( units, 1, off, 0 );
                        speed = -SPEED;
                    }
                }
            }
            else {
                setPosition( units, p, off, 0 );
            }
        }

        public Edge randomOutEdge(Node node) {
            int min = 0 ;
            int max = (int) node.leavingEdges().count();

            int rand = (int) (min + (Math.random() * (max - min)));

            return node.getLeavingEdge(rand);
        }
    }

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
