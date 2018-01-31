package ui.graphstream.org.gs_ui_androidtestFull;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.ui.android_viewer.util.DefaultFragment;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

public class Activity_LayoutTest extends Activity implements ViewerListener {

    private static final int CONTENT_VIEW_ID = 10101010;
    private DefaultFragment fragment ;
    private Graph graph ;
    protected boolean loop = true;

    DorogovtsevMendesGenerator generator ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout frame = new FrameLayout(this);
        frame.setId(CONTENT_VIEW_ID);
        setContentView(frame, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        graph = new MultiGraph("Layout Test");
        graph.setAttribute("ui.antialias");

        generator = new DorogovtsevMendesGenerator();

        display(savedInstanceState, graph, true);
    }

    /**
     * In onStart, the AndroidViewer is already created
     */
    @Override
    protected void onStart() {
        super.onStart();

        ViewerPipe pipe = fragment.getViewer().newViewerPipe();
        pipe.addViewerListener(this);

        graph.setAttribute("ui.stylesheet", styleSheet);

        generator.addSink( graph );
        generator.setDirectedEdges( true, true );
        generator.begin();
        int i = 0;
        while ( i < 100 ) {
            generator.nextEvents();
            i += 1;
        }
        generator.end();

        graph.forEach( n -> n.setAttribute( "ui.label", "truc" ));

        new Thread( () ->  {
            while (loop) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                pipe.pump();
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
                    "fill-mode: gradient-radial;"+
                    "fill-color: white, gray;"+
                    "padding: 60px;"+
            "}"+
            "node {"+
                    "shape: circle;"+
                    "size: 48,48;"+
                    "fill-mode: gradient-vertical;"+
                    "fill-color: white, rgb(200,200,200);"+
                    "stroke-mode: plain;"+
                    "stroke-color: rgba(255,255,0,255);"+
                    "stroke-width: 9px;"+
                    "shadow-mode: plain;"+
                    "shadow-width: 0px;"+
                    "shadow-offset: 15px, -15px;"+
                    "shadow-color: rgba(0,0,0,100);"+
                    "text-visibility-mode: zoom-range;"+
                    "text-visibility: 0, 0.9;"+
            "}"+
            "node:clicked {"+
                    "stroke-mode: plain;"+
                    "stroke-color: red;"+
            "}"+
            "node:selected {"+
                    "stroke-mode: plain;"+
                    "stroke-width: 4px;"+
                    "stroke-color: blue;"+
            "}"+
            "edge {"+
                    "size: 5px;"+
                    "arrow-size: 20px, 15px;"+
                    "shape: cubic-curve;"+
                    "fill-color: rgb(128,128,128);"+
                    "fill-mode: plain;"+
                    "stroke-mode: plain;"+
                    "stroke-color: rgb(80,80,80);"+
                    "stroke-width: 9px;"+
                    "shadow-mode: none;"+
                    "shadow-color: rgba(0,0,0,50);"+
                    "shadow-offset: 15px, -15px;"+
                    "shadow-width: 0px;"+
                    "arrow-shape: diamond;"+
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
}
