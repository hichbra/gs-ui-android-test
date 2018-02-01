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
import org.graphstream.ui.geom.Point3;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import ui.graphstream.org.gs_ui_androidtest.R;

public class Activity_TestSprites extends Activity implements ViewerListener {

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
        Node E = graph.addNode( "E" );

        C.setAttribute("ui.points",
                new Point3(-0.05f, -0.05f, 0f),
                new Point3( 0f,    -0.02f, 0f),
                new Point3( 0.05f, -0.05f, 0f),
                new Point3( 0f,     0.05f, 0f));

        graph.addEdge( "AB1", "A", "B", true );
        graph.addEdge( "AB2", "B", "A", true );
        graph.addEdge( "BC", "B", "C" );
        graph.addEdge( "CD", "C", "D", true );
        graph.addEdge( "DA", "D", "A" );
        graph.addEdge( "DE", "D", "E", true );
        graph.addEdge( "EB", "E", "B", true );
        graph.addEdge( "BB", "B", "B", true );

        graph.getEdge("CD").setAttribute("ui.points",
                new Point3(1, 0, 0),
                new Point3(0.6f, 0.1f, 0f),
                new Point3(0.3f,-0.1f, 0f),
                new Point3(0, 0, 0));


        A.setAttribute("xyz",new double[] { 0, 1, 0 });
        B.setAttribute("xyz",new double[] { 1.5, 1, 0 });
        C.setAttribute("xyz",new double[] { 1, 0, 0 });
        D.setAttribute("xyz",new double[] { 0, 0, 0 });
        E.setAttribute("xyz",new double[] { 0.4, 0.6, 0 });

        A.setAttribute("label", "A");
        B.setAttribute("label", "B");
        C.setAttribute("label", "C");
        D.setAttribute("label", "D");
        E.setAttribute("label", "E");

        SpriteManager sman = new SpriteManager( graph );

        MovingEdgeSprite s1 = sman.addSprite("S1", MovingEdgeSprite.class);
        MovingEdgeSprite s2 = sman.addSprite("S2", MovingEdgeSprite.class);
        MovingEdgeSprite s3 = sman.addSprite("S3", MovingEdgeSprite.class);
        MovingEdgeSprite s4 = sman.addSprite("S4", MovingEdgeSprite.class);
        DataSprite s5 = sman.addSprite("S5", DataSprite.class);
        MovingEdgeSprite s6 = sman.addSprite("S6", MovingEdgeSprite.class);
        MovingEdgeSprite s7 = sman.addSprite("S7", MovingEdgeSprite.class);
        MovingEdgeSprite s8 = sman.addSprite("S8", MovingEdgeSprite.class);

        s1.attachToEdge("AB1");
        s2.attachToEdge("CD");
        s3.attachToEdge("DA");
        s4.attachToEdge("EB");
        s5.attachToNode("A");
        s6.attachToNode("D");
        s7.attachToEdge("AB2");
        s8.attachToEdge("EB");

        s2.setOffsetPx(20);
        s3.setOffsetPx(15);
        s4.setOffsetPx(4);
        s5.setPosition(StyleConstants.Units.PX, 0, 30, 0);
        s5.setData(new float[]{0.3f, 0.5f, 0.2f});
        //s6.setOffsetPx(20);
        s8.setPosition(0.5f, 0.5f, 0f);

        s1.setAttribute("ui.label", "FooBar1");
        s2.setAttribute("ui.label", "FooBar2");
        s4.setAttribute("ui.label", "FooBar4");
        s7.setAttribute("ui.label", "FooBar7");

        s8.setAttribute("ui.points",
                new Point3(-0.02f, -0.02f, 0f),
                new Point3( 0f,    -0.01f, 0f),
                new Point3( 0.02f, -0.02f, 0f),
                new Point3( 0f,     0.02f, 0f));

        E.setAttribute("ui.pie-values", 0.2f, 0.3f, 0.4f, 0.1f);

        new Thread( () ->  {
            while( loop ) {
                pipe.pump();
                s1.move();
                s2.move();
                s3.move();
                s4.move();
                s6.move();
                s7.move();
                s8.move();
                sleep( 4 );
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

    private String styleSheet = "graph {"+
            "	canvas-color: white;"+
            "	fill-mode: gradient-radial;"+
            "	fill-color: white, #EEEEEE;"+
            "	padding: 60px;"+
            "}"+
            "node {"+
            "	shape: circle;"+
            "	size: 60px;"+
            "	fill-mode: plain;"+
            "	fill-color: white;"+
            "	stroke-mode: plain;"+
            "	stroke-color: grey;"+
            "	stroke-width: 5px;"+
            "	text-size: 20px;"+
            "}"+
            "node:clicked {"+
            "	stroke-mode: plain;"+
            "	stroke-color: red;"+
            "}"+
            "node:selected {"+
            "	stroke-mode: plain;"+
            "	stroke-color: blue;"+
            "}"+
            "node#C {"+
            "	shape: polygon;"+
            "}"+
            "node#E {"+
            "	shape: pie-chart;"+
            "	fill-color: red, green, blue, yellow, magenta;"+
            "	size: 150px;"+
            "	stroke-mode: plain;"+
            "	stroke-width: 3px;"+
            "	stroke-color: black;"+
            "}"+
            "edge {"+
            "	shape: line;"+
            "	size: 3px;"+
            "	fill-color: grey;"+
            "	fill-mode: plain;"+
            "	arrow-shape: arrow;"+
            "	arrow-size: 15px, 6px;"+
            "}"+
            "edge#BC {"+
            "	shape: cubic-curve;"+
            "}"+
            "edge#EB {"+
            "	shape: cubic-curve;"+
            "}"+
            "edge#CD {"+
            "	shape: polyline;"+
            "}"+
            "sprite {"+
            "	shape: circle;"+
            "	fill-color: #944;"+
            "	z-index: -1;"+
            "}"+
            "sprite#S2 {"+
            "	shape: arrow;"+
            "	sprite-orientation: projection;"+
            "	size: 80px, 40px;"+
            "	fill-color: #449;"+
            "}"+
            "sprite#S3 {"+
            "	shape: arrow;"+
            "	sprite-orientation: to;"+
            "	size: 20px, 10px;"+
            "	fill-color: #494;"+
            "}"+
            "sprite#S4 {"+
            "	shape: flow;"+
            "	size: 16px;"+
            "	fill-color: #99A9;"+
            "	sprite-orientation: to;"+
            "}"+
            "sprite#S5 {"+
            "	shape: pie-chart;"+
            "	size: 160px;"+
            "	fill-color: cyan, magenta, yellow, red, green, blue;"+
            "	stroke-mode: plain;"+
            "	stroke-width: 1px;"+
            "	stroke-color: black;"+
            "}"+
            "sprite#S6 {"+
            "	shape: circle;"+
            "	size: 120px;"+
            "	fill-color: #55C;"+
            "}"+
            "sprite#S7 {"+
            "	shape: box;"+
            "	size: 150px, 70px;"+
            "	sprite-orientation: projection;"+
            "	fill-mode: image-scaled;"+
            "	fill-image: url('"+URL_IMAGE+"');"+
            "	fill-color: red;"+
            "	stroke-mode: none;"+
            "}"+
            "sprite#S8 {"+
            "	shape: polygon;"+
            "	fill-color: yellow;"+
            "	stroke-mode: plain;"+
            "	stroke-width: 5px;"+
            "	stroke-color: red;"+
            "	shadow-mode: plain;"+
            "	shadow-color: #707070;"+
            "	shadow-offset: 3px, -3px;"+
            "}";

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
