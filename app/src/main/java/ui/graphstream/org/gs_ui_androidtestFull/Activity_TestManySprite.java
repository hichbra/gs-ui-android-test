package ui.graphstream.org.gs_ui_androidtestFull;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.graphicGraph.stylesheet.Values;
import org.graphstream.ui.spriteManager.Sprite;
import org.graphstream.ui.spriteManager.SpriteFactory;
import org.graphstream.ui.spriteManager.SpriteManager;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

public class Activity_TestManySprite extends Activity implements ViewerListener {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;
    private Graph graph ;
    protected boolean loop = true;

    /** The set of sprites. */
    SpriteManager sprites = null;

    int NODE_COUNT = 100;
    int SPRITE_COUNT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        graph = new MultiGraph("TestSize");
        graph.setAttribute("ui.antialias");

        Log.e("Debug", NODE_COUNT+" nodes, "+SPRITE_COUNT+" sprites%n" );

        display(savedInstanceState, graph, true);
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

        DorogovtsevMendesGenerator gen    = new DorogovtsevMendesGenerator();

        gen.addSink( graph );
//				gen.setDirectedEdges( true, true )
        gen.begin();
        int i = 0;
        while ( i < NODE_COUNT ) {
            gen.nextEvents(); i += 1 ;
        }
        gen.end();

        sleep( 1000 );
        addSprites();

        new Thread( () ->  {
            while( loop ) {
                pipe.pump();
                moveSprites();
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

    private String styleSheet =
            "graph {"+
                    "fill-mode: plain;"+
                    "fill-color: white, gray;"+
                    "padding: 60px;"+
                    "}"+
                    "node {"+
                    "shape: circle;"+
                    "size: 10px;"+
                    "fill-mode: plain;"+
                    "fill-color: grey;"+
                    "stroke-mode: none;"+
                    "text-visibility-mode: zoom-range;"+
                    "text-visibility: 0, 0.9;"+
                    "}"+
                    "edge {"+
                    "size: 1px;"+
                    "shape: line;"+
                    "fill-color: grey;"+
                    "fill-mode: plain;"+
                    "stroke-mode: none;"+
                    "}"+
                    "sprite {"+
                    "shape: circle;"+
                    "size: 6px;"+
                    "fill-mode: plain;"+
                    "fill-color: red;"+
                    "stroke-mode: none;"+
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

    private void moveSprites() {
        sprites.forEach( s -> ((TestSprite)s).move() );
    }


    private void addSprites() {
        sprites = new SpriteManager( graph );
        sprites.setSpriteFactory( new TestSpriteFactory() );

        for( int i = 0 ; i < SPRITE_COUNT ; i++ ) {
            sprites.addSprite( i+"" );
        }

        sprites.forEach ( s ->s.attachToEdge( randomEdge( graph ).getId() ));
    }

    private Edge randomEdge(Graph graph) {
        int min = 0 ;
        int max = (int) graph.edges().count();

        int rand = (int) (min + (Math.random() * (max - min)));

        return graph.getEdge(rand);
    }

    class TestSpriteFactory extends SpriteFactory {
        @Override
        public Sprite newSprite(String identifier, SpriteManager manager, Values position) {
            return new TestSprite(identifier, manager);
        }
    }

    class TestSprite extends Sprite {
        double dir = 0.01f;

        public TestSprite( String identifier, SpriteManager manager ) {
            super( identifier, manager );
        }

        public void move() {
            double p = getX();

            p += dir;

            if( p < 0 || p > 1 )
                chooseNextEdge();
            else
                setPosition( p );
        }

        public void chooseNextEdge() {
            Edge edge = (Edge) getAttachment();
            Node node = edge.getSourceNode();
            if( dir > 0 )
                node = edge.getTargetNode() ;


            Edge next = randomEdge( node );
            double pos = 0;

            if( node == next.getSourceNode() ) {
                dir =  0.01f;
                pos = 0;
            }
            else {
                dir = -0.01f;
                pos = 1;
            }

            attachToEdge( next.getId() );
            setPosition( pos );
        }

        private Edge randomEdge(Node node) {
            int min = 0 ;
            int max = (int) node.edges().count();

            int rand = (int) (min + (Math.random() * (max - min)));

            return node.getEdge(rand);
        }
    }
}
